package com.quizlock.gatekeeper.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.quizlock.gatekeeper.data.model.QuizHistory;
import com.quizlock.gatekeeper.data.model.QuizQuestion;
import com.quizlock.gatekeeper.data.repository.QuizRepository;

/**
 * ViewModel for QuizActivity
 */
public class QuizViewModel extends ViewModel {
    
    private final QuizRepository repository;
    private final MutableLiveData<QuizQuestion> questionLiveData = new MutableLiveData<>();
    private final MutableLiveData<AnswerResult> answerResult = new MutableLiveData<>();
    private final MutableLiveData<Boolean> loading = new MutableLiveData<>();
    
    public QuizViewModel(QuizRepository repository) {
        this.repository = repository;
    }
    
    public LiveData<QuizQuestion> getQuestionLiveData() {
        return questionLiveData;
    }
    
    public LiveData<AnswerResult> getAnswerResult() {
        return answerResult;
    }
    
    public LiveData<Boolean> getLoading() {
        return loading;
    }
    
    public void loadRandomQuestion(String category) {
        loading.setValue(true);
        
        repository.getRandomQuestion(category, null, question -> {
            loading.postValue(false);
            questionLiveData.postValue(question);
        });
    }
    
    public void submitAnswer(QuizQuestion question, String userAnswer, long timeTaken, String appPackage) {
        boolean isCorrect = question.isCorrectAnswer(userAnswer);
        
        // Save to history
        QuizHistory history = new QuizHistory(
            question.getId(),
            userAnswer,
            question.getCorrectAnswer(),
            isCorrect,
            question.getCategory(),
            question.getDifficulty(),
            appPackage,
            timeTaken
        );
        
        repository.insertHistory(history);
        
        // Return result
        answerResult.setValue(new AnswerResult(isCorrect, question.getCorrectAnswer()));
        
        if (isCorrect) {
            // Record successful unlock
            repository.recordSuccessfulUnlock(appPackage);
        } else {
            // Record blocked attempt
            repository.recordBlockedAttempt(appPackage);
        }
    }
    
    public void unlockApp(String packageName) {
        // This will be handled by the accessibility service
        // For now, we just record the unlock
        repository.recordSuccessfulUnlock(packageName);
    }
    
    /**
     * Result class for answer submission
     */
    public static class AnswerResult {
        private final boolean correct;
        private final String correctAnswer;
        
        public AnswerResult(boolean correct, String correctAnswer) {
            this.correct = correct;
            this.correctAnswer = correctAnswer;
        }
        
        public boolean isCorrect() {
            return correct;
        }
        
        public String getCorrectAnswer() {
            return correctAnswer;
        }
    }
}