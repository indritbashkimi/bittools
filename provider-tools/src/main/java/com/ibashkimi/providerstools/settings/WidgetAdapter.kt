package com.ibashkimi.providerstools.settings

import android.content.Context
import android.graphics.Color
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.recyclerview.widget.RecyclerView
import com.ibashkimi.providerstools.R
import com.ibashkimi.providerstools.data.WidgetItem
import com.ibashkimi.theme.utils.StyleUtils
import kotlin.math.roundToInt


class WidgetAdapter(
    private val context: Context,
    private val items: List<WidgetItem>,
    var selectedItem: Int,
    width: Int, height: Int
) : RecyclerView.Adapter<WidgetAdapter.ThemeViewHolder>() {

    var listener: WidgetAdapterListener? = null

    private var selectedHolder: ThemeViewHolder? = null

    private val width: Int

    private val height: Int

    private val unselectedColor: Int
    private val selectedColor: Int

    init {
        this.width = dpToPx(width)
        this.height = dpToPx(height)
        this.unselectedColor = R.color.card_background_color
        this.selectedColor = StyleUtils.obtainColor(context, R.attr.colorAccent, Color.RED)
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ThemeViewHolder {
        val rootView = LayoutInflater.from(parent.context).inflate(
            R.layout.item_theme_layout,
            parent,
            false
        ) as FrameLayout
        val rootParams = rootView.layoutParams
        rootParams.width = width
        rootParams.height = height
        rootView.layoutParams = rootParams

        val layout = items[viewType].layout
        val itemView = LayoutInflater.from(parent.context).inflate(layout, parent, false)
        val params = FrameLayout.LayoutParams(
            FrameLayout.LayoutParams.MATCH_PARENT,
            FrameLayout.LayoutParams.MATCH_PARENT
        )
        val margin = dpToPx(2)
        params.setMargins(margin, margin, margin, margin)

        val border =
            LayoutInflater.from(parent.context).inflate(R.layout.item_theme_border, parent, false)
        val borderParams = FrameLayout.LayoutParams(
            FrameLayout.LayoutParams.MATCH_PARENT,
            FrameLayout.LayoutParams.MATCH_PARENT
        )
        val borderMargin = dpToPx(0)
        borderParams.setMargins(borderMargin, borderMargin, borderMargin, borderMargin)
        border.background = context.getDrawable(R.drawable.theme_item_border_unselected)

        rootView.addView(itemView, params)
        rootView.addView(border, borderParams)

        return ThemeViewHolder(rootView)
    }

    private fun dpToPx(dp: Int): Int {
        val displayMetrics = context.resources.displayMetrics
        return (dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT)).roundToInt()
    }

    override fun onBindViewHolder(holder: ThemeViewHolder, position: Int) {
        //holder.id = items[position].id
        val item = items[position]
        if (position == selectedItem) {
            holder.isSelected = true
            selectedHolder = holder
        }
        holder.itemView.setOnClickListener {
            if (holder == selectedHolder)
                return@setOnClickListener
            selectedHolder?.isSelected = false
            selectedHolder = holder
            selectedHolder!!.isSelected = true
            selectedItem = position
            notifyDataSetChanged()
            listener?.onWidgetSelected(item)
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun getSelectedItem(): String? {
        return selectedHolder?.id
    }

    inner class ThemeViewHolder(root: View) : RecyclerView.ViewHolder(root) {
        private val border: View = itemView.findViewById(R.id.border_view)
        var id: String? = null

        private var _isSelected = false

        var isSelected: Boolean
            get() = _isSelected
            set(value) {
                _isSelected = value
                if (value) {
                    border.background = context.getDrawable(R.drawable.theme_item_border_selected)
                } else {
                    border.background = context.getDrawable(R.drawable.theme_item_border_unselected)
                }
            }
    }
}
