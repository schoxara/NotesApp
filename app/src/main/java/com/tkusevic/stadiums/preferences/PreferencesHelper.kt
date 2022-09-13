package com.tkusevic.stadiums.preferences

interface PreferencesHelper {

    fun getId(): String

    fun removeId()

    fun saveId(userId: String)

    fun getUserId(): String

    fun userIdExists(): Boolean

    fun getUserToken(): String

    fun setUserToken(token: String)
}