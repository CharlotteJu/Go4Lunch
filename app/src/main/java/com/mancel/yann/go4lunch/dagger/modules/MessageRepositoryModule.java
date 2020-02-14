package com.mancel.yann.go4lunch.dagger.modules;

import com.mancel.yann.go4lunch.repositories.MessageRepository;
import com.mancel.yann.go4lunch.repositories.MessageRepositoryImpl;

import javax.inject.Singleton;

import dagger.Binds;
import dagger.Module;

/**
 * Created by Yann MANCEL on 14/02/2020.
 * Name of the project: Go4Lunch
 * Name of the package: com.mancel.yann.go4lunch.dagger.modules
 */
@Module
public abstract class MessageRepositoryModule {

    // METHODS -------------------------------------------------------------------------------------

    /**
     * Creates a binds between the {@link MessageRepository} interface and
     * the {@link MessageRepositoryImpl} class
     * @param repository a {@link MessageRepositoryImpl}
     * @return a {@link MessageRepository}
     */
    @Singleton
    @Binds
    abstract MessageRepository bindMessageRepository(MessageRepositoryImpl repository);
}
