package oceny;

import java.io.IOException;
import java.net.URI;

import com.mongodb.MongoClient;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.linking.DeclarativeLinkingFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;

import javax.ws.rs.core.Application;

public class RESTServer {

    // Base URI the Grizzly HTTP server will listen on
    public static final String BASE_URI = "http://localhost:8000/oceny/";

    public static HttpServer startServer() {
        // create a resource config that scans for JAX-RS resources and providers
        // in com.example.rest package
        final ResourceConfig rc = new ResourceConfig().packages("oceny")
                .packages("org.glassfish.jersey.examples.linking").register(DeclarativeLinkingFeature.class);

        // create and start a new instance of grizzly http server
        // exposing the Jersey application at BASE_URI
        return GrizzlyHttpServerFactory.createHttpServer(URI.create(BASE_URI), rc);
    }

    public static void main(String[] args) throws IOException {
        /*final Morphia morphia = new Morphia();

        // tell Morphia where to find your classes
        // can be called multiple times with different packages or classes
        morphia.mapPackage("oceny");

        // create the Datastore connecting to the default port on the local host
        final Datastore datastore = morphia.createDatastore(new MongoClient(), "eproto");
        datastore.ensureIndexes();
        */

        final HttpServer server = startServer();
        System.out.println(String.format("Jersey app started with WADL available at "
                + "%sapplication.wadl\nHit enter to stop it...", BASE_URI));
        System.in.read();
        server.stop();
    }
}
