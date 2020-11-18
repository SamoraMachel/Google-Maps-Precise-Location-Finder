package com.example.googlemapsapi;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

public class MapsActivity extends Fragment implements OnMapReadyCallback{

    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private GoogleMap mMap;
    private boolean locationPermissionGranted;
    private LocationFinder finder = new LocationFinder(getContext());


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
//        SupportMapFragment mapFragment;
//        mapFragment = (SupportMapFragment) getSupportFragmentManager()
//                .findFragmentById(R.id.map);
//        mapFragment.getMapAsync(this);

        if (ActivityCompat.checkSelfPermission(getContext(),
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(getContext(),
                        Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {

            getLocationPermission();
            return;
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        inflate the layout
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.activity_maps, container, false);

        return root;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        finder = new LocationFinder(getContext());
        if(finder.canGetLocation()) {
            double latitude = finder.getLatitude();
            double longitude = finder.getLongitude();

            LatLng myLocation = new LatLng(latitude, longitude);
            LatLng Thika = new LatLng(-1.0461881, 37.0714562);
            LatLng Makuyu = new LatLng(-0.8361161, 37.2406453);

            PolylineOptions polylineOptions = new PolylineOptions()
                    .add(myLocation)
                    .add(Thika)
                    .add(Makuyu);
            Polyline polyline =  mMap.addPolyline(polylineOptions);


            mMap.addMarker(new MarkerOptions().position(myLocation).title("My Location"));
            mMap.addMarker(new MarkerOptions().position(Thika).title("Thika"));
            mMap.addMarker(new MarkerOptions().position(Makuyu).title("Muranga"));

            mMap.moveCamera(CameraUpdateFactory.newLatLng(myLocation));
            mMap.animateCamera(CameraUpdateFactory.zoomTo(10), 2000, null);
        } else {
            // Add a marker in Sydney and move the camera
            LatLng sydney = new LatLng(-34, 151);
            mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
            finder.showSettingsAlert();
        }


    }

    private void getLocationPermission() {
        if (ContextCompat.checkSelfPermission(getContext().getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            locationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        locationPermissionGranted = false;
        switch(requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION:
                if(grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    locationPermissionGranted = true;

                }
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        finder = new LocationFinder(getContext());;
//        if(finder.canGetLocation()) {
//            LatLng myLocation = new LatLng(finder.getLatitude(), finder.getLongitude());
//            mMap.addMarker(new MarkerOptions().position(myLocation).title("My Location"));
//            mMap.moveCamera(CameraUpdateFactory.newLatLng(myLocation));
//        } else {
//            finder.showSettingAlert();
//        }
    }

}