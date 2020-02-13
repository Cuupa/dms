package com.cuupa.dms.service.extern

data class Metadata(val name: String, val value: String) {

    override fun toString(): String {
        return "name: $name value: $value"
    }

}