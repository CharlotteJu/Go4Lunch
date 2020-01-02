package com.mancel.yann.go4lunch.viewModels;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.mancel.yann.go4lunch.repositories.PlaceRepository;

/**
 * Created by Yann MANCEL on 02/01/2020.
 * Name of the project: Go4Lunch
 * Name of the package: com.mancel.yann.go4lunch.viewModels
 *
 * A class which implements {@link ViewModelProvider.Factory}.
 */
public class MapViewModelFactory implements ViewModelProvider.Factory {

    // FIELDS --------------------------------------------------------------------------------------

    @NonNull
    private PlaceRepository mPlaceRepository;

    // CONSTRUCTORS --------------------------------------------------------------------------------

    /**
     * Constructor
     * @param placeRepository a {@link PlaceRepository}
     */
    public MapViewModelFactory(@NonNull PlaceRepository placeRepository) {
        this.mPlaceRepository = placeRepository;
    }

    // METHODS -------------------------------------------------------------------------------------

    // ViewModelProvider.Factory interface --

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(MapViewModel.class)) {
            return (T) new MapViewModel(this.mPlaceRepository);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
