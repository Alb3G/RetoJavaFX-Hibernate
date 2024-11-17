package org.intro.retojfxhib.services;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.intro.retojfxhib.models.SessionToken;
import org.intro.retojfxhib.utils.Util;
import org.mindrot.jbcrypt.BCrypt;

import java.time.Duration;
import java.time.LocalDateTime;

/**
 * Este servicio podria ser un DAO si quisieramos, pero la mayoría de métodos
 * no se usaban por ello lo extraje en un servicio aparte solo para la sesion.
 * En él gestionamos diferentes métodos para guardar o eliminar el token de sesion
 * de un usuario en particular, y la propiedad de userEmail es para gestionar que
 * el email input del login aparezca relleno al hacer logOut o no.
 */
public final class SessionService {
    private SessionFactory sessionFactory;
    @Getter @Setter
    private static String userEmail;

    public SessionService(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    /**
     * Comprobamos la existencia del token
     * @return Boolean y determina si el token de la sesion existe en la Db.
     */
    public boolean sessionTokenByUserIdExists() {
        boolean res = false;
        try(var session = sessionFactory.openSession()) {
            Query<SessionToken> query = session.createQuery("select st from SessionToken st", SessionToken.class);
            SessionToken sessionToken = query.getSingleResultOrNull();
            if(sessionToken != null)
                res = true;
        }
        return res;
    }

    /**
     * Recupearmos el objeto SessionToken que se encuentre activo en ese momento.
     * @return SessionToken()
     */
    public SessionToken getSessionToken() {
        SessionToken sessionToken;
        try(var session = sessionFactory.openSession()) {
            Query<SessionToken> query = session.createQuery("select st from SessionToken st", SessionToken.class);
            sessionToken = query.getSingleResult();
        }
        return sessionToken;
    }

    /**
     * Creamos un nuevo SessionToken, lo que es el token va encriptado para
     * mejorar la seguridad al transportar los datos de una vista a otra.
     * @param id del usuario que va a referenciar en el token en cuestión.
     */
    public void saveSessionToken(Long id) {
        try(var session = sessionFactory.openSession()) {
            session.beginTransaction();
            SessionToken sessionToken = new SessionToken();
            sessionToken.setUserId(id);
            sessionToken.setToken(BCrypt.hashpw(Util.randomRegisterCode(20), BCrypt.gensalt()));
            sessionToken.setTimeStamp(LocalDateTime.now());
            session.persist(sessionToken);
            session.getTransaction().commit();
        }
    }

    /**
     * Eliminamos el Session token por su identificador.
     * @param id Del token a eliminar.
     */
    public void deleteSessionTokenById(Long id) {
        try(var session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.remove(session.get(SessionToken.class, id));
            session.getTransaction().commit();
        }
    }

    /**
     * Método para comprobar si las 12h desde que se creó el token han transcurrido
     * si es así se procederá a eliminar el token.
     * @return True/False en caso de que esté o no expirado el token.
     */
    public boolean sessionTokenIsExpired() {
        boolean res = false;
        SessionToken sessionToken = getSessionToken();
        LocalDateTime currentTime = LocalDateTime.now();
        if(Duration.between(sessionToken.getTimeStamp(), currentTime).toHours() >= 12) {
            res = true;
            deleteSessionTokenById(sessionToken.getSessionTokenId());
        }

        return res;
    }
}
