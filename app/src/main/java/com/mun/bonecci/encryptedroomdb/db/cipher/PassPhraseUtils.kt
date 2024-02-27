package com.mun.bonecci.encryptedroomdb.db.cipher

import android.content.Context
import java.util.UUID
import java.util.concurrent.atomic.AtomicReference

/**
 * Utility class for managing the passphrase used for database encryption.
 */
object PassPhraseUtils {
    private var cachedPassphrase: String? = null
    private const val PASSPHRASE_KEY = "SHARED_PREF_PASSPHRASE_KEY"
    private val atomicPassphrase = AtomicReference<String?>(null)

    /**
     * Retrieves the passphrase used for database encryption. If the passphrase has been
     * previously retrieved, it is cached and returned. Otherwise, a new passphrase is generated
     * and stored in SharedPreferences.
     *
     * @param context The application context.
     * @return The passphrase used for database encryption.
     */
    fun getPassphrase(context: Context): String {
        val cached = cachedPassphrase
        if (cached != null) return cached

        val fromAtomic = atomicPassphrase.get()
        if (fromAtomic != null) return fromAtomic

        return synchronized(this) {
            val fromCacheOrAtomic = cachedPassphrase ?: atomicPassphrase.get()
            if (fromCacheOrAtomic != null) return fromCacheOrAtomic

            val passphrase = readPassphraseFromPreferences(context)
            cachedPassphrase = passphrase
            atomicPassphrase.set(passphrase)
            passphrase
        }
    }

    /**
     * Reads the passphrase from SharedPreferences. If a passphrase does not exist,
     * generates a new passphrase, stores it in SharedPreferences, and returns it.
     *
     * @param context The application context.
     * @return The passphrase retrieved from SharedPreferences or a newly generated passphrase.
     */
    private fun readPassphraseFromPreferences(context: Context): String {
        val sharedPreferences = context.getSharedPreferences(PASSPHRASE_KEY, Context.MODE_PRIVATE)
        return sharedPreferences.getString(PASSPHRASE_KEY, null)?.let {
            it
        } ?: run {
            val newPassphrase = UUID.randomUUID().toString()
            sharedPreferences.edit().apply {
                putString(PASSPHRASE_KEY, newPassphrase)
                apply()
            }
            newPassphrase
        }
    }
}