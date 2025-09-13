package com.quizlock.gatekeeper.ui.main;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.quizlock.gatekeeper.R;
import com.quizlock.gatekeeper.data.model.QuizHistory;
import com.quizlock.gatekeeper.utils.AppInfoHelper;

import java.util.List;

/**
 * Adapter for displaying quiz history in RecyclerView
 */
public class QuizHistoryAdapter extends RecyclerView.Adapter<QuizHistoryAdapter.ViewHolder> {
    
    private List<QuizHistory> historyList;
    private Context context;
    
    public QuizHistoryAdapter(List<QuizHistory> historyList) {
        this.historyList = historyList;
    }
    
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.item_quiz_history, parent, false);
        return new ViewHolder(view);
    }
    
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        QuizHistory history = historyList.get(position);
        holder.bind(history);
    }
    
    @Override
    public int getItemCount() {
        return historyList.size();
    }
    
    public void updateData(List<QuizHistory> newData) {
        this.historyList = newData;
        notifyDataSetChanged();
    }
    
    class ViewHolder extends RecyclerView.ViewHolder {
        private View viewResultIndicator;
        private TextView tvQuestionPreview;
        private TextView tvCategory;
        private TextView tvTimeTaken;
        private TextView tvTimestamp;
        
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            viewResultIndicator = itemView.findViewById(R.id.view_result_indicator);
            tvQuestionPreview = itemView.findViewById(R.id.tv_question_preview);
            tvCategory = itemView.findViewById(R.id.tv_category);
            tvTimeTaken = itemView.findViewById(R.id.tv_time_taken);
            tvTimestamp = itemView.findViewById(R.id.tv_timestamp);
        }
        
        public void bind(QuizHistory history) {
            // Set result indicator color
            int colorRes = history.isCorrect() ? R.color.success : R.color.error;
            viewResultIndicator.setBackgroundTintList(
                ContextCompat.getColorStateList(context, colorRes)
            );
            
            // Set question preview (this would need actual question text from database)
            tvQuestionPreview.setText("Quiz question answered");
            
            // Set category with appropriate color
            String category = history.getCategory();
            tvCategory.setText(getCategoryDisplayName(category));
            int categoryColor = getCategoryColor(category);
            tvCategory.setBackgroundTintList(
                ContextCompat.getColorStateList(context, categoryColor)
            );
            
            // Set time taken
            long timeMs = history.getTimeTakenMs();
            String timeText = AppInfoHelper.formatDuration(timeMs);
            tvTimeTaken.setText(timeText);
            
            // Set timestamp
            String timeAgo = AppInfoHelper.getTimeAgoText(history.getAnsweredAt());
            tvTimestamp.setText(timeAgo);
        }
        
        private String getCategoryDisplayName(String category) {
            switch (category.toLowerCase()) {
                case "math":
                    return "Mathematics";
                case "coding":
                    return "Programming";
                case "general":
                    return "General";
                default:
                    return category;
            }
        }
        
        private int getCategoryColor(String category) {
            switch (category.toLowerCase()) {
                case "math":
                    return R.color.category_math;
                case "coding":
                    return R.color.category_coding;
                case "general":
                    return R.color.category_general;
                default:
                    return R.color.primary;
            }
        }
    }
}