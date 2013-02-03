package nl.hannito.model;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public abstract class Source {
    private String owner;
    private long added_on = Calendar.getInstance().getTimeInMillis();
    private List<? extends Question> questions = new ArrayList<>();

    public Source(String owner) {
        this.owner = owner;
    }
}
