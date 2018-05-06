package oceny.resources;

import oceny.ObjectIdJaxbAdapter;
import oceny.db.MongoHandler;
import org.bson.types.ObjectId;
import org.glassfish.jersey.linking.InjectLink;
import org.glassfish.jersey.linking.InjectLinks;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.query.Query;
import org.mongodb.morphia.query.UpdateOperations;

import javax.ws.rs.core.Link;
import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.beans.Transient;
import java.util.List;

@Entity("courses")
@XmlRootElement
@XmlType(propOrder = {"id", "name", "lecturer", "objectId", "links"})
public class Course {
    @Id
    @XmlJavaTypeAdapter(ObjectIdJaxbAdapter.class)
    private ObjectId objectId;
    @XmlElement
    private long id;
    @XmlElement
    private String name;
    @XmlElement
    private String lecturer;

    @InjectLinks({
            @InjectLink(value = "/courses/{id}", rel = "self"),
            @InjectLink(value = "/courses", rel = "parent"),
    })
    @XmlElement(name = "link")
    @XmlElementWrapper(name = "links")
    @XmlJavaTypeAdapter(Link.JaxbAdapter.class)
    private List<Link> links;

    public Course() { this.id = getIdfromDB(); }

    public Course(String name, String lecturer) {
        this.id = getIdfromDB();
        this.name = name;
        this.lecturer = lecturer;
    }

    @Transient
    public ObjectId getObjectId() {
        return objectId;
    }

    @Transient
    public void setObjectId(ObjectId objectId) {
        this.objectId = objectId;
    }

    @Transient
    public long getId() {
        return id;
    }

    @Transient
    public void setId(long id) {
        this.id = id;
    }

    @Transient
    public String getName() {
        return name;
    }

    @Transient
    public void setName(String name) {
        this.name = name;
    }

    @Transient
    public String getLecturer() {
        return lecturer;
    }

    @Transient
    public void setLecturer(String lecturer) {
        this.lecturer = lecturer;
    }

    private long getIdfromDB() {
        //get id from db and increment it
        Datastore datastore = MongoHandler.getInstance().getDatastore();
        final Query<Indexes> findQuery = datastore.createQuery(Indexes.class);
        UpdateOperations<Indexes> operation = datastore.createUpdateOperations(Indexes.class).inc("courseLastId");
        Indexes indexes = datastore.findAndModify(findQuery, operation );
        return indexes.getCourseLastId();
    }
}
