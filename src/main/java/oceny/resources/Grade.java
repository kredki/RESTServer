package oceny.resources;

import oceny.DateParser;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.Date;
import java.util.concurrent.atomic.AtomicLong;

@XmlRootElement
public class Grade {
    private long id;
    private float value;
    private Date date;
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
