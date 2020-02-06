package com.mancel.yann.go4lunch.views.adapters;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.RequestManager;
import com.mancel.yann.go4lunch.R;
import com.mancel.yann.go4lunch.models.User;
import com.mancel.yann.go4lunch.utils.UserDiffCallback;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Yann MANCEL on 21/11/2019.
 * Name of the project: Go4Lunch
 * Name of the package: com.mancel.yann.go4lunch.views.adapters
 *
 * A {@link RecyclerView.Adapter} subclass.
 */
public class WorkmateAdapter extends RecyclerView.Adapter<WorkmateAdapter.WorkmateViewHolder> {

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
        final View view = layoutInflater.inflate(WorkmateAdapter.WorkmateViewHolder.getLayout(), parent, false);

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

    // -- Users --

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

    // INNER CLASSES -------------------------------------------------------------------------------

    /**
     * A {@link RecyclerView.ViewHolder} subclass.
     */
    public static class WorkmateViewHolder extends RecyclerView.ViewHolder {

        // FIELDS ----------------------------------------------------------------------------------

        @BindView(R.id.item_workmate_image)
        ImageView mImage;
        @BindView(R.id.item_workmate_text)
        TextView mText;

        // CONSTRUCTORS ----------------------------------------------------------------------------

        /**
         * Constructor
         * @param itemView a {@link View}
         */
        public WorkmateViewHolder(@NonNull View itemView) {
            super(itemView);

            // Using the ButterKnife library
            ButterKnife.bind(this, itemView);
        }

        // METHODS ---------------------------------------------------------------------------------

        // -- Layout --

        /**
         * Returns the layout value
         * @return an integer that contains the layout value
         */
        public static int getLayout() {
            return R.layout.item_workmate;
        }

        // -- Update UI --

        /**
         * Updates the item
         * @param user  a {@link User} that allows to update the item
         * @param glide a {@link RequestManager}
         */
        public void updateWorkmate(@NonNull final User user,
                                   @NonNull final RequestManager glide) {
            // ImageView (using to Glide library)
            glide.load(user.getUrlPicture())
                 .circleCrop()
                 .fallback(R.drawable.ic_person)
                 .error(R.drawable.ic_close)
                 .into(this.mImage);

            // TextView: Text
            String text;

            if (user.getPlaceIdOfRestaurant() == null) {
                // [User] has't decided yet
                text = itemView.getContext().getString(R.string.text_item_workmate_no_choice,
                                                       user.getUsername());
            }
            else {
                // {User] is eating [food type] ([Name])
                text = itemView.getContext().getString(R.string.text_item_workmate_choice,
                                                       user.getUsername(),
                                                       user.getNameOfRestaurant());
            }

            this.mText.setText(text);

            // TextView: Style
            if (Build.VERSION.SDK_INT < 23) {
                this.mText.setTextAppearance(itemView.getContext(),
                                             (user.getPlaceIdOfRestaurant() == null) ? R.style.TextViewStyle15 :
                                                                                       R.style.TextViewStyle14);
            }
            else {
                this.mText.setTextAppearance( (user.getPlaceIdOfRestaurant() == null) ? R.style.TextViewStyle15 :
                                                                                        R.style.TextViewStyle14);
            }
        }
    }
}
