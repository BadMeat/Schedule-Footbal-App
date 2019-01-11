package com.example.user.sceball.adapter

import android.graphics.Color
import android.graphics.Typeface
import android.support.v7.widget.RecyclerView
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.example.user.sceball.R
import com.example.user.sceball.convertDate
import com.example.user.sceball.db.Favorite
import com.squareup.picasso.Picasso
import org.jetbrains.anko.*

/**
 * Created by Bencoleng on 13/11/2018.
 */
class FavoriteMatchAdapter(
    private val favorite: MutableList<Favorite>,
    private val listener: (Favorite) -> Unit
) : RecyclerView.Adapter<FavoriteViewHolder>(), Filterable {

    private var filterList: MutableList<Favorite> = favorite

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(input: CharSequence?): FilterResults {
                val myInput: String = input.toString().toLowerCase()
                filterList = if (input.isNullOrEmpty()) {
                    favorite
                } else {
                    val resultList = mutableListOf<Favorite>()
                    for (row: Favorite in favorite) {
                        val tempName = row.homeName?.toLowerCase()
                        val tempName2 = row.awayName?.toLowerCase()
                        if (tempName != null && tempName2 != null) {
                            if (tempName.contains(myInput) || tempName2.contains(myInput)) {
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
                filterList = results!!.values as MutableList<Favorite>
                notifyDataSetChanged()
            }

        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteViewHolder {
        return FavoriteViewHolder(TeamUI().createView(AnkoContext.create(parent.context, parent)))
    }

    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {
        holder.bindItem(filterList[position], listener)
    }

    override fun getItemCount(): Int = filterList.size

}

class TeamUI : AnkoComponent<ViewGroup> {
    override fun createView(ui: AnkoContext<ViewGroup>): View {
        return with(ui) {
            linearLayout {
                lparams(matchParent, wrapContent)
                orientation = LinearLayout.VERTICAL
                linearLayout {
                    weightSum = 4f
                    lparams(matchParent, wrapContent) {
                        margin = 50
                    }
                    linearLayout {
                        orientation = LinearLayout.VERTICAL
                        lparams(0, wrapContent, 1f)
                        imageView {
                            id = R.id.imageOne
                        }.lparams {
                            height = dip(50)
                            width = dip(50)
                            gravity = Gravity.CENTER
                        }
                        textView {
                            id = R.id.teamOne
                            gravity = Gravity.CENTER
                        }.lparams(matchParent, wrapContent) {
                            topMargin = dip(10)
                        }
                    }

                    linearLayout {
                        orientation = LinearLayout.VERTICAL
                        weightSum = 2f
                        lparams(0, wrapContent, 2f)

                        /* Tanggalnya */
                        textView {
                            id = R.id.jam
                            gravity = Gravity.CENTER
                            text = resources.getString(R.string.stadium)
                        }.lparams(matchParent, 0, 1f)
                        /* Score Dan VS */
                        linearLayout {
                            lparams(matchParent, 0, 1f) {
                                gravity = Gravity.CENTER
                            }

                            /*
                             * Score 1
                             */
                            textView {
                                id = R.id.goal_one
                                gravity = Gravity.CENTER
                                textSize = 20f
                                typeface = Typeface.defaultFromStyle(Typeface.BOLD)
                            }.lparams(0, matchParent, 1f)

                            textView {
                                text = resources.getString(R.string.vs)
                                gravity = Gravity.CENTER
                            }.lparams(0, matchParent, 1f)

                            /*
                             * Score 2
                             */
                            textView {
                                id = R.id.goal_two
                                gravity = Gravity.CENTER
                                textSize = 20f
                                typeface = Typeface.defaultFromStyle(Typeface.BOLD)
                            }.lparams(0, matchParent, 1f)
                        }
                    }

                    linearLayout {
                        orientation = LinearLayout.VERTICAL
                        lparams(0, wrapContent, 1f)
                        imageView {
                            id = R.id.imageTwo
                        }.lparams {
                            height = dip(50)
                            width = dip(50)
                            gravity = Gravity.CENTER
                        }
                        textView {
                            id = R.id.teamTwo
                            gravity = Gravity.CENTER
                        }.lparams(matchParent, wrapContent) {
                            topMargin = dip(10)
                        }
                    }
                }
                /* Garis */
                textView {
                    gravity = Gravity.CENTER
                    backgroundColor = Color.GRAY
                }.lparams(matchParent, 1) {
                    topMargin = dip(20)
                }
            }
        }
    }
}

class FavoriteViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    private val teamBadge: ImageView = view.find(R.id.imageOne)
    private val teamName: TextView = view.find(R.id.teamOne)
    private val teamGoal: TextView = view.find(R.id.goal_one)
    private val awayBadge: ImageView = view.find(R.id.imageTwo)
    private val awayName: TextView = view.find(R.id.teamTwo)
    private val awayGoal: TextView = view.find(R.id.goal_two)
    private val jam: TextView = view.find(R.id.jam)

    fun bindItem(favorite: Favorite, listener: (Favorite) -> Unit) {
        Picasso.get().load(favorite.homeBadge).into(teamBadge)
        Picasso.get().load(favorite.awayBadge).into(awayBadge)
        teamName.text = favorite.homeName
        teamGoal.text = if (favorite.homeScore == -1) "" else favorite.homeScore.toString()
        awayName.text = favorite.awayName
        awayGoal.text = if (favorite.awayScore == -1) "" else favorite.awayScore.toString()
        jam.text = favorite.evenTime
        itemView.setOnClickListener { listener(favorite) }
    }
}