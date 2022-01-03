package crabster.rudakov.sberschoollesson33hwk.ui.settings

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceManager
import crabster.rudakov.sberschoollesson33hwk.R

class SettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.preferencies)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.setBackgroundResource(R.drawable.background_gradient)
    }

    override fun onPreferenceTreeClick(preference: Preference?): Boolean {
        val prefs = PreferenceManager.getDefaultSharedPreferences(requireActivity())
        switchDarkTheme(prefs)
        preference?.let {
            if (it.key == getString(R.string.dark_theme_pref_key)) {
                activity?.recreate()
            }
        }
        return super.onPreferenceTreeClick(preference)
    }

    private fun switchDarkTheme(prefs: SharedPreferences) {
        val isOn = prefs.getBoolean(getString(R.string.dark_theme_pref_key), false)
        val dataPreferences = requireActivity().getSharedPreferences(
            getString(R.string.shared_preferences_storage_name),
            Context.MODE_PRIVATE
        )
        val editor = dataPreferences.edit()
        editor.putBoolean(getString(R.string.dark_theme_status_key), isOn).apply()
    }

}