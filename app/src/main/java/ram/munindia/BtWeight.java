package ram.munindia;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Set;
import java.util.UUID;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import ram.munindia.PrintBt.DeviceListActivity;
import ram.munindia.PrintBt.UnicodeFormatter;
import ram.munindia.validations.Validations;

public class BtWeight extends AppCompatActivity implements View.OnClickListener,Runnable {

    ImageView scanning_qrcode,myimage_back,done_img;
   static EditText barcodenumber;
   static EditText weight;
   Button btscan;

    SharedPreferences ss;
//cardvie textviews
    TextView txproductcode,txproductcategory,txproductsubcategory,txproducttype,txproductsubtype,txcolor,txsize,txqty,txbarcodenumber;
CardView cardlayout;
    //bt
    protected static final String TAG = "TAG";
    private static final int REQUEST_CONNECT_DEVICE = 1;
    private static final int REQUEST_ENABLE_BT = 2;
    private static final int REQUEST_IMG= 3;


    Button mScan, mPrint, mDisc;
    BluetoothAdapter mBluetoothAdapter;
    private UUID applicationUUID = UUID
            .fromString("00001101-0000-1000-8000-00805F9B34FB");
    private ProgressDialog mBluetoothConnectProgressDialog;
    private BluetoothSocket mBluetoothSocket;
    BluetoothDevice mBluetoothDevice;
    boolean chh=false;
    ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bt_weight);

        ss = getSharedPreferences("Login", MODE_PRIVATE);
        pd = new ProgressDialog(BtWeight.this);
        pd.setMessage("Checking ..");
        pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pd.setIndeterminate(true);
        pd.setCancelable(false);

