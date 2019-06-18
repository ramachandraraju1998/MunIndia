package ram.munindia;

import android.app.Dialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ram.munindia.validations.Validations;

public class ScanInput extends AppCompatActivity implements View.OnClickListener {

    static EditText barcodenumberscaninput;
    EditText date;
    Spinner shift,line,employee,barcodetype;
    ImageView scannerinput,myimage_back,done_img;
    ArrayAdapter<String> shifta,linea,employeea,barcodetypea;
    List<String >  shifts,lines,employees,barcodetypes;

    //data viewer
//    TextView barcodedataview,sizedataview,dataview;
LinearLayout dataviewerlayout;
String finaldata="";
    String bar="",size="",data="";

    //down list
    private NonScrollListView list;
    ArrayList<String>  listItems=new ArrayList<>();
    ArrayAdapter<String> adapter;


    //msp
    Map<String, List<String>> map = new HashMap<String, List<String>>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scan_input);
        mapss();

//        barcodedataview=findViewById(R.id.barcodedataview);
//        sizedataview=findViewById(R.id.sizedataview);
//        dataview=findViewById(R.id.dataview);
        dataviewerlayout=findViewById(R.id.dataviewerlayout);


       // list obj
        list = findViewById(R.id.list);



//down list adapter;
        adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, listItems);
        list.setAdapter(adapter);


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

        shifts.add("Shift-A");
        shifts.add("Shift-B");

        shifta = new ArrayAdapter<String>(
                this, R.layout.support_simple_spinner_dropdown_item, shifts);

        shifta.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        shift.setAdapter(shifta);

        // listApi();
        shifta.notifyDataSetChanged();
        // Toast.makeText(getBaseContext(), responseBody, Toast.LENGTH_SHORT).show();

//line adpter;

        // Initializing an ArrayAdapter
        lines.add("Line-1");
        lines.add("Line-2");
        linea = new ArrayAdapter<String>(
                this, R.layout.support_simple_spinner_dropdown_item, lines);

        linea.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        line.setAdapter(linea);

        // listApi();
        linea.notifyDataSetChanged();
        // Toast.makeText(getBaseContext(), responseBody, Toast.LENGTH_SHORT).show();

//shift employee;

        // Initializing an ArrayAdapter
        employees.add("Raju");
        employees.add("Mounica");
        employees.add("Pavan");
        employees.add("Suresh");
        employees.add("RamaKrishna");
        employeea = new ArrayAdapter<String>(
                this, R.layout.support_simple_spinner_dropdown_item, employees);

        employeea.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        employee.setAdapter(employeea);

        // listApi();
        employeea.notifyDataSetChanged();
        // Toast.makeText(getBaseContext(), responseBody, Toast.LENGTH_SHORT).show();

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

    private void mapss() {

        // create list one and store values

        List<String> valSetOne = new ArrayList<String>();

        valSetOne.add("Apple");

        valSetOne.add("Aeroplane");

        // create list two and store values

        List<String> valSetTwo = new ArrayList<String>();

        valSetTwo.add("Bat");

        valSetTwo.add("Banana");

        // create list three and store values

        List<String> valSetThree = new ArrayList<String>();

        valSetThree.add("Cat");

        valSetThree.add("Car");

        // put values into map

        map.put("A", valSetOne);

        map.put("B", valSetTwo);

        map.put("C", valSetThree);

        // iterate and display values

        System.out.println("Fetching Keys and corresponding [Multiple] Values n");
        Toast.makeText(ScanInput.this,map.entrySet().toString(),Toast.LENGTH_LONG).show();
        System.out.println(map.entrySet());

        for (Map.Entry<String, List<String>> entry : map.entrySet()) {

            String key = entry.getKey();

            List<String> values = entry.getValue();

            System.out.println("Key = " + key);

            System.out.println("Values = " + values + "n");

        }

    }


    @Override
    protected void onRestart() {
        super.onRestart();

    if(!barcodenumberscaninput.getText().toString().trim().isEmpty() || !barcodenumberscaninput.getText().toString().trim().equals("") ){
//alert dialog
        final Dialog dialog = new Dialog(ScanInput.this, R.style.PauseDialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.custom_dialog);

        TextView text = dialog.findViewById(R.id.text_dialog);
        text.setText("Scaned-"+barcodenumberscaninput.getText().toString());


        ImageView b = dialog.findViewById(R.id.b);

        Button addButton = dialog.findViewById(R.id.btn_add);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dataviewerlayout.setVisibility(View.VISIBLE);

             //   barcodedataview,sizedataview,dataview;

//
 bar=barcodenumberscaninput.getText().toString();
 size="XXL ";
 data="data ";
//                barcodedataview.setText(bar);
//                sizedataview.setText(size);
//                dataview.setText(data);




//adding to the data

               //  this line adds the data of your EditText and puts in your array
                finaldata= String.format("%1$-25s %2$13s %3$15s", bar, size, data);
                listItems.add(finaldata);
                // next thing you have to do is check if your adapter has changed
                adapter.notifyDataSetChanged();



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

        Toast.makeText(ScanInput.this,"Not Scaned Barcode",Toast.LENGTH_SHORT).show();
    }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.scannerinput:

                if (Validations.hasActiveInternetConnection(ScanInput.this)) {
                    Intent barcodescanner = new Intent(ScanInput.this, Barcodescanner.class);
                    barcodescanner.putExtra("click","scannerinput");
                    startActivity(barcodescanner);
                } else {
                    Toast.makeText(ScanInput.this,"please check internet connection",Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.myimage_back:
                finish();
                break;
            case R.id.done_img:
                // done();
                break;



        }
    }



//    public static String[] GetStringArray(ArrayList<String> arr) {
//
//        // declaration and initialise String Array
//        String str[] = new String[arr.size()];
//
//        // ArrayList to Array Conversion
//        for (int j = 0; j < arr.size(); j++) {
//
//            // Assign each value to String array
//            str[j] = arr.get(j);
//        }
//
//        return str;
//    }
}
