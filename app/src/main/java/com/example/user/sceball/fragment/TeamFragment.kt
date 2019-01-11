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
import android.widget.*
import com.example.user.sceball.*
import com.example.user.sceball.adapter.TeamAdapter
import com.example.user.sceball.model.League
import com.example.user.sceball.model.Team
import com.example.user.sceball.model.TeamView
import com.example.user.sceball.presenter.TeamPresenter
import com.google.gson.Gson
import org.jetbrains.anko.*
import org.jetbrains.anko.design.snackbar
import org.jetbrains.anko.recyclerview.v7.recyclerView
import org.jetbrains.anko.support.v4.ctx
import org.jetbrains.anko.support.v4.intentFor
import org.jetbrains.anko.support.v4.onRefresh
import org.jetbrains.anko.support.v4.swipeRefreshLayout

/**
 * Created by Bencoleng on 14/11/2018.
 */
class TeamFragment : Fragment(), AnkoComponent<Context>, TeamView, SearchView.OnQueryTextListener {

    /*
     * List
     */
    var teamList: MutableList<Team> = mutableListOf()
    private var leagueList: MutableList<String?> = mutableListOf()
    private var leagueId: MutableList<String?> = mutableListOf()

    private lateinit var presenter: TeamPresenter
    private lateinit var adapter: TeamAdapter
    private lateinit var recycleView: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var spinner: Spinner
    private lateinit var searchView: SearchView

    private var leagueName: String? = ""

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

            val request = ApiRepository()
            val gson = Gson()
            presenter = TeamPresenter(this, request, gson)
            presenter.getLeague()
            swipeRefreshLayout.onRefresh {
                presenter.getTeam(leagueName)
            }
        } else {
            swipeRefreshLayout.snackbar(resources.getString(R.string.noconection))
            progressBar.invisible()
        }

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        setHasOptionsMenu(true)
        return createView(AnkoContext.create(ctx))
    }

    override fun onPrepareOptionsMenu(menu: Menu?) {
        val myMenu = menu?.findItem(R.id.action_search)
        searchView = myMenu?.actionView as SearchView
        searchView.setOnQueryTextListener(this)
    }

    override fun createView(ui: AnkoContext<Context>): View = with(ui) {
        linearLayout {
            lparams(width = matchParent, height = wrapContent)
            orientation = LinearLayout.VERTICAL
            topPadding = dip(16)
            leftPadding = dip(16)
            rightPadding = dip(16)


            spinner = spinner {
                background = ctx.getDrawable(R.drawable.gradient_spinner)
            }.lparams(matchParent, wrapContent)

            swipeRefreshLayout = swipeRefreshLayout {
                setColorSchemeResources(
                    R.color.colorAccent,
                    android.R.color.holo_green_light,
                    android.R.color.holo_orange_light,
                    android.R.color.holo_red_light
                )

                relativeLayout {
                    lparams(width = matchParent, height = wrapContent)

                    recycleView = recyclerView {
                        id = R.id.nextfrag
                        lparams(width = matchParent, height = wrapContent)
                        layoutManager = LinearLayoutManager(ctx)
                    }



                    progressBar = progressBar {
                    }.lparams {
                        centerHorizontally()
                    }
                }
            }
        }
    }

    override fun showLoading() {
        progressBar.visible()
    }

    override fun hideLoading() {
        progressBar.invisible()
    }

    override fun showTeam(data: List<Team>) {
        swipeRefreshLayout.isRefreshing = false
        teamList.clear()
        teamList.addAll(data)
        adapter.notifyDataSetChanged()
    }

    override fun showAllLeague(data: List<League>) {
        leagueList.clear()
        for (e: League in data) {
            if (e.strSport.equals("Soccer", true) && !e.strLeague.equals("_No League", true)) {
                leagueList.add(e.strLeague)
                leagueId.add(e.idLeague)
            }
        }
        val spinnerAdapter = ArrayAdapter(ctx, android.R.layout.simple_spinner_dropdown_item, leagueList)
        spinner.adapter = spinnerAdapter

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                leagueName = leagueId[position]
                presenter.getTeam(leagueId[position])
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

    }

    override fun onQueryTextSubmit(p0: String?): Boolean {
        return false
    }

    override fun onQueryTextChange(p0: String?): Boolean {
        adapter.filter.filter(p0)
        return true
    }
}