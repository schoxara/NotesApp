package com.tkusevic.stadiums.preferences

import android.content.SharedPreferences
import com.tkusevic.stadiums.commons.constants.PREFS_USER_ID_KEY
import com.tkusevic.stadiums.commons.constants.PREFS_USER_TOKEN

class PreferencesHelperImpl constructor(private val preferences: SharedPreferences) :
    PreferencesHelper {

    override fun getId(): String = preferences.getString(PREFS_USER_ID_KEY, "").toString()

    override fun removeId() = preferences.edit().clear().apply()

    override fun saveId(userId: String) =
        preferences.edit().putString(PREFS_USER_ID_KEY, userId).apply()

    override fun getUserId(): String =
        preferences.getString(PREFS_USER_ID_KEY, "").toString()

    override fun userIdExists(): Boolean = getId().isNotBlank()

    override fun getUserToken() =
        preferences.getString(PREFS_USER_TOKEN, "").toString()

    override fun setUserToken(userToken: String) =
        preferences.edit().putString(PREFS_USER_TOKEN, userToken).apply()
}