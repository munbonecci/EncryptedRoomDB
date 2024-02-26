package com.mun.bonecci.encryptedroomdb.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import com.mun.bonecci.encryptedroomdb.R
import com.mun.bonecci.encryptedroomdb.commons.ToolbarButtonVisibilityListener
import com.mun.bonecci.encryptedroomdb.databinding.ActivityMainBinding

/**
 * The main activity responsible for hosting the navigation graph and toolbar.
 */
class MainActivity : AppCompatActivity(), ToolbarButtonVisibilityListener {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private lateinit var fragmentContainer: NavHostFragment
    private lateinit var appBarConfiguration: AppBarConfiguration

    /**
     * Called when the activity is created.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        setNavGraph()
    }

    /**
     * Sets up the navigation graph and fragment container.
     */
    private fun setNavGraph() {
        fragmentContainer =
            supportFragmentManager.findFragmentById(binding.container.id) as NavHostFragment
        navController = fragmentContainer.navController
        addNavigation()
    }

    /**
     * Adds navigation to the navigation controller.
     */
    private fun addNavigation() {
        navController.graph =
            navController.navInflater.inflate(R.navigation.nav_graph)
        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)
    }

    /**
     * Handles the Up button navigation.
     */
    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    /**
     * Called when the visibility of the toolbar button changes.
     */
    override fun onToolbarButtonVisibilityChanged(shouldBeVisible: Boolean) {
        setAddButtonVisibility(shouldBeVisible)
    }

    /**
     * Sets the visibility of the toolbar button.
     */
    private fun setAddButtonVisibility(shouldBeVisible: Boolean) {
        binding.toolbarButton.visibility = if (shouldBeVisible) View.VISIBLE else View.GONE
    }
}
