package nl.hannito.webservice;

import com.sun.jersey.core.header.FormDataContentDisposition;
import com.sun.jersey.multipart.FormDataBodyPart;
import com.sun.jersey.multipart.FormDataParam;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import nl.hannito.DAO.ImageSourceDAO;
import nl.hannito.model.Image;
import nl.hannito.model.ImageUpload;
import nl.hannito.repository.ImageRepository;
import nl.hannito.repository.SourceRepository;
import org.bson.types.ObjectId;

@Stateless
@Path("/sources")
public class SourceWebservice {

    @Inject ImageRepository imageRepository;
    @Inject SourceRepository sourceRepository;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllSources() {
        return Response.ok(sourceRepository.getAllSources()).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{id}")
    public Response getSource(@PathParam("id") String objectId) {
        String foundSource = sourceRepository.getSource(objectId);
        if(foundSource == null) {
            return Response.status(Status.NOT_FOUND).build();
        }
        return Response.ok(foundSource).build();
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createSource(ImageSourceDAO image, @Context HttpServletRequest req) {
        String createdSource = sourceRepository.createSource(
                new Image(image, req.getSession(false).getAttribute("username").toString()));
        return Response.ok(createdSource).build();
    }
    
    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{id}")
    public Response deleteSource(@PathParam("id") String objectId) {
        sourceRepository.remove(objectId);
        return Response.ok().build();
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Path("/upload")
    public Response uploadImage(@FormDataParam("image") FormDataBodyPart body,
            @FormDataParam("image") InputStream uploadedInputStream,
            @FormDataParam("image") FormDataContentDisposition fileDetail,
            @Context HttpServletRequest req) {
        
        ByteArrayOutputStream inputstream = copyInputStream(uploadedInputStream);

        ObjectId imageId = new ObjectId();
        ObjectId thumbnailId = new ObjectId();

        createImage(imageId, new ByteArrayInputStream(inputstream.toByteArray()), body.getName(), 
                body.getMediaType().toString(), req.getSession(false).getAttribute("username").toString());
        createThumbnail(thumbnailId, imageId, new ByteArrayInputStream(inputstream.toByteArray()), 
                body.getName(), body.getMediaType().toString(), req.getSession(false).getAttribute("username").toString());

        return Response.ok(new ImageUpload(imageId.toString(), thumbnailId.toString())).build();
    }

    private ByteArrayOutputStream copyInputStream(InputStream inputStream) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        try {
            byte[] buffer = new byte[1024];
            int len;
            while ((len = inputStream.read(buffer)) > -1) {
                baos.write(buffer, 0, len);
            }
            baos.flush();
        } catch (IOException ex) {
            Logger.getLogger(SourceWebservice.class.getName()).log(Level.SEVERE, null, ex);
        }

        return baos;
    }

    private void createImage(ObjectId objectId, InputStream image, String name, String mediaType, String username) {
        imageRepository.createFile(objectId, image, name, mediaType, username);
    }

    private void createThumbnail(ObjectId objectId, ObjectId masterId, InputStream image, String name, String mediaType, String username) {
        imageRepository.createThumbnail(objectId, image, name, mediaType, username, masterId.toString());
    }
}
