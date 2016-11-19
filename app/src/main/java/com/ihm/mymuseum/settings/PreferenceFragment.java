package com.ihm.mymuseum.settings;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;


public class PreferenceFragment extends Fragment {

    //TODO: remove these arguments if useless
    protected static final String ARG_PARAM1 = "param1";
    protected static final String ARG_PARAM2 = "param2";

    protected String mParam1;
    protected String mParam2;

    protected OnLoadListener listener;

    public PreferenceFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnLoadListener) {
            listener = (OnLoadListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement OnLoadListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    public interface OnLoadListener {
        void onLoadFragment(Fragment f);
        void onLoadActivity(Context context, Class<?> c);
    }

}
