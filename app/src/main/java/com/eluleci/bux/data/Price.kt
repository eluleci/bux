package com.eluleci.bux.data

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Price(
        @SerializedName("currency")
        val currency: String,

        @SerializedName("amount")
        val amount: String,

        @SerializedName("decimals")
        val decimals: Int
) : Serializable {

    override fun toString(): String {
        return "$amount $currency"
    }
}