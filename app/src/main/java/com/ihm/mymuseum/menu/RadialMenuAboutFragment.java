package com.ihm.mymuseum.menu;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.ihm.mymuseum.R;

/**
 * 
 * @author Arindam Nath
 *
 */
public class RadialMenuAboutFragment extends Fragment {
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view  = inflater.inflate(R.layout.layout_about, container, false);
		return view;
	}
}