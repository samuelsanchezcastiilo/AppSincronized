package com.apps.poultryapp.Login;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.apps.poultryapp.Login.Login.Data.SessionPref;
import com.apps.poultryapp.Login.Login.View.Login;
import com.apps.poultryapp.R;

public class Splash extends AppCompatActivity {
    private final int DURACION_SPLASH = 3000; // 3 segundos
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        //this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        final  boolean log = SessionPref.get(getApplicationContext()).isLoggedIn();
        System.out.println(log);



        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //log =true;
                if (log == true){
                    startActivity(new Intent(Splash.this, Home.class));
                    finish();
                }else {
                    Intent intent = new Intent(Splash.this, Login.class);
                    startActivity(intent);
                    finish();
                }

            }
        },DURACION_SPLASH);
    }
}
