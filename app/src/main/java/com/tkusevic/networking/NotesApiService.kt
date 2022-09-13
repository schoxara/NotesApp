package hr.ferit.matijavrabelj.corona.networking

import com.tkusevic.stadiums.data.model.LoginResponse
import com.tkusevic.stadiums.data.model.Notes
import com.tkusevic.stadiums.data.model.RegisterResponse
import com.tkusevic.stadiums.data.model.request.CreateNoteRequest
import com.tkusevic.stadiums.data.model.request.LoginRequest
import com.tkusevic.stadiums.data.model.request.RegisterRequest
import com.tkusevic.stadiums.data.model.request.UpdateNoteRequest
import hr.ferit.matijavrabelj.corona.model.response.AddNoteResponse
import retrofit2.Call
import retrofit2.http.*


interface NotesApiService {

    @GET("notes/")
    fun getNotes(): Call<MutableList<Notes>>

    @GET("notes/personal/mine/")
    fun getMyNotes(): Call<MutableList<Notes>>

    @GET("notes/{id}/")
    fun getNotesById(@Path("id") id: String): Call<Notes>

    @DELETE("notes/{id}/")
    fun deleteNote(@Path("id") id: String): Call<Notes>

    @POST("notes/create/")
    fun createNote(@Body body: CreateNoteRequest): Call<AddNoteResponse>

    @PATCH("notes/{id}/")
    fun updateNote(@Path("id") id: String, @Body body: UpdateNoteRequest): Call<Notes>

    @POST("auth/login/")
    fun login(@Body body: LoginRequest): Call<LoginResponse>

    @POST("user/register/")
    fun register(@Body body: RegisterRequest): Call<RegisterResponse>

}