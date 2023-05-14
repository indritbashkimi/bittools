package com.ibashkimi.providerstools.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ibashkimi.providerstools.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProviderSettingsFragment : Fragment() {

    val viewModel: ProviderSettingsViewModel by viewModels()

    private lateinit var adapter: WidgetAdapter

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

        val recyclerView = root.findViewById<RecyclerView>(R.id.layout_recycler_view)
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

        val layouts = viewModel.layouts
        adapter = WidgetAdapter(
            requireContext(),
            layouts.layout,
            layouts.selectedIndex,
            100,
            160
        )
        adapter.listener = WidgetAdapterListener { viewModel.onLayoutSelected(it) }
        recyclerView.adapter = adapter

        viewModel.layoutLiveData.observe(viewLifecycleOwner) {
            childFragmentManager.beginTransaction()
                .replace(
                    R.id.sections_container,
                    WidgetsFragment.newInstance(it.id), it.id
                )
                .commit()
        }

        return root
    }
}