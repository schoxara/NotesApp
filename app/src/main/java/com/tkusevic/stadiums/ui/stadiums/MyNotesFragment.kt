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
import com.tkusevic.stadiums.data.model.Notes
import com.tkusevic.stadiums.ui.listeners.OnItemClickListener
import com.tkusevic.stadiums.ui.signIn.SignInActivity
import com.tkusevic.stadiums.ui.notes_details.NotesDetailsActivity
import com.tkusevic.stadiums.ui.stadiums.adapter.MyNotesAdapter
import com.tkusevic.stadiums.ui.stadiums.views.MyNotesView
import hr.ferit.matijavrabelj.corona.networking.BackendFactory
import kotlinx.android.synthetic.main.fragment_my_notes.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MyNotesFragment : Fragment(), OnItemClickListener, MyNotesView {

    private val interactor = BackendFactory.getNotesInteractor()

    private val adapter by lazy { MyNotesAdapter(this) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_my_notes, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initListeners()
        initRecycler()
        getNotes()
    }

    fun getNotes() {
        this.interactor.getMyNotes(getNotesCallback())
    }


    private fun getNotesCallback(): Callback<MutableList<Notes>> = object :
        Callback<MutableList<Notes>> {
        override fun onFailure(call: Call<MutableList<Notes>>?, t: Throwable?) {
            // TODO
        }

        override fun onResponse(
            call: Call<MutableList<Notes>>?,
            response: Response<MutableList<Notes>>
        ) {
            if (response.isSuccessful) {
                when (response.code()) {
                    RESPONSE_OK -> onSuccessfulRequest(response.body())
                    else -> { // TODO }
                    }
                }
            }
        }
    }

    private fun initListeners() {
        signOutMyNotes.onClick {
            val intent = Intent(activity, SignInActivity::class.java)
            startActivity(intent)
            activity?.finish()
        }
    }

    private fun initRecycler() {
        recyclerViewMyNotes.adapter = adapter
        val lm = LinearLayoutManager(activity)
        recyclerViewMyNotes.layoutManager = lm

        registerForContextMenu(recyclerViewMyNotes);

    }

    // logic functions
    private fun onSuccessfulRequest(notes: MutableList<Notes>?) {
        if (notes?.toList()?.isEmpty() == true) {
            this.adapter.clearList()
            this.showMessageOnScreen()
        } else {
            notes?.toList().let {
                if (it != null) {
                    adapter.setList(it)
                }
            }
            this.hideMessageOnScreen()
        }
    }

    override fun showMessageOnScreen() = noNotesMyNotes.show()

    override fun hideMessageOnScreen() = noNotesMyNotes.hide()

    override fun onNotesClick(stadium: Notes) {
        val bundle = Bundle()
        bundle.putSerializable(NOTES_KEY, stadium)
        val intent = Intent(activity, NotesDetailsActivity::class.java).putExtras(bundle)
        startActivity(intent)
    }
}