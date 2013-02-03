package nl.hannito.model;

import java.util.List;

public class UserProfile {

    private String username;
    private List<Question> questions;

    public UserProfile() {
    }

    public UserProfile(String username, List<Question> questions) {
        this.username = username;
        this.questions = questions;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }
}
