package com.mancel.yann.go4lunch.dagger.modules;

import com.mancel.yann.go4lunch.repositories.PlaceRepository;
import com.mancel.yann.go4lunch.repositories.PlaceRepositoryImpl;

import javax.inject.Singleton;

import dagger.Binds;
import dagger.Module;

/**
 * Created by Yann MANCEL on 14/02/2020.
 * Name of the project: Go4Lunch
 * Name of the package: com.mancel.yann.go4lunch.dagger.modules
 */
@Module
public abstract class PlaceRepositoryModule {

    // METHODS -------------------------------------------------------------------------------------

    /**
     * Creates a binds between the {@link PlaceRepository} interface and
     * the {@link PlaceRepositoryImpl} class
     * @param repository a {@link PlaceRepositoryImpl}
     * @return a {@link PlaceRepository}
     */
    @Singleton
    @Binds
    abstract PlaceRepository bindPlaceRepository(PlaceRepositoryImpl repository);
}
