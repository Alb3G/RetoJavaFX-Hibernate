package org.intro.retojfxhib;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.intro.retojfxhib.dto.CopyDTO;
import org.intro.retojfxhib.models.Movie;
import org.intro.retojfxhib.models.SessionToken;
import org.intro.retojfxhib.models.User;

/**
 * Singleton de la Session donde gestionaremos datos del usuario,
 * y el propio usuario mientras este navega por la aplicación si este
 * la abandona la sesion debe de eliminarse por completo para que pueda ser
 * usada por el siguiente usuario que se conecte a la app.
 */
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
    private boolean isRememberUser;

    /**
     * Método para recuperar la instancia del singleton.
     * @return SessionManager instance.
     */
    public static synchronized SessionManager getInstance() {
        if (instance == null) {
            instance = new SessionManager();
        }
        return instance;
    }

    /**
     * Método para vaciar de datos la sesion.
     */
    public void logout() {
        currentUser = null;
        selectedMovie = null;
        selectedCopyDTO = null;
        verificationCode = null;
    }
}
