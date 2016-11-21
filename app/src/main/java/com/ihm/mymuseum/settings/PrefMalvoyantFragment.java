package com.ihm.mymuseum.settings;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.ihm.mymuseum.R;
import com.ihm.mymuseum.Tools;


public class PrefMalvoyantFragment extends PreferenceFragment{

    public PrefMalvoyantFragment() {
        super();
    }

    //TODO: Remove paramters if useless
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
                Tools.setPreference(getActivity(), getString(R.string.pref_audio_mode),false);
                Fragment fg = PrefCategorieFragment.newInstance("","");
                listener.onLoadFragment(fg);
            }
        });

        return v;
    }

}
