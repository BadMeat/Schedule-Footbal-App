package com.example.user.sceball

import android.database.sqlite.SQLiteConstraintException
import android.graphics.Color
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import com.example.user.sceball.db.PlayerTable
import com.example.user.sceball.db.database
import com.example.user.sceball.model.Player
import com.example.user.sceball.model.PlayerView
import com.example.user.sceball.model.Team
import com.example.user.sceball.presenter.PlayerDetailPresenter
import com.google.gson.Gson
import com.squareup.picasso.Picasso
import org.jetbrains.anko.*
import org.jetbrains.anko.db.classParser
import org.jetbrains.anko.db.delete
import org.jetbrains.anko.db.insert
import org.jetbrains.anko.db.select

class PlayerDetailActivity : AppCompatActivity(), PlayerView {

    private lateinit var progresBar: ProgressBar
    private lateinit var presenter: PlayerDetailPresenter
    private lateinit var flagImage: ImageView
    private lateinit var playerImage: ImageView
    private lateinit var playerName: TextView
    private lateinit var playerDesc: TextView
    private lateinit var playerTeam: TextView
    private lateinit var playerBord: TextView
    private lateinit var playerWage: TextView
    private lateinit var playerPosition: TextView
    private lateinit var playerNational: TextView
    private lateinit var playerId: String
    private var playerImageUrl: String? = ""
    private var flagName: String? = ""

    private var menuItem: Menu? = null
    private var isFavorite: Boolean = false

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.detail_menu, menu)
        menuItem = menu
        setFavorite()
        return true
    }

    private fun setFavorite() {
        if (isFavorite)
            menuItem?.getItem(0)?.icon = ContextCompat.getDrawable(this, R.drawable.ic_sentiment_satisfied_black_24dp)
        else
            menuItem?.getItem(0)?.icon = ContextCompat.getDrawable(this, R.drawable.ic_sentiment_neutral_black_24dp)
    }

    private fun favoritePlayer() {
        database.use {
            val result = select(PlayerTable.TABLE_PLAYER)
                .whereArgs(
                    "(PLAYER_ID = {id})",
                    "id" to playerId
                )

            val fav = result.parseList(classParser<PlayerTable>())
            println("Favku Pak : $fav")
            if (!fav.isEmpty()) isFavorite = true
        }
    }

    private fun addFavoritePlayer() {
        try {
            database.use {
                insert(
                    PlayerTable.TABLE_PLAYER,
                    PlayerTable.PLAYER_ID to playerId,
                    PlayerTable.PLAYER_NAME to playerName.text,
                    PlayerTable.PLAYER_NAT to playerNational.text,
                    PlayerTable.PLAYER_PIC to playerImageUrl,
                    PlayerTable.PLAYER_TEAM to playerTeam.text
                )
            }
        } catch (ex: SQLiteConstraintException) {
            toast(ex.localizedMessage)
        }
    }

    private fun deleteFavoritePlayer() {
        database.use {
            delete(
                PlayerTable.TABLE_PLAYER, "(PLAYER_ID = {id})",
                "id" to playerId
            )
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            R.id.add_to_favorite -> {
                if (isFavorite) deleteFavoritePlayer() else addFavoritePlayer()

                isFavorite = !isFavorite
                setFavorite()

                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        val idTam = intent.getStringExtra("idPlayer")
        playerId = idTam
        super.onCreate(savedInstanceState)
        supportActionBar?.title = "Player Detail"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        linearLayout {
            orientation = LinearLayout.VERTICAL
            lparams(matchParent, wrapContent) {
                margin = 50
            }
            progresBar = progressBar {

            }

            /* Garis */
            linearLayout {
                lparams(matchParent, 1)
                backgroundColor = Color.BLACK
            }
            playerName = textView {
                textSize = 20f
            }.lparams(matchParent, wrapContent) {
                bottomMargin = 20
            }
            /* Garis */
            linearLayout {
                lparams(matchParent, 1)
                backgroundColor = Color.BLACK
            }

            linearLayout {
                weightSum = 3f
                lparams(matchParent, wrapContent) {
                    topMargin = 40
                }
                /* Foto Pemain */
                playerImage = imageView {

                }.lparams(0, 300, 1f)

                /* Informasi Pemain */
                linearLayout {
                    orientation = LinearLayout.VERTICAL
                    lparams(0, matchParent, 2f) {
                        margin = 10
                    }

                    playerNational = textView {

                    }.lparams(matchParent, wrapContent)

                    playerTeam = textView {

                    }.lparams(matchParent, wrapContent)

                    playerBord = textView {

                    }.lparams(matchParent, wrapContent)

                    playerWage = textView {

                    }.lparams(matchParent, wrapContent)

                    playerPosition = textView {
                    }.lparams(matchParent, wrapContent)

                }
            }

            /* Tulisan Deskripsi */
            linearLayout {
                lparams(matchParent, wrapContent) {
                    topMargin = 40
                }
                backgroundColor = getColor(R.color.firstColor)
                textView {
                    textSize = 20f
                    text = getString(R.string.deskription)
                }.lparams(matchParent, wrapContent) {
                    margin = 10
                }
            }

            scrollView {
                lparams(matchParent, wrapContent) {
                    topMargin = 50
                    bottomMargin = 50
                }
                playerDesc = textView {

                }.lparams(matchParent, wrapContent)
            }

        }
        val gson = Gson()
        val request = ApiRepository()
        favoritePlayer()
        presenter = PlayerDetailPresenter(this, request, gson)
        presenter.getDetailPlayer(idTam)
    }

    override fun showLoading() {
        progresBar.visible()
    }

    override fun hideLoading() {
        progresBar.invisible()
    }

    override fun showPlayer(data: List<Player>) {
        if (data.isEmpty()) {
            toast("Data Kosong")
        } else {
            val a = data[0]
            if(data[0].strCutout != null){
                Picasso.get().load(data[0].strCutout).into(playerImage)
            }else{
                playerImage.imageResource = R.drawable.noimageicon
            }

            playerImageUrl = data[0].strCutout
            flagName = a.strNationality
            playerName.text = a.strPlayer
            playerDesc.text = a.strDescriptionEN
            playerTeam.text = getString(R.string.team, a.strTeam)
            playerBord.text = getString(R.string.born, convertSimpleData(a.dateBorn))
            var wage = a.strWage
            if (a.strWage == null || a.strWage.equals("")) {
                wage = getString(R.string.unkwn)
            }
            playerWage.text = getString(R.string.wage, wage)
            playerPosition.text = getString(R.string.position, a.strPosition)
            playerNational.text = getString(R.string.national, a.strNationality)
        }
    }

    override fun showTeam(data: List<Team>) {

    }
}
