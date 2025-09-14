package com.smartappgatekeeper.service;

import com.smartappgatekeeper.model.Question;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Service for managing comprehensive question bank
 * Provides questions across multiple topics and difficulty levels
 */
public class QuestionBankService {
    
    private static QuestionBankService instance;
    private List<Question> allQuestions;
    private Random random;
    
    private QuestionBankService() {
        random = new Random();
        allQuestions = new ArrayList<>();
        initializeQuestionBank();
    }
    
    public static QuestionBankService getInstance() {
        if (instance == null) {
            instance = new QuestionBankService();
        }
        return instance;
    }
    
    /**
     * Initialize comprehensive question bank
     */
    private void initializeQuestionBank() {
        // Programming Questions
        addProgrammingQuestions();
        
        // Mathematics Questions
        addMathematicsQuestions();
        
        // Science Questions
        addScienceQuestions();
        
        // General Knowledge Questions
        addGeneralKnowledgeQuestions();
        
        // Technology Questions
        addTechnologyQuestions();
        
        // History Questions
        addHistoryQuestions();
        
        // Geography Questions
        addGeographyQuestions();
    }
    
    /**
     * Add programming questions
     */
    private void addProgrammingQuestions() {
        // Easy Programming Questions
        addQuestion("What does HTML stand for?", 
            "HyperText Markup Language", "High Tech Modern Language", 
            "Home Tool Markup Language", "Hyperlink and Text Markup Language", 
            "A", "HTML is the standard markup language for creating web pages.", 
            "Programming", "easy");
            
        addQuestion("Which programming language is known for its use in data science?", 
            "Java", "Python", "C++", "JavaScript", 
            "B", "Python is widely used in data science due to its libraries like NumPy and Pandas.", 
            "Programming", "easy");
            
        addQuestion("What is the output of: print(2 + 3 * 4) in Python?", 
            "20", "14", "11", "12", 
            "B", "Python follows order of operations: 3 * 4 = 12, then 2 + 12 = 14.", 
            "Programming", "medium");
            
        addQuestion("Which keyword is used to define a function in Python?", 
            "function", "def", "func", "define", 
            "B", "The 'def' keyword is used to define functions in Python.", 
            "Programming", "easy");
            
        addQuestion("What does API stand for?", 
            "Application Programming Interface", "Advanced Programming Interface", 
            "Automated Programming Interface", "Application Process Interface", 
            "A", "API stands for Application Programming Interface.", 
            "Programming", "medium");
            
        addQuestion("Which data structure follows LIFO principle?", 
            "Queue", "Stack", "Array", "Linked List", 
            "B", "Stack follows Last In, First Out (LIFO) principle.", 
            "Programming", "medium");
            
        addQuestion("What is the time complexity of binary search?", 
            "O(n)", "O(log n)", "O(n²)", "O(1)", 
            "B", "Binary search has O(log n) time complexity.", 
            "Programming", "hard");
            
        addQuestion("Which design pattern ensures only one instance of a class exists?", 
            "Factory", "Singleton", "Observer", "Builder", 
            "B", "Singleton pattern ensures only one instance of a class exists.", 
            "Programming", "hard");
    }
    
    /**
     * Add mathematics questions
     */
    private void addMathematicsQuestions() {
        addQuestion("What is the derivative of x²?", 
            "x", "2x", "x²", "2x²", 
            "B", "The derivative of x² is 2x using the power rule.", 
            "Mathematics", "medium");
            
        addQuestion("What is the value of π (pi) to 2 decimal places?", 
            "3.14", "3.15", "3.16", "3.13", 
            "A", "π is approximately 3.14159, so 3.14 to 2 decimal places.", 
            "Mathematics", "easy");
            
        addQuestion("What is the square root of 144?", 
            "11", "12", "13", "14", 
            "B", "12 × 12 = 144, so the square root of 144 is 12.", 
            "Mathematics", "easy");
            
        addQuestion("What is the area of a circle with radius 5?", 
            "25π", "10π", "50π", "5π", 
            "A", "Area of circle = πr² = π(5)² = 25π.", 
            "Mathematics", "medium");
            
        addQuestion("What is the sum of angles in a triangle?", 
            "90°", "180°", "270°", "360°", 
            "B", "The sum of angles in any triangle is always 180°.", 
            "Mathematics", "easy");
            
        addQuestion("What is 2 to the power of 8?", 
            "128", "256", "512", "1024", 
            "B", "2⁸ = 2 × 2 × 2 × 2 × 2 × 2 × 2 × 2 = 256.", 
            "Mathematics", "medium");
            
        addQuestion("What is the integral of 2x?", 
            "x²", "x² + C", "2x²", "2x² + C", 
            "B", "The integral of 2x is x² + C (where C is the constant of integration).", 
            "Mathematics", "hard");
    }
    
