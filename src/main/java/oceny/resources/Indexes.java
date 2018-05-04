package oceny.resources;

import oceny.ObjectIdJaxbAdapter;
import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Transient;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

@Entity("indexes")
@XmlRootElement
public class Indexes {
    @Transient
    private static Indexes instance = null;
    @Id
    @XmlJavaTypeAdapter(ObjectIdJaxbAdapter.class)
    private ObjectId objectId;
    private long studentLastId;
    private long courseLastId;
    private long gradeLastId;

    public Indexes() { }

    public static Indexes getInstance() {
        if (instance != null) {
            return instance;
        } else {
            instance = new Indexes();
            return instance;
        }
    }

    public void setAllToZero() {
        this.studentLastId = 0;
        this.courseLastId = 0;
        this.gradeLastId = 0;
    }

    public void setIndexes(long studentLastId, long courseLastId, long gradeLastId) {
        this.studentLastId = studentLastId;
        this.courseLastId = courseLastId;
        this.gradeLastId = gradeLastId;
    }

    public ObjectId getObjectId() {
        return objectId;
    }

    public void setObjectId(ObjectId objectId) {
        this.objectId = objectId;
    }

    public long getStudentLastId() {
        return studentLastId;
    }

    public void setStudentLastId(long studentLastId) {
        this.studentLastId = studentLastId;
    }

    public long getCourseLastId() {
        return courseLastId;
    }

    public void setCourseLastId(long courseLastId) {
        this.courseLastId = courseLastId;
    }

    public long getGradeLastId() {
        return gradeLastId;
    }

    public void setGradeLastId(long gradeLastId) {
        this.gradeLastId = gradeLastId;
    }
}
