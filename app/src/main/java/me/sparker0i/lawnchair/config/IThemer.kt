package me.sparker0i.lawnchair.config

import android.content.Context
import me.sparker0i.lawnchair.allapps.theme.IAllAppsThemer
import me.sparker0i.lawnchair.popup.theme.IPopupThemer

interface IThemer {

    fun allAppsTheme(context: Context): IAllAppsThemer
    fun popupTheme(context: Context): IPopupThemer
}