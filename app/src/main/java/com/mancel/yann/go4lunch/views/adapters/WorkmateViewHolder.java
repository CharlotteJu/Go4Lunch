package com.mancel.yann.go4lunch.views.adapters;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.RequestManager;
import com.mancel.yann.go4lunch.R;
import com.mancel.yann.go4lunch.models.User;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Yann MANCEL on 21/11/2019.
 * Name of the project: Go4Lunch
 * Name of the package: com.mancel.yann.go4lunch.views.adapters
 *
 * A {@link RecyclerView.ViewHolder} subclass.
 */
class WorkmateViewHolder extends RecyclerView.ViewHolder {

    // FIELDS --------------------------------------------------------------------------------------

    @BindView(R.id.item_workmate_image)
    ImageView mImage;
    @BindView(R.id.item_workmate_text)
    TextView mText;

    // CONSTRUCTORS --------------------------------------------------------------------------------

    /**
     * Constructor
     * @param itemView a {@link View}
     */
    WorkmateViewHolder(@NonNull View itemView) {
        super(itemView);

        // Using the ButterKnife library
        ButterKnife.bind(this, itemView);
    }

    // METHODS -------------------------------------------------------------------------------------

    // -- Layout --

    /**
     * Returns the layout value
     * @return an integer that contains the layout value
     */
    static int getLayout() {
        return R.layout.item_workmate;
    }

    // -- Update UI --

    /**
     * Updates the item
     * @param user  a {@link User} that allows to update the item
     * @param glide a {@link RequestManager}
     */
    void updateWorkmate(@NonNull User user, @NonNull final RequestManager glide) {
        // ImageView (using to Glide library)
        glide.load(user.getUrlPicture())
             .circleCrop()
             .fallback(R.drawable.ic_person)
             .error(R.drawable.ic_close)
             .into(this.mImage);

        // TextView
        // TODO: 16/12/2019 Change style and text according to user.getSelectedRestaurant() method
        final String text = (user.getSelectedRestaurant() == null) ? user.getUsername() + ": no restaurant" :
                                                                     user.getUsername() + ": restaurant";

        this.mText.setText(text);
    }
}
