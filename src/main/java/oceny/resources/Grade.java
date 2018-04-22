package oceny.resources;

import oceny.DateParser;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.Date;
import java.util.concurrent.atomic.AtomicLong;

@XmlRootElement
public class Grade {
    private final long id;
    private final float value;
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
    */

    public void setCourse(Course course) { this.course = course; }

    private Grade(Grade.GradeBuilder builder){
        this.id = builder.id;
        this.value = builder.value;
        this.date = builder.date;
        this.course = builder.course;
    }

    public Grade(){
        Grade grade = new Grade.GradeBuilder().id().build();
        this.id = grade.getId();
        this.value = grade.getValue();
        this.date = grade.getDate();
        this.course = grade.getCourse();
    }

    public Grade(long id, float value, String date, Course course){
        Grade grade = new GradeBuilder().id()
                .value(value)
                .date(date)
                .course(course)
                .build();
        this.id = grade.getId();
        this.value = grade.getValue();
        this.date = grade.getDate();
        this.course = grade.getCourse();
    }

    public long getId(){
        return this.id;
    }

    public float getValue() {
        return this.value;
    }

    public Date getDate() {
        return date;
    }

    public Course getCourse() {
        return this.course;
    }

    @Override
    public String toString(){
        return "ID: " + id
                + " Value: " + value + "\n"
                + " Date: " + date + "\n"
                + " Course: " + course;
    }

    public static class GradeBuilder{
        private long id;
        private float value;
        private Date date = new Date();
        private Course course;

        public Grade.GradeBuilder id(){
            this.id = Grade.counter.getAndIncrement();
            return this;
        }

        public Grade.GradeBuilder id(long id){
            this.id = id;
            return this;
        }

        public Grade.GradeBuilder value(float value){
            this.value = (float) (0.5 * Math.round(value * 2));
            if(this.value > 5) {
                this.value = 5f;
            } else if(this.value <= 2.5) {
                this.value = 2f;
            }
            return this;
        }

        public Grade.GradeBuilder date(String dateString){
            DateParser dateParser = new DateParser(dateString);
            Date date = dateParser.getDate();
            this.date = date;
            return this;
        }

        public Grade.GradeBuilder course(Course course){
            this.course = course;
            return this;
        }


        public Grade build(){
            return new Grade(this);
        }

    }
}
