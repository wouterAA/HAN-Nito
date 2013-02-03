package nl.hannito.webservice;

import nl.hannito.DAO.UserDAO;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import nl.hannito.DAO.LoginDAO;
import nl.hannito.entity.User;
import nl.hannito.repository.UserPostgresRepository;
import nl.hannito.util.PasswordGenerator;

@Path("/")
@Stateless
public class AccountManager {

    @EJB UserPostgresRepository database;

    @POST
    @Path("/login")
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
    public Response login(LoginDAO loginData, @Context HttpServletRequest req) {
        loginData.setPassword(PasswordGenerator.hash(loginData.getPassword()));
        User foundUser = database.login(loginData);
        if(foundUser == null) {
            return Response.status(Status.UNAUTHORIZED).build();
        }
        req.getSession(true).setAttribute("username", foundUser.getUsername());
        System.out.println(req.getSession().getAttribute("username"));
        return Response.ok(req.getSession().getAttribute("username")).build();
    }

    @POST
    @Path("/logout")
    public Response logout(@Context HttpServletRequest req) {
        req.getSession().invalidate();
        return Response.ok().build();
    }

    @POST
    @Path("/register")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces({MediaType.APPLICATION_JSON})
    public Response register(@Context HttpServletRequest req, UserDAO newUser) {
        newUser.setPassword(PasswordGenerator.hash(newUser.getPassword()));
        System.out.println(newUser.toString());
        User foundUserByUsername = database.findByUsername(newUser.getUsername());
        User foundUserByEmail = database.findByEmail(newUser.getEmail());
        
        try {
            if(foundUserByUsername == null && foundUserByEmail == null) {
                User createdUser = database.create(new User(newUser));
                req.getSession(true).setAttribute("username", createdUser.getUsername());
                return Response.ok(createdUser.getUsername()).build();
            }
        } catch (Exception e) {}
        return Response.status(Status.CONFLICT).build();
    }
    
    @POST
    @Path("/checkusername")
    @Produces({MediaType.APPLICATION_JSON})
    public Response checkUsername(String username) {
        User foundUser = database.findByUsername(username);
        if(foundUser == null) {
            return Response.ok().build();
        }
        return Response.status(Status.CONFLICT).build();
    }
    
    @POST
    @Path("/checkemail")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces({MediaType.APPLICATION_JSON})
    public Response checkEmail(String email) {
        User foundUser = database.findByEmail(email);
        if(foundUser == null) {
            return Response.ok().build();
        }
        return Response.status(Status.CONFLICT).build();
    }
    
    @GET
    @Path("/ping")
    public Response ping(@Context HttpServletRequest req) {
        if(req.getSession(true).getAttribute("username") == null) {
            return Response.status(Status.FORBIDDEN).entity("no session with an username attribute has been set").build();
        }
        return Response.ok(req.getSession().getAttribute("username")).build();
    }
}
