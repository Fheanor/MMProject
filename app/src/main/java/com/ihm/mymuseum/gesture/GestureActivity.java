package com.ihm.mymuseum.gesture;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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
import android.widget.Toast;

import com.ihm.mymuseum.Oeuvre;
import com.ihm.mymuseum.R;
import com.ihm.mymuseum.qrcode.QrCodeActivity;
import com.ihm.mymuseum.speaker.Speaker;
import com.ihm.mymuseum.voiceCommands.PermissionHandler;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.logging.Logger;

import static android.content.ContentValues.TAG;
import static com.ihm.mymuseum.R.id.gestures;
import static com.ihm.mymuseum.R.raw.gesture;

/**
 * Created by Julian on 19/11/2016.
 */

public class GestureActivity extends Activity implements GestureTimer.OnFinishedListener {

    private static final String PARAM_OEUVRE = "oeuvre";
    private Oeuvre oeuvre;

    private GestureTimer timer;
    private static final int MAX_TIME = 1000;

    private Gesture currentGesture;
    private GestureLibrary gLib;

    private Speaker speaker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        speaker = new Speaker(this);

        if(savedInstanceState != null){
            oeuvre = (Oeuvre)savedInstanceState.getSerializable(PARAM_OEUVRE);
        } else {
            Bundle b = getIntent().getExtras();
            if(b != null){
                oeuvre = (Oeuvre)b.getSerializable(PARAM_OEUVRE);
            }
        }

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_gesture);

        gLib = GestureLibraries.fromRawResource(this, gesture);
        if (!gLib.load()) {
            Log.w(TAG, "could not load gesture library");
            finish();
        }

        currentGesture = new Gesture();

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

        // one prediction needed
        if (predictions.size() > 0) {
            final Prediction prediction = predictions.get(0);
            // checking prediction
            Logger.getAnonymousLogger().info("PREDICTION : "+prediction.name);

            if (prediction.score < 10.0) {
                // and action
                runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(GestureActivity.this, prediction.name,
                                Toast.LENGTH_SHORT).show();
                    }
                });

                switch (prediction.name){
                    case "A":
                        speakOut("L'artiste."+oeuvre.getInfoArtiste());
                        break;
                    case "O":
                        speakOut("L'oeuvre."+oeuvre.getDescription());
                        break;
                    case "D":
                        speakOut("Audiodescription."+oeuvre.getAudiodescription());
                        break;
                    case "R":
                        speakOut("Retour.");
                        stopSpeak();
                        startActivity(new Intent(this, QrCodeActivity.class));
                        break;
                    default:
                        break;
                }

            }else{
                speakOut(speaker.readFile(R.raw.gesturemode));
            }
        }

        currentGesture = new Gesture();
    }

    @Override
    public void onBackPressed() {
        stopSpeak();
        finish();
    }


    public void speakOut(String informations) {
        stopSpeak();
        if(PermissionHandler.checkPermission(this,PermissionHandler.RECORD_AUDIO)) {
            speaker.speak(informations);
        }else {
            PermissionHandler.askForPermission(PermissionHandler.RECORD_AUDIO,this);
        }
    }

    public void stopSpeak(){
        speaker.stop();
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        speaker.destroy();
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstance){
        savedInstance.putSerializable(PARAM_OEUVRE, oeuvre);
    }

    public static Intent buildActivity(Context context, Serializable oeuvre){
        Intent intent = new Intent(context, GestureActivity.class);
        intent.putExtra(PARAM_OEUVRE, oeuvre);
        return intent;
    }
}
