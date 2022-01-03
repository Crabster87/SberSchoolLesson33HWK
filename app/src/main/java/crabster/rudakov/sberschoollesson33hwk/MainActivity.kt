package crabster.rudakov.sberschoollesson33hwk

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import crabster.rudakov.sberschoollesson33hwk.ui.weather.WeatherFragmentSharedViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var weatherFragmentSharedViewModel: WeatherFragmentSharedViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        weatherFragmentSharedViewModel =
            ViewModelProvider(this)[WeatherFragmentSharedViewModel::class.java]
        setColorTheme()
        setContentView(R.layout.activity_main)

        val navView: BottomNavigationView = findViewById(R.id.navView)
        val navController = findNavController(R.id.navigation_host_fragment)
        navView.setupWithNavController(navController)
    }

    private fun setColorTheme() {
        val dataPreferences = getSharedPreferences(getString(R.string.shared_preferences_storage_name), MODE_PRIVATE)
        val isDarkThemeOn = dataPreferences.getBoolean(getString(R.string.dark_theme_status_key), false)
        if (isDarkThemeOn) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
    }

}