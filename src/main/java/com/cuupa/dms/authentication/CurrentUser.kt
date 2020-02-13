package com.cuupa.dms.authentication;

import com.vaadin.flow.server.VaadinRequest;
import com.vaadin.flow.server.VaadinService;

import java.util.Objects;

public final class CurrentUser {

    /**
     * The attribute key used to store the username in the session.
     */
    public static final String CURRENT_USER_SESSION_ATTRIBUTE_KEY = CurrentUser.class.getCanonicalName();

    private CurrentUser() {
    }

    /**
     * Returns the name of the current user stored in the current session, or an
     * empty string if no user name is stored.
     *
     * @throws IllegalStateException if the current session cannot be accessed.
     */
    public static User get() {
        User
                currentUser =
                (User) getCurrentRequest().getWrappedSession().getAttribute(CURRENT_USER_SESSION_ATTRIBUTE_KEY);
        return Objects.requireNonNullElse(currentUser, new User());
    }

    /**
     * Sets the name of the current user and stores it in the current session.
     * Using a {@code null} username will remove the username from the session.
     *
     * @throws IllegalStateException if the current session cannot be accessed.
     */
    public static void set(User currentUser) {
        if (currentUser == null) {
            getCurrentRequest().getWrappedSession().removeAttribute(CURRENT_USER_SESSION_ATTRIBUTE_KEY);
        } else {
            getCurrentRequest().getWrappedSession().setAttribute(CURRENT_USER_SESSION_ATTRIBUTE_KEY, currentUser);
        }
    }

    private static VaadinRequest getCurrentRequest() {
        VaadinRequest request = VaadinService.getCurrentRequest();
        if (request == null) {
            throw new IllegalStateException("No request bound to current thread.");
        }
        return request;
    }
}
