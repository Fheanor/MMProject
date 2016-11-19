package com.ihm.mymuseum.settings;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.ihm.mymuseum.R;
import com.ihm.mymuseum.SplashScreenActivity;

import java.util.Stack;
import java.util.logging.Logger;

public class PreferenceActivity extends Activity implements PreferenceFragment.OnLoadListener {

    private boolean isInitialized = false;
    public static final String EXTRA_INIT = "init";

    private Stack<Fragment> fragmentStack = new Stack<>();

    private Logger log = Logger.getAnonymousLogger();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preference);

        if(savedInstanceState != null) isInitialized = savedInstanceState.getBoolean(EXTRA_INIT, false);
        if (!isInitialized) {
            isInitialized = true;
            startActivity(new Intent(this, SplashScreenActivity.class));
        }

        Fragment fg = PrefMalvoyantFragment.newInstance("","");
        onLoadFragment(fg);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(EXTRA_INIT, isInitialized);
    }

    public void onLoadFragment(Fragment f){
        fragmentStack.push(f);
        getFragmentManager().beginTransaction().replace(R.id.layoutFragment, f).commit();
    }

    public void onLoadActivity(Context context, Class<?> c){
        Logger.getAnonymousLogger().info("loadActivity");
        startActivity(new Intent(context, c));
        finish();
    }


    @Override
    public void onBackPressed(){
        fragmentStack.pop();
        if (fragmentStack.size() == 0) {
            super.onBackPressed();
        } else {
            getFragmentManager().beginTransaction().replace(R.id.layoutFragment, fragmentStack.peek()).commit();
        }
    }

}
