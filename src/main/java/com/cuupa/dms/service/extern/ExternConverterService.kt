package com.cuupa.dms.service.extern

import com.google.gson.Gson
import com.packenius.hexr.HexR
import org.springframework.beans.factory.annotation.Value
import org.springframework.web.client.RestTemplate
import java.util.*

class ExternConverterService(private val template: RestTemplate, private val gson: Gson,
                             private val decoder: Base64.Decoder) {

    @Value("\${services.remote.converter.url}")
    private val converterUrl: String? = null

    fun convert(document: Document): Document {
        println("Base64")
        println(document.contentBase64)
        println("HexDump")
        println(HexR().dump(document.content))

        val decode = decoder.decode(document.contentBase64)
        assert(document.content.contentEquals(decode))

        val format = converterUrl + "target=pdf"
        val json = gson.toJson(document)
        val response = template.postForEntity(format, json, String::class.java)
        return if (response.statusCode.is2xxSuccessful) {
            gson.fromJson(response.body, Document::class.java)
        } else {
            Document("", ByteArray(0))
        }
    }
}