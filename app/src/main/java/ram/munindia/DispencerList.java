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

public class DispencerList extends AppCompatActivity implements View.OnClickListener {

    ImageView dislocationbarcodescan, discartonbarcodescan, myimage_back, done_img;
    static EditText dislocationbarcodenumber, discartonbarcodenumber;

    OkHttpClient client;
    SharedPreferences ss;
    ProgressDialog pd;
    String firstlastdata="null",secondlastdate="null";
    String locationid="null",locationsallowable="null";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dispencer_list);

        client = new OkHttpClient();

        pd = new ProgressDialog(DispencerList.this);
        pd.setMessage("Checking...");
        pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pd.setIndeterminate(true);
        pd.setCancelable(false);


        dislocationbarcodescan = findViewById(R.id.dislocationbarcodescan);
        discartonbarcodescan = findViewById(R.id.discartonbarcodescan);
        myimage_back = findViewById(R.id.myimage_back);
        done_img = findViewById(R.id.done_img);

        dislocationbarcodescan.setOnClickListener(this);
        discartonbarcodescan.setOnClickListener(this);
        myimage_back.setOnClickListener(this);
        done_img.setOnClickListener(this);
        dislocationbarcodenumber = findViewById(R.id.dislocationbarcodenumber);
        discartonbarcodenumber = findViewById(R.id.discartonbarcodenumber);

        dislocationbarcodenumber.setLongClickable(false);
        discartonbarcodenumber.setLongClickable(false);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.dislocationbarcodescan:
                if (Validations.hasActiveInternetConnection(DispencerList.this)) {
                    Intent barcodescanner = new Intent(DispencerList.this, Barcodescanner.class);
                    barcodescanner.putExtra("click","dislocationbarcodescan");
                    startActivity(barcodescanner);
                } else {
                    Toast.makeText(DispencerList.this,"please check internet connection",Toast.LENGTH_SHORT).show();
                }

                break;
            case R.id.discartonbarcodescan:

                if (Validations.hasActiveInternetConnection(DispencerList.this)) {
                    Intent barcodescanner = new Intent(DispencerList.this, Barcodescanner.class);
                    barcodescanner.putExtra("click","discartonbarcodescan");
                    startActivity(barcodescanner);
                } else {
                    Toast.makeText(DispencerList.this,"please check internet connection",Toast.LENGTH_SHORT).show();
                }

                break;

            case R.id.done_img:

                if(!TextUtils.isEmpty(dislocationbarcodenumber.getText().toString().trim())) {
                    if (!TextUtils.isEmpty(discartonbarcodenumber.getText().toString().trim())) {
                        Toast.makeText(DispencerList.this,"done",Toast.LENGTH_SHORT).show();
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

        if(!TextUtils.isEmpty(dislocationbarcodenumber.getText().toString().trim()) || TextUtils.isEmpty(discartonbarcodenumber.getText().toString().trim())){

            if(!TextUtils.isEmpty(dislocationbarcodenumber.getText().toString().trim())){

                String first=dislocationbarcodenumber.getText().toString().trim();


                if(!first.equals(firstlastdata)){
                    pd.show();
                    locationScan();

                }

            }


            if(!TextUtils.isEmpty(discartonbarcodenumber.getText().toString().trim())){

                String second=discartonbarcodenumber.getText().toString().trim();

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
                .add("location", dislocationbarcodenumber.getText().toString().trim())
                .build();

        Request request = new Request.Builder()
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + ss.getString("access_token", ""))
                .url(Login.web+"index.php?r=restapi/api/get-pm-locations")
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
                        dislocationbarcodenumber.setText("");
                        discartonbarcodenumber.setText("");

                        // Stuff that updates the UI
                        Toast.makeText(DispencerList.this, "Please try again server busy at this moment", Toast.LENGTH_LONG).show();

                    }

                });
            }

            @Override
            public void onResponse(okhttp3.Call call, final okhttp3.Response response) throws IOException {
                //  pd.dismiss();
                if (!response.isSuccessful()) {
                    dislocationbarcodenumber.setText("");
                    discartonbarcodenumber.setText("");
                    pd.dismiss();
                    Log.d("result", response.toString());
                    // System.out.println("token=" + ss.getString("access_token", ""));
                    System.out.println("responce=" + response.toString());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            pd.dismiss();
                            Toast.makeText(DispencerList.this, "No Responce", Toast.LENGTH_LONG).show();
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
                            firstlastdata = dislocationbarcodenumber.getText().toString().trim();

                            locationid=obj.getString("locationid");
                            locationsallowable=obj.getString("locationsallowable");


                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(DispencerList.this,"Success",Toast.LENGTH_SHORT).show();
                                    //
                                    //  pd.dismiss();
                                    //  Login.showDialog(RmLocation.this, "Success", true);
                                }
                            });


                        } else {

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    dislocationbarcodenumber.setText("");
                                    discartonbarcodenumber.setText("");
                                    locationid="null";
                                    locationsallowable="null";



                                    //   Login.showDialog(RmLocation.this, "Failed", false);
                                    // fstretrn=false;

                                }
                            });

                        }


                    } catch (JSONException e) {
                        dislocationbarcodenumber.setText("");
                        discartonbarcodenumber.setText("");
                        locationid="null";
                        locationsallowable="null";
                        pd.dismiss();

                        e.printStackTrace();
                    }
                }
            }
        });


    }



    private void cartonScan() {

        RequestBody formBody = new FormBody.Builder()
                .add("loc_id",locationid)
                .add("carton_no",discartonbarcodenumber.getText().toString().trim())
                .add("max_allowable",locationsallowable)

                .build();

        Request request = new Request.Builder()
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + ss.getString("access_token", ""))
                .url(Login.web+"index.php?r=restapi/api/get-pm-detials")
                .post(formBody)
                .build();

        client.newCall(request).enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {
                discartonbarcodenumber.setText("");
                Log.d("result", e.getMessage());
                e.printStackTrace();
                pd.dismiss();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        // Stuff that updates the UI
                        Toast.makeText(DispencerList.this,"Please try again server busy at this moment", Toast.LENGTH_LONG).show();

                    }
                });
            }

            @Override
            public void onResponse(okhttp3.Call call, final okhttp3.Response response) throws IOException {



                //  pd.dismiss();
                if (!response.isSuccessful()) {
                    discartonbarcodenumber.setText("");

                    Log.d("result", response.toString());
                    System.out.println("token="+ ss.getString("access_token", ""));
                    System.out.println("responce="+response.toString());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            pd.dismiss();
                            Toast.makeText(DispencerList.this, "No Responce", Toast.LENGTH_LONG).show();
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
                            secondlastdate=discartonbarcodenumber.getText().toString().trim();


                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(DispencerList.this,"Success",Toast.LENGTH_SHORT).show();
                                    // Login.showDialog(RmLocation.this,"Success",true);
                                }
                            });



                        }else{

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    // Login.showDialog(RmLocation.this,"Failed",false);
                                    discartonbarcodenumber.setText("");
                                }
                            });


                        }


                    } catch (JSONException e) {
                        pd.dismiss();
                        discartonbarcodenumber.setText("");
                        e.printStackTrace();
                    }
                }
            }
        });


    }


    private void done() {

        RequestBody formBody = new FormBody.Builder()
                .add("loc_id",locationid)
                .add("carton_no",discartonbarcodenumber.getText().toString().trim())
                .add("max_allowable",locationsallowable)
                .build();

        Request request = new Request.Builder()
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + ss.getString("access_token", ""))
                .url(Login.web+"index.php?r=restapi/api/pm-location-update")
                .post(formBody)
                .build();

        client.newCall(request).enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {
                discartonbarcodenumber.setText("");
                Log.d("result", e.getMessage());
                e.printStackTrace();
                pd.dismiss();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        // Stuff that updates the UI
                        Toast.makeText(DispencerList.this,"Please try again server busy at this moment", Toast.LENGTH_LONG).show();

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
                    Toast.makeText(DispencerList.this,"ponce", Toast.LENGTH_LONG).show();
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
                                    Login.showDialog(DispencerList.this,"Success",true);
                                    Toast.makeText(DispencerList.this,"Success",Toast.LENGTH_SHORT).show();
                                    dislocationbarcodenumber.setText("");
                                    discartonbarcodenumber.setText("");
                                    // Login.showDialog(RmLocation.this,"Success",true);
                                }
                            });



                        }else{

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    // Login.showDialog(RmLocation.this,"Failed",true);
                                    Toast.makeText(DispencerList.this,"Failed",Toast.LENGTH_SHORT).show();
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
