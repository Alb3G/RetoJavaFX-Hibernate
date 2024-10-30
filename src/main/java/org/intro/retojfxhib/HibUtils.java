package org.intro.retojfxhib;

import lombok.Getter;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibUtils {
    @Getter
    private static final SessionFactory sessionFactory;

    static {
        sessionFactory = new Configuration()
                .configure()
                .setProperty("hibernate.connection.username", System.getenv("HIBERNATE_USER"))
                .setProperty("hibernate.connection.password", System.getenv("HIBERNATE_PASSWORD"))
                .buildSessionFactory();
    }
}
