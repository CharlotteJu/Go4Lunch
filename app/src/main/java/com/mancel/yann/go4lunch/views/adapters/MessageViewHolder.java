package com.mancel.yann.go4lunch.views.adapters;

import android.graphics.drawable.GradientDrawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.RequestManager;
import com.mancel.yann.go4lunch.R;
import com.mancel.yann.go4lunch.models.Message;

import java.text.SimpleDateFormat;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Yann MANCEL on 27/01/2020.
 * Name of the project: Go4Lunch
 * Name of the package: com.mancel.yann.go4lunch.views.adapters
 *
 * A {@link RecyclerView.ViewHolder} subclass.
 */
public class MessageViewHolder extends RecyclerView.ViewHolder {

    // FIELDS --------------------------------------------------------------------------------------

    @BindView(R.id.item_chat_ConstraintLayout)
    ConstraintLayout mConstraintLayout;
    @BindView(R.id.item_chat_image)
    ImageView mImage;
    @BindView(R.id.item_chat_message)
    TextView mMessage;
    @BindView(R.id.item_chat_hours)
    TextView mHours;

    // METHODS -------------------------------------------------------------------------------------

    /**
     * Constructor
     * @param itemView a {@link View}
     */
    public MessageViewHolder(@NonNull View itemView) {
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
        return R.layout.item_message;
    }

    // -- Update UI --

    /**
     * Updates the item
     * @param message           a {@link Message} that allows to update the item
     * @param glide             a {@link RequestManager}
     * @param uidOfCurrentUser  a {@link String} that contains the uid of current user
     */
    void updateMessage(@NonNull final Message message,
                       @NonNull final RequestManager glide,
                       @NonNull final String uidOfCurrentUser) {
        // Checks if the current user has send le message
        final boolean isCurrentUser = uidOfCurrentUser.equals(message.getUser().getUid());

        // Image (using to Glide library)
        glide.load(message.getUser().getUrlPicture())
             .circleCrop()
             .fallback(R.drawable.ic_person)
             .error(R.drawable.ic_close)
             .into(this.mImage);

        // Message
        this.mMessage.setText(message.getMessage());

        // Background of message
        ((GradientDrawable) this.mMessage.getBackground()).setColor( isCurrentUser ? itemView.getResources().getColor(R.color.colorPrimary) :
                                                                                     itemView.getResources().getColor(R.color.colorOtherUser));

        // Hours
        this.mHours.setText(new SimpleDateFormat("HH:mm").format(message.getDateCreated()));

        // Configures the positions of UI
        this.configurePositionsOfUI(isCurrentUser);
    }

    /**
     * Configures the positions of UI
     * @param isCurrentUser a boolean that is true if the current user has sent the message
     */
    private void configurePositionsOfUI(final boolean isCurrentUser) {
        // ConstraintSet
        final ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone(this.mConstraintLayout);

        if (isCurrentUser) {
            // Image
            constraintSet.connect(this.mImage.getId(), ConstraintSet.START,
                                  this.mMessage.getId(), ConstraintSet.END);

            constraintSet.connect(this.mImage.getId(), ConstraintSet.END,
                                  this.mConstraintLayout.getId(), ConstraintSet.END);

            // Message
            constraintSet.connect(this.mMessage.getId(), ConstraintSet.START,
                                  this.mConstraintLayout.getId(), ConstraintSet.START);

            constraintSet.connect(this.mMessage.getId(), ConstraintSet.END,
                                  this.mImage.getId(), ConstraintSet.START);

            // Hours
            constraintSet.setHorizontalBias(this.mHours.getId(), 0.0F);
        }
        else {
            // Image
            constraintSet.connect(this.mImage.getId(), ConstraintSet.START,
                                  this.mConstraintLayout.getId(), ConstraintSet.START);

            constraintSet.connect(this.mImage.getId(), ConstraintSet.END,
                                  this.mMessage.getId(), ConstraintSet.START);

            // Message
            constraintSet.connect(this.mMessage.getId(), ConstraintSet.START,
                                  this.mImage.getId(), ConstraintSet.END);

            constraintSet.connect(this.mMessage.getId(), ConstraintSet.END,
                                  this.mConstraintLayout.getId(), ConstraintSet.END);

            // Hours
            constraintSet.setHorizontalBias(this.mHours.getId(), 1.0F);
        }

        constraintSet.applyTo(this.mConstraintLayout);
    }
}
