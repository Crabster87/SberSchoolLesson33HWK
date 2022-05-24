package crabster.rudakov.sberschoollesson33hwk

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setColorTheme()
        setContentView(R.layout.activity_main)

        val navView: BottomNavigationView = findViewById(R.id.navView)
        val navController = findNavController(R.id.navigation_host_fragment)
        navView.setupWithNavController(navController)
    }

    private fun setColorTheme() {
        val dataPreferences =
            getSharedPreferences(getString(R.string.shared_preferences_storage_name), MODE_PRIVATE)
        val isDarkThemeOn =
            dataPreferences.getBoolean(getString(R.string.dark_theme_status_key), false)
        if (isDarkThemeOn) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
    }

}