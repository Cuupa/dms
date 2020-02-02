package com.cuupa.dms.authentication;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.server.VaadinSession;
import org.apache.commons.lang3.StringUtils;

public class BasicAccessControl implements AccessControl {

    @Override
    public boolean signIn(String username, String password) {
        if (StringUtils.isBlank(username)) {
            return false;
        }


        if (!username.equals(password)) {
            return false;
        }

        CurrentUser.set(username);
        return true;
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
        return CurrentUser.get();
    }

    @Override
    public void singOut() {
        VaadinSession.getCurrent().getSession().invalidate();
        UI.getCurrent().navigate("");
    }
}
