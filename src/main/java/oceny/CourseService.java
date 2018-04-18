package oceny;

import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/")
public class CourseService {
    private final CopyOnWriteArrayList<Course> cList = CourseList.getInstance();
    private final CopyOnWriteArrayList<Grade> gradeList = GradeList.getInstance();
    private final CopyOnWriteArrayList<Student> studentList = StudentList.getInstance();

    @GET
    @Path("/courses")
    @Produces(MediaType.TEXT_PLAIN)
    public String getAllCourses() {
        return "---Courses List---\n"
                + cList.stream()
                .map(c -> c.toString())
                .collect(Collectors.joining("\n"));
    }

    @POST
    @Path("/courses")
    @Produces(MediaType.APPLICATION_JSON)
    public Response addCourse(Course course){
        cList.add(course);
        return Response.status(201).build();
    }

    @GET
    @Path("/courses/{id}")
    @Produces(MediaType.TEXT_PLAIN)
    public String getStudent(@PathParam("id") long id) {
        Optional<Course> match
                = cList.stream()
                .filter(c -> c.getId() == id)
                .findFirst();
        if (match.isPresent()) {
            return "---Course---\n" + match.get().toString();
        } else {
            return "Course not found";
        }
    }

    @PUT
    @Path("/courses/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateCourse(Course course){
        int matchIdx = 0;
        Optional<Course> match = cList.stream()
                .filter(c -> c.getId() == course.getId())
                .findFirst();
        if (match.isPresent()) {
            matchIdx = cList.indexOf(match.get());
            cList.set(matchIdx, course);
            return Response.status(Response.Status.OK).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @DELETE
    @Path("/courses/{id}")
    public void deleteCourse(@PathParam("id") long id){
        Predicate<Course> course = c -> c.getId() == id;
        if (!cList.removeIf(course)) {
            throw new NotFoundException(new JsonError("Error", "Course " + id + " not found"));
        }
    }
}
