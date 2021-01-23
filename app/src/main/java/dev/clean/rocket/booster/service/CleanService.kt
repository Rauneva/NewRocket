package dev.clean.rocket.booster.service

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.AccessibilityServiceInfo
import android.app.ActivityOptions
import android.content.Intent
import android.os.Build
import android.os.Handler
import android.provider.Settings
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo
import dev.clean.rocket.booster.R

class CleanService : AccessibilityService() {
    private var cache_localize = "cache"
    private var ok_localize = "ok"
    private var free = "free"
    private var used_apps = "used apps"
    private var remove = "remove"
    private var junk = "100"
    private var cleanCacheClicked = false
    private var freeClicked = false
    private var freeClicked2 = false
    private var appClicked = false
    private var scroldown = false

    private var internalStorageClicked = false
    private var storage = "internal storage"


    private var handleReturnToHome: Handler? = null
    private var returnToHome: Runnable? = null

    override fun onServiceConnected() {
        val info = serviceInfo
        info.eventTypes = AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED or
                AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED or AccessibilityEvent.TYPE_VIEW_SCROLLED
        info.packageNames = arrayOf("com.android.settings")
        info.feedbackType = AccessibilityServiceInfo.FEEDBACK_GENERIC
        info.notificationTimeout = 100
        this.serviceInfo = info
        val intent = Intent()
        intent.setClassName("com.clean.emptyrocket", "com.clean.emptyrocket.Scanning_Junk")
        intent.putExtra("fromService", true)
        intent.action = Intent.ACTION_MAIN
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP or
                Intent.FLAG_ACTIVITY_NO_ANIMATION or Intent.FLAG_ACTIVITY_NO_HISTORY)
        this.application.startActivity(intent)
    }

    override fun onStartCommand(prevIntent: Intent?, flags: Int, startId: Int): Int {
        if (prevIntent?.getStringExtra("junk") != null) {
            junk = prevIntent.getStringExtra("junk")
        }
        scroldown = false
        cleanCacheClicked = false
        freeClicked = false
        freeClicked2 = false
        appClicked = false
        ok_localize = getString(R.string.ok)
        cache_localize = getString(R.string.cache)

        used_apps = getString(R.string.used_apps)
        free = getString(R.string.free)
        remove = getString(R.string.remove)

        storage = getString(R.string.internal_storage)

        val intent = Intent(Settings.ACTION_MEMORY_CARD_SETTINGS)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP or
                Intent.FLAG_ACTIVITY_NO_ANIMATION or Intent.FLAG_ACTIVITY_NO_HISTORY)
        val options = ActivityOptions.makeCustomAnimation(this@CleanService, 0, 0)
        this@CleanService.startActivity(intent, options.toBundle())
        //TODO: за 3 секунды сервис должен успеть завершить работу.
        /*returnToHome = Runnable {
            cleanCacheClicked = false
            internalStorageClicked = false
            freeClicked = false
            freeClicked2 = false
            appClicked = false
            scroldown = false
            val intent = Intent()
            intent.putExtra("junk", junk)
            intent.setClassName("com.clean.emptyrocket", "com.clean.emptyrocket.Scanning_Junk")
            intent.action = Intent.ACTION_MAIN
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP or
                    Intent.FLAG_ACTIVITY_NO_ANIMATION or Intent.FLAG_ACTIVITY_NO_HISTORY)
            intent.putExtra("isScanned", true)
            this@CleanService.application.startActivity(intent)
        }
        handleReturnToHome = Handler()
        handleReturnToHome!!.postDelayed(returnToHome, 1000)*/
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent) {
        val nodeInfo = event.source
        if (nodeInfo != null) {
            val eventType = event.eventType
            if (eventType == AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED || eventType == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED
                    || eventType == AccessibilityEvent.TYPE_VIEW_SCROLLED) {
                if (android.os.Build.VERSION.SDK_INT <= Build.VERSION_CODES.N_MR1) {
                    if (android.os.Build.VERSION.SDK_INT == Build.VERSION_CODES.M) {
                        if (!internalStorageClicked)
                            internalStorageClicked = tryAction(nodeInfo, storage, AccessibilityNodeInfo.ACTION_CLICK)
                        else if (!scroldown)
                            scroldown = scroll(nodeInfo)
                        else if (!cleanCacheClicked) {
                            cleanCacheClicked = tryAction(nodeInfo, cache_localize, AccessibilityNodeInfo.ACTION_CLICK)
                        } else if (tryAction(nodeInfo, ok_localize, AccessibilityNodeInfo.ACTION_CLICK)) {
                            startScaningJunk()
                        }
                    } else {
                        if (!scroldown)
                            scroldown = scroll(nodeInfo)
                        else if (!cleanCacheClicked) {
                            cleanCacheClicked = tryAction(nodeInfo, cache_localize, AccessibilityNodeInfo.ACTION_CLICK)
                        } else if (tryAction(nodeInfo, ok_localize, AccessibilityNodeInfo.ACTION_CLICK)) {
                            startScaningJunk()
                        }
                    }
                } else if (!freeClicked) {
                    freeClicked = tryAction(nodeInfo, free, AccessibilityNodeInfo.ACTION_CLICK)
                } /*else if (!appClicked) {
                    appClicked = tryAction(nodeInfo, used_apps, AccessibilityNodeInfo.ACTION_CLICK);
                } else if(!freeClicked2) {
                    freeClicked2 = tryAction(nodeInfo, free, AccessibilityNodeInfo.ACTION_CLICK);
                }
                TODO: нет возможности нажать на очистку всех элементов, поэтому ждем пока пользователь выберет приложения и
                TODO: нажмет на кнопку free
                */
                else if (tryAction(nodeInfo, remove, AccessibilityNodeInfo.ACTION_CLICK)) {
                }
            }
        }
    }

    private fun startScaningJunk() {
        scroldown = false
        cleanCacheClicked = false
        internalStorageClicked = false
        freeClicked = false
        freeClicked2 = false
        appClicked = false
        handleReturnToHome!!.removeCallbacks(returnToHome)
        val intent = Intent()
        intent.putExtra("junk", junk)
        intent.setClassName("com.clean.emptyrocket", "com.clean.emptyrocket.Scanning_Junk")
        intent.action = Intent.ACTION_MAIN
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP or
                Intent.FLAG_ACTIVITY_NO_ANIMATION or Intent.FLAG_ACTIVITY_NO_HISTORY)
        intent.putExtra("isScanned", true)
        this.application.startActivity(intent)
    }


    override fun onDestroy() {
        super.onDestroy()
    }


    override fun onInterrupt() {

    }

    /*private boolean scroll(AccessibilityNodeInfo nodeInfo, int direction) {
        if(nodeInfo != null) {
            int childCounts = nodeInfo.getChildCount();
            if(nodeInfo.getText() != null && nodeInfo.getText().toString().toLowerCase().contains(s) && nodeInfo.isClickable()) {
                nodeInfo.performAction(direction);
                return true;
            }
            for(int i = 0; i < childCounts; i++) {

                AccessibilityNodeInfo childInfo =  nodeInfo.getChild(i);
                if (childInfo == null) continue;
                if() {
                    if(childInfo.isClickable()) {
                        childInfo.performAction(direction);
                        return true;
                    }
                    if(nodeInfo.isClickable()) {
                        nodeInfo.performAction(direction);
                        return true;
                    }
                }
                if(tryAction(childInfo, s, Action)) return true;
            }
        }
        return false;
    }*/

    private fun getListItemNodeInfo(source: AccessibilityNodeInfo): AccessibilityNodeInfo? {
        var current = source
        while (true) {
            val parent = current.parent ?: return null
            if (TASK_LIST_VIEW_CLASS_NAME == parent.className) {
                return current
            }
            // NOTE: Recycle the infos.
            val oldCurrent = current
            current = parent
            oldCurrent.recycle()
        }
    }


    private fun scroll(nodeInfo: AccessibilityNodeInfo?): Boolean {
        if (nodeInfo != null) {
            val childCounts = nodeInfo.childCount
            if (nodeInfo.isScrollable) {
                return nodeInfo.performAction(AccessibilityNodeInfo.ACTION_SCROLL_FORWARD)
            }
            for (i in 0 until childCounts) {

                val childInfo = nodeInfo.getChild(i) ?: continue
                if (childInfo.isScrollable) {
                    return nodeInfo.performAction(AccessibilityNodeInfo.ACTION_SCROLL_FORWARD)
                }
                if (scroll(childInfo)) return true
            }
        }
        return false
    }

    private fun tryAction(nodeInfo: AccessibilityNodeInfo?, s: String, Action: Int): Boolean {
        if (nodeInfo != null) {
            val childCounts = nodeInfo.childCount
            if (nodeInfo.text != null && nodeInfo.text.toString().toLowerCase().contains(s.toLowerCase()) && nodeInfo.isClickable) {
                nodeInfo.performAction(Action)
                return true
            }
            for (i in 0 until childCounts) {

                val childInfo = nodeInfo.getChild(i) ?: continue
                val itemText = childInfo.text
                if (itemText != null && itemText.toString().toLowerCase().contains(s.toLowerCase())) {
                    if (childInfo.isClickable) {
                        childInfo.performAction(Action)
                        return true
                    }
                    if (nodeInfo.isClickable) {
                        nodeInfo.performAction(Action)
                        return true
                    }
                }
                if (tryAction(childInfo, s, Action)) return true
            }
        }
        return false
    }

    private fun myTryClickClear(nodeInfo: AccessibilityNodeInfo?): Boolean {
        if (nodeInfo != null) {
            val childCounts = nodeInfo.childCount
            if (nodeInfo.text != null && nodeInfo.text.toString().toLowerCase().contains(cache_localize) && nodeInfo.isClickable) {
                nodeInfo.performAction(AccessibilityNodeInfo.ACTION_CLICK)
                cleanCacheClicked = true
                return true
            }
            for (i in 0 until childCounts) {

                val childInfo = nodeInfo.getChild(i) ?: continue
                val itemText = childInfo.text
                if (itemText != null && itemText.toString().toLowerCase().contains(cache_localize)) {
                    if (nodeInfo.isClickable) {
                        nodeInfo.performAction(AccessibilityNodeInfo.ACTION_CLICK)
                        cleanCacheClicked = true
                        return true
                    }
                }
                if (myTryClickClear(childInfo)) return true
            }
        }
        return false
    }

    private fun myTryClickOk(nodeInfo: AccessibilityNodeInfo?): Boolean {
        if (nodeInfo != null) {
            val childCounts = nodeInfo.childCount
            if (nodeInfo.text != null && nodeInfo.text.toString().toLowerCase().contains(ok_localize) && nodeInfo.isClickable) {
                nodeInfo.performAction(AccessibilityNodeInfo.ACTION_CLICK)
                return true
            }
            for (i in 0 until childCounts) {

                val childInfo = nodeInfo.getChild(i) ?: continue
                val itemText = childInfo.text
                if (itemText != null && itemText.toString().toLowerCase().contains(ok_localize)) {
                    if (nodeInfo.isClickable) {
                        nodeInfo.performAction(AccessibilityNodeInfo.ACTION_CLICK)
                        return true
                    }
                }
                if (myTryClickOk(childInfo)) return true
            }
        }
        return false
    }

    companion object {
        private val TASK_LIST_VIEW_CLASS_NAME = "com.example.android.apis.accessibility.TaskListView"
    }
}
