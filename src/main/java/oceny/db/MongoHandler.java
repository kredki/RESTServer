package oceny.db;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;
import oceny.lists.CourseList;
import oceny.lists.GradeList;
import oceny.lists.StudentList;
import oceny.resources.Course;
import oceny.resources.Grade;
import oceny.resources.Indexes;
import oceny.resources.Student;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;
import org.mongodb.morphia.query.Query;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class MongoHandler {
    private static MongoHandler instance = null;
    private final Morphia morphia;
    private final Datastore datastore;
    private final CopyOnWriteArrayList<Student> studentList = StudentList.getInstance();
    private final CopyOnWriteArrayList<Course> courseList = CourseList.getInstance();
    private final CopyOnWriteArrayList<Grade> gradetList = GradeList.getInstance();

    public static MongoHandler getInstance() {
        if (instance != null) {
            return instance;
        } else {
            instance = new MongoHandler();
            return instance;
        }
    }

    public Morphia getMorphia() {
        return morphia;
    }

    public Datastore getDatastore() {
        return datastore;
    }

    /**
     * check if collection exists
     * @param db
     * @param collectionName
     * @return false if collection doesn't exist or is empty
     */
    public boolean isCollectionExists(DB db, String collectionName)
    {

        DBCollection table = db.getCollection(collectionName);
        return (table.count()>0)?true:false;
    }

    private MongoHandler() {
        morphia = new Morphia();
        //set morphia to store empty values
        morphia.getMapper().getOptions().setStoreEmpties(true);

        // tell Morphia where to find your classes
        // can be called multiple times with different packages or classes
        morphia.mapPackage("oceny");

        // create the Datastore connecting to the port 8004 on the local host
        this.datastore = morphia.createDatastore(new MongoClient("localhost" , 8004), "gradesDB");
        this.datastore.ensureIndexes();
        initialize();

    }

    private void initialize() {
        boolean coursesExists = isCollectionExists(datastore.getDB(), "courses");
        System.out.println("coursesExists " + coursesExists);
        if(!coursesExists) {
            CourseList.initList();
            this.datastore.save(courseList);
        }

        Query<Indexes> query = datastore.createQuery(Indexes.class);
        List<Indexes> indexes = query.asList();

        Indexes indexesInstance = Indexes.getInstance();
        if(indexes.isEmpty()) {
            indexesInstance.setAllToOne();
            this.datastore.save(indexesInstance);
        } else {
            Indexes indexesTemp = indexes.get(0);
            indexesInstance.setIndexes(indexesTemp.getStudentLastId(),
                    indexesTemp.getCourseLastId(), indexesTemp.getGradeLastId());
        }
        boolean gradesExists = isCollectionExists(datastore.getDB(), "grades");
        System.out.println("gradesExists" + gradesExists);
        if(!gradesExists) {
            GradeList.initList();
            this.datastore.save(gradetList);
        }

        boolean studentsExists = isCollectionExists(datastore.getDB(), "students");
        System.out.println("studentsExists " + studentsExists);
        if(!studentsExists) {
            StudentList.initList();
            this.datastore.save(studentList);
        }
    }
}
