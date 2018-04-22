package oceny.services;

import oceny.JsonError;
import oceny.NotFoundException;
import oceny.lists.StudentList;
import oceny.XmlError;
import oceny.resources.Student;

import java.util.function.Predicate;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/")
public class StudentService {
    private final CopyOnWriteArrayList<Student> cList = StudentList.getInstance();

    @GET
    @Path("/students")
    @Produces(MediaType.TEXT_PLAIN)
    public String getAllStudents() {
        return "---Students List---\n"
                + cList.stream()
                .map(c -> c.toString())
                .collect(Collectors.joining("\n"));
    }

    @GET
    @Path("/students")
    @Produces(MediaType.APPLICATION_JSON)
    public CopyOnWriteArrayList<Student> getAllStudentsJson() {
        return cList;
    }

    @GET
    @Path("/students")
    @Produces(MediaType.APPLICATION_XML)
    public CopyOnWriteArrayList<Student> getAllStudentsXml() {
        return cList;
    }

    @POST
    @Path("/students")
    @Produces(MediaType.APPLICATION_JSON)
    public Response addStudent(Student student){
        cList.add(student);
        return Response.status(201).build();
    }

    @GET
    @Path("/students/{index}")
    @Produces(MediaType.TEXT_PLAIN)
    public String getStudent(@PathParam("index") long index) {
        Optional<Student> match
                = cList.stream()
                .filter(c -> c.getIndex() == index)
                .findFirst();
        if (match.isPresent()) {
            return "---Student---\n" + match.get().toString();
        } else {
            return "Student not found";
        }
    }

    @GET
    @Path("/students/{index}")
    @Produces(MediaType.APPLICATION_JSON)
    public Student getStudentJson(@PathParam("index") long index) {
        Optional<Student> match
                = cList.stream()
                .filter(c -> c.getIndex() == index)
                .findFirst();
        if (match.isPresent()) {
            return match.get();
        } else {
            throw new oceny.NotFoundException(new JsonError("Error", "Student " + index + " not found"));
        }
    }

    @GET
    @Path("/students/{index}")
    @Produces(MediaType.APPLICATION_XML)
    public Student getStudentXml(@PathParam("index") long index) {
        Optional<Student> match
                = cList.stream()
                .filter(c -> c.getIndex() == index)
                .findFirst();
        if (match.isPresent()) {
            return match.get();
        } else {
            throw new oceny.NotFoundException(new XmlError("Error", "Student " + index + " not found"));
        }
    }

    @PUT
    @Path("/students/{index}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateStudent(Student student){
        int matchIdx = 0;
        Optional<Student> match = cList.stream()
                .filter(c -> c.getIndex() == student.getIndex())
                .findFirst();
        if (match.isPresent()) {
            matchIdx = cList.indexOf(match.get());
            cList.set(matchIdx, student);
            return Response.status(Response.Status.OK).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @DELETE
    @Path("/students/{index}")
    public void deleteStudent(@PathParam("index") long index){
        Predicate<Student> student = c -> c.getIndex() == index;
        if (!cList.removeIf(student)) {
            throw new NotFoundException(new JsonError("Error", "Student " + index + " not found"));
        }
    }
}
