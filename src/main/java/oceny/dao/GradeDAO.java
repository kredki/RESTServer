package oceny.dao;

import com.mongodb.WriteResult;
import oceny.db.MongoHandler;
import oceny.resources.Course;
import oceny.resources.Grade;
import oceny.resources.Student;
import org.bson.types.ObjectId;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.query.Query;
import org.mongodb.morphia.query.UpdateOperations;

import java.util.ArrayList;
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
    public List<Grade> getStudentGradesList(long index, Long courseId, float gradeGreater, float gradeLess) {
        Query<Student> query = datastore.createQuery(Student.class).field("index").equal(index);
        Student student = query.get();
        if(student == null) {
            return null;
        } else {
            List<Grade> gradeList = query.get().getGrades();
            List<Grade> result = new ArrayList<>();
            for (Grade g : gradeList) {
                if(courseId != 0) {
                    float gradeValue = g.getValue();
                    if(g.getCourse().getId() == courseId &&
                            gradeValue >= gradeGreater && gradeValue <= gradeLess) {
                        result.add(g);
                    }
                } else {
                    float gradeValue = g.getValue();
                    if(gradeValue >= gradeGreater && gradeValue <= gradeLess) {
                        result.add(g);
                    }
                }
            }
            return result;
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
        final Query<Student> studentQuery = datastore.createQuery(Student.class).field("index").equal(index);
        Student student = studentQuery.get();
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
        student.addGrade(grade);
        final UpdateOperations<Student> updateOperation = datastore.createUpdateOperations(Student.class)
                .set("grades", student.getGrades());
        datastore.update(studentQuery, updateOperation);
        return true;
    }

    /**
     * update grade, or create new one if doesn't exist
     * @param index index of student
     * @param grade grade to update
     * @return true if successful, false if course or student doesn't exist
     */
    public boolean updateGrade(long index, Grade grade) {
        //check if student exists
        final Query<Student> studentQuery = datastore.createQuery(Student.class).field("index").equal(index);
        Student student = studentQuery.get();
        if(student == null) {
            return false;
        }

        //check if course exists
        final Query<Course> courseQuery = datastore.createQuery(Course.class).field("objectId").equal(grade.getCourse().getObjectId());
        Course course = courseQuery.get();
        if (course == null) {
            return false;
        }

        //check if grade exists
        final Query<Grade> gradeQuery = datastore.createQuery(Grade.class).field("objectId").equal(grade.getObjectId());
        Grade checkIfExistsGrade = gradeQuery.get();
        if (checkIfExistsGrade == null) {
            datastore.save(grade);
            student.addGrade(grade);
        } else {
            student.setGradeOnList(grade);
        }

        final UpdateOperations<Student> updateOperation = datastore.createUpdateOperations(Student.class)
                .set("grades", student.getGrades());
        datastore.update(studentQuery, updateOperation);

        final UpdateOperations<Grade> updateGradeOperation = datastore.createUpdateOperations(Grade.class)
                .set("value", grade.getValue())
                .set("date", grade.getDate())
                .set("course", course);
        datastore.update(gradeQuery, updateGradeOperation);
        return true;
    }

    /**
     * delete grade
     * @param index index of student
     * @param id id of grade
     * @return true if successful, false if grade or student not found
     */
    public boolean deleteGrade(long index, long id) {
        //check if student exists
        final Query<Student> studentQuery = datastore.createQuery(Student.class).field("index").equal(index);
        Student student = studentQuery.get();
        if(student == null) {
            return false;
        }

        //check if grade exists
        final Query<Grade> gradeQuery = datastore.createQuery(Grade.class).field("id").equal(id);
        Grade grade = gradeQuery.get();
        if (grade == null) {
            return false;
        } else {
            //remove grade from student
            student.removeGrade(grade);
            final UpdateOperations<Student> updateOperation = datastore.createUpdateOperations(Student.class)
                    .set("grades", student.getGrades());
            datastore.update(studentQuery, updateOperation);

            WriteResult result = datastore.delete(gradeQuery);
            return result.getN() > 0;
        }
    }
}
