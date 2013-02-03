package nl.hannito.model;

public class ImageUpload {

    private String objectId;
    private String thumbnailId;

    public ImageUpload() {
    }

    public ImageUpload(String objectId, String thumbnailId) {
        this.objectId = objectId;
        this.thumbnailId = thumbnailId;
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

    @Override
    public String toString() {
        return "ImageUpload{" + "objectId=" + objectId + ", thumbnailId=" + thumbnailId + '}';
    }
}
