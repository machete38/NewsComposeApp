package com.machete3845.newsapi.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class Language
{
    @SerialName("ar")
    ar,
    @SerialName("de")
    de,
    @SerialName("en")
    en,
    @SerialName("es")
    es,
    @SerialName("fr")
    fr,
    @SerialName("he")
    he,
    @SerialName("it")
    it,
    @SerialName("nl")
    nl,
    @SerialName("no")
    no,
    @SerialName("pt")
    pt,
    @SerialName("ru")
    ru,
    @SerialName("sv")
    sv,
    @SerialName("ud")
    ud,
    @SerialName("zh")
    zh,
}