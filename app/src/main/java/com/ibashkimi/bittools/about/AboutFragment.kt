package com.ibashkimi.bittools.about

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources
import androidx.appcompat.widget.Toolbar
import androidx.browser.customtabs.CustomTabsIntent
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import com.ibashkimi.bittools.BuildConfig
import com.ibashkimi.bittools.R

class AboutFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_about, container, false)

        root.findViewById<Toolbar>(R.id.toolbar).apply {
            setTitle(R.string.action_about)
            setNavigationIcon(R.drawable.ic_back_nav)
            setNavigationOnClickListener { findNavController().navigateUp() }
        }

        root.findViewById<TextView>(R.id.title).text = getString(about.title)
        root.findViewById<TextView>(R.id.version).text = about.version
        root.findViewById<TextView>(R.id.description).text = getString(about.description)

        root.findViewById<TextView>(R.id.source_code).setOnClickListener {
            CustomTabsIntent.Builder().build().launchUrl(
                requireContext(),
                Uri.parse(requireContext().getString(R.string.source_code_url))
            )
        }

        val mainLayout = root.findViewById<ViewGroup>(R.id.main_layout)

        for (section in about.sections) {
            val sectionTitle = LayoutInflater.from(mainLayout.context)
                .inflate(R.layout.item_section_title, mainLayout, false) as TextView
            sectionTitle.setText(section.title)
            mainLayout.addView(sectionTitle)
            for (item in section.items) {
                val itemLayout = LayoutInflater.from(mainLayout.context)
                    .inflate(R.layout.item_section_item, mainLayout, false)
                itemLayout.findViewById<ImageView>(R.id.icon)
                    .setImageDrawable(AppCompatResources.getDrawable(requireContext(), item.icon))
                itemLayout.findViewById<TextView>(R.id.title)
                    .setText(item.title)
                itemLayout.setOnClickListener(item.onClick)
                mainLayout.addView(itemLayout)
                //if (section.items.size > 1 && index < section.items.size - 1)
                //    LayoutInflater.from(mainLayout.context).inflate(R.layout.day_night_divider, mainLayout, true)
            }
        }

        return root
    }

    private val about: About by lazy {
        about {
            title = R.string.app_name
            version = getString(R.string.version, BuildConfig.VERSION_NAME)
            description = R.string.app_description
            section {
                title = R.string.about_header_help
                item {
                    icon = R.drawable.ic_feedback
                    title = R.string.about_send_feedback
                    onClick = View.OnClickListener { sendFeedback() }
                }
            }
            section {
                title = R.string.about_header_legal
                item {
                    icon = R.drawable.ic_privacy_policy
                    title = R.string.privacy_policy
                    onClick =
                        Navigation.createNavigateOnClickListener(R.id.action_about_to_privacyPolicy)
                }
                item {
                    icon = R.drawable.ic_licenses
                    title = R.string.about_licenses
                    onClick =
                        Navigation.createNavigateOnClickListener(R.id.action_action_to_licenses)
                }
            }
        }
    }

    private fun sendFeedback() {
        val address = getString(R.string.author_email)
        val subject = getString(R.string.feedback_subject)

        val emailIntent = Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:$address"))
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject)

        val chooserTitle = getString(R.string.feedback_chooser_title)
        startActivity(Intent.createChooser(emailIntent, chooserTitle))
    }
}
