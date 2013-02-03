package nl.hannito.model;

import com.mongodb.DBObject;
import java.util.Comparator;

public class DBObjectComparator implements Comparator<DBObject> {

    /**
     * Compares 2 DBObjects on the number of matching tags
     * @param o1 Object 1
     * @param o2 Object 2
     * @return result int
     */
    @Override
    public int compare(DBObject o1, DBObject o2) {
        int i1 = (int)o1.get("matches");
        int i2 = (int)o2.get("matches");
        return (i1>i2 ? -1 : (i1==i2 ? 0 : 1));
    }
}
