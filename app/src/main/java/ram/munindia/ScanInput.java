package ram.munindia;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import ram.munindia.ModalandAdatpters.DispatchAdapter;
import ram.munindia.ModalandAdatpters.DispatchModel;
import ram.munindia.ModalandAdatpters.ScanInputAdapter;
import ram.munindia.ModalandAdatpters.ScanInputModel;
import ram.munindia.validations.Validations;

public class ScanInput extends AppCompatActivity implements View.OnClickListener {

    static EditText barcodenumberscaninput;
    EditText date;
    Spinner shift,line,employee,barcodetype;
    ImageView scannerinput,myimage_back,done_img;
    ArrayAdapter<String> shifta,linea,employeea,barcodetypea;
    List<String >  shifts,lines,employees,barcodetypes;
    ArrayList<String> data;
SharedPreferences ss;
    ProgressDialog pd;
   static String beforescaned="null";

    int i=10;

LinearLayout dataviewerlayout;
String finaldata="";
  //  String bar="",size="",data="";

//recycler
RecyclerView recyclerview;
    private ArrayList<ScanInputModel> list=null;
    ScanInputAdapter adapter;
   public static TextView total;


    //msp
    Map<String, List<String>> map = new HashMap<String, List<String>>();
    Map<String,String> shiftmap = new HashMap<>();
    Map<String,String> linemap = new HashMap<>();
    Map<String,String> empnamemap = new HashMap<>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scan_input);



        list = new ArrayList<ScanInputModel>();
        recyclerview =findViewById(R.id.recyclerview);
        recyclerview.setLayoutManager(new LinearLayoutManager(this));
       recyclerview.setNestedScrollingEnabled(false);

        pd = new ProgressDialog(ScanInput.this);
        pd.setMessage("Getting Data...");
        pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pd.setIndeterminate(true);
        pd.setCancelable(false);
        ss = getSharedPreferences("Login", MODE_PRIVATE);


        // check Internet
        if (Validations.hasActiveInternetConnection(this))
        {
            getData();
            //  Log.d("===========================", "Internet Present");
        }
        else
        {
            Toast.makeText(ScanInput.this,"Please Check Internet Connection", Toast.LENGTH_LONG).show();
            //Log.d("===========================", "No Internet");
            this.registerReceiver(this.mConnReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        }


//        barcodedataview=findViewById(R.id.barcodedataview);
//        sizedataview=findViewById(R.id.sizedataview);
//        dataview=findViewById(R.id.dataview);
        dataviewerlayout=findViewById(R.id.dataviewerlayout);

        total=findViewById(R.id.total);





        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        String formattedDate = df.format(c);

        date=findViewById(R.id.date);
        barcodenumberscaninput=findViewById(R.id.barcodenumber);
        shift=findViewById(R.id.shift);
        line=findViewById(R.id.line);
        employee=findViewById(R.id.employee);
        barcodetype=findViewById(R.id.barcodetype);
        scannerinput=findViewById(R.id.scannerinput);
        myimage_back=findViewById(R.id.myimage_back);
        done_img=findViewById(R.id.done_img);

        myimage_back.setOnClickListener(this);
        done_img.setOnClickListener(this);
        scannerinput.setOnClickListener(this);

        date.setLongClickable(false);
        barcodenumberscaninput.setLongClickable(false);
        //setting date;
        date.setText(formattedDate.toString());



//List<String> obj created and insiated by select
        shifts =new ArrayList<>();
        lines =new ArrayList<>();
        employees =new ArrayList<>();
        barcodetypes =new ArrayList<>();

        shifts.add("Select Shift");
        lines.add("Select Line");
        employees.add("Select Employe");
        barcodetypes.add("Select BarcodeType");




//shift adpter;

        // Initializing an ArrayAdapter

//        shifts.add("Shift-A");
//        shifts.add("Shift-B");

        shifta = new ArrayAdapter<String>(
                this, R.layout.support_simple_spinner_dropdown_item, shifts);

        shifta.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        shift.setAdapter(shifta);

        // listApi();
        shifta.notifyDataSetChanged();
        // Toast.makeText(getBaseContext(), responseBody, Toast.LENGTH_SHORT).show();

//line adpter;

        // Initializing an ArrayAdapter
//        lines.add("Line-1");
//        lines.add("Line-2");
        linea = new ArrayAdapter<String>(
                this, R.layout.support_simple_spinner_dropdown_item, lines);

        linea.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        line.setAdapter(linea);

        // listApi();
        linea.notifyDataSetChanged();
        // Toast.makeText(getBaseContext(), responseBody, Toast.LENGTH_SHORT).show();

//shift employee;

        // Initializing an ArrayAdapter
//        employees.add("Raju");
//        employees.add("Mounica");
//        employees.add("Pavan");
//        employees.add("Suresh");
//        employees.add("RamaKrishna");
        employeea = new ArrayAdapter<String>(
                this, R.layout.support_simple_spinner_dropdown_item, employees);

        employeea.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        employee.setAdapter(employeea);

        // listApi();
        employeea.notifyDataSetChanged();
        // Toast.makeText(getBaseContext(), responseBody, Toast.LENGTH_SHORT).show();

//barcodetype adpter;

        // Initializing an ArrayAdapter
//        barcodetypes.add("cell");
//        barcodetypes.add("carton");
        barcodetypea = new ArrayAdapter<String>(
                this, R.layout.support_simple_spinner_dropdown_item, barcodetypes);

        barcodetypea.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        barcodetype.setAdapter(barcodetypea);

        // listApi();
        barcodetypea.notifyDataSetChanged();
        // Toast.makeText(getBaseContext(), responseBody, Toast.LENGTH_SHORT).show();


    }




    @Override
    protected void onRestart() {
        super.onRestart();


            if (!barcodenumberscaninput.getText().toString().trim().isEmpty() || !barcodenumberscaninput.getText().toString().trim().equals("")) {
//alert dialog
                if (!beforescaned.equals(barcodenumberscaninput.getText().toString())) {

pd.show();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
pd.dismiss();

String sd=barcodenumberscaninput.getText().toString().trim();

                    if(!sd.isEmpty()) {



                        final Dialog dialog = new Dialog(ScanInput.this, R.style.PauseDialog);
                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        dialog.setCancelable(false);
                        dialog.setContentView(R.layout.custom_dialog);

                        TextView text = dialog.findViewById(R.id.text_dialog);
                        text.setText("Scaned-" + barcodenumberscaninput.getText().toString());


                        ImageView b = dialog.findViewById(R.id.b);

                        Button addButton = dialog.findViewById(R.id.btn_add);
                        addButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                            //    dataviewerlayout.setVisibility(View.VISIBLE);

                                barcodeScan();

//adding to the data

                                //  this line adds the data of your EditText and puts in your array
                             //   finaldata = String.format("%1$-25s %2$13s %3$15s", bar, size, data);

                                // next thing you have to do is check if your adapter has changed



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

                        Toast.makeText(ScanInput.this, "Barcode Not Found", Toast.LENGTH_SHORT).show();

                    }    }
                    }, 1000);



                }

            }else {

                Toast.makeText(ScanInput.this, "Not Scaned Barcode", Toast.LENGTH_SHORT).show();
            }

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.scannerinput:

                if (Validations.hasActiveInternetConnection(ScanInput.this)) {
                    if(barcodetype.getSelectedItemPosition()!=0){
                    Intent barcodescanner = new Intent(ScanInput.this, Barcodescanner.class);
                    barcodescanner.putExtra("click","scannerinput");
                    startActivity(barcodescanner);
                    }else{
                        Toast.makeText(ScanInput.this,"please select barcode type",Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(ScanInput.this,"please check internet connection",Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.myimage_back:
                finish();
                break;

            case R.id.done_img:
                data=new ArrayList<>();
     data.clear();

for(int i = 0;i<list.size();i++){

data.add(list.get(i).getId());

}
if(list.size()!=0) {
    if(shift.getSelectedItemPosition()!=0) {
        if(line.getSelectedItemPosition()!=0) {
            if(employee.getSelectedItemPosition()!=0) {
                if(barcodetype.getSelectedItemPosition()!=0) {

                   done();
                }else{
                    Login.alert("Please select Barcode Type",ScanInput.this);
                }
            }else{
                Login.alert("Please select Employe",ScanInput.this);
            }
        }else{
            Login.alert("Please select Line",ScanInput.this);
        }

    }else {

        Login.alert("Please select Shitft",ScanInput.this);
    }

}else{
    Toast.makeText(ScanInput.this,"No data",Toast.LENGTH_SHORT).show();
}
                // done();
                break;

        }
    }


    private void getData() {
        pd.show();

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + ss.getString("access_token", ""))
                .url(Login.web+"index.php?r=restapi/api/scan-input-details")
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
                        Toast.makeText(ScanInput.this,"Please try again server busy at this moment", Toast.LENGTH_LONG).show();

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
                            Toast.makeText(ScanInput.this, "No Responce", Toast.LENGTH_LONG).show();
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
//shifts
                            JSONArray shift=obj.getJSONArray("shifts");
                            for(int i=0;i<shift.length();i++){
                                JSONObject sh=shift.getJSONObject(i);
                                shifts.add(sh.getString("shift_type"));
                                shiftmap.put(sh.getString("id"),sh.getString("shift_type"));
                            }
//lines
                            JSONArray line=obj.getJSONArray("lines");
                            for(int i=0;i<line.length();i++){
                                JSONObject sh=line.getJSONObject(i);
                                lines.add(sh.getString("line"));
                                linemap.put(sh.getString("id"),sh.getString("line"));
                            }
//employess
                            JSONArray emp=obj.getJSONArray("employees");
                            for(int i=0;i<emp.length();i++){
                                JSONObject sh=emp.getJSONObject(i);
                                employees.add(sh.getString("emp_name"));
                                empnamemap.put(sh.getString("id"),sh.getString("emp_name"));
                            }
//barcode_type
                            JSONObject barcode_type=obj.getJSONObject("barcode_type");
                            barcodetypes.add(barcode_type.getString("cell"));
                            barcodetypes.add(barcode_type.getString("carton"));


                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                 //   Login.showDialog(ScanInput.this,"Success",true);
                                    Toast.makeText(ScanInput.this,"Success",Toast.LENGTH_SHORT).show();
                                }
                            });



                        }else{

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                   // Login.showDialog(ScanInput.this,"Failed",false);

                                    Toast.makeText(ScanInput.this,"Failed",Toast.LENGTH_SHORT).show();
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

   private void barcodeScan() {

        OkHttpClient client = new OkHttpClient();

        RequestBody formBody = new FormBody.Builder()
                .add("barcode", barcodenumberscaninput.getText().toString().trim())
                .add("barcode_type", String.valueOf(barcodetype.getSelectedItem()))
                .build();

        Request request = new Request.Builder()
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + ss.getString("access_token", ""))
               // .url(Login.web+"index.php?r=restapi/api/scan-input")
                .url(Login.web+"index.php?r=restapi/api/scan-input")
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
                        barcodenumberscaninput.setText("");
                        // Stuff that updates the UI
                        Toast.makeText(ScanInput.this,"Please try again server busy at this moment", Toast.LENGTH_LONG).show();

                    }
                });
            }

            @Override
            public void onResponse(okhttp3.Call call, final okhttp3.Response response) throws IOException {
                //  pd.dismiss();
                if (!response.isSuccessful()) {
                    barcodenumberscaninput.setText("");
                    pd.dismiss();
                    Log.d("result", response.toString());
                    System.out.println("token="+ ss.getString("access_token", ""));
                    System.out.println("responce="+response.toString());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            pd.dismiss();
                            Toast.makeText(ScanInput.this, "No Responce", Toast.LENGTH_LONG).show();
                        }
                    });
                    throw new IOException("Unexpected code " + response);
                } else {

                    pd.dismiss();
                    Log.d("result", response.toString());
                    String responseBody = response.body().string();
                    Log.d("hhresult", responseBody.toString());

                    final JSONObject obj;
                    try {
                        obj= new JSONObject(responseBody);
                        if(obj.getString("status").equals("true")){
                            list.clear();
                            JSONArray productsList=obj.getJSONArray("productsList");
                            for(int i=0;i<productsList.length();i++){

                              JSONObject jo=productsList.getJSONObject(i);
                              JSONObject joproduct=jo.getJSONObject("product");
                              JSONObject joproductCategory=jo.getJSONObject("productCategory");
                              JSONObject joproductSubCategory=jo.getJSONObject("productSubCategory");
                              JSONObject jocolor=jo.getJSONObject("color");
                              JSONObject josize=jo.getJSONObject("size");
                              JSONObject jogrammage=jo.getJSONObject("grammage");

                                String barcode,product,productcode,productcategory,productsubcategory,color,size,grammage,quantity,id;
                                barcode = jo.getString("barcode_no");
                                product=joproduct.getString("product_varient_name");
                                productcode=joproduct.getString("product_code");
                                productcategory=joproductCategory.getString("product_category");
                                productsubcategory=joproductSubCategory.getString("sub_category");
                                color =jocolor.getString("color");
                                size=josize.getString("sizes");
                                grammage=jogrammage.getString("grammage");
                                quantity=jo.getString("qty");
                                id=jo.getString("id");

                                Log.d("iiid=",id);


                                list.add(new ScanInputModel(barcode,product,productcode,productcategory,productsubcategory,color,size,grammage,quantity,id));

                            }


                            beforescaned=barcodenumberscaninput.getText().toString().trim();

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    int size=list.size();
                                    adapter = new ScanInputAdapter(list, R.layout.scaninputsiongle, getApplicationContext());
                                    recyclerview.setAdapter(adapter);
                                    total.setText("Total="+adapter.getItemCount());

                                    Toast.makeText(ScanInput.this,"Success",Toast.LENGTH_SHORT).show();

                                    //  Login.showDialog(ScanInput.this,"Success",true);
                                }

                            });


                        }else{

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(ScanInput.this,"Failed",Toast.LENGTH_SHORT).show();
                                    try {
                                        Login.alert(obj.getString("msg"),ScanInput.this);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    // Login.showDialog(ScanInput.this,"Failed",false);
                                    barcodenumberscaninput.setText("");
                                }
                            });

                        }


                    } catch (JSONException e) {
                        barcodenumberscaninput.setText("");
                        pd.dismiss();
                        e.printStackTrace();
                    }
                }
            }
        });

    }

    private void done() {



        OkHttpClient client = new OkHttpClient();

        Log.d("datadata", String.valueOf(data));
        RequestBody formBody = new FormBody.Builder()
                .add("date", date.getText().toString())
                .add("shift", String.valueOf(getKeyFromValue(shiftmap,shift.getSelectedItem().toString())))
                .add("line",String.valueOf(getKeyFromValue(linemap,line.getSelectedItem().toString())))
                .add("name",String.valueOf(getKeyFromValue(empnamemap,employee.getSelectedItem().toString())))
                .add("type",barcodetype.getSelectedItem().toString())
                .add("id",barcodenumberscaninput.getText().toString())
                .add("data", String.valueOf(data))
              //  .add("data",data)
                .build();

        Request request = new Request.Builder()
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + ss.getString("access_token", ""))
                .url(Login.web+"index.php?r=restapi/api/scan-input-save-detials")
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
                        barcodenumberscaninput.setText("");
                        // Stuff that updates the UI
                        Toast.makeText(ScanInput.this,"Please try again server busy at this moment", Toast.LENGTH_LONG).show();
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
                            barcodenumberscaninput.setText("");
                            pd.dismiss();
                            Toast.makeText(ScanInput.this, "No Responce", Toast.LENGTH_SHORT).show();

                        }
                    });
                    throw new IOException("Unexpected code " + response);
                } else {
                    pd.dismiss();
                    Log.d("result", response.toString());
                    String responseBody = response.body().string();
                    Log.d("hhresult", responseBody.toString());

                    final JSONObject obj;
                    try {
                        obj= new JSONObject(responseBody);
                        if(obj.getString("Success").equals("true")){

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    list.clear();
                                    adapter.notifyDataSetChanged();
                                    if(list.isEmpty()) {
                                        total.setText("Total=0");
                                        barcodenumberscaninput.setText("");
                                    }

                                Login.showDialog(ScanInput.this,"Success",true);

                                  //  Toast.makeText(ScanInput.this,"Success",Toast.LENGTH_SHORT).show();

                                }

                            });


                        }else{

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                   Toast.makeText(ScanInput.this,"Failed",Toast.LENGTH_SHORT).show();
                                  //  Login.showDialog(ScanInput.this,"Failed",false);
                                    try {
                                        Login.alert(obj.getString("msg"),ScanInput.this);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    // Login.showDialog(ScanInput.this,"Failed",false);
                                   // barcodenumberscaninput.setText("");
                                }
                            });

                        }

                    } catch (JSONException e) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                pd.dismiss();
                                barcodenumberscaninput.setText("");
                            }
                        });


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


