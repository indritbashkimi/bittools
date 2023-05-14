package com.ibashkimi.ruler.protractor

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.ibashkimi.ruler.R

class ProtractorFragment : Fragment() {

    private lateinit var protractor: Protractor

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_protractor, container, false)
        view.findViewById<Toolbar>(R.id.toolbar).apply {
            setTitle(R.string.protractor)
            setNavigationIcon(R.drawable.ic_back_nav)
            setNavigationOnClickListener { findNavController().navigateUp() }
        }
        protractor = view.findViewById(R.id.protractor)
        return view
    }

    override fun onResume() {
        super.onResume()
        protractor.onRestore()
    }

    override fun onPause() {
        super.onPause()
        protractor.onSave()
    }
}
