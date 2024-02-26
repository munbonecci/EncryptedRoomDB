package com.mun.bonecci.encryptedroomdb.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mun.bonecci.encryptedroomdb.data.User

/**
 * Data Access Object (DAO) interface for user-related database operations.
 */
@Dao
interface UserDao {
    /**
     * Inserts a user into the database.
     *
     * @param user The user to be inserted.
     * @return The ID of the inserted user.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: User): Long

    /**
     * Retrieves all users from the database.
     *
     * @return A list of users stored in the database.
     */
    @Query("SELECT * FROM user")
    suspend fun getUsers(): List<User>

    /**
     * Retrieves a user from the database by ID.
     *
     * @param id The ID of the user to retrieve.
     * @return The user with the specified ID.
     */
    @Query("SELECT * FROM user WHERE id = :id")
    suspend fun getUserFromId(id: String): User

    /**
     * Deletes a user from the database by ID.
     *
     * @param id The ID of the user to delete.
     * @return The number of users deleted (should be 1 if successful).
     */
    @Query("DELETE FROM user WHERE id = :id")
    suspend fun deleteUser(id: String): Int

    /**
     * Deletes all users from the database.
     */
    @Query("DELETE FROM user")
    suspend fun deleteAll()
}