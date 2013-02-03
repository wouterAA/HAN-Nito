package nl.hannito.webservice;

import com.mongodb.DBObject;
import com.mongodb.util.JSON;
import java.util.Arrays;
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
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import nl.hannito.model.QuestionFilter;
import nl.hannito.repository.IQuestionRepository;
import nl.hannito.repository.QuestionValidator;
import org.bson.types.ObjectId;

@Stateless
@Path("/questions")
public class QuestionWebservice {
    
    @Inject IQuestionRepository questionRepository;
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{id}")
    public Response getQuestion(@PathParam("id") ObjectId id) {
        String question = questionRepository.get(id);
        if(question == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.status(Response.Status.OK).entity(questionRepository.get(id)).build();
    }
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllQuestions() {
        return Response.status(Response.Status.OK).entity(questionRepository.getAll()).build();
    }
    
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createQuestion(String jsonQuestion, @Context HttpServletRequest req) {
        try {
            String question = QuestionValidator.validate(jsonQuestion);
            DBObject dbObject = (DBObject) JSON.parse(question);
            dbObject.put("owner", req.getSession(false).getAttribute("username"));
            questionRepository.create(dbObject);
            return Response.status(Response.Status.OK).entity(dbObject.toString()).build();
        }catch(ValidationException ex) {
            return Response.status(Response.Status.CONFLICT).build(); 
        }
    } 
    
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{id}")
    public Response updateQuestion(@PathParam("id") ObjectId id, String jsonQuestion) {
        try {
            String question = QuestionValidator.validate(jsonQuestion);
            DBObject dbObject = (DBObject) JSON.parse(question);
            String updatedQuestion = questionRepository.update(id, dbObject);
            if(updatedQuestion == null) {
                return Response.status(Response.Status.NOT_FOUND).build();
            }
            return Response.status(Response.Status.CREATED).entity(dbObject).build();
        }catch(ValidationException ex) {
            return Response.status(Response.Status.CONFLICT).build();
        }
    }
    
    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{id}")
    public Response deleteQuestion(@PathParam("id") String id) {
        questionRepository.delete(id);
        return Response.ok().build();
    }
    
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/search")
    public Response filterQuestions(QuestionFilter filter) {
        return Response.ok().entity(questionRepository.filterQuestions(filter)).build();
    }
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/difficulty/{difficulty}")
    public Response findQuestions(@PathParam("difficulty") String difficulty) {
        return Response.ok().entity(questionRepository.findByDifficulty(difficulty)).build();
    }
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/count")
    public Response count() {
        return Response.ok().entity(questionRepository.count()).build();
    }
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{from}-{to}")
    public Response range(@PathParam("from") int from, @PathParam("to") int to) {
        return Response.ok().entity(questionRepository.range(from, to)).build();
    }
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{key}/{value}")
    public Response findByKeyValue(@PathParam("key") String key, @PathParam("value") String value) {
        String [] allowedKeys = {"difficulty", "tags", "owner"};
        if(Arrays.asList(allowedKeys).indexOf(key) > -1) {
            return Response.ok(questionRepository.findByKeyValue(key.toLowerCase(), value, true)).build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }
}
