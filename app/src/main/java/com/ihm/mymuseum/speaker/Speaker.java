package com.ihm.mymuseum.speaker;

import android.content.Context;
import android.content.res.Resources;
import android.speech.tts.TextToSpeech;
import android.util.Log;

import java.io.InputStream;
import java.util.Locale;

/**
 * Created by Coralie on 20/11/2016.
 */
public class Speaker implements TextToSpeech.OnInitListener {

    private static Locale language = Locale.FRANCE;
    private final int SHORT_DURATION = 1000;
    private static TextToSpeech tts;
    private boolean isReady = false;

    private Context context;
    private String TAG = "Speaker";

    public Speaker(Context context){
        this.context = context;
        tts = new TextToSpeech(context, this);
        tts.setPitch(0.8f);
        tts.setSpeechRate(1.2f);
    }

    @Override
    public void onInit(int status) {
        if(status == TextToSpeech.SUCCESS){
            tts.setLanguage(language);
            isReady = true;
        } else{
            isReady = false;
        }
    }

    public void speak(String text){
        if(isReady) {
            tts.speak(text, TextToSpeech.QUEUE_ADD, null, null);
        }
    }

    public void setSpeedRate(float speechrate) {
        tts.setSpeechRate(speechrate);
    }

    public void setPitchRate(float pitchrate) {
        tts.setPitch(pitchrate);
    }

    public boolean isSpeaking() {
        return tts.isSpeaking();
    }

    public void pause(int duration){
        tts.playSilentUtterance(duration, TextToSpeech.QUEUE_ADD, null);
    }

    public void stop() {
        tts.stop();
    }

    public void destroy() {
        tts.shutdown();
    }


    public void speakOut(int rawfile) {
        this.speak(readFile(rawfile));
        this.pause(SHORT_DURATION);
    }

    public String readFile(int rawfile) {
        String result = new String();
        Log.i(TAG,"error");
        try {
            Resources res = context.getResources();
            InputStream input_stream = res.openRawResource(rawfile);

            byte[] b = new byte[input_stream.available()];
            input_stream.read(b);
            result = new String(b);
        } catch (Exception e) {
            Log.e("readFile", e.getMessage());
        }
        return result;
    }
}
