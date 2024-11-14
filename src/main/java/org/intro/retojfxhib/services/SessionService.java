package org.intro.retojfxhib.services;

import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.intro.retojfxhib.models.SessionToken;
import org.intro.retojfxhib.utils.Util;
import org.mindrot.jbcrypt.BCrypt;

import java.time.Duration;
import java.time.LocalDateTime;

public final class SessionService {
    private SessionFactory sessionFactory;

    public SessionService(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

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

    public SessionToken getSessionToken() {
        SessionToken sessionToken;
        try(var session = sessionFactory.openSession()) {
            Query<SessionToken> query = session.createQuery("select st from SessionToken st", SessionToken.class);
            sessionToken = query.getSingleResult();
        }
        return sessionToken;
    }

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

    public void deleteSessionTokenById(Long id) {
        try(var session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.remove(session.get(SessionToken.class, id));
            session.getTransaction().commit();
        }
    }

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
