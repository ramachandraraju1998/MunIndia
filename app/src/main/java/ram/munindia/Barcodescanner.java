package ram.munindia;

import android.app.ActionBar;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import me.dm7.barcodescanner.zbar.BarcodeFormat;
import me.dm7.barcodescanner.zbar.Result;
import me.dm7.barcodescanner.zbar.ZBarScannerView;

public class Barcodescanner extends AppCompatActivity implements ZBarScannerView.ResultHandler {
    private ZBarScannerView mScannerView;
    ProgressDialog pd;
    String res;
    Boolean sss=false;
    String click;

    @Override
    public void onCreate(Bundle state) {

        super.onCreate(state);
        mScannerView = new ZBarScannerView(this);    // Programmatically initialize the scanner view
        List<BarcodeFormat> myformat = new ArrayList<>();
        myformat.add(BarcodeFormat.EAN13);
        myformat.add(BarcodeFormat.EAN8);
        myformat.add(BarcodeFormat.CODE39);
        myformat.add(BarcodeFormat.CODE93);
        myformat.add(BarcodeFormat.CODE128);

        myformat.add(BarcodeFormat.CODABAR);


        mScannerView.setFormats(myformat);
        setContentView(mScannerView);                // Set the scanner view as the content view
       click = getIntent().getStringExtra("click");

       //conditions
if(click.equals("scanning_qrcode")) {
    BtWeight.barcodenumber.setText("");

}else if(click.equals("scannerinput")){
    ScanInput.beforescaned="null";
    ScanInput.barcodenumberscaninput.setText(" ");

}else if(click.equals("dispatch")){
    Dispatch.dispatchbarcodenumber.setText(" ");

}

//
    }

    @Override
    public void onResume() {
        super.onResume();
        mScannerView.setResultHandler(this); // Register ourselves as a handler for scan results.
        mScannerView.startCamera();          // Start camera on resume
    }

    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();           // Stop camera on pause
    }

    @Override
    public void handleResult(Result result) {



        // Do something with the result here
        Log.v("kkkk", result.getContents()); // Prints scan results
        Log.v("uuuu", result.getBarcodeFormat().getName()); // Prints the scan format (qrcode, pdf417 etc.)
     //res=result.getContents();
        pd = new ProgressDialog(Barcodescanner.this);
        pd.setMessage("Searching the Barcode..");
        pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pd.setIndeterminate(true);
        pd.setCancelable(false);
        pd.dismiss();
        pd.show();


//conditions
if(click.equals("scanning_qrcode")) {

    BtWeight.barcodenumber.setText(result.getContents());
    BtWeight.weight.setText("0");

}else if(click.equals("locationbarcodescan")){
    RmLocation.locationbarcodenumber.setText(result.getContents());
    RmLocation.firstlastdata="null";

}else if(click.equals("cartonbarcodescan")){
    RmLocation.cartonbarcodenumber.setText(result.getContents());
    RmLocation.secondlastdate="null";

}else if(click.equals("scannerinput")){
    ScanInput.barcodenumberscaninput.setText(result.getContents());

}else if(click.equals("fglocationbarcodescan")){

    FgLocation.fglocationbarcodenumber.setText(result.getContents());
    FgLocation.firstlastdata="null";

}else if(click.equals("fgcartonbarcodescan")){

    FgLocation.fgcartonbarcodenumber.setText(result.getContents());
    FgLocation.secondlastdate="null";

}else if(click.equals("dislocationbarcodescan")){
    DispencerList.dislocationbarcodenumber.setText(result.getContents());
    DispencerList.firstlastdata="null";

}else if(click.equals("discartonbarcodescan")){
    DispencerList.discartonbarcodenumber.setText(result.getContents());
    DispencerList.secondlastdate="null";
}else if(click.equals("dispatch")){
    Dispatch.dispatchbarcodenumber.setText(result.getContents());

}


//
        pd.dismiss();
            onBackPressed();

        // If you would like to resume scanning, call this method below:
        //mScannerView.resumeCameraPreview(this);
    }


    }



