package nl.hannito.model;

import com.itextpdf.text.List;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import java.util.HashMap;
import java.util.Map;
import javax.validation.constraints.NotNull;
import nl.hannito.generators.ExamPDFGenerator;

public class TheoremQuestion extends Question {

    @NotNull
    private String theorem1;
    @NotNull
    private String theorem2;
    @NotNull
    private char correct_answer;
    
    private Map answers;
    
    public TheoremQuestion() {
        super.question = "Gegeven zijn de volgende stellingen:";
        answers = new HashMap();
        answers.put('a', "Stelling 1 is waar en stelling 2 is niet waar");
        answers.put('b', "Stelling 2 is waar en stelling 1 is niet waar");
        answers.put('c', "Beide stellingen zijn waar");
        answers.put('d', "Beide stellingen zijn niet waar");
    }
    
    @Override
    public Paragraph generatePDF(int questionNumber) {
        Paragraph paragraph = new Paragraph(questionNumber + ". " + this.question, ExamPDFGenerator.questionFont);
        Paragraph theorem1p = new Paragraph("Stelling 1: " + theorem1, ExamPDFGenerator.subFont);
        Paragraph theorem2p = new Paragraph("Stelling 2: " + theorem2, ExamPDFGenerator.subFont);
        System.out.println(answers.keySet());
        List list = new List(false, false, 10);
        list.setListSymbol("");
        for(Object key: answers.keySet()) {
            list.add(key.toString() + ") " + answers.get(key.toString()));
            System.out.println(key.toString());
        }
        
        paragraph.add(theorem1p);
        paragraph.add(theorem2p);
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
