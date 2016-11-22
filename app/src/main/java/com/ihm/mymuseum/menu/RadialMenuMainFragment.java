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
 *
 */
public class RadialMenuMainFragment extends Fragment {

	private static final String PARAM_OEUVRE = "oeuvre";
	private Oeuvre oeuvre;

	private TextView tv;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view  = inflater.inflate(R.layout.layout_main, container, false);

		if(savedInstanceState != null){
			oeuvre = (Oeuvre)savedInstanceState.getSerializable(PARAM_OEUVRE);
		} else if(getArguments() != null){
			oeuvre = (Oeuvre)getArguments().getSerializable(PARAM_OEUVRE);
		} else {
			oeuvre = new Oeuvre();
		}

		tv = (TextView) view.findViewById(R.id.nameOeuvre);
		tv.setText(oeuvre.getNom());
		return view;
	}

	@Override
	public void onSaveInstanceState(Bundle savedInstance){
		savedInstance.putSerializable(PARAM_OEUVRE, oeuvre);
	}

	public static RadialMenuMainFragment newInstance(Oeuvre oeuvre) {
		RadialMenuMainFragment fragment = new RadialMenuMainFragment();
		Bundle args = new Bundle();
		args.putSerializable(PARAM_OEUVRE, oeuvre);
		fragment.setArguments(args);
		return fragment;
	}
}
