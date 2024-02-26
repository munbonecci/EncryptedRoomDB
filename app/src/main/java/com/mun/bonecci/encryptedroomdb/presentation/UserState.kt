package com.mun.bonecci.encryptedroomdb.presentation

import com.mun.bonecci.encryptedroomdb.data.User

/**
 * Represents the state of user data.
 *
 * @property isLoading Indicates whether data is currently being loaded.
 * @property users The list of users, if available.
 * @property error The error message, if an error occurred.
 */
data class UserState(
    val isLoading: Boolean = false,
    val users: List<User>? = null,
    val error: String = ""
)
