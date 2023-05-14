package com.ibashkimi.bittools.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ibashkimi.bittools.R
import com.ibashkimi.theme.preference.ThemeAdapter
import com.ibashkimi.theme.theme.Theme

class ThemeFragment : Fragment(), ThemeAdapter.ThemeSelectedListener {

    private lateinit var themeAdapter: ThemeAdapter

    private val viewModel: ThemeViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_theme, container, false)

        root.findViewById<Toolbar>(R.id.toolbar).apply {
            setTitle(R.string.title_change_theme)
            setNavigationIcon(R.drawable.ic_cancel_toolbar)
            setNavigationOnClickListener { findNavController().navigateUp() }
        }

        root.findViewById<RecyclerView>(R.id.recyclerView).apply {
            layoutManager = GridLayoutManager(
                context,
                resources.getInteger(com.ibashkimi.theme.R.integer.theme_columns)
            )
            themeAdapter = ThemeAdapter(context, emptyList(), -1, { false }, this@ThemeFragment)
            adapter = themeAdapter
        }
        viewModel.themes(root.context).observe(viewLifecycleOwner) {
            themeAdapter.themes = it.first
            themeAdapter.selectedIndex = it.second
            themeAdapter.notifyDataSetChanged()
        }

        return root
    }

    override fun onThemeSelected(theme: Theme) {
        viewModel.setTheme(theme)
    }
}
