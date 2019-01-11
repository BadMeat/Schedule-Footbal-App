package com.example.user.sceball

import android.database.sqlite.SQLiteConstraintException
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import com.example.user.sceball.db.TeamTable
import com.example.user.sceball.db.database
import com.example.user.sceball.model.League
import com.example.user.sceball.model.Team
import com.example.user.sceball.model.TeamView
import com.example.user.sceball.presenter.TeamDetailPresenter
import com.google.gson.Gson
import com.squareup.picasso.Picasso
import org.jetbrains.anko.*
import org.jetbrains.anko.db.classParser
import org.jetbrains.anko.db.delete
import org.jetbrains.anko.db.insert
import org.jetbrains.anko.db.select

class TeamDetailActivity : AppCompatActivity(), TeamView {
    override fun showLoading() {
        progressBar.visible()
    }

    override fun hideLoading() {
        progressBar.invisible()
    }

    override fun showTeam(data: List<Team>) {
        val a = data[0]
        teamName.text = a.strTeam
        teamLeague.text = getString(R.string.league, a.strLeague)
        teamFormedYear.text = getString(R.string.formed, a.intFormedYear)
        teamManager.text = getString(R.string.manager, a.strManager)
        teamStadion.text = getString(R.string.stadium, a.strStadium)
        teamDesciption.text = a.strDescriptionEN
        Picasso.get().load(a.strTeamBadge).into(teamImage)
        teamBadge = a.strTeamBadge
    }

    override fun showAllLeague(data: List<League>) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private lateinit var presenter: TeamDetailPresenter
    private lateinit var progressBar: ProgressBar

    private lateinit var teamImage: ImageView
    private lateinit var teamName: TextView
    private lateinit var teamLeague: TextView
    private lateinit var teamFormedYear: TextView
    private lateinit var teamManager: TextView
    private lateinit var teamStadion: TextView
    private lateinit var teamDesciption: TextView
    private lateinit var teamId: String
    private var teamBadge: String? = ""

    private var menuItem: Menu? = null
    private var isFavorite: Boolean = false

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.detail_menu, menu)
        menuItem = menu
        setFavorite()
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            R.id.add_to_favorite -> {
                if (isFavorite) removeFromFavorite() else addToFavorite()

                isFavorite = !isFavorite
                setFavorite()

                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun favoriteState() {
        database.use {
            val result = select(TeamTable.TABLE_TEAM)
                .whereArgs(
                    "(TEAM_ID = {id})",
                    "id" to teamId
                )
            val fav = result.parseList(classParser<TeamTable>())
            if (!fav.isEmpty()) isFavorite = true
        }
    }

    private fun setFavorite() {
        if (isFavorite)
            menuItem?.getItem(0)?.icon = ContextCompat.getDrawable(this, R.drawable.ic_sentiment_satisfied_black_24dp)
        else
            menuItem?.getItem(0)?.icon = ContextCompat.getDrawable(this, R.drawable.ic_sentiment_neutral_black_24dp)
    }

    private fun addToFavorite() {
        try {
            database.use {
                insert(
                    TeamTable.TABLE_TEAM,
                    TeamTable.TEAM_ID to teamId,
                    TeamTable.TEAM_NAME to teamName.text,
                    TeamTable.TEAM_BADGE to teamBadge,
                    TeamTable.TEAM_LEAGUE to teamLeague.text,
                    TeamTable.TEAM_MANAGER to teamManager.text

                )
            }
        } catch (e: SQLiteConstraintException) {
            toast(e.localizedMessage)
        }
    }

    private fun removeFromFavorite() {
        database.use {
            delete(
                TeamTable.TABLE_TEAM, "(TEAM_ID = {id})",
                "id" to teamId
            )
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.title = "Team Detail"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        linearLayout {
            orientation = LinearLayout.VERTICAL
            lparams(matchParent, wrapContent) {
                margin = 40
            }

            /* Garis */
            linearLayout {
                backgroundColor = Color.BLACK
                lparams(matchParent, 4)
            }

            /* Nama Team */
            teamName = textView {
                textSize = 20f
                typeface = Typeface.defaultFromStyle(Typeface.BOLD)
                backgroundColor = getColor(R.color.firstColor)
                textAlignment = TextView.TEXT_ALIGNMENT_CENTER
            }.lparams(matchParent, wrapContent)

            /* Garis */
            linearLayout {
                backgroundColor = Color.BLACK
                lparams(matchParent, 4)
            }

            /* Foto Dan Deskripsi */
            linearLayout {
                weightSum = 6f
                lparams(matchParent, wrapContent)
                /* Foto Team */
                teamImage = imageView {
                    backgroundColor = Color.GRAY
                }.lparams(0, wrapContent, 2f)

                /* Deskripsi */
                linearLayout {
                    orientation = LinearLayout.VERTICAL
                    backgroundColor = Color.BLACK
                    lparams(0, matchParent, 4f) {
                        padding = 10
                    }
                    /* Liga */
                    teamLeague = textView {
                        textColor = Color.WHITE
                    }.lparams(matchParent, wrapContent)
                    /* Formed Year */
                    teamFormedYear = textView {
                        textColor = Color.WHITE
                    }.lparams(matchParent, wrapContent)
                    /* Manager */
                    teamManager = textView {
                        textColor = Color.WHITE
                    }.lparams(matchParent, wrapContent)
                    /* Stadion */
                    teamStadion = textView {
                        textColor = Color.WHITE
                    }.lparams(matchParent, wrapContent)
                }
            }

            /* Garis */
            linearLayout {
                backgroundColor = Color.BLACK
                lparams(matchParent, 4) {
                    topMargin = 40
                }
            }

            /* Tulisan Deskription */
            textView {
                textSize = 20f
                text = getString(R.string.deskription)
                backgroundColor = getColor(R.color.firstColor)
            }.lparams(matchParent, wrapContent) {
                padding = 10
            }

            /* Garis */
            linearLayout {
                backgroundColor = Color.BLACK
                lparams(matchParent, 4)
            }

            scrollView {
                teamDesciption = textView {

                }.lparams(matchParent, wrapContent)
            }.lparams(matchParent, wrapContent)
        }

        teamId = intent.getStringExtra("teamId")
        val gson = Gson()
        val request = ApiRepository()
        favoriteState()
        presenter = TeamDetailPresenter(this, request, gson)
        presenter.getTeamById(teamId)
    }
}
