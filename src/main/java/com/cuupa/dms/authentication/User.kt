package com.cuupa.dms.authentication

data class User(var id: Int, val username: String, var firstname: String?, var lastname: String?, var confirmed: Boolean) {

    constructor(username: String, firstname: String?, lastname: String?, confirmed: Boolean) : this(0, username, firstname, lastname, confirmed)

    constructor() : this(0, "", null, null, false)

    val isEmpty: Boolean
        get() = username.isEmpty()

}