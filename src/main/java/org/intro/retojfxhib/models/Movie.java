package org.intro.retojfxhib.models;

import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;

@Data
@Entity
@Table(name = "Movie")
public class Movie implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String genre;
    @Column(name = "release_year")
    private Integer releaseYear;
    private String description;
    private String director;
    @Column(name = "teaser_url")
    private String teaserUrl;
    private String poster;
}
