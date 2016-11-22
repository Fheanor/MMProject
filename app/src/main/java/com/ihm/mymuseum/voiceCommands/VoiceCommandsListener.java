package com.ihm.mymuseum.voiceCommands;

/**
 * Created by Coralie on 22/11/2016.
 */

import android.os.Bundle;
import android.os.Handler;
import android.speech.RecognitionListener;
import android.speech.SpeechRecognizer;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by Coralie on 19/11/2016.
 */
public abstract class VoiceCommandsListener implements RecognitionListener
{

    public String result;
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
    public abstract void onError(int error);
    /*{
        if(error== SpeechRecognizer.ERROR_RECOGNIZER_BUSY) {
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
    }*/

    @Override
    public void onEvent(int eventType, Bundle params) {}

    @Override
    public void onPartialResults(Bundle partialResults) {}

    @Override
    public void onReadyForSpeech(Bundle params) {}

    @Override
    public abstract void onResults(Bundle results);
                /*
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

            }*/




    @Override
    public void onRmsChanged(float rmsdB) {}

}
