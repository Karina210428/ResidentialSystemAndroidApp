package activity.admin.gps;

import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;


import api.RetrofitClient;
import model.SharedPreference;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LocationService extends Service {

    private static final int LOCATION_INTERVAL = 60 * 1000;
    private static final float LOCATION_DISTANCE = 0;
    private Context context;

    LocationListener[] mLocationListeners = new LocationListener[]{
            new LocationListener(LocationManager.GPS_PROVIDER),
            new LocationListener(LocationManager.NETWORK_PROVIDER)
    };
    private LocationManager mLocationManager = null;
    private Double latitude, longitude;
    private String token = "";

    public LocationService() {
        super();
        this.context = LocationService.this;
    }

    private void initializeLocationManager() {
        if (mLocationManager == null) {
            mLocationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("TEST", "Service started");
        super.onStartCommand(intent, flags, startId);
        return START_STICKY;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        token = SharedPreference.getInstance(this).getToken();
        initializeLocationManager();

            try {
                mLocationManager.requestLocationUpdates(
                        LocationManager.NETWORK_PROVIDER, LOCATION_INTERVAL, LOCATION_DISTANCE,
                        mLocationListeners[1]);
            } catch (java.lang.SecurityException ex) {
                Log.i("TEST", "fail to request location update, ignore", ex);
            } catch (IllegalArgumentException ex) {
                Log.d("TEST", "network provider does not exist, " + ex.getMessage());
            }
            try {
                mLocationManager.requestLocationUpdates(
                        LocationManager.GPS_PROVIDER, LOCATION_INTERVAL, LOCATION_DISTANCE,
                        mLocationListeners[0]);
            } catch (java.lang.SecurityException ex) {
                Log.i("TEST", "fail to request location update, ignore", ex);
            } catch (IllegalArgumentException ex) {
                Log.d("TEST", "gps provider does not exist " + ex.getMessage());
            }
        }


    @Override
    public void onDestroy() {
        Log.i("TEST", "LocationService onDestroy");
        super.onDestroy();
        if (mLocationManager != null) {
            for (int i = 0; i < mLocationListeners.length; i++) {
                try {
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) !=
                            PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                    mLocationManager.removeUpdates(mLocationListeners[i]);
                } catch (Exception ex) {
                    Log.i("TEST", "fail to remove location listners, ignore", ex);
                }
            }
        }
    }

    private class LocationListener implements android.location.LocationListener{

        Location mLastLocation;
        public LocationListener(String provider) {
            mLastLocation = new Location(provider);
        }

        @Override
        public void onLocationChanged(Location location) {
            latitude = location.getLatitude();
            longitude = location.getLongitude();
            Log.w("TEST", "latlong=" + latitude + ":" + longitude + " token=" + token);
            mLastLocation.set(location);
            new SendToServer().execute(latitude, longitude, new Double(0));
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

        class SendToServer extends AsyncTask<Double, Double, Double> {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            protected Double doInBackground(Double... latlong) {
                model.Location location = new model.Location(latlong[0], latlong[1]);

                RetrofitClient.getInstance().getEmployeeApi().sentLocation(token, location).enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if(response.code()==200 && response.body()!=null){
                            Log.d("TEST", "Create Response: " + response);
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Log.i("TEST", "location send error=" + t.toString());
                    }
                });
                return new Double(1);
            }
        }
    }
}
