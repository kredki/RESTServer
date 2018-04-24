package oceny.resources;

import oceny.DateParser;
import org.glassfish.jersey.linking.InjectLink;
import org.glassfish.jersey.linking.InjectLinks;

import javax.ws.rs.core.Link;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@XmlRootElement
public class Grade {
    @XmlElement
    private long id;
    @XmlElement
    private float value;
    @XmlElement
    private Date date;
    @XmlElement
    private Course course;
    private static final AtomicLong counter = new AtomicLong(100);

    /*@InjectLinks({
            @InjectLink(value = "/students/{index}/grades/{id}", rel = "self"),
            @InjectLink(value = "/grades", rel = "parent"),
            @InjectLink(value = "/students/{index}", rel = "student")
    })
    @XmlElement(name = "link")
    @XmlElementWrapper(name = "links")
    @XmlJavaTypeAdapter(Link.JaxbAdapter.class)
    private List<Link> links;
    */

    public Grade(float value, String dateString, Course course) {
        this.id = counter.getAndIncrement();
        this.value = value;
        DateParser dateParser = new DateParser(dateString);
        Date date = dateParser.getDate();
        this.date = date;
        this.course = course;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }
}
