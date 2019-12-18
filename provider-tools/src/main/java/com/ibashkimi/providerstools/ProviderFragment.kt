package com.ibashkimi.providerstools

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.ibashkimi.provider.provider.Provider
import com.ibashkimi.providerstools.resolver.*
import com.ibashkimi.providerstools.theme.Gauges
import com.ibashkimi.providerstools.theme.Layouts
import com.ibashkimi.shared.Tool

class ProviderFragment : Fragment() {

    private val args: ProviderFragmentArgs by navArgs()

    private val tool: Tool
        get() = args.tool

    private val viewModel: ProviderViewModel by activityViewModels()

    private lateinit var provider: Provider

    private var listening = true

    private lateinit var preferenceHelper: ToolPreferenceHelper

    private lateinit var actionButton: FloatingActionButton

    private lateinit var map: Map<String, Int>

    private var layout = -1

    private val displays = ArrayList<ProviderDisplay>(3)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        preferenceHelper = tool.helper(requireContext())
        provider = args.tool.getProvider(requireContext(), preferenceHelper)

        when (val layout = preferenceHelper.layout) {
            Layouts.LAYOUT_SIMPLE -> {
                this.layout = R.layout.fragment_simple
                this.map = mapOf(Gauges.PREF_KEY_WIDGET_1 to R.id.container_1)
            }
            Layouts.LAYOUT_NORMAL -> {
                this.layout = R.layout.fragment_normal_layout
                this.map = mapOf(
                    Gauges.PREF_KEY_WIDGET_1 to R.id.container_1,
                    Gauges.PREF_KEY_WIDGET_2 to R.id.container_2
                )
            }
            Layouts.LAYOUT_RICH -> {
                this.layout = R.layout.fragment_rich_layout
                this.map = mapOf(
                    Gauges.PREF_KEY_WIDGET_1 to R.id.container_1,
                    Gauges.PREF_KEY_WIDGET_2 to R.id.container_2,
                    Gauges.PREF_KEY_WIDGET_3 to R.id.container_3
                )
            }
            else -> throw IllegalArgumentException("Unknown layout $layout.")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(this.layout, container, false)

        val toolbar = rootView.findViewById<Toolbar>(R.id.toolbar)
        toolbar.setTitle(tool.title)
        toolbar.setNavigationIcon(R.drawable.ic_back_nav)
        toolbar.setNavigationOnClickListener { requireActivity().onBackPressed() }
        toolbar.inflateMenu(R.menu.provider).also {
            toolbar.menu.findItem(R.id.action_unit).isVisible = tool.allSupportedUnits.size > 1
        }
        toolbar.setOnMenuItemClickListener {
            toolbar.menu.close()
            when (it.itemId) {
                R.id.action_unit -> {
                    val supportedUnits = tool.allSupportedUnits
                    val unit = preferenceHelper.measurementUnit
                    val items: Array<CharSequence> = tool.allSupportedUnits.map { measureUnit ->
                        getString(measureUnit.title)
                    }.toTypedArray()
                    AlertDialog.Builder(requireContext())
                        .setSingleChoiceItems(items, supportedUnits.indexOf(unit)) { dialog, pos ->
                            preferenceHelper.measurementUnit = supportedUnits[pos]
                            dialog.dismiss()
                            requireActivity().recreate()
                        }
                        .setTitle(R.string.change_unit_title)
                        .setNegativeButton(android.R.string.cancel) { dialog, _ ->
                            dialog.dismiss()
                        }
                        .create().show()
                    true
                }
                R.id.action_sampling -> {
                    AlertDialog.Builder(requireContext())
                        .setSingleChoiceItems(
                            R.array.preferences_sensor_rate_human_value,
                            preferenceHelper.providerSamplingRate
                        ) { dialog, pos ->
                            preferenceHelper.providerSamplingRate = pos
                            dialog.dismiss()
                            requireActivity().recreate()
                        }
                        .setTitle(R.string.settings_sampling_rate_title)
                        .setNegativeButton(android.R.string.cancel) { dialog, _ ->
                            dialog.dismiss()
                        }
                        .create().show()
                    true
                }
                R.id.action_settings -> {
                    findNavController().navigate(
                        ProviderFragmentDirections.actionProviderFragmentToProviderSettings(tool)
                    )
                    true
                }
                else -> super.onOptionsItemSelected(it)
            }
        }
        this.actionButton = rootView.findViewById<FloatingActionButton>(R.id.fab).also {
            it.setOnClickListener {
                if (listening) {
                    pauseData()
                    actionButton.setImageResource(R.drawable.ic_play)
                } else {
                    resumeData()
                    actionButton.setImageResource(R.drawable.ic_pause)
                }
            }
        }

        displays.clear()
        for ((widgetPosition, widgetContainer) in map) {
            val layoutRes = preferenceHelper.getWidgetLayout(widgetPosition)
            val root = LayoutInflater.from(requireContext())
                .inflate(layoutRes, rootView.findViewById<View>(widgetContainer) as ViewGroup, true)
            val display = root.findViewById<View>(R.id.display) as ProviderDisplay
            display.setDisplayParams(preferenceHelper.getDisplayParams(requireContext()))
            displays.add(display)
        }

        viewModel.settingsChangedLiveData.observe(viewLifecycleOwner, Observer {
            android.util.Log.d("ProviderFragment", "layoutChanged? : $it, activity: $activity")
            if (it) {
                viewModel.settingsChangedLiveData.value = false
                requireActivity().recreate()
            }
        })

        return rootView
    }

    override fun onResume() {
        super.onResume()
        resumeData()
    }

    override fun onPause() {
        super.onPause()
        pauseData()
    }

    private fun pauseData() {
        listening = false
        for (display in displays)
            provider.unregister(display)
    }

    private fun resumeData() {
        listening = true
        for (display in displays)
            provider.register(display)
    }
}
