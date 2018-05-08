package oceny.services;

import oceny.dao.StudentDAO;
import oceny.exceptions.JsonError;
import oceny.exceptions.NotFoundException;
import oceny.resources.Student;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/")
@Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
public class StudentService {
    private final StudentDAO studentDAO = StudentDAO.getInstance();

    private static final String defaultString = "";

    @GET
    @Path("/students")
    public List<Student> getAllStudentsJson(@DefaultValue(defaultString) @QueryParam("firstname") String firstName,
                                            @DefaultValue(defaultString) @QueryParam("lastname") String lastName,
                                            @DefaultValue(defaultString) @QueryParam("birthday") String birthday,
                                            @DefaultValue(defaultString) @QueryParam("birthdayfrom") String birthdayFrom,
                                            @DefaultValue(defaultString) @QueryParam("birthdayto") String birthdayTo) {
        if (isDefaultValue(firstName, lastName, birthday, birthdayFrom, birthdayTo)) {
            return studentDAO.getStudentsList();
        } else {
            return studentDAO.getStudentsList(firstName, lastName, birthday, birthdayFrom, birthdayTo);
        }
    }

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
            throw new NotFoundException(new JsonError("Error", "Student " + index + " not found"));
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
            throw new NotFoundException(new JsonError("Error", "Student " + student.getIndex() + " not put"));
        }
    }

    @DELETE
    @Path("/students/{index}")
    public Response deleteStudent(@PathParam("index") long index){
        if (studentDAO.deleteStudent(index)) {
            return Response.status(202).build();
        } else {
            throw new NotFoundException(new JsonError("Error", "Student " + index + " not found"));
        }
    }

    //check if all string are default
    private boolean isDefaultValue(String s1, String s2, String s3, String s4, String s5) {
        String result = s1 + s2 + s3 + s4 + s5;
        return result.equals(defaultString);
    }
}
