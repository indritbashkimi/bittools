package com.ibashkimi.bittools.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.ibashkimi.bittools.R

class SettingsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_settings, container, false)
        root.findViewById<Toolbar>(R.id.toolbar).apply {
            setTitle(R.string.action_settings)
            setNavigationIcon(R.drawable.ic_back_nav)
            setNavigationOnClickListener { findNavController().navigateUp() }
        }
        return root
    }
}