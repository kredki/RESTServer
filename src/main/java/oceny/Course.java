package oceny;

import java.util.Date;
import java.util.concurrent.atomic.AtomicLong;

public class Course {

    private final long id;
    private final String name;
    private final String lecturer;
    private static final AtomicLong counter = new AtomicLong(100);

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
