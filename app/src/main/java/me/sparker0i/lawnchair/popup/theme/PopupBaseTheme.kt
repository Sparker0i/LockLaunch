package me.sparker0i.lawnchair.popup.theme

import me.sparker0i.lawnchair.R

class PopupBaseTheme : IPopupThemer {

    override val itemBg = R.drawable.bg_white_round_rect
    override val itemSpacing = R.dimen.popup_items_spacing
    override val backgroundRadius = R.dimen.bg_round_rect_radius
    override val wrapInMain = false
}