package ram.munindia;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import ram.munindia.ModalandAdatpters.DispatchAdapter;
import ram.munindia.ModalandAdatpters.DispatchModel;

public class DispatchList extends AppCompatActivity implements View.OnClickListener {

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dispatchlist);

     //   li.setVisibility(View.VISIBLE);


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
        salesdata.add("SAL-10101");
        salesdata.add("SAL-10120");

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

        pd = new ProgressDialog(DispatchList.this);
        pd.setMessage("Fetching Invoices ..");
        pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pd.setIndeterminate(true);
        pd.setCancelable(false);
     //   pd.show();




    }

    private void getList() {


for(int i=0;i<=5;i++) {

    list.add(new DispatchModel("rajuu", "brown", "XXL", "3", "15"));

}
runOnUiThread(new Runnable() {
    @Override
    public void run() {
        int size=list.size();
        adapter = new DispatchAdapter(list, R.layout.dispatchsingle, getApplicationContext());
        recyclerview.setAdapter(adapter);
        total.setText("Total="+size);

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
}
