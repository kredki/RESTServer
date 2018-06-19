package oceny.resources;

import com.fasterxml.jackson.annotation.JsonFormat;
import oceny.DateParser;
import oceny.ObjectIdJaxbAdapter;
import oceny.db.MongoHandler;
import org.bson.types.ObjectId;
import org.glassfish.jersey.linking.InjectLink;
import org.glassfish.jersey.linking.InjectLinks;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.annotations.*;
import org.mongodb.morphia.query.Query;
import org.mongodb.morphia.query.UpdateOperations;

import javax.ws.rs.core.Link;
import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.beans.Transient;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@Entity("students")
@XmlRootElement
@XmlType(propOrder = {"index", "firstName", "lastName", "birthday", "grades", "objectId", "links"})
public class Student {

    @Id
    @XmlElement
    @XmlJavaTypeAdapter(ObjectIdJaxbAdapter.class)
    ObjectId objectId;
    @XmlElement
    @Indexed(name = "index", unique = true)
    private long index;
    @XmlElement
    private String firstName;
    @XmlElement
    private String lastName;
    @XmlElement
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd", timezone="CET")
    private Date birthday;
    @Reference
    @XmlElement
    private List<Grade> grades;

    @InjectLinks({
            @InjectLink(value = "/students/{index}", rel = "self"),
            @InjectLink(value = "/students", rel = "parent"),
            @InjectLink(value = "/students/{index}/grades", rel = "grades")
    })
    @XmlElement(name = "link")
    @XmlElementWrapper(name = "links")
    @XmlJavaTypeAdapter(Link.JaxbAdapter.class)
    private List<Link> links;

    public Student() {
        this.index = getIndexFromDB();
        this.grades = new ArrayList<>();
    }

    public Student(String firstName, String lastName, String birthdayString, List<Grade> grades) {
        this.index = getIndexFromDB();
        this.firstName = firstName;
        this.lastName = lastName;
        DateParser dateParser = new DateParser(birthdayString);
        Date birthday = dateParser.getDate();
        this.birthday = birthday;
        this.grades = grades;
        int i = 0;
        for (Grade g: grades) {
            grades.set(i, g);
            i++;
        }
    }

    public Student(String firstName, String lastName, String birthdayString) {
        this.index = getIndexFromDB();
        this.firstName = firstName;
        this.lastName = lastName;
        DateParser dateParser = new DateParser(birthdayString);
        Date birthday = dateParser.getDate();
        this.birthday = birthday;
        this.grades = new ArrayList<>();
    }

    public ObjectId getObjectId() { return objectId; }

    @XmlTransient
    public void setObjectId(ObjectId objectId) { this.objectId = objectId; }

    public long getIndex() { return index; }

    @XmlTransient
    public void setIndex(long index) { this.index = index; }

    public String getFirstName() { return firstName; }

    @XmlTransient
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }

    public Date getBirthday() { return birthday; }

    public List<Grade> getGrades() { return grades; }

    public void addGrade(Grade grade) {
        grade.setStudentOwnerIndex(this.index);
        this.grades.add(grade);
    }

    public void setGradeOnList(Grade grade) {
        int i = 0;
        long id = grade.getId();
        for (Grade g : this.grades) {
            if(g.getId() == id) {
                grade.setStudentOwnerIndex(this.index);
                this.grades.set(i, grade);
                return;
            }
            i++;
        }
    }

    public void removeGrade(Grade grade) {
        int i = 0;
        long id = grade.getId();
        for (Grade g : this.grades) {
            if(g.getId() == id) {
                this.grades.remove(i);
                return;
            }
            i++;
        }
    }

    /**
     * get index from DB and increment it
     * @return index
     */
    private long getIndexFromDB() {
        //get index from db and increment it
        Datastore datastore = MongoHandler.getInstance().getDatastore();
        final Query<Indexes> findQuery = datastore.createQuery(Indexes.class);
        UpdateOperations<Indexes> operation = datastore.createUpdateOperations(Indexes.class).inc("studentLastId", 1);
        Indexes indexes = datastore.findAndModify(findQuery, operation );

        //update index in db
        long studentIndex = indexes.getStudentLastId();
        /*final Query<Student> updateQuery = datastore.createQuery(Student.class).field("objectId").equal(this.objectId);
        final UpdateOperations<Student> updateOperation = datastore.createUpdateOperations(Student.class).set("index", studentIndex);
        datastore.update(updateQuery, updateOperation);
*/
        return studentIndex;
    }
}