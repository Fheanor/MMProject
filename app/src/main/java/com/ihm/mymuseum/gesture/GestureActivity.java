package com.ihm.mymuseum.gesture;

import android.app.Activity;
import android.gesture.Gesture;
import android.gesture.GestureLibraries;
import android.gesture.GestureLibrary;
import android.gesture.GestureOverlayView;
import android.gesture.GestureStroke;
import android.gesture.Prediction;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.Window;
import android.widget.ImageView;
import android.widget.Toast;

import com.ihm.mymuseum.Oeuvre;
import com.ihm.mymuseum.R;
import com.ihm.mymuseum.Tools;
import com.ihm.mymuseum.speaker.Speaker;
import com.ihm.mymuseum.voiceCommands.PermissionHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import static android.content.ContentValues.TAG;
import static com.ihm.mymuseum.R.id.gestures;
import static com.ihm.mymuseum.R.raw.gesture;

/**
 * Created by Julian on 19/11/2016.
 */

public class GestureActivity extends Activity implements GestureTimer.OnFinishedListener {

    private GestureTimer timer;

    private static final int MAX_TIME = 1000;

    private Gesture currentGesture;

    private GestureLibrary gLib;

    private ImageView img;

    private Speaker speaker;
    private List<Oeuvre> oeuvres;
    private Oeuvre oeuvre;
    private String informations;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        oeuvres = Tools.getOeuvres(this.getAssets(), "Oeuvres.xml");
        for(Oeuvre oeuvre : oeuvres) {
            if (oeuvre.getNom().equals(Tools.oeuvre)) {
                this.oeuvre = oeuvre;
            }
        }
        speaker = new Speaker(this);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_gesture);

        gLib = GestureLibraries.fromRawResource(this, gesture);
        if (!gLib.load()) {
            Log.w(TAG, "could not load gesture library");
            finish();
        }

        currentGesture = new Gesture();

        img = (ImageView)findViewById(R.id.imageView2);

        final GestureOverlayView gestureView = (GestureOverlayView) findViewById(gestures);

        gestureView.addOnGestureListener(new GestureOverlayView.OnGestureListener() {
            @Override
            public void onGestureStarted(GestureOverlayView overlay, MotionEvent event) {
                if(timer != null && !timer.isTimeFinished()){
                    timer.interrupt();
                }

                timer = new GestureTimer(MAX_TIME);
                timer.setOnFinishedListener(GestureActivity.this);
                timer.start();
            }

            @Override
            public void onGestureEnded(GestureOverlayView overlay, MotionEvent event) {

                for(GestureStroke gs : overlay.getGesture().getStrokes()){
                    currentGesture.addStroke(gs);
                }



                img.setImageBitmap(currentGesture.toBitmap(50,50,1, R.color.color_splash));
                timer.setIsGestureFinished(true);
            }

            @Override
            public void onGesture(GestureOverlayView overlay, MotionEvent event) {}

            @Override
            public void onGestureCancelled(GestureOverlayView overlay, MotionEvent event) {}
        });
    }

    @Override
    public void onFinished(){
        ArrayList<Prediction> predictions = gLib.recognize(currentGesture);

        Logger.getAnonymousLogger().info("GESTURE PREDICTION : " + predictions.size());
        /*
        for(Prediction p : predictions){
            Logger.getAnonymousLogger().info(p.name + " "+ p.score);
        }*/

        // one prediction needed
        if (predictions.size() > 0) {
            final Prediction prediction = predictions.get(0);
            // checking prediction
            Logger.getAnonymousLogger().info("PREDICTION : "+prediction.name);

            if (prediction.score > 1.0) {
                // and action
                runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(GestureActivity.this, prediction.name,
                                Toast.LENGTH_SHORT).show();
                    }
                });

            }

            switch (prediction.name){
                case "A":
                    informations = oeuvre.getInfoArtiste();
                    speakOut();
                    break;
                case "O":
                    informations = oeuvre.getDescription();
                    speakOut();
                    break;
                case "J":
                    informations = oeuvre.getAudiodescription();
                    speakOut();
                    break;
                case "Z":
                    stopSpeak();
                    finish();
                    break;
                default:
                    break;
            }
        }

        currentGesture = new Gesture();
        runOnUiThread(new Runnable() {
            public void run() {
                img.setImageBitmap(null);
            }
        });
    }

    @Override
    public void onBackPressed() {
        stopSpeak();
        finish();
    }


    public void speakOut() {
        stopSpeak();
        if(PermissionHandler.checkPermission(this,PermissionHandler.RECORD_AUDIO)) {
            speaker.speak(this.informations);
        }else {
            PermissionHandler.askForPermission(PermissionHandler.RECORD_AUDIO,this);
        }
    }

    public void stopSpeak(){
        speaker.stop();
    }
}
