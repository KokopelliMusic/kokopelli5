package nl.kokopellimusic.kokopelli5.entities;

import java.util.Date;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Entity;
import lombok.ToString;
import se.michaelthelin.spotify.SpotifyApi;

@Entity
@ToString
public class SpotifySession extends PanacheEntity {

  public String cookie;
  public String accessToken;
  public String refreshToken;
  public Date expiresAt;

  /**
   * Refreshes the access token if it has expired.
   * Uses the spotify api
   */
  public void refreshToken(SpotifyApi api) {
    throw new UnsupportedOperationException("Not implemented yet");
  }

  public static SpotifySession get(String cookie) {
    return find("cookie", cookie).firstResult();
  }
}