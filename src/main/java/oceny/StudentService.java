package oceny;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

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
}
