package ram.munindia;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
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

import okhttp3.OkHttpClient;
import okhttp3.Request;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dispatch);

        pd = new ProgressDialog(Dispatch.this);
        pd.setMessage("Fetching Data ..");
        pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pd.setIndeterminate(true);
        pd.setCancelable(false);

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
        saledatalist();
//        salesdata.add("SAL-10101");
//        salesdata.add("SAL-10120");

        salesadapter = new ArrayAdapter<String>(
                this, R.layout.support_simple_spinner_dropdown_item, salesdata);

        salesadapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        saleslist.setAdapter(salesadapter);

        // listApi();
        salesadapter.notifyDataSetChanged();




    }


    @Override
    protected void onRestart() {
        super.onRestart();

        if(!dispatchbarcodenumber.getText().toString().trim().isEmpty() || !dispatchbarcodenumber.getText().toString().trim().equals("") ){
//alert dialog
            final Dialog dialog = new Dialog(Dispatch.this, R.style.PauseDialog);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCancelable(false);
            dialog.setContentView(R.layout.custom_dialog);

            TextView text = dialog.findViewById(R.id.text_dialog);
            text.setText("Scaned-"+dispatchbarcodenumber.getText().toString());




            ImageView b = dialog.findViewById(R.id.b);

            Button addButton = dialog.findViewById(R.id.btn_add);
            addButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //dataviewerlayout.setVisibility(View.VISIBLE);

                    //   barcodedataview,sizedataview,dataview;

                    dataviewerlayout.setVisibility(View.VISIBLE);
                    bar=bar+dispatchbarcodenumber.getText().toString()+"\n";
                    size=size+"XXL "+"\n";
                    data=data+"data "+"\n";
                barcodedataview.setText(bar);
                sizedataview.setText(size);
                dataview.setText(data);


                    dialog.dismiss();
                }
            });

            Button cancleButton = dialog.findViewById(R.id.btn_cancle);
            cancleButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    dialog.dismiss();
                }
            });
            dialog.show();


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
                break;

            case R.id.scannerinput:

                if (Validations.hasActiveInternetConnection(Dispatch.this)) {
                    Intent barcodescanner = new Intent(Dispatch.this, Barcodescanner.class);
                   barcodescanner.putExtra("click","dispatch");
                    startActivity(barcodescanner);
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
                .url(Login.web+"index.php?r=restapi/api/get-sale-order-detials")
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
}
