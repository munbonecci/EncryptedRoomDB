package com.mun.bonecci.encryptedroomdb.commons

/**
 * Interface for notifying changes in the visibility of a toolbar button.
 */
interface ToolbarButtonVisibilityListener {
    /**
     * Called when the visibility of the toolbar button should be changed.
     *
     * @param shouldBeVisible Flag indicating whether the toolbar button should be visible or not.
     */
    fun onToolbarButtonVisibilityChanged(shouldBeVisible: Boolean)
}