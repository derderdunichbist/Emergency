package org.hsrt.mc.emergency.gps;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Service;
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
import android.os.IBinder;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;

import org.hsrt.mc.emergency.R;

import java.util.List;
import java.util.Locale;

/**
 * Created by Andreas Mueller on 28.05.2016.
 */
/// <summary>
/// GPS Service Class which implements a Location Listener to get longitude and latitude coordinates
/// </summary>
public class GPS extends Service implements LocationListener
{

    private final Context context;

    Location location;

    protected LocationManager locationManager;

    boolean isGPSEnabled = false;
    boolean isNetworkEnabled = false;
    boolean canGetLocation = false;

    double latitude;
    double longitude;

    private static final long DISTANCE = 0;
    private static final long TIME = 0;

    final private int PERMISSION = 23;


    /*
     * Constructor, saves the context in a var and launches the getLocation class when instantiated
     */
    public GPS(Context context)
    {
        this.context = context;
        getLocation();
    }

    /*
     * Asks for gps and internet accessability, then checks for the required permissions and will finally
     * gets the last known location from the location manager.
     * The longitude and latitude will be saved for later use.
     */
    public Location getLocation()
    {
        try {
            locationManager = (LocationManager) context.getSystemService(LOCATION_SERVICE);

            isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

            isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if (!isGPSEnabled && !isNetworkEnabled)
            {

            } else {
                this.canGetLocation = true;

                if (isNetworkEnabled)
                {

                    if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
                    {

                        ActivityCompat.requestPermissions((Activity) context,
                                new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION},
                                PERMISSION);


                    }else
                    {

                        locationManager.requestLocationUpdates(
                                LocationManager.NETWORK_PROVIDER,
                                TIME,
                                DISTANCE, this);
                    }


                    if (locationManager != null)
                    {

                        location = locationManager
                                .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);


                        if (location != null)
                        {

                            latitude = location.getLatitude();
                            longitude = location.getLongitude();
                        }
                    }

                }

                if(isGPSEnabled)
                {


                    if(location == null)
                    {
                        locationManager.requestLocationUpdates
                                (
                                // Time and Distance is 0 so the user can get the next position even when he is just 10 meters away from his last permission
                                LocationManager.GPS_PROVIDER,
                                TIME,
                                DISTANCE, this);



                        if(locationManager != null)
                        {
                            location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

                            if(location != null)
                            {
                                latitude = location.getLatitude();
                                longitude = location.getLongitude();
                            }
                        }
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return location;
    }


    public double getLatitude()
    {
        if(location != null)
        {
            latitude = location.getLatitude();
        }
        return latitude;
    }

    public double getLongitude()
    {
        if(location != null)
        {
            longitude = location.getLongitude();
        }

        return longitude;
    }

    /*
     * This function provides a Geocoder which will convert the latitudes in a
     * Address. The format is as follows: STREET NUMBER ZIP CODE CITY
     * The Address will be saved in a list and concardinated to a string.
     * Finally the method returns the string with the address.
     */
    public String getGeoLocation(double latitude, double longitude)
    {
        String address = "";

        Geocoder geo = new Geocoder(this.context, Locale.getDefault());

        try {

            List<Address> addressList = geo.getFromLocation(latitude, longitude, 1);
            if (addressList != null) {
                Address addresses = addressList.get(0);
                StringBuilder addressStr = new StringBuilder("");

                for (int i = 0; i < addresses.getMaxAddressLineIndex(); i++) {
                    addressStr.append(addresses.getAddressLine(i)).append("\n");
                }
                address = addressStr.toString();

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return address;
    }


    public boolean canGetLocation()
    {
        return this.canGetLocation;
    }



   /*
    * If the GPS Service is not enabled a dialog will pop up and asks the user to accept the access
    */
    public void showSettings()
    {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);

        alertDialog.setTitle(R.string.gps_required);

        alertDialog.setMessage(R.string.please_enable_gps);

        alertDialog.setPositiveButton(R.string.enable_gps, new DialogInterface.OnClickListener()
        {

            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                context.startActivity(intent);
            }
        });

        alertDialog.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener()
        {

            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                dialog.cancel();
            }
        });

        alertDialog.show();
    }

    @Override
    public void onLocationChanged(Location arg0)
    {
    }

    @Override
    public void onProviderDisabled(String arg0)
    {
    }

    @Override
    public void onProviderEnabled(String arg0)
    {
    }

    @Override
    public void onStatusChanged(String arg0, int arg1, Bundle arg2)
    {
    }

    @Override
    public IBinder onBind(Intent intent)
    {
        return null;
    }

}