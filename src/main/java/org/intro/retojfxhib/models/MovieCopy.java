package org.intro.retojfxhib.models;

import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;

@Data
@Entity
@Table(name = "MovieCopy")
public class MovieCopy implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "copy_id")
    private Long copyId;
    @Column(name = "movie_id")
    private Long movieId;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    @Column(name = "movie_condition")
    private String movieCondition;
    private String platform;

    @Override
    public String toString() {
        return "MovieCopy{" +
                "copyId=" + copyId +
                ", userId=" + user.getId() +
                ", movieId=" + movieId +
                ", movieCondition=" + movieCondition +
                ", platform=" + platform +
                '}';
    }
}
