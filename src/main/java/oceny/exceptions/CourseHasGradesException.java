package oceny.exceptions;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public class CourseHasGradesException extends WebApplicationException {
    public CourseHasGradesException() {
        super(Response.status(Response.Status.NOT_ACCEPTABLE).type(MediaType.TEXT_PLAIN).build());
    }

    /**
     * Create a HTTP 404 (Not Found) exception.
     * @param message the String that is the entity of the 404 response.
     */
    public CourseHasGradesException(String message) {
        super(Response.status(Response.Status.NOT_ACCEPTABLE).entity(message).type(MediaType.TEXT_PLAIN).build());
    }

    public CourseHasGradesException(JsonError jse) {
        super(Response.status(Response.Status.NOT_ACCEPTABLE).entity(jse).type(MediaType.APPLICATION_JSON).build());
    }

    public CourseHasGradesException(XmlError xmle) {
        super(Response.status(Response.Status.NOT_ACCEPTABLE).entity(xmle).type(MediaType.APPLICATION_XML).build());
    }
}
