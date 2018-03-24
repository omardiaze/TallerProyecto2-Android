package pe.edu.upc.municipalidadmovil.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.EditText;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

/**
 * Created by Brayan on 28/01/2018.
 */

public class GeoLocation {
    private static final int REQUEST_PERMISSIONS_REQUEST_CODE = 34;
    private static final long UPDATE_INTERVAL_IN_MILLISECONDS = 10000;
    private static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS = UPDATE_INTERVAL_IN_MILLISECONDS / 2;

    private static final String TAG = "LOCATION LOG";
    private FusedLocationProviderClient mFusedLocationClient;
    private Location mCurrentLocation;

    private LocationRequest mLocationRequest;
    private LocationCallback mLocationCallback;
    private boolean mRequestingLocationUpdates;
    private Context context;

    /**
     * Crear una nueva instancia de Geolocalizacion
     * @param context es el contexto actual donde se ejecuta la geolocalizacion
     * @return void crea una nueva instanacia.
     */
    public GeoLocation(Context context) {
        this.mRequestingLocationUpdates = false;
        this.mFusedLocationClient = LocationServices.getFusedLocationProviderClient(context);
        this.context = context;
    }

    /**
     * Crear una nueva instancia de Geolocalizacion
     * @param pLongitud es el objeto que recibirá el valor de la Longitud
     * @param pLatitud  es el objeto que recibirá el valor de la Latitud
     * @param pAltitud  es el objeto que recibirá el valor de la Altitud
     * @param pExactitud  es el objeto que recibirá el valor del margen de error
     * @return void asigna los valores a los objetos enviados como parámetros.
     */
    public void getTextCoordinates(final EditText pLongitud, final EditText pLatitud,
                                   final EditText pAltitud, final EditText pExactitud) {
        if (!checkPermissions()) {
            requestPermissions();
        } else {
            getCustomLocation(pLongitud, pLatitud, pAltitud, pExactitud);
        }
    }

    private boolean checkPermissions() {
        int permissionState = ActivityCompat.checkSelfPermission(context,
                Manifest.permission.ACCESS_FINE_LOCATION);
        return permissionState == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermissions() {
        boolean shouldProvideRationale =
                ActivityCompat.shouldShowRequestPermissionRationale((Activity) context,
                        Manifest.permission.ACCESS_FINE_LOCATION);
        if (shouldProvideRationale) {
            Log.i(TAG, "Displaying permission rationale to provide additional context.");
        } else {
            Log.i(TAG, "Requesting permission");
            startLocationPermissionRequest();
        }
    }

    private void startLocationPermissionRequest() {
        ActivityCompat.requestPermissions((Activity) context,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION},
                REQUEST_PERMISSIONS_REQUEST_CODE);
    }

    @SuppressWarnings("MissingPermission")
    private void getCustomLocation(final EditText pLongitud, final EditText pLatitud,
                                   final EditText pAltitud, final EditText pExactitud) {
        if (!mRequestingLocationUpdates) {
            createLocationRequest();
            createLocationCallback();
            mRequestingLocationUpdates = true;
            mFusedLocationClient.requestLocationUpdates(mLocationRequest,
                    mLocationCallback, Looper.myLooper());
        }

        mFusedLocationClient.getLastLocation()
                .addOnCompleteListener((Activity) context, new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        if (task.isSuccessful() && task.getResult() != null) {
                            mCurrentLocation = task.getResult();
                            pLatitud.setText(String.valueOf(mCurrentLocation.getLatitude()));
                            pLongitud.setText(String.valueOf(mCurrentLocation.getLongitude()));

                            if (pAltitud != null) {
                                double vAltitud = mCurrentLocation.getAltitude();
                                pAltitud.setText(String.valueOf(Utilitario.roundDecimals(vAltitud, 3)));
                            }
                            if (pExactitud != null) {
                                double vAccurancy = mCurrentLocation.getAccuracy();
                                pExactitud.setText(String.valueOf(Utilitario.roundDecimals(vAccurancy, 3)));
                            }
                        } else {
                            Log.w(TAG, "getLastLocation:exception", task.getException());
                        }
                    }
                });
    }

    private void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);
        mLocationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    private void createLocationCallback() {
        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                mCurrentLocation = locationResult.getLastLocation();
            }
        };
    }

    public void stopLocationUpdates() {
        if (!mRequestingLocationUpdates) {
            Log.d(TAG, "stopLocationUpdates: updates never requested, no-op.");
            return;
        }
        mFusedLocationClient.removeLocationUpdates(mLocationCallback)
                .addOnCompleteListener((Activity) context, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        mRequestingLocationUpdates = false;
                    }
                });
    }
}
