package nl.hannito.generators;

import com.itextpdf.text.Paragraph;

public interface IPDFGenerateable {
    /**
     * Generate a paragraph for the ExamPDFGenerator
     * @param questionNumber the number that this question should get
     * @return itextpdf.text.paragraph
     */
    public Paragraph generatePDF(int questionNumber);
    
    /**
     * Generate a paragraph for the ExamPDFGenerator answer model
     * @param questionNumber the number that this question should get
     * @return itextpdf.text.paragraph
     */
    public Paragraph generateAnswerPDF(int questionNumber);
}
