package nl.kokopellimusic.kokopelli5.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.ToString;

@Entity
@ToString
public class Player extends PanacheEntity {
  public String name;
  public int score;
  public boolean active;

  @ManyToOne
  @JsonIgnore @ToString.Exclude
  public GameSession gameSession;
}
