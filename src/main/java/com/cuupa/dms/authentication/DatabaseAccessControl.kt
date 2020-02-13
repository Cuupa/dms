package com.cuupa.dms.authentication

import com.cuupa.dms.authentication.CurrentUser.get
import com.cuupa.dms.authentication.CurrentUser.set
import com.cuupa.dms.database.user.DbUser
import com.cuupa.dms.database.user.UserRepository
import com.cuupa.dms.service.EncryptionService
import com.vaadin.flow.component.UI
import com.vaadin.flow.server.VaadinSession
import org.springframework.beans.factory.annotation.Autowired
import java.security.NoSuchAlgorithmException
import java.security.spec.InvalidKeySpecException

class DatabaseAccessControl(@param:Autowired private val userRepository: UserRepository, @param:Autowired private val encryptionService: EncryptionService) : AccessControl {

    override fun signIn(username: String, password: String, alreadyHashed: Boolean): Boolean {
        var dbUser = userRepository.findByUsername(username)
        if (dbUser != null) {
            val encryptedPassword = getPassword(password, alreadyHashed, dbUser)
            dbUser = userRepository.findByUsernameAndPassword(username, encryptedPassword)
            if (dbUser != null) {
                val user = User(dbUser.id!!,
                        dbUser.username,
                        dbUser.firstname,
                        dbUser.lastname,
                        dbUser.isConfirmed)
                set(user)
            }
            return true
        }
        return false
    }

    override fun signIn(username: String, accessToken: String): Boolean {
        return userRepository.findByUsernameAndAccesstoken(username, accessToken) != null
    }

    override fun getUser(username: String): User? {
        val dbUser = userRepository.findByUsername(username)
        val user = User(dbUser!!.id!!,
                dbUser.username,
                dbUser.firstname,
                dbUser.lastname,
                dbUser.isConfirmed)
        return user
    }

    @Throws(InvalidKeySpecException::class, NoSuchAlgorithmException::class)
    private fun getPassword(password: String, alreadyHashed: Boolean, dbUser: DbUser): String {
        return if (!alreadyHashed) {
            encryptionService.getEncryptedPassword(password, dbUser.salt)
        } else {
            password
        }
    }

    override val isUserSingedIn: Boolean
        get() = !get().isEmpty

    override fun isUserRole(role: UserRole?): Boolean {
        return false
    }

    override val principalName: String
        get() = get().username

    override fun singOut() {
        VaadinSession.getCurrent().session.invalidate()
        UI.getCurrent().navigate("")
    }

    override fun register(username: String, password: String, salt: String, firstname: String?, lastname: String?, accesstoken: String): Boolean {
        val byUsername = userRepository.findByUsername(username)
        return if (byUsername == null) {
            val dbUser = DbUser(username, password, salt, firstname, lastname, accesstoken, false)
            userRepository.save(dbUser) != null
        } else {
            false
        }
    }

    override fun save(user: User, encryptedPassword: String, newSalt: String, accessToken: String): Boolean {
        val dbUser = DbUser(user.id,
                user.username,
                encryptedPassword,
                newSalt,
                user.firstname,
                user.lastname,
                accessToken,
                user.confirmed)
        return userRepository.save(dbUser) != null
    }

    override fun save(user: User, accessToken: String): Boolean {
        val dbUser = userRepository.findByUsername(user.username)!!
        val updatedUser = DbUser(user.id, user.username, dbUser.password, dbUser.salt, user.firstname, user.lastname, accessToken, user.confirmed)
        return userRepository.save(updatedUser) != null
    }

    override fun getAccessToken(username: String): String {
        return userRepository.findByUsername(username)!!.accesstoken
    }
}