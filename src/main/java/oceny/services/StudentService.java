package oceny.services;

import oceny.exceptions.JsonError;
import oceny.exceptions.NotFoundException;
import oceny.lists.StudentList;
import oceny.resources.Student;

import java.net.URI;
import java.net.URISyntaxException;
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
    private final CopyOnWriteArrayList<Student> studentList = StudentList.getInstance();


    @GET
    @Path("/students")
    public CopyOnWriteArrayList<Student> getAllStudentsJson() {
        return studentList;
    }

    @POST
    @Path("/students")
    public Response addStudent(Student student){
        studentList.add(student);
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
    public Student getStudentJson(@PathParam("index") long index) {
        Optional<Student> match
                = studentList.stream()
                .filter(c -> c.getIndex() == index)
                .findFirst();
        if (match.isPresent()) {
            return match.get();
        } else {
            throw new oceny.exceptions.NotFoundException(new JsonError("Error", "Student " + index + " not found"));
        }
    }

    @PUT
    @Path("/students/{index}")
    public Response updateStudent(Student student){
        int matchIdx = 0;
        Optional<Student> match = studentList.stream()
                .filter(c -> c.getIndex() == student.getIndex())
                .findFirst();
        if (match.isPresent()) {
            matchIdx = studentList.indexOf(match.get());
            studentList.set(matchIdx, student);
            return Response.status(Response.Status.OK).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @DELETE
    @Path("/students/{index}")
    public void deleteStudent(@PathParam("index") long index){
        Predicate<Student> student = c -> c.getIndex() == index;
        if (!studentList.removeIf(student)) {
            throw new NotFoundException(new JsonError("Error", "Student " + index + " not found"));
        }
    }
}
