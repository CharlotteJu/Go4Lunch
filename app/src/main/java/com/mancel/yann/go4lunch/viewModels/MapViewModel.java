package com.mancel.yann.go4lunch.viewModels;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.mancel.yann.go4lunch.repositories.PlaceRepository;

import java.util.List;

/**
 * Created by Yann MANCEL on 10/12/2019.
 * Name of the project: Go4Lunch
 * Name of the package: com.mancel.yann.go4lunch.viewModels
 *
 * A {@link ViewModel} subclass.
 */
public class MapViewModel extends ViewModel {

    // FIELDS --------------------------------------------------------------------------------------

    @NonNull
    private PlaceRepository mPlaceRepository;

    private MutableLiveData<List<String>> mRestaurants;

    private static final String TAG = MapViewModel.class.getSimpleName();

    // CONSTRUCTORS --------------------------------------------------------------------------------

    /**
     * Constructor with an argument
     * @param placeRepository a {@link PlaceRepository}
     */
    public MapViewModel(@NonNull PlaceRepository placeRepository) {
        Log.d(TAG, "MapViewModel");

        this.mPlaceRepository = placeRepository;
    }

    // METHODS -------------------------------------------------------------------------------------

    // -- ViewModel --

    @Override
    protected void onCleared() {
        super.onCleared();
        Log.d(TAG, "onCleared");
    }

    // -- POIs --

    /**
     * Gets the {@link LiveData} of {@link List<String>}
     * @return a {@link LiveData} of {@link List<String>}
     */
    public LiveData<List<String>> getRestaurants() {
        if (this.mRestaurants == null) {
            this.mRestaurants = new MutableLiveData<>();
        }

        return this.mRestaurants;
    }

    /**
     * Loads the Points Of Interest thanks to the coordinates
     * @param latitude  a double for the latitude
     * @param longitude a double for the longitude
     */
    public void loadPOIs(double latitude, double longitude) {
        // Error: Out of boundaries of coordinates
        if (!(latitude >= -90.0 && latitude <= 90.0 && longitude >= -180.0 && longitude <= 180.0)) {
            return;
        }


    }
}
