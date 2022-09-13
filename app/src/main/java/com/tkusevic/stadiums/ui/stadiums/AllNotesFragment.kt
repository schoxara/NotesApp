package com.tkusevic.stadiums.ui.stadiums

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.tkusevic.stadiums.R
import com.tkusevic.stadiums.commons.constants.NOTES_KEY
import com.tkusevic.stadiums.commons.constants.RESPONSE_OK
import com.tkusevic.stadiums.commons.extensions.hide
import com.tkusevic.stadiums.commons.extensions.onClick
import com.tkusevic.stadiums.commons.extensions.show
import com.tkusevic.stadiums.commons.extensions.toast
import com.tkusevic.stadiums.data.model.Notes
import com.tkusevic.stadiums.ui.listeners.OnItemClickListener
import com.tkusevic.stadiums.ui.signIn.SignInActivity
import com.tkusevic.stadiums.ui.notes_details.NotesDetailsActivity
import com.tkusevic.stadiums.ui.stadiums.adapter.AllNotesAdapter
import com.tkusevic.stadiums.ui.stadiums.views.AllNnotesView
import hr.ferit.matijavrabelj.corona.networking.BackendFactory
import kotlinx.android.synthetic.main.fragment_all_notes.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AllNotesFragment : Fragment(), OnItemClickListener, AllNnotesView {

    private val interactor = BackendFactory.getNotesInteractor()

    private val adapter by lazy { AllNotesAdapter(this) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_all_notes, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initListeners()
        initRecyclerView()
        getStadiums()
    }

    private fun initListeners() {
        signOutAllStadiums.onClick {
            val intent = Intent(activity, SignInActivity::class.java)
            startActivity(intent)
            activity?.finish()
        }
    }

    private fun initRecyclerView() {
        recyclerViewTopRated.adapter = adapter
        val lm = LinearLayoutManager(activity)
        recyclerViewTopRated.layoutManager = lm
    }

    fun getStadiums(): Unit = run {
        interactor.getNotesResponse(getNotesCallback())
    }

    override fun onNotesClick(stadium: Notes) {
        val bundle = Bundle()
        bundle.putSerializable(NOTES_KEY, stadium)
        val intent = Intent(activity, NotesDetailsActivity::class.java).putExtras(bundle)
        startActivity(intent)
    }

    override fun setNotes(notes: List<Notes>) {
        this.adapter.setItems(notes)
    }

    override fun addNotes(notes: List<Notes>) {
        this.adapter.addItems(notes)
    }

    private fun getNotesCallback(): Callback<MutableList<Notes>> = object :
        Callback<MutableList<Notes>> {
        override fun onFailure(call: Call<MutableList<Notes>>?, t: Throwable?) {
            handleSomethingWentWrong()
        }

        override fun onResponse(call: Call<MutableList<Notes>>?, response: Response<MutableList<Notes>>) {
            if (response.isSuccessful) {
                when (response.code()) {
                    RESPONSE_OK -> onItemsReceived(response.body())
                    else -> handleSomethingWentWrong()
                }
            }
        }
    }

    private fun onItemsReceived(response: MutableList<Notes>?) {
        response?.run {
            adapter.setItems(this)
            if(this.size > 0){
                hideMessageOnScreen()
            }
        }
    }

    // view functions
    private fun handleSomethingWentWrong() {
        this.activity?.toast("Something went wrong!")
    }

    private fun showMessageOnScreen() = noStadiums.show()

    private fun hideMessageOnScreen() = noStadiums.hide()

}

