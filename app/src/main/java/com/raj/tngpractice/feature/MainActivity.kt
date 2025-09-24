package com.raj.tngpractice.feature

import android.os.Bundle
import android.view.View
import android.view.ViewGroup.MarginLayoutParams
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updateLayoutParams
import androidx.core.view.updatePadding
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.raj.tngpractice.R
import com.raj.tngpractice.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var mainBinding: ActivityMainBinding
    private lateinit var navController : NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        mainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mainBinding.root)
        setupNavigation()
    }

    private fun setupNavigation() {
        navController = (supportFragmentManager.findFragmentById(R.id.navHostFragment) as NavHostFragment).navController
        navController.apply {
            setGraph(R.navigation.nav_graph)
            addOnDestinationChangedListener { _, destination, _ ->
                /* in case each fragment have different bottom insets */
                handleNavigationInsets(destination.id == R.id.usersFragment)
            }
        }
    }

    private fun handleNavigationInsets(isManualHandling : Boolean) {
        ViewCompat.setOnApplyWindowInsetsListener(mainBinding.navHostFragment) { v: View, windowInsets: WindowInsetsCompat ->
            val insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars() or WindowInsetsCompat.Type.displayCutout() or WindowInsetsCompat.Type.ime())
            v.updateLayoutParams<MarginLayoutParams> { topMargin = insets.top }
            v.updatePadding(0, 0, 0, if (isManualHandling) 0 else insets.bottom)
            windowInsets
        }
    }

}