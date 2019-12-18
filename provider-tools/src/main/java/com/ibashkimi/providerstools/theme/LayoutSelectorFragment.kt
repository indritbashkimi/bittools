package com.ibashkimi.providerstools.theme

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ibashkimi.providerstools.R
import com.ibashkimi.providerstools.settings.ProviderSettingsFragment
import com.ibashkimi.shared.Tool


class LayoutSelectorFragment : Fragment(), Adapter.ThemeAdapterListener {

    private val tool: Tool by lazy { arguments!!.getParcelable<Tool>(ARG_TOOL)!! }

    private lateinit var adapter: Adapter

    private val widgetProvider: WidgetProvider by lazy { WidgetProvider(tool) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_theme_selector, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val recyclerView = view.findViewById<RecyclerView>(R.id.layout_recycler_view)
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

        val items: List<WidgetItem> = widgetProvider.layouts
        val preferences = (parentFragment as ProviderSettingsFragment).preferences
        val selectedLayout = preferences.getString("layout", Layouts.LAYOUT_SIMPLE)
        val layoutPosition = items.indices.firstOrNull { items[it].id == selectedLayout } ?: 0

        adapter = Adapter(requireContext(), items, layoutPosition, 100, 160)
        adapter.listener = this
        recyclerView.adapter = adapter
        onThemeItemSelected(items[layoutPosition], false)
    }

    override fun onThemeItemSelected(item: WidgetItem, save: Boolean) {
        (parentFragment as ProviderSettingsFragment).onLayoutSelected(item, save)
    }

    companion object {

        private const val ARG_TOOL = "arg_tool"

        fun newInstance(tool: Tool): LayoutSelectorFragment {
            val fragment = LayoutSelectorFragment()
            val args = Bundle()
            args.putParcelable(ARG_TOOL, tool)
            fragment.arguments = args
            return fragment
        }
    }
}