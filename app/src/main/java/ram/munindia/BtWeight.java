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
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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


        pd = new ProgressDialog(BtWeight.this);
        pd.setMessage("Checking ..");
        pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pd.setIndeterminate(true);
        pd.setCancelable(false);


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

                   alert("please scan barcode",this);

               }else{
                   if(TextUtils.equals(weight.getText().toString(),"0") || TextUtils.isEmpty(weight.getText().toString().trim())) {
                       alert("Weight should not be null",this);

                   }else{
                       Toast.makeText(BtWeight.this, "done", Toast.LENGTH_SHORT).show();
                   }

               }
              //  done();

                break;


        }
    }


    @Override
    protected void onRestart() {
        super.onRestart();

runOnUiThread(new Runnable() {
    @Override
    public void run() {
        if (barcodenumber.getText().toString().length() == 0) {
            Toast.makeText(BtWeight.this, "Failed", Toast.LENGTH_SHORT).show();

        }else{

            pd.show();

            OkHttpClient client = new OkHttpClient();


            Request request = new Request.Builder()
                    .header("Accept", "application/json")
                    .header("Content-Type", "application/json")
                    // .header("Authorization", "Bearer" + ss.getString("access_token", ""))
                    .url("http://www.amock.io/api/sriramvarma/sad")
                    .get()
                    .build();


            client.newCall(request).enqueue(new okhttp3.Callback() {
                @Override
                public void onFailure(okhttp3.Call call, IOException e) {
                    Log.d("result", e.getMessage().toString());
                    e.printStackTrace();
                    // pd.dismiss();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            // Stuff that updates the UI
                            alert("Please try again server busy at this moment", BtWeight.this);

                        }
                    });
                }

                @Override
                public void onResponse(okhttp3.Call call, final okhttp3.Response response) throws IOException {
                    //  pd.dismiss();
                    if (!response.isSuccessful()) {
                        //  pd.dismiss();
                        Log.d("result", response.toString());
                        throw new IOException("Unexpected code " + response);
                    } else {
                        pd.dismiss();

                        Log.d("result", response.toString());
                        String responseBody = response.body().string();
                        System.out.println("Dadi " + responseBody.toString());
                        final JSONObject obj;
                        try {
                            obj= new JSONObject(responseBody);
                            if(obj.getString("status").equals("true")){
                                pd.dismiss();
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(BtWeight.this, "Success", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });


        }
    }
});





    }

    private void alert(String data, Context context){

        final android.app.AlertDialog.Builder builder1;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder1 = new android.app.AlertDialog.Builder(context, android.R.style.Theme_DeviceDefault_Dialog_Alert);
        } else {
            builder1 = new android.app.AlertDialog.Builder(context);
        }
       //builder1.setTitle(title);
        builder1.setMessage(data);
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                "Ok",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });


        AlertDialog alert11 = builder1.create();
        alert11.show();
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
        if(text.length()>=5) {
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
