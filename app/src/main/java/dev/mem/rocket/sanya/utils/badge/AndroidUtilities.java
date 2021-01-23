package dev.mem.rocket.sanya.utils.badge;

import dev.mem.rocket.sanya.MyApp;

class AndroidUtilities {

    public static void runOnUIThread(Runnable runnable) {
        runOnUIThread(runnable, 0);
    }

    public static void runOnUIThread(Runnable runnable, long delay) {
        if (delay == 0) {
            MyApp.Companion.getInstance().getApplicationHandler().post(runnable);
        } else {
            MyApp.Companion.getInstance().getApplicationHandler().postDelayed(runnable, delay);
        }
    }

    public static void cancelRunOnUIThread(Runnable runnable) {
        MyApp.Companion.getInstance().getApplicationHandler().removeCallbacks(runnable);
    }
}
