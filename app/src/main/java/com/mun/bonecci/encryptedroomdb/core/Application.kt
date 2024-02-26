package com.mun.bonecci.encryptedroomdb.core

import android.app.Application

/**
 * Custom application class.
 */
class Application : Application() {
    companion object {
        /**
         * Singleton instance of the application.
         */
        var instance: Application? = null
            private set
    }

    /**
     * Called when the application is starting.
     */
    override fun onCreate() {
        super.onCreate()
        instance = this
    }
}