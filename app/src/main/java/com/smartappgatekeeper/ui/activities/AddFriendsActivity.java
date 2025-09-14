package com.smartappgatekeeper.ui.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.smartappgatekeeper.R;
import com.smartappgatekeeper.ui.adapters.FriendsAdapter;
import com.smartappgatekeeper.model.Friend;

import java.util.ArrayList;
import java.util.List;

/**
 * Add Friends Activity - Apple-style friend management
 * Allows users to search, add, and manage friends
 */
public class AddFriendsActivity extends AppCompatActivity {
    
    private EditText editSearchFriends;
    private Button buttonSearch;
    private RecyclerView recyclerViewFriends;
    private LinearLayout layoutSearchResults;
    private TextView textNoResults;
    
    private FriendsAdapter friendsAdapter;
    private List<Friend> friendsList = new ArrayList<>();
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friends);
        
        initViews();
        setupRecyclerView();
        setupClickListeners();
        loadSuggestedFriends();
    }
    
    private void initViews() {
        editSearchFriends = findViewById(R.id.edit_search_friends);
        buttonSearch = findViewById(R.id.button_search);
        recyclerViewFriends = findViewById(R.id.recycler_view_friends);
        layoutSearchResults = findViewById(R.id.layout_search_results);
        textNoResults = findViewById(R.id.text_no_results);
        
        // Setup back button
        findViewById(R.id.button_back).setOnClickListener(v -> finish());
    }
    
    private void setupRecyclerView() {
        friendsAdapter = new FriendsAdapter(friendsList, this::onFriendAction);
        recyclerViewFriends.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewFriends.setAdapter(friendsAdapter);
    }
    
    private void setupClickListeners() {
        buttonSearch.setOnClickListener(v -> searchFriends());
    }
    
    private void searchFriends() {
        String query = editSearchFriends.getText().toString().trim();
        if (query.isEmpty()) {
            Toast.makeText(this, "Please enter a name or email", Toast.LENGTH_SHORT).show();
            return;
        }
        
        // TODO: Implement actual search
        // For now, show mock results
        performMockSearch(query);
    }
    
    private void performMockSearch(String query) {
        friendsList.clear();
        
        // Mock search results
        if (query.toLowerCase().contains("alice")) {
            friendsList.add(new Friend("alice123", "Alice Johnson", "alice@example.com", "Level 3", 1200, false));
        }
        if (query.toLowerCase().contains("bob")) {
            friendsList.add(new Friend("bob456", "Bob Smith", "bob@example.com", "Level 5", 2100, false));
        }
        if (query.toLowerCase().contains("carol")) {
            friendsList.add(new Friend("carol789", "Carol Davis", "carol@example.com", "Level 2", 800, false));
        }
        
        if (friendsList.isEmpty()) {
            textNoResults.setVisibility(View.VISIBLE);
            recyclerViewFriends.setVisibility(View.GONE);
        } else {
            textNoResults.setVisibility(View.GONE);
            recyclerViewFriends.setVisibility(View.VISIBLE);
            friendsAdapter.notifyDataSetChanged();
        }
    }
    
    private void loadSuggestedFriends() {
        // Load suggested friends based on mutual connections, level, etc.
        friendsList.clear();
        friendsList.add(new Friend("suggested1", "Emma Wilson", "emma@example.com", "Level 4", 1500, false));
        friendsList.add(new Friend("suggested2", "Mike Brown", "mike@example.com", "Level 3", 1100, false));
        friendsList.add(new Friend("suggested3", "Sarah Lee", "sarah@example.com", "Level 5", 2200, false));
        
        friendsAdapter.notifyDataSetChanged();
    }
    
    private void onFriendAction(Friend friend, String action) {
        if ("add".equals(action)) {
            addFriend(friend);
        } else if ("remove".equals(action)) {
            removeFriend(friend);
        }
    }
    
    private void addFriend(Friend friend) {
        // TODO: Implement actual friend addition
        Toast.makeText(this, "Friend request sent to " + friend.getName(), Toast.LENGTH_SHORT).show();
        friend.setAdded(true);
        friendsAdapter.notifyDataSetChanged();
    }
    
    private void removeFriend(Friend friend) {
        // TODO: Implement actual friend removal
        Toast.makeText(this, "Removed " + friend.getName() + " from friends", Toast.LENGTH_SHORT).show();
        friendsList.remove(friend);
        friendsAdapter.notifyDataSetChanged();
    }
}
