package oceny.services;

import oceny.dao.CourseDAO;
import oceny.exceptions.JsonError;
import oceny.exceptions.NotFoundException;
import oceny.lists.CourseList;
import oceny.lists.GradeList;
import oceny.lists.StudentList;
import oceny.resources.Course;
import oceny.resources.Grade;
import oceny.resources.Student;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Predicate;
import javax.ws.rs.*;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/")
@Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
public class CourseService {
    private final CourseDAO courseDAO = CourseDAO.getInstance();


    @GET
    @Path("/courses")
    public GenericEntity<List<Course>> getCoursesList(@DefaultValue("") @QueryParam("lecturer") String lecturer) {
        List<Course> courses = courseDAO.getCoursesList(lecturer);
        GenericEntity<List<Course>> genericEntity = new GenericEntity<List<Course>>(courses) {};
        return genericEntity;
    }

    @POST
    @Path("/courses")
    public Response addCourse(Course course){
        courseDAO.addCourse(course);
        URI uri = null;
        try {
            uri = new URI("http://localhost:8000/oceny/courses/" + course.getId());
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return Response.created(uri).build();
    }

    @GET
    @Path("/courses/{id}")
    public Course getCourse(@PathParam("id") long id) {
        Course course = courseDAO.getCourse(id);
        if (course != null) {
            return course;
        } else {
            throw new NotFoundException(new JsonError("Error", "Course " + id + " not found"));
        }
    }

    @PUT
    @Path("/courses/{id}")
    public Response updateCourse(@PathParam("id") long id, Course course){
        if(id != course.getId()) {
            throw new NotFoundException(new JsonError("Error", "Index in url and in request body are different"));
        }

        if(courseDAO.updateCourse(course)) {
            return Response.status(Response.Status.OK).build();
        } else {
            throw new NotFoundException(new JsonError("Error", "Course " + course.getId() + " not put"));
        }
    }

    @DELETE
    @Path("/courses/{id}")
    public Response deleteCourse(@PathParam("id") long id){
        if (courseDAO.deleteCourse(id)) {
            return Response.status(202).build();
        } else {
            throw new NotFoundException(new JsonError("Error", "Course " + id + " not deleted. Course have assigened grades or not exists."));
        }
    }
}
