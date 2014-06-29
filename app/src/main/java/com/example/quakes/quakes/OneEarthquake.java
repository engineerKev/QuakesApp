package com.example.quakes.quakes;

import android.app.ListActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

/**
 * Created by kevinruiz on 6/29/14.
 */
public class OneEarthquake extends ListActivity{
    String[] items = { "this", "is", "a", "really","silly", "list" };
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_earthquake);
        setListAdapter(new ArrayAdapter<String>(this, android.R.layout.activity_list_item, items));
    }


    @Override
    protected void onListItemClick(ListView l, View v, int position, long id){
        super.onListItemClick(l,v,position,id);
        String text = " position:" + position + " " + items[position];
    }

}
