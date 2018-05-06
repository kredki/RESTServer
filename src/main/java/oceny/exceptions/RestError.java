package oceny.exceptions;
/*
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

public class RestError implements ExceptionMapper<Throwable> {


    @Override
    public Response toResponse(Throwable ex) {
        ex.printStackTrace();
        return Response.serverError().entity(ex.getMessage()).build();
    }
}*/

//error handling for debug purpose only!!
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class RestError implements ExceptionMapper<Exception> {

    @Override
    public Response toResponse(Exception e) {
        e.printStackTrace();
        return Response
                .status(Status.INTERNAL_SERVER_ERROR)
                .type(MediaType.APPLICATION_JSON)
                .entity(e.getCause())
                .build();
    }

}