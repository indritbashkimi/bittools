package com.ibashkimi.providerstools.settings

import android.content.Context
import android.graphics.Color
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
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

    private val selectedColor: Int

    private val selectedStrokeWidth: Int

    init {
        this.width = width.toPx()
        this.height = height.toPx()
        val accentColor = StyleUtils.obtainColor(context, R.attr.colorAccent, Color.RED)
        this.selectedColor = accentColor
        this.selectedStrokeWidth = 4.toPx()
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

        rootView.addView(itemView, params)

        return ThemeViewHolder(rootView)
    }

    override fun onBindViewHolder(holder: ThemeViewHolder, position: Int) {
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

    private fun Int.toPx(): Int = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        this.toFloat(),
        context.resources.displayMetrics
    ).roundToInt()

    inner class ThemeViewHolder(root: View) : RecyclerView.ViewHolder(root) {
        private val card = itemView.findViewById<MaterialCardView>(R.id.card_view)
        var id: String? = null

        private var _isSelected = false

        var isSelected: Boolean
            get() = _isSelected
            set(value) {
                _isSelected = value
                card.strokeWidth = if (value) selectedStrokeWidth else 0
            }
    }
}
