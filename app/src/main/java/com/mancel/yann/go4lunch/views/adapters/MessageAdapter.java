package com.mancel.yann.go4lunch.views.adapters;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.RequestManager;
import com.mancel.yann.go4lunch.R;
import com.mancel.yann.go4lunch.models.Message;
import com.mancel.yann.go4lunch.utils.MessageDiffCallback;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Yann MANCEL on 27/01/2020.
 * Name of the project: Go4Lunch
 * Name of the package: com.mancel.yann.go4lunch.views.adapters
 *
 * A {@link RecyclerView.Adapter} subclass.
 */
public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {

    // FIELDS --------------------------------------------------------------------------------------

    @NonNull
    private final AdapterListener mCallback;

    @NonNull
    private final RequestManager mGlide;

    @NonNull
    private final String mUidOfCurrentUser;

    @NonNull
    private List<Message> mMessages;

    // CONSTRUCTORS --------------------------------------------------------------------------------

    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.
     * @param callback          a {@link AdapterListener} for the callback system
     * @param glide             a {@link RequestManager}
     * @param uidOfCurrentUser  a {@link String} that contains the uid of current user
     */
    public MessageAdapter(@NonNull final AdapterListener callback,
                          @NonNull final RequestManager glide,
                          @NonNull final String uidOfCurrentUser) {
        this.mCallback = callback;
        this.mGlide = glide;
        this.mUidOfCurrentUser = uidOfCurrentUser;
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
        final View view = layoutInflater.inflate(MessageAdapter.MessageViewHolder.getLayout(), parent, false);

        return new MessageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
        holder.updateMessage(this.mMessages.get(position), this.mGlide, this.mUidOfCurrentUser);
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

    // INNER CLASSES -------------------------------------------------------------------------------

    /**
     * A {@link RecyclerView.ViewHolder} subclass.
     */
    public static class MessageViewHolder extends RecyclerView.ViewHolder {

        // FIELDS ----------------------------------------------------------------------------------

        @BindView(R.id.item_chat_ConstraintLayout)
        ConstraintLayout mConstraintLayout;
        @BindView(R.id.item_chat_image)
        ImageView mImage;
        @BindView(R.id.item_chat_message)
        TextView mMessage;
        @BindView(R.id.item_chat_hours)
        TextView mHours;

        // METHODS ---------------------------------------------------------------------------------

        /**
         * Constructor
         * @param itemView a {@link View}
         */
        public MessageViewHolder(@NonNull View itemView) {
            super(itemView);

            // Using the ButterKnife library
            ButterKnife.bind(this, itemView);
        }

        // METHODS ---------------------------------------------------------------------------------

        // -- Layout --

        /**
         * Returns the layout value
         * @return an integer that contains the layout value
         */
        public static int getLayout() {
            return R.layout.item_message;
        }

        // -- Update UI --

        /**
         * Updates the item
         * @param message           a {@link Message} that allows to update the item
         * @param glide             a {@link RequestManager}
         * @param uidOfCurrentUser  a {@link String} that contains the uid of current user
         */
        public void updateMessage(@NonNull final Message message,
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
}
