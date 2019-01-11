package com.example.user.sceball.adapter

import android.graphics.Color
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.example.user.sceball.R
import com.example.user.sceball.db.PlayerTable
import com.squareup.picasso.Picasso
import org.jetbrains.anko.*

class FavoritePlayerAdapter(
    private val player: MutableList<PlayerTable>,
    private val listener: (PlayerTable) -> Unit
) : RecyclerView.Adapter<PlayerFavoriteHolder>(), Filterable {


    private var filterList: MutableList<PlayerTable> = player

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(input: CharSequence?): FilterResults {
                val myInput: String = input.toString()
                filterList = if (input.isNullOrEmpty()) {
                    player
                } else {
                    val resultList = mutableListOf<PlayerTable>()
                    for (row: PlayerTable in player) {
                        val tempName = row.playerName?.toLowerCase()
                        if (tempName != null) {
                            if (tempName.contains(myInput)) {
                                resultList.add(row)
                            }
                        }
                    }
                    resultList
                }
                val filterResult = Filter.FilterResults()
                filterResult.values = filterList
                return filterResult
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                filterList = results!!.values as MutableList<PlayerTable>
                notifyDataSetChanged()
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): PlayerFavoriteHolder {
        return PlayerFavoriteHolder(PlayerTeamUI().createView(AnkoContext.create(parent.context, parent)))
    }

    override fun getItemCount(): Int = filterList.size

    override fun onBindViewHolder(holder: PlayerFavoriteHolder, position: Int) {
        holder.bindItem(filterList[position], listener)
    }

}

class PlayerTeamUI : AnkoComponent<ViewGroup> {
    override fun createView(ui: AnkoContext<ViewGroup>): View {
        return with(ui) {
            linearLayout {
                orientation = LinearLayout.VERTICAL
                lparams(matchParent, wrapContent)
                linearLayout {
                    lparams(matchParent, wrapContent) {
                        margin = 50
                    }
                    weightSum = 3f
                    imageView {
                        id = R.id.imageOne
                    }.lparams(0, 200, 1f)

                    linearLayout {
                        lparams(0, wrapContent, 2f) {
                            leftMargin = 10
                        }
                        orientation = LinearLayout.VERTICAL

                        textView {
                            id = R.id.text1
                        }.lparams(matchParent, wrapContent)

                        textView {
                            id = R.id.text2
                        }.lparams(matchParent, wrapContent)

                        textView {
                            id = R.id.text3
                        }.lparams(matchParent, wrapContent)
                    }
                }
                /*===== Garis Pemabatan =====*/
                linearLayout {
                    backgroundColor = Color.BLACK
                    lparams(matchParent, 3)
                }
            }
        }
    }
}

class PlayerFavoriteHolder(view: View) : RecyclerView.ViewHolder(view) {
    private val playerImage: ImageView = view.findViewById(R.id.imageOne)
    private val playerName: TextView = view.findViewById(R.id.text1)
    private val playerTeam: TextView = view.findViewById(R.id.text2)
    private val playerNat: TextView = view.findViewById(R.id.text3)

    fun bindItem(player: PlayerTable, listerner: (PlayerTable) -> Unit) {
        if(player.playerPic != null){
            Picasso.get().load(player.playerPic).into(playerImage)
        }else{
            playerImage.imageResource = R.drawable.noimageicon
        }

        playerName.text = player.playerName
        playerTeam.text = player.playerTeam
        playerNat.text = player.playerNat
        itemView.setOnClickListener {
            listerner(player)
        }
    }
}