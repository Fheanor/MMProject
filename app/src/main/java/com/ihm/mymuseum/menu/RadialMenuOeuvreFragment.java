package com.ihm.mymuseum.menu;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ihm.mymuseum.Oeuvre;
import com.ihm.mymuseum.R;

/**
 * Created by Coralie on 22/11/2016.
 */
public class RadialMenuOeuvreFragment extends Fragment{

	private static final String PARAM_OEUVRE = "oeuvre";
	private static final String PARAM_INFO = "currentInfo";
	private Oeuvre oeuvre;
	private String info;

	private TextView tv;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view  = inflater.inflate(R.layout.layout_oeuvre, container, false);

		if(savedInstanceState != null){
			oeuvre = (Oeuvre)savedInstanceState.getSerializable(PARAM_OEUVRE);
			info = savedInstanceState.getString(PARAM_INFO);
		} else if(getArguments() != null){
			oeuvre = (Oeuvre)getArguments().getSerializable(PARAM_OEUVRE);
			info = getArguments().getString(PARAM_INFO);
		} else {
			oeuvre = new Oeuvre();
		}

		tv = (TextView) view.findViewById(R.id.TextViewOeuvre);
		tv.setText(oeuvre.toString() + info);
		return view;
	}

	@Override
	public void onSaveInstanceState(Bundle savedInstance){
		savedInstance.putSerializable(PARAM_OEUVRE, oeuvre);
		savedInstance.putString(PARAM_INFO, info);
	}

	public static RadialMenuOeuvreFragment newInstance(Oeuvre oeuvre, String info) {
		RadialMenuOeuvreFragment fragment = new RadialMenuOeuvreFragment();
		Bundle args = new Bundle();
		args.putSerializable(PARAM_OEUVRE, oeuvre);
		args.putString(PARAM_INFO, info);
		fragment.setArguments(args);
		return fragment;
	}




}
