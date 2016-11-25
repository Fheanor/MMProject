package com.ihm.mymuseum.menu;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.widget.FrameLayout;

import com.ihm.mymuseum.Oeuvre;
import com.ihm.mymuseum.R;
import com.ihm.mymuseum.speaker.Speaker;
import com.ihm.mymuseum.voiceCommands.PermissionHandler;
import com.touchmenotapps.widget.radialmenu.menu.v2.RadialMenuItem;
import com.touchmenotapps.widget.radialmenu.menu.v2.RadialMenuRenderer;
import com.touchmenotapps.widget.radialmenu.menu.v2.RadialMenuRenderer.OnRadailMenuClick;

import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 */
public class RadialMenuActivity extends FragmentActivity {

	private static final String TAG = "RadialMenuActivity";

	private static final String PARAM_OEUVRE = "oeuvre";
	private Oeuvre oeuvre;

	private Speaker speaker;

	//Variable declarations
	private RadialMenuRenderer mRenderer;
	private FrameLayout mHolderLayout;
	public RadialMenuItem menuOeuvreItem, menuArtisteItem, menuQRCodeItem;
	private ArrayList<RadialMenuItem> mMenuItems = new ArrayList<RadialMenuItem>(0);

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_holder);
		speaker = new Speaker(this);
		Bundle b;

		if(savedInstanceState != null){
			oeuvre = (Oeuvre)savedInstanceState.getSerializable(PARAM_OEUVRE);
		} else if( (b = getIntent().getExtras()) != null){
			oeuvre = (Oeuvre)b.getSerializable(PARAM_OEUVRE);
		} else {
			oeuvre = new Oeuvre();
		}

		//Init the frame layout
		mHolderLayout = (FrameLayout) findViewById(R.id.fragment_container);

		// Init the Radial Menu and menu items
		mRenderer = new RadialMenuRenderer(mHolderLayout, true, 100, 200);
		menuOeuvreItem = new RadialMenuItem("Oeuvre", "Oeuvre");
		menuQRCodeItem = new RadialMenuItem("QRCode","QRCode");
		menuArtisteItem = new RadialMenuItem("Artiste","Artiste");
		mRenderer.setMenuBackgroundColor(R.color.circular_menu_bg);

		//Add the menu Items
		mMenuItems.add(menuOeuvreItem);
		mMenuItems.add(menuArtisteItem);
		mMenuItems.add(menuQRCodeItem);

		mRenderer.setRadialMenuContent(mMenuItems);
		mHolderLayout.addView(mRenderer.renderView());

		//Handle the menu item interactions

		menuOeuvreItem.setOnRadialMenuClickListener(new OnRadailMenuClick() {
			@Override
			public void onRadailMenuClickedListener(String id) {
				//Can edit based on preference. Also can add animations here.
				String info = oeuvre.getDescription();
				getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
				getSupportFragmentManager().beginTransaction().replace(mHolderLayout.getId(), RadialMenuOeuvreFragment.newInstance(oeuvre, info)).commit();
				speakOut(info);
			}
		});

		menuArtisteItem.setOnRadialMenuClickListener(new OnRadailMenuClick() {
			@Override
			public void onRadailMenuClickedListener(String id) {
				//Can edit based on preference. Also can add animations here.
				String info = oeuvre.getInfoArtiste();
				getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
				getSupportFragmentManager().beginTransaction().replace(mHolderLayout.getId(), RadialMenuOeuvreFragment.newInstance(oeuvre, info)).commit();
				speakOut(info);
			}
		});

		menuQRCodeItem.setOnRadialMenuClickListener(new OnRadailMenuClick() {
			@Override
			public void onRadailMenuClickedListener(String id) {
				stopSpeak();
				finish();
			}
		});

	}
	
	@Override
	protected void onResume() {
		super.onResume();
		//Init with home fragment
		getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
		getSupportFragmentManager().beginTransaction().replace(mHolderLayout.getId(), RadialMenuMainFragment.newInstance(oeuvre)).commit();
	}

	@Override
	public void onBackPressed() {
		stopSpeak();
		finish();
	}


	public void speakOut(String informations) {
		stopSpeak();
		if(PermissionHandler.checkPermission(this,PermissionHandler.RECORD_AUDIO)) {
			speaker.speak(informations);
		}else {
			PermissionHandler.askForPermission(PermissionHandler.RECORD_AUDIO,this);
		}
	}

	public void stopSpeak(){
		speaker.stop();
	}

	@Override
	public void onDestroy(){
		super.onDestroy();
		speaker.stop();
		speaker.destroy();
	}

	@Override
	public void onPause(){
		super.onPause();
		Log.i(TAG,"PAUSE");
	}

	@Override
	public void onStop(){
		super.onStop();
		Log.i(TAG,"STOP");
	}

	@Override
	public void onSaveInstanceState(Bundle savedInstance){
		savedInstance.putSerializable(PARAM_OEUVRE, oeuvre);
	}

	public static Intent buildActivity(Context context, Serializable oeuvre){
		Intent intent = new Intent(context, RadialMenuActivity.class);
		intent.putExtra(PARAM_OEUVRE, oeuvre);
		return intent;
	}

}
