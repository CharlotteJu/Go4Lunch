package com.mancel.yann.go4lunch.views.fragments;

import android.content.res.Resources;
import android.location.Location;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.mancel.yann.go4lunch.R;
import com.mancel.yann.go4lunch.liveDatas.LocationLiveData;
import com.mancel.yann.go4lunch.models.LocationData;
import com.mancel.yann.go4lunch.models.POI;
import com.mancel.yann.go4lunch.repositories.PlaceRepositoryImpl;
import com.mancel.yann.go4lunch.repositories.UserRepositoryImpl;
import com.mancel.yann.go4lunch.utils.GeneratorBitmap;
import com.mancel.yann.go4lunch.viewModels.GoogleMapsAndFirestoreViewModel;
import com.mancel.yann.go4lunch.viewModels.GoogleMapsAndFirestoreViewModelFactory;
import com.mancel.yann.go4lunch.views.bases.BaseFragment;

import java.util.List;

import butterknife.OnClick;

/**
 * Created by Yann MANCEL on 19/11/2019.
 * Name of the project: Go4Lunch
 * Name of the package: com.mancel.yann.go4lunch.views.fragments
 *
 * A {@link BaseFragment} subclass which implements {@link OnMapReadyCallback},
 * {@link GoogleMap.OnCameraMoveStartedListener}, {@link GoogleMap.OnCameraIdleListener} and
 * {@link GoogleMap.OnMarkerClickListener}.
 */
