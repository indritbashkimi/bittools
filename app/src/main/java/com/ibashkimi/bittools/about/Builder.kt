package com.ibashkimi.bittools.about

import android.view.View
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

data class About(
    @StringRes val title: Int,
    val version: String,
    @StringRes val description: Int,
    val sections: List<Section>
)

class Item(
    @StringRes val title: Int,
    @DrawableRes val icon: Int,
    val onClick: View.OnClickListener?
)

class Section(val title: Int, val items: List<Item>)


@DslMarker
annotation class AboutDsl

@AboutDsl
class ItemBuilder(
    initialTitle: Int,
    initialIcon: Int,
    initialOnClickListener: View.OnClickListener?
) {

    var title: Int = initialTitle

    var icon: Int = initialIcon

    var onClick: View.OnClickListener? = initialOnClickListener

    fun build(): Item {
        return Item(title, icon, onClick)
    }
}

@AboutDsl
class SectionBuilder {

    var title: Int = 0

    private val items = mutableListOf<Item>()

    fun build(): Section {
        return Section(title, items)
    }

    fun item(
        title: Int = 0,
        icon: Int = 0,
        onClickListener: View.OnClickListener? = null,
        setup: ItemBuilder.() -> Unit = {}
    ) {
        val itemBuilder = ItemBuilder(title, icon, onClickListener)
        itemBuilder.setup()
        items += itemBuilder.build()
    }

}

@AboutDsl
class AboutBuilder {

    var title: Int = 0

    var version: String = ""

    var description: Int = 0

    private val sections = mutableListOf<Section>()

    operator fun Section.unaryPlus() {
        sections += this
    }

    fun section(setup: SectionBuilder.() -> Unit = {}) {
        val houseBuilder = SectionBuilder()
        houseBuilder.setup()
        sections += houseBuilder.build()
    }

    fun build(): About {
        return About(title, version, description, sections)
    }

    @Suppress("UNUSED_PARAMETER")
    @Deprecated(
        level = DeprecationLevel.ERROR,
        message = "Villages can't be nested."
    )
    fun about(param: () -> Unit = {}) {
    }
}

@AboutDsl
fun about(setup: AboutBuilder.() -> Unit): About {
    val aboutBuilder = AboutBuilder()
    aboutBuilder.setup()
    return aboutBuilder.build()
}