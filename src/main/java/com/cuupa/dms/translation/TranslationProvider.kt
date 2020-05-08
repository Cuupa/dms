package com.cuupa.dms.translation

import com.vaadin.flow.i18n.I18NProvider
import org.apache.commons.logging.Log
import org.apache.commons.logging.LogFactory
import java.text.MessageFormat
import java.util.*

class TranslationProvider : I18NProvider {

    private val log: Log = LogFactory.getLog(TranslationProvider::class.java)

    private val bundlePrefix = "translate"

    private val localeEn = Locale("en", "GB")
    private val localeDe = Locale("de", "DE")

    private val locales = listOf(localeEn, localeDe)

    override fun getProvidedLocales(): List<Locale> {
        return locales
    }

    override fun getTranslation(key: String?, locale: Locale, params: Array<Any>): String {
        if (key == null) {
            log.warn("TranslationProvider got request with null value key")
            return ""
        }

        val resourceBundle = ResourceBundle.getBundle(bundlePrefix, locale)
        val value = resourceBundle.getString(key)

        if (params.isNotEmpty()) {
            return MessageFormat.format(value, params)
        }

        return value
    }
}