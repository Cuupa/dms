package com.cuupa.dms.controller

class ProcessParameters {

    private var dueDate: String? = null
    private var username: String = ""

    fun withUser(username: String): ProcessParameters {
        this.username = username
        return this
    }

    fun withDueDate(dueDate: String?): ProcessParameters {
        this.dueDate = dueDate
        return this
    }

    fun getDueDate(): String? {
        return dueDate
    }

    fun getOwner(): String {
        return username
    }
}