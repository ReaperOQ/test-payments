package ru.reaperoq.test_task.datasource

import android.util.Log
import com.liftric.kvault.KVault
import ru.reaperoq.test_task.datasource.models.LoginRequest
import ru.reaperoq.test_task.datasource.models.Payment
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MainDataSource @Inject constructor(
    private val kVault: KVault,
    private val service: MainService
) {
    companion object {
        private const val TAG = "MainDataSource"
    }

    private val token
        get() = kVault.string("token")

    enum class ErrorCodes(val code: Int) {
        ERROR_CODE_1003(1003),
        ERROR_CODE_OTHER(0),
        ERROR_CODE_NO_INTERNET(-1)
    }

    suspend fun login(login: String, password: String): Result<Boolean> {
        try {
            val response = service.login(LoginRequest(login, password))
            return if (response.isSuccessful) {
                val token = response.body()?.data?.token
                if (token.isNullOrEmpty()) {
                    if (response.body()?.error?.code == 1003) {
                        Result.failure(Throwable(ErrorCodes.ERROR_CODE_1003.name))
                    } else {
                        Result.failure(Throwable(ErrorCodes.ERROR_CODE_OTHER.name))
                    }
                } else {
                    kVault.set("token", token)
                    Result.success(true)
                }
            } else {
                if (response.body()?.error?.code == 1003) {
                    Result.failure(Throwable(ErrorCodes.ERROR_CODE_1003.name))
                } else {
                    Result.failure(Throwable(ErrorCodes.ERROR_CODE_OTHER.name))
                }
            }
        } catch (e: Exception) {
            return Result.failure(Throwable(ErrorCodes.ERROR_CODE_NO_INTERNET.name))
        }
    }

    suspend fun getPayments(): Result<List<Payment>> {
        Log.d(TAG, "getPayments: token = $token")
        try {
            if (token.isNullOrEmpty()) {
                return Result.failure(Throwable("Token is empty"))
            }
            val response = service.getPayments(token!!)
            return if (response.isSuccessful) {
                Result.success(response.body()?.data ?: emptyList())
            } else {
                if (response.body()?.error?.code == 1003) {
                    Result.failure(Throwable(ErrorCodes.ERROR_CODE_1003.name))
                } else {
                    Result.failure(Throwable(ErrorCodes.ERROR_CODE_OTHER.name))
                }
            }
        } catch (e: Exception) {
            Log.d(TAG, "getPayments: ${e.stackTraceToString()}")
            return Result.failure(Throwable(ErrorCodes.ERROR_CODE_NO_INTERNET.name))
        }
    }

    fun logout() {
        kVault.clear()
    }

    fun isLoggedIn(): Boolean {
        return !token.isNullOrEmpty()
    }
}