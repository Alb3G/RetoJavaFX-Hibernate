package org.intro.retojfxhib.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.intro.retojfxhib.HibUtils;
import org.intro.retojfxhib.dao.MovieDAO;
import org.intro.retojfxhib.models.Movie;
import org.intro.retojfxhib.models.MovieCopy;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CopyDTO {
    private MovieCopy copy;
    private String condition;
    private String platform;

    public String getTitle() {
        return new MovieDAO(HibUtils.getSessionFactory()).findById(copy.getMovieId()).getTitle();
    }

    public Movie getMovie() {
        return new MovieDAO(HibUtils.getSessionFactory()).findById(copy.getMovieId());
    }
}
