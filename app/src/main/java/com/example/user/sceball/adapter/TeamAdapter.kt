package com.example.user.sceball.adapter

import android.graphics.Color
import android.graphics.Typeface
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.example.user.sceball.R
import com.example.user.sceball.model.Team
import com.squareup.picasso.Picasso
import org.jetbrains.anko.*

/**
 * Created by Bencoleng on 14/11/2018.
 */
class TeamAdapter(
    private var team: MutableList<Team>,
    private val listener: (Team) -> Unit
) : RecyclerView.Adapter<TeamHolder>(), Filterable {

    private var itemFilter: List<Team> = team

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TeamHolder {
        return TeamHolder(TeamLeagueUI().createView(AnkoContext.create(parent.context, parent)))
    }

    override fun getItemCount(): Int = itemFilter.size

    override fun onBindViewHolder(holder: TeamHolder, position: Int) {
        holder.bindItem(itemFilter[position], listener)
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun publishResults(input: CharSequence?, pilter: FilterResults?) {
                itemFilter = pilter!!.values as MutableList<Team>
                notifyDataSetChanged()
            }

            override fun performFiltering(input: CharSequence?): FilterResults {
                val myInput: String = input.toString()
                itemFilter = if (input.isNullOrEmpty()) {
                    team
                } else {
                    val resultList = mutableListOf<Team>()
                    for (row: Team in team) {
                        val tempRow = row.strTeam?.toLowerCase()
                        if (tempRow != null) {
                            if (tempRow.contains(myInput)) {
                                resultList.add(row)
                                println("Add Pak : $row")
                            }
                        }
                    }
                    resultList
                }
                val filterResults = Filter.FilterResults()
                filterResults.values = itemFilter
                return filterResults
            }
        }
    }
}

class TeamLeagueUI : AnkoComponent<ViewGroup> {
    override fun createView(ui: AnkoContext<ViewGroup>): View {
        return with(ui) {
            linearLayout {
                lparams(matchParent, wrapContent)
                orientation = LinearLayout.VERTICAL

                linearLayout {
                    weightSum = 4f
                    orientation = LinearLayout.HORIZONTAL
                    lparams(matchParent, wrapContent) {
                        margin = 50
                    }

                    /* Foto */
                    linearLayout {
                        lparams(0, matchParent, 1f)
                        imageView {
                            id = R.id.imageOne
                            image = ContextCompat.getDrawable(ctx,R.drawable.ic_flag_black_24dp)
                        }.lparams(matchParent, 150) {
                            gravity = Gravity.CENTER
                        }
                    }

                    /* Deskripsi */
                    linearLayout {
                        orientation = LinearLayout.VERTICAL
                        lparams(0, matchParent, 3f)
                        weightSum = 6f

                        /* Nama Team */
                        textView {
                            id = R.id.teamOne
                            gravity = Gravity.CENTER
                            typeface = Typeface.defaultFromStyle(Typeface.BOLD)
                            textSize = 16f
                        }.lparams(matchParent, 0, 3f) {
                            topMargin = dip(5)
                            padding = 5
                        }

                        /* Deskripsi */
                        textView {
                            id = R.id.desc
                            maxLines = 3
                            gravity = Gravity.CENTER
                        }.lparams(matchParent, 0, 3f) {
                            topMargin = dip(5)
                            padding = 5
                        }
                    }
                }
                /* Garis */
                linearLayout {
                    backgroundColor = Color.BLACK
                    lparams(matchParent, 4)
                }
            }
        }
    }
}


class TeamHolder(view: View) : RecyclerView.ViewHolder(view) {

    private val team: TextView = view.find(R.id.teamOne)
    private val image: ImageView = view.find(R.id.imageOne)
    private val teamDesc: TextView = view.find(R.id.desc)


    fun bindItem(teams: Team, listener: (Team) -> Unit) {
        team.text = teams.strTeam
        teamDesc.text = if (teams.strDescriptionEN != null) teams.strDescriptionEN else "None"
        Picasso.get().load(teams.strTeamBadge).into(image)
        itemView.setOnClickListener {
            listener(teams)
        }
    }
}