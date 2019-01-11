package com.example.user.sceball.fragment

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.*
import com.example.user.sceball.*
import com.example.user.sceball.adapter.PassAdapter
import com.example.user.sceball.model.MatchEvent
import com.example.user.sceball.model.SearchMatchView
import com.example.user.sceball.presenter.SearchPresenter
import com.google.gson.Gson
import org.jetbrains.anko.*
import org.jetbrains.anko.design.snackbar
import org.jetbrains.anko.recyclerview.v7.recyclerView
import org.jetbrains.anko.support.v4.ctx
import org.jetbrains.anko.support.v4.intentFor
import org.jetbrains.anko.support.v4.onRefresh
import org.jetbrains.anko.support.v4.swipeRefreshLayout

/**
 * Created by Bencoleng on 11/11/2018.
 */
class SearchMatchFragment : Fragment(), AnkoComponent<Context>, SearchMatchView, SearchView.OnQueryTextListener {

    private lateinit var progressBar: ProgressBar

    private var passEvent: MutableList<MatchEvent> = mutableListOf()
    private lateinit var passPreseneter: SearchPresenter
    private lateinit var passAdapter: PassAdapter
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var listEvent: RecyclerView
    private var passKey: String? = ""
    private lateinit var button: Button
    private lateinit var editText: EditText
    private lateinit var searchView: SearchView

    override fun showLoading() {
        progressBar.visible()
    }

    override fun hideLoading() {
        progressBar.invisible()
    }

    override fun onPrepareOptionsMenu(menu: Menu?) {
        val myMenu = menu?.findItem(R.id.action_search)
        searchView = myMenu?.actionView as SearchView
        searchView.setOnQueryTextListener(this)
    }

    override fun showSearchEvent(data: List<MatchEvent>) {
        swipeRefreshLayout.isRefreshing = false
        passEvent.clear()
        if (data.isEmpty()) {
            passAdapter.notifyDataSetChanged()
            passKey = ""
            swipeRefreshLayout.snackbar("Jadwal Kosong").show()
        } else {
            for (e: MatchEvent in data) {
                if (e.strSport.equals("Soccer", true)) {
                    passEvent.add(e)
                }
            }
        }
        passAdapter.notifyDataSetChanged()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        setHasOptionsMenu(true)
        super.onActivityCreated(savedInstanceState)

        if (isNetworkAvailable(context)) {
            passAdapter = PassAdapter(passEvent) {
                startActivity(
                    intentFor<DetailActivity>(
                        "idHome" to it.idHome,
                        "idAway" to it.idAway,
                        "idEvent" to it.idEvent
                    )
                )
            }

            listEvent.adapter = passAdapter

            val request = ApiRepository()
            val gson = Gson()
            passPreseneter = SearchPresenter(this, request, gson)
            passPreseneter.getSearchEvent(passKey)

            button.setOnClickListener {
                passKey = editText.text.toString()
                passPreseneter.getSearchEvent(passKey)
                val aw = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                aw.hideSoftInputFromWindow(view?.windowToken, 0)
            }

            swipeRefreshLayout.onRefresh {
                passPreseneter.getSearchEvent(passKey)
            }
        }else{
            swipeRefreshLayout.snackbar(resources.getString(R.string.noconection))
            progressBar.invisible()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return createView(AnkoContext.create(ctx))
    }

    override fun createView(ui: AnkoContext<Context>): View = with(ui) {

        linearLayout {
            lparams(width = matchParent, height = wrapContent)
            orientation = LinearLayout.VERTICAL
            topPadding = dip(16)
            leftPadding = dip(16)
            rightPadding = dip(16)

            linearLayout {
                editText = editText {

                }.lparams(0, wrapContent) {
                    weight = 1f
                }
                button = button {
                    text = getString(R.string.cari)
                }.lparams(0, wrapContent) {
                    weight = 0.5f
                }
            }.lparams(matchParent, wrapContent)



            swipeRefreshLayout = swipeRefreshLayout {
                setColorSchemeResources(
                    com.example.user.sceball.R.color.colorAccent,
                    android.R.color.holo_green_light,
                    android.R.color.holo_orange_light,
                    android.R.color.holo_red_light
                )

                relativeLayout {
                    lparams(width = matchParent, height = wrapContent)

                    listEvent = recyclerView {
                        lparams(width = matchParent, height = wrapContent)
                        layoutManager = LinearLayoutManager(ctx)
                    }

                    progressBar = progressBar {
                    }.lparams {
                        centerHorizontally()
//                        visibility = View.GONE
                    }
                }
            }
        }
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        return false
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        passAdapter.filter.filter(newText)
        return true
    }
}