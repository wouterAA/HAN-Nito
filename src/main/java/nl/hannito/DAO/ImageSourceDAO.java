package nl.hannito.DAO;

public class ImageSourceDAO {

    private String objectId;
    private String thumbnailId;
    private String description;

    public ImageSourceDAO() {
    }

    public ImageSourceDAO(String objectId, String thumbnailId, String description) {
        this.objectId = objectId;
        this.thumbnailId = thumbnailId;
        this.description = description;
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public String getThumbnailId() {
        return thumbnailId;
    }

    public void setThumbnailId(String thumbnailId) {
        this.thumbnailId = thumbnailId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "ImageSourceDAO{" + "objectId=" + objectId + ", thumbnailId=" + thumbnailId + ", description=" + description + '}';
    }
}
