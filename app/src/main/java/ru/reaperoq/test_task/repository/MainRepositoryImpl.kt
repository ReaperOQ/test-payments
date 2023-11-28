package ru.reaperoq.test_task.repository

import ru.reaperoq.test_task.datasource.MainDataSource
import ru.reaperoq.test_task.datasource.models.Payment
import javax.inject.Inject

class MainRepositoryImpl @Inject constructor(
    private val dataSource: MainDataSource
) : MainRepository {
    override suspend fun login(login: String, password: String): Result<Boolean> =
        dataSource.login(login, password)

    override suspend fun getPayments(): Result<List<Payment>> =
        dataSource.getPayments()

    override fun logout() {
        dataSource.logout()
    }

    override fun isLoggedIn(): Boolean =
        dataSource.isLoggedIn()
}