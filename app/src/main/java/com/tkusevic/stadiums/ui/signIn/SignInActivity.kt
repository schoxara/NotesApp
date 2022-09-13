package com.tkusevic.stadiums.ui.signIn

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.tkusevic.stadiums.R
import com.tkusevic.stadiums.App
import com.tkusevic.stadiums.commons.constants.EMAIL_ERROR
import com.tkusevic.stadiums.commons.constants.PASSWORD_ERROR
import com.tkusevic.stadiums.commons.constants.RESPONSE_OK_201
import com.tkusevic.stadiums.commons.extensions.hide
import com.tkusevic.stadiums.commons.extensions.onClick
import com.tkusevic.stadiums.commons.extensions.show
import com.tkusevic.stadiums.commons.utils.checkEmailEmpty
import com.tkusevic.stadiums.commons.utils.checkPasswordEmpty
import com.tkusevic.stadiums.commons.utils.isValidEmail
import com.tkusevic.stadiums.data.model.LoginResponse
import com.tkusevic.stadiums.data.model.request.LoginRequest
import com.tkusevic.stadiums.preferences.PreferencesHelperImpl
import com.tkusevic.stadiums.ui.registration.RegistrationActivity
import com.tkusevic.stadiums.ui.stadiums.NotesActivity
import hr.ferit.matijavrabelj.corona.networking.BackendFactory
import kotlinx.android.synthetic.main.activity_sign_in.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignInActivity : AppCompatActivity(), SignInView {

    private val interactor = BackendFactory.getNotesInteractor()

    private val prefs: PreferencesHelperImpl by lazy {
        PreferencesHelperImpl(App.prefs)
    }

    override

    fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)
        initListeners()
    }

    private fun initListeners() {
        signIn.onClick {
            onSignInClick(
                emailSignIn.text.toString(),
                passwordSignIn.text.toString(),
            )
        }
        goToRegistration.onClick { startActivity(Intent(this, RegistrationActivity::class.java)) }
    }

    // logic functions
    private fun onSignInClick(email: String, password: String) {
        this.showProgressAndHideOther()
        if (!email.isEmpty() && !password.isEmpty() && password.length > 5) {
            tryToSignIn(email, password)
        } else {
            this.hideProgressAndShowOther()
        }
    }

    private fun tryToSignIn(email: String, password: String) {
        this.checkInputEmpty(email, password);
        val body = LoginRequest(username = email, password)

        interactor.login(body, getLoginCallback())
    }


    private fun getLoginCallback(): Callback<LoginResponse> = object :
        Callback<LoginResponse> {
        override fun onFailure(call: Call<LoginResponse>?, t: Throwable?) {
            // TODO
        }

        override fun onResponse(
            call: Call<LoginResponse>?,
            response: Response<LoginResponse>
        ) {
            if (response.isSuccessful) {
                when (response.code()) {
                    RESPONSE_OK_201 -> onItemsReceived(response)
                    else -> { //TODO }
                    }
                }
            }
        }
    }

    private fun onItemsReceived(response: Response<LoginResponse>) {
        response.body()?.run {
            Log.d("TOMKIC", this.toString())
            prefs.saveId(this.userId)
            prefs.setUserToken(this.access_token)
            startStadiumActivity(this)
            hideProgressAndShowOther()
        }
    }


    private fun checkInputEmpty(email: String, password: String) {
        if (checkEmailEmpty(email.trim()) || !isValidEmail(email.trim()))
            this.showEmailError()
        else this.hideEmailError()

        if (checkPasswordEmpty(password.trim()) || password.trim().length < 6)
            this.showPasswordError()
        else this.hidePasswordError()
    }

    // view functions
    override fun hidePasswordError() {
        layoutPasswordSign.isErrorEnabled = false
    }

    override fun hideEmailError() {
        layoutEmailSign.isErrorEnabled = false
    }

    override fun showPasswordError() {
        layoutPasswordSign.error = PASSWORD_ERROR
    }

    override fun showEmailError() {
        layoutEmailSign.error = EMAIL_ERROR
    }

    override fun showProgressAndHideOther() {
        progressSign.show()
        layoutWithoutImageSign.hide()
    }

    override fun hideProgressAndShowOther() {
        progressSign.hide()
        layoutWithoutImageSign.show()
    }

    override fun startStadiumActivity(user: LoginResponse) {
        val intent = Intent(this, NotesActivity::class.java)
        startActivity(intent)
        finish()
    }

    override fun showMessage(message: String) =
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}
