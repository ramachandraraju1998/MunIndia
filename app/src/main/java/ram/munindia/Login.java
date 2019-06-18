package ram.munindia;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

public class Login extends AppCompatActivity  {

    EditText username,password;
    Button login;
    ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        username=findViewById(R.id.username);
        password=findViewById(R.id.password);
        login=(Button) findViewById(R.id.login);



        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               login();
            }
        });


    }

    private void login() {

//        String us= username.getText().toString();
//        String ps= password.getText().toString();
//
//        if(!us.equals("") || !ps.equals("")) {
//            if (us.equals("abcd") && ps.equals("1234")){

                pd = new ProgressDialog(Login.this);
                pd.setMessage("Validating ..");
                pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                pd.setIndeterminate(true);
                pd.setCancelable(false);
                pd.show();


                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent i = new Intent(Login.this,Dashboard.class);
                        pd.dismiss();
                        startActivity(i);
                    }
                }, 1000);
//
//            }else{
//                Toast.makeText(Login.this,"Please enter Correct Detais",Toast.LENGTH_LONG).show();
//            }
//
//        }else{
//
//            Toast.makeText(Login.this,"Please enter Username and Password",Toast.LENGTH_LONG).show();
//
//        }
    }
}
