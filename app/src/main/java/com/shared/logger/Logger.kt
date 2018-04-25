package com.shared.logger

import android.util.Log


class Logger {

    companion object {
        fun log(message: String) {
            Log.i("Info", message);
        }
    }



}