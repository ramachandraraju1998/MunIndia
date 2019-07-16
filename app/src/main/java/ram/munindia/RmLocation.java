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

public class RmLocation extends AppCompatActivity implements View.OnClickListener {

    ImageView locationbarcodescan,cartonbarcodescan,myimage_back,done_img;
    static  EditText locationbarcodenumber,cartonbarcodenumber;

//for checking last value while restarting
    String firstlastdata="null",secondlastdate="null";
    Boolean fstretrn=false,secretrn=false;
    String locationid="null",locationsallowable="null";

    OkHttpClient client;
    SharedPreferences ss;
    ProgressDialog pd;
   // String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rm_location);

        pd = new ProgressDialog(RmLocation.this);
        pd.setMessage("Checking...");
        pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pd.setIndeterminate(true);
        pd.setCancelable(false);

        locationbarcodescan=findViewById(R.id.locationbarcodescan);
        cartonbarcodescan=findViewById(R.id.cartonbarcodescan);
        myimage_back=findViewById(R.id.myimage_back);
        done_img=findViewById(R.id.done_img);

        locationbarcodescan.setOnClickListener(this);
        cartonbarcodescan.setOnClickListener(this);
        myimage_back.setOnClickListener(this);
        done_img.setOnClickListener(this);

        locationbarcodenumber=findViewById(R.id.locationbarcodenumber);
        cartonbarcodenumber=findViewById(R.id.cartonbarcodenumber);


        locationbarcodenumber.setLongClickable(false);
        cartonbarcodenumber.setLongClickable(false);

