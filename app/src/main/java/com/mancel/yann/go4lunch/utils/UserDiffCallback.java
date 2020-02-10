package com.mancel.yann.go4lunch.utils;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;

import com.mancel.yann.go4lunch.models.User;

import java.util.List;
import java.util.Objects;

/**
 * Created by Yann MANCEL on 08/01/2020.
 * Name of the project: Go4Lunch
 * Name of the package: com.mancel.yann.go4lunch.utils
 *
 * A {@link DiffUtil.Callback} subclass.
 */
public class UserDiffCallback extends DiffUtil.Callback {

    // FIELDS --------------------------------------------------------------------------------------

    @NonNull
    private final List<User> mOldUsers;

    @NonNull
    private final List<User> mNewUsers;

    // CONSTRUCTORS --------------------------------------------------------------------------------

    /**
     * Constructor with the odl and new lists
     * @param oldUsers a {@link List<User>} that contains the old list
     * @param newUsers a {@link List<User>} that contains the new list
     */
    public UserDiffCallback(@NonNull final List<User> oldUsers,
                            @NonNull final List<User> newUsers) {
        this.mOldUsers = oldUsers;
        this.mNewUsers = newUsers;
    }

    // METHODS -------------------------------------------------------------------------------------

    // -- DiffUtil.Callback --

    @Override
    public int getOldListSize() {
        return this.mOldUsers.size();
    }

    @Override
    public int getNewListSize() {
        return this.mNewUsers.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        // Comparison based on the user's Uid
        final String oldName = this.mOldUsers.get(oldItemPosition).getUid();
        final String newName = this.mNewUsers.get(newItemPosition).getUid();

        return oldName.equals(newName);
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        // Comparison based on:
        //  - the name of user
        //  - the photo's url of user
        //  - the place Id of selected restaurant

        return Objects.equals(this.mOldUsers.get(oldItemPosition).getUsername(),
                              this.mNewUsers.get(newItemPosition).getUsername()) &&

               Objects.equals(this.mOldUsers.get(oldItemPosition).getUrlPicture(),
                              this.mNewUsers.get(newItemPosition).getUrlPicture()) &&

               Objects.equals(this.mOldUsers.get(oldItemPosition).getPlaceIdOfRestaurant(),
                              this.mNewUsers.get(newItemPosition).getPlaceIdOfRestaurant());
    }
}