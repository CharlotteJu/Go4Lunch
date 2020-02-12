package com.mancel.yann.go4lunch.viewModels;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.mancel.yann.go4lunch.repositories.LikeRepository;
import com.mancel.yann.go4lunch.repositories.MessageRepository;
import com.mancel.yann.go4lunch.repositories.PlaceRepository;
import com.mancel.yann.go4lunch.repositories.UserRepository;

/**
 * Created by Yann MANCEL on 23/12/2019.
 * Name of the project: Go4Lunch
 * Name of the package: com.mancel.yann.go4lunch.viewModels
 *
 * A class which implements {@link ViewModelProvider.Factory}.
 */
public class Go4LunchViewModelFactory implements ViewModelProvider.Factory {

    // FIELDS --------------------------------------------------------------------------------------

    @NonNull
    private final UserRepository mUserRepository;

    @NonNull
    private final LikeRepository mLikeRepository;

    @NonNull
    private final MessageRepository mMessageRepository;

    @NonNull
    private final PlaceRepository mPlaceRepository;

    // CONSTRUCTORS --------------------------------------------------------------------------------

    /**
     * Constructor with 4 repositories
     * @param userRepository    a {@link UserRepository} for data from Firebase Firestore
     * @param likeRepository    a {@link LikeRepository} for data from Firebase Firestore
     * @param messageRepository a {@link MessageRepository} for data from Firebase Firestore
     * @param placeRepository   a {@link PlaceRepository} for data from Google Maps
     */
    public Go4LunchViewModelFactory(@NonNull final UserRepository userRepository,
                                    @NonNull final LikeRepository likeRepository,
                                    @NonNull final MessageRepository messageRepository,
                                    @NonNull final PlaceRepository placeRepository) {
        this.mUserRepository = userRepository;
        this.mLikeRepository = likeRepository;
        this.mMessageRepository = messageRepository;
        this.mPlaceRepository = placeRepository;
    }

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
