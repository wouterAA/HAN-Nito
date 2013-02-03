package nl.hannito.webservice;

import com.google.gson.Gson;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import nl.hannito.entity.User;
import nl.hannito.model.Question;
import nl.hannito.model.UserProfile;
import nl.hannito.repository.QuestionRepository;
import nl.hannito.repository.UserPostgresRepository;

@Stateless
@Path("/profiles")
public class UserWebservice {
    
    @EJB UserPostgresRepository database;
    @Inject QuestionRepository mongo;
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{username}")
    public Response getUser(@PathParam("username") String username) {
        User foundUser = database.findByUsername(username);
        if(foundUser == null) {
            return Response.status(Status.NOT_FOUND).build();
        }
        Gson gson = new Gson();
        List<Question> questions = gson.fromJson(mongo.findByKeyValue("owner", foundUser.getUsername(), true), List.class);
        
        return Response.ok(new UserProfile(foundUser.getUsername(), questions)).build();
    }
    
}
