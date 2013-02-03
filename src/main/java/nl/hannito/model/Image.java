package nl.hannito.model;

import nl.hannito.DAO.ImageSourceDAO;
import org.bson.types.ObjectId;

public class Image extends Source {
    private String image;
    private String thumbnail;
    private final String source_type = "image";
    private String description;

    public Image(ImageSourceDAO image, String owner) {
        super(owner);
        this.image = new ObjectId(image.getObjectId()).toString();
        this.thumbnail = new ObjectId(image.getThumbnailId()).toString();
    }
}
