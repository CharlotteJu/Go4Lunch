package com.mancel.yann.go4lunch.utils;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;

import com.mancel.yann.go4lunch.models.Message;

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
        // Comparison based on:
        //  - the content of message
        //  - the date of creation of message
        //  - the user's Uid

        return Objects.equals(this.mOldMessages.get(oldItemPosition).getMessage(),
                              this.mNewMessages.get(newItemPosition).getMessage()) &&

               Objects.equals(this.mOldMessages.get(oldItemPosition).getDateCreated(),
                              this.mNewMessages.get(newItemPosition).getDateCreated()) &&

               Objects.equals(this.mOldMessages.get(oldItemPosition).getUser().getUid(),
                              this.mNewMessages.get(newItemPosition).getUser().getUid());
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        // Comparison based on:
        //  - the photo's url of user

        return Objects.equals(this.mOldMessages.get(oldItemPosition).getUser().getUrlPicture(),
                              this.mNewMessages.get(newItemPosition).getUser().getUrlPicture());
    }
}