package com.cuupa.dms.authentication

import com.vaadin.flow.server.VaadinRequest
import com.vaadin.flow.server.VaadinService

object CurrentUser {
    /**
     * The attribute key used to store the username in the session.
     */
    private val CURRENT_USER_SESSION_ATTRIBUTE_KEY: String = CurrentUser::class.java.canonicalName

    /**
     * Returns the name of the current user stored in the current session, or an
     * empty string if no user name is stored.
     *
     * @throws IllegalStateException if the current session cannot be accessed.
     */
    @JvmStatic
    fun get(): User {
        val currentUser: User? = currentRequest.wrappedSession.getAttribute(CURRENT_USER_SESSION_ATTRIBUTE_KEY) as User?
        return currentUser ?: User()
    }

    /**
     * Sets the name of the current user and stores it in the current session.
     * Using a `null` username will remove the username from the session.
     *
     * @throws IllegalStateException if the current session cannot be accessed.
     */
    @JvmStatic
    fun set(currentUser: User?) {
        if (currentUser == null) {
            currentRequest.wrappedSession.removeAttribute(CURRENT_USER_SESSION_ATTRIBUTE_KEY)
        } else {
            currentRequest.wrappedSession.setAttribute(CURRENT_USER_SESSION_ATTRIBUTE_KEY, currentUser)
        }
    }

    private val currentRequest: VaadinRequest
        get() = VaadinService.getCurrentRequest()
                ?: throw IllegalStateException("No request bound to current thread.")
}