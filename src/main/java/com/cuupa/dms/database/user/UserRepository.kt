package com.cuupa.dms.database.user

import org.springframework.data.repository.CrudRepository

interface UserRepository : CrudRepository<DbUser?, Int?> {
    fun findByUsername(username: String): DbUser?
    fun findByUsernameAndAccesstoken(username: String, accessToken: String): DbUser?
    fun findByUsernameAndPassword(username: String, password: String): DbUser?
    fun save(user: DbUser): DbUser?
    fun existsUserByUsername(username: String): Boolean
}