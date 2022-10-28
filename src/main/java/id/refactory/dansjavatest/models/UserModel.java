package id.refactory.dansjavatest.models;

import javax.persistence.*;

@Entity
@Table(name = "tbl_user")
public class UserModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "username", unique = true)
    private String username;

    @Column(name = "password", unique = true)
    private String password;

    @Column(name = "createdAt")
    private Long createdAt;

    public UserModel() {

    }

    public UserModel(Long id, String username, String password, Long createdAt) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public Long getCreatedAt() {
        return createdAt;
    }
}
