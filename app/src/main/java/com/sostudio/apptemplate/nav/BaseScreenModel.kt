package com.sostudio.apptemplate.nav

import android.util.Log
import androidx.annotation.CallSuper
import cafe.adriel.voyager.core.model.ScreenModel

interface BaseScreenModel : ScreenModel {

    @CallSuper
    override fun onDispose() {
        super.onDispose()
        Log.v(this.toString(), "onDispose called")
    }
}
