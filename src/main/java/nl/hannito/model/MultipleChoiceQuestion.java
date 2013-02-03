package nl.hannito.model;

import com.itextpdf.text.List;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import java.util.Map;
import java.util.Set;
import javax.validation.constraints.NotNull;
import nl.hannito.generators.ExamPDFGenerator;

public class MultipleChoiceQuestion extends Question{
    @NotNull
    private char correct_answer;
    @NotNull
    private Map answers;

    @Override
    public Paragraph generatePDF(int questionNumber) {
        
        Paragraph paragraph = new Paragraph(questionNumber + ". " + this.question, ExamPDFGenerator.questionFont);
        System.out.println(answers.keySet());
        List list = new List(false, false, 10);
        list.setListSymbol("");
        for(Object key: answers.keySet()) {
            list.add(key.toString() + ") " + answers.get(key.toString()));
            System.out.println(key.toString());
        }
        paragraph.add(list);
        return paragraph;
    }

    @Override
    public Paragraph generateAnswerPDF(int questionNumber) {
        String answer = this.correct_answer + "";
        Phrase letter = new Phrase(questionNumber + ". ", ExamPDFGenerator.questionFont);
        Paragraph paragraph = new Paragraph();
        
        paragraph.add(letter);
        paragraph.add(new Phrase(answer.toUpperCase(), ExamPDFGenerator.standard));
        return paragraph;
    }
}
