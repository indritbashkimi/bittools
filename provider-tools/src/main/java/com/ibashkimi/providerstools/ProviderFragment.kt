package com.ibashkimi.providerstools

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.appcompat.widget.Toolbar
import androidx.core.view.updateLayoutParams
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.ibashkimi.provider.providerdata.SensorData
import com.ibashkimi.providerstools.data.Gauges
import com.ibashkimi.providerstools.data.Layouts
import com.ibashkimi.providerstools.data.ProviderDisplay
import com.ibashkimi.providerstools.data.ToolPreferenceHelper
import com.ibashkimi.providerstools.data.allSupportedUnits
import com.ibashkimi.providerstools.data.helper
import com.ibashkimi.providerstools.data.title
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProviderFragment : Fragment() {

    private val args: ProviderFragmentArgs by navArgs()

    private val viewModel: ProviderViewModel by viewModels()

    private lateinit var preferenceHelper: ToolPreferenceHelper

    private val displays = ArrayList<ProviderDisplay>(3)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        preferenceHelper = args.tool.helper(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val layoutType = preferenceHelper.layout
        val layoutRes = when (layoutType) {
            Layouts.LAYOUT_SIMPLE -> R.layout.fragment_simple
            Layouts.LAYOUT_NORMAL -> R.layout.fragment_normal_layout
            Layouts.LAYOUT_RICH -> R.layout.fragment_rich_layout
            else -> throw IllegalArgumentException("Unknown layout $layoutType.")
        }
        val rootView = inflater.inflate(layoutRes, container, false)
        setUpToolbar(rootView)
        displays.addAll(setUpLayout(rootView, layoutType))
        setUpFab(rootView)

        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel.measurementUnit.observe(viewLifecycleOwner) {
            setUpDisplayParams()
        }
        viewModel.setTool(args.tool, preferenceHelper.measurementUnit)
        viewModel.sensorData.observe(viewLifecycleOwner) {
            onDataChanged(it)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        displays.clear()
    }

    private fun setUpLayout(rootView: View, layoutType: String): List<ProviderDisplay> {
        return when (layoutType) {
            Layouts.LAYOUT_SIMPLE -> {
                val display1Container = rootView.findViewById<View>(R.id.container_1) as ViewGroup
                val display1Layout = preferenceHelper.getWidgetLayout(Gauges.PREF_KEY_WIDGET_1)
                val display1 = LayoutInflater.from(requireContext())
                    .inflate(display1Layout, display1Container, false)
                display1Container.addView(display1)
                listOf(display1 as ProviderDisplay)
            }

            Layouts.LAYOUT_NORMAL -> {
                val display1Container = rootView.findViewById<View>(R.id.container_1) as ViewGroup
                val display1Layout = preferenceHelper.getWidgetLayout(Gauges.PREF_KEY_WIDGET_1)
                val display1 = LayoutInflater.from(requireContext())
                    .inflate(display1Layout, display1Container, false)
                display1Container.addView(display1)

                val appbar = rootView.findViewById<AppBarLayout>(R.id.appbar)
                val display2 = LayoutInflater.from(appbar.context)
                    .inflate(
                        preferenceHelper.getWidgetLayout(Gauges.PREF_KEY_WIDGET_2),
                        appbar,
                        false
                    )
                appbar.addView(display2)

                listOf(display1 as ProviderDisplay, display2 as ProviderDisplay)
            }

            Layouts.LAYOUT_RICH -> {
                val display1Container = rootView.findViewById<View>(R.id.container_1) as ViewGroup
                val display1Layout = preferenceHelper.getWidgetLayout(Gauges.PREF_KEY_WIDGET_1)
                val display1 = LayoutInflater.from(requireContext())
                    .inflate(display1Layout, display1Container, false)
                display1Container.addView(display1)

                val appbar = rootView.findViewById<AppBarLayout>(R.id.appbar)
                val appBarDisplayLayout = LinearLayout(appbar.context)
                appBarDisplayLayout.orientation = LinearLayout.HORIZONTAL
                val display2 = LayoutInflater.from(appBarDisplayLayout.context).inflate(
                    preferenceHelper.getWidgetLayout(Gauges.PREF_KEY_WIDGET_2),
                    appBarDisplayLayout,
                    false
                )
                val display3 = LayoutInflater.from(appbar.context).inflate(
                    preferenceHelper.getWidgetLayout(Gauges.PREF_KEY_WIDGET_3),
                    appBarDisplayLayout,
                    false
                )
                display2.updateLayoutParams<LinearLayout.LayoutParams> {
                    width = 0
                    weight = 0.5f
                }
                display3.updateLayoutParams<LinearLayout.LayoutParams> {
                    width = 0
                    weight = 0.5f
                }
                appBarDisplayLayout.addView(display2)
                appBarDisplayLayout.addView(display3)
                appbar.addView(appBarDisplayLayout)
                appBarDisplayLayout.updateLayoutParams<LinearLayout.LayoutParams> {
                    width = LinearLayout.LayoutParams.MATCH_PARENT
                    height = LinearLayout.LayoutParams.WRAP_CONTENT
                }

                listOf(
                    display1 as ProviderDisplay,
                    display2 as ProviderDisplay,
                    display3 as ProviderDisplay
                )
            }

            else -> throw IllegalArgumentException("Unknown layout $layoutType.")
        }
    }

    private fun setUpToolbar(rootView: View) {
        val tool = args.tool
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
                    showMeasurementUnitOptions()
                    true
                }

                R.id.action_sampling -> {
                    showSamplingRateOptions()
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
    }

    private fun setUpFab(rootView: View) {
        rootView.findViewById<FloatingActionButton>(R.id.fab).also { fab ->
            fab.setOnClickListener {
                if (viewModel.isListening) {
                    viewModel.pause()
                    fab.setImageResource(R.drawable.ic_play)
                } else {
                    viewModel.resume()
                    fab.setImageResource(R.drawable.ic_pause)
                }
            }
        }
    }

    private fun onDataChanged(data: SensorData) {
        displays.forEach { it.onDataChanged(data) }
    }

    private fun setUpDisplayParams() {
        val params = preferenceHelper.getDisplayParams(requireContext())
        displays.forEach { it.setDisplayParams(params) }
    }

    private fun showMeasurementUnitOptions() {
        val supportedUnits = args.tool.allSupportedUnits
        val unit = preferenceHelper.measurementUnit
        val items: Array<CharSequence> = args.tool.allSupportedUnits.map { measureUnit ->
            getString(measureUnit.title)
        }.toTypedArray()
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(R.string.change_unit_title)
            .setSingleChoiceItems(items, supportedUnits.indexOf(unit)) { dialog, pos ->
                preferenceHelper.measurementUnit = supportedUnits[pos]
                dialog.dismiss()
                viewModel.setTool(args.tool, preferenceHelper.measurementUnit)
            }
            .setNegativeButton(android.R.string.cancel) { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    private fun showSamplingRateOptions() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(R.string.settings_sampling_rate_title)
            .setSingleChoiceItems(
                R.array.preferences_sensor_rate_human_value,
                preferenceHelper.providerSamplingRate
            ) { dialog, pos ->
                preferenceHelper.providerSamplingRate = pos
                dialog.dismiss()
                requireActivity().recreate()
            }
            .setNegativeButton(android.R.string.cancel) { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }
}
