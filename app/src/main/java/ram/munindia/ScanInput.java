package ram.munindia;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import ram.munindia.validations.Validations;

public class ScanInput extends AppCompatActivity {

   static EditText barcodenumber;
    EditText date;
    Spinner shift,line,employee,barcodetype;
    ImageView scannerinput;
    ArrayAdapter<String> shifta,linea,employeea,barcodetypea;
    List<String >  shifts,lines,employees,barcodetypes;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scan_input);

        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        String formattedDate = df.format(c);

        date=findViewById(R.id.date);
        barcodenumber=findViewById(R.id.barcodenumber);
        shift=findViewById(R.id.shift);
        line=findViewById(R.id.line);
        employee=findViewById(R.id.employee);
        barcodetype=findViewById(R.id.barcodetype);
        scannerinput=findViewById(R.id.scannerinput);


        date.setLongClickable(false);
        barcodenumber.setLongClickable(false);
        //setting date;
        date.setText(formattedDate.toString());

        //scanner
        scannerinput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Validations.hasActiveInternetConnection(ScanInput.this)) {
                    Intent barcodescanner = new Intent(ScanInput.this, Barcodescanner.class);
                    barcodescanner.putExtra("click","scannerinput");
                    startActivity(barcodescanner);
                } else {
                    Toast.makeText(ScanInput.this,"please check internet connection",Toast.LENGTH_SHORT).show();
                }
            }
        });


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
