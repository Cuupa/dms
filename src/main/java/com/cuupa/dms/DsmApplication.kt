package com.cuupa.dms

import com.cuupa.dms.storage.StorageService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

@SpringBootApplication
open class DsmApplication {

    @Autowired
    private val storage: StorageService? = null

    //@PostConstruct
    fun setup() {
        storage!!.deleteAll()

    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            SpringApplication.run(DsmApplication::class.java, *args)
        }
    }
}