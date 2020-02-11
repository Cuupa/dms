package com.cuupa.dms.authentication;

import org.apache.commons.lang3.StringUtils;

public class User {

    private final String username;

    private String name;

    private String lastname;

    public User(String username, String name, String lastname) {
        this.username = username;
        this.name = name;
        this.lastname = lastname;
    }

    public User() {
        username = "";
    }

    public String getUsername() {
        return username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
}
