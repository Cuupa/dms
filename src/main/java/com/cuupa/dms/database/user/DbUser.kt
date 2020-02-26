package com.cuupa.dms.database.user

import javax.persistence.*

@Entity
@Table(name = "dmsusers")
class DbUser {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Int? = null
        private set
    lateinit var username: String
    lateinit var password: String
    lateinit var salt: String
    var firstname: String? = null
    var lastname: String? = null
    lateinit var accesstoken: String
    var isConfirmed = false

    constructor(id: Int, username: String, password: String, salt: String, firstname: String?, lastname: String?,
                accesstoken: String, confirmed: Boolean) {
        this.id = id
        this.username = username
        this.password = password
        this.salt = salt
        this.firstname = firstname
        this.lastname = lastname
        this.accesstoken = accesstoken
        isConfirmed = confirmed
    }

    constructor(username: String, password: String, salt: String, firstname: String?, lastname: String?,
                accesstoken: String, confirmed: Boolean) {
        this.username = username
        this.password = password
        this.salt = salt
        this.firstname = firstname
        this.lastname = lastname
        this.accesstoken = accesstoken
        isConfirmed = confirmed
    }

    constructor()

    override fun toString(): String {
        return "User{" + "id=" + id + ", username='" + username + '\'' + ", firstname='" +
                firstname +
                '\'' +
                ", lastname='" +
                lastname +
                '\'' +
                '}'
    }

}