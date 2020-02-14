package com.mancel.yann.go4lunch.dagger.modules;

import com.mancel.yann.go4lunch.repositories.UserRepository;
import com.mancel.yann.go4lunch.repositories.UserRepositoryImpl;

import javax.inject.Singleton;

import dagger.Binds;
import dagger.Module;

/**
 * Created by Yann MANCEL on 14/02/2020.
 * Name of the project: Go4Lunch
 * Name of the package: com.mancel.yann.go4lunch.dagger.modules
 */
@Module
public abstract class UserRepositoryModule {

    // METHODS -------------------------------------------------------------------------------------

    /**
     * Creates a binds between the {@link UserRepository} interface and
     * the {@link UserRepositoryImpl} class
     * @param repository a {@link UserRepositoryImpl}
     * @return a {@link UserRepository}
     */
    @Singleton
    @Binds
    abstract UserRepository bindUserRepository(UserRepositoryImpl repository);
}
