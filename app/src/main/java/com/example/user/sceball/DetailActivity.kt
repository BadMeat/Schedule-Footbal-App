package com.example.user.sceball

import android.database.sqlite.SQLiteConstraintException
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.os.Handler
import android.support.v4.content.ContextCompat
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.app.AppCompatActivity
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import android.view.WindowManager
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import com.example.user.sceball.db.Favorite
import com.example.user.sceball.db.database
import com.example.user.sceball.model.DetailView
import com.example.user.sceball.model.MatchEvent
import com.example.user.sceball.model.TeamDetail
import com.example.user.sceball.presenter.DetailPresenter
import com.google.gson.Gson
import com.squareup.picasso.Picasso
import org.jetbrains.anko.*
import org.jetbrains.anko.db.classParser
import org.jetbrains.anko.db.delete
import org.jetbrains.anko.db.insert
import org.jetbrains.anko.db.select
import org.jetbrains.anko.design.appBarLayout
import org.jetbrains.anko.design.snackbar
import org.jetbrains.anko.support.v4.onRefresh
import org.jetbrains.anko.support.v4.swipeRefreshLayout

/**
 * Created by Bencoleng on 12/11/2018.
 */
class DetailActivity : AppCompatActivity(), DetailView {
    private var teams: MutableList<TeamDetail> = mutableListOf()
    private var aways: MutableList<TeamDetail> = mutableListOf()
    private var events: MutableList<MatchEvent> = mutableListOf()
    private lateinit var presenter: DetailPresenter

    private lateinit var progressBar: ProgressBar
    private lateinit var swipeRefresh: SwipeRefreshLayout

    private lateinit var img1: ImageView
    private lateinit var img2: ImageView

    private lateinit var nm1: TextView
    private lateinit var nm2: TextView

    private var menuItem: Menu? = null
    private var isFavorite: Boolean = false

    private lateinit var home: TeamDetail
    private lateinit var away: TeamDetail

    private lateinit var id: String
    private var idHome: Int = 0
    private var idAway: Int = 0

    /*
     * Home
     */

    private lateinit var textTime: TextView
    private lateinit var textScore1: TextView
    private lateinit var textScore2: TextView
    private lateinit var textGoals1: TextView
    private lateinit var textGoals2: TextView
    private lateinit var textShoot1: TextView
    private lateinit var textShoot2: TextView
    private lateinit var textGoalKeeper1: TextView
    private lateinit var textGoalKeeper2: TextView
    private lateinit var textDefense1: TextView
    private lateinit var textDefense2: TextView
    private lateinit var textMid1: TextView
    private lateinit var textMid2: TextView
    private lateinit var textForward1: TextView
    private lateinit var textForward2: TextView
    private lateinit var textSubtitle1: TextView
    private lateinit var textSubtitle2: TextView

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

    private fun setFavorite() {
        if (isFavorite)
            menuItem?.getItem(0)?.icon = ContextCompat.getDrawable(this, R.drawable.ic_sentiment_satisfied_black_24dp)
        else
            menuItem?.getItem(0)?.icon = ContextCompat.getDrawable(this, R.drawable.ic_sentiment_neutral_black_24dp)
    }

    private fun favoriteState() {
        database.use {
            val result = select(Favorite.TABLE_FAVORITE)
                .whereArgs(
                    "(EVENT_ID = {id})",
                    "id" to id
                )
            val favorite = result.parseList(classParser<Favorite>())
            if (!favorite.isEmpty()) isFavorite = true
        }
    }

    fun removeFromFavorite() {
        try {
            database.use {
                delete(
                    Favorite.TABLE_FAVORITE, "(EVENT_ID = {id})",
                    "id" to id
                )
            }
            swipeRefresh.snackbar("Removed to favorite").show()
        } catch (e: SQLiteConstraintException) {
            swipeRefresh.snackbar(e.localizedMessage).show()
        }
    }

