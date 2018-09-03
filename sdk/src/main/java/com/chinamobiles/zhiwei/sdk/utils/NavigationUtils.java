package com.chinamobiles.zhiwei.sdk.utils;

import android.support.design.internal.NavigationMenuView;
import android.support.design.widget.NavigationView;

/**
 *
 * <p>
 * NavigationView utils
 */

public class NavigationUtils {

    public static void disableNavigationViewScrollbars(NavigationView navigationView) {
        if (navigationView != null) {
            NavigationMenuView navigationMenuView = (NavigationMenuView) navigationView
                    .getChildAt(0);
            if (navigationMenuView != null) {
                navigationMenuView.setVerticalScrollBarEnabled(false);
            }
        }
    }
}
