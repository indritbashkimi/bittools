package com.ibashkimi.providerstools

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.ibashkimi.provider.providerdata.SensorData
import com.ibashkimi.providerstools.model.ProviderDisplay
import com.ibashkimi.providerstools.theme.Gauges
import com.ibashkimi.providerstools.theme.Layouts
import com.ibashkimi.providerstools.utils.ToolPreferenceHelper
import com.ibashkimi.providerstools.utils.allSupportedUnits
import com.ibashkimi.providerstools.utils.helper
import com.ibashkimi.providerstools.utils.title

class ProviderFragment : Fragment() {

    private val args: ProviderFragmentArgs by navArgs()

    private val viewModel: ProviderViewModel by viewModels()

    private lateinit var preferenceHelper: ToolPreferenceHelper

    private lateinit var displayMap: Map<String, Int>

    private var layout = -1

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
        setupLayout()
        val rootView = inflater.inflate(this.layout, container, false)
        setUpToolbar(rootView)
        setUpFab(rootView)
        setUpDisplays(rootView)

        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel.measurementUnit.observe(viewLifecycleOwner, Observer {
            setUpDisplayParams()
        })

        viewModel.setTool(args.tool, preferenceHelper.measurementUnit)

        viewModel.sensorData.observe(viewLifecycleOwner, Observer {
            onDataChanged(it)
        })
    }

    private fun setupLayout() {
        when (val layout = preferenceHelper.layout) {
            Layouts.LAYOUT_SIMPLE -> {
                this.layout = R.layout.fragment_simple
                this.displayMap = mapOf(Gauges.PREF_KEY_WIDGET_1 to R.id.container_1)
            }
            Layouts.LAYOUT_NORMAL -> {
                this.layout = R.layout.fragment_normal_layout
                this.displayMap = mapOf(
                    Gauges.PREF_KEY_WIDGET_1 to R.id.container_1,
                    Gauges.PREF_KEY_WIDGET_2 to R.id.container_2
                )
            }
            Layouts.LAYOUT_RICH -> {
                this.layout = R.layout.fragment_rich_layout
                this.displayMap = mapOf(
                    Gauges.PREF_KEY_WIDGET_1 to R.id.container_1,
                    Gauges.PREF_KEY_WIDGET_2 to R.id.container_2,
                    Gauges.PREF_KEY_WIDGET_3 to R.id.container_3
                )
            }
            else -> throw IllegalArgumentException("Unknown layout $layout.")
        }
    }

    private fun onDataChanged(data: SensorData) {
        displays.forEach { it.onDataChanged(data) }
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

    private fun setUpDisplays(rootView: View) {
        displays.clear()
        for ((widgetPosition, widgetContainer) in displayMap) {
            val layoutRes = preferenceHelper.getWidgetLayout(widgetPosition)
            val container = rootView.findViewById<View>(widgetContainer) as ViewGroup
            val root = LayoutInflater.from(container.context)
                .inflate(layoutRes, container, true)
            val display = root.findViewById<View>(R.id.display) as ProviderDisplay
            displays.add(display)
        }
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
        AlertDialog.Builder(requireContext())
            .setTitle(R.string.change_unit_title)
            .setSingleChoiceItems(items, supportedUnits.indexOf(unit)) { dialog, pos ->
                preferenceHelper.measurementUnit = supportedUnits[pos]
                dialog.dismiss()
                viewModel.setTool(args.tool, preferenceHelper.measurementUnit)
            }
            .setNegativeButton(android.R.string.cancel) { dialog, _ ->
                dialog.dismiss()
            }
            .create().show()
    }

    private fun showSamplingRateOptions() {
        AlertDialog.Builder(requireContext())
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
            .create().show()
    }
}
