package me.sparker0i.lawnchair.allapps

import android.content.Context
import android.graphics.Point
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import me.sparker0i.lawnchair.*
import me.sparker0i.lawnchair.dragndrop.DragOptions
import me.sparker0i.lawnchair.dragndrop.DragView
import me.sparker0i.lawnchair.shortcuts.ShortcutDragPreviewProvider
import java.lang.ref.WeakReference

class AllAppsIconRowView(context: Context, attrs: AttributeSet) :
        LinearLayout(context, attrs), me.sparker0i.lawnchair.BaseRecyclerViewFastScrollBar.FastScrollFocusableView, View.OnTouchListener {

    lateinit var icon: me.sparker0i.lawnchair.BubbleTextView
    lateinit var title: TextView

    var text: CharSequence?
        get() = title.text
        set(value) {
            title.text = value
        }

    var textColor: Int
        get() = title.currentTextColor
        set(value) {
            title.setTextColor(value)
        }

    val iconShift = Point()
    val iconLastTouchPos = Point()
    val tempPoint = Point()
    val launcher = me.sparker0i.lawnchair.Launcher.getLauncher(context)
    var dragView: WeakReference<me.sparker0i.lawnchair.dragndrop.DragView?> = WeakReference(null as me.sparker0i.lawnchair.dragndrop.DragView?)

    val deferDragCondition = object : me.sparker0i.lawnchair.dragndrop.DragOptions.DeferDragCondition() {

        override fun onDragStart() {
            dragView.get()?.animateShift(0, 0, true)
        }
    }

    init {
        setOnTouchListener(this)
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        icon = findViewById(android.R.id.icon)
        title = findViewById(android.R.id.title)
    }

    override fun setFastScrollFocusState(focusState: me.sparker0i.lawnchair.FastBitmapDrawable.State?, animated: Boolean) {
        icon.setFastScrollFocusState(focusState, animated)
    }

    fun applyFromApplicationInfo(appInfo: me.sparker0i.lawnchair.AppInfo) {
        icon.applyFromApplicationInfo(appInfo, false)
    }

    override fun onTouch(view: View, motionEvent: MotionEvent): Boolean {
        when (motionEvent.action) {
            MotionEvent.ACTION_DOWN -> iconLastTouchPos.set(motionEvent.x.toInt(), motionEvent.y.toInt())
            MotionEvent.ACTION_MOVE -> iconLastTouchPos.set(motionEvent.x.toInt(), motionEvent.y.toInt())
        }
        return false
    }

    fun getIconCenter(): Point {
        val halfHeight = measuredHeight / 2
        tempPoint.x = halfHeight
        tempPoint.y = halfHeight
        if (me.sparker0i.lawnchair.Utilities.isRtl(resources))
            tempPoint.x = measuredWidth - tempPoint.x
        return tempPoint
    }

    fun beginDrag(source: me.sparker0i.lawnchair.DragSource) {
        iconShift.x = iconLastTouchPos.x - getIconCenter().x
        iconShift.y = iconLastTouchPos.y - launcher.deviceProfile.iconSizePx
        val dragOptions = me.sparker0i.lawnchair.dragndrop.DragOptions().apply { deferDragCondition = this@AllAppsIconRowView.deferDragCondition }
        dragView = WeakReference(
                launcher.workspace.beginDragShared(icon, source, icon.tag as me.sparker0i.lawnchair.ItemInfo, me.sparker0i.lawnchair.shortcuts.ShortcutDragPreviewProvider(icon, iconShift), dragOptions))
        dragView.get()?.shift(-iconShift.x, -iconShift.y)
    }

}