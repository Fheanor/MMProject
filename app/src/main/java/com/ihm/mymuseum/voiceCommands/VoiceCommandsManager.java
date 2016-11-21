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

    VoiceCommandsActivity speechRecognizerActivity;

    protected AudioManager audioManager;
    protected SpeechRecognizer speechRecognizer;
    protected Intent speechRecognizerIntent;

    private Speaker speaker;

    protected boolean isListening;
    private boolean isStreamSolo;
    private static boolean first=true;
    private boolean mute=true;

    private final static String TAG="SpeechRecognizerManager";


    public VoiceCommandsManager(Context context)
    {
        Log.i(TAG, " is instantiated." );

        this.speechRecognizerActivity = (VoiceCommandsActivity) context;
        speaker = new Speaker(this.speechRecognizerActivity);
        audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(context);
        speechRecognizer.setRecognitionListener(new SpeechRecognitionListener());
        speechRecognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, context.getPackageName());
        // Give instruction
        speaker.speakOut(R.raw.martin_luther_king);
        startListening();

    }

    public void listenAgain()
    {
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
        Log.i(TAG,"Here");
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

    public void mute(boolean mute)
    {
        mute=mute;
    }

    public boolean isInMuteMode()
    {
        return mute;
    }




    /**
     * Created by Coralie on 19/11/2016.
     */
    protected class SpeechRecognitionListener implements RecognitionListener
    {

        private String result;
        private final String NO_RESULT = "NO_RESULT";


        public void setResults(ArrayList<String> results) {
            if(results!=null && results.size()>0)
            {
                result = results.get(0);
            }
            else
                result = NO_RESULT;
        }

        public String getResults(){
            return result;
        }

        @Override
        public void onBeginningOfSpeech() {}

        @Override
        public void onBufferReceived(byte[] buffer) {}

        @Override
        public void onEndOfSpeech() {}

        @Override
        public synchronized void onError(int error)
        {
            if(error==SpeechRecognizer.ERROR_RECOGNIZER_BUSY) {
                ArrayList<String> errorList=new ArrayList<String>(1);
                errorList.add("ERROR RECOGNIZER BUSY");
                setResults(errorList);
                return;
            }
            if(error==SpeechRecognizer.ERROR_NO_MATCH) {
                setResults(null);
            }
            if(error==SpeechRecognizer.ERROR_NETWORK) {
                ArrayList<String> errorList=new ArrayList<String>(1);
                errorList.add("STOPPED LISTENING");
                setResults(errorList);
            }
            Log.d(TAG, "error = " + error);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    listenAgain();
                }
            },100);

            if(first){
                Log.i(TAG,"HERE");
                speechRecognizerActivity.speakOut();
                first = false;
            }
        }

        @Override
        public void onEvent(int eventType, Bundle params) {}

        @Override
        public void onPartialResults(Bundle partialResults) {}

        @Override
        public void onReadyForSpeech(Bundle params) {}

        @Override
        public void onResults(Bundle results)
        {
            if(results!=null && speechRecognizerActivity.isConfigSelected() && speechRecognizerActivity.isUserSelected()) {
                speechRecognizerActivity.stopSpeechRecognition();
            }else if(results!=null && speechRecognizerActivity.isConfigSelected() && !speechRecognizerActivity.isUserSelected()) {
                setResults(results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION));
                switch (this.result) {
                    case "enfant":
                    case "enfants":
                    case "Enfant":
                    case "Enfants":
                        Log.i(TAG, "Keyword recognised : " + this.result);
                        // Destroy SpeechRecognizer & Valid result
                        speechRecognizerActivity.setEnfant(true);
                        speechRecognizerActivity.stopSpeechRecognition();
                        speechRecognizerActivity.speakOut();
                        break;
                    case "adulte":
                    case "adultes":
                    case "Adulte":
                    case "Adultes":
                        Log.i(TAG, "Keyword recognised : " + this.result);
                        // Destroy SpeechRecognizer & Valid result
                        speechRecognizerActivity.setEnfant(false);
                        speechRecognizerActivity.stopSpeechRecognition();
                        speechRecognizerActivity.speakOut();
                        break;
                    default:
                        Log.i(TAG, "Try to select user : " + this.result);
                        // Give instruction and listen again
                        speechRecognizerActivity.speakOut();
                        break;
                }
            }else if(results!=null && !speechRecognizerActivity.isConfigSelected()) {
                setResults(results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION));
                switch (this.result) {
                    case "malvoyant":
                    case "malvoyants":
                    case "Malvoyant":
                    case "Malvoyants":
                    case "malvoyante":
                    case "malvoyantes":
                    case "Malvoyante":
                    case "Malvoyantes":
                        Log.i(TAG, "Keyword recognised : " + this.result);
                        // Set the configuration, Give the next instruction and listen again
                        speechRecognizerActivity.setMalvoyant(true);
                        speechRecognizerActivity.speakOut();
                        break;
                    default:
                        Log.i(TAG, "Try to config : " + this.result);
                        // Give instruction and listen again
                        speechRecognizerActivity.speakOut();
                        break;
                }

            }


        }

        @Override
        public void onRmsChanged(float rmsdB) {}

    }
}

