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
class ThemeAdapter2(
    var themes: List<ThemeAdapterItem>,
    var selectedIndex: Int,
    private val onThemeSelected: (String) -> Unit
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
        val item = themes[position]

        holder.primary.setBackgroundColor(item.attrs.appBarBackground)
        holder.primaryDark.setBackgroundColor(item.attrs.colorPrimaryDark)
        holder.background.setBackgroundColor(item.attrs.windowBackground)
        holder.lock.visibility = if (item.isPremium) View.VISIBLE else View.GONE

        when (val background = holder.secondary.background) {
            is ShapeDrawable -> background.paint.color = item.attrs.colorSecondary
            is GradientDrawable -> background.setColor(item.attrs.colorSecondary)
            is ColorDrawable -> background.color = item.attrs.colorSecondary
        }

        holder.border.visibility = View.INVISIBLE
        if (position == selectedIndex) {
            holder.border.visibility = View.VISIBLE
            selectedHolder = holder
        }
        holder.rootView.setOnClickListener {
            onThemeSelected(item.themeId)
        }
    }

    override fun getItemCount(): Int {
        return themes.size
    }
}