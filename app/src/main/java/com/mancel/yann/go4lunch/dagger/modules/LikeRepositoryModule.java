package com.mancel.yann.go4lunch.dagger.modules;

import com.mancel.yann.go4lunch.repositories.LikeRepository;
import com.mancel.yann.go4lunch.repositories.LikeRepositoryImpl;

import javax.inject.Singleton;

import dagger.Binds;
import dagger.Module;

/**
 * Created by Yann MANCEL on 14/02/2020.
 * Name of the project: Go4Lunch
 * Name of the package: com.mancel.yann.go4lunch.dagger.modules
 */
@Module
public abstract class LikeRepositoryModule {

    // METHODS -------------------------------------------------------------------------------------

    /**
     * Creates a binds between the {@link LikeRepository} interface and
     * the {@link LikeRepositoryImpl} class
     * @param repository a {@link LikeRepositoryImpl}
     * @return a {@link LikeRepository}
     */
    @Singleton
    @Binds
    abstract LikeRepository bindLikeRepository(LikeRepositoryImpl repository);
}
