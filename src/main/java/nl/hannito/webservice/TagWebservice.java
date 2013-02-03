package nl.hannito.webservice;

import nl.hannito.repository.TagPostgresRepository;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.servlet.http.HttpServletResponse;
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
import javax.ws.rs.core.Response.Status;
import nl.hannito.entity.Tag;

@Stateless
@Path("tags")
public class TagWebservice {
    @EJB private TagPostgresRepository database;
    
    @POST
    @Consumes({MediaType.APPLICATION_JSON})
    public Response create(Tag entity) {
        Tag createdEntity = database.create(entity);
        if(createdEntity == null) {
            return Response.status(Status.CONFLICT).build();
        }
        return Response.status(Status.CREATED).entity(createdEntity).build();
    }

    @PUT
    @Path("{id}")
    @Consumes({MediaType.APPLICATION_JSON})
    public Response edit(@PathParam("id") String id, Tag entity) {
        Tag foundTag = database.find(id);
        if(foundTag == null) {
            return Response.status(Status.NOT_FOUND).build();
        }
        Tag edittedTag = database.edit(foundTag, entity);
        return Response.ok().entity(edittedTag).build();
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
        Tag foundTag = database.find(id);
        if(foundTag == null) {
            return Response.status(Status.NOT_FOUND).build();
        }
        return Response.ok().entity(foundTag).build();
    }

    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public Response findAll(@Context HttpServletResponse response) {
        return Response.ok().entity(database.findAll()).build();
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
