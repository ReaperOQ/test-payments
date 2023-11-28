package ru.reaperoq.test_task.repository

import ru.reaperoq.test_task.datasource.models.Payment
import javax.inject.Singleton

@Singleton
interface MainRepository {
    suspend fun login(login: String, password: String): Result<Boolean>

    suspend fun getPayments(): Result<List<Payment>>

    fun logout()

    fun isLoggedIn(): Boolean
}