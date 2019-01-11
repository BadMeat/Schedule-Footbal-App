package com.example.user.sceball.fragment

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.InputType
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.*
import com.example.user.sceball.*
import com.example.user.sceball.adapter.TeamAdapter
import com.example.user.sceball.model.League
import com.example.user.sceball.model.Team
import com.example.user.sceball.model.TeamView
import com.example.user.sceball.presenter.SearchTeamPresenter
import com.google.gson.Gson
import org.jetbrains.anko.*
import org.jetbrains.anko.design.snackbar
import org.jetbrains.anko.recyclerview.v7.recyclerView
import org.jetbrains.anko.support.v4.ctx
import org.jetbrains.anko.support.v4.intentFor
import org.jetbrains.anko.support.v4.onRefresh
import org.jetbrains.anko.support.v4.swipeRefreshLayout

class SearchTeamFragment : Fragment(), AnkoComponent<Context>, TeamView, SearchView.OnQueryTextListener {

    private var teamList: MutableList<Team> = mutableListOf()
    private lateinit var presenter: SearchTeamPresenter
    private lateinit var adapter: TeamAdapter
    private lateinit var recycleView: RecyclerView
    private var teamName: String = ""
    private lateinit var button: Button
    private lateinit var textTeamName: EditText
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var progresBar: ProgressBar
    private lateinit var searchView: SearchView

    override fun createView(ui: AnkoContext<Context>): View = with(ui) {
        linearLayout {
            orientation = LinearLayout.VERTICAL
            topPadding = dip(16)
            leftPadding = dip(16)
            rightPadding = dip(16)
            lparams(matchParent, wrapContent) {
                margin = 40
            }
            linearLayout {
                weightSum = 3f
                textTeamName = editText {
                    maxLines = 1
                    inputType = InputType.TYPE_CLASS_TEXT
                }.lparams(0, wrapContent, 2f)
                button = button {
                    text = getString(R.string.cari)
                }.lparams(0, wrapContent, 1f)
            }

            swipeRefreshLayout = swipeRefreshLayout {
                setColorSchemeResources(
                    R.color.colorAccent,
                    android.R.color.holo_green_light,
                    android.R.color.holo_orange_light,
                    android.R.color.holo_red_light
                )
                relativeLayout {
                    recycleView = recyclerView {
                        lparams(matchParent, wrapContent)
                        layoutManager = LinearLayoutManager(ctx)
                    }
                    progresBar = progressBar {
                    }.lparams {
                        centerHorizontally()
                    }
                }
            }
        }
    }

    override fun onPrepareOptionsMenu(menu: Menu?) {
        val myMenu = menu?.findItem(R.id.action_search)
        searchView = myMenu?.actionView as SearchView
        searchView.setOnQueryTextListener(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        setHasOptionsMenu(true)
        return createView(AnkoContext.create(ctx))
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        if (isNetworkAvailable(context)) {
            adapter = TeamAdapter(teamList) {
                startActivity(
                    intentFor<PlayerActivity>(
                        "teamName" to it.strTeam,
                        "teamId" to it.idTeam
                    )
                )
            }
            recycleView.adapter = adapter
            val gson = Gson()
            val request = ApiRepository()
            presenter = SearchTeamPresenter(this, request, gson)
            presenter.getTeamByName(null)
            button.setOnClickListener {
                teamName = textTeamName.text.toString()
                presenter.getTeamByName(teamName)
                val aw = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                aw.hideSoftInputFromWindow(view?.windowToken, 0)
                textTeamName.text.clear()
            }
            swipeRefreshLayout.onRefresh {
                presenter.getTeamByName(teamName)
            }
        } else {
            swipeRefreshLayout.snackbar(resources.getString(R.string.noconection))
            progresBar.invisible()
        }
    }

    override fun showLoading() {
        progresBar.visible()
    }

    override fun hideLoading() {
        progresBar.invisible()
    }

    override fun showTeam(data: List<Team>) {
        swipeRefreshLayout.isRefreshing = false
        teamList.clear()
        for (e: Team in data) {
            if (e.strSport.equals("Soccer")) {
                teamList.add(e)
            }
        }
        adapter.notifyDataSetChanged()
    }

    override fun showAllLeague(data: List<League>) {

    }

    override fun onQueryTextSubmit(p0: String?): Boolean {
        return false
    }

    override fun onQueryTextChange(p0: String?): Boolean {
        adapter.filter.filter(p0)
        return true
    }
}