package com.pentateuch.watersupply.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class LocationHelper implements LocationListener, AsyncTaskListener<Double,Integer,List<Address>> {

    private Context context;
    private LocationUpdateListener listener;
    private LocationManager locationManager;


    public LocationHelper(Context context, LocationUpdateListener listener) {
        this.context = context;
        this.listener = listener;
        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
    }

    public void findCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        } else if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER))
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 500, 0, this);
            else if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER))
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 500, 0, this);
        } else {
            NotifyDialog dialog = new NotifyDialog(context);
            dialog.setClickListener(new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    context.startActivity(intent);
                }
            });
            dialog.show("Enable Location Setting", "Ok");
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        final double lan = location.getLongitude();
        final double lat = location.getLatitude();
        locationManager.removeUpdates(this);
        AsyncTaskHelper<Double,Integer,List<Address>> helper = new AsyncTaskHelper<>(this);
        helper.execute(lat,lan);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public List<Address> onBeginExecution(Double[] doubles) {
        Geocoder geocoder = new Geocoder(context, Locale.ENGLISH);
        List<Address> addresses = new ArrayList<>();
        try {
            addresses =  geocoder.getFromLocation(doubles[0], doubles[1], 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return addresses;
    }

    @Override
    public void onPreExecution() {

    }

    @Override
    public void onPostExecution(List<Address> addresses) {
        if(addresses.size() != 0) {
            Address address = addresses.get(0);
            String detail = String.format("%s \n%s \n%s \n%s  %s",address.getThoroughfare(),address.getSubLocality(),address.getAdminArea(),address.getLocality(),address.getCountryName());
            listener.onChange(detail,address.getPostalCode());
        }
    }
}
