package oceny.resources;

import oceny.DateParser;
import oceny.db.MongoHandler;
import org.glassfish.jersey.linking.InjectLink;
import org.glassfish.jersey.linking.InjectLinks;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.annotations.*;
import org.mongodb.morphia.query.Query;
import org.mongodb.morphia.query.UpdateOperations;

import javax.ws.rs.core.Link;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@Entity("students")
@XmlRootElement
public class Student {

    @Id
    @XmlElement
    private long index;
    @XmlElement
    private String firstName;
    @XmlElement
    private String lastName;
    @XmlElement
    private Date birthday;
    @Embedded
    @XmlElement
    private List<Grade> grades;
    private static final AtomicLong counter = new AtomicLong(100);

    @InjectLinks({
            @InjectLink(value = "/students/{index}", rel = "self"),
            @InjectLink(value = "/students", rel = "parent"),
            @InjectLink(value = "/students/{index}/grades", rel = "grades")
    })
    @XmlElement(name = "link")
    @XmlElementWrapper(name = "links")
    @XmlJavaTypeAdapter(Link.JaxbAdapter.class)
    private List<Link> links;

    public Student() { }

    public Student(String firstName, String lastName, String birthdayString, List<Grade> grades) {
        //get index from db and increment it
        Datastore datastore = MongoHandler.getInstance().getDatastore();
        final Query<Indexes> findQuery = datastore.createQuery(Indexes.class);
        UpdateOperations<Indexes> operation = datastore.createUpdateOperations(Indexes.class).inc("studentLastId");
        Indexes indexes = datastore.findAndModify(findQuery, operation );
        this.index = indexes.getStudentLastId();

        this.firstName = firstName;
        this.lastName = lastName;
        DateParser dateParser = new DateParser(birthdayString);
        Date birthday = dateParser.getDate();
        this.birthday = birthday;
        this.grades = grades;
        int i = 0;
        for (Grade g: grades) {
            g.setStudentOwnerIndex(this.index);
            grades.set(i, g);
        }
    }

    public Student(String firstName, String lastName, String birthdayString) {
        this.index = counter.getAndIncrement();
        this.firstName = firstName;
        this.lastName = lastName;
        DateParser dateParser = new DateParser(birthdayString);
        Date birthday = dateParser.getDate();
        this.birthday = birthday;
        this.grades = new ArrayList<>();
    }

    @XmlAttribute
    public long getIndex() {
        return index;
    }

    public void setIndex(long index) {
        this.index = index;
    }

    @XmlElement
    public String getFirstName() {
        return firstName;
    }

    @XmlElement
    public String getLastName() {
        return lastName;
    }

    @XmlElement
    public Date getBirthday() {
        return birthday;
    }

    @XmlElement
    public List<Grade> getGrades() {
        return grades;
    }

    public void addGrade(Grade grade) {
        grade.setStudentOwnerIndex(this.index);
        grades.add(grade);
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
}