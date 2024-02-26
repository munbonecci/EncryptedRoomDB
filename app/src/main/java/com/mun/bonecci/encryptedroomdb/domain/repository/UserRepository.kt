package com.mun.bonecci.encryptedroomdb.domain.repository

import com.mun.bonecci.encryptedroomdb.data.User
import com.mun.bonecci.encryptedroomdb.commons.Result


/**
 * Interface defining repository operations for user data.
 */
interface UserRepository {
    /**
     * Retrieves a list of users from the local database.
     *
     * @return A result indicating success or failure, along with the list of users if successful.
     */
    suspend fun getUserListFromDB(): Result<List<User>>

    /**
     * Retrieves a user from the local database by its ID.
     *
     * @param id The ID of the user to retrieve.
     * @return A result indicating success or failure, along with the user if found.
     */
    suspend fun getUserFromDB(id: String): Result<User>

    /**
     * Inserts a new user into the local database.
     *
     * @param user The user to insert.
     * @return A result indicating success or failure, along with the ID of the inserted user if successful.
     */
    suspend fun insertUserToDB(user: User): Result<Long>

    /**
     * Deletes a user from the local database by its ID.
     *
     * @param id The ID of the user to delete.
     * @return A result indicating success or failure, along with the number of rows affected by the deletion operation.
     */
    suspend fun deleteUserFromDB(id: String): Result<Int>

    /**
     * Deletes all users from the local database.
     *
     * @return A result indicating success or failure.
     */
    suspend fun deleteAllUsersFromDB(): Result<Unit>
}