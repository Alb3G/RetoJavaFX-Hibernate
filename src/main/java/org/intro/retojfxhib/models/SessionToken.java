package org.intro.retojfxhib.models;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * Esta clase representa una entidad SessionToken,
 * es una tabla pensada para tener un sólo registro
 * en el que se almacenará de forma temporal el token
 * de la sesion de un usuario para que se mantenga activa
 * hasta un máximo de 12h, controlamos el tiempo que la
 * sesion está activada con el timeStamp del registro.
 * @author Alberto Guzman
 */
@Data
@Entity
public class SessionToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "session_token_id", nullable = false)
    private Long sessionTokenId;
    @Column(name = "user_id")
    private Long userId;
    private String token;
    @Column(name = "time_stamp")
    private LocalDateTime timeStamp;

    @Override
    public String toString() {
        return "SessionToken{" +
                "sessionTokenId=" + sessionTokenId +
                ", userId=" + userId +
                ", token=" + token +
                '}';
    }
}