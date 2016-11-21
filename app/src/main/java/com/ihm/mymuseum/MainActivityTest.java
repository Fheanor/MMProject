package com.ihm.mymuseum;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.ihm.mymuseum.voiceCommands.VoiceCommandsActivity;

public class MainActivityTest extends AppCompatActivity {

    Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_test);

        btn = (Button)findViewById(R.id.button);

        btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent appel = new Intent(MainActivityTest.this, VoiceCommandsActivity.class);
                startActivity(appel);
            }
        });
    }
}
