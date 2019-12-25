package com.ibashkimi.providerstools.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.navigation.NavigationView
import com.ibashkimi.providerstools.R
import com.ibashkimi.providerstools.utils.allSupportedUnits
import com.ibashkimi.providerstools.utils.helper

class ToolSettingsDialogFragment : BottomSheetDialogFragment() {

    private val args: ToolSettingsDialogFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.dialog_tool_settings, container, false)

        val navigationView = root.findViewById<NavigationView>(R.id.navigation_view)
        navigationView.menu.findItem(R.id.action_unit).isVisible =
            args.tool.allSupportedUnits.size > 1
        navigationView.setNavigationItemSelectedListener {
            val preferenceHelper = args.tool.helper(requireContext())
            //dismiss()
            when (it.itemId) {
                R.id.action_unit -> {
                    val supportedUnits = args.tool.allSupportedUnits
                    val unit = preferenceHelper.measurementUnit
                    val items: Array<CharSequence> =
                        args.tool.allSupportedUnits.map { measureUnit ->
                            getString(measureUnit.title)
                        }.toTypedArray()
                    AlertDialog.Builder(requireContext())
                        .setSingleChoiceItems(items, supportedUnits.indexOf(unit)) { dialog, pos ->
                            preferenceHelper.measurementUnit = supportedUnits[pos]
                            dialog.dismiss()
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
                        ToolSettingsDialogFragmentDirections.actionToolSettingsToPersonalize(args.tool)
                    )
                    true
                }
                else -> super.onOptionsItemSelected(it)
            }
        }

        return root
    }
}