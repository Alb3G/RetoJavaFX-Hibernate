module org.intro.retojfxhib {
    requires javafx.fxml;
    requires org.hibernate.orm.core;
    requires static lombok;
    requires jakarta.persistence;
    requires java.naming;
    requires jbcrypt;
    requires javafx.web;
    requires jakarta.mail;
    requires net.sf.jasperreports.core;
    requires org.apache.commons.io;
    requires java.desktop;

    opens org.intro.retojfxhib.dto;
    opens org.intro.retojfxhib.models;
    opens org.intro.retojfxhib to javafx.fxml;
    exports org.intro.retojfxhib;
    exports org.intro.retojfxhib.controllers;
    opens org.intro.retojfxhib.controllers to javafx.fxml;
    opens org.intro.retojfxhib.services;
}