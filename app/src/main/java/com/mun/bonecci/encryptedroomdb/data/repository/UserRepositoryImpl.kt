package com.mun.bonecci.encryptedroomdb.data.repository

import com.mun.bonecci.encryptedroomdb.data.User
import com.mun.bonecci.encryptedroomdb.db.UserDao
import com.mun.bonecci.encryptedroomdb.db.UserDatabase
import com.mun.bonecci.encryptedroomdb.domain.repository.UserRepository
import com.mun.bonecci.encryptedroomdb.commons.Result
import com.mun.bonecci.encryptedroomdb.core.Application

/**
 * Implementation of the [UserRepository] interface.
 */
class UserRepositoryImpl : UserRepository {

    /**
     * The user database instance.
     */
    private var userDatabase: UserDao =
        UserDatabase.getInstance(Application.instance!!.applicationContext).userDao()

    /**
     * Retrieves the list of users from the database.
     *
     * @return A [Result] containing the list of users if successful, or an error message otherwise.
     */
    override suspend fun getUserListFromDB(): Result<List<User>> {
        runCatching {
            val userList = userDatabase.getUsers()
            return Result.Success(userList)
        }.getOrElse {
            return Result.Error("Error fetching users in database")
        }
    }

    /**
     * Retrieves a user from the database based on the provided ID.
     *
     * @param id The ID of the user to retrieve.
     * @return A [Result] containing the user if found, or an error message otherwise.
     */
    override suspend fun getUserFromDB(id: String): Result<User> {
        runCatching {
            val user = userDatabase.getUserFromId(id)
            return Result.Success(user)
        }.getOrElse {
            return Result.Error("Error getting user in database")
        }
    }

    /**
     * Inserts a user into the database.
     *
     * @param user The user to insert.
     * @return A [Result] containing the ID of the inserted user if successful, or an error message otherwise.
     */
    override suspend fun insertUserToDB(user: User): Result<Long> {
        runCatching {
            val userResult = userDatabase.insertUser(user)
            return Result.Success(userResult)
        }.getOrElse {
            return Result.Error("Error inserting user in database")
        }
    }

    /**
     * Deletes a user from the database based on the provided ID.
     *
     * @param id The ID of the user to delete.
     * @return A [Result] containing the number of rows affected if successful, or an error message otherwise.
     */
    override suspend fun deleteUserFromDB(id: String): Result<Int> {
        runCatching {
            val user = userDatabase.deleteUser(id)
            return Result.Success(user)
        }.getOrElse {
            return Result.Error("Error deleting user in database")
        }
    }

    /**
     * Deletes all users from the database.
     *
     * @return A [Result] indicating success or failure.
     */
    override suspend fun deleteAllUsersFromDB(): Result<Unit> {
        runCatching {
            val user = userDatabase.deleteAll()
            return Result.Success(user)
        }.getOrElse {
            return Result.Error("Error deleting all users in database")
        }
    }
}