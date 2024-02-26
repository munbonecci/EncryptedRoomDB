package com.mun.bonecci.encryptedroomdb.presentation

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mun.bonecci.encryptedroomdb.data.User
import com.mun.bonecci.encryptedroomdb.data.repository.UserRepositoryImpl
import kotlinx.coroutines.launch
import com.mun.bonecci.encryptedroomdb.commons.Result
import com.mun.bonecci.encryptedroomdb.domain.use_case.GenerateRandomUserUseCase
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

/**
 * ViewModel responsible for managing user data and business logic.
 */
class UserViewModel : ViewModel() {

    private val _usersState = MutableLiveData<UserState>()
    val userState: LiveData<UserState> = _usersState
    var userData: MutableLiveData<User> = MutableLiveData()
    private val generateRandomUserUseCase: GenerateRandomUserUseCase = GenerateRandomUserUseCase()
    private val userRepository = UserRepositoryImpl()

    /**
     * Adds a new user to the database.
     */
    fun addUser() {
        generateRandomUserUseCase.invoke().onEach { result ->
            when (result) {
                is Result.Success -> {
                    val user = result.data
                    insertUserData(user)
                }

                is Result.Error -> {
                    val errorMessage = result.message
                    Log.d("Error: ", errorMessage)
                }
            }
        }.launchIn(viewModelScope)
    }

    private fun insertUserData(user: User) {
        viewModelScope.launch {
            when (val result = userRepository.insertUserToDB(user)) {
                is Result.Success -> {
                    fetchUserData()
                }

                is Result.Error -> {
                    Log.d("Error: ", result.message)
                }
            }
        }
    }

    /**
     * Fetches user data from the database.
     */
    @SuppressLint("NullSafeMutableLiveData")
    fun fetchUserData() {
        viewModelScope.launch {
            when (val result = userRepository.getUserListFromDB()) {
                is Result.Success -> {
                    _usersState.value = UserState(isLoading = false, users = result.data)
                }

                is Result.Error -> {
                    Log.d("Error: ", result.message)
                    _usersState.value = UserState(isLoading = false, error = result.message)
                }
            }
        }
    }

    /**
     * Removes user data from the database.
     *
     * @param id The ID of the user to be removed.
     */
    fun removeUserData(id: String) {
        viewModelScope.launch {
            when (val result = userRepository.deleteUserFromDB(id)) {
                is Result.Success -> {
                    fetchUserData()
                }

                is Result.Error -> {
                    Log.d("Error: ", result.message)
                }
            }
        }
    }

    /**
     * Deletes all user data from the database.
     */
    fun cleanAllUsersData() {
        viewModelScope.launch {
            when (val result = userRepository.deleteAllUsersFromDB()) {
                is Result.Success -> {
                    fetchUserData()
                }

                is Result.Error -> {
                    Log.d("Error: ", result.message)
                }
            }
        }
    }

    /**
     * Retrieves user data by ID from the database.
     *
     * @param id The ID of the user to retrieve.
     */
    @SuppressLint("NullSafeMutableLiveData")
    fun getUserFromId(id: String) {
        viewModelScope.launch {
            when (val result = userRepository.getUserFromDB(id)) {
                is Result.Success -> {
                    userData.value = result.data
                }

                is Result.Error -> {
                    Log.d("Error: ", result.message)
                }
            }
        }
    }

}