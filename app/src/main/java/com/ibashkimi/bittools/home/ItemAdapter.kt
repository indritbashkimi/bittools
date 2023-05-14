package com.ibashkimi.bittools.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ibashkimi.bittools.R

class ItemAdapter(
    var tools: List<Item>,
    var unsupportedTools: List<Item>,
    private val layout: Int,
    private val listener: ClickListener
) : RecyclerView.Adapter<ItemAdapter.ViewHolder>() {

    /* Interface for handling clicks - both normal and long ones. */
    interface ClickListener {

        /**
         * Called when the view is clicked.
         * @param item    item clicked
         * @param isLongClick true if long click, false otherwise
         */
        fun onItemClick(item: Item, isLongClick: Boolean)

        fun onHideClicked()
    }

    override fun getItemViewType(position: Int): Int {
        return when {
            position < tools.size -> 0
            position == tools.size -> 1
            else -> 2
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return when (viewType) {
            1 -> {
                val itemView = LayoutInflater.from(parent.context)
                    .inflate(R.layout.tool_item_separator, parent, false)
                SeparatorViewHolder(itemView, listener)
            }

            else -> {
                val itemView =
                    LayoutInflater.from(parent.context).inflate(this.layout, parent, false)
                ToolViewHolder(itemView, listener)
            }
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        when (getItemViewType(position)) {
            0 -> holder.bind(tools[position])
            1 -> holder.bind(null)
            else -> holder.bind(unsupportedTools[position - tools.size - 1])
        }
    }

    override fun getItemCount(): Int =
        tools.size + unsupportedTools.size + if (unsupportedTools.isNotEmpty()) 1 else 0

    abstract class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        abstract fun bind(item: Item?)

        abstract fun unbind()
    }

    class SeparatorViewHolder(itemView: View, listener: ClickListener) : ViewHolder(itemView) {
        private val hideButton: Button = itemView.findViewById(R.id.hide_action)

        init {
            hideButton.setOnClickListener {
                listener.onHideClicked()
            }
        }

        override fun bind(item: Item?) {

        }

        override fun unbind() {

        }
    }

    inner class ToolViewHolder(itemView: View, private val clickListener: ClickListener) :
        ViewHolder(itemView), View.OnClickListener, View.OnLongClickListener {
        private val text: TextView = itemView.findViewById(R.id.name_text)
        private val image: ImageView = itemView.findViewById(R.id.icon)
        lateinit var item: Item

        init {
            itemView.setOnClickListener(this)
            itemView.setOnLongClickListener(this)
        }

        override fun bind(item: Item?) {
            this.item = item!!
            text.setText(this.item.name)
            image.setImageResource(this.item.icon)
        }

        override fun unbind() {

        }

        override fun onClick(v: View) {
            clickListener.onItemClick(item, false)
        }

        override fun onLongClick(v: View): Boolean = false
    }
}
