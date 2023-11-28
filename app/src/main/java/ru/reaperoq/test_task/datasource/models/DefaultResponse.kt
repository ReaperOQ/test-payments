package ru.reaperoq.test_task.datasource.models

import com.google.gson.annotations.SerializedName

data class DefaultResponse<T>(
    @SerializedName("success")
    val success: Boolean,
    @SerializedName("response")
    val data: T? = null,
    @SerializedName("error")
    val error: Error? = null,
)

data class Error(
    @SerializedName("error_code")
    val code: Int,
    @SerializedName("error_message")
    val message: String
)