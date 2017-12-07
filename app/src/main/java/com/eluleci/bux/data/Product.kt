package com.eluleci.bux.data

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Product(
        @SerializedName("symbol")
        val symbol: String = "",

        @SerializedName("displayName")
        val displayName: String = "",

        @SerializedName("securityId")
        val securityId: String = "",

        @SerializedName("quoteCurrency")
        val quoteCurrency: String = "",

        @SerializedName("displayDecimals")
        val displayDecimals: Int = 0,

        @SerializedName("maxLeverage")
        val maxLeverage: Int = 0,

        @SerializedName("multiplier")
        val multiplier: Int = 0,

        @SerializedName("currentPrice")
        val currentPrice: Price = Price("", "", 0),

        @SerializedName("closingPrice")
        val closingPrice: Price = Price("", "", 0),

        @SerializedName("dayRange")
        val dayRange: Range = Range("", 0, "", ""),

        @SerializedName("yearRange")
        val yearRange: Range = Range("", 0, "", ""),

        @SerializedName("category")
        val category: String = "",

        @SerializedName("favorite")
        val favorite: Boolean = false,

        @SerializedName("productMarketStatus")
        val productMarketStatus: String = "",

        @SerializedName("tags")
        val tags: List<String> = emptyList()

) : Serializable