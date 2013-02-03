package nl.hannito.model;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import org.bson.types.ObjectId;

public class Exam {

    protected long added_on = Calendar.getInstance().getTimeInMillis();
    private String owner;
    private String subject;
    private String instruction;
    private List<Question> questions = new ArrayList();
    private List<? extends Source> source_questions = new ArrayList();

    public Exam() {
    }

    public Exam(String owner, String subject, String instruction) {
        this.owner = owner;
        this.subject = subject;
        this.instruction = instruction;
    }

    public Exam(String owner, String subject, String instruction, List<Question> questions, List<? extends Source> source_questions) {
        this.owner = owner;
        this.subject = subject;
        this.instruction = instruction;
        this.questions = questions;
        this.source_questions = source_questions;
    }

    public long getAdded_on() {
        return added_on;
    }

    public void setAdded_on(long added_on) {
        this.added_on = added_on;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getInstruction() {
        return instruction;
    }

    public void setInstruction(String instruction) {
        this.instruction = instruction;
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }

    public List<? extends Source> getSource_questions() {
        return source_questions;
    }

    public void setSource_questions(List<? extends Source> source_questions) {
        this.source_questions = source_questions;
    }
}
