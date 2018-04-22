package oceny.lists;

import oceny.resources.Course;
import oceny.resources.Grade;

import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

public class GradeList {
    private static final CopyOnWriteArrayList<Grade> cList = new CopyOnWriteArrayList<>();
    private static final CopyOnWriteArrayList<Course> courseList = CourseList.getInstance();

    static {
        cList.add(new Grade(3.0f, "2018-04-13", courseList.get(0)));
        cList.add(new Grade(3.0f, "2018-04-13", courseList.get(1)));
        cList.add(new Grade(3.0f, "2018-04-13", courseList.get(2)));

        cList.add(new Grade(3.5f, "2018-04-13", courseList.get(0)));
        cList.add(new Grade(3.5f, "2018-04-13", courseList.get(1)));
        cList.add(new Grade(3.5f,"2018-04-13", courseList.get(2)));

        cList.add(new Grade(4.5f, "2018-04-13", courseList.get(0)));
        cList.add(new Grade(4.5f, "2018-04-13", courseList.get(1)));
        cList.add(new Grade(4.5f, "2018-04-13", courseList.get(0)));
    }

    private GradeList(){}

    public static CopyOnWriteArrayList<Grade> getInstance(){
        return cList;
    }

    public static void testList(){
        CopyOnWriteArrayList<Grade> list = GradeList.getInstance();
        list.stream()
                .forEach(i -> System.out.println(i));
        String cString =
                list.stream()
                        .map( c -> c.toString())
                        .collect(Collectors.joining("\n"));
        System.out.println(cString);
    }

    public static void main(String[] args) {
        GradeList.testList();
    }
}

