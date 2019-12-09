package com.mancel.yann.go4lunch.liveDatas;

import android.content.Context;
import android.location.Location;
import android.os.Looper;

import androidx.lifecycle.LiveData;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.Task;
import com.mancel.yann.go4lunch.models.LocationData;

/**
 * Created by Yann MANCEL on 09/12/2019.
 * Name of the project: Go4Lunch
 * Name of the package: com.mancel.yann.go4lunch.liveDatas
 *
 * A {@link LiveData} subclass.
 */
public class LocationLiveData extends LiveData<LocationData> {

    // FIELDS --------------------------------------------------------------------------------------

    private Context mContext;

    private FusedLocationProviderClient mFusedLocationProviderClient;
    private LocationRequest mLocationRequest;
    private LocationCallback mLocationCallback;

    private boolean isFirstSubscriber = true;

    // CONSTRUCTORS --------------------------------------------------------------------------------

    /**
     * Constructor
     * @param context a {@link Context}
     */
    public LocationLiveData(Context context) {
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
                    setValue(new LocationData(location, null));
                }
            }
        };
    }

    // -- Request location --

    /**
     * Requests the last location
     */
    private void requestLastLocation() {
        this.mFusedLocationProviderClient.getLastLocation()
                                         .addOnSuccessListener( location ->
                                             this.setValue(new LocationData(location, null))
                                         )
                                         .addOnFailureListener( exception ->
                                             this.setValue(new LocationData(null, exception))
                                         );
    }

    /**
     * Requests the update location
     */
    public void requestUpdateLocation() {
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                                                                             .addLocationRequest(this.mLocationRequest);

        SettingsClient client = LocationServices.getSettingsClient(this.mContext);

        Task<LocationSettingsResponse> task = client.checkLocationSettings(builder.build())
                                                    .addOnSuccessListener( locationSettingsResponse ->
                                                        this.requestLocation()
                                                    )
                                                    .addOnFailureListener( exception ->
                                                        this.setValue(new LocationData(null, exception))
                                                    );
    }

    /**
     * Requests the location
     */
    private void requestLocation() {
        this.mFusedLocationProviderClient.requestLocationUpdates(this.mLocationRequest,
                                                                 this.mLocationCallback,
                                                                 Looper.getMainLooper())
                                         .addOnFailureListener( exception ->
                                             this.setValue(new LocationData(null, exception))
                                         );
    }
}
