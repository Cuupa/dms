package com.cuupa.dms.authentication;

import java.io.Serializable;

public interface AccessControl extends Serializable {

    boolean signIn(String username, String password);

    boolean isUserSingedIn();

    boolean isUserRole(UserRole role);

    String getPrincipalName();

    void singOut();

    boolean register(String username, String password, String salt, String firstname, String lastname);
}
