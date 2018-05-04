package oceny.dao;

import oceny.db.MongoHandler;
import org.mongodb.morphia.Datastore;

public class CourseDAO {
    private static MongoHandler mongoHandler;
    private static CourseDAO instance = null;
    private static Datastore datastore;
    private CourseDAO() {
        mongoHandler = MongoHandler.getInstance();
        datastore = mongoHandler.getDatastore();
    }

    public static CourseDAO getInstance() {
        if (instance != null) {
            return instance;
        } else {
            instance = new CourseDAO();
            return instance;
        }
    }
}
