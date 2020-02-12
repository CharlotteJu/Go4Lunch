package com.mancel.yann.go4lunch.repositories;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.SetOptions;
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

        chats ..................................................... [Collection]
        |
        +--- general_chat ......................................... [Document]
             |
             +--- messages ........................................ [Collection]
                  |
                  +--- message (value created by Firestore) ....... [Document]
                       |
                       +--- message ............................... [Data]
                       +--- date_created .......................... [Data]
                       +--- user .................................. [Data]
                            |
                            +--- uid .............................. [Data]
                            +--- username ......................... [Data]
                            +--- url_picture ...................... [Data]
                            +--- place_id_of_restaurant ........... [Data]
                            +--- name_of_restaurant ............... [Data]
                            +--- food_type_of_restaurant .......... [Data]
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
                                           .set(messageToCreate, SetOptions.merge());
    }

    // -- Read --

    @NonNull
    @Override
    public Query getAllMessages() {
        return this.getMessagesCollection().orderBy("date_created", Query.Direction.ASCENDING)
                                           .limit(50);
    }
}
