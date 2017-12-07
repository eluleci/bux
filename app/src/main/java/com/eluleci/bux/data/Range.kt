package com.eluleci.bux.data

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Range(
        @SerializedName("currency")
        val currency: String,

        @SerializedName("decimals")
        val decimals: Int,

        @SerializedName("high")
        val high: String,

        @SerializedName("low")
        val low: String
) : Serializable
