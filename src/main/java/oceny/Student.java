package oceny;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

public class Student {

    private final long index;
    private final String firstName;
    private final String lastName;
    private final Date birthday;
    private final List<Grade> grades;
    private static final AtomicLong counter = new AtomicLong(100);

    private Student(StudentBuilder builder){
        this.index = builder.index;
        this.firstName = builder.firstName;
        this.lastName = builder.lastName;
        this.birthday = builder.birthday;
        this.grades = builder.grades;
    }

    public Student(){
        Student student = new Student.StudentBuilder().index().build();
        this.index = student.getIndex();
        this.firstName = student.getFirstName();
        this.lastName = student.getLastName();
        this.birthday = student.getBirthday();
        this.grades = student.getGrades();
    }

    public Student(long index, String firstName, String lastName, String birthday){
        Student student = new Student.StudentBuilder().index()
                .firstName(firstName)
                .lastName(lastName)
                .birthday(birthday)
                .build();
        this.index = student.getIndex();
        this.firstName = student.getFirstName();
        this.lastName = student.getLastName();
        this.birthday = student.getBirthday();
        this.grades = student.getGrades();
    }

    public Student(long index, String firstName, String lastName, String birthday, List<Grade> grades){
        Student student = new StudentBuilder().index()
                .firstName(firstName)
                .lastName(lastName)
                .birthday(birthday)
                .grades(grades)
                .build();
        this.index = student.getIndex();
        this.firstName = student.getFirstName();
        this.lastName = student.getLastName();
        this.birthday = student.getBirthday();
        this.grades = student.getGrades();
    }

    public Student(long index, String firstName, String lastName, String birthday, Grade grade){
        Student student = new StudentBuilder().index()
                .firstName(firstName)
                .lastName(lastName)
                .birthday(birthday)
                .grades(grade)
                .build();
        this.index = student.getIndex();
        this.firstName = student.getFirstName();
        this.lastName = student.getLastName();
        this.birthday = student.getBirthday();
        this.grades = student.getGrades();
    }

    public long getIndex(){
        return this.index;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public String getLastName() {
        return this.lastName;
    }

    public Date getBirthday(){
        return this.birthday;
    }

    public List<Grade> getGrades(){
        return this.grades;
    }

    @Override
    public String toString(){
        return "ID: " + index
                + " First: " + firstName
                + " Last: " + lastName + "\n"
                + " Birthday " + birthday + "\n"
                + " Grades " + grades;
    }

    public static class StudentBuilder{
        private long index;
        private String firstName = "";
        private String lastName = "";
        private Date birthday = new Date();
        private List<Grade> grades = new ArrayList<>();

        public StudentBuilder index(){
            this.index = Student.counter.getAndIncrement();
            return this;
        }

        public StudentBuilder index(long index){
            this.index = index;
            return this;
        }

        public StudentBuilder firstName(String firstName){
            this.firstName = firstName;
            return this;
        }

        public StudentBuilder lastName(String lastName){
            this.lastName = lastName;
            return this;
        }

        public StudentBuilder birthday(String birthdayString){
            DateParser dateParser = new DateParser(birthdayString);
            Date birthday = dateParser.getDate();
            this.birthday = birthday;
            return this;
        }

        public StudentBuilder grades(List<Grade> grades){
            this.grades = grades;
            return this;
        }

        public StudentBuilder grades(Grade grade){
            List<Grade> grades = new ArrayList<>();
            grades.add(grade);
            this.grades = grades;
            return this;
        }

        public Student build(){
            return new Student(this);
        }

    }
}
