package nl.hannito.generators;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;
import nl.hannito.model.Exam;
import nl.hannito.model.Question;

public class ExamPDFGenerator implements IExamGenerator {

    private static String FILE = "c:/temp/FirstPdf.pdf";
    public static Font titleFont = new Font(Font.FontFamily.TIMES_ROMAN, 24,
        Font.BOLD);
    public static Font subTitleFont = new Font(Font.FontFamily.TIMES_ROMAN, 18,
        Font.BOLD);
    public static Font redFont = new Font(Font.FontFamily.TIMES_ROMAN, 12,
        Font.NORMAL, BaseColor.RED);
    public static Font questionFont = new Font(Font.FontFamily.TIMES_ROMAN, 12,
        Font.BOLD);
    public static Font subFont = new Font(Font.FontFamily.TIMES_ROMAN, 12,
        Font.ITALIC);
    public static Font smallBold = new Font(Font.FontFamily.TIMES_ROMAN, 12,
        Font.BOLD);
    public static Font standard = new Font(Font.FontFamily.TIMES_ROMAN, 12,
        Font.NORMAL);
    
    @Override
    public ByteArrayOutputStream generate(Exam exam, String semester, String course, Calendar date, String tijdsduur) {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        try {
            Document document = new Document();
            PdfWriter.getInstance(document, output);
            document.open();
            addMetaData(document, exam);
            addTitlePage(document, exam, semester, course, date, tijdsduur, false);
            addContent(document, exam, false);
            document.close();
        } catch (DocumentException ex) {
            Logger.getLogger(ExamPDFGenerator.class.getName()).log(Level.SEVERE, null, ex);
        }
        ByteArrayOutputStream output2 = new ByteArrayOutputStream();
        try{
            PdfReader reader = new PdfReader(output.toByteArray());
            // Create a stamper
            PdfStamper stamper
                = new PdfStamper(reader, output2);
            // Loop over the pages and add a header to each page
            int n = reader.getNumberOfPages();
            for (int i = 1; i <= n; i++) {
                getHeaderTable(i, n, semester, course).writeSelectedRows(
                        0, -1, 34, 803, stamper.getOverContent(i));
            }
            // Close the stamper
            stamper.close();
            reader.close();
        } catch (DocumentException | IOException ex) {
                Logger.getLogger(ExamPDFGenerator.class.getName()).log(Level.SEVERE, null, ex);
         }
        
        
        return output2;
    }
    
     public static PdfPTable getHeaderTable(int x, int y, String semester, String course) {
        PdfPTable table = new PdfPTable(2);
        table.setTotalWidth(527);
        table.setLockedWidth(true);
        table.getDefaultCell().setFixedHeight(20);
        table.getDefaultCell().setBorder(Rectangle.BOTTOM);
        table.addCell(semester + " - " + course);
        table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
        table.addCell(String.format("Pagina %d van %d", x, y));
        return table;
    }
    
    @Override
    public ByteArrayOutputStream generateAnswerSheet(Exam exam, String semester, String course, Calendar date, String tijdsduur) {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        try {
            Document document = new Document();
            PdfWriter.getInstance(document, output);
            document.open();
            addMetaData(document, exam);
            addTitlePage(document, exam, semester, course, date, tijdsduur, true);
            addContent(document, exam, true);
            document.close();
        } catch (DocumentException ex) {
            Logger.getLogger(ExamPDFGenerator.class.getName()).log(Level.SEVERE, null, ex);
        }
        return output;
    }
    
    
    /**
     * Adds meta data to the given document
     * @param document The document to add the metadata to
     * @param exam The exam for which this metadata is
     */
    private static void addMetaData(Document document, Exam exam) {
        document.setMargins(50, 50, 50, 50);
        document.addTitle(exam.getSubject());
        document.addSubject(exam.getSubject());
        //still static
        document.addKeywords("Java, PDF, iText");
        document.addAuthor(exam.getOwner());
        document.addCreator("HAN-Nito");
  }
    
