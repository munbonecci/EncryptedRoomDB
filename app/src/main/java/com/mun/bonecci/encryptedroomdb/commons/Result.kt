package com.mun.bonecci.encryptedroomdb.commons

/**
 * Sealed class representing the result of an operation, either success or error.
 *
 * @param T The type of data encapsulated by the result.
 */
sealed class Result<out T> {
    /**
     * Represents a successful result containing the data of type T.
     *
     * @property data The data encapsulated by the success result.
     */
    data class Success<out T>(val data: T) : Result<T>()

    /**
     * Represents an error result containing an error message.
     *
     * @property message The error message associated with the error result.
     */
    data class Error(val message: String) : Result<Nothing>()
}