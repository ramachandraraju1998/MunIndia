package ram.munindia;

import android.app.Dialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

import java.util.ArrayList;
import java.util.List;

import ram.munindia.validations.Validations;

import static ram.munindia.ScanInput.barcodenumberscaninput;

public class Dispatch extends AppCompatActivity implements View.OnClickListener {
    Spinner saleslist,barcodetype;
    ArrayAdapter<String> salesadapter,barcodetypea;
    List<String> salesdata,barcodetypes;
    ImageView myimage_back,done_img,scannerinput;
    static EditText dispatchbarcodenumber;
    LinearLayout dataviewerlayout;
//data viewer
    TextView barcodedataview,sizedataview,dataview;
    String bar="",size="",data="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dispatch);

        myimage_back=findViewById(R.id.myimage_back);
        done_img=findViewById(R.id.done_img);
        dispatchbarcodenumber=findViewById(R.id.dispatchbarcodenumber);
        dataviewerlayout=findViewById(R.id.dataviewerlayout);


        //
        barcodedataview=findViewById(R.id.barcodedataview);
        sizedataview=findViewById(R.id.sizedataview);
        dataview=findViewById(R.id.dataview);

        saleslist =findViewById(R.id.saleslist);
        barcodetype=findViewById(R.id.barcodetype);
        scannerinput=findViewById(R.id.scannerinput);
        scannerinput.setOnClickListener(this);

        salesdata= new ArrayList<>();

        salesdata.add("select sales Order");
        salesdata.add("SAL-10101");
        salesdata.add("SAL-10120");

        salesadapter = new ArrayAdapter<String>(
                this, R.layout.support_simple_spinner_dropdown_item, salesdata);

        salesadapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        saleslist.setAdapter(salesadapter);

        // listApi();
        salesadapter.notifyDataSetChanged();


        barcodetypes =new ArrayList<>();
        barcodetypes.add("Select BarcodeType");


//barcodetype adpter;

        // Initializing an ArrayAdapter
        barcodetypes.add("cell");
        barcodetypes.add("carton");
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
}
