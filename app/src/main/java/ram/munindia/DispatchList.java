package ram.munindia;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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
import ram.munindia.ModalandAdatpters.DispatchAdapter;
import ram.munindia.ModalandAdatpters.DispatchModel;
import ram.munindia.validations.Validations;

public class DispatchList extends AppCompatActivity implements View.OnClickListener {

    SharedPreferences ss;
    Spinner saleslist;
    ArrayAdapter<String> salesadapter;
    List<String> salesdata;
    ImageView myimage_back,done_img;
    LinearLayout li;

    //recyy
    ProgressDialog pd;
    RecyclerView recyclerview;
   private ArrayList<DispatchModel> list=null;
    DispatchAdapter adapter;
    TextView total;
    Map<String,String> saleordeermap = new HashMap<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dispatchlist);

        pd = new ProgressDialog(DispatchList.this);
        pd.setMessage("Fetching Data ..");
        pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pd.setIndeterminate(true);
        pd.setCancelable(false);
     //   li.setVisibility(View.VISIBLE);

        ss = getSharedPreferences("Login", MODE_PRIVATE);
            list = new ArrayList<DispatchModel>();

        myimage_back=findViewById(R.id.myimage_back);
        done_img=findViewById(R.id.done_img);
        li=findViewById(R.id.linearlayoutlist);
        total=findViewById(R.id.total);

        myimage_back.setOnClickListener(this);
        done_img.setOnClickListener(this);


        saleslist =findViewById(R.id.saleslist);
        recyclerview =findViewById(R.id.recyclerview);
        recyclerview.setLayoutManager(new LinearLayoutManager(this));


        //spinner data
 salesdata= new ArrayList<>();

        salesdata.add("select sales Order");
        // check Internet
        if (Validations.hasActiveInternetConnection(this))
        {
            saledatalist();
            //  Log.d("===========================", "Internet Present");
        }
        else
        {
            Toast.makeText(DispatchList.this,"Please Check Internet Connection", Toast.LENGTH_LONG).show();
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
        salesadapter.notifyDataSetChanged();


        // Toast.makeText(getBaseContext(), responseBody, Toast.LENGTH_SHORT).show();


        saleslist.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                                @Override
                                                public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {


                                                    if (!saleslist.getSelectedItem().equals("select sales Order")) {
                                                        li.setVisibility(View.GONE);
                                                    }
                                                    //   print.setVisibility(View.GONE);
                                                    //  success.setVisibility(View.GONE);
                                                    if (position != 0) {
                                                        li.setVisibility(View.VISIBLE);
                                                        list.removeAll(list);
                                                        getList();


                                                    }
                                                    if (position == 0) {
                                                        li.setVisibility(View.GONE);
                                              Toast.makeText(DispatchList.this, "please select SaleOrder List", Toast.LENGTH_SHORT).show();
                                                    }
                                                    // selectionCurrent= position;

                                                }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }

        });




//rec


        adapter = new DispatchAdapter(list, R.layout.dispatchsingle, getApplicationContext());
        recyclerview.setAdapter(adapter);

     //   pd.show();


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
                pd.dismiss();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        // Stuff that updates the UI
                        Toast.makeText(DispatchList.this,"Please try again server busy at this moment", Toast.LENGTH_LONG).show();

                    }
                });
            }

            @Override
            public void onResponse(okhttp3.Call call, final okhttp3.Response response) throws IOException {
                //  pd.dismiss();
                if (!response.isSuccessful()) {

                    Log.d("result", response.toString());
                    System.out.println("token="+ ss.getString("access_token", ""));
                    System.out.println("responce="+response.toString());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(DispatchList.this,"No Responce", Toast.LENGTH_LONG).show();
                            pd.dismiss();
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

                                    Toast.makeText(DispatchList.this,"Success",Toast.LENGTH_SHORT).show();
                                    pd.dismiss();

                                }
                            });

                        }else{

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    // Login.showDialog(ScanInput.this,"Failed",false);

                                    Toast.makeText(DispatchList.this,"Failed",Toast.LENGTH_SHORT).show();

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

    private void getList() {
        pd.show();

        OkHttpClient client = new OkHttpClient();
        RequestBody formBody = new FormBody.Builder()
                .add("sale_order_code", String.valueOf(getKeyFromValue(saleordeermap,saleslist.getSelectedItem())))
                .build();

        Request request = new Request.Builder()
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + ss.getString("access_token", ""))
                //.url("http://www.amock.io/api/sriramvarma/saleorderitems")
                 .url(Login.web+"index.php?r=restapi/api/get-sale-order-item-details")
                .post(formBody)
                .build();


        client.newCall(request).enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {
                Log.d("result", e.getMessage().toString());
                e.printStackTrace();
                pd.dismiss();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        li.setVisibility(View.GONE);

                        // Stuff that updates the UI
                        Toast.makeText(DispatchList.this,"Please try again server busy at this moment", Toast.LENGTH_LONG).show();

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
                            Toast.makeText(DispatchList.this,"No Responce", Toast.LENGTH_LONG).show();
                            li.setVisibility(View.GONE);
                            pd.dismiss();
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

                        JSONArray sale_order_items=obj.getJSONArray("sale_order_items");


                            for(int i=0;i<sale_order_items.length();i++) {
                                JSONObject jobj=sale_order_items.getJSONObject(i);


                                list.add(new DispatchModel(jobj.getString("sale_order_code"), jobj.getString("barcode_no"), jobj.getString("prepared_date"), jobj.getString("dispatched_date"), jobj.getString("quantity"),jobj.getString("dispatched")));

                            }
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    int size=list.size();
                                    adapter = new DispatchAdapter(list, R.layout.dispatchsingle, getApplicationContext());
                                    recyclerview.setAdapter(adapter);
                                    total.setText("Total="+size);
                                    pd.dismiss();

                                }
                            });

                        }else{

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    // Login.showDialog(ScanInput.this,"Failed",false);

                                    Toast.makeText(DispatchList.this,"Failed",Toast.LENGTH_SHORT).show();
                                }
                            });

                        }

                    } catch (JSONException e) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                pd.dismiss();
                                li.setVisibility(View.GONE);
                            }
                        });

                        e.printStackTrace();
                    }
                }
            }
        });

    }

    @Override
    public void onClick(View v) {
    switch (v.getId()){


    case R.id.myimage_back:
        finish();
        break;
    case R.id.done_img:
        // done();
        break;

        }
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
