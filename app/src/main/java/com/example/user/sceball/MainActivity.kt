package com.example.user.sceball

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.WindowManager
import com.imangazaliev.circlemenu.CircleMenu
import com.imangazaliev.circlemenu.CircleMenuButton
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.intentFor


class MainActivity : AppCompatActivity() {

    override fun onResume() {
        super.onResume()
        window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportActionBar?.title = "SceBall Main Menu"

        circleMenu.setEventListener(object : CircleMenu.EventListener {
            override fun onMenuOpenAnimationStart() {

            }

            override fun onMenuOpenAnimationEnd() {

            }

            override fun onMenuCloseAnimationStart() {

            }

            override fun onMenuCloseAnimationEnd() {

            }

            override fun onButtonClickAnimationStart(menuButton: CircleMenuButton) {
                window.setFlags(
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
                )
            }

            override fun onButtonClickAnimationEnd(menuButton: CircleMenuButton) {
                if (menuButton.id == R.id.favorite) {
                    startActivity(intentFor<FavoriteDetailActivity>())
                }
                if (menuButton.id == R.id.pass) {
                    startActivity(intentFor<MatchActivity>())
                }
                if (menuButton.id == R.id.next) {
                    startActivity(intentFor<TeamActivity>())
                }

            }

        })
    }
}
