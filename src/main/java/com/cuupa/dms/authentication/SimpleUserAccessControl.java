package com.cuupa.dms.authentication;

public class SimpleUserAccessControl implements UserAccessControl {

    @Override
    public boolean isAuthenticated(String username, String password) {
        return true;
    }
}