    /**
     * Adds a title page to the given document
     * @param document Document to add a title page to
     * @param exam Exam to generate title page for
     * @param semester Semester of the exam
     * @param course Course of the exam
     * @param date Date of the exam
     * @param tijdsduur Time available for the exam
     * @param answerModel Boolean whether this is for an answermodel
     */
    private static void addTitlePage(Document document, Exam exam, String semester, String course, Calendar date, String tijdsduur, boolean answerModel) {
        try {
            Paragraph preface = new Paragraph();
            addEmptyLine(preface, 5);
            Paragraph school = new Paragraph("Hogeschool van Arnhem en Nijmegen", titleFont);
            school.setAlignment(Element.ALIGN_CENTER);
            preface.add(school);
            Paragraph afdeling = new Paragraph("Informatica Communicatie Academie", subTitleFont);
            afdeling.setAlignment(Element.ALIGN_CENTER);
            preface.add(afdeling);
            addEmptyLine(preface, 4);

            Paragraph title = new Paragraph(semester + " - " + course, subTitleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            preface.add(title);
            
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
            
            Paragraph datum = new Paragraph(sdf.format(date.getTime()), subTitleFont);
            datum.setAlignment(Element.ALIGN_CENTER);
            preface.add(datum);
            
            addEmptyLine(preface, 12);
            
            if(answerModel) {
                Paragraph answers = new Paragraph("Antwoordmodel", subTitleFont);
                answers.setAlignment(Element.ALIGN_CENTER);
                preface.add(answers);
            }
            else {
                PdfPTable table = new PdfPTable(2);
                PdfPCell naam = new PdfPCell(new Phrase("Naam"));
                PdfPCell studentnummer = new PdfPCell(new Phrase("Studentnummer"));
                PdfPCell klas = new PdfPCell(new Phrase("Klas"));
                PdfPCell docent = new PdfPCell(new Phrase("Docent"));
                PdfPCell dots = new PdfPCell(new Phrase("............................................................"));
                naam.setBorder(Rectangle.NO_BORDER);
                studentnummer.setBorder(Rectangle.NO_BORDER);
                klas.setBorder(Rectangle.NO_BORDER);
                dots.setBorder(Rectangle.NO_BORDER);
                docent.setBorder(Rectangle.NO_BORDER);
                table.addCell(naam);
                table.addCell(dots);
                table.addCell(studentnummer);
                table.addCell(dots);
                table.addCell(klas);
                table.addCell(dots);
                table.addCell(docent);
                table.addCell(dots);
                preface.add(table);
            }
            

            addEmptyLine(preface, 2);
            
            Paragraph tijdsduurParagraph = new Paragraph("Tijdsduur: " + tijdsduur + " minuten", subTitleFont);
            tijdsduurParagraph.setAlignment(Element.ALIGN_CENTER);
            preface.add(tijdsduurParagraph);
            
            Paragraph aantalVragen = new Paragraph("Aantal vragen: " + exam.getQuestions().size(), subTitleFont);
            aantalVragen.setAlignment(Element.ALIGN_CENTER);
            preface.add(aantalVragen);
            
            Paragraph instruction = new Paragraph(exam.getInstruction(), smallBold);
            instruction.setAlignment(Element.ALIGN_CENTER);
            preface.add(instruction);

            preface.add(new Paragraph("",
                redFont));
            document.add(preface);

            document.newPage();
        } catch (DocumentException ex) {
            Logger.getLogger(ExamPDFGenerator.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * Add content to the given document, the content are the questions
     * @param document The document to add content to
     * @param exam The exam of which the questions should be asses
     * @param answerModel Boolean whether this is an answersheet
     * @throws DocumentException 
     */
    private static void addContent(Document document, Exam exam, boolean answerModel) throws DocumentException {
        int i = 1;
        for(Question question: exam.getQuestions()) {
            document.add(new Paragraph(" "));
            PdfPTable table = new PdfPTable(1);
            PdfPCell cell = new PdfPCell();
            cell.setBorder(Rectangle.NO_BORDER);
            Paragraph questionParagraph;
            if(answerModel) {
                questionParagraph = question.generateAnswerPDF(i);
            }
            else {
                questionParagraph = question.generatePDF(i);
            }
            cell.addElement(questionParagraph);
            cell.setPadding(0);
            cell.setUseBorderPadding(false);
            table.addCell(cell);
            table.setKeepTogether(true);
//            questionParagraph.add(0, new Paragraph(" "));
            //addEmptyLine(questionParagraph, 1);
          //  questionParagraph.setKeepTogether(true);
            document.add(table);
            
            i++;
        }
  }
    
    /**
     * adds one or more empty lines to the paragraph
     * @param paragraph the paragraph to add the lines to
     * @param number the number of empty lines you wish to be added
     */
    public static void addEmptyLine(Paragraph paragraph, int number) {
        for (int i = 0; i < number; i++) {
          paragraph.add(new Paragraph(" "));
        }
   }

    
    
    
    
    
}
