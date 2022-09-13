package com.tkusevic.stadiums.ui.signIn

import com.tkusevic.stadiums.data.model.LoginResponse

interface SignInView {

    fun showEmailError()

    fun showPasswordError()

    fun hideEmailError()

    fun hidePasswordError()

    fun showProgressAndHideOther()

    fun hideProgressAndShowOther()

    fun startStadiumActivity(user: LoginResponse)

    fun showMessage(message: String)
}