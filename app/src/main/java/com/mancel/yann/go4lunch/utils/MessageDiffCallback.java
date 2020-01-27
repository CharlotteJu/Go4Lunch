package com.mancel.yann.go4lunch.utils;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;

import com.mancel.yann.go4lunch.models.Message;
import com.mancel.yann.go4lunch.models.User;

import java.util.List;
import java.util.Objects;

/**
 * Created by Yann MANCEL on 27/01/2020.
 * Name of the project: Go4Lunch
 * Name of the package: com.mancel.yann.go4lunch.utils
 *
 * A {@link DiffUtil.Callback} subclass.
 */
public class MessageDiffCallback extends DiffUtil.Callback {

    // FIELDS --------------------------------------------------------------------------------------

    @NonNull
    private final List<Message> mOldMessages;

    @NonNull
    private final List<Message> mNewMessages;

    // CONSTRUCTORS --------------------------------------------------------------------------------

    /**
     * Constructor with the odl and new lists
     * @param oldMessages a {@link List<Message>} that contains the old list
     * @param newMessages a {@link List<Message>} that contains the new list
     */
    public MessageDiffCallback(@NonNull final List<Message> oldMessages,
                               @NonNull final List<Message> newMessages) {
        this.mOldMessages = oldMessages;
        this.mNewMessages = newMessages;
    }

    // METHODS -------------------------------------------------------------------------------------

    // -- DiffUtil.Callback --

    @Override
    public int getOldListSize() {
        return this.mOldMessages.size();
    }

    @Override
    public int getNewListSize() {
        return this.mNewMessages.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        // Comparison based on the user's Uid
//        final String oldName = this.mOldUsers.get(oldItemPosition).getUid();
//        final String newName = this.mNewUsers.get(newItemPosition).getUid();
//
//        return oldName.equals(newName);

        return false;
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        // Comparison based on:
        //  - the name of user
        //  - the photo's url of user
        //  - the place Id of selected restaurant

//        return Objects.equals(this.mOldUsers.get(oldItemPosition).getUsername(),
//                              this.mNewUsers.get(newItemPosition).getUsername()) &&
//
//               Objects.equals(this.mOldUsers.get(oldItemPosition).getUrlPicture(),
//                              this.mNewUsers.get(newItemPosition).getUrlPicture()) &&
//
//               Objects.equals(this.mOldUsers.get(oldItemPosition).getPlaceIdOfRestaurant(),
//                              this.mNewUsers.get(newItemPosition).getPlaceIdOfRestaurant());

        return false;
    }
}