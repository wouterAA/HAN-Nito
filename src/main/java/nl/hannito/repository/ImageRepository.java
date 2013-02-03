package nl.hannito.repository;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.gridfs.GridFS;
import com.mongodb.gridfs.GridFSDBFile;
import com.mongodb.gridfs.GridFSInputFile;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import nl.hannito.util.ImageResizer;
import org.bson.types.ObjectId;

/**
 * 
 */
public class ImageRepository {
    
    private GridFS imageStorage;
    
    public ImageRepository() {
        DB db = MongoConnection.getInstance().getDB();
        imageStorage = new GridFS(db, "images");
    }
    
    /**
     * Gives all the images
     * @return JSON String of all images
     */
    public String getAllImages() {
        return imageStorage.getFileList().toArray().toString();
    }
    
    /**
     * Gives the GridFSDBFile of the requested image.
     * @param objectId 
     * @return GridFSDBFile of the image
     */
    public GridFSDBFile getImage(String objectId) {
        return imageStorage.findOne(new BasicDBObject("_id", new ObjectId(objectId)));
    }

    /**
     * Creates a file in the database
     * @param objectId
     * @param image
     * @param name
     * @param mediaType
     * @param username
     * @param fullSizeId 
     */
    public void createFile(ObjectId objectId, InputStream image, String name, String mediaType, String username, String fullSizeId) {
        GridFSInputFile createdFile = imageStorage.createFile(image);
        createdFile.setId(objectId);
        createdFile.setContentType(mediaType);
        createdFile.setFilename(name);
        createdFile.put("uploaded_by", username);
        createdFile.put("master_id", new ObjectId(fullSizeId));
        createdFile.save();
    }
    
    /**
     * Creates a file in the database
     * @param objectId
     * @param image
     * @param name
     * @param mediaType
     * @param username 
     */
    public void createFile(ObjectId objectId, InputStream image, String name, String mediaType, String username) {
        GridFSInputFile createdFile = imageStorage.createFile(image);
        createdFile.setId(objectId);
        createdFile.setContentType(mediaType);
        createdFile.setFilename(name);
        createdFile.put("uploaded_by", username);
        createdFile.save();
    }
    
    /**
     * Creates a thumbnail in the database
     * @param objectId
     * @param image
     * @param name
     * @param mediaType
     * @param username
     * @param masterId 
     */
    public void createThumbnail(ObjectId objectId, InputStream image, String name, String mediaType, String username, String masterId) {
        try {
            byte[] thumbnail = ImageResizer.resize(image);
            System.out.println("Start creating thumbnail");
            createFile(objectId, new ByteArrayInputStream(thumbnail), name, mediaType, username, masterId);
        } catch (IOException ex) {
            Logger.getLogger(ImageRepository.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Deletes an image in the database
     * @param objectId 
     */
    public void removeImageAndItsChildren(String objectId) {
        imageStorage.remove(new BasicDBObject("_id", new ObjectId(objectId)));
        imageStorage.remove(new BasicDBObject("master_id", new ObjectId(objectId)));
    }
}
