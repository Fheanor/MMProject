package com.ihm.mymuseum.menu;


import java.util.ArrayList;
import java.util.List;

import com.ihm.mymuseum.Oeuvre;
import com.ihm.mymuseum.Tools;
import com.ihm.mymuseum.speaker.Speaker;
import com.ihm.mymuseum.voiceCommands.PermissionHandler;
import com.touchmenotapps.widget.radialmenu.menu.v2.RadialMenuItem;
import com.touchmenotapps.widget.radialmenu.menu.v2.RadialMenuRenderer;
import com.touchmenotapps.widget.radialmenu.menu.v2.RadialMenuRenderer.OnRadailMenuClick;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.widget.FrameLayout;

import com.ihm.mymuseum.R;

/**
 *
 */
public class RadialMenuActivity extends FragmentActivity {

	private Speaker speaker;
	private List<Oeuvre> oeuvres;
	private Oeuvre oeuvre;
	private String informations;

	//Variable declarations
	private RadialMenuRenderer mRenderer;
	private FrameLayout mHolderLayout;
	public RadialMenuItem menuOeuvreItem, menuArtisteItem, menuQRCodeItem;
	private ArrayList<RadialMenuItem> mMenuItems = new ArrayList<RadialMenuItem>(0);

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_holder);
		oeuvres = Tools.getOeuvres(this.getAssets(), "Oeuvres.xml");
		for(Oeuvre oeuvre : oeuvres) {
			if (oeuvre.getNom().equals(Tools.oeuvre)) {
				this.oeuvre = oeuvre;
			}
		}
		speaker = new Speaker(this);

		//Init the frame layout
		mHolderLayout = (FrameLayout) findViewById(R.id.fragment_container);

		// Init the Radial Menu and menu items
		mRenderer = new RadialMenuRenderer(mHolderLayout, true, 100, 200);
		menuOeuvreItem = new RadialMenuItem("Oeuvre", "Oeuvre");
		menuQRCodeItem = new RadialMenuItem("QRCode","QRCode");
		menuArtisteItem = new RadialMenuItem("Artiste","Artiste");

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
				getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
				getSupportFragmentManager().beginTransaction().replace(mHolderLayout.getId(), new RadialMenuOeuvreFragment()).commit();
				informations = oeuvre.getDescription();
				speakOut();
			}
		});

		menuArtisteItem.setOnRadialMenuClickListener(new OnRadailMenuClick() {
			@Override
			public void onRadailMenuClickedListener(String id) {
				//Can edit based on preference. Also can add animations here.
				getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
				getSupportFragmentManager().beginTransaction().replace(mHolderLayout.getId(), new RadialMenuOeuvreFragment()).commit();
				informations = oeuvre.getInfoArtiste();
				speakOut();
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
		getSupportFragmentManager().beginTransaction().replace(mHolderLayout.getId(), new RadialMenuMainFragment()).commit();
	}

	@Override
	public void onBackPressed() {
		stopSpeak();
		finish();
	}


	public void speakOut() {
		stopSpeak();
		if(PermissionHandler.checkPermission(this,PermissionHandler.RECORD_AUDIO)) {
			speaker.speak(this.informations);
		}else {
			PermissionHandler.askForPermission(PermissionHandler.RECORD_AUDIO,this);
		}
	}

	public void stopSpeak(){
		speaker.stop();
	}
}
