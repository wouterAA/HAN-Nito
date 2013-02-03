package nl.hannito.webservice;

import com.mongodb.gridfs.GridFSDBFile;
import java.util.Date;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import nl.hannito.repository.ImageRepository;

@Stateless
@Path("/images")
public class ImageWebservice {
    
    @Inject ImageRepository imageRepository;
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllImages() {
        return Response.ok(imageRepository.getAllImages()).build();
    }
    
    @DELETE
    @Path("{objectid}")
    public Response removeImages(@PathParam("objectid") String objectId) {
        imageRepository.removeImageAndItsChildren(objectId);
        return Response.ok().build();
    }
    
    @GET
    @Path("{objectid}")
    public Response getImage(@PathParam("objectid") String objectId, @HeaderParam("If-Modified-Since") String modified) {
        GridFSDBFile mongoFile = imageRepository.getImage(objectId);
        if(mongoFile != null) {
            if(modified != null) {
                if(new Date(modified).before(mongoFile.getUploadDate())) {
                    return Response.status(Status.NOT_MODIFIED).build();
                }
            }
            return Response.ok(mongoFile.getInputStream(), mongoFile.getContentType()).
                    lastModified(mongoFile.getUploadDate()).
                    build();
        }
        return Response.status(Status.NOT_FOUND).build();
    }
}
