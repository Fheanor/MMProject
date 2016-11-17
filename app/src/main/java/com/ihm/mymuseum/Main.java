package com.ihm.mymuseum;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.ihm.mymuseum.qrcode.QrCodeActivity;

import java.util.List;

public class Main extends Activity {
    private static final int CAMERA_PERMISSION = 1;
    private Class<?> classActivity;

    private List<Oeuvre> oeuvres;

    private TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar2);
        //setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchActivity(QrCodeActivity.class);
            }
        });
        oeuvres = Tools.getOeuvres(getAssets(), "Oeuvres.xml");

        tv = (TextView) findViewById(R.id.showPrefTxt);

        Button btn = (Button) findViewById(R.id.btnPref);
        btn.setText("Show pref");
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Boolean m = Tools.getPreferrence(Main.this.getApplicationContext()).getBoolean("Malvoyant", false);
                Tools.setPreference(Main.this.getApplicationContext(), "Malvoyant", !m);
                tv.setText(tv.getText() + "\n" + "Malvoyant: " +
                        String.valueOf(Tools.getPreferrence(Main.this.getApplicationContext()).getBoolean("Malvoyant", false)));
            }
        });



    }

    public void launchActivity(Class<?> cActivity) { //TODO
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            classActivity = cActivity;
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION);
        } else {
            Intent intent = new Intent(this, cActivity);
            startActivityForResult(intent, QrCodeActivity.CODE_REQUEST);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,  String permissions[], int[] grantResults) { //TODO
        switch (requestCode) {
            case CAMERA_PERMISSION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if(classActivity != null) {
                        Intent intent = new Intent(this, classActivity);
                        startActivityForResult(intent, QrCodeActivity.CODE_REQUEST);
                    }
                } else {
                    Toast.makeText(this, "Please grant camera permission to use the QR Scanner", Toast.LENGTH_SHORT).show();
                }
                return;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if (requestCode == QrCodeActivity.CODE_REQUEST) {
            if(resultCode == QrCodeActivity.CODE_OK){
                String msg = data.getStringExtra(QrCodeActivity.MESSAGE);
                TextView tv = (TextView) findViewById(R.id.textView);

                for(Oeuvre oeuvre : oeuvres) {
                    if (oeuvre.getNom().equals(msg)) {
                        tv.setText(oeuvre.toString());
                    }
                }
            }
        }
    }

}
