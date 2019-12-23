package com.mancel.yann.go4lunch.viewModels;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.mancel.yann.go4lunch.repositories.UserRepository;

/**
 * Created by Yann MANCEL on 23/12/2019.
 * Name of the project: Go4Lunch
 * Name of the package: com.mancel.yann.go4lunch.viewModels
 *
 * A class which implements {@link ViewModelProvider.Factory}.
 */
public class UserViewModelFactory implements ViewModelProvider.Factory {

    // FIELDS --------------------------------------------------------------------------------------

    @NonNull
    private UserRepository mUserRepository;

    // CONSTRUCTORS --------------------------------------------------------------------------------

    /**
     * Constructor
     * @param userRepository a {@link UserRepository}
     */
    public UserViewModelFactory(@NonNull final UserRepository userRepository) {
        this.mUserRepository = userRepository;
    }

    // METHODS -------------------------------------------------------------------------------------

    // ViewModelProvider.Factory interface --

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(UserViewModel.class)) {
            return (T) new UserViewModel(this.mUserRepository);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
