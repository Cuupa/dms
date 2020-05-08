package com.cuupa.dms.service.extern

import com.google.gson.Gson
import org.springframework.beans.factory.annotation.Value
import org.springframework.web.client.RestTemplate
import java.lang.reflect.Type

class ExternSemanticService(private val restTemplate: RestTemplate, private val gson: Gson, private val listType:
Type) {

    @Value("\${services.remote.semantic.url}")
    private val semanticUrl: String? = null

    fun analyze(text: ByteArray): List<SemanticResult> {
        val response = restTemplate.postForEntity(semanticUrl, text, String::class.java)
        return if (response.statusCode.is2xxSuccessful) {
            gson.fromJson(response.body, listType)
        } else {
            listOf()
        }
    }
}