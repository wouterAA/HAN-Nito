package nl.hannito.model;

public class QuestionFilter {
    private String[] question_type;
    private String[] difficulty;
    private String[] tags;
    
    
    public String[] getQuestion_type() {
        return question_type;
    }

    public void setQuestion_type(String[] question_type) {
        this.question_type = question_type;
    }

    public String[] getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String[] difficulty) {
        this.difficulty = difficulty;
    }

    public String[] getTags() {
        return tags;
    }

    public void setTags(String[] tags) {
        this.tags = tags;
    }
}
