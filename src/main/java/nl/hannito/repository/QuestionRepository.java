package nl.hannito.repository;

import com.mongodb.AggregationOutput;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;
import nl.hannito.model.DBObjectComparator;
import nl.hannito.model.QuestionFilter;
import org.bson.types.ObjectId;

public class QuestionRepository implements IQuestionRepository{
    
    private DBCollection coll;
    
    public QuestionRepository() {
        DB db = MongoConnection.getInstance().getDB();
        coll = db.getCollection("questions");
    }

    @Override
    public String get(ObjectId id) {
        DBObject object = coll.findOne(id);
        System.out.println(object);
        if(object == null) {
            return null;
        }
        return object.toString();
    }

    @Override
    public String update(ObjectId id, DBObject question) {
        DBObject oldquestion = coll.findOne(id);
        if(oldquestion != null) {
            System.out.println(oldquestion);
            return question.put(null, id).toString();
        }
        System.out.println("bestaat niet");
        return null;
    }

    @Override
    public void delete(String id) {
        DBObject dbObject = new BasicDBObject().append("_id", new ObjectId(id));
        coll.remove(dbObject);
    }

    @Override
    public String create(DBObject object) {
        return coll.insert(object).toString();
    }

    @Override
    public String getAll() {
        return coll.find().sort(new BasicDBObject("added_on", -1)).toArray().toString();
    }
    
    @Override
    public String findByDifficulty(String difficulty) {
        BasicDBObject query = new BasicDBObject("difficulty", difficulty);
        return coll.find(query).toArray().toString();
    }

    @Override
    public String findByOwner(String owner) {
        BasicDBObject query = new BasicDBObject("owner", owner);
        return coll.find(query).sort(new BasicDBObject("added_on", -1)).toArray().toString();
    }
    
    @Override
    public int count() {
        return coll.find().count();
    }

    @Override
    public String range(int from, int to) {
        return coll.find().skip(from).limit(to).toArray().toString();
    }
    
    @Override
    public String filterQuestions(QuestionFilter filter) {
        DBObject query = new BasicDBObject();
        Iterator itr = null;
        DBObject difficulty = new BasicDBObject();
        DBObject questionType = new BasicDBObject();
        if(filter.getDifficulty() != null && filter.getDifficulty().length > 0) {
            query.put("difficulty", new BasicDBObject("$in", filter.getDifficulty()));
            difficulty.put("difficulty", new BasicDBObject("$in", filter.getDifficulty()));
        }
        if(filter.getQuestion_type() != null && filter.getQuestion_type().length > 0) {
            query.put("question_type", new BasicDBObject("$in", filter.getQuestion_type()));
            questionType.put("question_type", new BasicDBObject("$in", filter.getQuestion_type()));
        }
        if(filter.getTags() != null && filter.getTags().length > 0) {
            query.put("tags", new BasicDBObject("$in", filter.getTags()));
            BasicDBObject group = new BasicDBObject();
            group.put("_id", "$_id");
            group.put("matches", new BasicDBObject("$sum", 1));
            AggregationOutput result = coll.aggregate( new BasicDBObject("$match", new BasicDBObject("tags", new BasicDBObject("$in", filter.getTags()))),
                            new BasicDBObject("$match", difficulty),
                            new BasicDBObject("$match", questionType),
                            new BasicDBObject("$unwind", "$tags"),
                            new BasicDBObject("$match", new BasicDBObject("tags", new BasicDBObject("$in", filter.getTags()))),
                            new BasicDBObject("$group", group),
                            new BasicDBObject("$sort", new BasicDBObject("_id", -1)));
            System.out.println(result.getCommand());
            itr = result.results().iterator();
        }
        List<DBObject> queryResult = coll.find(query).sort(new BasicDBObject("_id", -1)).toArray();
        if(itr != null && !queryResult.isEmpty()) {
            int j = 0;
            while(itr.hasNext()) {
                DBObject obj = (BasicDBObject)itr.next();
                queryResult.get(j).put("matches", obj.get("matches"));
                System.out.println(obj + "  /n" + queryResult.get(j));
                j++;
            }
            Collections.sort(queryResult, new DBObjectComparator());
        }
        return queryResult.toString();
    }

    @Override
    public String findByKeyValue(String key, String value, boolean caseInsensitive) {
        BasicDBObject query = new BasicDBObject(key, value);
        if(caseInsensitive) {
            query = new BasicDBObject(key, Pattern.compile(".*" + value + ".*" , Pattern.CASE_INSENSITIVE));     
        }
        return coll.find(query).toArray().toString();
    }
}
