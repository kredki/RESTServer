package oceny.lists;

import oceny.resources.Course;

import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

public class CourseList {
    private static final CopyOnWriteArrayList<Course> cList = new CopyOnWriteArrayList<>();

    static {

        cList.add(new Course("Przedmiot1", "Jan Nowak"));
        cList.add(new Course("Przedmiot2", "Andrzej Kowalski"));
        cList.add(new Course("Przedmiot3", "Mateusz Kowalski"));
    }

    private CourseList(){}

    public static CopyOnWriteArrayList<Course> getInstance(){
        return cList;
    }

    public static void testList(){
        CopyOnWriteArrayList<Course> list = CourseList.getInstance();
        list.stream()
                .forEach(i -> System.out.println(i));
        String cString =
                list.stream()
                        .map( c -> c.toString())
                        .collect(Collectors.joining("\n"));
        System.out.println(cString);
    }

    public static void main(String[] args) {
        CourseList.testList();
    }
}
