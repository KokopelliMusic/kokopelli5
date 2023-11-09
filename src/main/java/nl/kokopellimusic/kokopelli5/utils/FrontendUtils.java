package nl.kokopellimusic.kokopelli5.utils;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;

public class FrontendUtils {

  private static final String GAME_PATH = "games";
  
  public static URI uri(String url, String path) {

    if (path.startsWith("/")) {
      path = path.substring(1);
    }

    return URI.create(url + "/" + path);
  }

  /**
   * Read a random JSON file from the resources directory
   */
  public static String getRandomGame() {
    try {
      var uri = Thread.currentThread().getContextClassLoader().getResource(GAME_PATH).toURI();
      File[] files = (new File(uri)).listFiles();

      // Now read a random file
      var randomFile = files[(int) (Math.random() * files.length)];
      var str = new String(Files.readAllBytes(randomFile.toPath()));

      return str;
    } catch (IOException|URISyntaxException e) {
      e.printStackTrace();
      System.err.println("Could not read games directory, exiting...");
      System.exit(1);
    }



    return "";
  }
}
