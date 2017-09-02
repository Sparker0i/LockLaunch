package me.sparker0i.lawnchair.pixelify

import android.content.res.Resources
import android.graphics.drawable.Drawable
import me.sparker0i.lawnchair.Utilities
import me.sparker0i.lawnchair.graphics.IconShapeOverride
import me.sparker0i.lawnchair.util.DrawableUtils
import me.sparker0i.lawnchair.util.drawableInflater
import me.sparker0i.lawnchair.util.overrideSdk

class AdaptiveIconProvider {

    companion object {

        const val TAG = "AdaptiveIconProvider"

        fun getDrawableForDensity(res: Resources, id: Int, density: Int, shapeInfo: IconShapeOverride.ShapeInfo): Drawable {
            var drawable: Drawable? = null
            if (shapeInfo.useRoundIcon && !me.sparker0i.lawnchair.Utilities.ATLEAST_OREO) {
                // Backport for < O
                res.overrideSdk(26) {
                    drawable = try {
                        res.getDrawableForDensity(id, density)
                    } catch (e: Resources.NotFoundException) {
                        val drawableInflater = res.drawableInflater
                        val parser = res.getXml(id)
                        DrawableUtils.inflateFromXml(drawableInflater, parser)
                    }
                }
            } else if (!shapeInfo.useRoundIcon && me.sparker0i.lawnchair.Utilities.ATLEAST_OREO) {
                // Force non-rounded icons on O
                res.overrideSdk(25) {
                    drawable = res.getDrawableForDensity(id, density)
                }
            } else {
                drawable = res.getDrawableForDensity(id, density)
            }
            return drawable!!
        }
    }
}