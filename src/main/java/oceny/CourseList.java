package oceny;

import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

public class CourseList {
    private static final CopyOnWriteArrayList<Course> cList = new CopyOnWriteArrayList<>();

    static {

        cList.add(new Course.CourseBuilder().id()
                .name("Przedmiot1")
                .lecturer("Jan Nowak")
                .build());
        cList.add(new Course.CourseBuilder().id()
                .name("Przedmiot2")
                .lecturer("Andrzej Kowalski")
                .build());
        cList.add(new Course.CourseBuilder().id()
                .name("Przedmiot3")
                .lecturer("Mateusz Kowalski")
                .build());
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
