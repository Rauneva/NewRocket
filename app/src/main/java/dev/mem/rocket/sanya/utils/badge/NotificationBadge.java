package dev.mem.rocket.sanya.utils.badge;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.os.Build;

import java.io.Closeable;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import dev.mem.rocket.sanya.MyApp;
import me.leolin.shortcutbadger.impl.DefaultBadger;
import me.leolin.shortcutbadger.impl.OPPOHomeBader;
import me.leolin.shortcutbadger.impl.VivoHomeBadger;
import me.leolin.shortcutbadger.impl.ZukHomeBadger;

public class NotificationBadge {

    private static final List<Class<? extends Badger>> BADGERS = new LinkedList<>();
    private static boolean initied;
    private static Badger badger;
    private static ComponentName componentName;

    public interface Badger {
        void executeBadge(int badgeCount);

        List<String> getSupportLaunchers();
    }


    public static class XiaomiHomeBadger implements Badger {

        public static final String INTENT_ACTION = "android.intent.action.APPLICATION_MESSAGE_UPDATE";
        public static final String EXTRA_UPDATE_APP_COMPONENT_NAME = "android.intent.extra.update_application_component_name";
        public static final String EXTRA_UPDATE_APP_MSG_TEXT = "android.intent.extra.update_application_message_text";

        @Override
        public void executeBadge(int badgeCount) {
            try {
                Class miuiNotificationClass = Class.forName("android.app.MiuiNotification");
                Object miuiNotification = miuiNotificationClass.newInstance();
                Field field = miuiNotification.getClass().getDeclaredField("messageCount");
                field.setAccessible(true);
                field.set(miuiNotification, String.valueOf(badgeCount == 0 ? "" : badgeCount));
            } catch (Throwable e) {
                final Intent localIntent = new Intent(INTENT_ACTION);
                localIntent.putExtra(EXTRA_UPDATE_APP_COMPONENT_NAME, "dev.mem.rocket.sanya" + "/" + "dev.mem.rocket.sanya.utils.badgeNotificationBadge");
                localIntent.putExtra(EXTRA_UPDATE_APP_MSG_TEXT, String.valueOf(badgeCount == 0 ? "" : badgeCount));
                if (canResolveBroadcast(localIntent)) {
                    AndroidUtilities.runOnUIThread(new Runnable() {
                        @Override
                        public void run() {

                            MyApp.Companion.getInstance().getApplicationContext().sendBroadcast(localIntent);
                        }
                    });
                }
            }
        }

        @Override
        public List<String> getSupportLaunchers() {
            return Arrays.asList(
                    "com.miui.miuilite",
                    "com.miui.home",
                    "com.miui.miuihome",
                    "com.miui.miuihome2",
                    "com.miui.mihome",
                    "com.miui.mihome2"
            );
        }
    }

    public static class DefaultBadger implements Badger {
        private static final String INTENT_ACTION = "android.intent.action.BADGE_COUNT_UPDATE";
        private static final String INTENT_EXTRA_BADGE_COUNT = "badge_count";
        private static final String INTENT_EXTRA_PACKAGENAME = "badge_count_package_name";
        private static final String INTENT_EXTRA_ACTIVITY_NAME = "badge_count_class_name";

        @Override
        public void executeBadge(int badgeCount) {
            final Intent intent = new Intent(INTENT_ACTION);
            intent.putExtra(INTENT_EXTRA_BADGE_COUNT, badgeCount);
            intent.putExtra(INTENT_EXTRA_PACKAGENAME, "dev.mem.rocket.sanya");
            intent.putExtra(INTENT_EXTRA_ACTIVITY_NAME, "dev.mem.rocket.sanya.utils.badgeNotificationBadge");
            AndroidUtilities.runOnUIThread(new Runnable() {
                @Override
                public void run() {
                    try {
                        MyApp.Companion.getInstance().getApplicationContext().sendBroadcast(intent);
                    } catch (Exception ignore) {

                    }
                }
            });
        }

