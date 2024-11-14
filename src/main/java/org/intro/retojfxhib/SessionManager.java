package org.intro.retojfxhib;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.intro.retojfxhib.dto.CopyDTO;
import org.intro.retojfxhib.models.Movie;
import org.intro.retojfxhib.models.SessionToken;
import org.intro.retojfxhib.models.User;

@Getter
@Setter
@NoArgsConstructor
public class SessionManager {
    private static SessionManager instance;
    private User currentUser;
    private Movie selectedMovie;
    private CopyDTO selectedCopyDTO;
    private String verificationCode;
    private SessionToken sessionToken;

    public static synchronized SessionManager getInstance() {
        if (instance == null) {
            instance = new SessionManager();
        }
        return instance;
    }

    public void logout() {
        currentUser = null;
        selectedMovie = null;
        selectedCopyDTO = null;
        verificationCode = null;
    }
}
