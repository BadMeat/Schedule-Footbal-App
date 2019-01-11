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
import android.widget.ProgressBar
import android.widget.SearchView
import com.example.user.sceball.PlayerDetailActivity
import com.example.user.sceball.R
import com.example.user.sceball.adapter.FavoritePlayerAdapter
import com.example.user.sceball.db.PlayerTable
import com.example.user.sceball.db.database
import org.jetbrains.anko.*
import org.jetbrains.anko.db.classParser
import org.jetbrains.anko.db.select
import org.jetbrains.anko.recyclerview.v7.recyclerView
import org.jetbrains.anko.support.v4.ctx
import org.jetbrains.anko.support.v4.intentFor
import org.jetbrains.anko.support.v4.onRefresh
import org.jetbrains.anko.support.v4.swipeRefreshLayout

class FavoritePlayerFragment : Fragment(), AnkoComponent<Context>, SearchView.OnQueryTextListener {

    private var playerList: MutableList<PlayerTable> = mutableListOf()
    private lateinit var progressBar: ProgressBar
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: FavoritePlayerAdapter
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var searchView: SearchView

    override fun createView(ui: AnkoContext<Context>): View = with(ui) {
        linearLayout {
            lparams(matchParent, wrapContent)
            swipeRefreshLayout = swipeRefreshLayout {
                setColorSchemeResources(
                    R.color.colorAccent,
                    android.R.color.holo_green_light,
                    android.R.color.holo_orange_light,
                    android.R.color.holo_red_light
                )
                recyclerView = recyclerView {
                    lparams(matchParent, wrapContent)
                    layoutManager = LinearLayoutManager(ctx)
                }
            }

            progressBar = progressBar {

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
        adapter = FavoritePlayerAdapter(playerList) {
            startActivity(
                intentFor<PlayerDetailActivity>(
                    "idPlayer" to it.playerId
                )
            )
        }
        recyclerView.adapter = adapter
        showPlayer()
        swipeRefreshLayout.onRefresh {
            playerList.clear()
            showPlayer()
        }
    }

    private fun showPlayer() {
        context?.database?.use {
            swipeRefreshLayout.isRefreshing = false
            val data = select(PlayerTable.TABLE_PLAYER)
            val player = data.parseList(classParser<PlayerTable>())
            playerList.clear()
            playerList.addAll(player)
            adapter.notifyDataSetChanged()
        }
    }

    override fun onResume() {
        super.onResume()
        showPlayer()
        swipeRefreshLayout.onRefresh {
            playerList.clear()
            showPlayer()
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