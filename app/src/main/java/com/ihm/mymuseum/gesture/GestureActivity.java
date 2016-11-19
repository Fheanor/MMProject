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

import com.ihm.mymuseum.R;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import static android.content.ContentValues.TAG;
import static com.ihm.mymuseum.R.raw.gesture;

/**
 * Created by Julian on 19/11/2016.
 */

public class GestureActivity extends Activity implements GestureTimer.OnFinishedListener {

    private GestureTimer timer;

    private static final int MAX_TIME = 1000;

    private List<Gesture> gestures = new ArrayList<>();
    private Gesture currentGesture;

    private GestureLibrary gLib;

    private ImageView img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_gesture);

        gLib = GestureLibraries.fromRawResource(this, gesture);
        if (!gLib.load()) {
            Log.w(TAG, "could not load gesture library");
            finish();
        }

        img = (ImageView)findViewById(R.id.imageView2);

        final GestureOverlayView gestureView = (GestureOverlayView) findViewById(R.id.gestures);

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
                gestures.add(overlay.getGesture());

                currentGesture = new Gesture();
                for(Gesture g : gestures){
                    for(GestureStroke gs : g.getStrokes()){
                        currentGesture.addStroke(gs);
                    }
                }

                img.setImageBitmap(currentGesture.toBitmap(50,50,1, R.color.color_splash));
            }

            @Override
            public void onGesture(GestureOverlayView overlay, MotionEvent event) {}

            @Override
            public void onGestureCancelled(GestureOverlayView overlay, MotionEvent event) {}
        });
    }

    @Override
    public void onFinished(){
        gestures.clear();
        runOnUiThread(new Runnable() {
            public void run() {
                img.setImageBitmap(null);
            }
        });
        ArrayList<Prediction> predictions = gLib.recognize(currentGesture);

        Logger.getAnonymousLogger().info("Perform gesture");

        // one prediction needed
        if (predictions.size() > 0) {
            final Prediction prediction = predictions.get(0);
            // checking prediction

            if (prediction.score > 1.0) {
                // and action
                runOnUiThread(new Runnable() {
                    public void run() {

                        img.setImageBitmap(null);
                        Toast.makeText(GestureActivity.this, prediction.name,
                                Toast.LENGTH_SHORT).show();
                    }
                });

            }
        }
    }
}
