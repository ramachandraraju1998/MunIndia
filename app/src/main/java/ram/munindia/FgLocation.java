package ram.munindia;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import ram.munindia.validations.Validations;

public class FgLocation extends AppCompatActivity implements View.OnClickListener {

    ImageView fglocationbarcodescan,fgcartonbarcodescan,myimage_back,done_img;
    static EditText fglocationbarcodenumber,fgcartonbarcodenumber;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fg_location);


        fglocationbarcodescan=findViewById(R.id.fglocationbarcodescan);
        fgcartonbarcodescan=findViewById(R.id.fgcartonbarcodescan);
        myimage_back=findViewById(R.id.myimage_back);
        done_img=findViewById(R.id.done_img);

        fglocationbarcodescan.setOnClickListener(this);
        fgcartonbarcodescan.setOnClickListener(this);
        myimage_back.setOnClickListener(this);
        done_img.setOnClickListener(this);

        fglocationbarcodenumber=findViewById(R.id.fglocationbarcodenumber);
        fgcartonbarcodenumber=findViewById(R.id.fgcartonbarcodenumber);


        fglocationbarcodenumber.setLongClickable(false);
        fgcartonbarcodenumber.setLongClickable(false);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fglocationbarcodescan:
                if (Validations.hasActiveInternetConnection(FgLocation.this)) {
                    Intent barcodescanner = new Intent(FgLocation.this, Barcodescanner.class);
                    barcodescanner.putExtra("click","fglocationbarcodescan");
                    startActivity(barcodescanner);
                } else {
                    Toast.makeText(FgLocation.this,"please check internet connection",Toast.LENGTH_SHORT).show();
                }

                break;
            case R.id.fgcartonbarcodescan:

                if (Validations.hasActiveInternetConnection(FgLocation.this)) {
                    Intent barcodescanner = new Intent(FgLocation.this, Barcodescanner.class);
                    barcodescanner.putExtra("click","fgcartonbarcodescan");
                    startActivity(barcodescanner);
                } else {
                    Toast.makeText(FgLocation.this,"please check internet connection",Toast.LENGTH_SHORT).show();
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
