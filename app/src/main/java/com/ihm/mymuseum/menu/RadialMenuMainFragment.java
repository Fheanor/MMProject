package com.ihm.mymuseum.menu;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ihm.mymuseum.R;
import com.ihm.mymuseum.Tools;

/**
 *
 */
public class RadialMenuMainFragment extends Fragment{

	private TextView tv;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view  = inflater.inflate(R.layout.layout_main, container, false);
		tv = (TextView) view.findViewById(R.id.nameOeuvre);
		tv.setText(Tools.oeuvre.getNom());
		return view;
	}
}
