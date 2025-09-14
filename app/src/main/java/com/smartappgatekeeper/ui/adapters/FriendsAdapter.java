package com.smartappgatekeeper.ui.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.smartappgatekeeper.R;
import com.smartappgatekeeper.model.Friend;

import java.util.List;

/**
 * Friends Adapter for RecyclerView
 * Displays friends list with add/remove functionality
 */
public class FriendsAdapter extends RecyclerView.Adapter<FriendsAdapter.FriendViewHolder> {
    
    private List<Friend> friendsList;
    private OnFriendActionListener actionListener;
    
    public interface OnFriendActionListener {
        void onFriendAction(Friend friend, String action);
    }
    
    public FriendsAdapter(List<Friend> friendsList, OnFriendActionListener actionListener) {
        this.friendsList = friendsList;
        this.actionListener = actionListener;
    }
    
    @NonNull
    @Override
    public FriendViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_friend, parent, false);
        return new FriendViewHolder(view);
    }
    
    @Override
    public void onBindViewHolder(@NonNull FriendViewHolder holder, int position) {
        Friend friend = friendsList.get(position);
        holder.bind(friend);
    }
    
    @Override
    public int getItemCount() {
        return friendsList.size();
    }
    
    class FriendViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageAvatar;
        private TextView textName, textLevel, textCoins;
        private Button buttonAction;
        
        public FriendViewHolder(@NonNull View itemView) {
            super(itemView);
            imageAvatar = itemView.findViewById(R.id.image_friend_avatar);
            textName = itemView.findViewById(R.id.text_friend_name);
            textLevel = itemView.findViewById(R.id.text_friend_level);
            textCoins = itemView.findViewById(R.id.text_friend_coins);
            buttonAction = itemView.findViewById(R.id.button_friend_action);
        }
        
        public void bind(Friend friend) {
            textName.setText(friend.getName());
            textLevel.setText(friend.getLevel());
            textCoins.setText(String.format("%,d coins", friend.getCoins()));
            
            if (friend.isAdded()) {
                buttonAction.setText("Remove");
                buttonAction.setBackgroundResource(R.drawable.apple_button_secondary);
                buttonAction.setOnClickListener(v -> actionListener.onFriendAction(friend, "remove"));
            } else {
                buttonAction.setText("Add");
                buttonAction.setBackgroundResource(R.drawable.apple_button_primary);
                buttonAction.setOnClickListener(v -> actionListener.onFriendAction(friend, "add"));
            }
            
            // Set default avatar
            imageAvatar.setImageResource(R.drawable.ic_profile);
        }
    }
}
