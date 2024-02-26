package com.mun.bonecci.encryptedroomdb.commons

import android.os.Bundle
import androidx.annotation.IdRes
import androidx.navigation.NavController

/**
 * Safe navigation extension function for NavController.
 * Navigates to the specified action with the given bundle, handling any potential exceptions.
 *
 * @param action The ID of the action to navigate to.
 * @param bundle The bundle to be passed with the navigation action.
 * @return true if navigation was successful, false otherwise.
 */
fun NavController.safeNavigate(@IdRes action: Int, bundle: Bundle): Boolean =
    try {
        navigate(action, bundle)
        true
    } catch (t: Throwable) {
        false
    }
