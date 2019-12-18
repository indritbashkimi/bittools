package com.ibashkimi.providerstools.theme

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ibashkimi.providerstools.R
import com.ibashkimi.providerstools.settings.ProviderSettingsFragment

class WidgetsFragment : Fragment() {

    private val layoutId: String by lazy { arguments!!.getString("layoutId")!! }

    private val parent: ProviderSettingsFragment by lazy {
        parentFragment as ProviderSettingsFragment?
            ?: throw IllegalStateException("Parent must be ProviderSettingsFragment.")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_layout_sections, container, false)

        val isPremium = arguments!!.getBoolean("isPremium")
        val preferences = parent.preferences
        val sections = parent.sectionsOf(layoutId)

        if (sections.size > 3) throw IllegalStateException("Cannot handle more than 3 sections.")

        if (sections.isNotEmpty()) {
            val sectionId = sections[0].sectionId
            val section1Title = rootView.findViewById<TextView>(R.id.section1Title)
            section1Title.text = sectionId.mapToTitle()
            val displayItems = sections[0].displayStyles.map { WidgetItem(it, resolveLayout(it)) }
            val selectedDisplay =
                preferences.getString(Gauges.PREF_KEY_WIDGET_1, sections[0].displayStyles[0])
            val selectedDisplayPosition =
                displayItems.indices.firstOrNull { displayItems[it].id == selectedDisplay }
                    ?: 0
            val adapter1 = Adapter(context!!, displayItems, selectedDisplayPosition, 160, 160)
            val section1RecyclerView =
                rootView.findViewById<RecyclerView>(R.id.section1RecyclerView)
            section1RecyclerView.layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            section1RecyclerView.adapter = adapter1
            adapter1.listener = object : Adapter.ThemeAdapterListener {
                override fun onThemeItemSelected(item: WidgetItem, save: Boolean) {
                    parent.onWidgetSelected(sectionId, item, save)
                }
            }
        } else
            throw IllegalStateException("sections has 0 size.")

        if (sections.size >= 2) {
            val sectionId = sections[1].sectionId
            val section2Title = rootView.findViewById<TextView>(R.id.section2Title)
            section2Title.visibility = View.VISIBLE
            section2Title.text = sectionId.mapToTitle()
            val displayItems = sections[1].displayStyles.map { WidgetItem(it, resolveLayout(it)) }
            val selectedDisplay =
                preferences.getString(Gauges.PREF_KEY_WIDGET_2, sections[1].displayStyles[0])
            val selectedDisplayPosition =
                displayItems.indices.firstOrNull { displayItems[it].id == selectedDisplay }
                    ?: 0
            val adapter2 = Adapter(context!!, displayItems, selectedDisplayPosition, 160, 160)
            val section2RecyclerView =
                rootView.findViewById<RecyclerView>(R.id.section2RecyclerView)
            section2RecyclerView.layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            section2RecyclerView.adapter = adapter2
            adapter2.listener = object : Adapter.ThemeAdapterListener {
                override fun onThemeItemSelected(item: WidgetItem, save: Boolean) {
                    parent.onWidgetSelected(sectionId, item, save)
                }
            }
        }

        if (sections.size >= 3) {
            val sectionId = sections[2].sectionId
            val section2Title = rootView.findViewById<TextView>(R.id.section3Title)
            section2Title.visibility = View.VISIBLE
            section2Title.text = sectionId.mapToTitle()
            val displayItems = sections[2].displayStyles.map { WidgetItem(it, resolveLayout(it)) }
            val selectedDisplay =
                preferences.getString(Gauges.PREF_KEY_WIDGET_3, sections[2].displayStyles[0])
            val selectedDisplayPosition =
                displayItems.indices.firstOrNull { displayItems[it].id == selectedDisplay }
                    ?: 0
            val adapter3 = Adapter(context!!, displayItems, selectedDisplayPosition, 160, 160)
            val section3RecyclerView =
                rootView.findViewById<RecyclerView>(R.id.section3RecyclerView)
            section3RecyclerView.layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            section3RecyclerView.adapter = adapter3
            adapter3.listener = object : Adapter.ThemeAdapterListener {
                override fun onThemeItemSelected(item: WidgetItem, save: Boolean) {
                    parent.onWidgetSelected(sectionId, item, save)
                }
            }
        }
        return rootView
    }

    private fun String.mapToTitle(): String = context!!.getString(
        when (this) {
            Gauges.PREF_KEY_WIDGET_1 -> R.string.section1_title
            Gauges.PREF_KEY_WIDGET_2 -> R.string.section2_title
            Gauges.PREF_KEY_WIDGET_3 -> R.string.section3_title
            else -> throw IllegalArgumentException()
        }
    )

    @LayoutRes
    private fun resolveLayout(displayStyle: String): Int {
        return Gauges.previewStyle(displayStyle)
    }

    companion object {
        fun newInstance(layoutId: String): WidgetsFragment {
            val fragment = WidgetsFragment()
            val args = Bundle()
            args.putString("layoutId", layoutId)
            fragment.arguments = args
            return fragment
        }
    }
}