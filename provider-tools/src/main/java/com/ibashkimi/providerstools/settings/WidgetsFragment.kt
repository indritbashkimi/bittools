package com.ibashkimi.providerstools.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ibashkimi.providerstools.R
import com.ibashkimi.providerstools.data.Gauges
import com.ibashkimi.providerstools.data.WidgetItem

class WidgetsFragment : Fragment() {

    private lateinit var viewModel: ProviderSettingsViewModel

    private val layoutId: String by lazy { arguments!!.getString("layoutId")!! }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = (requireParentFragment() as ProviderSettingsFragment).viewModel

        val root =
            inflater.inflate(R.layout.fragment_widgets, container, false) as ViewGroup

        viewModel.sectionsOf(layoutId).forEach { section ->
            val titleText = LayoutInflater.from(root.context).inflate(
                R.layout.item_section_title, root, false
            ) as TextView
            titleText.text = section.sectionId.mapToTitle()
            root.addView(titleText)

            val recyclerView = LayoutInflater.from(root.context).inflate(
                R.layout.item_section_recycler, root, false
            ) as RecyclerView
            root.addView(recyclerView)

            val displayItems = section.displayStyles.map {
                WidgetItem(it, viewModel.getGaugeLayoutRes(it))
            }
            val selectedDisplay = viewModel.selectedDisplayOf(section)
            val selectedDisplayPosition =
                displayItems.indices.firstOrNull { displayItems[it].id == selectedDisplay } ?: 0
            val adapter =
                WidgetAdapter(recyclerView.context, displayItems, selectedDisplayPosition, 140, 140)
            recyclerView.layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            recyclerView.adapter = adapter
            adapter.listener = WidgetAdapterListener {
                viewModel.onWidgetSelected(section.sectionId, it)
            }
        }

        return root
    }

    private fun String.mapToTitle(): String = context!!.getString(
        when (this) {
            Gauges.PREF_KEY_WIDGET_1 -> R.string.section1_title
            Gauges.PREF_KEY_WIDGET_2 -> R.string.section2_title
            Gauges.PREF_KEY_WIDGET_3 -> R.string.section3_title
            else -> throw IllegalArgumentException()
        }
    )

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