package com.example.user.sceball.adapter

import android.graphics.Color
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.example.user.sceball.R
import com.example.user.sceball.model.Player
import com.squareup.picasso.Picasso
import org.jetbrains.anko.*

/**
 * Created by Bencoleng on 18/11/2018.
 */
class PlayerAdapter(
    private val player: List<Player>,
    private val listener: (Player) -> Unit
) : RecyclerView.Adapter<PlayerHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): PlayerHolder {
        return PlayerHolder(PlayerUI().createView(AnkoContext.create(parent.context, parent)))
    }

    override fun getItemCount(): Int = player.size

    override fun onBindViewHolder(holder: PlayerHolder, position: Int) {
        holder.bindItem(player[position], listener)
    }
}

class PlayerUI : AnkoComponent<ViewGroup> {
    override fun createView(ui: AnkoContext<ViewGroup>): View {
        return with(ui) {
            linearLayout {
                lparams(matchParent, wrapContent)
                orientation = LinearLayout.VERTICAL
                linearLayout {
                    orientation = LinearLayout.HORIZONTAL
                    weightSum = 3f
                    lparams(matchParent, wrapContent) {
                        margin = 10
                    }

                    padding = dip(10)

                    /*===== Foto Pemain =====*/
                    linearLayout {
                        lparams(0, 200, 1f) {
                            margin = 10
                        }
                        imageView {
                            id = R.id.image
                            imageResource = R.drawable.ic_person_black_24dp
                        }.lparams(wrapContent, 150)
                    }

                    /*===== Informasi Pemain =====*/
                    linearLayout {
                        orientation = LinearLayout.VERTICAL
                        weightSum = 2f
                        lparams(0, matchParent, 2f) {
                            margin = 10
                        }
                        textView {
                            id = R.id.playerName
                        }.lparams(matchParent, wrapContent, 1f)
                        textView {
                            id = R.id.playerPrice
                        }.lparams(matchParent, wrapContent, 1f)
                    }
                }

                /*===== Garis Pemabatan =====*/
                linearLayout {
                    backgroundColor = Color.BLACK
                    lparams(matchParent, 3)
                }

                /*===== Deksripsi Pemain =====*/
                textView {
                    id = R.id.desc
                    maxLines = 3
                }.lparams(matchParent, wrapContent)

                /*===== Garis Pemabatan =====*/
                linearLayout {
                    backgroundColor = Color.BLACK
                    lparams(matchParent, 3)
                }
            }

        }
    }

}

class PlayerHolder(view: View) : RecyclerView.ViewHolder(view) {
    private val imageView: ImageView = view.findViewById(R.id.image)
    private val name: TextView = view.findViewById(R.id.playerName)
    private val playerPrice: TextView = view.findViewById(R.id.playerPrice)
    private val desc: TextView = view.findViewById(R.id.desc)

    fun bindItem(player: Player, listener: (Player) -> Unit) {
        println("Image Cutout ${player.strCutout}")
        if(player.strCutout != null){
            Picasso.get().load(player.strCutout).into(imageView)
        }else{
            imageView.imageResource = R.drawable.noimageicon
        }
        name.text = player.strPlayer
        desc.text = player.strDescriptionEN
        playerPrice.text = player.strWage
        itemView.setOnClickListener {
            listener(player)
        }
    }
}
