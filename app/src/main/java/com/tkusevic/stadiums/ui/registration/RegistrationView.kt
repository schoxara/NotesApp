package com.tkusevic.stadiums.ui.registration

import com.tkusevic.stadiums.data.model.RegisterResponse

interface RegistrationView {

    fun showEmailError()

    fun showPasswordError()

    fun hideEmailError()

    fun hidePasswordError()

    fun showProgressAndHideOther()

    fun hideProgressAndShowOther()

    fun showNameError()

    fun hideNameError()

    fun showMessage(message: String)

    fun startSignIn()

    fun startLoginScreen(user: RegisterResponse)
}