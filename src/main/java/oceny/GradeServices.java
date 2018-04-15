package oceny;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

@Path("/")
public class GradeServices {
    private final CopyOnWriteArrayList<Grade> cList = GradeList.getInstance();
    private final CopyOnWriteArrayList<Student> studentList = StudentList.getInstance();

    /*@GET
    @Path("/courses")
    @Produces(MediaType.TEXT_PLAIN)
    public String getAllStudents() {
        return "---Students List---\n"
                + cList.stream()
                .map(c -> c.toString())
                .collect(Collectors.joining("\n"));
    }*/

    @GET
    @Path("/students/{index}/grades")
    @Produces(MediaType.TEXT_PLAIN)
    public String getStudentGrades(@PathParam("index") long index) {
        System.out.println("looking for grades of student " + index);
        Optional<Student> match
                = studentList.stream()
                .filter(c -> c.getIndex() == index)
                .findFirst();
        if (match.isPresent()) {
            return "---Grades---\n"
                    + match.get().getGrades().stream()
                    .map(c -> c.toString())
                    .collect(Collectors.joining("\n"));

        } else {
            return "Student not found";
        }
    }

    @GET
    @Path("/students/{index}/grades/{id}")
    @Produces(MediaType.TEXT_PLAIN)
    public String getStudentGradeById(@PathParam("index") long index, @PathParam("id") long id) {
        System.out.println("Looking for grade " + id + "of student " + index);
        Optional<Student> match
                = studentList.stream()
                .filter(c -> c.getIndex() == index)
                .findFirst();
        if (match.isPresent()) {
            Optional<Grade> match2
                    = cList.stream()
                    .filter(c -> c.getId() == id)
                    .findFirst();
            if (match2.isPresent()) {
                return "---Grade---\n" + match2.get().toString();
            } else {
                return "Grade not found";
            }

        } else {
            return "Student not found";
        }
    }
}
