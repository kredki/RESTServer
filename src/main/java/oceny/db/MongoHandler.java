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

import java.util.concurrent.CopyOnWriteArrayList;

public class MongoHandler {
    private static MongoHandler instance = null;
    private static Morphia morphia;
    private static Datastore datastore = null;

    public static MongoHandler getInstance() {
        if (instance != null) {
            return instance;
        } else {
            instance = new MongoHandler();
            initialize();
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
    public static boolean isCollectionExists(DB db, String collectionName)
    {

        DBCollection table = db.getCollection(collectionName);
        return table.count()>0;
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
        //initialize();

    }

    private static void initialize() {
        boolean indexesExists = isCollectionExists(datastore.getDB(), "indexes");
        Indexes indexesInstance = Indexes.getInstance();
        if(!indexesExists) {
            /*
            //Query<Indexes> query = datastore.createQuery(Indexes.class);
            //List<Indexes> indexes = query.asList();
            final Query<Indexes> findQuery = datastore.createQuery(Indexes.class);
            Indexes indexes = findQuery.get();
            Indexes indexesTemp = indexes;
            indexesInstance.setIndexes(indexesTemp.getStudentLastId(),
                    indexesTemp.getCourseLastId(), indexesTemp.getGradeLastId());
        } else {
        */
            indexesInstance.setAllToZero();
            datastore.save(indexesInstance);
        }

        boolean coursesExists = isCollectionExists(datastore.getDB(), "courses");
        System.out.println("coursesExists " + coursesExists);
        if(!coursesExists) {
            CopyOnWriteArrayList<Course> courseList = CourseList.getInstance();
            CourseList.initList();
            datastore.save(courseList);
        }

        boolean gradesExists = isCollectionExists(datastore.getDB(), "grades");
        System.out.println("gradesExists" + gradesExists);
        if(!gradesExists) {
            CopyOnWriteArrayList<Grade> gradetList = GradeList.getInstance();
            GradeList.initList();
            datastore.save(gradetList);
        }

        boolean studentsExists = isCollectionExists(datastore.getDB(), "students");
        System.out.println("studentsExists " + studentsExists);
        if(!studentsExists) {
            CopyOnWriteArrayList<Student> studentList = StudentList.getInstance();
            StudentList.initList();
            datastore.save(studentList);
        }
    }
}
