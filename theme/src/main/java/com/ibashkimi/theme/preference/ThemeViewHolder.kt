package com.ibashkimi.theme.preference

import android.view.View
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import com.ibashkimi.theme.R

class ThemeViewHolder(val rootView: View) : RecyclerView.ViewHolder(rootView) {
    val card: MaterialCardView = itemView.findViewById(R.id.card_view)
    val primary: View = rootView.findViewById(R.id.second)
    val primaryDark: View = rootView.findViewById(R.id.first)
    val secondary: View = rootView.findViewById(R.id.third)
    val background: View = rootView.findViewById(R.id.body)
    val lock: ImageView = rootView.findViewById(R.id.lock_icon)
}