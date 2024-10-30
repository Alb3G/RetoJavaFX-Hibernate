package org.intro.retojfxhib.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.intro.retojfxhib.models.Movie;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CopyDTO {
    private Movie movie;
    private String condition;
    private String platform;

    public String getTitle() {
        return movie.getTitle();
    }
}
