package oceny.services;

import oceny.dao.GradeDAO;
import oceny.lists.CourseList;
import oceny.lists.GradeList;
import oceny.exceptions.JsonError;
import oceny.exceptions.NotFoundException;
import oceny.lists.StudentList;
import oceny.resources.Course;
import oceny.resources.Grade;
import oceny.resources.Student;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Predicate;

@Path("/students/{index}/grades")
@Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
public class GradeServices {
    private final GradeDAO gradeDAO = GradeDAO.getInstance().getInstance();
    private final CopyOnWriteArrayList<Grade> gradeList = GradeList.getInstance();
    private final CopyOnWriteArrayList<Student> studentList = StudentList.getInstance();
    private final CopyOnWriteArrayList<Course> courseList = CourseList.getInstance();


    @GET
    public List<Grade> getStudentGrades(@PathParam("index") long index) {
        List<Grade> grades = gradeDAO.getStudentGradesList(index);
        if(grades == null) {
            throw new NotFoundException(new JsonError("Error", "Student " + index + " not found"));
        } else {
            return grades;
        }
    }

    @POST
    public Response addGrade(@PathParam("index") long index, Grade grade){
        if (gradeDAO.addGrade(index, grade)) {
            URI uri = null;
            try {
                uri = new URI("http://localhost:8000/oceny/students/" + index +"/grades");
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
            return Response.created(uri).build();
        } else {
            throw new NotFoundException(new JsonError("Error", "Student " + index + " or course not found"));
        }
    }

    @GET
    @Path("/{id}")
    public Grade getStudentGrade(@PathParam("index") long index, @PathParam("id") long id) {
        Grade grade = gradeDAO.getGrade(index, id);
        if(grade == null) {
            throw new NotFoundException(new JsonError("Error", "Student " + index + " or grade " + id + " of this student not found"));
        } else {
            return grade;
        }
    }

    @PUT
    @Path("/{id}")
    public Response updateGrade(@PathParam("index") long index, Grade grade){
        int matchIdx = 0;
        int matchIdx2 = 0;
        Optional<Grade> match = gradeList.stream()
                .filter(c -> c.getId() == grade.getId())
                .findFirst();
        if (match.isPresent()) {
            matchIdx = gradeList.indexOf(match.get());
            Optional<Student> match2 = studentList.stream()
                    .filter(c -> c.getIndex() == index)
                    .findFirst();
            if(match2.isPresent()) {
                matchIdx2 = studentList.indexOf(match2.get());
                gradeList.set(matchIdx, grade);
                Student student =match2.get();
                student.setGradeOnList(grade);
                studentList.set(matchIdx2, student);
                return Response.status(Response.Status.OK).build();
            } else {
                Response.status(Response.Status.NOT_FOUND).build();
            }
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }

    @DELETE
    @Path("/{id}")
    public void deleteGrade(@PathParam("index") long index, @PathParam("id") long id){
        Optional<Student> match = studentList.stream()
                .filter(c -> c.getIndex() == index)
                .findFirst();
        if(match.isPresent()) {
            Optional<Grade> match2 = gradeList.stream()
                    .filter(c -> c.getId() == id)
                    .findFirst();
            if(match2.isPresent()) {
                match.get().removeGrade(match2.get());
                Predicate<Grade> grade = c -> c.getId() == id;
                if (!gradeList.removeIf(grade)) {
                    throw new NotFoundException(new JsonError("Error", "Grade " + id + " not found"));
                }
            } else {
                throw new NotFoundException(new JsonError("Error", "Student " + id + " not found"));
            }
        } else {
            Response.status(Response.Status.NOT_FOUND).build();
        }
    }
}
