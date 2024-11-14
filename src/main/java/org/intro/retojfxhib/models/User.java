package org.intro.retojfxhib.models;

import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@Table(name = "User")
public class User implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "user_name")
    private String userName;
    private String email;
    private String password;
    @Column(name = "is_admin")
    private boolean isAdmin;
    @Column(name = "is_verified")
    private boolean isVerified;
    @OneToMany(mappedBy = "user")
    private List<MovieCopy> copies;
    @Column(name = "created_at")
    private LocalDateTime createdAt;


    public void addCopy(MovieCopy movieCopy) {
        movieCopy.setUser(this);
        copies.add(movieCopy);
    }

    @Override
    public String toString() {
        return "User{" +
                "email=" + email  +
                ", Admin=" + isAdmin +
                ", copies=" + getCopies() +
                '}';
    }
}
