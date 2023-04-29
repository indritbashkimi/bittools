package com.ibashkimi.ruler.ruler1

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.preference.PreferenceManager
import com.ibashkimi.ruler.R


class Ruler1Fragment : Fragment() {
    private lateinit var overlay: Ruler1Overlay

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_ruler1, container, false)

        rootView.findViewById<Toolbar>(R.id.toolbar).apply {
            setTitle(R.string.ruler_title)
            setNavigationIcon(R.drawable.ic_back_nav)
            setNavigationOnClickListener { findNavController().navigateUp() }
        }

        overlay = rootView.findViewById(R.id.ruler1overlay)
        return rootView
    }

    override fun onResume() {
        super.onResume()
        val prefs = PreferenceManager.getDefaultSharedPreferences(requireContext())
        val x = prefs.getFloat(RULER1_X_TOUCH, 0f)
        val y = prefs.getFloat(RULER1_Y_TOUCH, 0f)
        overlay.setPoint(x, y)
    }

    override fun onPause() {
        super.onPause()
        PreferenceManager.getDefaultSharedPreferences(requireContext())
            .edit()
            .putFloat(RULER1_X_TOUCH, overlay.x)
            .putFloat(RULER1_Y_TOUCH, overlay.y)
            .apply()
    }

    companion object {
        private const val RULER1_X_TOUCH = "ruler1_x_touch"
        private const val RULER1_Y_TOUCH = "ruler1_y_touch"
    }
}