package com.example.quakes.quakes;

/**
 * Created by kevinruiz on 6/29/14.
 */
public class Earthquake {
    public Properties properties;
    public Geometry geometry;

    public Earthquake() {

    }
    public Properties getProps(){
        return properties;
    }
    public Geometry getGeo(){
        return geometry;
    }
    @Override
    public String toString() {
        return  properties.getType() +": "+
                properties.getLocation()+"\n"+
                "magnitude: " + properties.getMag() +"\n"+
                "latitude: " + geometry.getLatitude() +"\n"+
                "longitude: " + geometry.getLongitude();
    }
}
