package com.eluleci.bux.data

import com.google.gson.annotations.SerializedName

data class PortfolioPerformance(
        @SerializedName("accountValue")
        val accountValue: Price,

        @SerializedName("performance")
        val performance: String,

        @SerializedName("suggestFunding")
        val suggestFunding: Boolean
)