package me.sparker0i.lawnchair.allapps.theme

import android.content.Context
import me.sparker0i.lawnchair.R

class AllAppsVerticalTheme(context: Context) : AllAppsBaseTheme(context) {

    override val iconLayout = R.layout.all_apps_icon_vertical
    override fun numIconPerRow(default: Int) = 1
    override fun iconHeight(default: Int) = context.resources.getDimensionPixelSize(R.dimen.all_apps_vertical_row_height)
}