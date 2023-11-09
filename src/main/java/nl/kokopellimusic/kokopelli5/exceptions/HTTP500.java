package nl.kokopellimusic.kokopelli5.exceptions;

import jakarta.ws.rs.ServerErrorException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;


public class HTTP500 extends ServerErrorException {
  public HTTP500(String message) {
    super(Response
      .status(500)
      .entity("{\"error\": \"" + message + "\", \"status\": 500}")
      .type(MediaType.APPLICATION_JSON)
      .build());
  }
}