//card text
        txproductcode = findViewById(R.id.txproductcode);
        txproductcategory = findViewById(R.id.txproductcategory);
        txproductsubcategory = findViewById(R.id.txproductsubcategory);
        txproducttype = findViewById(R.id.txproducttype);
        txproductsubtype = findViewById(R.id.txproductsubtype);
        txcolor = findViewById(R.id.txcolor);
        txsize = findViewById(R.id.txsize);
        txqty = findViewById(R.id.txqty);
        txbarcodenumber = findViewById(R.id.txbarcodenumber);
        cardlayout = findViewById(R.id.cardlayout);

        
        scanning_qrcode=findViewById(R.id.scanning_qrcode);
        myimage_back=findViewById(R.id.myimage_back);
        done_img=findViewById(R.id.done_img);
        weight=findViewById(R.id.weight);


        btscan=findViewById(R.id.btscan);
        btscan.setOnClickListener(new View.OnClickListener() {
            public void onClick(View mView) {
                if(chh==true){
                    if(mBluetoothAdapter!=null) {
                        try {
                            weight.setText("");
                            getWeight();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }else{   scan(); }
                }else {
                    scan();
                }
            }
        });



        scanning_qrcode.setOnClickListener(this);
        myimage_back.setOnClickListener(this);
        done_img.setOnClickListener(this);



        barcodenumber=findViewById(R.id.barcodenumber);

        barcodenumber.setLongClickable(false);
        weight.setLongClickable(false);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.scanning_qrcode:
                if (Validations.hasActiveInternetConnection(BtWeight.this)) {
                    Intent barcodescanner = new Intent(BtWeight.this, Barcodescanner.class);
                    barcodescanner.putExtra("click","scanning_qrcode");
                    startActivity(barcodescanner);
                } else {
                    Toast.makeText(BtWeight.this,"please check internet connection",Toast.LENGTH_SHORT).show();
                }

                break;
            case R.id.myimage_back:

                finish();

                break;
            case R.id.done_img:

               if(TextUtils.isEmpty(barcodenumber.getText().toString().trim()))
               {
               //    Login.showDialog(BtWeight.this,"please scan barcode",true);
                   Login.alert("please scan barcode",this);

               }else{
                   if(TextUtils.equals(weight.getText().toString(),"0") || TextUtils.isEmpty(weight.getText().toString().trim())) {
                       Login.alert("Weight should not be null",this);
//                       Login.showDialog(BtWeight.this,"Weight should not be null",true);

                   }else{
                       pd.setMessage("sending data..");
                       pd.show();
                       done();
                       Toast.makeText(BtWeight.this, "done", Toast.LENGTH_SHORT).show();
                   }

               }
              //  done();

                break;


        }
    }

    private void done() {


        OkHttpClient client = new OkHttpClient();

        RequestBody formBody = new FormBody.Builder()
                .add("barcode", barcodenumber.getText().toString().trim())
                .add("weight",weight.getText().toString().trim())
                .build();

        Request request = new Request.Builder()
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + ss.getString("access_token", ""))
                .url(Login.web+"index.php?r=restapi/api/update-barcode-weight")
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

                        // Stuff that updates the UI
                        Toast.makeText(BtWeight.this,"Please try again server busy at this moment", Toast.LENGTH_LONG).show();

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
                            Toast.makeText(BtWeight.this, "No Responce", Toast.LENGTH_SHORT).show();

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
                        if(obj.getString("success").equals("true")){
runOnUiThread(new Runnable() {
    @Override
    public void run() {
        Login.showDialog(BtWeight.this,"Successfully updated Weight",true);
    }
});



                        }else{

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Login.showDialog(BtWeight.this,"Failed to Update Weight",false);

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


    @Override
    protected void onRestart() {
        super.onRestart();

        if (barcodenumber.getText().toString().length() == 0) {


            Toast.makeText(BtWeight.this, "Failed", Toast.LENGTH_SHORT).show();

        }else{

            pd.show();


            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    cardlayout.setVisibility(View.GONE);

                }
            });


            OkHttpClient client = new OkHttpClient();

            RequestBody formBody = new FormBody.Builder()
                    .add("barcode", barcodenumber.getText().toString().trim())
                    .build();

            Request request = new Request.Builder()
                    .header("Accept", "application/json")
                    .header("Content-Type", "application/json")
                    .header("Authorization", "Bearer " + ss.getString("access_token", ""))
                    .url(Login.web+"index.php?r=restapi/api/get-barcode-details")
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

                            // Stuff that updates the UI
                            Login.alert("Please try again server busy at this moment", BtWeight.this);

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
                        throw new IOException("Unexpected code " + response);
                    } else {

                        pd.dismiss();
                        Log.d("result", response.toString());
                        String responseBody = response.body().string();
                        final JSONObject obj;
                        try {
                            obj= new JSONObject(responseBody);
                            if(obj.getString("success").equals("true")){


                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        cardlayout.setVisibility(View.VISIBLE);
                                    }
                                });



                                JSONObject data = obj.getJSONObject("data");
                                JSONObject product = data.getJSONObject("product");
                                JSONObject productCategory = data.getJSONObject("productCategory");
                                JSONObject productSubCategory = data.getJSONObject("productSubCategory");
                                JSONObject productType = data.getJSONObject("productType");
                                JSONObject productSubType = data.getJSONObject("productSubType");
                                JSONObject color = data.getJSONObject("color");
                                JSONObject size = data.getJSONObject("size");

   // txproductcode,txproductcategory,txproductsubcategory,txproducttype,txproductsubtype,txcolor,txsize,txqty;
                            txbarcodenumber.setText(data.getString("barcode_no"));
                            txproductcode.setText(product.getString("product_code").toString());
                            txproductcategory.setText(productCategory.getString("product_category").toString());
                            txproductsubcategory.setText(productSubCategory.getString("sub_category").toString());
                            txproducttype.setText(productType.getString("product_type").toString());
                            txproductsubtype.setText(productSubType.getString("product_sub_type").toString());
                            txcolor.setText(color.getString("color").toString());
                            txsize.setText(size.getString("sizes").toString());
                            txqty.setText(data.getString("qty").toString());




                            }else{
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        barcodenumber.setText(" ");
                                        cardlayout.setVisibility(View.GONE);
                                        Toast.makeText(BtWeight.this,"Barcode not Valid",Toast.LENGTH_LONG).show();
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


    private void scan() {


        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        if (mBluetoothAdapter == null) {
            Toast.makeText(BtWeight.this, "Message1", Toast.LENGTH_SHORT).show();
        } else {
            if (!mBluetoothAdapter.isEnabled()) {
                Intent enableBtIntent = new Intent(
                        BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent,
                        REQUEST_ENABLE_BT);
            }
            else {
                ListPairedDevices();
                Intent connectIntent = new Intent(BtWeight.this,
                        DeviceListActivity.class);
                //   print();
                startActivityForResult(connectIntent,
                        REQUEST_CONNECT_DEVICE);

            }
        }

    }
    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        try {
            if (mBluetoothSocket != null)
                chh=false;
            mBluetoothSocket.close();
        } catch (Exception e) {
            Log.e("Tag", "Exe ", e);
        }
    }

    @Override
    public void onBackPressed() {
        try {
            if (mBluetoothSocket != null)
                mBluetoothSocket.close();
        } catch (Exception e) {
            Log.e("Tag", "Exe ", e);
        }
        setResult(RESULT_CANCELED);
        finish();
    }


    public void onActivityResult(int mRequestCode, int mResultCode,
                                 Intent mDataIntent) {
        super.onActivityResult(mRequestCode, mResultCode, mDataIntent);

        switch (mRequestCode) {
            case REQUEST_CONNECT_DEVICE:
                if (mResultCode == Activity.RESULT_OK) {
                    Bundle mExtra = mDataIntent.getExtras();
                    String mDeviceAddress = mExtra.getString("DeviceAddress");
                    Log.v("", "Coming incoming address " + mDeviceAddress);
                    mBluetoothDevice = mBluetoothAdapter
                            .getRemoteDevice(mDeviceAddress);
                    mBluetoothConnectProgressDialog = ProgressDialog.show(this,
                            "Connecting...", mBluetoothDevice.getName() + " : "
                                    + mBluetoothDevice.getAddress(), true, true);
                    Thread mBlutoothConnectThread = new Thread(this);
                    mBlutoothConnectThread.start();
                    // pairToDevice(mBluetoothDevice); This method is replaced by
                    // progress dialog with thread
                }
                break;

            case REQUEST_ENABLE_BT:
                if (mResultCode == Activity.RESULT_OK) {
                    ListPairedDevices();
                    Intent connectIntent = new Intent(BtWeight.this,
                            DeviceListActivity.class);
                    startActivityForResult(connectIntent, REQUEST_CONNECT_DEVICE);
                } else {
                    Toast.makeText(BtWeight.this, "Message", Toast.LENGTH_SHORT).show();
                }
                break;

        }
    }


    private void ListPairedDevices() {
        Set<BluetoothDevice> mPairedDevices = mBluetoothAdapter
                .getBondedDevices();
        if (mPairedDevices.size() > 0) {
            for (BluetoothDevice mDevice : mPairedDevices) {
                Log.v("", "PairedDevices: " + mDevice.getName() + "  "
                        + mDevice.getAddress());
            }
        }
    }

    public void run() {
        try {
            mBluetoothSocket = mBluetoothDevice
                    .createRfcommSocketToServiceRecord(applicationUUID);
            mBluetoothAdapter.cancelDiscovery();
            mBluetoothSocket.connect();
            mHandler.sendEmptyMessage(0);
        } catch (IOException eConnectException) {
            Log.d("", "CouldNotConnectToSocket", eConnectException);
            closeSocket(mBluetoothSocket);
            return;
        }
    }

    private void closeSocket(BluetoothSocket nOpenSocket) {
        try {
            nOpenSocket.close();
            Log.d("", "SocketClosed");
        } catch (IOException ex) {
            Log.d("", "CouldNotCloseSocket");
        }
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            mBluetoothConnectProgressDialog.dismiss();
            Toast.makeText(BtWeight.this, "DeviceConnected", Toast.LENGTH_SHORT).show();
            chh=true;
//            try {
//                getWeight();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
        }
    };

    public static byte intToByteArray(int value) {
        byte[] b = ByteBuffer.allocate(4).putInt(value).array();

        for (int k = 0; k < b.length; k++) {
            System.out.println("Selva  [" + k + "] = " + "0x"
                    + UnicodeFormatter.byteToHex(b[k]));
        }

        return b[3];
    }

    public byte[] sel(int val) {
        ByteBuffer buffer = ByteBuffer.allocate(2);
        buffer.putInt(val);
        buffer.flip();
        return buffer.array();
    }


    private void getWeight() throws IOException {

        byte[] buffer = new byte[256];
        ByteArrayInputStream input = new ByteArrayInputStream(buffer);
        InputStream inputStream = mBluetoothSocket.getInputStream();
        int length = inputStream.read(buffer);
        String text = new String(buffer, 0, length);
        if(text.length()>5) {
            String tx = text.substring(1,6);

            //     int i = Integer.parseInt(tx);


            //String dd= (new DecimalFormat("##.###").format(tx));




            //DecimalFormat df = new DecimalFormat("#.000");
            // String ss = String.format("%.3f",tx);
            try{

                Float f=Float.parseFloat(tx);
                DecimalFormat format = new DecimalFormat("#.000");
//                String numberAsString = String.format ("%.4f", f);
                f=f/1000;
              //  sri=f.toString();


                String s=  format.format(f);
                weight.setText(s.toString());

            }
            catch(NumberFormatException NFE)
            {
                System.out.println("NumberFormatException: " + NFE.getMessage());
            }
        }
    }



}
