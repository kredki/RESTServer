package oceny.dao;

import com.mongodb.WriteResult;
import oceny.db.MongoHandler;
import oceny.resources.Course;
import oceny.resources.Grade;
import oceny.resources.Student;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.query.Query;
import org.mongodb.morphia.query.UpdateOperations;

import java.util.List;

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

    public List<Course> getCoursesList() {
        Query<Course> query = datastore.createQuery(Course.class);
        return query.asList();
    }

    public Course getCourse(Long id) {
        final Query<Course> query = datastore.createQuery(Course.class).field("id").equal(id);
        return query.get();
    }

    public void addCourse(Course course) {
        datastore.save(course);
    }

    public boolean updateCourse(Course course) {
        Course courseInDB = getCourse(course.getId());
        if(courseInDB == null) {
            return false;
        } else {
            final Query<Course> updateQuery = datastore.createQuery(Course.class).field("objectId").equal(course.getObjectId());
            final UpdateOperations<Course> updateOperation = datastore.createUpdateOperations(Course.class)
                    .set("id", course.getId())
                    .set("name", course.getName())
                    .set("lecturer", course.getLecturer());
            datastore.update(updateQuery, updateOperation);
            return true;
        }
    }

    public boolean deleteCourse(long id) {
        List<Student> studentList = StudentDAO.getInstance().getStudentsList();
        for(Student student : studentList) {
            List<Grade> gradelist = student.getGrades();
            for(Grade grade : gradelist) {
                if(grade.getCourse().getId() == id) {
                    return false;
                }
            }
        }
        final Query<Course> query = datastore.createQuery(Course.class).field("id").equal(id);
        WriteResult result = datastore.delete(query);
        return result.getN()>0;
    }
}
