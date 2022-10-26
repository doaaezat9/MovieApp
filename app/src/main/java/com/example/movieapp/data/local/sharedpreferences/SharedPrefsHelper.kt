package com.example.movieapp.data.local.sharedpreferences

import android.content.Context
import android.content.SharedPreferences

class SharedPrefsHelper(context: Context) {




    companion object {
        private const val SHARED_PREFS_NAME = "UP_Data"
        const val HOURS_UNTIL_PROMPT = 4
        const val MILLIS_UNTIL_PROMPT = HOURS_UNTIL_PROMPT  * 60 * 60 * 1000
        const val LAST_UPDATE = "LAST_UP"
    }

    private val sharedPreferences: SharedPreferences
    init {
        sharedPreferences = context.getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE)
    }



    fun delete(key: String?) {
        if (sharedPreferences.contains(key)) {
            editor.remove(key).commit()
        }
    }

    fun save(key: String?, value: Any?) {
        val editor = editor
        if (value is Boolean) {
            editor.putBoolean(key, (value as Boolean?)!!)
        } else if (value is Int) {
            editor.putInt(key, (value as Int?)!!)
        } else if (value is Float) {
            editor.putFloat(key, (value as Float?)!!)
        } else if (value is Long) {
            editor.putLong(key, (value as Long?)!!)
        } else if (value is String) {
            editor.putString(key, value as String?)
        } else if (value is Enum<*>) {
            editor.putString(key, value.toString())
        } else if (value != null) {
            throw RuntimeException("Attempting to save non-supported preference")
        }
        editor.commit()
    }

    operator fun <T> get(key: String?): T? {
        return sharedPreferences.all[key] as T?
    }

    operator fun <T> get(key: String?, defValue: T): T {
        val returnValue = sharedPreferences.all[key] as T?
        return returnValue ?: defValue
    }

    private fun has(key: String): Boolean {
        return sharedPreferences.contains(key)
    }

    fun clearAllData() {
        val editor = editor
        editor.clear().commit()
    }

    private val editor: SharedPreferences.Editor
        private get() = sharedPreferences.edit()


}