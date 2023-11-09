package nl.kokopellimusic.kokopelli5.entities;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.annotation.Generated;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.ToString;
import se.michaelthelin.spotify.model_objects.specification.Track;

@Entity
@ToString
public class GameSession extends PanacheEntityBase {

  // Cookie that is also used for the spotify session
  @Id
  @Generated("uuid")
  public String cookie;

  // List of players
  @OneToMany(fetch = FetchType.EAGER, orphanRemoval = true, cascade = CascadeType.ALL, mappedBy = "gameSession")
  public List<Player> players = new ArrayList<Player>();

  // Games that is currently playing
  @Column(length = 65535, columnDefinition = "text")
  public String currentGame;

  // When the last game was played?
  public Date lastGameAt;

  // The current song, this is a very long json string
  @Column(length = 65535, columnDefinition = "text")
  public String currentSong;
  
  @CreationTimestamp
  public Date createdAt;

  @UpdateTimestamp
  public Date updatedAt;

  public void addPlayer(Player p) {
    p.gameSession = this;
    players.add(p);
  }

  public void setCurrentSong(Track track) {
    var mapper = new ObjectMapper();
    try {
      this.currentSong = mapper.writeValueAsString(track);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public static GameSession get(String cookie) {
    return find("cookie", cookie).firstResult();
  }
}