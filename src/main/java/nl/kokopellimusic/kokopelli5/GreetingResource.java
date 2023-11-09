package nl.kokopellimusic.kokopelli5;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/hello")
public class GreetingResource {

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String index() {
        return "Kokopelli 5 API, see https://game.kokopellimusic.nl/ for the game.";
    }
}
