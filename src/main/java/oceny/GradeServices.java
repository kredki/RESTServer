package oceny;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Path("/")
public class GradeServices {
    private final CopyOnWriteArrayList<Grade> cList = GradeList.getInstance();
    private final CopyOnWriteArrayList<Student> studentList = StudentList.getInstance();

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

    @POST
    @Path("/students/{index}/grades")
    @Produces(MediaType.APPLICATION_JSON)
    public Response addGrade(@PathParam("index") long index, Grade grade){
        Optional<Student> match = studentList.stream()
                .filter(c -> c.getIndex() == index)
                .findFirst();

        if (match.isPresent()) {
            Student student = match.get();
            student.addGrade(grade);
            return Response.status(Response.Status.OK).build();
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
                match.get().rmoveGradeOnList(match2.get());
                Predicate<Grade> grade = c -> c.getId() == id;
                if (!cList.removeIf(grade)) {
                    throw new NotFoundException(new JsonError("Error", "Grade " + id + " not found"));
                }
            } else {
                throw new NotFoundException(new JsonError("Error", "Grade " + id + " not found"));
            }
        } else {
            Response.status(Response.Status.NOT_FOUND).build();
        }
    }
}
