package ram.munindia;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import ram.munindia.validations.Validations;

public class FgLocation extends AppCompatActivity implements View.OnClickListener {

    ImageView fglocationbarcodescan,fgcartonbarcodescan,myimage_back,done_img;
    static EditText fglocationbarcodenumber,fgcartonbarcodenumber;

    //for checking last value while restarting
   static String firstlastdata="null",secondlastdate="null";

    String locationid="null",locationsallowable="null";

    OkHttpClient client;
    SharedPreferences ss;
    ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fg_location);
        pd = new ProgressDialog(FgLocation.this);
        pd.setMessage("Checking...");
        pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pd.setIndeterminate(true);
        pd.setCancelable(false);



        fglocationbarcodescan=findViewById(R.id.fglocationbarcodescan);
        fgcartonbarcodescan=findViewById(R.id.fgcartonbarcodescan);
        myimage_back=findViewById(R.id.myimage_back);
        done_img=findViewById(R.id.done_img);

        fglocationbarcodescan.setOnClickListener(this);
        fgcartonbarcodescan.setOnClickListener(this);
        myimage_back.setOnClickListener(this);
        done_img.setOnClickListener(this);

        fglocationbarcodenumber=findViewById(R.id.fglocationbarcodenumber);
        fgcartonbarcodenumber=findViewById(R.id.fgcartonbarcodenumber);


        fglocationbarcodenumber.setLongClickable(false);
        fgcartonbarcodenumber.setLongClickable(false);

        client = new OkHttpClient();
        ss = getSharedPreferences("Login", MODE_PRIVATE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fglocationbarcodescan:
                if (Validations.hasActiveInternetConnection(FgLocation.this)) {
                    Intent barcodescanner = new Intent(FgLocation.this, Barcodescanner.class);
                    barcodescanner.putExtra("click","fglocationbarcodescan");
                    startActivity(barcodescanner);
                } else {
                    Toast.makeText(FgLocation.this,"please check internet connection",Toast.LENGTH_SHORT).show();
                }

                break;
            case R.id.fgcartonbarcodescan:

                if (Validations.hasActiveInternetConnection(FgLocation.this)) {
                    Intent barcodescanner = new Intent(FgLocation.this, Barcodescanner.class);
                    barcodescanner.putExtra("click","fgcartonbarcodescan");
                    startActivity(barcodescanner);
                } else {
                    Toast.makeText(FgLocation.this,"please check internet connection",Toast.LENGTH_SHORT).show();
                }

                break;

            case R.id.done_img:
                if(!TextUtils.isEmpty(fglocationbarcodenumber.getText().toString().trim())) {
                    if (!TextUtils.isEmpty(fgcartonbarcodenumber.getText().toString().trim())) {
                        Toast.makeText(FgLocation.this,"done",Toast.LENGTH_SHORT).show();
                        done();
                    }else{
                        Login.alert("Please Scan Carton Barcode",this);
                    }
                }else{
                    Login.alert("Please Scan Location Barcode",this);
                }

//                done();
                break;

            case R.id.myimage_back:

                finish();
                break;

        }
    }

           @Override
        protected void onRestart() {
            super.onRestart();

        if(!TextUtils.isEmpty(fglocationbarcodenumber.getText().toString().trim()) || TextUtils.isEmpty(fgcartonbarcodenumber.getText().toString().trim())){

            if(!TextUtils.isEmpty(fglocationbarcodenumber.getText().toString().trim())){

                String first=fglocationbarcodenumber.getText().toString().trim();


                if(!first.equals(firstlastdata)){
                    pd.show();
                    locationScan();

                }

            }

            if(!TextUtils.isEmpty(fgcartonbarcodenumber.getText().toString().trim())){

                String second=fgcartonbarcodenumber.getText().toString().trim();

                if(!second.equals(secondlastdate)) {
                    pd.show();
                    cartonScan();

                }
            }
        }
    }

    private void locationScan() {

        // fstretrn=false;
        RequestBody formBody = new FormBody.Builder()
                .add("location", fglocationbarcodenumber.getText().toString().trim())
                .build();

        Request request = new Request.Builder()
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + ss.getString("access_token", ""))
                .url(Login.web+"index.php?r=restapi/api/location-avail")
                .post(formBody)
                .build();


        client.newCall(request).enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {
                Log.d("result", e.getMessage());
                e.printStackTrace();
                pd.dismiss();
                //fstretrn=false;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        fglocationbarcodenumber.setText("");
                        fgcartonbarcodenumber.setText("");

                        // Stuff that updates the UI
                        Toast.makeText(FgLocation.this, "Please try again server busy at this moment", Toast.LENGTH_LONG).show();

                    }

                });
            }

            @Override
            public void onResponse(okhttp3.Call call, final okhttp3.Response response) throws IOException {
                //  pd.dismiss();
                if (!response.isSuccessful()) {
                    fglocationbarcodenumber.setText("");
                    fgcartonbarcodenumber.setText("");
                    pd.dismiss();
                    Log.d("result", response.toString());
                   // System.out.println("token=" + ss.getString("access_token", ""));
                    System.out.println("responce=" + response.toString());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            pd.dismiss();
                            Toast.makeText(FgLocation.this, "No Responce", Toast.LENGTH_SHORT).show();

                        }
                    });
                    throw new IOException("Unexpected code " + response);
                } else {

                    pd.dismiss();
                    Log.d("result", response.toString());
                    String responseBody = response.body().string();
                    System.out.println("responce data="+responseBody);
                    final JSONObject obj;
                    try {
                        obj = new JSONObject(responseBody);


                            if(obj.getString("locationsallowable")!="0") {
                                // fstretrn=true;
                                firstlastdata = fglocationbarcodenumber.getText().toString().trim();

                                locationid = obj.getString("locationid");
                                locationsallowable = obj.getString("locationsallowable");


                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(FgLocation.this, "Success", Toast.LENGTH_SHORT).show();
                                        //
                                        //  pd.dismiss();
                                        //  Login.showDialog(RmLocation.this, "Success", true);
                                    }
                                });


                        } else {

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        Login.alert(obj.getString("msg"),FgLocation.this);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    fglocationbarcodenumber.setText("");
                                    fgcartonbarcodenumber.setText("");
                                    locationid="null";
                                    locationsallowable="null";


                                    //   Login.showDialog(RmLocation.this, "Failed", false);
                                    // fstretrn=false;

                                }
                            });

                        }


                    } catch (JSONException e) {
                        fglocationbarcodenumber.setText("");
                        fgcartonbarcodenumber.setText("");
                        locationid="null";
                        locationsallowable="null";
                        pd.dismiss();
                        e.printStackTrace();
                    }
                }
            }
        });

        // return fstretrn;
    }

    private void cartonScan() {
        RequestBody formBody = new FormBody.Builder()
                .add("location",locationid)
                .add("type","fg")
                .add("barcode",fgcartonbarcodenumber.getText().toString().trim())
                .add("maxallowable",locationsallowable)

                .build();

        Request request = new Request.Builder()
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + ss.getString("access_token", ""))
                .url(Login.web+"index.php?r=restapi/api/location-check")
                .post(formBody)
                .build();

        client.newCall(request).enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {
                fgcartonbarcodenumber.setText("");
                Log.d("result", e.getMessage());
                e.printStackTrace();
                pd.dismiss();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        // Stuff that updates the UI
                        Toast.makeText(FgLocation.this,"Please try again server busy at this moment", Toast.LENGTH_LONG).show();

                    }
                });
            }

            @Override
            public void onResponse(okhttp3.Call call, final okhttp3.Response response) throws IOException {



                //  pd.dismiss();
                if (!response.isSuccessful()) {
                    fgcartonbarcodenumber.setText("");
                    pd.dismiss();
                    Log.d("result", response.toString());
                    System.out.println("token="+ ss.getString("access_token", ""));
                    System.out.println("responce="+response.toString());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            pd.dismiss();
                            Toast.makeText(FgLocation.this, "No Responce", Toast.LENGTH_SHORT).show();

                        }
                    });
                    throw new IOException("Unexpected code " + response);
                } else {

                    pd.dismiss();
                    Log.d("result", response.toString());
                    String responseBody = response.body().string();
                    System.out.println("responce data1="+responseBody);
                    final JSONObject obj;
                    try {
                        obj= new JSONObject(responseBody);
                        if(obj.getString("success").equals("true")){
                            secondlastdate=fgcartonbarcodenumber.getText().toString().trim();


                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(FgLocation.this,"Success",Toast.LENGTH_SHORT).show();
                                    // Login.showDialog(RmLocation.this,"Success",true);
                                }
                            });



                        }else{

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    // Login.showDialog(RmLocation.this,"Failed",false);

                                    fgcartonbarcodenumber.setText("");
                                }
                            });


                        }


                    } catch (JSONException e) {
                        pd.dismiss();
                        fgcartonbarcodenumber.setText("");
                        e.printStackTrace();
                    }
                }
            }
        });
        // return false;
    }


    private void done() {

        RequestBody formBody = new FormBody.Builder()
                .add("location",locationid)
                .add("type","fg")
                .add("barcode",fgcartonbarcodenumber.getText().toString().trim())
                .add("maxallowable",locationsallowable)
                .build();

        Request request = new Request.Builder()
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + ss.getString("access_token", ""))
                .url(Login.web+"index.php?r=restapi/api/location-update")
                .post(formBody)
                .build();

        client.newCall(request).enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {
                fgcartonbarcodenumber.setText("");
                Log.d("result", e.getMessage());
                e.printStackTrace();
                pd.dismiss();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        // Stuff that updates the UI
                        Toast.makeText(FgLocation.this,"Please try again server busy at this moment", Toast.LENGTH_LONG).show();

                    }
                });
            }

            @Override
            public void onResponse(okhttp3.Call call, final okhttp3.Response response) throws IOException {



                //  pd.dismiss();
                if (!response.isSuccessful()) {

                    pd.dismiss();
                    Log.d("result", response.toString());
                    System.out.println("token="+ ss.getString("access_token", ""));
                    System.out.println("responce="+response.toString());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            pd.dismiss();
                            Toast.makeText(FgLocation.this, "No Responce", Toast.LENGTH_SHORT).show();
                        }
                    });
                    throw new IOException("Unexpected code " + response);
                } else {

                    pd.dismiss();
                    Log.d("result", response.toString());
                    String responseBody = response.body().string();
                    System.out.println("responce data1="+responseBody);
                    final JSONObject obj;
                    try {
                        obj= new JSONObject(responseBody);
                        if(obj.getString("success").equals("true")){

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Login.showDialog(FgLocation.this,"Success",true);
                                    Toast.makeText(FgLocation.this,"Success",Toast.LENGTH_SHORT).show();
                                    fglocationbarcodenumber.setText("");
                                    fgcartonbarcodenumber.setText("");
                                    // Login.showDialog(RmLocation.this,"Success",true);
                                }
                            });

                        }else{

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    // Login.showDialog(RmLocation.this,"Failed",true);
                                    Toast.makeText(FgLocation.this,"Failed",Toast.LENGTH_SHORT).show();
                                    // Login.showDialog(RmLocation.this,"Success",true);
                                }
                            });

                        }

                    } catch (JSONException e) {
                        pd.dismiss();
                        e.printStackTrace();
                    }
                }
            }
        });

    }
}
