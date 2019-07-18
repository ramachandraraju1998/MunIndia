package ram.munindia;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import ram.munindia.validations.SessionManager;
import ram.munindia.validations.Validations;

public class Login extends AppCompatActivity {

    EditText username, password;
    Button login;
    ProgressDialog pd;
    CheckBox checkbox;
    static String web="http://192.168.0.8/mun_india/web/";
    private SessionManager session;
    String us,ps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        //session
        // Session Manager
        session = new SessionManager(getApplicationContext());

        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        checkbox = findViewById(R.id.checkbox);
        login = (Button) findViewById(R.id.login);




        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(Validations.hasActiveInternetConnection(Login.this)) {
                    pd = new ProgressDialog(Login.this);
                    pd.setMessage("Validating ..");
                    pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                    pd.setIndeterminate(true);
                    pd.setCancelable(false);
                    pd.show();

                    login();
                }else{
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            // Stuff that updates the UI
                            Toast.makeText(getBaseContext(), "PleaseCheck Internet Connection", Toast.LENGTH_SHORT).show();
                        }
                    });

                }

            }
        });


        //direct login
        if(session.isLoggedIn()) {
            Intent i = new Intent(getApplicationContext(),Dashboard.class);
            startActivity(i);
            // finish();

        }


    }

    private void login() {

         us = username.getText().toString();
         ps = password.getText().toString();




        OkHttpClient client = new OkHttpClient();


        RequestBody formBody = new FormBody.Builder()

                .add("username", us)

                .add("password", ps)

                .build();
        Request request = new Request.Builder()
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .url(web+"index.php?r=restapi/user/login")
                .post(formBody)
                .build();


        client.newCall(request).enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {
                pd.dismiss();


                e.printStackTrace();

                System.out.println("onFailur="+e);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        Toast.makeText(getBaseContext(), "Failed", Toast.LENGTH_SHORT).show();
                    }
                });

                //pd.dismiss();
            }

            @Override
            public void onResponse(okhttp3.Call call, final okhttp3.Response response) throws IOException {
                // pd.dismiss();

                if (!response.isSuccessful()) {

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            pd.dismiss();
                            Toast.makeText(getBaseContext(), "No Responce", Toast.LENGTH_SHORT).show();

                        }
                    });


                    throw new IOException("Unexpected code " + response);

                } else {
                     pd.dismiss();
                    Log.d("result", response.toString());
                    String responseBody = response.body().string();
                    final JSONObject obj;
                    try {
                        obj = new JSONObject(responseBody);
                        if (obj.getString("success").equals("true")) {
                            JSONObject data= obj.getJSONObject("data");


                            SharedPreferences.Editor ss = getSharedPreferences("Login", MODE_PRIVATE).edit();
                            ss.putString("data",data.toString());
                            ss.putString("access_token",data.getString("auth_key"));
                            ss.commit();

                            //session creating
                            if(checkbox.isChecked()) {
                                session.createLoginSession(us, ps);
                            }

                            Intent i = new Intent(Login.this, Dashboard.class);
                            pd.dismiss();
                            startActivity(i);


                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {

                                        Toast.makeText(getBaseContext(), "Login Success", Toast.LENGTH_SHORT).show();

                                    }
                                });


                        } else {


                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                      //  login.setVisibility(View.GONE);
                                        // Stuff that updates the UI
                                        Toast.makeText(getBaseContext(), "Please Check Username & Password", Toast.LENGTH_SHORT).show();

                                    }
                                });




                        }
                    }catch (JSONException e) {
                        e.printStackTrace();
                    }

                }






            }
        });
    }


    public static void alert(String data, Context context){

        final android.app.AlertDialog.Builder builder1;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder1 = new android.app.AlertDialog.Builder(context, android.R.style.Theme_DeviceDefault_Dialog_Alert);
        } else {
            builder1 = new android.app.AlertDialog.Builder(context);
        }
        //builder1.setTitle(title);
        builder1.setMessage(data);
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                "Ok",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });


        android.app.AlertDialog alert11 = builder1.create();
        alert11.show();
    }


    public static void showDialog(Activity activity, String msg, Boolean status) {
        final Dialog dialog = new Dialog(activity, R.style.PauseDialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.custom_dialog_ok);

        TextView text = dialog.findViewById(R.id.text_dialog);
        text.setText(msg);

        ImageView b = dialog.findViewById(R.id.b);
        if(status==false){
            b.setVisibility(View.GONE);
        }else{
            b.setVisibility(View.VISIBLE);
        }

        Button dialogButton = dialog.findViewById(R.id.btn_dialog);
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
            }
        });
        dialog.show();
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
