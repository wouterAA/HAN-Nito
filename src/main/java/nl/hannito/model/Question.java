package nl.hannito.model;

import java.util.Calendar;
import javax.validation.constraints.NotNull;
import nl.hannito.generators.IPDFGenerateable;

public abstract class Question implements IPDFGenerateable{ 
    protected String id;
    protected String owner;
    protected String[] tags;
    @NotNull
    protected String difficulty;
    protected long added_on = Calendar.getInstance().getTimeInMillis();
    @NotNull
    protected String question;
    @NotNull
    protected String question_type;
}
