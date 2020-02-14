package com.mancel.yann.go4lunch.dagger.components;

import com.mancel.yann.go4lunch.dagger.modules.LikeRepositoryModule;
import com.mancel.yann.go4lunch.dagger.modules.MessageRepositoryModule;
import com.mancel.yann.go4lunch.dagger.modules.PlaceRepositoryModule;
import com.mancel.yann.go4lunch.dagger.modules.UserRepositoryModule;
import com.mancel.yann.go4lunch.viewModels.Go4LunchViewModelFactory;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by Yann MANCEL on 14/02/2020.
 * Name of the project: Go4Lunch
 * Name of the package: com.mancel.yann.go4lunch.dagger.components
 */
@Singleton
@Component(modules = {UserRepositoryModule.class,
                      MessageRepositoryModule.class,
                      LikeRepositoryModule.class,
                      PlaceRepositoryModule.class})
public interface Go4LunchComponent {

    // METHODS -------------------------------------------------------------------------------------

    /**
     * Get the {@link Go4LunchViewModelFactory}
     * @return a {@link Go4LunchViewModelFactory}
     */
    Go4LunchViewModelFactory getViewModelFactory();
}
