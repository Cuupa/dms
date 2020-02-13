package com.cuupa.dms.authentication

import com.vaadin.flow.component.UI
import com.vaadin.flow.server.VaadinSession
import org.apache.commons.lang3.StringUtils

class BasicAccessControl : AccessControl {

    override fun signIn(username: String, password: String, alreadyHashed: Boolean): Boolean {
        if (StringUtils.isBlank(username)) {
            return false
        }
        if (username != password) {
            return false
        }
        CurrentUser.set(User(0, username, username, username, false))
        return true
    }

    override fun getUser(username: String): User? {
        return null
    }

    override val isUserSingedIn: Boolean
        get() = !CurrentUser.get().isEmpty

    override fun isUserRole(role: UserRole?): Boolean {
        return false
    }

    override val principalName: String
        get() = CurrentUser.get().username

    override fun singOut() {
        VaadinSession.getCurrent().session.invalidate()
        UI.getCurrent().navigate("")
    }

    override fun register(username: String, password: String, salt: String, firstname: String?, lastname: String?, accesstoken: String): Boolean {
        return true
    }

    override fun save(user: User, encryptedPassword: String, newSalt: String, accessToken: String): Boolean {
        return true
    }

    override fun save(user: User, accessToken: String): Boolean {
        return true
    }

    override fun getAccessToken(username: String): String {
        return ""
    }

    override fun signIn(username: String, accessToken: String): Boolean {
        return false
    }
}