package ram.munindia;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    static String url="http://www.schemaxtech.in/mun_india/web/";

TextView tv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv= (TextView) findViewById(R.id.tv);
        Animation myFadeInAnimation = AnimationUtils.loadAnimation(this, R.anim.zoomin);
        tv.startAnimation(myFadeInAnimation); //Set animation to your ImageView
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent login = new Intent(MainActivity.this, Login.class);
                startActivity(login);
            }
        }, 2000);



    }
}