    fun addToFavorite() {
        val score1: Int = if (textScore1.text.toString().equals("", true)) -1 else textScore1.text.toString().toInt()
        val score2: Int = if (textScore2.text.toString().equals("", true)) -1 else textScore2.text.toString().toInt()
        try {
            database.use {
                insert(
                    Favorite.TABLE_FAVORITE,
                    Favorite.EVENT_ID to id,
                    Favorite.EVENT_TIME to textTime.text,
                    Favorite.HOME_ID to idHome,
                    Favorite.HOME_NAME to home.teamNm,
                    Favorite.HOME_BADGE to home.teamId,
                    Favorite.HOME_SCORE to score1,
                    Favorite.AWAY_NAME to away.teamNm,
                    Favorite.AWAY_BADGE to away.teamId,
                    Favorite.AWAY_SCORE to score2,
                    Favorite.AWAY_ID to idAway
                )
            }
            swipeRefresh.snackbar("Added to favorite").show()
        } catch (e: SQLiteConstraintException) {
            swipeRefresh.snackbar(e.localizedMessage).show()
        }

    }

    private fun separatisme(pState: String?): String? {
        val separate1 = pState?.split(";".toRegex())
        var e: String? = ""
        if (separate1 != null) {
            for (a in separate1.indices) {
                if (a == separate1.lastIndex) {
                    e += separate1[a]
                } else if (a == 0) {
                    e += "- " + separate1[a] + "\n"
                } else {
                    e += "- " + separate1[a] + "\n"
                }
            }
        }
        return e
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.setFlags(
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
        )

        idHome = intent.getIntExtra("idHome", 0)
        idAway = intent.getIntExtra("idAway", 0)
        id = intent.getStringExtra("idEvent")

        supportActionBar?.title = "Match Detail"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        linearLayout {
            
        }

        linearLayout {
            id = R.id.test_layout
            appBarLayout {
                backgroundColor = Color.RED
                lparams(matchParent, wrapContent)
            }
            orientation = LinearLayout.VERTICAL
            lparams(matchParent, matchParent)
            swipeRefresh = swipeRefreshLayout {
                setColorSchemeResources(
                    R.color.colorAccent,
                    android.R.color.holo_green_light,
                    android.R.color.holo_orange_light,
                    android.R.color.holo_red_light
                )
                scrollView {
                    linearLayout {
                        orientation = LinearLayout.VERTICAL
                        lparams(matchParent, matchParent) {
                            margin = dip(10)
                        }
                        progressBar = progressBar {
                        }
                        textTime =
                                textView {
                                    gravity = Gravity.CENTER
                                    typeface = Typeface.defaultFromStyle(Typeface.BOLD)
                                    textSize = 20f
                                }.lparams(matchParent, wrapContent) {
                                    bottomMargin = dip(20)
                                }
                        /*
                        * Team
                        */
                        linearLayout {
                            lparams(matchParent, wrapContent)
                            linearLayout {
                                orientation = LinearLayout.VERTICAL
                                lparams(width = 0, height = wrapContent) {
                                    weight = 1f
                                }
                                img1 = imageView {
                                }.lparams {
                                    height = dip(50)
                                    width = dip(50)
                                    gravity = Gravity.CENTER
                                }
                                nm1 = textView {
                                    gravity = Gravity.CENTER
                                }.lparams(matchParent, wrapContent) {
                                    topMargin = dip(10)
                                }
                            }

                            /*
                             * Score 1
                             */
                            textScore1 = textView {
                                gravity = Gravity.CENTER
                                textSize = 20f
                                typeface = Typeface.defaultFromStyle(Typeface.BOLD)
                            }.lparams(width = 0, height = matchParent, weight = 1f)

                            textView {
                                text = "VS"
                                gravity = Gravity.CENTER
                            }.lparams(width = 0, height = matchParent, weight = 1f)

                            /*
                             * Score 2
                             */
                            textScore2 = textView {
                                gravity = Gravity.CENTER
                                textSize = 20f
                                typeface = Typeface.defaultFromStyle(Typeface.BOLD)
                            }.lparams(width = 0, height = matchParent, weight = 1f)
                            linearLayout {
                                orientation = LinearLayout.VERTICAL
                                lparams(width = 0, height = wrapContent) {
                                    weight = 1f
                                }
                                img2 = imageView {

                                }.lparams {
                                    height = dip(50)
                                    width = dip(50)
                                    gravity = Gravity.CENTER
                                }
                                nm2 = textView {
                                    gravity = Gravity.CENTER
                                }.lparams(matchParent, wrapContent) {
                                    topMargin = dip(10)
                                }
                            }
                        }

                        textView {
                            gravity = Gravity.CENTER
                            backgroundColor = Color.GRAY
                        }.lparams(matchParent, 1) {
                            topMargin = dip(20)
                        }

                        /*
                     * Goals
                     */
                        linearLayout {
                            lparams(matchParent, wrapContent) {
                                topMargin = dip(20)
                            }
                            textGoals1 = textView {
                                gravity = Gravity.CENTER
                            }.lparams(0, wrapContent, 1f)

                            textView {
                                text = "GOALS"
                                gravity = Gravity.CENTER
                            }.lparams(0, wrapContent, 1f)

                            textGoals2 = textView {
                                gravity = Gravity.CENTER
                            }.lparams(0, wrapContent, 1f)
                        }
                        /*
                     * Shoot
                     */
                        linearLayout {
                            lparams(matchParent, wrapContent) {
                                topMargin = dip(20)
                            }
                            textShoot1 = textView {
                                gravity = Gravity.CENTER
                            }.lparams(0, wrapContent, 1f)

                            textView {
                                text = "Shoots"
                                gravity = Gravity.CENTER
                            }.lparams(0, wrapContent, 1f)

                            textShoot2 = textView {
                                gravity = Gravity.CENTER
                            }.lparams(0, wrapContent, 1f)
                        }

                        /*
                         * Garisnya
                         */
                        textView {
                            gravity = Gravity.CENTER
                            backgroundColor = Color.GRAY
                        }.lparams(matchParent, 1) {
                            topMargin = dip(20)
                        }

                        /*
                         * Lineups
                        */
                        textView {
                            text = "Lineups"
                            gravity = Gravity.CENTER
                            typeface = Typeface.defaultFromStyle(Typeface.BOLD)
                        }.lparams(matchParent, wrapContent) {
                            topMargin = dip(20)
                        }

                        /*
                         * Garisnya
                         */
                        textView {
                            gravity = Gravity.CENTER
                            backgroundColor = Color.GRAY
                        }.lparams(matchParent, 1) {
                            topMargin = dip(20)
                        }

                        /*
                     * Goal Keeper
                     */
                        linearLayout {
                            lparams(matchParent, wrapContent) {
                                topMargin = dip(20)
                            }
                            textGoalKeeper1 = textView {
                                //                                val e: String? = separatisme(strHomeLineupGoalkeeper)
                                gravity = Gravity.CENTER
                            }.lparams(0, wrapContent, 1f)

                            textView {
                                text = "Goal Keeper"
                                gravity = Gravity.CENTER
                            }.lparams(0, wrapContent, 1f)

                            textGoalKeeper2 = textView {
                                //                                val e: String? = separatisme(strAwayLineupGoalkeeper)
                                gravity = Gravity.CENTER
                            }.lparams(0, wrapContent, 1f)
                        }

                        /*
                     * Deffense
                     */
                        linearLayout {
                            lparams(matchParent, wrapContent) {
                                topMargin = dip(20)
                            }
                            textDefense1 = textView {
                                //                                val e: String? = separatisme(strHomeLineupDefense)
                                gravity = Gravity.CENTER
                            }.lparams(0, wrapContent, 1f)

                            textView {
                                text = "Deffense"
                                gravity = Gravity.CENTER
                            }.lparams(0, wrapContent, 1f)

                            textDefense2 = textView {
                                gravity = Gravity.CENTER
                            }.lparams(0, wrapContent, 1f)
                        }

                        /*
                     * Midfield
                     */
                        linearLayout {
                            lparams(matchParent, wrapContent) {
                                topMargin = dip(20)
                            }
                            textMid1 = textView {
                                //                                val e: String? = separatisme(strHomeLineupMidfield)
                                gravity = Gravity.CENTER
                            }.lparams(0, wrapContent, 1f)

                            textView {
                                text = "Midfield"
                                gravity = Gravity.CENTER
                            }.lparams(0, wrapContent, 1f)

                            textMid2 = textView {
                                //                                val e: String? = separatisme(strAwayLineupMidfield)
                                gravity = Gravity.CENTER
                            }.lparams(0, wrapContent, 1f)
                        }

                        /*
                     * Forward
                     */
                        linearLayout {
                            lparams(matchParent, wrapContent) {
                                topMargin = dip(20)
                            }
                            textForward1 = textView {
                                //                                val e: String? = separatisme(strHomeLineupForward)
                                gravity = Gravity.CENTER
                            }.lparams(0, wrapContent, 1f)

                            textView {
                                text = "Forward"
                                gravity = Gravity.CENTER
                            }.lparams(0, wrapContent, 1f)

                            textForward2 = textView {
                                //                                val e: String? = separatisme(strAwayLineupForward)
                                gravity = Gravity.CENTER
                            }.lparams(0, wrapContent, 1f)
                        }

                        /*
                     * Subtitle
                     */
                        linearLayout {
                            lparams(matchParent, wrapContent) {
                                topMargin = dip(20)
                            }
                            textSubtitle1 = textView {
                                gravity = Gravity.CENTER
                            }.lparams(0, wrapContent, 1f)

                            textView {
                                text = "Subtitle"
                                gravity = Gravity.CENTER
                            }.lparams(0, wrapContent, 1f)

                            textSubtitle2 = textView {
                                gravity = Gravity.CENTER
                            }.lparams(0, wrapContent, 1f)
                        }
                    }
                }
            }
        }
        val request = ApiRepository()
        val gson = Gson()
        favoriteState()
        presenter = DetailPresenter(this, request, gson)
        presenter.getTeam(idHome, idAway)
        presenter.getDetailEvent(id)
        swipeRefresh.onRefresh {
            presenter.getTeam(idHome, idAway)
        }

        Handler().postDelayed({
            window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
        }, 1500)
    }

