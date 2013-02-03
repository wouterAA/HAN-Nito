package nl.hannito.repository;

import com.google.gson.Gson;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.util.JSON;
import nl.hannito.model.Source;
import org.bson.types.ObjectId;

public class SourceRepository {

    private DBCollection coll;
    
    public SourceRepository() {
        DB db = MongoConnection.getInstance().getDB();
        coll = db.getCollection("source_questions");
    }
    
    /**
     * Gives all the sources
     * @return JSON String of sources
     */
    public String getAllSources() {
        return coll.find().sort(new BasicDBObject("added_on", -1)).toArray().toString();
    }
    
    /**
     * Gives the source with the given id
     * @param objectId id to search for
     * @return JSON String of the source
     */
    public String getSource(String objectId) {
        return coll.findOne(new ObjectId(objectId)).toString();
    }

    /**
     * Deletes the source
     * @param objectId id of the source to remove
     */
    public void remove(String objectId) {
        DBObject foundSource = coll.findOne(new BasicDBObject("_id", new ObjectId(objectId)));
        if(foundSource.get("source_type").toString().equalsIgnoreCase("Image")) {
            String imageObjectId = foundSource.get("image.ObjectId").toString();
            new ImageRepository().removeImageAndItsChildren(imageObjectId);
        }
        coll.remove(foundSource);
    }
    
    /**
     * Creates a source in the database
     * @param source source object to create
     * @return JSON String of the created source
     */
    public String createSource(Source source) {
        Gson gson = new Gson();
        String sourceAsJSON = gson.toJson(source);
        DBObject dbObject = (DBObject)JSON.parse(sourceAsJSON);
        coll.insert(dbObject);
        
        return dbObject.toString();
    }
}
