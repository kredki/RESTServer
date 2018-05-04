package oceny.services;

import oceny.dao.StudentDAO;
import oceny.exceptions.JsonError;
import oceny.exceptions.NotFoundException;
import oceny.lists.StudentList;
import oceny.resources.Student;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.function.Predicate;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/")
@Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
public class StudentService {
    private final StudentDAO studentDAO = StudentDAO.getInstance();

    @GET
    @Path("/students")
    public List<Student> getAllStudentsJson() { return studentDAO.getStudentsList(); }

    @POST
    @Path("/students")
    public Response addStudent(Student student){
        studentDAO.addStudent(student);
        URI uri = null;
        try {
            uri = new URI("http://localhost:8000/oceny/students");
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return Response.created(uri).build();
    }

    @GET
    @Path("/students/{index}")
    public Student getStudent(@PathParam("index") long index) {
        Student student = studentDAO.getStudent(index);
        if (student != null) {
            return student;
        } else {
            throw new oceny.exceptions.NotFoundException(new JsonError("Error", "Student " + index + " not found"));
        }
    }

    @PUT
    @Path("/students/{index}")
    public Response updateStudent(@PathParam("index") long index, Student student){
        if(index != student.getIndex()) {
            throw new NotFoundException(new JsonError("Error", "Index in url and in request body are different"));
        }
        if(studentDAO.updateStudent(student)) {
            return Response.status(Response.Status.OK).build();
        } else {
            throw new NotFoundException(new JsonError("Error", "Student " + student.getIndex() + " not found"));
        }
    }

    @DELETE
    @Path("/students/{index}")
    public Response deleteStudent(@PathParam("index") long index){
        if (studentDAO.deleteStudent(index)) {
            String result = "Student " + index + " deleted!";
            return Response.status(202).build();
        } else {
            throw new NotFoundException(new JsonError("Error", "Student " + index + " not found"));
        }
    }
}
