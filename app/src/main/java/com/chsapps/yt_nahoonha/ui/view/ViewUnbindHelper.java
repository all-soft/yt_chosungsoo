package com.chsapps.yt_nahoonha.ui.view;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;

public class ViewUnbindHelper {
    /**
     * Removes the reference to the activity from every view in a view hierarchy
     * (listeners, images etc.). This method should be called in the onDestroy() method
     * of each activity.
     * This code may stinks, but better than worse - suspiciously, Android framework
     * does not free resources immediately which are consumed by Views and this leads to
     * OutOfMemoryError sometimes although there are no user mistakes.
     *
     * @param view View to free from memory
     * @see http://code.google.com/p/android/issues/detail?serviceId=8488
     */
    public static void unbindReferences(View view) {
        try {
            if (view != null) {
                if (view instanceof ViewGroup) {
                    unbindViewGroupReferences((ViewGroup)view);
                }
            }
        } catch (Exception ignore) {
        }
    }

    /**
     * Removes the reference to the activity from every view in a view hierarchy
     * (listeners, images etc.). This method should be called in the onDestroy() method
     * of each activity.
     * This code may stinks, but better than worse - suspiciously, Android framework
     * does not free resources immediately which are consumed by Views and this leads to
     * OutOfMemoryError sometimes although there are no user mistakes.
     *
     * @param view View to free from memory
     * @see http://code.google.com/p/android/issues/detail?serviceId=8488
     */
    public static void unbindReferences(Activity activity, int viewID) {
        try {
            View view = activity.findViewById(viewID);
            if (view != null) {
                if (view instanceof ViewGroup) {
                    unbindViewGroupReferences((ViewGroup)view);
                }
            }
        } catch (Exception ignore) {
        }
    }

    private static void unbindViewGroupReferences(ViewGroup viewGroup) {
        try {
            int nrOfChildren = viewGroup.getChildCount();
            for (int i = 0; i < nrOfChildren; i++) {
                View view = viewGroup.getChildAt(i);
                if (view instanceof ViewGroup) {
                    unbindViewGroupReferences((ViewGroup)view);
                }
            }
            if(viewGroup.getChildCount() > 0) {
                viewGroup.removeAllViews();
            }
        } catch (Exception ignore) {
        }
    }

}
