package com.mancel.yann.go4lunch.repositories;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.Query;
import com.mancel.yann.go4lunch.models.User;

import java.util.Date;

/**
 * Created by Yann MANCEL on 27/01/2020.
 * Name of the project: Go4Lunch
 * Name of the package: com.mancel.yann.go4lunch.repositories
 */
public interface MessageRepository {

    // METHODS -------------------------------------------------------------------------------------

    // -- CollectionReference --

    /**
     * Gets the messages collection
     * @return a {@link CollectionReference} that contains the reference of the messages collection
     */
    @NonNull
    CollectionReference getMessagesCollection();

    // -- Create --

    /**
     * Creates or sets a {@link com.mancel.yann.go4lunch.models.Message}
     * @param message       a {@link String} that contains the message
     * @param dateCreated   a {@link Date} that contains the date created
     * @param user          a {@link User} that contains the user who has created the message
     * @return a {@link Task} of {@link Void}
     */
    @NonNull
    Task<Void> createMessage(@NonNull final String message,
                             @NonNull final Date dateCreated,
                             @NonNull final User user);

    // -- Read --

    /**
     * Gets all messages into the collection
     * @return a {@link Query} that contains the messages
     */
    @NonNull
    Query getAllMessages();
}
