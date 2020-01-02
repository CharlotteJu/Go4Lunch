package com.mancel.yann.go4lunch.views.adapters;

import android.content.Context;
import android.os.Build;
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
     * @param user      a {@link User} that allows to update the item
     * @param glide     a {@link RequestManager}
     * @param context   a {@link Context} (just to retrieve the resources)
     */
    void updateWorkmate(@NonNull final User user,
                        @NonNull final RequestManager glide,
                        @NonNull final Context context) {
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
            text = context.getString(R.string.text_item_workmate_no_choice,
                                     user.getUsername());
        }
        else {
            // {User] is eating [food type] ([Name])
            text = context.getString(R.string.text_item_workmate_choice,
                                    user.getUsername(),
                                                user.getFoodTypeOfRestaurant(),
                                                user.getNameOfRestaurant());
        }

        this.mText.setText(text);

        // TextView: Style
        if (Build.VERSION.SDK_INT < 23) {
            this.mText.setTextAppearance(context,
                                         (user.getPlaceIdOfRestaurant() == null) ? R.style.TextViewStyle2 :
                                                                                   R.style.TextViewStyle1);
        }
        else {
            this.mText.setTextAppearance( (user.getPlaceIdOfRestaurant() == null) ? R.style.TextViewStyle2 :
                                                                                    R.style.TextViewStyle1);
        }
    }
}
