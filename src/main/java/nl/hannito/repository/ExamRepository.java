package nl.hannito.repository;

import com.google.gson.Gson;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.WriteResult;
import com.mongodb.util.JSON;
import java.util.regex.Pattern;
import nl.hannito.model.Exam;
import org.bson.types.ObjectId;

public class ExamRepository {

    private DBCollection coll;
    private Gson gson = new Gson();
    
    /**
     * Constructor for the ExamRepository, creates a MongoDB connection
     */
    public ExamRepository() {
        DB db = MongoConnection.getInstance().getDB();
        coll = db.getCollection("exams");
    }

    /**
     * Returns the String representation of the MongoDB object with the given id
     * @param id objectId to find the object of
     * @return JSON String representation of the MongoDB object
     */
    public String get(ObjectId id) {
        DBObject object = coll.findOne(id);
        System.out.println(1 + object.toString());
        if (object == null) {
            return null;
        }
        return object.toString();
    }
    
    /**
     * Returns the String representation of the MongoDB exam of the given owner
     * @param username User of whom to get the exams
     * @return JSON String representation of the MongoDB object
     */
    public String get(String username) {
        return coll.find(new BasicDBObject().append("owner", username)).toArray().toString();
    }

    /**
     * Removes the exam with the given id
     * @param id String id of the exam you wish to delete
     */
    public void delete(String id) {
        DBObject dbObject = new BasicDBObject().append("_id", new ObjectId(id));
        coll.remove(dbObject);
    }

    /**
     * Create an exam from a DBObject
     * @param object DBObject to add as an exam.
     * @return JSON String representation of the WriteResult
     */
    public String create(DBObject object) {
        return coll.insert(object).toString();
    }
    
    /**
     * Create an exam from an Exam object
     * @param exam Exam object to add as an exam
     * @return JSON String representation of the inserted exam
     */
    public String create(Exam exam) {
        DBObject dbObject = (DBObject) JSON.parse(gson.toJson(exam));
        WriteResult insert = coll.insert(dbObject);
        
        return dbObject.toString();
    }
    
    /**
     * Updates an exam
     * @param exam The updated exam
     * @param id The id of the exam to update
     * @return JSON String representation of the updated exam
     */
    public String update(Exam exam, String id) {
        DBObject dbObject = (DBObject) JSON.parse(gson.toJson(exam));
        WriteResult update = coll.update(new BasicDBObject().append("_id", new ObjectId(id)), dbObject);
        return dbObject.toString();
    }

    /**
     * 
     * @return JSON String representation of all exams
     */
    public String getAll() {
        return coll.find().sort(new BasicDBObject("added_on", -1)).toArray().toString();
    }

    /**
     * 
     * @return Total number of exams
     */
    public int count() {
        return coll.find().count();
    }

    /**
     * Returns the exams within the given range
     * @param from Starting point
     * @param to Ending point
     * @return JSON String representation of all exams within the given range
     */
    public String range(int from, int to) {
        return coll.find().skip(from).limit(to).toArray().toString();
    }

    /**
     * @deprecated 
     * @param key
     * @param value
     * @param caseInsensitive
     * @return 
     */
    public String findByKeyValue(String key, String value, boolean caseInsensitive) {
        BasicDBObject query = new BasicDBObject(key, value);
        if (caseInsensitive) {
            query = new BasicDBObject(key, Pattern.compile(".*" + value + ".*", Pattern.CASE_INSENSITIVE));
        }
        return coll.find(query).toArray().toString();
    }
}
