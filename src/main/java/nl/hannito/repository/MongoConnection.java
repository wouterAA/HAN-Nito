package nl.hannito.repository;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import java.net.UnknownHostException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.ejb.Stateless;

/**
 * This is a singleton class to connect to MongoDB
 * 
 */
public class MongoConnection {
    
    private DB db;
    private static MongoConnection instance;
    
    /**
     * private constructor, singleton
     */
    private MongoConnection() {
        
    }
    
    /**
     * Returns an instance of the MongoConnection class
     * @return MongoConnection
     */
    public static MongoConnection getInstance() {
        if(instance == null){
            instance = new MongoConnection();
        }
        return instance;
    }
    
    /**
     * Gives the DB for this MongoConnection
     * @return DB instance
     */
    public DB getDB() {
        try {
            MongoClient mongoClient = new MongoClient("127.0.0.1", 27017);
            db = mongoClient.getDB("HAN-Nito");
            //uncomment and replace 'username' and 'password' to use authentication with mongodb
            //boolean auth = db.authenticate("username", "password".toCharArray());
        } catch (UnknownHostException ex) {
            Logger.getLogger(MongoConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return db;
    }
    
}
