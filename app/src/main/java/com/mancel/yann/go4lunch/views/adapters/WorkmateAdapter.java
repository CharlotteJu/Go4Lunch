package com.mancel.yann.go4lunch.views.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.bumptech.glide.RequestManager;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.mancel.yann.go4lunch.models.User;

/**
 * Created by Yann MANCEL on 21/11/2019.
 * Name of the project: Go4Lunch
 * Name of the package: com.mancel.yann.go4lunch.views.adapters
 *
 * A {@link FirestoreRecyclerAdapter} subclass.
 */
public class WorkmateAdapter extends FirestoreRecyclerAdapter<User, WorkmateViewHolder> {

    // INTERFACES ----------------------------------------------------------------------------------

    public interface WorkmateAdapterListener {
        /**
         * the callback method is activated when the {@link FirestoreRecyclerAdapter} calls
         * its onDataChanged method.
         */
        void onDataChanged();
    }

    // FIELDS --------------------------------------------------------------------------------------

    @NonNull
    private final RequestManager mGlide;
    @NonNull
    private final WorkmateAdapterListener mCallback;

    // CONSTRUCTORS --------------------------------------------------------------------------------

    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.
     * See {@link FirestoreRecyclerOptions} for configuration options.
     * @param options   a {@link FirestoreRecyclerOptions} of {@link User}
     * @param callback  a {@link WorkmateAdapterListener} for the callback system
     * @param glide     a {@link RequestManager}
     */
    public WorkmateAdapter(@NonNull final FirestoreRecyclerOptions<User> options,
                           @NonNull final WorkmateAdapterListener callback,
                           @NonNull final RequestManager glide) {
        super(options);

        this.mCallback = callback;
        this.mGlide = glide;
    }

    // METHODS -------------------------------------------------------------------------------------

    // -- FirestoreRecyclerAdapter --

    @NonNull
    @Override
    public WorkmateViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Creates a Context to the LayoutInflater
        final Context context = parent.getContext();
        final LayoutInflater layoutInflater = LayoutInflater.from(context);

        // Creates the View thanks to the inflater
        final View view = layoutInflater.inflate(WorkmateViewHolder.getLayout(), parent, false);

        return new WorkmateViewHolder(view);
    }

    @Override
    protected void onBindViewHolder(@NonNull WorkmateViewHolder workmateViewHolder, int i, @NonNull User user) {
        workmateViewHolder.updateWorkmate(user, this.mGlide);
    }

    @Override
    public void onDataChanged() {
        super.onDataChanged();
        this.mCallback.onDataChanged();
    }
}
