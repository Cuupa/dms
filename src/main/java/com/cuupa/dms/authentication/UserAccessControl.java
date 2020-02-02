package com.cuupa.dms.authentication;

public interface UserAccessControl {

    boolean isAuthenticated(String username, String password);
}
