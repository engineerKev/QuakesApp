package com.example.quakes.quakes;

import android.app.Activity;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.os.AsyncTask;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import android.content.Intent;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

public class EarthquakeActivity extends Activity {
    private List<Earthquake> quakes;
    private ListView listView;
    private List<Earthquake> onScreenQuakes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_earthquake);

        listView = (ListView) findViewById(R.id.list);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                showMap(quakes.get(i));
            }
        });


        EarthquakeFinder finder = new EarthquakeFinder();
        finder.execute();

    }

    public void showMap(Earthquake earthquake) {
        Double eLat = earthquake.getGeo().getLatitude();
        Double eLong = earthquake.getGeo().getLongitude();
        Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                Uri.parse("geo:" + eLat + "," + eLong + "?q=" + eLat + "," +
                        eLong + "(" + earthquake.getProps().getLocation() + "\n" + "magnitude: " + earthquake.getProps().getMag() + ")")
        );
        startActivity(intent);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.earthquake, menu);
        return true;
    }

    private void handleEarthquakeList(final List<Earthquake> quakes) {
        this.quakes = quakes;

        if (onScreenQuakes == null) {
            onScreenQuakes = quakes;
        } else {
            Geometry oldGeo = onScreenQuakes.get(0).getGeo();
            Geometry newGeo = quakes.get(0).getGeo();
            if ((oldGeo.getLongitude().equals(newGeo.getLongitude()))
                    && (oldGeo.getLatitude().equals(newGeo.getLatitude()))) {
                Toast.makeText(getApplicationContext(), "No new earthquakes reported", Toast.LENGTH_LONG).show();
            } else {
                this.quakes.removeAll(onScreenQuakes);
                onScreenQuakes = quakes;

                //get titles location of new list items and show in toast but still just update the WHOLE view
                //or you could somehow change the color of old data, like make it fade out or something and keep the new data black
            }

        }

        listView.setAdapter(new EarthquakeAdapter(EarthquakeActivity.this, R.layout.list_item, quakes.toArray(new Earthquake[0])));

    }

    private void failedLoadingEarthquakes(final Exception e) {
        Log.e("Earthquakes", e.toString(), e);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(EarthquakeActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private class EarthquakeFinder extends AsyncTask<Void, Void, List<Earthquake>> {
        public static final String SERVER_URL = "http://earthquake.usgs.gov/earthquakes/feed/v1.0/summary/all_day.geojson";

        @Override
        protected List<Earthquake> doInBackground(Void... params) {
            try {
                //Create an HTTP client
                HttpClient client = new DefaultHttpClient();
                HttpGet get = new HttpGet(SERVER_URL);

                //perform  the request and check the status code
                HttpResponse response = client.execute(get);
                StatusLine statusLine = response.getStatusLine();
                if (statusLine.getStatusCode() == 200) {
                    HttpEntity entity = response.getEntity();
                    InputStream content = entity.getContent();

                    try {
                        //Read the server response and attempt to parse it as JSON
                        Reader reader = new InputStreamReader(content);
                        GsonBuilder gsonBuilder = new GsonBuilder();
                        Gson gson = gsonBuilder.create();
                        Features features = gson.fromJson(reader, Features.class);
                        content.close();
                        return features.getAllQuakes();
                    } catch (Exception ex) {
                        failedLoadingEarthquakes(ex);
                    }
                } else {
                    failedLoadingEarthquakes(new Exception(String.valueOf(statusLine.getStatusCode())));
                }
            } catch (Exception ex) {
                failedLoadingEarthquakes(ex);
            }
            return null;
        }

        @Override
        protected void onPostExecute(List<Earthquake> quakes) {
            super.onPostExecute(quakes);

            handleEarthquakeList(quakes);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_refresh) {
            EarthquakeFinder finder = new EarthquakeFinder();
            finder.execute();
        }
        return super.onOptionsItemSelected(item);
    }
}

