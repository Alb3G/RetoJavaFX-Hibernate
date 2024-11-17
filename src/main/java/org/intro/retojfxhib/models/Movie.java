package org.intro.retojfxhib.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Clase que representa la entidad Movie de la DB.
 * @author Alberto Guzman.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
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
