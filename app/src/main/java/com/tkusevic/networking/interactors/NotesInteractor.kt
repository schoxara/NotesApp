package hr.ferit.matijavrabelj.corona.networking.interactors

import com.tkusevic.stadiums.data.model.LoginResponse
import com.tkusevic.stadiums.data.model.Notes
import com.tkusevic.stadiums.data.model.RegisterResponse
import com.tkusevic.stadiums.data.model.request.CreateNoteRequest
import com.tkusevic.stadiums.data.model.request.LoginRequest
import com.tkusevic.stadiums.data.model.request.RegisterRequest
import com.tkusevic.stadiums.data.model.request.UpdateNoteRequest
import hr.ferit.matijavrabelj.corona.model.response.AddNoteResponse
import retrofit2.Callback

interface NotesInteractor {

    fun getNotesResponse(getNotesResponse: Callback<MutableList<Notes>>)

    fun getMyNotes(getNotesResponse: Callback<MutableList<Notes>>)

    fun getNotesByid(id: String, getNotesResponse: Callback<Notes>)

    fun deleteNote(id: String, getNotesResponse: Callback<Notes>)

    fun createNote(body: CreateNoteRequest, getNotesResponse: Callback<AddNoteResponse>)

    fun updateNote(id: String,updateReq: UpdateNoteRequest, getNotesResponse: Callback<Notes>)

    fun login(body: LoginRequest, getLoginResponse: Callback<LoginResponse>)

    fun register(body: RegisterRequest, getRegisterResponse: Callback<RegisterResponse>)
}