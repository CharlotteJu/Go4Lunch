package com.mancel.yann.go4lunch.repositories;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.mancel.yann.go4lunch.models.Message;
import com.mancel.yann.go4lunch.models.User;

/**
 * Created by Yann MANCEL on 27/01/2020.
 * Name of the project: Go4Lunch
 * Name of the package: com.mancel.yann.go4lunch.repositories
 *
 * A class which implements {@link MessageRepository}.
 */
public class MessageRepositoryImpl implements MessageRepository {

    /*
        Firestore structure:

        chats
        |
        +--- general_chat
             |
             +--- messages
                  |
                  +--- message (value created by Firestore)
                       |
                       +--- message
                       +--- date_created
                       +--- user
                            |
                            +--- uid
                            +--- username
                            +--- url_picture
                            +--- place_id_of_restaurant
                            +--- name_of_restaurant
                            +--- food_type_of_restaurant
     */

    // FIELDS --------------------------------------------------------------------------------------

    private static final String COLLECTION_CHATS = "chats";
    private static final String DOCUMENT_GENERAL_CHAT = "general_chat";
    private static final String COLLECTION_MESSAGES = "messages";

    // METHODS -------------------------------------------------------------------------------------

    // -- CollectionReference --

    @NonNull
    @Override
    public CollectionReference getMessagesCollection() {
        return FirebaseFirestore.getInstance()
                                .collection(COLLECTION_CHATS)
                                .document(DOCUMENT_GENERAL_CHAT)
                                .collection(COLLECTION_MESSAGES);
    }

    // -- Create --

    @NonNull
    @Override
    public Task<Void> createMessage(@NonNull final String message,
                                    @NonNull final User user) {
        final Message messageToCreate = new Message(message, user);
        return this.getMessagesCollection().document()
                                           .set(messageToCreate);
    }

    // -- Read --

    @NonNull
    @Override
    public Query getAllMessages() {
        return this.getMessagesCollection().orderBy("date_created", Query.Direction.ASCENDING)
                                           .limit(50);
    }
}
