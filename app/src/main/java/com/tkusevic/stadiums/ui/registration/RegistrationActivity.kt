package com.tkusevic.stadiums.ui.registration

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.tkusevic.stadiums.R
import com.tkusevic.stadiums.commons.constants.*
import com.tkusevic.stadiums.commons.extensions.hide
import com.tkusevic.stadiums.commons.extensions.onClick
import com.tkusevic.stadiums.commons.extensions.show
import com.tkusevic.stadiums.commons.utils.checkEmailEmpty
import com.tkusevic.stadiums.commons.utils.checkNameEmpty
import com.tkusevic.stadiums.commons.utils.checkPasswordEmpty
import com.tkusevic.stadiums.commons.utils.isValidEmail
import com.tkusevic.stadiums.data.model.RegisterResponse
import com.tkusevic.stadiums.data.model.request.RegisterRequest
import com.tkusevic.stadiums.ui.signIn.SignInActivity
import hr.ferit.matijavrabelj.corona.networking.BackendFactory
import kotlinx.android.synthetic.main.activity_registration.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class RegistrationActivity : AppCompatActivity(), RegistrationView {

    private val interactor = BackendFactory.getNotesInteractor()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)
        initListeners()
    }

    private fun initListeners() {
        registrationBtn.onClick {
            this.onRegistrationClick(
                email.text.toString(), password.text.toString(), name.text.toString()
            )
        }
    }

    // logic functions
    private fun onRegistrationClick(email: String, password: String, name: String) {
        this.showProgressAndHideOther()
        if (!email.isEmpty() && !password.isEmpty() && password.length > 5)
            tryToRegister(email, password, name)
        else
            this.hideProgressAndShowOther()
        chechInputEmpty(email, password, name)
    }

    private fun chechInputEmpty(email: String, password: String, name: String) {
        if (checkEmailEmpty(email.trim()) || !isValidEmail(email.trim())) this.showEmailError() else this.hideEmailError()

        if (checkPasswordEmpty(password.trim()) || password.trim().length < 6) this.showPasswordError() else this.hidePasswordError()

        if (checkNameEmpty(name.trim())) this.showNameError() else this.hideNameError()
    }

    private fun tryToRegister(email: String, password: String, name: String) {
        val body = RegisterRequest(email, name, password, confirm = password)
        interactor.register(body, getRegisterCallback())

    }

    private fun getRegisterCallback(): Callback<RegisterResponse> = object :
        Callback<RegisterResponse> {
        override fun onFailure(call: Call<RegisterResponse>?, t: Throwable?) {
            hideProgressAndShowOther();
            showMessage(t?.message.toString())
        }

        override fun onResponse(
            call: Call<RegisterResponse>?,
            response: Response<RegisterResponse>
        ) {
            if (response.isSuccessful) {
                when (response.code()) {
                    RESPONSE_OK_201 -> onItemsReceived(response)
                    RESPONSE_NOT_OK -> notValidResponse(response.message().toString())
                    else -> {
                        hideProgressAndShowOther();
                        showMessage(response.message())
                    }
                }
            }
            else {
                hideProgressAndShowOther();
                showMessage(response.toString())
            }
        }
    }


    private fun onItemsReceived(response: Response<RegisterResponse>) {
        response.body()?.run {
            startLoginScreen(this)
            hideProgressAndShowOther()
        }
    }

    override fun startLoginScreen(user: RegisterResponse) {
        val intent = Intent(this, SignInActivity::class.java)
        startActivity(intent)
        finish()
    }

    // view functions
    private fun notValidResponse(message: String) {
        hideProgressAndShowOther();
        showMessage(message)
    }

    override fun showProgressAndHideOther() {
        progress.show()
        layoutWithoutImage.hide()
    }

    override fun hideProgressAndShowOther() {
        progress.hide()
        layoutWithoutImage.show()
    }

    override fun showEmailError() {
        layoutEmail.error = EMAIL_ERROR
    }

    override fun hideEmailError() {
        layoutEmail.isErrorEnabled = false
    }

    override fun showPasswordError() {
        layoutPassword.error = PASSWORD_ERROR
    }

    override fun hidePasswordError() {
        layoutPassword.isErrorEnabled = false
    }

    override fun showNameError() {
        layoutName.error = NO_NAME_ERROR
    }

    override fun hideNameError() {
        layoutName.isErrorEnabled = false
    }

    override fun startSignIn() = startActivity(Intent(this, SignInActivity::class.java))

    override fun showMessage(message: String) =
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

