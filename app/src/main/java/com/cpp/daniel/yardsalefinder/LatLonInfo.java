package com.cpp.daniel.yardsalefinder;

import static java.lang.Double.parseDouble;

/**
 * Created by Daniel on 7/16/2017.
 */

public class LatLonInfo {
    LatLonInfo(String lat,String lon){

        latitude= parseDouble(lat);
        longitude = parseDouble(lon);
        getLatitude();
        getLongitude();


    }



    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    private double latitude;
    private double longitude;




//
//    public String getLatitude() {
//        return latitude;
//    }
//
//    public void setLatitude(String latitude) {
//        this.latitude = latitude;
//    }
//
//    public String getLongitude() {
//        return Longitude;
//    }
//
//    public void setLongitude(String longitude) {
//        Longitude = longitude;
//    }
}
