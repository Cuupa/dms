package com.cuupa.dms.authentication;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.server.VaadinSession;
import org.apache.commons.lang3.StringUtils;

public class BasicAccessControl implements AccessControl {

    @Override
    public boolean signIn(String username, String password, boolean alreadyHashed) {
        if (StringUtils.isBlank(username)) {
            return false;
        }


        if (!username.equals(password)) {
            return false;
        }

        CurrentUser.set(new User(0, username, username, username, "", false));
        return true;
    }

    @Override
    public User getUser(String username) {
        return null;
    }

    @Override
    public boolean isUserSingedIn() {
        return !CurrentUser.get().isEmpty();
    }

    @Override
    public boolean isUserRole(UserRole role) {
        return false;
    }

    @Override
    public String getPrincipalName() {
        return CurrentUser.get().getUsername();
    }

    @Override
    public void singOut() {
        VaadinSession.getCurrent().getSession().invalidate();
        UI.getCurrent().navigate("");
    }

    @Override
    public boolean register(String value, String username, String password, String salt, String firstname, String lastname) {
        return true;
    }

    @Override
    public boolean save(User user) {
        return true;
    }
}
