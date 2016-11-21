package com.ihm.mymuseum.voiceCommands;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;

import com.ihm.mymuseum.R;
import com.ihm.mymuseum.speaker.Speaker;


/**
 * Created by Coralie on 20/11/2016.
 */
public class VoiceCommandsActivity extends Activity {

    private VoiceCommandsManager speechManager;
    private Speaker speaker;

    private String TAG = "ConfigActivity";
    private final int CHECK_CODE = 0x1;
    private final int SHORT_DURATION = 1000000;

    private boolean isMalvoyant = false;
    private boolean isMalentendant = false;
    private boolean isEnfant = false;
    private boolean configSelects = false;
    private boolean userSelects = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config);
        // Start speech recognition
        speaker = new Speaker(this);
        startSpeechRecognition();

    }

    private void checkTTS(){
        Intent check = new Intent();
        check.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
        startActivityForResult(check, CHECK_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case CHECK_CODE: {
                if (resultCode == TextToSpeech.Engine.CHECK_VOICE_DATA_PASS) {
                    speaker = new Speaker(this);
                    Log.i("A","speaker");
                } else {
                    Log.i("A","instal");
                    Intent install = new Intent();
                    install.setAction(TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
                    startActivity(install);
                }
            }
            default:
                break;
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        checkTTS();
    }

    @Override
    protected void onStop() {
        speaker.destroy();
        super.onStop();
    }

    @Override
    protected void onPause() {
        if(speechManager!=null) {
            speechManager.destroy();
            speechManager=null;
        }
        super.onPause();
    }

    public void speakOut() {
        if(!speaker.isSpeaking()) {
            stopSpeechRecognition();
            int rawfile;
            if(!isConfigSelected()){
                speaker.speak(speaker.readFile(R.raw.configuration));
                try {
                    Thread.currentThread().sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                startSpeechRecognition();
            }else if(!isUserSelected()){
                speaker.speak(speaker.readFile(R.raw.utilisateur));
                try {
                    Thread.currentThread().sleep(6100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                startSpeechRecognition();
            }else if (!isEnfant){
                speaker.speak(speaker.readFile(R.raw.adulte));
                try {
                    Thread.currentThread().sleep(3500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }else if(isEnfant){
                speaker.speak(speaker.readFile(R.raw.enfant));
                try {
                    Thread.currentThread().sleep(3500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } else{
                speaker.speak(speaker.readFile(R.raw.martin_luther_king));
                try {
                    Thread.currentThread().sleep(10000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void SetSpeechListener() {
        speechManager=new VoiceCommandsManager(this);
    }

    public void startSpeechRecognition(){
        if(PermissionHandler.checkPermission(this,PermissionHandler.RECORD_AUDIO)) {
            if (speechManager == null) {
                SetSpeechListener();
            } else if (!speechManager.ismIsListening()) {
                speechManager.destroy();
                SetSpeechListener();
            }
        }else {
            PermissionHandler.askForPermission(PermissionHandler.RECORD_AUDIO,this);
        }
    }

    public void stopSpeechRecognition(){
        if(PermissionHandler.checkPermission(this,PermissionHandler.RECORD_AUDIO)) {
            if(speechManager!=null) {
                speechManager.destroy();
                speechManager = null;
            }
        }else {
            PermissionHandler.askForPermission(PermissionHandler.RECORD_AUDIO,this);
        }
    }

    public void setMalvoyant(boolean b){
        this.isMalvoyant = b;
        this.configSelects = true;
    }

    public boolean isMalvoyant(){
        return this.isMalvoyant;
    }

    public void setEnfant(boolean b){
        this.isEnfant = b;
        this.userSelects = true;
    }

    public boolean isEnfant(){
        return this.isEnfant;
    }

    public void setMalentendant(boolean b){
        this.isMalentendant = b;
        this.configSelects = true;
    }

    public boolean isMalentendant(){
        return this.isMalentendant;
    }

    public boolean isConfigSelected(){
        return this.configSelects;
    }

    public boolean isUserSelected(){
        return this.userSelects;
    }


}
