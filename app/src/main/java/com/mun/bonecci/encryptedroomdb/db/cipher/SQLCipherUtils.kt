package com.mun.bonecci.encryptedroomdb.db.cipher

import android.content.Context
import android.util.Log
import net.sqlcipher.database.SQLiteDatabase
import net.sqlcipher.database.SQLiteException
import java.io.File

object SQLCipherUtils {

    /**
     * The detected state of the database, based on whether we can open it
     * without a passphrase.
     */
    enum class State {
        DOES_NOT_EXIST, UNENCRYPTED, ENCRYPTED
    }

    /**
     * Determine whether or not this database appears to be encrypted, based
     * on whether we can open it without a passphrase.
     *
     * @param context a Context
     * @param dbName the name of the database, as used with Room, SQLiteOpenHelper,
     * etc.
     * @return the detected state of the database
     */
    fun getDatabaseState(context: Context, dbName: String?): State {
        SQLiteDatabase.loadLibs(context)
        return getDatabaseState(context.getDatabasePath(dbName))
    }

    /**
     * Determine whether or not this database appears to be encrypted, based
     * on whether we can open it without a passphrase.
     *
     * NOTE: You are responsible for ensuring that net.sqlcipher.database.SQLiteDatabase.loadLibs()
     * is called before calling this method. This is handled automatically with the
     * getDatabaseState() method that takes a Context as a parameter.
     *
     * @param dbPath a File pointing to the database
     * @return the detected state of the database
     */
    private fun getDatabaseState(dbPath: File): State {
        if (!dbPath.exists()) return State.DOES_NOT_EXIST

        return try {
            SQLiteDatabase.openDatabase(
                dbPath.absolutePath, "", null,
                SQLiteDatabase.OPEN_READONLY, null, null
            ).use { db ->
                db.version // Accessing version to check if the database is encrypted
                State.UNENCRYPTED
            }
        } catch (e: SQLiteException) {
            State.ENCRYPTED
        }
    }

    /**
     * Migrates an existing SQLite database to an encrypted SQLite database using SQLCipher.
     * The original unencrypted database is replaced with the encrypted one.
     *
     * @param dataBaseName The name of the existing SQLite database.
     * @param context The application context.
     * @param password The password used to encrypt the database.
     * @throws SQLiteException If an error occurs during database migration.
     */
    fun migrateToEncryptedDatabase(
        dataBaseName: String, context: Context, password: String
    ) {
        // Obtain the paths for the original and temporary databases
        val databasePath = context.getDatabasePath(dataBaseName).path
        val temporaryDatabasePath = context.getDatabasePath("${dataBaseName}temp").absolutePath
        val originalFile = File(databasePath)
        // Check if the original database file exists
        if (originalFile.exists()) {
            // Create a reference to the temporary database file
            val newFile = File(temporaryDatabasePath)
            // Open the original database and execute SQL commands to encrypt it
            var database = getCurrentSqliteDatabase(databasePath)
            runCatching {
                database.rawExecSQL("ATTACH DATABASE '$temporaryDatabasePath' AS encrypted KEY '$password';")
                database.rawExecSQL("SELECT sqlcipher_export('encrypted')")
                database.rawExecSQL("DETACH DATABASE encrypted;")
                // Retrieve the database version
                val version = database.version
                // Close the database connection
                database.close()
                // Open the encrypted database and set its version
                database = SQLiteDatabase.openDatabase(
                    temporaryDatabasePath, password, null, SQLiteDatabase.OPEN_READWRITE
                )
                database.version = version
                // Close the database connection
                database.close()
                // Delete the original unencrypted database file
                originalFile.delete()
                // Rename the temporary file to the original file name
                newFile.renameTo(originalFile)
            }.onFailure {
                Log.e("DatabaseMigration", "Error migrating database", it)
            }
        }
    }

    /**
     * Retrieves the SQLiteDatabase object for the specified database path with optional encryption.
     *
     * @param databasePath The path to the SQLite database file.
     * @param password The password used for encryption (optional).
     * @return An instance of SQLiteDatabase.
     * @throws SQLiteException If there is an error opening the database.
     */
    @Throws(SQLiteException::class)
    private fun getCurrentSqliteDatabase(
        databasePath: String,
        password: String? = null
    ): SQLiteDatabase {
        // Set the passphrase to an empty string if not provided
        val passPhrase = password ?: ""
        // Open the SQLiteDatabase with the specified parameters
        return SQLiteDatabase.openDatabase(
            databasePath,
            passPhrase,
            null,
            SQLiteDatabase.CREATE_IF_NECESSARY
        )
    }

}