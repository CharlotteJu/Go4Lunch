package com.mancel.yann.go4lunch.views.adapters;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.RequestManager;
import com.mancel.yann.go4lunch.R;
import com.mancel.yann.go4lunch.models.Message;

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

    /**
     * Updates the item
     * @param message  a {@link Message} that allows to update the item
     * @param glide a {@link RequestManager}
     */
    void updateMessage(@NonNull final Message message,
                       @NonNull final RequestManager glide) {

    }
}
