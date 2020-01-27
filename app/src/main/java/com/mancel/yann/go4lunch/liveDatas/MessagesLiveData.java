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

    @SuppressWarnings("NullableProblems")
    @NonNull
    private ListenerRegistration mListenerRegistration;

    private static final String TAG = MessagesLiveData.class.getSimpleName();

    // CONSTRUCTORS --------------------------------------------------------------------------------

    /**
     * Constructor with an argument
     * @param query a {@link Query} to fetch all messages
     */
    public MessagesLiveData(@NonNull final Query query) {
        this.configureListenerRegistration(query);
    }

    // METHODS -------------------------------------------------------------------------------------

    // -- LiveData --

    @Override
    protected void onActive() {
        super.onActive();
        Log.d(TAG, "onActive");
    }

    @Override
    protected void onInactive() {
        super.onInactive();
        Log.d(TAG, "onInactive");

        // Stop listening to changes
        this.mListenerRegistration.remove();
    }

    // -- ListenerRegistration --

    /**
     * Configures the {@link ListenerRegistration}
     * @param query a {@link Query} to fetch all users
     */
    private void configureListenerRegistration(@NonNull final Query query) {
        // ListenerRegistration: SnapshotListener of Query
        this.mListenerRegistration = query.addSnapshotListener( (queryDocumentSnapshots, e) -> {
            if (e != null) {
                Log.e(TAG, "When addSnapshotListener to query (Update MessageAdapter): Listen failed.", e);
                return;
            }

            List<Message> messages = new ArrayList<>();

            if (queryDocumentSnapshots != null) {
                for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                    if (doc != null) {
                        messages.add(doc.toObject(Message.class));
                    }
                }
            }

            // Notify
            this.setValue(messages);
        });
    }
}