        client = new OkHttpClient();
        ss = getSharedPreferences("Login", MODE_PRIVATE);
    }


    @Override
    protected void onRestart() {
        super.onRestart();

        if(!TextUtils.isEmpty(locationbarcodenumber.getText().toString().trim()) || TextUtils.isEmpty(cartonbarcodenumber.getText().toString().trim())){

            if(!TextUtils.isEmpty(locationbarcodenumber.getText().toString().trim())){

                String first=locationbarcodenumber.getText().toString().trim();


                          if(!first.equals(firstlastdata)){
                                 pd.show();
                                 locationScan();

                        }

            }


            if(!TextUtils.isEmpty(cartonbarcodenumber.getText().toString().trim())){

                String second=cartonbarcodenumber.getText().toString().trim();

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
                .add("location", locationbarcodenumber.getText().toString().trim())


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
                        locationbarcodenumber.setText("");
                        cartonbarcodenumber.setText("");

                        // Stuff that updates the UI
                        Toast.makeText(RmLocation.this, "Please try again server busy at this moment", Toast.LENGTH_LONG).show();

                    }

                });
            }

            @Override
            public void onResponse(okhttp3.Call call, final okhttp3.Response response) throws IOException {
                //  pd.dismiss();
                if (!response.isSuccessful()) {
                    locationbarcodenumber.setText("");
                    cartonbarcodenumber.setText("");

                    Log.d("result", response.toString());
                    System.out.println("token=" + ss.getString("access_token", ""));
                    System.out.println("responce=" + response.toString());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            pd.dismiss();
                            Toast.makeText(RmLocation.this, "No Responce", Toast.LENGTH_LONG).show();
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
                        if (obj.getString("success").equals("true")) {
                           // fstretrn=true;
                            firstlastdata = locationbarcodenumber.getText().toString().trim();

                            locationid=obj.getString("locationid");
                            locationsallowable=obj.getString("locationsallowable");


                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(RmLocation.this,"Success",Toast.LENGTH_SHORT).show();
                                  //
                                    //  pd.dismiss();
                                  //  Login.showDialog(RmLocation.this, "Success", true);
                                }
                            });


                        } else {

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    locationbarcodenumber.setText("");
                                    cartonbarcodenumber.setText("");
                                    locationid="null";
                                    locationsallowable="null";



                                    //   Login.showDialog(RmLocation.this, "Failed", false);
                                   // fstretrn=false;

                                }
                            });

                        }


                    } catch (JSONException e) {
                        locationbarcodenumber.setText("");
                        cartonbarcodenumber.setText("");
                        locationid="null";
                        locationsallowable="null";
                        pd.dismiss();
                        fstretrn=false;
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
                .add("type","rm")
                .add("barcode",cartonbarcodenumber.getText().toString().trim())
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
                cartonbarcodenumber.setText("");
                Log.d("result", e.getMessage());
                e.printStackTrace();
                pd.dismiss();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        // Stuff that updates the UI
                        Toast.makeText(RmLocation.this,"Please try again server busy at this moment", Toast.LENGTH_LONG).show();

                    }
                });
            }

            @Override
            public void onResponse(okhttp3.Call call, final okhttp3.Response response) throws IOException {



                //  pd.dismiss();
                if (!response.isSuccessful()) {
                    cartonbarcodenumber.setText("");
                    pd.dismiss();
                    Log.d("result", response.toString());
                    System.out.println("token="+ ss.getString("access_token", ""));
                    System.out.println("responce="+response.toString());
                    Toast.makeText(RmLocation.this,"ponce", Toast.LENGTH_LONG).show();
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
                            secondlastdate=cartonbarcodenumber.getText().toString().trim();


                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(RmLocation.this,"Success",Toast.LENGTH_SHORT).show();
                                   // Login.showDialog(RmLocation.this,"Success",true);
                                }
                            });



                        }else{

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                   // Login.showDialog(RmLocation.this,"Failed",false);
                                    cartonbarcodenumber.setText("");
                                }
                            });


                        }


                    } catch (JSONException e) {
                        pd.dismiss();
                        cartonbarcodenumber.setText("");
                        e.printStackTrace();
                    }
                }
            }
        });
       // return false;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.locationbarcodescan:
                if (Validations.hasActiveInternetConnection(RmLocation.this)) {
                    Intent barcodescanner = new Intent(RmLocation.this, Barcodescanner.class);
                    barcodescanner.putExtra("click","locationbarcodescan");
                    startActivity(barcodescanner);
                } else {
                    Toast.makeText(RmLocation.this,"please check internet connection",Toast.LENGTH_SHORT).show();
                }

                break;
            case R.id.cartonbarcodescan:

                if (Validations.hasActiveInternetConnection(RmLocation.this)) {
                    if(locationid.equals("null") || locationsallowable.equals("null")){
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(RmLocation.this,"please Scan Location First",Toast.LENGTH_SHORT).show();
                                // Login.showDialog(RmLocation.this,"Success",true);
                            }
                        });

                    }else {
                        Intent barcodescanner = new Intent(RmLocation.this, Barcodescanner.class);
                        barcodescanner.putExtra("click", "cartonbarcodescan");
                        startActivity(barcodescanner);
                    }
                } else {
                    Toast.makeText(RmLocation.this,"please check internet connection",Toast.LENGTH_SHORT).show();
                }

                break;

            case R.id.done_img:

                if(!TextUtils.isEmpty(locationbarcodenumber.getText().toString().trim())) {
                    if (!TextUtils.isEmpty(cartonbarcodenumber.getText().toString().trim())) {
                        Toast.makeText(RmLocation.this,"done",Toast.LENGTH_SHORT).show();
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

    private void done() {

        RequestBody formBody = new FormBody.Builder()
                .add("location",locationid)
                .add("type","rm")
                .add("barcode",cartonbarcodenumber.getText().toString().trim())
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
                cartonbarcodenumber.setText("");
                Log.d("result", e.getMessage());
                e.printStackTrace();
                pd.dismiss();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        // Stuff that updates the UI
                        Toast.makeText(RmLocation.this,"Please try again server busy at this moment", Toast.LENGTH_LONG).show();

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
                            Toast.makeText(RmLocation.this, "No Responce", Toast.LENGTH_LONG).show();
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
                                    Login.showDialog(RmLocation.this,"Success",true);
                                    Toast.makeText(RmLocation.this,"Success",Toast.LENGTH_SHORT).show();
                                    locationbarcodenumber.setText("");
                                    cartonbarcodenumber.setText("");
                                    // Login.showDialog(RmLocation.this,"Success",true);
                                }
                            });



                        }else{

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                   // Login.showDialog(RmLocation.this,"Failed",true);
                                    Toast.makeText(RmLocation.this,"Failed",Toast.LENGTH_SHORT).show();
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
