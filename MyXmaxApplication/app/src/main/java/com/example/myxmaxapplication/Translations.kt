package com.example.myxmaxapplication

object Translations {
    private val en = mapOf(
        "TITLE_HOME" to "Christmas Countdown",
        "LABEL_DAYS" to "days",
        "LABEL_HOURS" to "hours",
        "LABEL_MINUTES" to "minutes",
        "TITLE_SETTINGS" to "Settings",
        "SNOW_LABEL" to "Enable snow"
    )

    private val cs = mapOf(
        "TITLE_HOME" to "Odpočet do Vánoc",
        "LABEL_DAYS" to "dny",
        "LABEL_HOURS" to "hod",
        "LABEL_MINUTES" to "min",
        "TITLE_SETTINGS" to "Nastavení",
        "SNOW_LABEL" to "Zapnout sníh"
    )

    fun t(lang: String, key: String): String {
        return when (lang) {
            "cs" -> cs[key] ?: key
            else -> en[key] ?: key
        }
    }
}

