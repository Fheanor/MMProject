package com.ihm.mymuseum.qrcode;

import android.app.Activity;
import android.os.Bundle;
import android.view.ViewGroup;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;
import com.ihm.mymuseum.Oeuvre;
import com.ihm.mymuseum.R;
import com.ihm.mymuseum.Tools;
import com.ihm.mymuseum.gesture.GestureActivity;
import com.ihm.mymuseum.menu.RadialMenuActivity;
import com.ihm.mymuseum.speaker.Speaker;
import com.ihm.mymuseum.voiceCommands.PermissionHandler;

import java.util.ArrayList;
import java.util.List;

public class QrCodeActivity extends Activity implements ZXingScannerView.ResultHandler {

    public static final String FLASH_STATE = "FLASH_STATE";
    public static final String AUTO_FOCUS_STATE = "AUTO_FOCUS_STATE";
    public static final String SELECTED_FORMATS = "SELECTED_FORMATS";
    private static final String CAMERA_ID = "CAMERA_ID";

    private Speaker speaker;
    private List<Oeuvre> oeuvres;

    private ZXingScannerView mScannerView;
    private boolean mFlash;
    private boolean mAutoFocus;
    private ArrayList<Integer> mSelectedIndices;
    private int mCameraId = -1; //Default camera : Rear_camera

    public static final String MESSAGE = "message";
    public static final int CODE_OK = 1;
    public static final int CODE_REQUEST = Tools.randomInt();

    @Override
    public void onCreate(Bundle state) {
        super.onCreate(state);
        oeuvres = Tools.getOeuvres("Oeuvres.xml");
        if(state != null) {
            mFlash = state.getBoolean(FLASH_STATE, false);
            mAutoFocus = state.getBoolean(AUTO_FOCUS_STATE, true);
            mSelectedIndices = state.getIntegerArrayList(SELECTED_FORMATS);
            mCameraId = state.getInt(CAMERA_ID, -1);
        } else {
            mFlash = false;
            mAutoFocus = true;
            mSelectedIndices = null;
            mCameraId = -1;
        }

        speaker = new Speaker(this);

        setContentView(R.layout.qrcode_activity);

        ViewGroup contentFrame = (ViewGroup) findViewById(R.id.content_frame_qr);
        mScannerView = new ZXingScannerView(this);
        setupFormats();
        contentFrame.addView(mScannerView);
    }

    @Override
    public void onResume() {
        super.onResume();
        mScannerView.setResultHandler(this);
        mScannerView.startCamera(mCameraId);
        mScannerView.setFlash(mFlash);
        mScannerView.setAutoFocus(mAutoFocus);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(FLASH_STATE, mFlash);
        outState.putBoolean(AUTO_FOCUS_STATE, mAutoFocus);
        outState.putIntegerArrayList(SELECTED_FORMATS, mSelectedIndices);
        outState.putInt(CAMERA_ID, mCameraId);
    }

    @Override
    public void handleResult(Result rawResult) {
        Oeuvre oeuvre = null;

        String result = rawResult.getText();
        for(Oeuvre o : oeuvres) {
            if (o.getNom().equals(result)) {
                oeuvre = o;
                break;
            }
        }

        if(oeuvre == null) { //Do something to inform that QrCode is wrong
            finish();
            return;
        }

        if(!Tools.getBooleanFromPreference(R.string.pref_audio_mode, false)) {
            startActivity(RadialMenuActivity.buildActivity(this, oeuvre));
        }else{
            speakOut(oeuvre.getNom());
            speakOut(speaker.readFile(R.raw.gesturemode));
            startActivity(GestureActivity.buildActivity(this, oeuvre));
        }
    }

    public void setupFormats() {
        List<BarcodeFormat> formats = new ArrayList<BarcodeFormat>();
        if(mSelectedIndices == null || mSelectedIndices.isEmpty()) {
            mSelectedIndices = new ArrayList<Integer>();
            for(int i = 0; i < ZXingScannerView.ALL_FORMATS.size(); i++) {
                mSelectedIndices.add(i);
            }
        }

        for(int index : mSelectedIndices) {
            formats.add(ZXingScannerView.ALL_FORMATS.get(index));
        }
        if(mScannerView != null) {
            mScannerView.setFormats(formats);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();
    }

    public void speakOut(String informations) {
        if(PermissionHandler.checkPermission(this,PermissionHandler.RECORD_AUDIO)) {
            speaker.speak(informations);
        }else {
            PermissionHandler.askForPermission(PermissionHandler.RECORD_AUDIO,this);
        }
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        speaker.destroy();
    }
}
