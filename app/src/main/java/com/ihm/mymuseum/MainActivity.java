package com.ihm.mymuseum;

import android.content.Intent;
import android.app.ListActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;

import com.ihm.mymuseum.menu.RadialMenuActivity;

public class MainActivity extends ListActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setListAdapter(new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, getResources()
                .getStringArray(R.array.main_menu)));

        startActivity(new Intent(MainActivity.this,
                RadialMenuActivity.class));

      //  setContentView(R.layout.activity_main);
    }
}
