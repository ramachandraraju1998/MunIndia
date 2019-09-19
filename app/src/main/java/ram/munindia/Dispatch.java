package ram.munindia;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import ram.munindia.validations.Validations;

import static ram.munindia.ScanInput.barcodenumberscaninput;

public class Dispatch extends AppCompatActivity implements View.OnClickListener {
    Spinner saleslist;
    ArrayAdapter<String> salesadapter;
    List<String> salesdata;
    ImageView myimage_back,done_img,scannerinput;
    static EditText dispatchbarcodenumber;
    LinearLayout dataviewerlayout;
//data viewer
    TextView barcodedataview,sizedataview,dataview;
    String bar="",size="",data="";

    SharedPreferences ss;
    ProgressDialog pd;
    Map<String,String> saleordeermap = new HashMap<>();
ArrayList<String> barcodescaned,idlist;
String code;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dispatch);
//        barcodescaned.clear();
        pd = new ProgressDialog(Dispatch.this);
        pd.setMessage("Fetching Data ..");
        pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pd.setIndeterminate(true);
        pd.setCancelable(false);

        barcodescaned= new ArrayList<>();
        idlist= new ArrayList<>();
        ss = getSharedPreferences("Login", MODE_PRIVATE);
        myimage_back=findViewById(R.id.myimage_back);
        done_img=findViewById(R.id.done_img);
        dispatchbarcodenumber=findViewById(R.id.dispatchbarcodenumber);
        dataviewerlayout=findViewById(R.id.dataviewerlayout);

        myimage_back.setOnClickListener(this);
        done_img.setOnClickListener(this);
        //
        barcodedataview=findViewById(R.id.barcodedataview);
        sizedataview=findViewById(R.id.sizedataview);
        dataview=findViewById(R.id.dataview);

        saleslist =findViewById(R.id.saleslist);

        scannerinput=findViewById(R.id.scannerinput);
        scannerinput.setOnClickListener(this);

        salesdata= new ArrayList<>();

        salesdata.add("select sales Order");

        if (Validations.hasActiveInternetConnection(this))
        {
            saledatalist();
            //  Log.d("===========================", "Internet Present");
        }
        else
        {
            Toast.makeText(Dispatch.this,"Please Check Internet Connection", Toast.LENGTH_LONG).show();
            //Log.d("===========================", "No Internet");
            this.registerReceiver(this.mConnReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        }
//        salesdata.add("SAL-10101");
//        salesdata.add("SAL-10120");

        salesadapter = new ArrayAdapter<String>(
                this, R.layout.support_simple_spinner_dropdown_item, salesdata);

        salesadapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        saleslist.setAdapter(salesadapter);

        // listApi();

        saleslist.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                String selectedItem = parent.getItemAtPosition(position).toString();
                if(!saleslist.getSelectedItem().equals("select sales Order") || saleslist.getSelectedItemPosition()!=0)
                {
                    bar="";size="";data="";
                    dataviewerlayout.setVisibility(View.GONE);
                            barcodescaned.clear();
                            idlist.clear();

                    barcodedataview.setText("");
                    sizedataview.setText("");
                    dataview.setText("");
                }
            } // to close the onItemSelected
            public void onNothingSelected(AdapterView<?> parent)
            {

            }
        });



    }


    @Override
    protected void onRestart() {
        super.onRestart();

        if(!dispatchbarcodenumber.getText().toString().trim().isEmpty() || !dispatchbarcodenumber.getText().toString().trim().equals("")){
//alert dialog
//            final Dialog dialog = new Dialog(Dispatch.this, R.style.PauseDialog);
//            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//            dialog.setCancelable(false);
//            dialog.setContentView(R.layout.custom_dialog);
//
//            TextView text = dialog.findViewById(R.id.text_dialog);
//            text.setText("Scaned-"+dispatchbarcodenumber.getText().toString());
//
//
//            ImageView b = dialog.findViewById(R.id.b);
//
//            Button addButton = dialog.findViewById(R.id.btn_add);
//            addButton.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    //dataviewerlayout.setVisibility(View.VISIBLE);
//
//                    //   barcodedataview,sizedataview,dataview;
//
//                    dataviewerlayout.setVisibility(View.VISIBLE);
//                    bar=bar+dispatchbarcodenumber.getText().toString()+"\n";
//                    size=size+"XXL "+"\n";
//                    data=data+"data "+"\n";
//                barcodedataview.setText(bar);
//                sizedataview.setText(size);
//                dataview.setText(data);
//
//
//                    dialog.dismiss();
//                }
//            });
//
//            Button cancleButton = dialog.findViewById(R.id.btn_cancle);
//            cancleButton.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//
//                    dialog.dismiss();
//                }
//            });
//            dialog.show();
//
            dataviewerlayout.setVisibility(View.VISIBLE);
            getData();
            dispatchbarcodenumber.setText("");

        }else{
            Toast.makeText(Dispatch.this,"Not Scaned Barcode",Toast.LENGTH_SHORT).show();
        }

    }



    @Override
    public void onClick(View v) {


        switch (v.getId()){


            case R.id.myimage_back:
                finish();
                break;
            case R.id.done_img:
                // done();
if(saleslist.getSelectedItemPosition()!=0 && !barcodescaned.isEmpty()){
                new AlertDialog.Builder(this)

                        .setMessage("Do you want to dispatch "+saleslist.getSelectedItem())
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                done();
                            }

                        })
                        .setNegativeButton("No", null)
                        .show();
                }

                break;

            case R.id.scannerinput:

                if (Validations.hasActiveInternetConnection(Dispatch.this)) {
                    if(saleslist.getSelectedItemPosition()!=0){
                    Intent barcodescanner = new Intent(Dispatch.this, Barcodescanner.class);
                   barcodescanner.putExtra("click","dispatch");
                    startActivity(barcodescanner);
                    }else{  Toast.makeText(Dispatch.this,"please select saleoder first",Toast.LENGTH_SHORT).show(); }
                } else {
                    Toast.makeText(Dispatch.this,"please check internet connection",Toast.LENGTH_SHORT).show();
                }
                break;

        }

    }



    private void saledatalist() {
        pd.show();

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + ss.getString("access_token", ""))
               // .url("http://www.amock.io/api/sriramvarma/saleslistdata")
                .url(Login.web+"index.php?r=restapi/api/get-sale-order-details")
                .get()
                .build();


        client.newCall(request).enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {
                Log.d("result", e.getMessage().toString());
                e.printStackTrace();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        pd.dismiss();
                        // Stuff that updates the UI
                        Toast.makeText(Dispatch.this,"Please try again server busy at this moment", Toast.LENGTH_LONG).show();

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
                            Toast.makeText(Dispatch.this, "No Responce", Toast.LENGTH_LONG).show();
                        }
                    });
                    throw new IOException("Unexpected code " + response);
                } else {

                    pd.dismiss();
                    Log.d("result", response.toString());
                    String responseBody = response.body().string();
                    final JSONObject obj;
                    try {
                        obj= new JSONObject(responseBody);
                        if(obj.getString("Success").equals("true")){



                            JSONArray Saleorderlist=obj.getJSONArray("Saleorderlist");
                            for(int i=0;i<Saleorderlist.length();i++){
                                JSONObject jobj=Saleorderlist.getJSONObject(i);

                                salesdata.add(jobj.getString("sale_order_code"));
                                saleordeermap.put(jobj.getString("id"),jobj.getString("sale_order_code"));
                            }

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    // Login.showDialog(ScanInput.this,"Failed",false);

                                    Toast.makeText(Dispatch.this,"Success",Toast.LENGTH_SHORT).show();
                                    pd.dismiss();

                                }
                            });

                        }else{

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    // Login.showDialog(ScanInput.this,"Failed",false);
                                    Toast.makeText(Dispatch.this,"Failed",Toast.LENGTH_SHORT).show();

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
private void getData(){

    pd.show();

    OkHttpClient client = new OkHttpClient();
    code=dispatchbarcodenumber.getText().toString().trim();
    RequestBody formBody = new FormBody.Builder()
            .add("barcode",code)
            .add("saleorder",String.valueOf(getKeyFromValue(saleordeermap,saleslist.getSelectedItem())))
            .build();
    Log.d("saleorder=",String.valueOf(getKeyFromValue(saleordeermap,saleslist.getSelectedItem())));

System.out.println("ccode="+code);
    Request request = new Request.Builder()
            .header("Accept", "application/json")
            .header("Content-Type", "application/json")
            .header("Authorization", "Bearer " + ss.getString("access_token", ""))
            .url(Login.web+"index.php?r=restapi/api/dispatch-scan")
            .post(formBody)
            .build();


    client.newCall(request).enqueue(new okhttp3.Callback() {
        @Override
        public void onFailure(okhttp3.Call call, IOException e) {
            Log.d("result", e.getMessage().toString());
            e.printStackTrace();

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    pd.dismiss();
                    // Stuff that updates the UI
                    Toast.makeText(Dispatch.this,"Please try again server busy at this moment", Toast.LENGTH_LONG).show();

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
                        Toast.makeText(Dispatch.this, "No Responce", Toast.LENGTH_LONG).show();
                    }
                });
                throw new IOException("Unexpected code " + response);
            } else {

                pd.dismiss();
                Log.d("result", response.toString());
                String responseBody = response.body().string();
                final JSONObject obj;
                try {
                    obj= new JSONObject(responseBody);
                    if(obj.getString("Success").equals("true")){

                       JSONObject checkstock= obj.getJSONObject("checkstock");
                       JSONObject dispatchlistdata= obj.getJSONObject("dispatchlistdata");




                       if(!barcodescaned.contains(code)) {

                           String add=dispatchbarcodenumber.getText().toString().trim();
                           barcodescaned.add(code);
                           Log.d("added",add);

                           bar = bar + checkstock.getString("barcode_no") + "\n";
                           size = size + checkstock.getString("sizes") + "\n";
                           data = data + checkstock.getString("product_name") + "\n";
                           //adding to barcode scaned


                               saleslist.setEnabled(false);
                               saleslist.setClickable(false);



                           idlist.add(dispatchlistdata.getString("id"));
                           runOnUiThread(new Runnable() {
                               @Override
                               public void run() {
                                   barcodedataview.setText(bar);
                                   sizedataview.setText(size);
                                   dataview.setText(data);

//                                   System.out.println("barcodescaned="+barcodescaned);
                                   Log.d("barcodescaned=", String.valueOf(barcodescaned));
                               }
                           });

                       }else{
                           runOnUiThread(new Runnable() {
                           @Override
                           public void run() {
                               Login.alert("Barcode Already Scaned..",Dispatch.this);
                               Log.d("barcodescaned", String.valueOf(barcodescaned));
                            }
                           });
                           System.out.println("barcodescaned="+barcodescaned);
                       }
                    }else{

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                // Login.showDialog(ScanInput.this,"Failed",false);
                                try {
                                    Login.showDialog(Dispatch.this,obj.getString("msg"),false);
                                    if(barcodescaned.isEmpty()){     dataviewerlayout.setVisibility(View.GONE);   }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                               // Toast.makeText(Dispatch.this,"Failed",Toast.LENGTH_SHORT).show();
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

    private void done() {



        pd.show();

        OkHttpClient client = new OkHttpClient();

        RequestBody formBody = new FormBody.Builder()
                .add("dispatchlistids", String.valueOf(idlist))
                .add("saleorderid", String.valueOf(getKeyFromValue(saleordeermap,saleslist.getSelectedItem())))
                .build();

        Request request = new Request.Builder()
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + ss.getString("access_token", ""))
                .url(Login.web+"index.php?r=restapi/api/dispatch")
                .post(formBody)
                .build();


        client.newCall(request).enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {
                Log.d("result", e.getMessage().toString());
                e.printStackTrace();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        pd.dismiss();
                        // Stuff that updates the UI
                        Toast.makeText(Dispatch.this,"Please try again server busy at this moment", Toast.LENGTH_LONG).show();

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
                            Toast.makeText(Dispatch.this, "No Responce", Toast.LENGTH_LONG).show();
                        }
                    });
                    Log.d("result=", response.toString());
                    throw new IOException("Unexpected code " + response);
                } else {


                    pd.dismiss();
                    Log.d("result=", response.toString());
                    String responseBody = response.body().string();
                    Log.d("responcebody=", responseBody);
                    final JSONObject obj;

                    try {
                        obj= new JSONObject(responseBody);
                        if(obj.getString("Success").equals("true")){


                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    // Login.showDialog(ScanInput.this,"Failed",false);
                                    saleslist.setEnabled(true);
                                    saleslist.setClickable(true);
                                    barcodescaned.clear();
                                    idlist.clear();
                                    bar="";size="";data="";
                                    dataviewerlayout.setVisibility(View.GONE);
                                    barcodedataview.setText("");
                                    sizedataview.setText("");
                                    dataview.setText("");
                                   // Toast.makeText(Dispatch.this,"Success",Toast.LENGTH_SHORT).show();
                                    Login.showDialog(Dispatch.this,"Success",true);
                                }
                            });

                        }else{

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    // Login.showDialog(ScanInput.this,"Failed",false);
                                    try {

                                        Login.showDialog(Dispatch.this,obj.getString("msg"),false);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    //Toast.makeText(Dispatch.this,"Failed",Toast.LENGTH_SHORT).show();
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

    private BroadcastReceiver mConnReceiver = new BroadcastReceiver()
    {
        public void onReceive(Context context, Intent intent)
        {
            boolean noConnectivity = intent.getBooleanExtra(ConnectivityManager.EXTRA_NO_CONNECTIVITY, false);
            String reason = intent.getStringExtra(ConnectivityManager.EXTRA_REASON);
            boolean isFailover = intent.getBooleanExtra(ConnectivityManager.EXTRA_IS_FAILOVER, false);

            NetworkInfo currentNetworkInfo = (NetworkInfo) intent.getParcelableExtra(ConnectivityManager.EXTRA_NETWORK_INFO);
            NetworkInfo otherNetworkInfo = (NetworkInfo) intent.getParcelableExtra(ConnectivityManager.EXTRA_OTHER_NETWORK_INFO);

            if (currentNetworkInfo.isConnected())
            {
                // Log.d("===========================", "Connected");
                finish();
                startActivity(getIntent());
                Toast.makeText(getApplicationContext(), "Connected",Toast.LENGTH_LONG).show();
            }
            else
            {
                //  Log.d("===========================", "Not Connected");
                Toast.makeText(getApplicationContext(), "internet Not Connected",
                        Toast.LENGTH_LONG).show();
            }
        }
    };

    public static Object getKeyFromValue(Map hm, Object value) {
        for (Object o : hm.keySet()) {
            if (hm.get(o).equals(value)) {
                return o;
            }
        }
        return null;
    }

}
