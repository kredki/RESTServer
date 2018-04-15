package oceny;

import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/")
public class CourseService {
    private final CopyOnWriteArrayList<Course> cList = CourseList.getInstance();

    @GET
    @Path("/courses")
    @Produces(MediaType.TEXT_PLAIN)
    public String getAllCourses() {
        return "---Courses List---\n"
                + cList.stream()
                .map(c -> c.toString())
                .collect(Collectors.joining("\n"));
    }
}
