package nl.hannito.repository;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import nl.hannito.model.QuestionFilter;
import org.bson.types.ObjectId;

public interface IQuestionRepository {
    
    /**
     * Returns the JSON String representation of the question with the given id
     * @param id The ObjectId of the question to get
     * @return JSON String representation of the question
     */
    public String get(ObjectId id);
    /**
     * Gets all the questions
     * @return JSON String representation of all the questions
     */
    public String getAll();
    /**
     * Create a question
     * @param object DBObject instance of the given question
     * @return JSON String of the inserted question
     */
    public String create(DBObject object);
    /**
     * Update a question
     * @param id id of the question to update
     * @param question question to replace the old one with
     * @return JSON String of the updated question
     */
    public String update(ObjectId id, DBObject question);
    /**
     * Delete a question
     * @param id id of the question to delete
     */
    public void delete(String id);
    /**
     * Gives all the questions with the given difficulty
     * @param difficulty The difficulty level to search for
     * @return JSON String of the questions
     */
    public String findByDifficulty(String difficulty);
    /**
     * Gives all the questions of the given owner
     * @param owner The owner to search for
     * @return JSON String of the questions
     */
    public String findByOwner(String owner);
    /**
     * Gives the number of questions
     * @return Number of questions
     */
    public int count();
    /**
     * Gives the questions within the given range
     * @param from
     * @param to
     * @return JSON String of the questions
     */
    public String range(int from, int to);
    /**
     * Gives the questions according to the filter. Orders the questions by number of matching tags.
     * @param filter QuestionFilter object with possible Tags, Difficulty and Questiontype
     * @return JSON String of the questions
     */
    public String filterQuestions(QuestionFilter filter);
    /**
     * @deprecated 
     * @param key
     * @param value
     * @param caseInsensitive
     * @return 
     */
    public String findByKeyValue(String key, String value, boolean caseInsensitive);
    
}