    override fun showLoading() {
        progressBar.visible()
    }

    override fun hideLoading() {
        progressBar.invisible()

    }

    override fun showTeamList(data: List<TeamDetail>) {
        if (data.isEmpty()) {
            toast("Data Kosong")
        } else {
            teams.clear()
            teams.addAll(data)
            Picasso.get().load(data[0].teamId).into(img1)
            nm1.text = data[0].teamNm
            home = TeamDetail(
                data[0].teamId,
                data[0].teamNm
            )
            swipeRefresh.isRefreshing = false
        }
    }

    override fun showAwayList(data: List<TeamDetail>) {
        if (data.isEmpty()) {
            toast("Data Kosong")
        } else {
            aways.clear()
            aways.addAll(data)
            Picasso.get().load(data[0].teamId).into(img2)
            nm2.text = data[0].teamNm
            away = TeamDetail(
                data[0].teamId,
                data[0].teamNm
            )
            swipeRefresh.isRefreshing = false
        }
    }

    override fun showEventList(data: List<MatchEvent>) {
        events.clear()
        events.addAll(data)
        textTime.text = convertDate(data[0].dateEvent, data[0].dateEvent + " " + data[0].strTime)
        textScore1.text = if (data[0].intHomeScore != null) data[0].intHomeScore.toString() else ""
        textScore2.text = if (data[0].intAwayScore != null) data[0].intAwayScore.toString() else ""
        textGoals1.text = separatisme(data[0].strHomeGoalDetails)
        textGoals2.text = separatisme(data[0].strAwayGoalDetails)
        textShoot1.text = if (data[0].intHomeShots != null) data[0].intHomeShots.toString() else ""
        textShoot2.text = if (data[0].intAwayShots != null) data[0].intAwayShots.toString() else ""
        textGoalKeeper1.text = separatisme(data[0].strHomeLineupGoalkeeper)
        textGoalKeeper2.text = separatisme(data[0].strAwayLineupGoalkeeper)
        textDefense1.text = separatisme(data[0].strHomeLineupDefense)
        textDefense2.text = separatisme(data[0].strAwayLineupDefense)
        textMid1.text = separatisme(data[0].strHomeLineupMidfield)
        textMid2.text = separatisme(data[0].strAwayLineupMidfield)
        textForward1.text = separatisme(data[0].strHomeLineupForward)
        textForward2.text = separatisme(data[0].strAwayLineupForward)
        textSubtitle1.text = separatisme(data[0].strHomeLineupSubstitutes)
        textSubtitle2.text = separatisme(data[0].strAwayLineupSubstitutes)
    }
}