        @Override
        public List<String> getSupportLaunchers() {
            return Arrays.asList(
                    "fr.neamar.kiss",
                    "com.quaap.launchtime",
                    "com.quaap.launchtime_official"
            );
        }
    }


    static {
        /*BADGERS.add(AdwHomeBadger.class);
        BADGERS.add(ApexHomeBadger.class);
        BADGERS.add(NewHtcHomeBadger.class);
        BADGERS.add(NovaHomeBadger.class);
        BADGERS.add(SonyHomeBadger.class);*/
        BADGERS.add(XiaomiHomeBadger.class);
       /* BADGERS.add(AsusHomeBadger.class);
        BADGERS.add(HuaweiHomeBadger.class);
        BADGERS.add(OPPOHomeBader.class);
        BADGERS.add(SamsungHomeBadger.class);
        BADGERS.add(ZukHomeBadger.class);
        BADGERS.add(VivoHomeBadger.class);*/
    }

    public static boolean applyCount(int badgeCount) {
        try {
            if (badger == null && !initied) {
                initBadger();
                initied = true;
            }
            if (badger == null) {
                return false;
            }
            badger.executeBadge(badgeCount);
            return true;
        } catch (Throwable e) {
            return false;
        }
    }

    private static boolean initBadger() {
        Context context = MyApp.Companion.getInstance().getApplicationContext();
        Intent launchIntent = context.getPackageManager().getLaunchIntentForPackage(context.getPackageName());
        if (launchIntent == null) {
            return false;
        }

        componentName = launchIntent.getComponent();

        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        ResolveInfo resolveInfo = context.getPackageManager().resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY);
        if (resolveInfo != null) {
            String currentHomePackage = resolveInfo.activityInfo.packageName;
            for (Class<? extends Badger> b : BADGERS) {
                Badger shortcutBadger = null;
                try {
                    shortcutBadger = b.newInstance();
                } catch (Exception ignored) {
                }
                if (shortcutBadger != null && shortcutBadger.getSupportLaunchers().contains(currentHomePackage)) {
                    badger = shortcutBadger;
                    break;
                }
            }
            if (badger != null) {
                return true;
            }
        }

        List<ResolveInfo> resolveInfos = context.getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        if (resolveInfos != null) {
            for (int a = 0; a < resolveInfos.size(); a++) {
                resolveInfo = resolveInfos.get(a);
                String currentHomePackage = resolveInfo.activityInfo.packageName;

                for (Class<? extends Badger> b : BADGERS) {
                    Badger shortcutBadger = null;
                    try {
                        shortcutBadger = b.newInstance();
                    } catch (Exception ignored) {
                    }
                    if (shortcutBadger != null && shortcutBadger.getSupportLaunchers().contains(currentHomePackage)) {
                        badger = shortcutBadger;
                        break;
                    }
                }
                if (badger != null) {
                    break;
                }
            }
        }

        if (badger == null) {
            if (Build.MANUFACTURER.equalsIgnoreCase("Xiaomi")) {
                badger = new XiaomiHomeBadger();
            } else if (Build.MANUFACTURER.equalsIgnoreCase("ZUK")) {
                //badger = new ZukHomeBadger();
            } else if (Build.MANUFACTURER.equalsIgnoreCase("OPPO")) {
               // badger = new OPPOHomeBader();
            } else if (Build.MANUFACTURER.equalsIgnoreCase("VIVO")) {
               // badger = new VivoHomeBadger();
            } else {
                //badger = new DefaultBadger();
            }
        }

        return true;
    }

    private static boolean canResolveBroadcast(Intent intent) {
        PackageManager packageManager = MyApp.Companion.getInstance().getApplicationContext().getPackageManager();
        List<ResolveInfo> receivers = packageManager.queryBroadcastReceivers(intent, 0);
        return receivers != null && receivers.size() > 0;
    }

    public static void close(Cursor cursor) {
        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }
    }

    public static void closeQuietly(Closeable closeable) {
        try {
            if (closeable != null) {
                closeable.close();
            }
        } catch (Throwable ignore) {

        }
    }
}
