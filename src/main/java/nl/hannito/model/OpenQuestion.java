package nl.hannito.model;

import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import javax.validation.constraints.NotNull;
import nl.hannito.generators.ExamPDFGenerator;

public class OpenQuestion extends Question {
    
    @NotNull
    private String correct_answer;
    
    @Override
    public Paragraph generatePDF(int questionNumber) {
        Paragraph paragraph = new Paragraph(questionNumber + ". " + this.question, ExamPDFGenerator.questionFont);
        ExamPDFGenerator.addEmptyLine(paragraph, 1);
        paragraph.add(new Paragraph("............................................................................................................................................................................................................................................"));
        return paragraph;
    }
    
    public OpenQuestion() {
    }

    @Override
    public String toString() {
        return super.owner;
    }

    @Override
    public Paragraph generateAnswerPDF(int questionNumber) {
        Paragraph paragraph = new Paragraph();
        Phrase letter = new Phrase(questionNumber + ". ", ExamPDFGenerator.questionFont);
        paragraph.add(letter);
        paragraph.add(new Phrase(this.correct_answer, ExamPDFGenerator.standard));
        
        return paragraph;
    }
    
    
    
    
    
}
