package nl.hannito.webservice;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import nl.hannito.entity.DifficultyLevel;
import nl.hannito.repository.DifficultyLevelPostgresRepository;

@Stateless
@Path("difficultylevels")
public class DifficultyLevelWebservice {

    @EJB private DifficultyLevelPostgresRepository database;

    @POST
    @Consumes({MediaType.APPLICATION_JSON})
    public Response create(DifficultyLevel entity) {
        DifficultyLevel createdDifficultyLevel = database.create(entity);
        if(createdDifficultyLevel == null) {
            return Response.status(Status.CONFLICT).build();
        }
        return Response.ok(createdDifficultyLevel).build();
    }

    @PUT
    @Path("{id}")
    @Consumes({MediaType.APPLICATION_JSON})
    public Response edit(@PathParam("id") String id, DifficultyLevel entity) {
        DifficultyLevel foundDifficultyLevel = database.find(id);
        if(foundDifficultyLevel == null) {
            return Response.status(Status.NOT_FOUND).build();
        }
        DifficultyLevel createdDifficultyLevel = database.edit(foundDifficultyLevel, entity);
        return Response.ok(createdDifficultyLevel).build();
    }

    @DELETE
    @Path("{id}")
    public Response remove(@PathParam("id") String id) {
        try {
            database.remove(database.find(id));
            return Response.ok().build();
        } catch (Exception e) {
            return Response.status(Status.CONFLICT).build();
        }
        
    }

    @GET
    @Path("{id}")
    @Produces({MediaType.APPLICATION_JSON})
    public Response find(@PathParam("id") String id) {
        DifficultyLevel foundDifficultyLevel = database.find(id);
        if(foundDifficultyLevel == null) {
            return Response.status(Status.NOT_FOUND).build();
        }
        return Response.ok(foundDifficultyLevel).build();
    }

    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public Response findAll() {
        return Response.ok(database.findAll()).build();
    }

    @GET
    @Path("{from}/{to}")
    @Produces({MediaType.APPLICATION_JSON})
    public Response findRange(@PathParam("from") Integer from, @PathParam("to") Integer to) {
        return Response.ok(database.findRange(new int[]{from, to})).build();
    }

    @GET
    @Path("count")
    @Produces(MediaType.TEXT_PLAIN)
    public Response countREST() {
        return Response.ok(String.valueOf(database.count())).build();
    }
}
