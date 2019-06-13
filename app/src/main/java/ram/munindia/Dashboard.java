package ram.munindia;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;

public class Dashboard extends AppCompatActivity implements View.OnClickListener {
CardView grnapproval,rmdashboard,btweighing,rmlocation,scaninput,fgloaction,fgscan,dispatch;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard);

        //cam permission
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.CAMERA)
                    != PackageManager.PERMISSION_GRANTED) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestPermissions(new String[]{Manifest.permission.CAMERA},
                            0);
                }

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant

                // return;
            }
        }

        grnapproval=findViewById(R.id.grnapproval);
        rmdashboard=findViewById(R.id.rmdashboard);
        btweighing=findViewById(R.id.btweighing);
        rmlocation=findViewById(R.id.rmlocation);
        scaninput=findViewById(R.id.scaninput);
        fgloaction=findViewById(R.id.fgloaction);
        fgscan=findViewById(R.id.fgscan);
        dispatch=findViewById(R.id.dispatch);

        grnapproval.setOnClickListener(this);
        rmdashboard.setOnClickListener(this);
        btweighing.setOnClickListener(this);
        rmlocation.setOnClickListener(this);
        scaninput.setOnClickListener(this);
        fgloaction.setOnClickListener(this);
        fgscan.setOnClickListener(this);
        dispatch.setOnClickListener(this);


    }
    public void vibrate() {
        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        // Vibrate for 500 milliseconds
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            v.vibrate(VibrationEffect.createOneShot(50, VibrationEffect.DEFAULT_AMPLITUDE));
        } else {
            //deprecated in API 26
            v.vibrate(50);
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.grnapproval:
                vibrate();

                break;
            case R.id.rmdashboard:
                vibrate();

                break;
            case R.id.btweighing:
                Intent btweighing = new Intent(Dashboard.this,BtWeight.class);
                startActivity(btweighing);
                vibrate();

                break;
            case R.id.rmlocation:
                Intent rmlocation = new Intent(Dashboard.this,RmLocation.class);
                startActivity(rmlocation);
                vibrate();

                break;
            case R.id.scaninput:
                Intent scaninput = new Intent(Dashboard.this,ScanInput.class);
                startActivity(scaninput);
                vibrate();

                break;
            case R.id.fgloaction:
                vibrate();

                break;
            case R.id.fgscan:
                vibrate();

                break;
            case R.id.dispatch:
                vibrate();

                break;


        }
    }
}
