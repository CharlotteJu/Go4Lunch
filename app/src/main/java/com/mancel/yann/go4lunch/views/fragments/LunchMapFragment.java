package com.mancel.yann.go4lunch.views.fragments;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.mancel.yann.go4lunch.R;
import com.mancel.yann.go4lunch.views.bases.BaseFragment;

/**
 * Created by Yann MANCEL on 19/11/2019.
 * Name of the project: Go4Lunch
 * Name of the package: com.mancel.yann.go4lunch.views.fragments
 *
 * A {@link BaseFragment} subclass.
 */
public class LunchMapFragment extends BaseFragment {

    // FIELDS --------------------------------------------------------------------------------------

    @Nullable
    private SupportMapFragment mMapFragment;

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
        this.configureSupportMapFragment();
    }

    // -- Google Maps --

    /**
     * Configures the {@link SupportMapFragment}
     */
    private void configureSupportMapFragment() {
        this.mMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.fragment_lunch_map_fragment);

        if (this.mMapFragment == null) {
            this.mMapFragment = SupportMapFragment.newInstance();

            getFragmentManager().beginTransaction()
                                .replace(R.id.fragment_lunch_map_fragment, this.mMapFragment)
                                .commit();
        }

        this.mMapFragment.getMapAsync(this::onMapReady);
    }

    /**
     * Updates the {@link GoogleMap}
     * @param googleMap a {@link GoogleMap}
     */
    public void onMapReady(GoogleMap googleMap) {
        // Add a marker in Sydney, Australia,
        // and move the map's camera to the same location.
        LatLng sydney = new LatLng(-33.852, 151.211);
        googleMap.addMarker(new MarkerOptions().position(sydney)
                                               .title("Marker in Sydney"));
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
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
