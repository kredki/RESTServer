package oceny.services;

import oceny.exceptions.JsonError;
import oceny.exceptions.NotFoundException;
import oceny.exceptions.XmlError;
import oceny.lists.CourseList;
import oceny.lists.GradeList;
import oceny.lists.StudentList;
import oceny.resources.Course;
import oceny.resources.Grade;
import oceny.resources.Student;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Predicate;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/")
@Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
public class CourseService {
    private final CopyOnWriteArrayList<Course> cList = CourseList.getInstance();
    private final CopyOnWriteArrayList<Grade> gradeList = GradeList.getInstance();
    private final CopyOnWriteArrayList<Student> studentList = StudentList.getInstance();


    @GET
    @Path("/courses")
    public CopyOnWriteArrayList<Course> getAllCourses() {
        return cList;
    }

    @POST
    @Path("/courses")
    public Response addCourse(Course course){
        cList.add(course);
        URI uri = null;
        try {
            uri = new URI("http://localhost:8000/oceny/courses");
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return Response.created(uri).build();
    }

    @GET
    @Path("/courses/{id}")
    public Course getCourseJson(@HeaderParam("Accept") String accepted, @PathParam("id") long id) {
        /*String mediaType = MediaType.APPLICATION_JSON;
        if(accepted != null) {
            mediaType = accepted;
        }
        Optional<Course> match
                = cList.stream()
                .filter(c -> c.getId() == id)
                .findFirst();
        if (match.isPresent()) {
            return Response.ok().entity(match.get()).type(mediaType).build();
        } else {
            if (mediaType.equals(MediaType.APPLICATION_XML)) {
                throw new NotFoundException(new XmlError("Error", "Course " + id + " not found"));
            } else if(mediaType.equals(MediaType.APPLICATION_JSON)) {
                throw new NotFoundException(new JsonError("Error", "Course " + id + " not found"));
            }
        }
        return null;*/

        Optional<Course> match
                = cList.stream()
                .filter(c -> c.getId() == id)
                .findFirst();
        if (match.isPresent()) {
            return match.get();
        } else {
            throw new NotFoundException(new JsonError("Error", "Course " + id + " not found"));
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
    public void deleteCourse(@HeaderParam("Accept") String accepted, @PathParam("id") long id){

        Predicate<Course> course = c -> c.getId() == id;
        if (!cList.removeIf(course)) {
            throw new NotFoundException(new JsonError("Error", "Course " + id + " not found"));
        }
    }
}
