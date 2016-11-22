package com.ihm.mymuseum.settings;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.ihm.mymuseum.Main;
import com.ihm.mymuseum.R;
import com.ihm.mymuseum.Tools;


public class PrefCategorieFragment extends PreferenceFragment implements View.OnClickListener{

    public PrefCategorieFragment() {
        super();
    }

    //TODO: Remove paramters if useless
    public static PrefCategorieFragment newInstance(String param1, String param2) {
        PrefCategorieFragment fragment = new PrefCategorieFragment();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.categorie_fragment, container, false);

        ((Button)v.findViewById(R.id.btnEnfant)).setOnClickListener(this);
        ((Button)v.findViewById(R.id.btnAdulte)).setOnClickListener(this);

        return v;
    }

    public void onClick(View v){
        if(v instanceof Button){
            String value = "";

            switch (((Button)v).getId()){
                case R.id.btnEnfant:
                    value = getString(R.string.pref_value_child);
                    break;
                case R.id.btnAdulte:
                    value = getString(R.string.pref_value_adult);
                    break;
                default:
                    return;
            }

            Tools.setPreference(R.string.pref_category, value);
            listener.onLoadActivity(getActivity(), Main.class);
        }
    }

}
