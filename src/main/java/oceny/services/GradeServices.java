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
import javax.ws.rs.core.GenericEntity;
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
    public GenericEntity<List<Grade>> getStudentGrades(@DefaultValue("0") @QueryParam("course") Long courseId,
                                        @DefaultValue("2.0") @QueryParam("gradegreater") float gradeGreater,
                                        @DefaultValue("5.0") @QueryParam("gradeless") float gradeLess,
                                        @PathParam("index") long index) {
        List<Grade> grades = gradeDAO.getStudentGradesList(index, courseId, gradeGreater, gradeLess);
        if(grades == null) {
            throw new NotFoundException(new JsonError("Error", "Student " + index + " not found"));
        } else {
            GenericEntity<List<Grade>> genericEntity = new GenericEntity<List<Grade>>(grades) {};
            return genericEntity;
        }
    }

    @POST
    public Response addGrade(@PathParam("index") long index, Grade grade){
        if (gradeDAO.addGrade(index, grade)) {
            URI uri = null;
            try {
                uri = new URI("http://localhost:8000/oceny/students/" + index +"/grades/" + grade.getId());
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
    public Response updateGrade(@PathParam("index") long index, @PathParam("id") long id, Grade grade){
        if(id != grade.getId()) {
            throw new NotFoundException(new JsonError("Error", "Id in url and in request body are different"));
        }

        if(gradeDAO.updateGrade(index, grade)) {
            return Response.status(Response.Status.OK).build();
        } else {
            throw new NotFoundException(new JsonError("Error", "Grade " + id + " not put"));
        }
    }

    @DELETE
    @Path("/{id}")
    public Response deleteGrade(@PathParam("index") long index, @PathParam("id") long id){
        if (gradeDAO.deleteGrade(index, id)) {
            return Response.status(202).build();
        } else {
            throw new NotFoundException(new JsonError("Error", "Grade " + id +" or student " + index + " not found"));
        }
    }
}
