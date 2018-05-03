package oceny.resources;

import oceny.DateParser;
import oceny.db.MongoHandler;
import org.glassfish.jersey.linking.InjectLink;
import org.glassfish.jersey.linking.InjectLinks;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.annotations.Embedded;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Reference;
import org.mongodb.morphia.query.Query;
import org.mongodb.morphia.query.UpdateOperations;

import javax.ws.rs.core.Link;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@Entity("grades")
@XmlRootElement
public class Grade {
    @Id
    @XmlElement
    private long id;
    @XmlElement
    private float value;
    @XmlElement
    private Date date;
    @XmlElement
    @Embedded
    private Course course;
    @XmlElement
    private long studentOwnerIndex;
    private static final AtomicLong counter = new AtomicLong(100);

    @InjectLinks({
            @InjectLink(value = "students/{studentOwnerIndex}/grades", rel = "parent"),
            @InjectLink(value = "students/{studentOwnerIndex}/grades/{id}", rel = "self")
    })
    @XmlElement(name = "link")
    @XmlElementWrapper(name = "links")
    @XmlJavaTypeAdapter(Link.JaxbAdapter.class)
    private List<Link> links;

    public Grade() { }

    public Grade(float value, String dateString, Course course) {
        //get id from db and increment it
        Datastore datastore = MongoHandler.getInstance().getDatastore();
        final Query<Indexes> findQuery = datastore.createQuery(Indexes.class);
        UpdateOperations<Indexes> operation = datastore.createUpdateOperations(Indexes.class).inc("gradeLastId");
        Indexes indexes = datastore.findAndModify(findQuery, operation );
        this.id = indexes.getGradeLastId();

        this.value = value;
        DateParser dateParser = new DateParser(dateString);
        Date date = dateParser.getDate();
        this.date = date;
        this.course = course;
    }

    @XmlAttribute
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @XmlElement
    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
    }

    @XmlElement
    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @XmlElement
    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    @XmlElement
    public long getStudentOwnerIndex() {
        return studentOwnerIndex;
    }

    public void setStudentOwnerIndex(long studentOwnerIndex) {
        this.studentOwnerIndex = studentOwnerIndex;
    }
}
