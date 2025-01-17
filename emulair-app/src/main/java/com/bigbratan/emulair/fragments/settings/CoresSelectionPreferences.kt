package com.bigbratan.emulair.fragments.settings

import android.content.Context
import com.bigbratan.emulair.ui.CustomListPreference
import androidx.preference.ListPreference
import androidx.preference.Preference
import androidx.preference.PreferenceScreen
import com.bigbratan.emulair.R
import com.bigbratan.emulair.managers.coresLibrary.LibraryIndexScheduler
import com.bigbratan.emulair.common.managers.coresLibrary.CoresSelection
import com.bigbratan.emulair.common.metadata.retrograde.GameSystem

class CoresSelectionPreferences {

    fun addCoresSelectionPreferences(preferenceScreen: PreferenceScreen) {
        val context = preferenceScreen.context

        GameSystem.all()
            .filter { it.systemCoreConfigs.size > 1 }
            .forEach {
                preferenceScreen.addPreference(createPreference(context, it))
            }
    }

    private fun createPreference(context: Context, system: GameSystem): Preference {
        val preference = CustomListPreference(context)
        preference.layoutResource = R.layout.layout_preference_main_list_block_top
        preference.key = CoresSelection.computeSystemPreferenceKey(system.id)
        preference.title = context.getString(system.titleResId)
        preference.isIconSpaceReserved = false
        preference.summaryProvider = ListPreference.SimpleSummaryProvider.getInstance()
        preference.setDefaultValue(system.systemCoreConfigs.map { it.coreID.coreName }.first())
        preference.isEnabled = system.systemCoreConfigs.size > 1

        preference.entries = system.systemCoreConfigs
            .map { it.coreID.coreDisplayName }
            .toTypedArray()

        preference.entryValues = system.systemCoreConfigs
            .map { it.coreID.coreName }
            .toTypedArray()

        preference.setOnPreferenceChangeListener { _, _ ->
            LibraryIndexScheduler.scheduleCoreUpdate(context.applicationContext)
            true
        }

        return preference
    }
}
