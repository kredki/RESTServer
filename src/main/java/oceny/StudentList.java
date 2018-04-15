package oceny;

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

        // Create list of customers
        cList.add(
                new Student.StudentBuilder().index()
                        .firstName("George")
                        .lastName("Washington")
                        .birthday("1732-02-23")
                        .grades(gradesList1)
                        .build()
        );

        cList.add(
                new Student.StudentBuilder().index()
                        .firstName("John")
                        .lastName("Adams")
                        .grades(gradesList2)
                        .birthday("1735-10-30")
                        .build()
        );

        cList.add(
                new Student.StudentBuilder().index()
                        .firstName("Thomas")
                        .lastName("Jefferson")
                        .birthday("1743-04-13")
                        .grades(gradesList3)
                        .build()
        );

        cList.add(
                new Student.StudentBuilder().index()
                        .firstName("James")
                        .lastName("Madison")
                        .birthday("1751-03-16")
                        .build()
        );

        cList.add(
                new Student.StudentBuilder().index()
                        .firstName("James")
                        .lastName("Monroe")
                        .birthday("1758-04-28")
                        .build()
        );

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
