package org.intro.retojfxhib.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.intro.retojfxhib.HibUtils;
import org.intro.retojfxhib.dao.MovieDAO;
import org.intro.retojfxhib.models.Movie;
import org.intro.retojfxhib.models.MovieCopy;

/**
 * La clase CopyDTO es una solución al problema de como mostrar
 * de una forma sencilla los datos de diferentes objetos en función
 * de lo que necesitamos para esta caso de uso concreto de la tabla.
 * @author Alberto Guzman
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CopyDTO {
    private MovieCopy copy;
    private String condition;
    private String platform;

    /**
     * Método que permite al titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
     * mapear la clase CopyDTO y asignarle a la columna Title el Título de una Movie teniendo un
     * objeto MovieCopy.
     * @return MovieTitle: String.
     */
    public String getTitle() {
        return new MovieDAO(HibUtils.getSessionFactory()).findById(copy.getMovieId()).getTitle();
    }

    /**
     * Método para devolver una película específica del objeto CopyDTO.
     * @return Movie
     */
    public Movie getMovie() {
        return new MovieDAO(HibUtils.getSessionFactory()).findById(copy.getMovieId());
    }
}
