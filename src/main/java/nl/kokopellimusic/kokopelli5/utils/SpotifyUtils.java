package nl.kokopellimusic.kokopelli5.utils;

import java.util.Calendar;

import org.eclipse.microprofile.config.ConfigProvider;

import jakarta.transaction.Transactional;
import nl.kokopellimusic.kokopelli5.entities.GameSession;
import nl.kokopellimusic.kokopelli5.entities.SpotifySession;
import nl.kokopellimusic.kokopelli5.exceptions.SessionInvalidException;
import se.michaelthelin.spotify.SpotifyApi;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;
import se.michaelthelin.spotify.exceptions.detailed.UnauthorizedException;
import se.michaelthelin.spotify.model_objects.miscellaneous.CurrentlyPlayingContext;

public class SpotifyUtils {

  @Transactional
  public static CurrentlyPlayingContext getCurrentlyPlaying(GameSession gameSession) throws SessionInvalidException, SpotifyWebApiException {
    SpotifySession spotifySession = SpotifySession.find("cookie", gameSession.cookie).firstResult();

    if (spotifySession == null) {
      throw new SessionInvalidException("Spotify session not found for cookie: " + gameSession.cookie);
    }

    var cal = Calendar.getInstance();
    // Time 10 minutes ago
    var time = cal.getTimeInMillis() - 600000;
    cal.setTimeInMillis(time);

    if (spotifySession.expiresAt.before(cal.getTime())) {
      spotifySession = refreshToken(gameSession);
    }

    SpotifyApi api = new SpotifyApi.Builder()
      .setAccessToken(spotifySession.accessToken)
      .setRefreshToken(spotifySession.refreshToken)
      .build();

    try {
      return api.getInformationAboutUsersCurrentPlayback().build().execute();
    } catch (UnauthorizedException e) {
      refreshToken(gameSession);
      
      return getCurrentlyPlaying(gameSession);
    } catch (Exception e) {
      e.printStackTrace();  
      throw new SpotifyWebApiException("Could not get current playback", e);
    }
  }

  @Transactional
  public static SpotifySession refreshToken(GameSession gameSession) throws SpotifyWebApiException, SessionInvalidException {
    SpotifySession spotifySession = SpotifySession.find("cookie", gameSession.cookie).firstResult();

    if (spotifySession == null) {
      throw new SessionInvalidException("Spotify session not found for cookie: " + gameSession.cookie);
    }

    String clientId = ConfigProvider.getConfig().getValue("kokopelli.spotify.client.id", String.class);
    String clientSecret = ConfigProvider.getConfig().getValue("kokopelli.spotify.client.secret", String.class);

    SpotifyApi api = new SpotifyApi.Builder()
      .setClientId(clientId)
      .setClientSecret(clientSecret)
      .setRefreshToken(spotifySession.refreshToken)
      .build();

    try {
      var acc = api.authorizationCodeRefresh().build().execute();

      spotifySession.accessToken = acc.getAccessToken();
      Calendar cal = Calendar.getInstance();
      cal.add(Calendar.SECOND, acc.getExpiresIn());
      spotifySession.expiresAt = cal.getTime();

      spotifySession.persist();

      return spotifySession;
    } catch (Exception e) {
      e.printStackTrace();  
      throw new SpotifyWebApiException("Could not refresh token", e);
    }
  }
}
