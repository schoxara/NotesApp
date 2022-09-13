package hr.ferit.matijavrabelj.corona.networking.interactors

import com.tkusevic.stadiums.data.model.LoginResponse
import com.tkusevic.stadiums.data.model.Notes
import com.tkusevic.stadiums.data.model.RegisterResponse
import com.tkusevic.stadiums.data.model.request.CreateNoteRequest
import com.tkusevic.stadiums.data.model.request.LoginRequest
import com.tkusevic.stadiums.data.model.request.RegisterRequest
import com.tkusevic.stadiums.data.model.request.UpdateNoteRequest
import hr.ferit.matijavrabelj.corona.model.response.AddNoteResponse
import hr.ferit.matijavrabelj.corona.networking.NotesApiService
import retrofit2.Callback

class NotesInteractorImpl(private val apiService: NotesApiService) : NotesInteractor {
    override fun getNotesResponse(getNotesResponse: Callback<MutableList<Notes>>) {
        this.apiService.getNotes().enqueue(getNotesResponse)
    }

    override fun getMyNotes(getNotesResponse: Callback<MutableList<Notes>>) {
        this.apiService.getMyNotes().enqueue(getNotesResponse)
    }

    override fun getNotesByid(id: String, getNotesResponse: Callback<Notes>) {
        this.apiService.getNotesById(id).enqueue(getNotesResponse)
    }

    override fun deleteNote(id: String,getNotesResponse: Callback<Notes>) {
        this.apiService.deleteNote(id).enqueue(getNotesResponse)
    }

    override fun createNote(body: CreateNoteRequest, getNotesResponse: Callback<AddNoteResponse>) {
        this.apiService.createNote(body).enqueue(getNotesResponse)
    }

    override fun updateNote(id: String, updateNoteRequest: UpdateNoteRequest, getNotesResponse: Callback<Notes>) {
        this.apiService.updateNote(id, updateNoteRequest).enqueue(getNotesResponse)
    }

    override fun login(body: LoginRequest, getLoginResponse: Callback<LoginResponse>) {
        this.apiService.login(body).enqueue(getLoginResponse)
    }

    override fun register(
        body: RegisterRequest,
        getRegisterResponse: Callback<RegisterResponse>
    ) {
        this.apiService.register(body).enqueue(getRegisterResponse)
    }

}