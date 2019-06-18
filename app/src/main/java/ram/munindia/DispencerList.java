package ram.munindia;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import ram.munindia.validations.Validations;

public class DispencerList extends AppCompatActivity implements View.OnClickListener {

    ImageView dislocationbarcodescan, discartonbarcodescan, myimage_back, done_img;
    static EditText dislocationbarcodenumber, discartonbarcodenumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dispencer_list);


        dislocationbarcodescan = findViewById(R.id.dislocationbarcodescan);
        discartonbarcodescan = findViewById(R.id.discartonbarcodescan);
        myimage_back = findViewById(R.id.myimage_back);
        done_img = findViewById(R.id.done_img);

        dislocationbarcodescan.setOnClickListener(this);
        discartonbarcodescan.setOnClickListener(this);
        myimage_back.setOnClickListener(this);
        done_img.setOnClickListener(this);
        dislocationbarcodenumber = findViewById(R.id.dislocationbarcodenumber);
        discartonbarcodenumber = findViewById(R.id.discartonbarcodenumber);

        dislocationbarcodenumber.setLongClickable(false);
        discartonbarcodenumber.setLongClickable(false);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.dislocationbarcodescan:
                if (Validations.hasActiveInternetConnection(DispencerList.this)) {
                    Intent barcodescanner = new Intent(DispencerList.this, Barcodescanner.class);
                    barcodescanner.putExtra("click","dislocationbarcodescan");
                    startActivity(barcodescanner);
                } else {
                    Toast.makeText(DispencerList.this,"please check internet connection",Toast.LENGTH_SHORT).show();
                }

                break;
            case R.id.discartonbarcodescan:

                if (Validations.hasActiveInternetConnection(DispencerList.this)) {
                    Intent barcodescanner = new Intent(DispencerList.this, Barcodescanner.class);
                    barcodescanner.putExtra("click","discartonbarcodescan");
                    startActivity(barcodescanner);
                } else {
                    Toast.makeText(DispencerList.this,"please check internet connection",Toast.LENGTH_SHORT).show();
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
