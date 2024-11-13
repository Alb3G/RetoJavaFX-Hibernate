package org.intro.retojfxhib.models;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

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