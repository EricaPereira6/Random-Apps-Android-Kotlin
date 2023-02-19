package dam_a42356.eventsapp

import android.util.Log

enum class Events(val event: String, val color : Int) {
    CREATE("onCreate", R.color.on_create_bg),
    DESTROY("onDestroy", R.color.normal_methods_bg),
    START("onStart", R.color.normal_methods_bg),
    STOP("onStop", R.color.normal_methods_bg),
    RESTART("onRestart", R.color.normal_methods_bg),
    RESUME("onResume", R.color.normal_methods_bg),
    PAUSE("onPause", R.color.normal_methods_bg),
    SAVEINSTANCESTATE("onSaveInstanceState", R.color.instance_state_bg),
    RESTOREINSTANCESTATE("onRestoreInstanceState", R.color.instance_state_bg),
    SCREENOFF("Screen OFF" , R.color.screen_turned_bg),
    SCREENON("Screen ON", R.color.screen_turned_bg),
    ACTION_SCAN_MODE_CHANGED("ACTION_SCAN_MODE_CHANGED", R.color.bluetooth_bg),
    ACTION_DISCOVERY_STARTED("ACTION_DISCOVERY_STARTED", R.color.bluetooth_bg),
    ACTION_DISCOVERY_FINISHED("ACTION_DISCOVERY_FINISHED", R.color.bluetooth_bg),
    ACTION_FOUND("ACTION_FOUND", R.color.bluetooth_bg),
    ACTION_STATE_CHANGED("ACTION_STATE_CHANGED", R.color.blt_extra_bg),
    SEPARATOR("- - - - - - - - - - - - - ", R.color.separator);


    fun getValue(str : String) : Events {
        for (e in values()){
            if (str == e.event){
                return e
            }
        }
        return SEPARATOR
    }
}
