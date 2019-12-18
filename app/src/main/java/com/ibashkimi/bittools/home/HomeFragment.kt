package com.ibashkimi.bittools.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.ibashkimi.bittools.R
import com.ibashkimi.shared.Tool

class HomeFragment : Fragment(), ItemAdapter.ClickListener {

    private val viewModel: HomeViewModel by viewModels()

    private lateinit var adapter: ItemAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_main, container, false)

        val toolbar = rootView.findViewById<Toolbar>(R.id.toolbar)
        toolbar.setTitle(R.string.app_name)
        toolbar.inflateMenu(R.menu.fragment_home)
        toolbar.setOnMenuItemClickListener {
            toolbar.menu.close()
            NavigationUI.onNavDestinationSelected(it, findNavController())
                    || super.onOptionsItemSelected(it)
        }

        val recyclerView = rootView.findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.setHasFixedSize(true)
        val itemLayout: Int = R.layout.tool_item_list
        val layoutManager = LinearLayoutManager(recyclerView.context)
        recyclerView.addItemDecoration(
            DividerItemDecoration(recyclerView.context, layoutManager.orientation)
        )

        recyclerView.layoutManager = layoutManager
        adapter = ItemAdapter(emptyList(), emptyList(), itemLayout, this)
        recyclerView.adapter = adapter
        viewModel.tools.observe(viewLifecycleOwner, Observer {
            adapter.tools = it.first
            adapter.unsupportedTools = it.second
            adapter.notifyDataSetChanged()
        })

        return rootView
    }

    override fun onItemClick(item: Item, isLongClick: Boolean) {
        findNavController().navigate(
            when (item.tool) {
                Tool.PROTRACTOR -> HomeFragmentDirections.actionHomeToProtractor()
                Tool.RULER -> HomeFragmentDirections.actionHomeToRuler()
                else -> HomeFragmentDirections.actionHomeToProviderTool(item.tool)
            }
        )
    }

    override fun onHideClicked() {
        viewModel.hideUnsupportedTools()
        view?.let {
            Snackbar.make(it, R.string.unsupported_tools_hidden_msg, Snackbar.LENGTH_LONG).show()
        }
    }
}
