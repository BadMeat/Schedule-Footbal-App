package com.example.user.sceball.adapter

import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.provider.CalendarContract
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.example.user.sceball.R
import com.example.user.sceball.convertDate
import com.example.user.sceball.convertGMT
import com.example.user.sceball.model.MatchEvent
import com.example.user.sceball.myDate
import org.jetbrains.anko.*
import java.util.*

/**
 * Created by Bencoleng on 11/11/2018.
 */
class PassAdapter(
    private var passEvent: MutableList<MatchEvent>,
    private val listener: (MatchEvent) -> Unit
) : RecyclerView.Adapter<PassEventHolder>(), Filterable {

    private var itemFilter: MutableList<MatchEvent> = passEvent

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(input: CharSequence?): FilterResults {
                val myInput: String = input.toString().toLowerCase()
                itemFilter = if (input.isNullOrEmpty()) {
                    passEvent
                } else {
                    val resultList = mutableListOf<MatchEvent>()
                    for (row: MatchEvent in passEvent) {
                        val tempInput = row.nmHome?.toLowerCase()
                        val tempInput2 = row.nmAway?.toLowerCase()
                        if (tempInput != null && tempInput2 != null) {
                            if (tempInput.contains(myInput) || tempInput2.contains(myInput)) {
                                resultList.add(row)
                            }
                        }
                    }
                    resultList
                }
                val filterResults = Filter.FilterResults()
                filterResults.values = itemFilter
                return filterResults
            }

            override fun publishResults(input: CharSequence?, pilter: FilterResults?) {
                itemFilter = pilter!!.values as MutableList<MatchEvent>
                notifyDataSetChanged()
            }

        }
    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): PassEventHolder {
        return PassEventHolder(
            PassEventUI().createView(
                AnkoContext.create(p0.context, p0)
            )
        )
    }

    override fun getItemCount(): Int = itemFilter.size

    override fun onBindViewHolder(holder: PassEventHolder, position: Int) {
        holder.bindItem(itemFilter[position], listener)
    }
}

class PassEventUI : AnkoComponent<ViewGroup> {
    override fun createView(ui: AnkoContext<ViewGroup>): View {
        return with(ui) {
            linearLayout {
                orientation = LinearLayout.VERTICAL
                lparams(matchParent, wrapContent) {
                    margin = 10
                }
                padding = dip(10)


                linearLayout {
                    lparams(matchParent, wrapContent)
                    weightSum = 10f

                    textView {
                        id = R.id.jadwal
                        gravity = Gravity.CENTER
                    }.lparams(0, wrapContent, 9f) {
                        topMargin = dip(10)
                        bottomMargin = dip(5)
                        gravity = Gravity.CENTER
                        leftPadding = 50
                    }
                    button {
                        id = R.id.addevent
                        background = ContextCompat.getDrawable(ctx, R.drawable.ic_notifications_active_black_24dp)
                    }.lparams(0, 100, 1f)
                }


                textView {
                    id = R.id.jam
                    gravity = Gravity.CENTER
                }.lparams(matchParent, wrapContent) {
                    bottomMargin = dip(10)
                }

                /*
                 * Team and Vs
                 */
                linearLayout {
                    orientation = LinearLayout.HORIZONTAL
                    lparams(width = matchParent)
                    textView {
                        id = R.id.teamOne
                        gravity = Gravity.START
                    }.lparams(0, wrapContent) {
                        weight = 1f
                    }

                    lparams(width = matchParent)
                    textView {
                        id = R.id.goal_one
                        gravity = Gravity.CENTER
                        typeface = Typeface.defaultFromStyle(Typeface.BOLD)
                    }.lparams(0, wrapContent, 1f)



                    textView {
                        text = resources.getString(R.string.vs)
                        gravity = Gravity.CENTER
                    }.lparams(0, wrapContent) {
                        weight = 1f
                    }

                    lparams(width = matchParent)
                    textView {
                        id = R.id.goal_two
                        gravity = Gravity.CENTER
                        typeface = Typeface.defaultFromStyle(Typeface.BOLD)
                    }.lparams(0, wrapContent, 1f)

                    textView {
                        id = R.id.teamTwo
                        gravity = Gravity.END
                    }.lparams(0, wrapContent) {
                        weight = 1f
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

class PassEventHolder(view: View) : RecyclerView.ViewHolder(view) {

    private val teamOne: TextView = view.find(R.id.teamOne)
    private val teamTwo: TextView = view.find(R.id.teamTwo)
    private val jadwal: TextView = view.find(R.id.jadwal)
    private val jam: TextView = view.find(R.id.jam)
    private val buttonEvent: Button = view.find(R.id.addevent)

    /*
     * Goal
     */
    private val goalOne: TextView = view.find(R.id.goal_one)
    private val goalTwo: TextView = view.find(R.id.goal_two)

    fun bindItem(teams: MatchEvent, listener: (MatchEvent) -> Unit) {
        teamOne.text = teams.nmHome
        teamTwo.text = teams.nmAway
        jadwal.text = convertDate(teams.dateEvent, teams.dateEvent + " " + teams.strTime)
        jam.text = convertGMT(teams.dateEvent + " " + teams.strTime)
        val scoreOne: String? = if (teams.intHomeScore == null) {
            ""
        } else {
            teams.intHomeScore.toString()
        }

        val scoreTwo: String? = if (teams.intAwayScore == null) {
            ""
        } else {
            teams.intAwayScore.toString()
        }
        goalOne.text = scoreOne
        goalTwo.text = scoreTwo
        buttonEvent.setOnClickListener {
            val e: Date = myDate(teams.dateEvent + " " + teams.strTime)
            val calDate = GregorianCalendar()
            calDate.time = e
            val intent: Intent = Intent(Intent.ACTION_INSERT)
                .setData(CalendarContract.Events.CONTENT_URI)
                .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, calDate.timeInMillis)
                .putExtra(CalendarContract.Events.TITLE, "${teams.nmHome} VS ${teams.nmAway}")
                .putExtra(CalendarContract.Events.DESCRIPTION, "${teams.nmHome} VS ${teams.nmAway}")
                .putExtra(CalendarContract.Events.EVENT_LOCATION, "Dicoding")
                .putExtra(CalendarContract.Events.AVAILABILITY, CalendarContract.Events.AVAILABILITY_BUSY)
                .putExtra(Intent.EXTRA_EMAIL, "wing.cronus@gmail.com,Arif Dwi Prasetya")
            itemView.context.startActivity(intent)

        }
        itemView.setOnClickListener {
            listener(teams)
        }
    }
}