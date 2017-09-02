package me.sparker0i.lawnchair.config

import android.content.Context
import me.sparker0i.lawnchair.Utilities
import me.sparker0i.lawnchair.allapps.theme.AllAppsBaseTheme
import me.sparker0i.lawnchair.allapps.theme.AllAppsVerticalTheme
import me.sparker0i.lawnchair.allapps.theme.IAllAppsThemer
import me.sparker0i.lawnchair.popup.theme.IPopupThemer
import me.sparker0i.lawnchair.popup.theme.PopupBaseTheme
import me.sparker0i.lawnchair.popup.theme.PopupCardTheme

open class ThemerImpl : IThemer {

    var allAppsTheme: IAllAppsThemer? = null
    var popupTheme: IPopupThemer? = null

    override fun allAppsTheme(context: Context): IAllAppsThemer {
        val useVerticalLayout = me.sparker0i.lawnchair.Utilities.getPrefs(context).verticalDrawerLayout
        if (allAppsTheme == null ||
                (useVerticalLayout && allAppsTheme !is AllAppsVerticalTheme) ||
                (!useVerticalLayout && allAppsTheme is AllAppsVerticalTheme))
            allAppsTheme = if (useVerticalLayout) AllAppsVerticalTheme(context) else AllAppsBaseTheme(context)
        return allAppsTheme!!
    }

    override fun popupTheme(context: Context): IPopupThemer {
        val useCardTheme = me.sparker0i.lawnchair.Utilities.getPrefs(context).popupCardTheme
        if (popupTheme == null ||
                (useCardTheme && popupTheme !is PopupCardTheme) ||
                (!useCardTheme && popupTheme !is PopupBaseTheme)) {
            popupTheme = if (useCardTheme) PopupCardTheme() else PopupBaseTheme()
        }
        return popupTheme!!
    }
}