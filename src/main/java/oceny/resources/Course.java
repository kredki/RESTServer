package oceny.resources;

import oceny.ObjectIdJaxbAdapter;
import oceny.db.MongoHandler;
import org.bson.types.ObjectId;
import org.glassfish.jersey.linking.InjectLink;
import org.glassfish.jersey.linking.InjectLinks;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Transient;
import org.mongodb.morphia.query.Query;
import org.mongodb.morphia.query.UpdateOperations;

import javax.ws.rs.core.Link;
import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@Entity("courses")
@XmlRootElement
@XmlType(propOrder = {"id", "name", "lecturer", "links"})
public class Course {
    @Id
    @XmlJavaTypeAdapter(ObjectIdJaxbAdapter.class)
    private ObjectId objectId;
    @Transient
    private long id;
    /*@Id
    @XmlElement
    private long id;*/
    @XmlElement
    private String name;
    @XmlElement
    private String lecturer;
    private static final AtomicLong counter = new AtomicLong(100);

    @InjectLinks({
            @InjectLink(value = "/courses/{id}", rel = "self"),
            @InjectLink(value = "/courses", rel = "parent"),
//            @InjectLink(value = "/students/{index}", rel = "student")
    })
    @XmlElement(name = "link")
    @XmlElementWrapper(name = "links")
    @XmlJavaTypeAdapter(Link.JaxbAdapter.class)
    private List<Link> links;

    public Course() { }

    public Course(String name, String lecturer) {
        //get id from db and increment it
        Datastore datastore = MongoHandler.getInstance().getDatastore();
        final Query<Indexes> findQuery = datastore.createQuery(Indexes.class);
        UpdateOperations<Indexes> operation = datastore.createUpdateOperations(Indexes.class).inc("courseLastId");
        Indexes indexes = datastore.findAndModify(findQuery, operation );
        this.id = indexes.getCourseLastId();

        this.name = name;
        this.lecturer = lecturer;
    }

    public ObjectId getObjectId() {
        return objectId;
    }

    public void setObjectId(ObjectId objectId) {
        this.objectId = objectId;
    }

    @XmlAttribute
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @XmlElement
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @XmlElement
    public String getLecturer() {
        return lecturer;
    }

    public void setLecturer(String lecturer) {
        this.lecturer = lecturer;
    }
}
