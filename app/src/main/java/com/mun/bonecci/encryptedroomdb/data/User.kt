package com.mun.bonecci.encryptedroomdb.data

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Represents a user entity in the database.
 *
 * @property id The unique identifier for the user. Automatically generated.
 * @property name The name of the user.
 * @property email The email address of the user.
 */
@Entity(tableName = "user")
data class User(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val email: String,
)