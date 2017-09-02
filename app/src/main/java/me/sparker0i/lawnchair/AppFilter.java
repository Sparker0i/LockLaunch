package me.sparker0i.lawnchair;

import android.content.ComponentName;
import android.content.Context;

public interface AppFilter {

    boolean shouldShowApp(ComponentName app, Context context);

}
