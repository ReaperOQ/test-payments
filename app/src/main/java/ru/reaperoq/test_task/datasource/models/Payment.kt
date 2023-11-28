package ru.reaperoq.test_task.datasource.models

import com.google.gson.annotations.SerializedName

data class Payment(
    @SerializedName("id")
    val id: Int,

    @SerializedName("title")
    val title: String,

    @SerializedName("amount")
    val amount: Double? = null,

    @SerializedName("created")
    val created: Long? = null,
)
