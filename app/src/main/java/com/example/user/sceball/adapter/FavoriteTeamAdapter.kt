package com.example.user.sceball.adapter

import android.graphics.Color
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.example.user.sceball.R
import com.example.user.sceball.db.TeamTable
import com.example.user.sceball.model.Team
import com.squareup.picasso.Picasso
import org.jetbrains.anko.*

/**
 * Created by Bencoleng on 21/11/2018.
 */
class FavoriteTeamAdapter(
    private val team: MutableList<TeamTable>,
    private val listener: (TeamTable) -> Unit
) : RecyclerView.Adapter<FavoriteTeamHolder>(), Filterable {

    private var filterList: MutableList<TeamTable> = team

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(input: CharSequence?): FilterResults {
                val myInput: String = input.toString()
                filterList = if (input.isNullOrEmpty()) {
                    team
                } else {
                    val resultList = mutableListOf<TeamTable>()
                    for (row: TeamTable in team) {
                        val tempInput = row.teamName?.toLowerCase()
                        if (tempInput != null) {
                            if (tempInput.contains(myInput)) {
                                resultList.add(row)
                            }
                        }
                    }
                    resultList
                }
                val resultLists = Filter.FilterResults()
                resultLists.values = filterList
                return resultLists
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                filterList = results!!.values as MutableList<TeamTable>
                notifyDataSetChanged()
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): FavoriteTeamHolder {
        return FavoriteTeamHolder(TeamFavUI().createView(AnkoContext.create(parent.context, parent)))
    }

    override fun getItemCount(): Int = filterList.size

    override fun onBindViewHolder(holder: FavoriteTeamHolder, position: Int) {
        holder.bindItem(filterList[position], listener)
    }
}

class FavoriteTeamHolder(view: View) : RecyclerView.ViewHolder(view) {
    private val teamImage: ImageView = view.findViewById(R.id.imageOne)
    private val teamName: TextView = view.findViewById(R.id.teamOne)
    private val teamleague: TextView = view.findViewById(R.id.teamTwo)
    private val teamManager: TextView = view.findViewById(R.id.text2)
    fun bindItem(team: TeamTable, listener: (TeamTable) -> Unit) {
        Picasso.get().load(team.teamBadge).into(teamImage)
        teamName.text = team.teamName
        teamleague.text = team.teamLeague
        teamManager.text = team.teamManger
        itemView.setOnClickListener {
            listener(team)
        }
    }
}

class TeamFavUI : AnkoComponent<ViewGroup> {
    override fun createView(ui: AnkoContext<ViewGroup>): View {
        return with(ui) {
            linearLayout {
                orientation = LinearLayout.VERTICAL
                lparams(matchParent, wrapContent)

                linearLayout {
                    weightSum = 3f
                    lparams(matchParent, wrapContent) {
                        margin = 50
                    }
                    imageView {
                        id = R.id.imageOne
                    }.lparams(0, 200, 1f)
                    linearLayout {
                        orientation = LinearLayout.VERTICAL
                        lparams(0, wrapContent, 2f)

                        /* NAMA */
                        textView {
                            id = R.id.teamOne
                        }.lparams(matchParent, wrapContent)

                        /* LEAGUE */
                        textView {
                            id = R.id.teamTwo
                        }.lparams(matchParent, wrapContent)

                        /* MANAGER */
                        textView {
                            id = R.id.text2
                        }.lparams(matchParent, wrapContent)
                    }
                }

                /* Garis */
                linearLayout {
                    lparams(matchParent, 4)
                    backgroundColor = Color.BLACK
                }
            }
        }
    }
}