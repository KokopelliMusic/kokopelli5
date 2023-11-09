package nl.kokopellimusic.kokopelli5;

import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Calendar;
import java.util.UUID;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jboss.resteasy.reactive.RestResponse;

import jakarta.transaction.Transactional;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.NewCookie;
import jakarta.ws.rs.core.Response;
import nl.kokopellimusic.kokopelli5.entities.SpotifySession;
import nl.kokopellimusic.kokopelli5.exceptions.HTTP400;
import se.michaelthelin.spotify.SpotifyApi;
import se.michaelthelin.spotify.SpotifyHttpManager;
import se.michaelthelin.spotify.model_objects.credentials.AuthorizationCodeCredentials;

@Path("/spotify")
public class SpotifyResource {

  @ConfigProperty(name = "kokopelli.spotify.client.id")
  String clientId;

  @ConfigProperty(name = "kokopelli.spotify.client.secret")
  String clientSecret;

  @ConfigProperty(name = "kokopelli.spotify.redirect.uri")
  String redirectUri;

  @ConfigProperty(name = "kokopelli.frontend.url")
  String frontendUrl;

  SpotifyApi api;

  private void createClient() {
    if (api != null) return;

    URI uri = SpotifyHttpManager.makeUri(redirectUri);

    this.api = new SpotifyApi.Builder()
      .setClientId(clientId)
      .setClientSecret(clientSecret)
      .setRedirectUri(uri)
      .build();
  }

  @Path("/login")
  @GET
  public RestResponse<Object> redirect() {
    // Redirect to spotify login page
    if (api == null) createClient();

    URI uri = api.authorizationCodeUri()
      .scope("user-read-private,user-read-playback-state,user-modify-playback-state,user-read-currently-playing")
      .show_dialog(true)
      .build()
      .execute();

    return RestResponse.seeOther(uri);
  }


  @Path("/login/callback")
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @Transactional
  public Response login(
    @QueryParam("code") String code
  ) {
    if (code == null || code.isEmpty()) {
      throw new HTTP400("No code (?code=) parameter provided");
    }

    if (api == null) createClient();

    AuthorizationCodeCredentials acc = null;

    try {
      acc = api.authorizationCode(code).build().execute();
    } catch (Exception e) {
      e.printStackTrace();

      var sb = new StringBuilder();
      sb.append(frontendUrl);
      sb.append("/login/fail?reason=");
      try {
        sb.append(URLEncoder.encode(e.getMessage(), StandardCharsets.UTF_8.toString()));
      } catch (Exception ignore) {}

      return Response
        .ok(sb.toString())
        .build();
    }

    // Save the session
    SpotifySession session = new SpotifySession();
    Calendar cal = Calendar.getInstance();
    cal.add(Calendar.SECOND, acc.getExpiresIn().intValue());
    
    session.accessToken = acc.getAccessToken();
    session.refreshToken = acc.getRefreshToken();
    session.expiresAt = cal.getTime();
    session.cookie = UUID.randomUUID().toString();

    session.persist();

    NewCookie cookie = new NewCookie.Builder("kokopelli5-token").value(session.cookie).path("/").build();

    return Response
      .seeOther(URI.create(frontendUrl + "/setup"))
      .cookie(cookie)
      .build();
  }
}
