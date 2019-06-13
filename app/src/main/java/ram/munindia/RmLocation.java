package ram.munindia;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import ram.munindia.validations.Validations;

public class RmLocation extends AppCompatActivity implements View.OnClickListener {

    ImageView locationbarcodescan,cartonbarcodescan,myimage_back,done_img;
    static  EditText locationbarcodenumber,cartonbarcodenumber;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rm_location);

        locationbarcodescan=findViewById(R.id.locationbarcodescan);
        cartonbarcodescan=findViewById(R.id.cartonbarcodescan);
        myimage_back=findViewById(R.id.myimage_back);
        done_img=findViewById(R.id.done_img);

        locationbarcodescan.setOnClickListener(this);
        cartonbarcodescan.setOnClickListener(this);
        myimage_back.setOnClickListener(this);
        done_img.setOnClickListener(this);

        locationbarcodenumber=findViewById(R.id.locationbarcodenumber);
        cartonbarcodenumber=findViewById(R.id.cartonbarcodenumber);


        locationbarcodenumber.setLongClickable(false);
        cartonbarcodenumber.setLongClickable(false);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.locationbarcodescan:
                if (Validations.hasActiveInternetConnection(RmLocation.this)) {
                    Intent barcodescanner = new Intent(RmLocation.this, Barcodescanner.class);
                    barcodescanner.putExtra("click","locationbarcodescan");
                    startActivity(barcodescanner);
                } else {
                    Toast.makeText(RmLocation.this,"please check internet connection",Toast.LENGTH_SHORT).show();
                }

                break;
            case R.id.cartonbarcodescan:

                if (Validations.hasActiveInternetConnection(RmLocation.this)) {
                    Intent barcodescanner = new Intent(RmLocation.this, Barcodescanner.class);
                    barcodescanner.putExtra("click","cartonbarcodescan");
                    startActivity(barcodescanner);
                } else {
                    Toast.makeText(RmLocation.this,"please check internet connection",Toast.LENGTH_SHORT).show();
                }

                break;

            case R.id.done_img:
//                done();
                break;

            case R.id.myimage_back:

                finish();
                break;

        }
    }
}