public class LunchMapFragment extends BaseFragment implements OnMapReadyCallback,
                                                              GoogleMap.OnCameraMoveStartedListener,
                                                              GoogleMap.OnCameraIdleListener,
                                                              GoogleMap.OnMarkerClickListener {

    // FIELDS --------------------------------------------------------------------------------------

    @SuppressWarnings("NullableProblems")
    @NonNull
    private GoogleMap mGoogleMap;

    @SuppressWarnings("NullableProblems")
    @NonNull
    private GoogleMapsAndFirestoreViewModel mViewModel;

    @SuppressWarnings("NullableProblems")
    @NonNull
    private Location mCurrentLocation;
    // TODO: 16/01/2020 Not useful, should pass by LocationLiveData

    private boolean mIsFirstLocation = true;
    private boolean mIsLocatedOnUser = true;

    private static final float DEFAULT_ZOOM = 17F;

    private static final String TAG = LunchMapFragment.class.getSimpleName();

    // CONSTRUCTORS --------------------------------------------------------------------------------

    public LunchMapFragment() {}

    // METHODS -------------------------------------------------------------------------------------

    // -- BaseFragment --

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_lunch_map;
    }

    @Override
    protected void configureDesign() {
        // UI
        this.configureSupportMapFragment();

        // ViewModel
        this.configureViewModel();

        // LiveData
        this.configureLocationLiveData();
        this.configurePOIsLiveData();
    }

    // -- Actions --

    @OnClick(R.id.fragment_lunch_map_FAB)
    public void onFABClicked(View view) {
        // Focusing on vision against the current position
        if (!this.mIsLocatedOnUser) {
            this.animateCamera();

            // Reset: Camera focus on user
            this.mIsLocatedOnUser = true;
        }
    }

    // -- OnMapReadyCallback interface --

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.mGoogleMap = googleMap;

        // Camera
        this.mGoogleMap.setOnCameraMoveStartedListener(this);
        this.mGoogleMap.setOnCameraIdleListener(this);

        // Markers
        this.mGoogleMap.setOnMarkerClickListener(this);

        // Configure the style of the GoogleMap
        this.configureGoogleMapStyle();
    }

    // -- GoogleMap.OnCameraMoveStartedListener interface --

    @Override
    public void onCameraMoveStarted(int reason) {
        switch (reason) {
            // The user gestured on the map
            case GoogleMap.OnCameraMoveStartedListener.REASON_GESTURE:
                // Projection's Center (visible region) = current location of user (ex: zoom or rotation)
                if (this.mGoogleMap.getProjection().getVisibleRegion().latLngBounds.getCenter().latitude == this.mCurrentLocation.getLatitude() &&
                    this.mGoogleMap.getProjection().getVisibleRegion().latLngBounds.getCenter().longitude == this.mCurrentLocation.getLongitude()) {
                    Log.d(TAG, "onCameraMoveStarted: REASON_GESTURE but same center");
                }
                else {
                    this.mIsLocatedOnUser = false;
                }

                break;

            // The user tapped something on the map (ex: tap on marker)
            case GoogleMap.OnCameraMoveStartedListener.REASON_API_ANIMATION:
                this.mIsLocatedOnUser = false;
                break;

            // The app moved the camera
            case GoogleMap.OnCameraMoveStartedListener.REASON_DEVELOPER_ANIMATION:
                // [The app moved the camera]
                // Do nothing
        }
    }

    // -- GoogleMap.OnCameraIdleListener interface --

    @Override
    public void onCameraIdle() {
        Log.d(TAG, "onCameraIdle: The camera has stopped moving.");
    }

    // -- GoogleMap.OnMarkerClickListener --

    @Override
    public boolean onMarkerClick(Marker marker) {
        // The default behavior (return false) for a marker click event is to show its info window (if available)
        // and move the camera such that the marker is centered on the map.
        return false;
    }

    // -- Google Maps --

    /**
     * Configures the {@link SupportMapFragment}
     */
    private void configureSupportMapFragment() {
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.fragment_lunch_map_fragment);

        if (mapFragment == null) {
            mapFragment = SupportMapFragment.newInstance();

            getFragmentManager().beginTransaction()
                                .add(R.id.fragment_lunch_map_fragment, mapFragment)
                                .commit();
        }

        mapFragment.getMapAsync(this);
    }

    /**
     * Configures the style of the {@link GoogleMap}
     */
    private void configureGoogleMapStyle() {
        // STYLE
        try {
            // Customises the styling of the base map using a JSON object defined
            // in a raw resource file.
            boolean success = this.mGoogleMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this.getContext(),
                                                                                               R.raw.google_maps_style_json));

            if (!success) {
                Log.e(TAG, "configureGoogleMapStyle: Style parsing failed.");
            }
        } catch (Resources.NotFoundException e) {
            Log.e(TAG, "configureGoogleMapStyle: Can't find style. Error: ", e);
        }

        // GESTURES
        this.mGoogleMap.getUiSettings().setZoomGesturesEnabled(true);
        this.mGoogleMap.getUiSettings().setRotateGesturesEnabled(true);

        // SCROLL
        this.mGoogleMap.getUiSettings().setScrollGesturesEnabled(true);
        this.mGoogleMap.getUiSettings().setScrollGesturesEnabledDuringRotateOrZoom(true);

        // MIN ZOOM LEVELS
        this.mGoogleMap.setMinZoomPreference( (this.mGoogleMap.getMinZoomLevel() > 10.0F) ? this.mGoogleMap.getMinZoomLevel() :
                                                                                            10.0F);

        // MAX ZOOM LEVELS
        this.mGoogleMap.setMaxZoomPreference( (this.mGoogleMap.getMaxZoomLevel() < 21.0F) ? this.mGoogleMap.getMaxZoomLevel() :
                                                                                            21.0F);

        // MY LOCATION
        this.mGoogleMap.setMyLocationEnabled(true);
        this.mGoogleMap.getUiSettings().setMyLocationButtonEnabled(false);
    }

    /**
     * Configures the camera of {@link GoogleMap}
     */
    private void configureCamera() {
        // Location to LatLng
        final LatLng currentLatLng = new LatLng(this.mCurrentLocation.getLatitude(),
                                                this.mCurrentLocation.getLongitude());

        // Camera
        this.mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng,
                                                                     DEFAULT_ZOOM));
    }

    /**
     * Animates the camera of {@link GoogleMap}
     */
    private void animateCamera() {
        // Location to LatLng -> Target
        final LatLng target = new LatLng(this.mCurrentLocation.getLatitude(),
                                         this.mCurrentLocation.getLongitude());

        // CameraPosition
        final CameraPosition cameraPosition = new CameraPosition.Builder()
                                                                .target(target)
                                                                .zoom(DEFAULT_ZOOM)
                                                                .build();

        // Camera
        this.mGoogleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }

    // -- GoogleMapsAndFirestoreViewModel --

    /**
     * Configures the {@link GoogleMapsAndFirestoreViewModel}
     */
    private void configureViewModel() {
        // TODO: 09/01/2020 UserRepository and PlaceRepository must be removed thanks to Dagger 2
        final GoogleMapsAndFirestoreViewModelFactory factory = new GoogleMapsAndFirestoreViewModelFactory(new UserRepositoryImpl(),
                                                                                                          new PlaceRepositoryImpl());

        this.mViewModel = ViewModelProviders.of(this.getActivity(), factory)
                                            .get(GoogleMapsAndFirestoreViewModel.class);
    }

    /**
     * Starts the location update from {@link LocationLiveData}
     */
    public void startLocationUpdate() {
        this.mViewModel.startLocationUpdate();
    }

    /**
     * Configures the {@link LiveData<LocationData>}
     */
    private void configureLocationLiveData() {
        // Bind between liveData of ViewModel and the SupportMapFragment
        this.mViewModel.getLocation(this.getContext())
                       .observe(this.getActivity(), this::onChangedLocationData);
    }

    /**
     * Configures the {@link LiveData} of {@link List<POI>}
     */
    private void configurePOIsLiveData() {
        // Bind between liveData of ViewModel and the SupportMapFragment
        // TODO: 16/01/2020 Add LocationLiveData to source of POIsLiveData for remove the reference of this LiveData
        this.mViewModel.getPOIs(this.getContext(), null)
                       .observe(this.getActivity(), this::onChangedPOIsData);
    }

    /**
     * Method to replace the {@link androidx.lifecycle.Observer} of {@link LocationData}
     * @param locationData a {@link LocationData}
     */
    private void onChangedLocationData (@NonNull final LocationData locationData) {
        // Exception
        if (this.handleLocationException(locationData.getException())) {
            return;
        }

        // No Location
        if (locationData.getLocation() == null) {
            return;
        }

        // Current location
        // TODO: 16/01/2020 add LocationLiveData to do a getValue()
        this.mCurrentLocation = locationData.getLocation();

        // POIs
        this.mViewModel.fetchNearbySearch(this.getContext(), locationData);

        // Focus on the current location of user
        if (this.mIsLocatedOnUser) {
            // First Location
            if (this.mIsFirstLocation) {
                this.mIsFirstLocation = false;
                this.configureCamera();
            }
            else {
                this.animateCamera();
            }
        }
    }

    /**
     * Method to replace the {@link androidx.lifecycle.Observer} of {@link LiveData} of {@link List<POI>}
     * @param poiList a {@link List<POI>}
     */
    private void onChangedPOIsData(@NonNull final List<POI> poiList) {
        Log.d(TAG, "onChangedPOIsData: POIs");
        // No POI
        if (poiList.size() == 0) {
            return;
        }

        // Remove Markers
        // TODO: 16/01/2020 remove markers

        for (POI poi : poiList) {
            int drawableValue = poi.getIsSelected() ? R.drawable.ic_star :
                                                      R.drawable.ic_close;

            final BitmapDescriptor bitmapDescriptor = GeneratorBitmap.bitmapDescriptorFromVector(this.getContext(),
                                                                                                 drawableValue);

            this.mGoogleMap.addMarker(new MarkerOptions().position(new LatLng(poi.getLatitude(),
                                                                              poi.getLongitude()))
                           .title(poi.getName())
                           .snippet("test to add marker")
                           .icon(bitmapDescriptor));

            // TODO: 16/01/2020 add info windows 
        }
    }

    // -- Instances --

    /**
     * Gets a new instance of {@link LunchMapFragment}
     * @return a {@link LunchMapFragment}
     */
    @NonNull
    public static LunchMapFragment newInstance() {
        return new LunchMapFragment();
    }
}