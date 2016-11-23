package com.ihm.mymuseum;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;

import java.util.Timer;
import java.util.TimerTask;

import static com.ihm.mymuseum.settings.PreferenceActivity.EXTRA_INIT;

public class SplashScreenActivity extends AppCompatActivity {

    public static final int CODE_OK = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.splash_screen);

        new Timer().schedule(new TimerTask(){
            @Override
            public void run(){
                Intent intent = new Intent();
                intent.putExtra(EXTRA_INIT, true);
                setResult(CODE_OK, intent);
                finish();
            }
        }, 1000);

        Tools.setPreferenceParameters(getApplicationContext(), getAssets());
    }

}
