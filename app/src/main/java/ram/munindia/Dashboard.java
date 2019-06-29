package ram.munindia;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;

import ram.munindia.validations.SessionManager;

public class Dashboard extends AppCompatActivity implements View.OnClickListener {
CardView grnapproval,rmdashboard,btweighing,rmlocation,scaninput,fgloaction,fgscan,dispatchlist,dispenserlocation,logout,dispatch;
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
//dispenser location
        grnapproval=findViewById(R.id.grnapproval);
        rmdashboard=findViewById(R.id.rmdashboard);
        btweighing=findViewById(R.id.btweighing);
        rmlocation=findViewById(R.id.rmlocation);
        scaninput=findViewById(R.id.scaninput);
        fgloaction=findViewById(R.id.fgloaction);
        fgscan=findViewById(R.id.fgscan);
        dispatchlist=findViewById(R.id.dispatchlist);
        dispenserlocation=findViewById(R.id.dispenserlocation);
        logout=findViewById(R.id.logout);
        dispatch=findViewById(R.id.dispatch);



        grnapproval.setOnClickListener(this);
        rmdashboard.setOnClickListener(this);
        btweighing.setOnClickListener(this);
        rmlocation.setOnClickListener(this);
        scaninput.setOnClickListener(this);
        fgloaction.setOnClickListener(this);
        fgscan.setOnClickListener(this);
        dispatch.setOnClickListener(this);
        dispatchlist.setOnClickListener(this);
        dispenserlocation.setOnClickListener(this);
        logout.setOnClickListener(this);



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
                Intent grnapproval = new Intent(Dashboard.this,GRNapproval.class);
                startActivity(grnapproval);

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
                Intent fglocation = new Intent(Dashboard.this,FgLocation.class);
                startActivity(fglocation);
                vibrate();

                break;
            case R.id.fgscan:
                vibrate();

                break;
            case R.id.dispatchlist:
                Intent dispatchlist = new Intent(Dashboard.this, DispatchList.class);
                startActivity(dispatchlist);
                vibrate();

                break;
            case R.id.dispenserlocation:
                Intent dispenserlocation = new Intent(Dashboard.this,DispencerList.class);
                startActivity(dispenserlocation);

                vibrate();

                break;

            case R.id.logout:

                SessionManager sm = new SessionManager(Dashboard.this);
                sm.logoutUser();

//                Intent logout = new Intent(Dashboard.this,MainActivity.class);
//                logout.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                startActivity(logout);

                        vibrate();

                    break;
            case R.id.dispatch:
                Intent dispatch = new Intent(Dashboard.this, Dispatch.class);
                startActivity(dispatch);
                vibrate();

                break;



        }
    }


    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)

                .setMessage("Do you want to Exit ")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        overridePendingTransition(R.anim.fadein, R.anim.fade_out);
                        finishAffinity();
                        System.exit(0);

                    }

                })
                .setNegativeButton("No", null)
                .show();
    }



}
