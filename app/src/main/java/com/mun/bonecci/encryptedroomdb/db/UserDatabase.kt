package com.mun.bonecci.encryptedroomdb.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.mun.bonecci.encryptedroomdb.data.User

/**
 * Room database class representing the user database.
 */
@Database(entities = [User::class], version = 1, exportSchema = false)
abstract class UserDatabase : RoomDatabase() {
    /**
     * Provides access to the UserDao interface for database operations.
     *
     * @return The UserDao instance.
     */
    abstract fun userDao(): UserDao

    /**
     * Companion object for accessing the database instance.
     */
    companion object {
        // Database instance variable
        private var instance: UserDatabase? = null

        /**
         * Returns the singleton instance of the UserDatabase.
         *
         * @param context The application context.
         * @return The UserDatabase instance.
         */
        @Synchronized
        fun getInstance(context: Context): UserDatabase {
            if (instance == null) {
                // Create database instance if it doesn't exist
                instance = Room.databaseBuilder(
                    context.applicationContext, UserDatabase::class.java,
                    "user_database"
                ).fallbackToDestructiveMigration().build()
            }
            return instance!!
        }
    }
}