package oceny.services;

import oceny.lists.CourseList;
import oceny.lists.GradeList;
import oceny.exceptions.JsonError;
import oceny.exceptions.NotFoundException;
import oceny.lists.StudentList;
import oceny.resources.Course;
import oceny.resources.Grade;
import oceny.resources.Student;

import javax.ws.rs.*;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Predicate;

@Path("/students/{index}/grades")
@Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
public class GradeServices {
    private final CopyOnWriteArrayList<Grade> gradeList = GradeList.getInstance();
    private final CopyOnWriteArrayList<Student> studentList = StudentList.getInstance();
    private final CopyOnWriteArrayList<Course> courseList = CourseList.getInstance();


    @GET
    public Response getStudentGradesJson(@PathParam("index") long index) {
        Optional<Student> match
                = studentList.stream()
                .filter(c -> c.getIndex() == index)
                .findFirst();
        if (match.isPresent()) {
            List<Grade> grades = match.get().getGrades();
            GenericEntity<List<Grade>> genericEntity = new GenericEntity<List<Grade>>(grades) {
            };
            return Response.ok(genericEntity).build();
            //return match.get().getGrades().stream();

        } else {
            throw new NotFoundException(new JsonError("Error", "Student " + index + " not found"));
        }
    }

    @POST
    public Response addGrade(@PathParam("index") long index, Grade grade){
        Optional<Student> match = studentList.stream()
                .filter(c -> c.getIndex() == index)
                .findFirst();

        if (match.isPresent()) {
            Optional<Course> match2 = courseList.stream()
                    .filter(c -> c.getId() == grade.getCourse().getId())
                    .findFirst();

            if (match2.isPresent()) {
                Student student = match.get();
                grade.setCourse(match2.get());
                student.addGrade(grade);
                gradeList.add(grade);
                return Response.status(Response.Status.OK).build();
            } else {
                return Response.status(Response.Status.NOT_FOUND).build();
            }
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @GET
    @Path("/{id}")
    public Grade getStudentGrade(@PathParam("index") long index, @PathParam("id") long id) {
        Optional<Grade> matchGrade = gradeList.stream()
                .filter(c -> c.getId() == id)
                .findFirst();
        Optional<Student> matchStudent = studentList.stream()
                .filter(c -> c.getIndex() == index)
                .findFirst();
        if(matchStudent.isPresent()) {
            if (matchGrade.isPresent()) {
                return matchGrade.get();
            } else {
                throw new NotFoundException(new JsonError("Error", "Grade " + id + " not found"));
            }
        } else {
            throw new NotFoundException(new JsonError("Error", "Student " + index + " not found"));
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
