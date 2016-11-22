package com.ihm.mymuseum.menu;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.ihm.mymuseum.Main;
import com.ihm.mymuseum.Oeuvre;
import com.ihm.mymuseum.R;
import com.ihm.mymuseum.Tools;
import com.ihm.mymuseum.gesture.GestureActivity;
import com.ihm.mymuseum.speaker.Speaker;
import com.ihm.mymuseum.voiceCommands.PermissionHandler;

import java.util.List;

/**
 * Created by Coralie on 22/11/2016.
 */
public class RadialMenuOeuvreFragment extends Fragment{

	private List<Oeuvre> oeuvres;
	private TextView tv;


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view  = inflater.inflate(R.layout.layout_oeuvre, container, false);
		oeuvres = Tools.getOeuvres(getActivity().getAssets(), "Oeuvres.xml");
		tv = (TextView) view.findViewById(R.id.TextViewOeuvre);
		String msg = Tools.oeuvre;
		for(Oeuvre oeuvre : oeuvres) {
			if (oeuvre.getNom().equals(msg)) {
				tv.setText(oeuvre.toString());
			}
		}
		return view;
	}




}
