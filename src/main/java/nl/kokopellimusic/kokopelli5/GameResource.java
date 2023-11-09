package nl.kokopellimusic.kokopelli5;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import jakarta.transaction.Transactional;
import jakarta.ws.rs.CookieParam;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import nl.kokopellimusic.kokopelli5.entities.GameSession;
import nl.kokopellimusic.kokopelli5.entities.Player;
import nl.kokopellimusic.kokopelli5.exceptions.HTTP400;
import nl.kokopellimusic.kokopelli5.utils.FrontendUtils;
import nl.kokopellimusic.kokopelli5.utils.SpotifyUtils;
import se.michaelthelin.spotify.model_objects.miscellaneous.CurrentlyPlayingContext;
import se.michaelthelin.spotify.model_objects.specification.Track;


@Path("/game")
public class GameResource {
  final static int TIME_BETWEEN_GAMES = 60_000;
  final static int GAME_DURATION = 15_000;

  @ConfigProperty(name = "kokopelli.frontend.url")
  String frontendUrl;

  @GET
  @Path("/start")
  @Transactional
  @Produces(MediaType.TEXT_PLAIN)
  public Response start(
    @QueryParam("players") String players,
    @CookieParam("kokopelli5-token") String cookie
  ) {
    // Players is a comma separated list of player names
    // Check if this is the case
    if (players == null || players.length() == 0) {
      throw new HTTP400("No players given");
    }

    // Split the players
    String[] playerNames = players.split(",");
    if (playerNames.length == 0) {
      throw new HTTP400("No players given");
    }

    // Get cookies from request

    if (cookie == null || cookie.length() == 0) {
      throw new HTTP400("No cookie set, return to home");
    }

    // First check if the session already exists
    GameSession existingSession = GameSession.get(cookie);

    if (existingSession != null) {
        var newPlayers = new ArrayList<Player>();
      
        Arrays.stream(players.split(",")).forEach(newPlayer -> {
          existingSession.players.forEach(oldPlayer -> {
            if (oldPlayer.name.equals(newPlayer)) {
              oldPlayer.active = true;
            } else {
              Player player = new Player();
              player.name = newPlayer;
              player.score = 0;
              player.active = true;
              newPlayers.add(player);
            }
            }
          );
        });
      
        existingSession.players.addAll(newPlayers);
      
        existingSession.persistAndFlush();
        return Response.ok().build();
      }



    // Create a new game session
    GameSession session = new GameSession();
    session.cookie = cookie;
    
    // Set the last game at to now
    // This makes sure that no games are played at the start
    session.lastGameAt = new Date();


    // Create players
    Arrays.stream(players.split(",")).forEach(playerName -> {
      Player player = new Player();
      player.name = playerName;
      player.score = 0;
      player.active = true;
      player.persist();

      session.addPlayer(player);
    });

    // Save the session
    session.persist();

    return Response.ok().build();
  }

  @GET
  @Path("/sync")
  @Transactional
  @Produces(MediaType.APPLICATION_JSON)
  public GameSession sync(
    @CookieParam("kokopelli5-token") String cookie
  ) {
    // Get the game session
    GameSession session = GameSession.get(cookie);

    try {
      CurrentlyPlayingContext currentPlayback = SpotifyUtils.getCurrentlyPlaying(session);

      if (currentPlayback.getIs_playing()) {
        session.setCurrentSong((Track) currentPlayback.getItem());
      } else {
        session.currentSong = null;
      }
      
      session.persist();
    } catch (Exception e) {}

    // Check if the session exists
    if (session == null) {
      throw new HTTP400("No session found");
    }

    // Now, we decide if we want to play a game.
    long lga = 0;
    if (session.lastGameAt != null) lga = session.lastGameAt.getTime();

    // If the last game was played more than 5 minutes ago, we play a game
    if (System.currentTimeMillis() - lga > TIME_BETWEEN_GAMES) {
      var randomGame = FrontendUtils.getRandomGame();
    
      session.currentGame = randomGame;
      session.lastGameAt = new Date();
      session.persist();
    // if more than one minute ago and less than 5 minutes
    } else if (System.currentTimeMillis() - lga > GAME_DURATION) {
      session.currentGame = null;
      session.persist();
    }

    // Return the session
    return session;
  }
}
