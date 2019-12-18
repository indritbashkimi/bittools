package com.ibashkimi.bittools.about

import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import androidx.appcompat.widget.Toolbar
import androidx.browser.customtabs.CustomTabsIntent
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.ibashkimi.bittools.R
import com.ibashkimi.theme.utils.StyleUtils

class PrivacyPolicyFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_privacy_policy, container, false)

        root.findViewById<Toolbar>(R.id.toolbar).apply {
            setTitle(R.string.privacy_policy)
            setNavigationIcon(R.drawable.ic_cancel_toolbar)
            setNavigationOnClickListener { findNavController().navigateUp() }
            inflateMenu(R.menu.open_in_browser)
            setOnMenuItemClickListener {
                menu.close()
                val builder = CustomTabsIntent.Builder()
                builder.setToolbarColor(
                    StyleUtils.obtainColor(
                        requireContext(),
                        R.attr.colorPrimary,
                        Color.BLACK
                    )
                )
                val customTabsIntent = builder.build()
                customTabsIntent.launchUrl(
                    requireContext(),
                    Uri.parse(requireContext().getString(R.string.privacy_policy_url))
                )
                true
            }
        }

        root.findViewById<WebView>(R.id.webView)
            .loadUrl("file:///android_asset/privacy_policy.html")

        return root
    }
}