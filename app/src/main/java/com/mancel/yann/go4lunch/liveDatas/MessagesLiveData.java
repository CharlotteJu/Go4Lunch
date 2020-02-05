package com.mancel.yann.go4lunch.liveDatas;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.mancel.yann.go4lunch.models.Message;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Yann MANCEL on 27/01/2020.
 * Name of the project: Go4Lunch
 * Name of the package: com.mancel.yann.go4lunch.liveDatas
 *
 * A {@link LiveData} of {@link List<Message>} subclass.
 */
public class MessagesLiveData extends LiveData<List<Message>> {

    // FIELDS --------------------------------------------------------------------------------------

    @NonNull
    private final Query mQuery;

    @SuppressWarnings("NullableProblems")
    @NonNull
    private ListenerRegistration mListenerRegistration;

    private static final String TAG = MessagesLiveData.class.getSimpleName();

    // CONSTRUCTORS --------------------------------------------------------------------------------

    /**
     * Constructor with an argument
     * @param query a {@link Query} to fetch messages from Firebase Firestore
     */
    public MessagesLiveData(@NonNull final Query query) {
        this.mQuery = query;
    }

    // METHODS -------------------------------------------------------------------------------------

    // -- LiveData --

    @Override
    protected void onActive() {
        super.onActive();
        this.configureListenerRegistration();
    }

    @Override
    protected void onInactive() {
        super.onInactive();

        // Stop listening to changes
        this.mListenerRegistration.remove();
    }

    // -- ListenerRegistration --

    /**
     * Configures the {@link ListenerRegistration}
     */
    private void configureListenerRegistration() {
        // ListenerRegistration: SnapshotListener of Query
        this.mListenerRegistration = this.mQuery.addSnapshotListener( (queryDocumentSnapshots, e) -> {
            if (e != null) {
                Log.e(TAG, "When addSnapshotListener to query (Update MessageAdapter): Listen failed.", e);
                return;
            }

            List<Message> messages = new ArrayList<>();

            if (queryDocumentSnapshots != null) {
                for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                    if (doc != null) {
                        final Message message = doc.toObject(Message.class);

                        // To avoid the value equal to null by Firebase Firestore
                        if (message.getDateCreated() == null) {
                            return;
                        }

                        messages.add(message);
                    }
                }
            }

            // Notify
            this.setValue(messages);
        });
    }
}