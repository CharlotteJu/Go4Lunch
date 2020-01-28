package com.mancel.yann.go4lunch.models;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.firestore.PropertyName;
import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;
import java.util.Objects;

/**
 * Created by Yann MANCEL on 27/01/2020.
 * Name of the project: Go4Lunch
 * Name of the package: com.mancel.yann.go4lunch.models
 *
 * Pojo class to Firebase Firestore.
 */
public class Message {

    // FIELDS --------------------------------------------------------------------------------------

    @NonNull
    private String mMessage;

    // Firebase Firestore will initialises this field thanks to @ServerTimestamp
    @Nullable
    private Date mDateCreated;

    @NonNull
    private User mUser;

    // CONSTRUCTORS --------------------------------------------------------------------------------

    /**
     * Constructor by default
     * It is this constructor that Firebase Firestore uses.
     */
    public Message() {
        this.mMessage = "";
        this.mUser = new User();
    }

    /**
     * Constructor
     * @param message       a {@link String} that contains the message
     * @param user          a {@link User} that contains the user who has created the message
     */
    public Message(@NonNull final String message,
                   @NonNull final User user) {
        this.mMessage = message;
        this.mUser = user;
    }

    // METHODS -------------------------------------------------------------------------------------

    // -- Getter --

    @PropertyName("message")
    @NonNull
    public String getMessage() {
        return this.mMessage;
    }

    @PropertyName("date_created")
    @ServerTimestamp
    @Nullable
    public Date getDateCreated() {
        return this.mDateCreated;
    }

    @PropertyName("user")
    @NonNull
    public User getUser() {
        return this.mUser;
    }

    // -- Setter --

    @PropertyName("message")
    public void setMessage(@NonNull String message) {
        this.mMessage = message;
    }

    @PropertyName("date_created")
    public void setDateCreated(@Nullable Date dateCreated) {
        this.mDateCreated = dateCreated;
    }

    @PropertyName("user")
    public void setUser(@NonNull User user) {
        this.mUser = user;
    }

    // -- Comparison --

    @Override
    public boolean equals(@Nullable Object obj) {
        // Same address
        if (this == obj) return true;

        // Null or the class is different
        if (obj == null || getClass() != obj.getClass()) return false;

        // Cast Object to Message
        final Message message = (Message) obj;

        return Objects.equals(this.mMessage, message.mMessage) &&
               Objects.equals(this.mDateCreated, message.mDateCreated) &&
               Objects.equals(this.mUser, message.mUser);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.mMessage, this.mDateCreated);
    }
}
