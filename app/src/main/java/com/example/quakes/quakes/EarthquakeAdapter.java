package com.example.quakes.quakes;

/**
 * Created by kevinruiz on 6/29/14.
 */

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.view.LayoutInflater;
import android.widget.TextView;

public class EarthquakeAdapter extends ArrayAdapter<Earthquake> {
    private final Context context;
    private final Earthquake[] earthquakes;

    public EarthquakeAdapter(Context context, int resource, Earthquake[] earthquakes) {
        super(context, resource, earthquakes);
        this.context = context;
        this.earthquakes = earthquakes;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View oneQuake = inflater.inflate(R.layout.list_item, parent, false);
        TextView textView = (TextView) oneQuake.findViewById(R.id.title);
        textView.setText(earthquakes[position].toString());
        return oneQuake;
    }

}
