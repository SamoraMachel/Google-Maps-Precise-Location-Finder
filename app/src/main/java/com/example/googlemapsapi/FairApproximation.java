package com.example.googlemapsapi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.widget.TextView;

public class FairApproximation extends AppCompatActivity {
    ViewPager backgroundMap, frontMap;
    TextView distance, rate, charge;
    MapsActivity mapsActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fair_approximation);

        backgroundMap = (ViewPager) findViewById(R.id.mapHolder);
        frontMap = (ViewPager) findViewById(R.id.MainMapView);
        distance = (TextView) findViewById(R.id.distance);
        rate = (TextView) findViewById(R.id.rate);
        charge = (TextView) findViewById(R.id.charge);
        mapsActivity = new MapsActivity();

        final MapAdapter adapter = new MapAdapter(getSupportFragmentManager(), this);
        adapter.addFragment(mapsActivity);

        backgroundMap.setAdapter(adapter);
        frontMap.setAdapter(adapter);

        double distanceTxt = getDistance();
        int rateTxt = 100;
        double chargeTxt = distanceTxt * rateTxt;

        distance.setText(String.format("%s Km", Double.toString(distanceTxt)));
        rate.setText(String.format("%s per Km", Double.toString(rateTxt)));
        charge.setText(String.format("Ksh. %s", Double.toString(chargeTxt)));
    }

    private double getDistance() {
        LocationFinder finder = new LocationFinder(this);
        if(finder.canGetLocation()) {
            double latitude = finder.getLatitude();
            double longitude = finder.getLongitude();
            double ThikaLatitude = -1.0461881;
            double ThikaLongitude = 37.0714562;


            int radiusOfEarth = 6371;
            double degreeLat = degreesToRadian(ThikaLatitude - latitude);
            double degreeLong = degreesToRadian(ThikaLongitude - longitude);
            double a =
                    Math.sin(degreeLat/2) * Math.sin(degreeLat/2) +
                    Math.cos(degreesToRadian(ThikaLatitude)) * Math.cos(degreesToRadian(latitude)) *
                    Math.sin(degreeLong/2) * Math.sin(degreeLong/2);
            double c = Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
            double distanceInKm = radiusOfEarth * c;

            return distanceInKm;

        } else return 0;

    }

    private double degreesToRadian(double deg) {
        return deg * (Math.PI / 180);
    }
}