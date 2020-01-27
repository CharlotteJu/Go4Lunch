package com.mancel.yann.go4lunch.views.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.RequestManager;
import com.mancel.yann.go4lunch.models.Message;
import com.mancel.yann.go4lunch.utils.MessageDiffCallback;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Yann MANCEL on 27/01/2020.
 * Name of the project: Go4Lunch
 * Name of the package: com.mancel.yann.go4lunch.views.adapters
 *
 * A {@link RecyclerView.Adapter} subclass.
 */
public class MessageAdapter extends RecyclerView.Adapter<MessageViewHolder> {

    // FIELDS --------------------------------------------------------------------------------------

    @NonNull
    private final AdapterListener mCallback;

    @NonNull
    private final RequestManager mGlide;

    @NonNull
    private List<Message> mMessages;

    // CONSTRUCTORS --------------------------------------------------------------------------------

    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.
     * @param callback  a {@link AdapterListener} for the callback system
     * @param glide     a {@link RequestManager}
     */
    public MessageAdapter(@NonNull final AdapterListener callback,
                          @NonNull final RequestManager glide) {
        this.mCallback = callback;
        this.mGlide = glide;
        this.mMessages = new ArrayList<>();
    }

    // METHODS -------------------------------------------------------------------------------------

    // -- RecyclerView.Adapter --

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Creates a Context to the LayoutInflater
        final Context context = parent.getContext();
        final LayoutInflater layoutInflater = LayoutInflater.from(context);

        // Creates the View thanks to the inflater
        final View view = layoutInflater.inflate(MessageViewHolder.getLayout(), parent, false);

        return new MessageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
        holder.updateMessage(this.mMessages.get(position), this.mGlide);
    }

    @Override
    public int getItemCount() {
        return this.mMessages.size();
    }

    // -- Messages --

    /**
     * Updates the data thanks to the {@link List<Message>} in argument
     * @param newMessages a {@link List<Message>} that contains the new data
     */
    public void updateData(@NonNull final List<Message> newMessages) {
        // Optimizes the performances of RecyclerView
        final MessageDiffCallback diffCallback = new MessageDiffCallback(this.mMessages, newMessages);
        final DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(diffCallback);

        // New data
        this.mMessages = newMessages;

        // Notifies adapter
        diffResult.dispatchUpdatesTo(this);

        // Callback to update UI
        this.mCallback.onDataChanged();
    }
}
