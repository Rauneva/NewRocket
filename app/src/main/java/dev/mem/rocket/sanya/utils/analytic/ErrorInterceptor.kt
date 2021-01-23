package dev.mem.rocket.sanya.utils.analytic

import com.amplitude.api.Amplitude
import org.json.JSONException
import org.json.JSONObject

object ErrorInterceptor {

    private const val MESSAGE = "mes"
    private const val TYPE = "type"
    private const val EXCEPTION = "exception"

    private const val remote = "remote"
    private const val looper = "looper"
    private const val packages = "packages"
    private const val kill_all = "kill_all"
    private const val ref = "ref"
    private const val default_prem = "default_prem"


    fun trackRemoteError(mes : String) {
        val eventProperties = JSONObject()
        try {
            eventProperties.put(MESSAGE, mes)
            eventProperties.put(TYPE, remote)
        } catch (exception: JSONException) {
            exception.printStackTrace()
        }
        Amplitude.getInstance().logEvent(EXCEPTION, eventProperties)
    }

    fun trackLooperError(mes : String) {
        val eventProperties = JSONObject()
        try {
            eventProperties.put(MESSAGE, mes)
            eventProperties.put(TYPE, looper)
        } catch (exception: JSONException) {
            exception.printStackTrace()
        }
        Amplitude.getInstance().logEvent(EXCEPTION, eventProperties)
    }

    fun trackPackagesError(mes : String) {
        val eventProperties = JSONObject()
        try {
            eventProperties.put(MESSAGE, mes)
            eventProperties.put(TYPE, packages)
        } catch (exception: JSONException) {
            exception.printStackTrace()
        }
        Amplitude.getInstance().logEvent(EXCEPTION, eventProperties)
    }

    fun trackKillError(mes : String) {
        val eventProperties = JSONObject()
        try {
            eventProperties.put(MESSAGE, mes)
            eventProperties.put(TYPE, kill_all)
        } catch (exception: JSONException) {
            exception.printStackTrace()
        }
        Amplitude.getInstance().logEvent(EXCEPTION, eventProperties)
    }

    fun trackInstallRefBug(mes : String) {
        val eventProperties = JSONObject()
        try {
            eventProperties.put(MESSAGE, mes)
            eventProperties.put(TYPE, ref)
        } catch (exception: JSONException) {
            exception.printStackTrace()
        }
        Amplitude.getInstance().logEvent(EXCEPTION, eventProperties)
    }


    fun trackDefaultPrem(mes : String) {
        val eventProperties = JSONObject()
        try {
            eventProperties.put(MESSAGE, mes)
            eventProperties.put(TYPE, default_prem)
        } catch (exception: JSONException) {
            exception.printStackTrace()
        }
        Amplitude.getInstance().logEvent(EXCEPTION, eventProperties)
    }
}