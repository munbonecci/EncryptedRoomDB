package com.mun.bonecci.encryptedroomdb.domain.use_case

import kotlin.random.Random
import com.mun.bonecci.encryptedroomdb.commons.Result
import com.mun.bonecci.encryptedroomdb.data.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow

/**
 * Use case for generating a random user.
 */
class GenerateRandomUserUseCase {
    /**
     * Invokes the use case to generate a random user.
     *
     * @return A flow emitting the result of generating a random user.
     */
    operator fun invoke(): Flow<Result<User>> = flow {
        try {
            val userRandomId = Random.nextInt(0, 7)
            val user = fakeUserList.getOrNull(userRandomId) ?: User(
                name = "Generic",
                email = "generic@email"
            )
            emit(Result.Success(user))
        } catch (e: Exception) {
            emit(Result.Error(message = e.message ?: GENERIC_ERROR))
        }
    }.catch { cause ->
        emit(
            Result.Error(
                message = cause.localizedMessage ?: GENERIC_ERROR
            )
        )
    }

    companion object {
        const val GENERIC_ERROR = "GENERIC_ERROR"
    }
}

/**
 * A list of fake users for generating random users.
 */
private val fakeUserList: List<User> = mutableListOf<User>().apply {
    add(User(name = "A", email = "@gmail.com"))
    add(User(name = "B", email = "@outlook.com"))
    add(User(name = "C", email = "@hotmail.com"))
    add(User(name = "D", email = "@outlook.com"))
    add(User(name = "E", email = "@gmail.com"))
    add(User(name = "F", email = "@hotmail.com"))
    add(User(name = "G", email = "@gmail.com"))
}