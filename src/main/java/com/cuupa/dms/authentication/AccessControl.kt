package com.cuupa.dms.authentication;

import java.io.Serializable;

public interface AccessControl extends Serializable {

    boolean signIn(final String username, final String password, final boolean alreadyHashed);

    User getUser(String username);

    boolean isUserSingedIn();

    boolean isUserRole(final UserRole role);

    String getPrincipalName();

    void singOut();

    boolean register(final String username, final String password, final String salt, final String firstname, final String lastname, final String accesstoken);

    boolean save(User user);

    boolean signIn(final String username, final String accessToken);
}
