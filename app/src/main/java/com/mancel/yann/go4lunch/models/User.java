package com.mancel.yann.go4lunch.models;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Objects;

/**
 * Created by Yann MANCEL on 12/12/2019.
 * Name of the project: Go4Lunch
 * Name of the package: com.mancel.yann.go4lunch.models
 */
public class User {

    // FIELDS --------------------------------------------------------------------------------------

    @NonNull
    private String uid;
    @NonNull
    private String username;
    @Nullable
    private String urlPicture;
    @Nullable
    private String selectedRestaurant;

    // CONSTRUCTORS --------------------------------------------------------------------------------

    /**
     * Constructor
     * @param uid           a {@link String} that contains the uid
     * @param username      a {@link String} that contains the username
     * @param urlPicture    a {@link String} that contains the Url of the picture
     */
    public User(@NonNull String uid,
                @NonNull String username,
                @Nullable String urlPicture) {
        this.uid = uid;
        this.username = username;
        this.urlPicture = urlPicture;
        this.selectedRestaurant = null;
    }

    // METHODS -------------------------------------------------------------------------------------

    // -- Getter --

    @NonNull
    public String getUid() {
        return this.uid;
    }

    @NonNull
    public String getUsername() {
        return this.username;
    }

    @Nullable
    public String getUrlPicture() {
        return this.urlPicture;
    }

    @Nullable
    public String getSelectedRestaurant() {
        return this.selectedRestaurant;
    }

    // -- Setter --

    public void setUid(@NonNull String uid) {
        this.uid = uid;
    }

    public void setUsername(@NonNull String username) {
        this.username = username;
    }

    public void setUrlPicture(@Nullable String urlPicture) {
        this.urlPicture = urlPicture;
    }

    public void setSelectedRestaurant(@Nullable String selectedRestaurant) {
        this.selectedRestaurant = selectedRestaurant;
    }

    // -- Comparison --

    @Override
    public boolean equals(@Nullable Object obj) {
        // Same address
        if (this == obj) return true;

        // Null or the class is different
        if (obj == null || getClass() != obj.getClass()) return false;

        // Cast Object to Meeting
        final User user = (User) obj;

        return Objects.equals(this.uid, user.uid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.uid);
    }
}
