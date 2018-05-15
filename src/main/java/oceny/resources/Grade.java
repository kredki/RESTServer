package oceny.resources;

import com.fasterxml.jackson.annotation.JsonFormat;
import oceny.DateParser;
import oceny.ObjectIdJaxbAdapter;
import oceny.db.MongoHandler;
import org.bson.types.ObjectId;
import org.glassfish.jersey.linking.InjectLink;
import org.glassfish.jersey.linking.InjectLinks;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Reference;
import org.mongodb.morphia.query.Query;
import org.mongodb.morphia.query.UpdateOperations;

import javax.ws.rs.core.Link;
import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.beans.Transient;
import java.util.Date;
import java.util.List;

@Entity("grades")
@XmlRootElement
@XmlType(propOrder = {"id", "value", "date", "course", "objectId", "studentOwnerIndex", "links"})
public class Grade {
    @Id
    @XmlElement
    @XmlJavaTypeAdapter(ObjectIdJaxbAdapter.class)
    ObjectId objectId;
    @XmlElement
    private long id;
    @XmlElement
    private float value;
    @XmlElement
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd", timezone="CET")
    private Date date;
    @XmlElement
    @Reference
    private Course course;
    @XmlElement
    private long studentOwnerIndex;

    @InjectLinks({
            @InjectLink(value = "students/{studentOwnerIndex}/grades", rel = "parent"),
            @InjectLink(value = "students/{studentOwnerIndex}/grades/{id}", rel = "self")
    })
    @XmlElement(name = "link")
    @XmlElementWrapper(name = "links")
    @XmlJavaTypeAdapter(Link.JaxbAdapter.class)
    private List<Link> links;

    public Grade() { this.id = getIdFromDB(); }

    public Grade(float value, String dateString, Course course) {
        this.id = getIdFromDB();
        this.value = value;
        DateParser dateParser = new DateParser(dateString);
        Date date = dateParser.getDate();
        this.date = date;
        this.course = course;
    }

    public long getId() {
        return id;
    }

    @XmlTransient
    public void setId(long id) {
        this.id = id;
    }

    public float getValue() {
        return value;
    }

    @XmlTransient
    public void setValue(float value) {
        this.value = value;
    }

    public Date getDate() {
        return date;
    }

    @XmlTransient
    public void setDate(Date date) {
        this.date = date;
    }

    public Course getCourse() {
        return course;
    }

    @XmlTransient
    public void setCourse(Course course) {
        this.course = course;
    }

    public long getStudentOwnerIndex() {
        return studentOwnerIndex;
    }

    @XmlTransient
    public void setStudentOwnerIndex(long studentOwnerIndex) {
        this.studentOwnerIndex = studentOwnerIndex;
    }

    private long getIdFromDB() {
        Datastore datastore = MongoHandler.getInstance().getDatastore();
        final Query<Indexes> findQuery = datastore.createQuery(Indexes.class);
        UpdateOperations<Indexes> operation = datastore.createUpdateOperations(Indexes.class).inc("gradeLastId");
        Indexes indexes = datastore.findAndModify(findQuery, operation );
        return indexes.getGradeLastId();
    }
}
