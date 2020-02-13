package com.cuupa.dms.authentication;

import org.apache.commons.lang3.StringUtils;

public class User {

    private final String username;

    private String firstname;

    private String lastname;

    private String accessToken;

    private String salt;

    private String password;

    private boolean confirmed;

    private int id;

    public User(int id, String username, String firstname, String lastname, String accessToken, boolean confirmed) {
        this.id = id;
        this.username = username;
        this.firstname = firstname;
        this.lastname = lastname;
        this.accessToken = accessToken;
        this.confirmed = confirmed;
    }

    public User() {
        username = "";
    }

    public String getUsername() {
        return username;
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

    public boolean isEmpty() {
        return StringUtils.isEmpty(username);
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isConfirmed() {
        return confirmed;
    }

    public int getId() {
        return id;
    }
}
