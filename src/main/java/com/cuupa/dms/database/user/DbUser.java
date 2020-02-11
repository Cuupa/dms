package com.cuupa.dms.database.user;

import javax.persistence.*;

@Entity
@Table(name = "dmsusers")
public class DbUser {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private String username;

    private String password;

    private String salt;

    private String firstname;

    private String lastname;

    private String accesstoken;

    public DbUser(String username, String password, String salt, String firstname, String lastname) {
        this.username = username;
        this.password = password;
        this.salt = salt;
        this.firstname = firstname;
        this.lastname = lastname;
    }

    public DbUser() {
    }

    public Integer getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getAccesstoken() {
        return accesstoken;
    }

    public void setAccesstoken(String accesstoken) {
        this.accesstoken = accesstoken;
    }

    @Override
    public String toString() {
        return "User{" +
               "id=" +
               id +
               ", username='" +
               username +
               '\'' +
               ", firstname='" +
               firstname +
               '\'' +
               ", surname='" +
               lastname +
               '\'' +
               '}';
    }

    public String getSalt() {
        return salt;
    }
}
