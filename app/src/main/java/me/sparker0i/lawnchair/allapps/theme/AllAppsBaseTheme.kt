package me.sparker0i.lawnchair.allapps.theme

import android.content.Context
import android.graphics.Color
import android.support.v4.content.ContextCompat
import me.sparker0i.lawnchair.R
import me.sparker0i.lawnchair.Utilities
import me.sparker0i.lawnchair.blur.BlurWallpaperProvider
import me.sparker0i.lawnchair.config.FeatureFlags
import me.sparker0i.lawnchair.dynamicui.ExtractedColors

open class AllAppsBaseTheme(val context: Context) : IAllAppsThemer {
    override val backgroundColor = me.sparker0i.lawnchair.Utilities
            .resolveAttributeData(FeatureFlags.applyDarkTheme(context, FeatureFlags.DARK_ALLAPPS), R.attr.allAppsContainerColor)
    override val backgroundColorBlur = me.sparker0i.lawnchair.Utilities
            .resolveAttributeData(FeatureFlags.applyDarkTheme(context, FeatureFlags.DARK_BLUR), R.attr.allAppsContainerColorBlur)

    override fun iconTextColor(backgroundAlpha: Int): Int {
        if (me.sparker0i.lawnchair.Utilities.getPrefs(context).useCustomAllAppsTextColor) {
            return me.sparker0i.lawnchair.Utilities.getPrefs(context).allAppsLabelColor
        } else if (FeatureFlags.useDarkTheme(FeatureFlags.DARK_ALLAPPS)) {
            return Color.WHITE
        } else if (backgroundAlpha < 128 && !BlurWallpaperProvider.isEnabled(BlurWallpaperProvider.BLUR_ALLAPPS) || backgroundAlpha < 50) {
            return Color.WHITE
        } else {
            return ContextCompat.getColor(context, R.color.quantum_panel_text_color)
        }
    }

    override val iconTextLines = 1
    override val searchTextColor = 0
    override val searchBarHintTextColor = me.sparker0i.lawnchair.Utilities.getDynamicAccent(context)
    override val fastScrollerHandleColor = me.sparker0i.lawnchair.Utilities.getDynamicAccent(context)
    override val fastScrollerPopupTintColor: Int
        get() {
            if (me.sparker0i.lawnchair.Utilities.getPrefs(context).enableDynamicUi) {
                val tint = me.sparker0i.lawnchair.Utilities.getDynamicAccent(context)
                if (tint != -1) {
                    return tint
                }
            }
            return 0
        }
    override val fastScrollerPopupTextColor: Int
        get() {
            var color = Color.WHITE
            if (me.sparker0i.lawnchair.Utilities.getPrefs(context).enableDynamicUi) {
                val tint = me.sparker0i.lawnchair.Utilities.getDynamicAccent(context)
                if (tint != -1) {
                    color = me.sparker0i.lawnchair.Utilities.getColor(context, me.sparker0i.lawnchair.dynamicui.ExtractedColors.VIBRANT_FOREGROUND_INDEX, Color.WHITE)
                }
            }
            return color
        }

    override val iconLayout = R.layout.all_apps_icon
    override fun numIconPerRow(default: Int) = default
    override fun iconHeight(default: Int) = default

}