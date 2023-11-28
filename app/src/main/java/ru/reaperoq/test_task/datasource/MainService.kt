package ru.reaperoq.test_task.datasource

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import ru.reaperoq.test_task.datasource.models.DefaultResponse
import ru.reaperoq.test_task.datasource.models.LoginRequest
import ru.reaperoq.test_task.datasource.models.LoginResponse
import ru.reaperoq.test_task.datasource.models.Payment

interface MainService {

    @POST
        ("api-test/login")
    suspend fun login(@Body body: LoginRequest): Response<DefaultResponse<LoginResponse>>

    @GET
        ("api-test/payments")
    suspend fun getPayments(@Header("token") token: String): Response<DefaultResponse<List<Payment>>>
}