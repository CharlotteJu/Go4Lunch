package com.mancel.yann.go4lunch.views.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.RequestManager;
import com.mancel.yann.go4lunch.models.User;

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

    // INTERFACES ----------------------------------------------------------------------------------

    public interface WorkmateAdapterListener {
        /**
         * the callback method is activated with the updateData method
         */
        void onDataChanged();
    }

    // FIELDS --------------------------------------------------------------------------------------

    @NonNull
    private final RequestManager mGlide;
    @NonNull
    private final WorkmateAdapterListener mCallback;
    @NonNull
    private final Context mContext;
    @NonNull
    private List<User> mUsers;

    // CONSTRUCTORS --------------------------------------------------------------------------------

    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.
     * @param callback  a {@link WorkmateAdapterListener} for the callback system
     * @param glide     a {@link RequestManager}
     * @param context   a {@link Context} (just to retrieve the resources)
     */
    public WorkmateAdapter(@NonNull final WorkmateAdapterListener callback,
                           @NonNull final RequestManager glide,
                           @NonNull final Context context) {
        this.mCallback = callback;
        this.mGlide = glide;
        this.mContext = context;
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
        holder.updateWorkmate(this.mUsers.get(position), this.mGlide, this.mContext);
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
        this.mUsers = newUsers;
        this.notifyDataSetChanged();

        // Callback to update UI
        this.mCallback.onDataChanged();
    }
}
