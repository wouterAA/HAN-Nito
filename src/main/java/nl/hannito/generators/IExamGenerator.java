package nl.hannito.generators;

import java.io.OutputStream;
import java.util.Calendar;
import java.util.Date;
import nl.hannito.model.Exam;

public interface IExamGenerator {
    /**
     * Generate an exam as an outputstream (ByteArrayOutputstream for example)
     * @param exam The exam to generate a file from
     * @param semester The semester of this exam
     * @param course The course of this exam
     * @param date The date of this exam
     * @param tijdsduur The available time for this exam
     * @return Outputstream of the generated file
     */
    public OutputStream generate(Exam exam, String semester, String course, Calendar date, String tijdsduur);
    
    /**
     * Generate an answer model of an exam as an outputstream (ByteArrayOutputstream for example)
     * @param exam The exam to generate a file from
     * @param semester The semester of this exam
     * @param course The course of this exam
     * @param date The date of this exam
     * @param tijdsduur The available time for this exam
     * @return Outputstream of the generated file
     */
    public OutputStream generateAnswerSheet(Exam exam, String semester, String course, Calendar date, String tijdsduur);
}


