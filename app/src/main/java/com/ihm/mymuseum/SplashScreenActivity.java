package com.ihm.mymuseum;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import static com.ihm.mymuseum.settings.PreferenceActivity.EXTRA_INIT;

public class SplashScreenActivity extends AppCompatActivity {

    private Typeface font;
    private TextView tv1;

    public static final int RESULT = Tools.randomInt();
    public static final int CODE_OK = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.splash_screen);
        tv1 = (TextView) findViewById(R.id.textView1);

        /*font = Typeface.createFromAsset(getAssets(), "fonts/OpenSans.ttf");
        tv1.setTypeface(font);*/

        ((Button)findViewById(R.id.button)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra(EXTRA_INIT, true);
                setResult(CODE_OK, intent);
                finish();
            }
        });

        Tools.setContext(getApplicationContext());
    }

}