    /**
     * Add science questions
     */
    private void addScienceQuestions() {
        addQuestion("What is the chemical symbol for gold?", 
            "Go", "Gd", "Au", "Ag", 
            "C", "Au is the chemical symbol for gold (from Latin 'aurum').", 
            "Science", "easy");
            
        addQuestion("What is the speed of light in vacuum?", 
            "300,000 km/s", "300,000,000 m/s", "3 × 10⁸ m/s", "All of the above", 
            "D", "The speed of light is approximately 3 × 10⁸ m/s or 300,000 km/s.", 
            "Science", "medium");
            
        addQuestion("What is the largest planet in our solar system?", 
            "Earth", "Jupiter", "Saturn", "Neptune", 
            "B", "Jupiter is the largest planet in our solar system.", 
            "Science", "easy");
            
        addQuestion("What is the pH of pure water?", 
            "6", "7", "8", "9", 
            "B", "Pure water has a pH of 7, which is neutral.", 
            "Science", "medium");
            
        addQuestion("What is the process by which plants make their food?", 
            "Respiration", "Photosynthesis", "Digestion", "Fermentation", 
            "B", "Photosynthesis is the process by which plants convert sunlight into food.", 
            "Science", "easy");
            
        addQuestion("What is the atomic number of carbon?", 
            "6", "12", "14", "18", 
            "A", "Carbon has an atomic number of 6, meaning it has 6 protons.", 
            "Science", "medium");
            
        addQuestion("What is the unit of electric current?", 
            "Volt", "Ampere", "Ohm", "Watt", 
            "B", "Ampere (A) is the unit of electric current.", 
            "Science", "medium");
    }
    
    /**
     * Add general knowledge questions
     */
    private void addGeneralKnowledgeQuestions() {
        addQuestion("What is the capital of Australia?", 
            "Sydney", "Melbourne", "Canberra", "Perth", 
            "C", "Canberra is the capital of Australia.", 
            "General Knowledge", "easy");
            
        addQuestion("Who painted the Mona Lisa?", 
            "Vincent van Gogh", "Pablo Picasso", "Leonardo da Vinci", "Michelangelo", 
            "C", "Leonardo da Vinci painted the Mona Lisa.", 
            "General Knowledge", "easy");
            
        addQuestion("What is the smallest country in the world?", 
            "Monaco", "Vatican City", "Liechtenstein", "San Marino", 
            "B", "Vatican City is the smallest country in the world.", 
            "General Knowledge", "medium");
            
        addQuestion("What year did World War II end?", 
            "1944", "1945", "1946", "1947", 
            "B", "World War II ended in 1945.", 
            "General Knowledge", "easy");
            
        addQuestion("What is the currency of Japan?", 
            "Won", "Yuan", "Yen", "Dollar", 
            "C", "The currency of Japan is the Yen.", 
            "General Knowledge", "easy");
            
        addQuestion("Who wrote '1984'?", 
            "George Orwell", "Aldous Huxley", "Ray Bradbury", "H.G. Wells", 
            "A", "George Orwell wrote the novel '1984'.", 
            "General Knowledge", "medium");
    }
    
    /**
     * Add technology questions
     */
    private void addTechnologyQuestions() {
        addQuestion("What does CPU stand for?", 
            "Central Processing Unit", "Computer Processing Unit", 
            "Central Program Unit", "Computer Program Unit", 
            "A", "CPU stands for Central Processing Unit.", 
            "Technology", "easy");
            
        addQuestion("What is the full form of URL?", 
            "Uniform Resource Locator", "Universal Resource Locator", 
            "Uniform Resource Link", "Universal Resource Link", 
            "A", "URL stands for Uniform Resource Locator.", 
            "Technology", "easy");
            
        addQuestion("What does RAM stand for?", 
            "Random Access Memory", "Read Access Memory", 
            "Random Available Memory", "Read Available Memory", 
            "A", "RAM stands for Random Access Memory.", 
            "Technology", "easy");
            
        addQuestion("What is the primary purpose of a firewall?", 
            "Speed up internet", "Block unauthorized access", 
            "Store data", "Display graphics", 
            "B", "A firewall's primary purpose is to block unauthorized access.", 
            "Technology", "medium");
            
        addQuestion("What does HTTP stand for?", 
            "HyperText Transfer Protocol", "High Tech Transfer Protocol", 
            "HyperText Transport Protocol", "High Tech Transport Protocol", 
            "A", "HTTP stands for HyperText Transfer Protocol.", 
            "Technology", "medium");
    }
    
