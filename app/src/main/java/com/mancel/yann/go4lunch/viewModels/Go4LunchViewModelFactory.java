package com.mancel.yann.go4lunch.viewModels;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.mancel.yann.go4lunch.repositories.LikeRepository;
import com.mancel.yann.go4lunch.repositories.MessageRepository;
import com.mancel.yann.go4lunch.repositories.PlaceRepository;
import com.mancel.yann.go4lunch.repositories.UserRepository;

import javax.inject.Inject;

/**
 * Created by Yann MANCEL on 23/12/2019.
 * Name of the project: Go4Lunch
 * Name of the package: com.mancel.yann.go4lunch.viewModels
 *
 * A class which implements {@link ViewModelProvider.Factory}.
 */
public class Go4LunchViewModelFactory implements ViewModelProvider.Factory {

    // FIELDS --------------------------------------------------------------------------------------

    @Inject
    UserRepository mUserRepository;

    @Inject
    LikeRepository mLikeRepository;

    @Inject
    MessageRepository mMessageRepository;

    @Inject
    PlaceRepository mPlaceRepository;

    // CONSTRUCTORS --------------------------------------------------------------------------------

    /**
     * Constructor with 4 repositories thanks to Dagger 2
     */
    @Inject
    public Go4LunchViewModelFactory() {}

    // METHODS -------------------------------------------------------------------------------------

    // ViewModelProvider.Factory interface --

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(Go4LunchViewModel.class)) {
            return (T) new Go4LunchViewModel(this.mUserRepository,
                                             this.mLikeRepository,
                                             this.mMessageRepository,
                                             this.mPlaceRepository);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
