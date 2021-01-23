package dev.mem.rocket.sanya.utils.ids

import dev.mem.rocket.sanya.utils.PreferencesProvider
import java.util.*

object Creator {

    fun getId() : String{
        var id = ""
        if (PreferencesProvider.getUserId() == PreferencesProvider.ID_EMPTY){
            PreferencesProvider.setUserId(UUID.randomUUID().toString())
            id = PreferencesProvider.getUserId()!!
        }else{
            id = PreferencesProvider.getUserId()!!
        }
        return id
    }
}