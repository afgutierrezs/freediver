package com.android.freediver.ui.home

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.android.freediver.R
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        setupBottonNavigationBar()
    }

    fun setupBottonNavigationBar() {

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        val navigationController = navHostFragment.navController
        val appBarConfiguration = AppBarConfiguration(setOf(R.id.trainingFragment, R.id.discoverFragment, R.id.profileFragment))

        setupActionBarWithNavController(navigationController, appBarConfiguration)
        bottomNavigationView.setupWithNavController(navigationController)
    }
}
