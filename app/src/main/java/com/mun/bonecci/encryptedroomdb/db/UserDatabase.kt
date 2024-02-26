package com.mun.bonecci.encryptedroomdb.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.mun.bonecci.encryptedroomdb.data.User
import com.mun.bonecci.encryptedroomdb.db.cipher.PassPhraseUtils
import com.mun.bonecci.encryptedroomdb.db.cipher.SQLCipherUtils
import net.sqlcipher.database.SupportFactory

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
        private const val DATABASE_NAME = "user_database.db"

        /**
         * Singleton pattern to obtain an instance of the encrypted UserDatabase.
         * If the database is unencrypted, it migrates the database to an encrypted one.
         *
         * @param context The application context.
         * @return An instance of the UserDatabase.
         */
        @Synchronized
        fun getInstance(context: Context): UserDatabase {
            // Get the user passphrase and convert it to a byte array
            val userPassphrase = PassPhraseUtils.getPassphrase(context)
            val passphrase = userPassphrase.toByteArray()

            // Check the state of the database encryption
            val state = SQLCipherUtils.getDatabaseState(context, DATABASE_NAME)

            // Create a SupportFactory using the passphrase
            val factory = SupportFactory(passphrase)

            // Migrate the database to an encrypted one if it is currently unencrypted
            if (state == SQLCipherUtils.State.UNENCRYPTED) {
                SQLCipherUtils.migrateToEncryptedDatabase(DATABASE_NAME, context, userPassphrase)
            }

            // Create or retrieve the database instance
            if (instance == null) {
                instance = Room.databaseBuilder(
                    context.applicationContext, UserDatabase::class.java, DATABASE_NAME
                ).openHelperFactory(factory).fallbackToDestructiveMigration().build()
            }

            return instance!!
        }

    }
}