package hr.ferit.matijavrabelj.corona.networking

import android.util.Log
import com.tkusevic.stadiums.App
import com.tkusevic.stadiums.commons.constants.BASE_URL
import com.tkusevic.stadiums.commons.utils.isEmpty
import com.tkusevic.stadiums.preferences.PreferencesHelperImpl
import hr.ferit.matijavrabelj.corona.networking.interactors.NotesInteractor
import hr.ferit.matijavrabelj.corona.networking.interactors.NotesInteractorImpl
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private const val AUTHORIZATION = "Authorization"


object BackendFactory {

    private var retrofit: Retrofit? = null
    private val interceptor = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)

    private val prefs: PreferencesHelperImpl by lazy {
        PreferencesHelperImpl(App.prefs)
    }

    private val httpClient =
        OkHttpClient.Builder()
            .addInterceptor { chain ->
                val request = chain.request().newBuilder()
                    .addHeader("accept", "application/json")
                    .addHeader("Content-Type", "application/json")
                if (!isEmpty(prefs.getUserToken())) {
                    Log.d("TOMKEE2", (!isEmpty(prefs.getUserToken())).toString())
                    Log.d("TOMKEE", prefs.getUserToken())
                    request.addHeader(AUTHORIZATION, "Bearer ${prefs.getUserToken()}")
                }
                chain.proceed(request.build())



            }
            .addInterceptor(interceptor)
            .build()

    private val client: Retrofit? = if (retrofit == null) createRetrofit() else retrofit

    private fun createRetrofit(): Retrofit? {
        retrofit = Retrofit.Builder()
            .client(httpClient)
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        return retrofit
    }

    private fun getService(): NotesApiService = this.client!!.create(NotesApiService::class.java)

    fun getNotesInteractor(): NotesInteractor = NotesInteractorImpl(getService())

}
