package com.cuupa.dms.service.extern

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.springframework.beans.factory.annotation.Value
import org.springframework.web.client.RestTemplate
import java.util.*

class ExternSemanticService {

    private val template = RestTemplate()
    private val gson = Gson()
    @Value("\${services.remote.semantic.url}")
    private val semanticUrl: String? = null

    fun analize(text: ByteArray): List<SemanticResult> {
        val response = template.postForEntity(semanticUrl, text, String::class.java)
        return if (response.statusCode.is2xxSuccessful) {
            val listType = object : TypeToken<ArrayList<SemanticResult?>?>() {}.type
            gson.fromJson(response.body, listType)
        } else {
            throw RuntimeException()
        }
    }
}