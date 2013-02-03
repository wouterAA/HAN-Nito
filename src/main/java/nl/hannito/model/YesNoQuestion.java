package nl.hannito.model;

import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import javax.validation.constraints.NotNull;
import nl.hannito.generators.ExamPDFGenerator;

public class YesNoQuestion extends Question {

    @NotNull
    private boolean correct_answer;
    
    @Override
    public Paragraph generatePDF(int questionNumber) {
        Paragraph paragraph = new Paragraph(questionNumber + ". " + this.question, ExamPDFGenerator.questionFont);
        paragraph.add(new Paragraph("Waar / Onwaar"));
        return paragraph;
    }

    @Override
    public Paragraph generateAnswerPDF(int questionNumber) {
        Phrase letter = new Phrase(questionNumber + ". ", ExamPDFGenerator.questionFont);
        Paragraph paragraph = new Paragraph();
        paragraph.add(letter);
        if(this.correct_answer) {
            paragraph.add(new Phrase("Waar", ExamPDFGenerator.standard));
        }
        else {
            paragraph.add(new Phrase("Onwaar", ExamPDFGenerator.standard));
        }
        return paragraph;
    }
    
}
