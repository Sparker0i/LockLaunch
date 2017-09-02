package me.sparker0i.lawnchair.widget;

import java.util.ArrayList;

import me.sparker0i.lawnchair.model.PackageItemInfo;
import me.sparker0i.lawnchair.model.PackageItemInfo;
import me.sparker0i.lawnchair.model.PackageItemInfo;

public class WidgetListRowEntry {
    public final PackageItemInfo pkgItem;
    public String titleSectionName;
    public final ArrayList widgets;

    public WidgetListRowEntry(PackageItemInfo packageItemInfo, ArrayList arrayList) {
        this.pkgItem = packageItemInfo;
        this.widgets = arrayList;
    }
}