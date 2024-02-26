package com.mun.bonecci.encryptedroomdb.db.cipher

import android.content.Context
import java.util.UUID

/**
 * Utility object for managing a unique identifier stored in SharedPreferences.
 */
object PassPhraseUtils {
    /** The unique identifier stored in memory. */
    private var uniqueID: String? = null

    /** The key used for storing and retrieving the unique identifier from SharedPreferences. */
    private const val PREF_DB_KEY = "PREF_DB_KEY"

    /**
     * Retrieves a unique identifier. If a unique identifier is not stored in memory,
     * it retrieves it from SharedPreferences. If it's not found in SharedPreferences,
     * it generates a new one, stores it in SharedPreferences, and returns it.
     *
     * @param context The application context.
     * @return The unique identifier.
     */
    @Synchronized
    fun getPassphrase(context: Context): String {
        return uniqueID ?: run {
            val sharedPrefs = context.getSharedPreferences(PREF_DB_KEY, Context.MODE_PRIVATE)

            sharedPrefs.getString(PREF_DB_KEY, null)?.also { existingUuid ->
                uniqueID = existingUuid
            } ?: run {
                val newUniqueID = UUID.randomUUID().toString()
                sharedPrefs.edit().apply {
                    putString(PREF_DB_KEY, newUniqueID)
                    apply()
                }
                uniqueID = newUniqueID
                newUniqueID
            }
        }
    }

}