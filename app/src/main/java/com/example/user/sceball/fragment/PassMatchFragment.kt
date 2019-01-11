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
import com.example.user.sceball.adapter.PassAdapter
import com.example.user.sceball.model.League
import com.example.user.sceball.model.MatchEvent
import com.example.user.sceball.model.MatchView
import com.example.user.sceball.presenter.MatchPresenter
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
class PassMatchFragment : Fragment(), AnkoComponent<Context>, MatchView, SearchView.OnQueryTextListener {

    private lateinit var progressBar: ProgressBar

    private var passEvent: MutableList<MatchEvent> = mutableListOf()
    private var myLeague: MutableList<String?> = mutableListOf()
    private var myListKey: MutableList<String?> = mutableListOf()
    private lateinit var passPreseneter: MatchPresenter
    private lateinit var passAdapter: PassAdapter
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var listEvent: RecyclerView
    private lateinit var spinner: Spinner
    private var passKey: String? = ""
    private lateinit var searchView: SearchView

    override fun showLoading() {
        progressBar.visible()
    }

    override fun hideLoading() {
        progressBar.invisible()
    }

    override fun showPassEvent(data: List<MatchEvent>) {
        swipeRefreshLayout.isRefreshing = false
        passEvent.clear()
        if (data.isEmpty()) {
            swipeRefreshLayout.snackbar( "Jadwal Kosong").show()
        } else {
            passEvent.addAll(data)
        }
        passAdapter.notifyDataSetChanged()
    }

    override fun showAllLeague(data: List<League>) {
        for (item: League in data) {
            if (item.strSport.equals("Soccer", true) && !item.strLeague.equals("_No League", true)) {
                myLeague.add(item.strLeague)
                myListKey.add(item.idLeague)
            }
        }

        val spinnerAdapter = ArrayAdapter(context, android.R.layout.simple_spinner_dropdown_item, myLeague)
        spinner.adapter = spinnerAdapter

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                passKey = myListKey[position]
                passPreseneter.getPassEvent(passKey)
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }
    }

    override fun onPrepareOptionsMenu(menu: Menu?) {
        val myMenu = menu?.findItem(R.id.action_search)
        searchView = myMenu?.actionView as SearchView
        searchView.setOnQueryTextListener(this)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
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
            passPreseneter = MatchPresenter(this, request, gson)
            passPreseneter.getAllLeague()

            swipeRefreshLayout.onRefresh {
                passPreseneter.getPassEvent(passKey)
            }
        }else{
            swipeRefreshLayout.snackbar(resources.getString(R.string.noconection))
            progressBar.invisible()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        setHasOptionsMenu(true)
        return createView(AnkoContext.create(ctx))
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
            }

            swipeRefreshLayout = swipeRefreshLayout {
                setColorSchemeResources(
                    R.color.colorAccent,
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