package oceny.resources;

import oceny.DateParser;
import org.glassfish.jersey.linking.InjectLink;
import org.glassfish.jersey.linking.InjectLinks;

import javax.ws.rs.core.Link;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@XmlRootElement
public class Student {

    private long index;
    private String firstName;
    private String lastName;
    private Date birthday;
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
        this.index = counter.getAndIncrement();
        this.firstName = firstName;
        this.lastName = lastName;
        DateParser dateParser = new DateParser(birthdayString);
        Date birthday = dateParser.getDate();
        this.birthday = birthday;
        this.grades = grades;
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

    public long getIndex() {
        return index;
    }

    public void setIndex(long index) {
        this.index = index;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public Date getBirthday() {
        return birthday;
    }

    public List<Grade> getGrades() {
        return grades;
    }

    public void addGrade(Grade grade) {
        grades.add(grade);
    }

    public void setGradeOnList(Grade grade) {
        int i = 0;
        long id = grade.getId();
        for (Grade g : this.grades) {
            if(g.getId() == id) {
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
