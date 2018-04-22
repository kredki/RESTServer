package oceny;

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
public class Course {

    private final long id;
    private final String name;
    private final String lecturer;
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

    private Course(Course.CourseBuilder builder){
        this.id = builder.id;
        this.name = builder.firstName;
        this.lecturer = builder.lastName;
    }

    public Course(){
        Course Course = new Course.CourseBuilder().id().build();
        this.id = Course.getId();
        this.name = Course.getName();
        this.lecturer = Course.getLecturer();
    }

    public Course(long id, String name, String lecturer, Date birthday){
        Course course = new Course.CourseBuilder().id()
                .name(name)
                .lecturer(lecturer)
                .build();
        this.id = course.getId();
        this.name = course.getName();
        this.lecturer = course.getLecturer();
    }

    public long getId(){
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public String getLecturer() {
        return this.lecturer;
    }

    @Override
    public String toString(){
        return "ID: " + id
                + " Name: " + name
                + " Lecturer: " + lecturer;
    }

    public static class CourseBuilder{
        private long id;
        private String firstName = "";
        private String lastName = "";
        private Date birthday = new Date();

        public Course.CourseBuilder id(){
            this.id = Course.counter.getAndIncrement();
            return this;
        }

        public Course.CourseBuilder id(long id){
            this.id = id;
            return this;
        }

        public Course.CourseBuilder name(String firstName){
            this.firstName = firstName;
            return this;
        }

        public Course.CourseBuilder lecturer(String lastName){
            this.lastName = lastName;
            return this;
        }


        public Course build(){
            return new Course(this);
        }

    }
}
