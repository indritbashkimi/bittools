package com.ibashkimi.theme.preference

import android.annotation.SuppressLint
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.ShapeDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ibashkimi.theme.R
import com.ibashkimi.theme.theme.Theme


@Suppress("unused")
class ThemeAdapter(
    var themes: List<DecodedTheme>,
    var selectedIndex: Int,
    private val isThemePremium: (Theme) -> Boolean,
    private val listener: ThemeSelectedListener?
) : RecyclerView.Adapter<ThemeViewHolder>() {

    private var selectedHolder: ThemeViewHolder? = null

    interface ThemeSelectedListener {
        fun onThemeSelected(theme: Theme)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ThemeViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.item_theme, parent, false)
        return ThemeViewHolder(itemView)
    }

    @SuppressLint("ResourceType")
    override fun onBindViewHolder(holder: ThemeViewHolder, position: Int) {
        val theme = themes[position]

        holder.primary.setBackgroundColor(theme.appBarBackground)
        holder.primaryDark.setBackgroundColor(theme.colorPrimaryDark)
        holder.background.setBackgroundColor(theme.windowBackground)
        holder.lock.visibility = if (isThemePremium(theme.theme)) View.VISIBLE else View.GONE

        when (val background = holder.secondary.background) {
            is ShapeDrawable -> background.paint.color = theme.colorSecondary
            is GradientDrawable -> background.setColor(theme.colorSecondary)
            is ColorDrawable -> background.color = theme.colorSecondary
        }
        //holder.secondary.setBackgroundColor(colorAccent);
        holder.border.visibility = View.INVISIBLE
        if (position == selectedIndex) {
            holder.border.visibility = View.VISIBLE
            selectedHolder = holder
        }
        if (listener != null) {
            holder.rootView.setOnClickListener {
                listener.onThemeSelected(theme.theme)
            }
        }
    }

    override fun getItemCount(): Int {
        return themes.size
    }
}