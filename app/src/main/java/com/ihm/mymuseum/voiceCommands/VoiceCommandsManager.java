package com.ihm.mymuseum.voiceCommands;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.util.Log;

import com.ihm.mymuseum.R;
import com.ihm.mymuseum.speaker.Speaker;

import java.util.ArrayList;

/**
 * SpeechRecognition is not intended to use as Continues Speech Recognition.
 * This manager provides a continues speech recognition, using SpeechRecognition
 * and turn off the beep sound.
 *
 * Created by Coralie on 19/11/2016.
 */
public class VoiceCommandsManager {

    //VoiceCommandsActivity speechRecognizerActivity;

    protected AudioManager audioManager;
    protected SpeechRecognizer speechRecognizer;
    protected Intent speechRecognizerIntent;

    //private Speaker speaker;

    protected boolean isListening;
    private boolean isStreamSolo;
    private static boolean first=true;
    private boolean mute=true;

    private final static String TAG="VoiceCommandsManager";


    public VoiceCommandsManager(Context context, VoiceCommandsListener listener)
    {
        Log.i(TAG, " is instantiated." );

        //this.speechRecognizerActivity = (VoiceCommandsActivity) context;
        //speaker = new Speaker(this.speechRecognizerActivity);
        audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(context);
        speechRecognizer.setRecognitionListener(listener);
        speechRecognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, context.getPackageName());
        // Give instruction
        //speaker.speakOut(R.raw.martin_luther_king);
        startListening();

    }

    public void listenAgain() {
        if(isListening) {
            isListening = false;
            speechRecognizer.cancel();
            startListening();
        }
    }

    private void startListening()
    {
        if(!isListening)
        {
            isListening = true;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                // turn off beep sound
                if (!isStreamSolo && mute) {
                    audioManager.setStreamMute(AudioManager.STREAM_NOTIFICATION, true);
                    audioManager.setStreamMute(AudioManager.STREAM_ALARM, true);
                    audioManager.setStreamMute(AudioManager.STREAM_MUSIC, true);
                    audioManager.setStreamMute(AudioManager.STREAM_RING, true);
                    audioManager.setStreamMute(AudioManager.STREAM_SYSTEM, true);
                    isStreamSolo = true;
                }
            }
            speechRecognizer.startListening(speechRecognizerIntent);
        }
    }

    public void destroy() {
        isListening = false;
        Log.i(TAG,"Destroy");
        if (true) {
            Log.i(TAG,"Nomute");
            audioManager.setStreamMute(AudioManager.STREAM_NOTIFICATION, false);
            audioManager.setStreamMute(AudioManager.STREAM_ALARM, false);
            audioManager.setStreamMute(AudioManager.STREAM_MUSIC, false);
            audioManager.setStreamMute(AudioManager.STREAM_RING, false);
            audioManager.setStreamMute(AudioManager.STREAM_SYSTEM, false);
            isStreamSolo = true;
        }
        Log.d(TAG, "onDestroy");
        if (speechRecognizer != null) {
            speechRecognizer.stopListening();
            speechRecognizer.cancel();
            speechRecognizer.destroy();
            speechRecognizer = null;
        }

    }

    public boolean ismIsListening() {
        return isListening;
    }

    public void mute(boolean mute) {
        mute=mute;
    }

    public boolean isInMuteMode() {
        return mute;
    }





}

