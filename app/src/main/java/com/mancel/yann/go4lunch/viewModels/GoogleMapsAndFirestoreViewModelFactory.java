package com.mancel.yann.go4lunch.viewModels;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.mancel.yann.go4lunch.repositories.PlaceRepository;
import com.mancel.yann.go4lunch.repositories.UserRepository;

/**
 * Created by Yann MANCEL on 23/12/2019.
 * Name of the project: Go4Lunch
 * Name of the package: com.mancel.yann.go4lunch.viewModels
 *
 * A class which implements {@link ViewModelProvider.Factory}.
 */
public class GoogleMapsAndFirestoreViewModelFactory implements ViewModelProvider.Factory {

    // FIELDS --------------------------------------------------------------------------------------

    @NonNull
    private final UserRepository mUserRepository;

    @NonNull
    private final PlaceRepository mPlaceRepository;

    // CONSTRUCTORS --------------------------------------------------------------------------------

    /**
     * Constructor with 2 repositories
     * @param userRepository    a {@link UserRepository} for data from Firebase Firestore
     * @param placeRepository   a {@link PlaceRepository} for data from Google Maps
     */
    public GoogleMapsAndFirestoreViewModelFactory(@NonNull UserRepository userRepository,
                                                  @NonNull PlaceRepository placeRepository) {
        this.mUserRepository = userRepository;
        this.mPlaceRepository = placeRepository;
    }

    // METHODS -------------------------------------------------------------------------------------

    // ViewModelProvider.Factory interface --

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(GoogleMapsAndFirestoreViewModel.class)) {
            return (T) new GoogleMapsAndFirestoreViewModel(this.mUserRepository,
                                                           this.mPlaceRepository);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}