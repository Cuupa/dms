package com.cuupa.dms

import com.cuupa.dms.storage.StorageService
import com.cuupa.dms.storage.document.Document
import com.cuupa.dms.storage.tag.Tag
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import java.time.LocalDateTime

@SpringBootApplication
open class DsmApplication {

    @Autowired
    private val storage: StorageService? = null

    fun setup() {
        storage!!.deleteAll()
        val document = Document("",
                "filename.pdf",
                "company 1",
                "user",
                LocalDateTime.now(),
                listOf(Tag("Bill"), Tag("Invoice")), "")
        val document2 = Document("",
                "filename2.pdf",
                "company 2",
                "user",
                LocalDateTime.now(),
                listOf(Tag("Bill"), Tag("Invoice")), "")
        val document3 = Document("",
                "filename3.pdf",
                "company 2",
                "a",
                LocalDateTime.now(),
                listOf(Tag("Bill"), Tag("Invoice")), "")
        storage.save(document)
        storage.save(document2)
        storage.save(document3)
    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            SpringApplication.run(DsmApplication::class.java, *args)
        }
    }
}