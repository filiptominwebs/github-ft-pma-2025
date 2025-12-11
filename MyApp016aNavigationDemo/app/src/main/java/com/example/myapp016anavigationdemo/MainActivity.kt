package com.example.myapp016anavigationdemo

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        val navHost = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHost.navController

        // Najdeme toolbar
        val toolbar = findViewById<MaterialToolbar>(R.id.toolbar)

        // Nastavíme ho jako ActionBar
        setSupportActionBar(toolbar)

        // DrawerLayout pro hamburger toggle
        val drawerLayout = findViewById<DrawerLayout>(R.id.drawer_layout)

        // Horní-level destinace: ty, kde chceme zobrazit hamburger místo back tlačítka
        appBarConfiguration = AppBarConfiguration(
            setOf(R.id.homeFragment, R.id.profileFragment, R.id.settingsFragment, R.id.aboutFragment),
            drawerLayout
        )

        // Až teď může NavigationUI pracovat s ActionBarem
        setupActionBarWithNavController(navController, appBarConfiguration)

        findViewById<BottomNavigationView>(R.id.bottomNav)
            .setupWithNavController(navController)

        findViewById<NavigationView>(R.id.drawerNav)
            .setupWithNavController(navController)
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}
