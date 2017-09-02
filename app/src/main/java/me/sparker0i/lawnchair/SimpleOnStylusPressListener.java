package me.sparker0i.lawnchair;

import android.view.View;
import me.sparker0i.lawnchair.StylusEventHelper.StylusButtonListener;

/**
 * Simple listener that performs a long click on the view after a stylus button press.
 */
public class SimpleOnStylusPressListener implements StylusEventHelper.StylusButtonListener {
    private View mView;

    public SimpleOnStylusPressListener(View view) {
        mView = view;
    }

    public boolean onPressed() {
        return mView.isLongClickable() && mView.performLongClick();
    }

    public boolean onReleased() {
        return false;
    }
}