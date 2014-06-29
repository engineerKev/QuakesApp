package com.example.quakes.quakes;

/**
 * Created by kevinruiz on 6/29/14.
 */
public class Properties {
    public double mag;
    public String place;
    public String type;

    public String getLocation(){
        return place;
    }
    public double getMag(){
        return mag;
    }
    public String getType(){
        return type;
    }
}
