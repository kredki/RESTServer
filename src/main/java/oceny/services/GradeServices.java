package oceny.services;

import oceny.lists.CourseList;
import oceny.lists.GradeList;
import oceny.JsonError;
import oceny.NotFoundException;
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
import java.util.stream.Collectors;

@Path("/")
public class GradeServices {
    private final CopyOnWriteArrayList<Grade> cList = GradeList.getInstance();
    private final CopyOnWriteArrayList<Student> studentList = StudentList.getInstance();
    private final CopyOnWriteArrayList<Course> courseList = CourseList.getInstance();

    @GET
    @Path("/students/{index}/grades")
    @Produces(MediaType.TEXT_PLAIN)
    public String getStudentGrades(@PathParam("index") long index) {
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
    @Path("/students/{index}/grades")
    @Produces(MediaType.APPLICATION_JSON)
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
            throw new oceny.NotFoundException(new JsonError("Error", "Student " + index + " not found"));
        }
    }

    /*@GET
    @Path("/students/{index}/grades")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response getStudentGrades(@HeaderParam("Accept") String accepted, @PathParam("index") long index) {
        String mediaType = MediaType.APPLICATION_JSON;
        if(accepted != null) {
            mediaType = accepted;
        }
        Optional<Student> match
                = studentList.stream()
                .filter(c -> c.getIndex() == index)
                .findFirst();
        if (match.isPresent()) {
            return Response.ok().entity(match.get().getGrades().stream()).type(mediaType).build();

        } else {
            if (mediaType.equals(MediaType.APPLICATION_XML)) {
                throw new NotFoundException(new XmlError("Error", "Student " + index + " not found"));
            } else if(mediaType.equals(MediaType.APPLICATION_JSON)) {
                throw new NotFoundException(new JsonError("Error", "Student " + index + " not found"));
            }
        }
        return null;
    }*/

    @POST
    @Path("/students/{index}/grades")
    @Produces(MediaType.APPLICATION_JSON)
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
                return Response.status(Response.Status.OK).build();
            } else {
                return Response.status(Response.Status.NOT_FOUND).build();
            }
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @GET
    @Path("/students/{index}/grades/{id}")
    @Produces(MediaType.TEXT_PLAIN)
    public String getStudentGradeById(@PathParam("index") long index, @PathParam("id") long id) {
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

    @PUT
    @Path("/students/{index}/grades/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateGrade(@PathParam("index") long index, Grade grade){
        int matchIdx = 0;
        int matchIdx2 = 0;
        Optional<Grade> match = cList.stream()
                .filter(c -> c.getId() == grade.getId())
                .findFirst();
        if (match.isPresent()) {
            matchIdx = cList.indexOf(match.get());
            Optional<Student> match2 = studentList.stream()
                    .filter(c -> c.getIndex() == index)
                    .findFirst();
            if(match2.isPresent()) {
                matchIdx2 = studentList.indexOf(match2.get());
                cList.set(matchIdx, grade);
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
    @Path("/students/{index}/grades/{id}")
    public void deleteGrade(@PathParam("index") long index, @PathParam("id") long id){
        Optional<Student> match = studentList.stream()
                .filter(c -> c.getIndex() == index)
                .findFirst();
        if(match.isPresent()) {
            Optional<Grade> match2 = cList.stream()
                    .filter(c -> c.getId() == id)
                    .findFirst();
            if(match2.isPresent()) {
                match.get().removeGrade(match2.get());
                Predicate<Grade> grade = c -> c.getId() == id;
                if (!cList.removeIf(grade)) {
                    throw new oceny.NotFoundException(new JsonError("Error", "Grade " + id + " not found"));
                }
            } else {
                throw new NotFoundException(new JsonError("Error", "Grade " + id + " not found"));
            }
        } else {
            Response.status(Response.Status.NOT_FOUND).build();
        }
    }
}
