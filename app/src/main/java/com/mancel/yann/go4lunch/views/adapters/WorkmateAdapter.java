package com.mancel.yann.go4lunch.views.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.RequestManager;
import com.mancel.yann.go4lunch.models.User;
import com.mancel.yann.go4lunch.utils.UserDiffCallback;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Yann MANCEL on 21/11/2019.
 * Name of the project: Go4Lunch
 * Name of the package: com.mancel.yann.go4lunch.views.adapters
 *
 * A {@link RecyclerView.Adapter} subclass.
 */
public class WorkmateAdapter extends RecyclerView.Adapter<WorkmateViewHolder> {

    // FIELDS --------------------------------------------------------------------------------------

    @NonNull
    private final AdapterListener mCallback;

    @NonNull
    private final RequestManager mGlide;

    @NonNull
    private List<User> mUsers;

    // CONSTRUCTORS --------------------------------------------------------------------------------

    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.
     * @param callback  a {@link AdapterListener} for the callback system
     * @param glide     a {@link RequestManager}
     */
    public WorkmateAdapter(@NonNull final AdapterListener callback,
                           @NonNull final RequestManager glide) {
        this.mCallback = callback;
        this.mGlide = glide;
        this.mUsers = new ArrayList<>();
    }

    // METHODS -------------------------------------------------------------------------------------

    // -- RecyclerView.Adapter --

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
    public void onBindViewHolder(@NonNull WorkmateViewHolder holder, int position) {
        holder.updateWorkmate(this.mUsers.get(position), this.mGlide);
    }

    @Override
    public int getItemCount() {
        return this.mUsers.size();
    }

    // -- User --

    /**
     * Updates the data thanks to the {@link List<User>} in argument
     * @param newUsers a {@link List<User>} that contains the new data
     */
    public void updateData(@NonNull final List<User> newUsers) {
        // Optimizes the performances of RecyclerView
        final UserDiffCallback diffCallback = new UserDiffCallback(this.mUsers, newUsers);
        final DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(diffCallback);

        // New data
        this.mUsers = newUsers;

        // Notifies adapter
        diffResult.dispatchUpdatesTo(this);

        // Callback to update UI
        this.mCallback.onDataChanged();
    }
}
