package com.example.quakes.quakes;

/**
 * Created by kevinruiz on 6/29/14.
 */
import java.util.ArrayList;

public class Geometry {
    public ArrayList<Double> coordinates;

    public Double getLatitude(){
        return coordinates.get(1);
    }

    public Double getLongitude(){
        return coordinates.get(0);
    }

}
