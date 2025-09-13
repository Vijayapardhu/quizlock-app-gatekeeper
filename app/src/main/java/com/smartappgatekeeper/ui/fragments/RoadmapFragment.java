package com.smartappgatekeeper.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.smartappgatekeeper.R;

/**
 * Roadmap Fragment - Learning progress and course roadmaps
 * FR-021: System shall support multiple learning platforms
 * FR-022: System shall display course roadmaps and progress
 * FR-023: System shall track course completion and achievements
 */
public class RoadmapFragment extends Fragment {
    
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_roadmap, container, false);
        return view;
    }
    
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        TextView textTitle = view.findViewById(R.id.text_title);
        if (textTitle != null) {
            textTitle.setText("Learning Roadmap");
        }
    }
}
