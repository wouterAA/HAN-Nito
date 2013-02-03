package nl.hannito.webservice;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Calendar;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.validation.ValidationException;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import nl.hannito.generators.ExamPDFGenerator;
import nl.hannito.model.Exam;
import nl.hannito.repository.ExamRepository;
import nl.hannito.repository.ExamValidator;
import org.bson.types.ObjectId;

@Stateless
@Path("/exams")
public class ExamWebservice {
    
    @Inject ExamRepository examRepository;
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAll() {
        return Response.ok(examRepository.getAll()).build();
    }
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{exam}")
    public Response getExam(@PathParam("exam") String exam) {
        return Response.ok(examRepository.get(new ObjectId(exam))).build();
    }
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/owner/{username}")
    public Response getUsersExams(@PathParam("username") String username) {
        return Response.ok(examRepository.get(username)).build();
    }
    
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addExam(String jsonQuestion, @Context HttpServletRequest req) {
        try {
            System.out.println(jsonQuestion);
            Exam exam = ExamValidator.validate(jsonQuestion);
            exam.setOwner(req.getSession(false).getAttribute("username").toString());
            String createdExam = examRepository.create(exam);
            return Response.ok(createdExam).build();
        }catch(ValidationException ex) {
            return Response.status(Status.CONFLICT).build(); 
        }
    }
    
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{exam}")
    public Response updateExam(String jsonExam, @PathParam("exam") String examid, @Context HttpServletRequest req) {
        try {
            System.out.println(jsonExam);
            Exam exam = ExamValidator.validate(jsonExam);
            if(exam.getOwner().equals(req.getSession(false).getAttribute("username").toString())){ 
                System.out.println("User is eigenaar");
                String createdExam = examRepository.update(exam, examid);
                return Response.ok(createdExam).build();
            }
            return Response.status(Status.FORBIDDEN).build();
        }catch(ValidationException ex) {
            return Response.status(Status.CONFLICT).build(); 
        }
    }
    
    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{exam}")
    public Response deleteExam(@PathParam("exam") String exam) {
        examRepository.delete(exam);
        return Response.ok().build();
    }
    
    @GET
    @Produces()
    @Path("/{exam}.pdf")
    public Response getExamPDF(@QueryParam("semester") String semester, @QueryParam("course") String course, @QueryParam("date") long dateLong, @QueryParam("duration") String tijdsduur, @PathParam("exam") String examid) {
        ExamPDFGenerator generator = new ExamPDFGenerator();
        String examstring = examRepository.get(new ObjectId(examid));

        Exam exam = ExamValidator.validate(examstring);
        Calendar date = Calendar.getInstance();
        date.setTimeInMillis(dateLong);
        ByteArrayOutputStream output = generator.generate(exam, semester, course, date, tijdsduur);
        
        byte[] data = output.toByteArray();
    ByteArrayInputStream inputstream = new ByteArrayInputStream(data);
        
        return Response.ok(inputstream, "application/pdf").build();
    }
    
    @GET
    @Produces()
    @Path("/{exam}_answers.pdf")
    public Response getExamAnswersPDF(@QueryParam("semester") String semester, @QueryParam("course") String course, @QueryParam("date") long dateLong, @QueryParam("duration") String tijdsduur, @PathParam("exam") String examid) {
        ExamPDFGenerator generator = new ExamPDFGenerator();
        String examstring = examRepository.get(new ObjectId(examid));

        Exam exam = ExamValidator.validate(examstring);
        Calendar date = Calendar.getInstance();
        date.setTimeInMillis(dateLong);
        ByteArrayOutputStream output = generator.generateAnswerSheet(exam, semester, course, date, tijdsduur);
        
        byte[] data = output.toByteArray();
    ByteArrayInputStream inputstream = new ByteArrayInputStream(data);
        
        return Response.ok(inputstream, "application/pdf").build();
    }

}
