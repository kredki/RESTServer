package oceny.dao;

import oceny.db.MongoHandler;
import oceny.resources.Course;
import oceny.resources.Grade;
import oceny.resources.Student;
import org.bson.types.ObjectId;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.query.Query;
import org.mongodb.morphia.query.UpdateOperations;

import java.util.List;

public class GradeDAO {
    private static MongoHandler mongoHandler;
    private static GradeDAO instance = null;
    private static Datastore datastore;
    private GradeDAO() {
        mongoHandler = MongoHandler.getInstance();
        datastore = mongoHandler.getDatastore();
    }

    public static GradeDAO getInstance() {
        if (instance != null) {
            return instance;
        } else {
            instance = new GradeDAO();
            return instance;
        }
    }

    /**
     * get list of grades of student
     * @param index index of student
     * @return list of grades of student, null when student not found
     */
    public List<Grade> getStudentGradesList(long index) {
        Query<Student> query = datastore.createQuery(Student.class).field("index").equal(index);
        Student student = query.get();
        if(student == null) {
            return null;
        } else {
            return query.get().getGrades();
        }
    }

    /**
     *
     * @param index index of student
     * @param id id of grade
     * @return grade of student, null when student or grade not found
     */
    public Grade getGrade(long index, long id) {
        Query<Student> query = datastore.createQuery(Student.class).field("index").equal(index);
        Student student = query.get();
        if(student == null) {
            return null;
        } else {
            List<Grade> gradesList = query.get().getGrades();
            Grade grade = null;
            for (Grade g : gradesList) {
                if(g.getId() == id) {
                    grade = g;
                    break;
                }
            }
            return grade;
        }
    }

    /**
     * add grade to student
     * @param index index of student
     * @param grade grade to add
     * @return true if succesful, false if not found student or course
     */
    public boolean addGrade(long index, Grade grade) {
        //check if student exists
        final Query<Student> query = datastore.createQuery(Student.class).field("index").equal(index);
        Student student = query.get();
        if(student == null) {
            return false;
        }

        //check if course exists
        final Query<Course> courseQuery = datastore.createQuery(Course.class).field("objectId").equal(grade.getCourse().getObjectId());
        Course course = courseQuery.get();
        if (course == null) {
            return false;
        }

        datastore.save(grade);
        //grade = datastore.createQuery(Grade.class).field("id").equal(grade.getId()).get();
        student.addGrade(grade);
        final UpdateOperations<Student> updateOperation = datastore.createUpdateOperations(Student.class)
                .set("grades", student.getGrades());
        datastore.update(query, updateOperation);
        return true;
    }
}
