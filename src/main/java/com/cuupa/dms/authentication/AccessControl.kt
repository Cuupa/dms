package com.cuupa.dms.authentication

import java.io.Serializable

interface AccessControl : Serializable {

    val isUserSingedIn: Boolean
    val principalName: String

    fun signIn(username: String, password: String, alreadyHashed: Boolean): Boolean
    fun getUser(username: String): User?
    fun isUserRole(role: UserRole?): Boolean
    fun singOut()
    fun register(username: String, password: String, salt: String, firstname: String?, lastname: String?,
                 accesstoken: String): Boolean

    fun save(user: User, encryptedPassword: String, newSalt: String, accessToken: String): Boolean
    fun signIn(username: String, accessToken: String): Boolean
    fun save(user: User, accessToken: String): Boolean
    fun getAccessToken(username: String): String
}