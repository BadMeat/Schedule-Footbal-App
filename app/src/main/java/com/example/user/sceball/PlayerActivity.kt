package com.example.user.sceball

import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.MenuItem
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import com.example.user.sceball.adapter.PlayerAdapter
import com.example.user.sceball.model.Player
import com.example.user.sceball.model.PlayerView
import com.example.user.sceball.model.Team
import com.example.user.sceball.presenter.PlayerPresenter
import com.google.gson.Gson
import com.squareup.picasso.Picasso
import org.jetbrains.anko.*
import org.jetbrains.anko.design.snackbar
import org.jetbrains.anko.recyclerview.v7.recyclerView
import org.jetbrains.anko.support.v4.onRefresh
import org.jetbrains.anko.support.v4.swipeRefreshLayout

class PlayerActivity : AppCompatActivity(), PlayerView {

    private val player: MutableList<Player> = mutableListOf()
    private lateinit var presenter: PlayerPresenter
    private lateinit var adapter: PlayerAdapter
    private lateinit var progressBar: ProgressBar
    private lateinit var recyclerView: RecyclerView
    private lateinit var swipeRefresh: SwipeRefreshLayout
    private var teamName: String? = ""
    private var teamId: String? = ""

    private lateinit var teamBadge: ImageView
    private lateinit var teamNam: TextView
    private lateinit var teamDesc: TextView
    private lateinit var lineLayout: LinearLayout


    override fun showLoading() {
        progressBar.visible()
    }

    override fun hideLoading() {
        progressBar.invisible()
    }

    override fun showPlayer(data: List<Player>) {
        swipeRefresh.isRefreshing = false
        player.clear()
        player.addAll(data)
        adapter.notifyDataSetChanged()
    }

    override fun showTeam(data: List<Team>) {
        if (data.isEmpty()) {
            swipeRefresh.snackbar("Data Kosong")
        } else {
            teamNam.text = data[0].strTeam
            teamDesc.text = data[0].strDescriptionEN
            Picasso.get().load(data[0].strTeamBadge).into(teamBadge)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportActionBar?.title = "Team"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        teamName = intent.getStringExtra("teamName")
        teamId = intent.getStringExtra("teamId")

        /*
         * Create layout
         */
        linearLayout {
            lparams(width = matchParent, height = wrapContent)
            orientation = LinearLayout.VERTICAL
            topPadding = dip(16)
            leftPadding = dip(16)
            rightPadding = dip(16)
            lineLayout = linearLayout {
                weightSum = 3f
                lparams(matchParent, wrapContent)
                isClickable = true

                /* Team Image */
                teamBadge = imageView {
                    backgroundColor = Color.GRAY
                }.lparams(0, matchParent) {
                    weight = 1f
                }

                /* Deskripsi */
                linearLayout {
                    backgroundColor = Color.BLACK
                    weightSum = 6f
                    orientation = LinearLayout.VERTICAL
                    lparams(0, matchParent, 2f)
                    teamNam = textView {
                        textAlignment = TextView.TEXT_ALIGNMENT_CENTER
                        typeface = Typeface.defaultFromStyle(Typeface.BOLD)
                        textSize = 18f
                        textColor = Color.WHITE
                    }.lparams(matchParent, 0, 1f)
                    teamDesc = textView {
                        textAlignment = TextView.TEXT_ALIGNMENT_CENTER
                        maxLines = 5
                        textColor = Color.WHITE
                    }.lparams(matchParent, 0, 5f) {
                        margin = 5
                    }
                }

            }
            linearLayout {
                lparams(matchParent, wrapContent) {
                    backgroundColor = getColor(R.color.firstColor)
                    textView {
                        textSize = 20f
                        text = getString(R.string.player)
                    }.lparams(matchParent, wrapContent) {
                        margin = 10
                    }
                }
            }
            swipeRefresh = swipeRefreshLayout {
                setColorSchemeResources(
                    R.color.colorAccent,
                    android.R.color.holo_green_light,
                    android.R.color.holo_orange_light,
                    android.R.color.holo_red_light
                )

                relativeLayout {
                    lparams(matchParent, wrapContent)
                    recyclerView = recyclerView {
                        lparams(matchParent, wrapContent)
                        layoutManager = LinearLayoutManager(ctx)
                    }
                    progressBar = progressBar {
                    }.lparams {
                        centerHorizontally()
                    }
                }
            }
        }

        /*
         * Set
         */
        adapter = PlayerAdapter(player) {
            startActivity(
                intentFor<PlayerDetailActivity>(
                    "idPlayer" to it.idPlayer
                )
            )
        }
        recyclerView.adapter = adapter

        val gson = Gson()
        val request = ApiRepository()

        presenter = PlayerPresenter(this, request, gson)
        presenter.getPlayer(teamName)
        presenter.getTeam(teamId)

        swipeRefresh.onRefresh {
            presenter.getPlayer(teamName)
        }

        lineLayout.setOnClickListener {
            startActivity(
                intentFor<TeamDetailActivity>(
                    "teamId" to teamId
                )
            )
        }
    }


}
