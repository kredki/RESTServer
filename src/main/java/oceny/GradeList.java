package oceny;

import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

public class GradeList {
    private static final CopyOnWriteArrayList<Grade> cList = new CopyOnWriteArrayList<>();
    private static final CopyOnWriteArrayList<Course> courseList = CourseList.getInstance();

    static {
        cList.add(new Grade.GradeBuilder().id()
                .value(3.0f)
                .date("2018-04-13")
                .course(courseList.get(0))
                .build());
        cList.add(new Grade.GradeBuilder().id()
                .value(3.0f)
                .date("2018-04-13")
                .course(courseList.get(1))
                .build());
        cList.add(new Grade.GradeBuilder().id()
                .value(3.0f)
                .date("2018-04-13")
                .course(courseList.get(2))
                .build());

        cList.add(new Grade.GradeBuilder().id()
                .value(3.5f)
                .date("2018-04-13")
                .course(courseList.get(0))
                .build());
        cList.add(new Grade.GradeBuilder().id()
                .value(3.5f)
                .date("2018-04-13")
                .course(courseList.get(1))
                .build());
        cList.add(new Grade.GradeBuilder().id()
                .value(3.5f)
                .date("2018-04-13")
                .course(courseList.get(2))
                .build());

        cList.add(new Grade.GradeBuilder().id()
                .value(4.5f)
                .date("2018-04-13")
                .course(courseList.get(0))
                .build());
        cList.add(new Grade.GradeBuilder().id()
                .value(4.5f)
                .date("2018-04-13")
                .course(courseList.get(1))
                .build());
        cList.add(new Grade.GradeBuilder().id()
                .value(4.5f)
                .date("2018-04-13")
                .course(courseList.get(0))
                .build());
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

