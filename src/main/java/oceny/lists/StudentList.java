package oceny.lists;

import oceny.resources.Grade;
import oceny.resources.Student;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

public class StudentList {
    private static final CopyOnWriteArrayList<Student> cList = new CopyOnWriteArrayList<>();
    private static final CopyOnWriteArrayList<Grade> gradeList = GradeList.getInstance();

    private static List<Grade> gradesList1 = new ArrayList<Grade>();
    private static List<Grade> gradesList2 = new ArrayList<Grade>();
    private static List<Grade> gradesList3 = new ArrayList<Grade>();

    static {

        gradesList1.add(gradeList.get(0));
        gradesList1.add(gradeList.get(1));
        gradesList1.add(gradeList.get(2));
        gradesList2.add(gradeList.get(3));
        gradesList2.add(gradeList.get(4));
        gradesList2.add(gradeList.get(5));
        gradesList3.add(gradeList.get(6));
        gradesList3.add(gradeList.get(7));
        gradesList3.add(gradeList.get(8));

        // Create list of students
        cList.add(new Student("George","Washington", "1732-02-23", gradesList1));

        cList.add(new Student("John", "Adams", "1735-10-30", gradesList2));

        cList.add(new Student("Thomas", "Jefferson","1743-04-13", gradesList3));

        cList.add(new Student("James", "Madison", "1751-03-16"));

        cList.add(new Student("James", "Monroe","1758-04-28"));

    }

    private StudentList(){}

    public static CopyOnWriteArrayList<Student> getInstance(){
        return cList;
    }

    public static void testList(){
        CopyOnWriteArrayList<Student> list = StudentList.getInstance();
        list.stream()
                .forEach(i -> System.out.println(i));
        String cString =
                list.stream()
                        .map( c -> c.toString())
                        .collect(Collectors.joining("\n"));
        System.out.println(cString);
    }

    public static void main(String[] args) {
        StudentList.testList();
    }
}
