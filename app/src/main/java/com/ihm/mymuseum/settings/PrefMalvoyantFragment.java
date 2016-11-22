package com.ihm.mymuseum.settings;

import android.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.speech.SpeechRecognizer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.ihm.mymuseum.Main;
import com.ihm.mymuseum.R;
import com.ihm.mymuseum.Tools;
import com.ihm.mymuseum.speaker.Speaker;
import com.ihm.mymuseum.voiceCommands.PermissionHandler;
import com.ihm.mymuseum.voiceCommands.VoiceCommandsListener;
import com.ihm.mymuseum.voiceCommands.VoiceCommandsManager;

import java.util.ArrayList;
import java.util.logging.Logger;


public class PrefMalvoyantFragment extends PreferenceFragment{

    private VoiceCommandsListener speechListener;
    private VoiceCommandsManager speechManager;
    private Speaker speaker;

    private final int CHECK_CODE = 0x1;
    private final static String TAG = "PrefMalvoyantFragment";
    private boolean init = true;

    public PrefMalvoyantFragment() {
        super();
    }

    //TODO: Remove parameters if useless
    public static PrefMalvoyantFragment newInstance(String param1, String param2) {
        PrefMalvoyantFragment fragment = new PrefMalvoyantFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        // Start speech recognition
        speaker = new Speaker(getActivity());
        startSpeechRecognition();
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.malvoyant_fragment, container, false);

        ((Button)v.findViewById(R.id.btnQuit)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });

        ((Button)v.findViewById(R.id.btnNext)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopSpeechRecognition();
                speaker.destroy();
                Tools.setPreference(R.string.pref_audio_mode, false);
                Fragment fg = PrefCategorieFragment.newInstance("","");
                listener.onLoadFragment(fg);
            }
        });

        return v;
    }

    private void startSpeechRecognition() {
        if(PermissionHandler.checkPermission(getActivity(),PermissionHandler.RECORD_AUDIO)) {
            if (speechManager == null) {
                SetSpeechManager();
            } /*else if (!speechManager.ismIsListening()) {
                speechManager.destroy();
                SetSpeechManager();
            }*/
        }else {
            PermissionHandler.askForPermission(PermissionHandler.RECORD_AUDIO,getActivity());
        }
    }

    private void stopSpeechRecognition() {
        if(PermissionHandler.checkPermission(getActivity(),PermissionHandler.RECORD_AUDIO)) {
            if(speechManager != null) {
                speechManager.destroy();
                speechManager = null;
            }
        }else {
            PermissionHandler.askForPermission(PermissionHandler.RECORD_AUDIO,getActivity());
        }
    }

    private void SetSpeechManager() {
        if(speechManager==null) {
            speechManager = new VoiceCommandsManager(getActivity(),new VoiceCommandsListener() {

                @Override
                public void onResults(Bundle results) {
                    if(results == null) return;

                    if (!Tools.getBooleanFromPreference(R.string.pref_audio_mode,false)) {
                        setResults(results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION));
                        switch (this.result) {
                            case "mode audio":
                            case "malvoyants":
                            case "Malvoyant":
                            case "Malvoyants":
                            case "malvoyante":
                            case "malvoyantes":
                            case "Malvoyante":
                            case "Malvoyantes":
                                Log.i(TAG, "Keyword recognised : " + this.result);
                                // Set the configuration, Give the next instruction and listen again
                                Tools.setPreference(R.string.pref_audio_mode, true);
                                speakOut();
                                break;
                            default:
                                Log.i(TAG, "Try to config : " + this.result);
                                // Give instruction and listen again
                                speakOut();
                                break;
                        }
                    } else if (Tools.getStringFromPreference(R.string.pref_category, "").equals("")) {
                        setResults(results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION));
                        switch (this.result) {
                            case "enfant":
                            case "enfants":
                            case "Enfant":
                            case "Enfants":
                                Log.i(TAG, "Keyword recognised : " + this.result);
                                // Destroy SpeechRecognizer & Valid result
                                Tools.setPreference(R.string.pref_category, getString(R.string.pref_value_child));
                                stopSpeechRecognition();
                                speakOut();
                                listener.onLoadActivity(getActivity(), Main.class);
                                break;
                            case "adulte":
                            case "adultes":
                            case "Adulte":
                            case "Adultes":
                                Log.i(TAG, "Keyword recognised : " + this.result);
                                // Destroy SpeechRecognizer & Valid result
                                Logger.getAnonymousLogger().info("Adult setted");
                                Tools.setPreference(R.string.pref_category, getString(R.string.pref_value_adult));
                                stopSpeechRecognition();
                                speakOut();
                                listener.onLoadActivity(getActivity(), Main.class);
                                break;
                            default:
                                Log.i(TAG, "Try to select user : " + this.result);
                                // Give instruction and listen again
                                speakOut();
                                break;
                        }
                    } else {
                        stopSpeechRecognition();
                    }
                }

                @Override
                public synchronized void onError (int error) {
                    giveFirstInstructions();

                    if (error == SpeechRecognizer.ERROR_RECOGNIZER_BUSY) {
                        ArrayList<String> errorList = new ArrayList<String>(1);
                        errorList.add("ERROR RECOGNIZER BUSY");
                        setResults(errorList);
                        return;
                    }
                    if (error == SpeechRecognizer.ERROR_NO_MATCH) {
                        setResults(null);
                    }
                    if (error == SpeechRecognizer.ERROR_NETWORK) {
                        ArrayList<String> errorList = new ArrayList<String>(1);
                        errorList.add("STOPPED LISTENING");
                        setResults(errorList);
                    }

                    Log.d(TAG, "error = " + error);
                    /*
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            speechManager.listenAgain();
                        }
                    }, 100);*/

                }
            });
        }
    }


    public void speakOut() {
        if(!speaker.isSpeaking()) {
            stopSpeechRecognition();
            String humanCategory = Tools.getStringFromPreference(R.string.pref_category, "");
            Logger.getAnonymousLogger().info("humanCategory: " + humanCategory);

            if(!Tools.getBooleanFromPreference(R.string.pref_audio_mode, false)) {
                speaker.speak(speaker.readFile(R.raw.configuration));
                try {
                    Thread.currentThread().sleep(3300);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                startSpeechRecognition();
            }else if(humanCategory.equals("")) {
                speaker.speak(speaker.readFile(R.raw.utilisateur));
                try {
                    Thread.currentThread().sleep(6100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                startSpeechRecognition();
            }else if(humanCategory.equals(getString(R.string.pref_value_adult))) {
                speaker.speak(speaker.readFile(R.raw.adulte));
                try {
                    Thread.currentThread().sleep(3500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }else if(humanCategory.equals(getString(R.string.pref_value_child))) {
                speaker.speak(speaker.readFile(R.raw.enfant));
                try {
                    Thread.currentThread().sleep(3500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } else{
                Log.i(TAG,"SpeakOut error !");
                startSpeechRecognition();
            }
        }
    }

    public void giveFirstInstructions(){
        if(init) {
            stopSpeechRecognition();
            init = false;
            speaker.speak(speaker.readFile(R.raw.configuration));
            try {
                Thread.currentThread().sleep(3300);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            startSpeechRecognition();
        }else {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    speechManager.listenAgain();
                }
            }, 100);
        }
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        speaker.destroy();
    }

}