    /**
     * Add history questions
     */
    private void addHistoryQuestions() {
        addQuestion("Who was the first person to walk on the moon?", 
            "Buzz Aldrin", "Neil Armstrong", "John Glenn", "Alan Shepard", 
            "B", "Neil Armstrong was the first person to walk on the moon.", 
            "History", "easy");
            
        addQuestion("In which year did the Berlin Wall fall?", 
            "1987", "1988", "1989", "1990", 
            "C", "The Berlin Wall fell in 1989.", 
            "History", "medium");
            
        addQuestion("Who was the first President of the United States?", 
            "Thomas Jefferson", "John Adams", "George Washington", "Benjamin Franklin", 
            "C", "George Washington was the first President of the United States.", 
            "History", "easy");
    }
    
    /**
     * Add geography questions
     */
    private void addGeographyQuestions() {
        addQuestion("What is the longest river in the world?", 
            "Amazon", "Nile", "Mississippi", "Yangtze", 
            "B", "The Nile River is the longest river in the world.", 
            "Geography", "easy");
            
        addQuestion("What is the largest ocean on Earth?", 
            "Atlantic", "Pacific", "Indian", "Arctic", 
            "B", "The Pacific Ocean is the largest ocean on Earth.", 
            "Geography", "easy");
            
        addQuestion("Which continent is known as the 'Dark Continent'?", 
            "Asia", "Africa", "South America", "Australia", 
            "B", "Africa is sometimes called the 'Dark Continent'.", 
            "Geography", "medium");
    }
    
    /**
     * Add a question to the bank
     */
    private void addQuestion(String question, String optionA, String optionB, 
                           String optionC, String optionD, String correctAnswer, 
                           String explanation, String topic, String difficulty) {
        Question q = new Question();
        q.setQuestionText(question);
        q.setOptionA(optionA);
        q.setOptionB(optionB);
        q.setOptionC(optionC);
        q.setOptionD(optionD);
        q.setCorrectAnswer(correctAnswer);
        q.setExplanation(explanation);
        q.setTopic(topic);
        q.setDifficulty(difficulty);
        q.setActive(true);
        allQuestions.add(q);
    }
    
    /**
     * Get questions by topic
     */
    public List<Question> getQuestionsByTopic(String topic) {
        List<Question> topicQuestions = new ArrayList<>();
        for (Question question : allQuestions) {
            if (question.getTopic().equals(topic)) {
                topicQuestions.add(question);
            }
        }
        return topicQuestions;
    }
    
    /**
     * Get questions by difficulty
     */
    public List<Question> getQuestionsByDifficulty(String difficulty) {
        List<Question> difficultyQuestions = new ArrayList<>();
        for (Question question : allQuestions) {
            if (question.getDifficulty().equals(difficulty)) {
                difficultyQuestions.add(question);
            }
        }
        return difficultyQuestions;
    }
    
    /**
     * Get random questions
     */
    public List<Question> getRandomQuestions(int count) {
        List<Question> randomQuestions = new ArrayList<>();
        List<Question> availableQuestions = new ArrayList<>(allQuestions);
        
        for (int i = 0; i < count && !availableQuestions.isEmpty(); i++) {
            int randomIndex = random.nextInt(availableQuestions.size());
            randomQuestions.add(availableQuestions.remove(randomIndex));
        }
        
        return randomQuestions;
    }
    
    /**
     * Get all available topics
     */
    public List<String> getAvailableTopics() {
        List<String> topics = new ArrayList<>();
        for (Question question : allQuestions) {
            if (!topics.contains(question.getTopic())) {
                topics.add(question.getTopic());
            }
        }
        return topics;
    }
    
    /**
     * Get all questions
     */
    public List<Question> getAllQuestions() {
        return new ArrayList<>(allQuestions);
    }
    
    /**
     * Get question count by topic
     */
    public int getQuestionCountByTopic(String topic) {
        int count = 0;
        for (Question question : allQuestions) {
            if (question.getTopic().equals(topic)) {
                count++;
            }
        }
        return count;
    }
    
    /**
     * Get total question count
     */
    public int getTotalQuestionCount() {
        return allQuestions.size();
    }
}
