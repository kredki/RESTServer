package oceny.dao;

import com.mongodb.WriteResult;
import oceny.db.MongoHandler;
import oceny.resources.Student;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.query.Query;
import org.mongodb.morphia.query.UpdateOperations;

import java.util.List;

public class StudentDAO {
    private static MongoHandler mongoHandler;
    private static StudentDAO instance = null;
    private static Datastore datastore;
    private StudentDAO() {
        mongoHandler = MongoHandler.getInstance();
        datastore = mongoHandler.getDatastore();
    }

    public static StudentDAO getInstance() {
        if (instance != null) {
            return instance;
        } else {
            instance = new StudentDAO();
            return instance;
        }
    }

    public List<Student> getStudentsList() {
        Query<Student> query = datastore.createQuery(Student.class);
        List<Student> studentslist = query.asList();
        return studentslist;
    }

    public Student getStudent(Long index) {
        final Query<Student> query = datastore.createQuery(Student.class).field("index").equal(index);
        return query.get();
    }

    public void addStudent(Student student) {
        datastore.save(student);
    }

    /**
     * update student
     * @param student updated student to write in DB
     * @return true if succesfull, false if student not found
     */
    public boolean updateStudent(Student student) {
        Student studentInDB = getStudent(student.getIndex());
        if(studentInDB == null) {
            return false;
        } else {
            final Query<Student> updateQuery = datastore.createQuery(Student.class).field("objectId").equal(student.getObjectId());
            final UpdateOperations<Student> updateOperation = datastore.createUpdateOperations(Student.class)
                    .set("index", student.getIndex())
                    .set("firstName", student.getFirstName())
                    .set("lastName", student.getLastName())
                    .set("birthday", student.getBirthday())
                    .set("grades", student.getGrades());
            datastore.update(updateQuery, updateOperation);
            return true;
        }
    }

    /**
     *delete student of given index
     * @param index index of student to delete
     * @return true if delete succesful, false if not
     */
    public boolean deleteStudent(long index) {
        final Query<Student> query = datastore.createQuery(Student.class).field("index").equal(index);
        WriteResult result = datastore.delete(query);
        return result.getN()>0;
    }
}
