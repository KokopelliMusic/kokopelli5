package nl.kokopellimusic.kokopelli5.exceptions;

import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;


public class HTTP400 extends BadRequestException {
  public HTTP400(String message) {
    super(Response
      .status(400)
      .entity("{\"error\": \"" + message + "\", \"status\": 400}")
      .type(MediaType.APPLICATION_JSON)
      .build());
  }
}
