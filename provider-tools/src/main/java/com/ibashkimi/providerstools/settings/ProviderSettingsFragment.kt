package com.ibashkimi.providerstools.settings

import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import com.ibashkimi.providerstools.ProviderViewModel
import com.ibashkimi.providerstools.R
import com.ibashkimi.providerstools.resolver.PreferencesResolver
import com.ibashkimi.providerstools.theme.*

class ProviderSettingsFragment : Fragment() {

    val preferences: SharedPreferences by lazy {
        PreferencesResolver.resolvePreferences(requireContext(), args.tool)
    }

    private val args: ProviderSettingsFragmentArgs by navArgs()

    private val layoutChangedViewModel: ProviderViewModel by activityViewModels()

    private val widgetProvider: WidgetProvider by lazy { WidgetProvider(args.tool) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_provider_settings, container, false)

        root.findViewById<Toolbar>(R.id.toolbar).apply {
            setTitle(R.string.action_personalize) // args.tool.title
            setNavigationIcon(R.drawable.ic_back_nav)
            setNavigationOnClickListener { findNavController().navigateUp() }
        }

        childFragmentManager.beginTransaction()
            .replace(
                R.id.container,
                LayoutSelectorFragment.newInstance(args.tool),
                FRAGMENT_TAG_LAYOUT
            )
            .commit()

        return root
    }

    fun onLayoutSelected(item: WidgetItem, save: Boolean = true) {
        if (save) {
            preferences.edit().putString("layout", item.id).apply()
            notifyLayoutChanged()
        }
        childFragmentManager.beginTransaction()
            .replace(
                R.id.sections_container,
                WidgetsFragment.newInstance(item.id), item.id
            )
            .commit()
    }

    fun onWidgetSelected(sectionId: String, displayStyle: WidgetItem, save: Boolean = true) {
        if (save) {
            preferences.edit().putString(sectionId, displayStyle.id).apply()
            notifyLayoutChanged()
        }
    }

    fun sectionsOf(layoutId: String): Array<Section> {
        return widgetProvider.getSections(layoutId)
    }

    private fun notifyLayoutChanged() {
        layoutChangedViewModel.settingsChangedLiveData.value = true
    }

    companion object {
        const val FRAGMENT_TAG_LAYOUT = "frag_layout"
    }
}