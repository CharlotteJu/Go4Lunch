package com.mancel.yann.go4lunch.liveDatas;

import android.content.Context;
import android.location.Location;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.SettingsClient;
import com.mancel.yann.go4lunch.models.LocationData;

/**
 * Created by Yann MANCEL on 09/12/2019.
 * Name of the project: Go4Lunch
 * Name of the package: com.mancel.yann.go4lunch.liveDatas
 *
 * A {@link LiveData<LocationData>} subclass.
 */
public class LocationLiveData extends LiveData<LocationData> {

    // FIELDS --------------------------------------------------------------------------------------

    @NonNull
    private final Context mContext;

    @SuppressWarnings("NullableProblems")
    @NonNull
    private FusedLocationProviderClient mFusedLocationProviderClient;

    @SuppressWarnings("NullableProblems")
    @NonNull
    private LocationRequest mLocationRequest;

    @SuppressWarnings("NullableProblems")
    @NonNull
    private LocationCallback mLocationCallback;

    private boolean isFirstSubscriber = true;

    private static final String TAG = LocationLiveData.class.getSimpleName();

    // CONSTRUCTORS --------------------------------------------------------------------------------

    /**
     * Constructor with the {@link Context}
     * @param context a {@link Context}
     */
    public LocationLiveData(@NonNull final Context context) {
        this.mContext = context.getApplicationContext();

        this.configureFusedLocationProviderClient();
        this.configureLocationRequest();
        this.configureLocationCallback();
    }

    // METHODS -------------------------------------------------------------------------------------

    // -- LiveData --

    @Override
    protected void onActive() {
        super.onActive();
        Log.d(TAG, "onActive");

        if (this.isFirstSubscriber) {
            this.requestLastLocation();
            this.requestUpdateLocation();

            // For the other subscribers
            this.isFirstSubscriber = false;
        }
    }

    @Override
    protected void onInactive() {
        super.onInactive();
        Log.d(TAG, "onInactive");

        this.mFusedLocationProviderClient.removeLocationUpdates(this.mLocationCallback);

        // For the other subscribers
        this.isFirstSubscriber = true;
    }

    // -- FusedLocationProviderClient --

    /**
     * Configure the {@link FusedLocationProviderClient}
     */
    private void configureFusedLocationProviderClient() {
        this.mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this.mContext);
    }

    // -- LocationRequest --

    /**
     * Creates the {@link LocationRequest}
     */
    private void configureLocationRequest() {
        this.mLocationRequest = LocationRequest.create();
        this.mLocationRequest.setInterval(10000);
        this.mLocationRequest.setFastestInterval(5000);
        this.mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    // -- LocationCallback --

    /**
     * Configures the {@link LocationCallback}
     */
    private void configureLocationCallback() {
        this.mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }

                for (Location location : locationResult.getLocations()) {
                    final double latitude = location.getLatitude();
                    final double longitude = location.getLongitude();

                    // Error: Out of boundaries of coordinates
                    if (!(latitude >= -90.0 && latitude <= 90.0 && longitude >= -180.0 && longitude <= 180.0)) {
                        return;
                    }

                    // Not useful: Same LocationData
                    if (getValue() != null) {
                        if (getValue().getLocation() != null) {
                            if (getValue().getLocation().getLatitude() == latitude &&
                                getValue().getLocation().getLongitude() == longitude) {
                                return;
                            }
                        }
                    }

                    // Notify
                    setValue(new LocationData(location, null));
                }
            }
        };
    }

    // -- Request location --

    /**
     * Requests the last location thanks to {@link FusedLocationProviderClient}
     */
    private void requestLastLocation() {
        this.mFusedLocationProviderClient.getLastLocation()
                                         .addOnSuccessListener( location -> {
                                             // Got last known location. In some rare situations this can be null.
                                             if (location != null) {
                                                 // Notify
                                                 this.setValue(new LocationData(location, null));
                                             }
                                         })
                                         .addOnFailureListener( exception ->
                                             // Notify
                                             this.setValue(new LocationData(null, exception))
                                         );
    }

    /**
     * Requests the update location
     */
    public void requestUpdateLocation() {
        final LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                                                                                   .addLocationRequest(this.mLocationRequest);

        final SettingsClient client = LocationServices.getSettingsClient(this.mContext);

        // TASK: Task<LocationSettingsResponse>
        client.checkLocationSettings(builder.build())
              .addOnSuccessListener( locationSettingsResponse ->
                  this.requestLocation()
              )
              .addOnFailureListener( exception ->
                  // Notify
                  this.setValue(new LocationData(null, exception))
              );
    }

    /**
     * Requests the location to create the looper
     */
    private void requestLocation() {
        this.mFusedLocationProviderClient.requestLocationUpdates(this.mLocationRequest,
                                                                 this.mLocationCallback,
                                                                 Looper.getMainLooper())
                                         .addOnFailureListener( exception ->
                                             // Notify
                                             this.setValue(new LocationData(null, exception))
                                         );
    }
}
