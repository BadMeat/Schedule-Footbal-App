package com.example.user.sceball.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import org.jetbrains.anko.db.*

/**
 * Created by Bencoleng on 13/11/2018.
 */
class MyDatabaseOpenHelper(ctx: Context) : ManagedSQLiteOpenHelper(ctx, "FavoriteTeam.db", null, 1) {
    companion object {
        private var instance: MyDatabaseOpenHelper? = null

        @Synchronized
        fun getInstance(ctx: Context): MyDatabaseOpenHelper {
            if (instance == null) {
                instance = MyDatabaseOpenHelper(ctx.applicationContext)
            }
            return instance as MyDatabaseOpenHelper
        }
    }

    override fun onCreate(db: SQLiteDatabase) {
        // Here you create tables
        db.createTable(
            Favorite.TABLE_FAVORITE, true,
            Favorite.ID to INTEGER + PRIMARY_KEY + AUTOINCREMENT,
            Favorite.EVENT_ID to TEXT + UNIQUE,
            Favorite.EVENT_TIME to TEXT,
            Favorite.HOME_ID to INTEGER,
            Favorite.HOME_NAME to TEXT,
            Favorite.HOME_BADGE to TEXT,
            Favorite.HOME_SCORE to INTEGER,
            Favorite.AWAY_ID to INTEGER,
            Favorite.AWAY_NAME to TEXT,
            Favorite.AWAY_BADGE to TEXT,
            Favorite.AWAY_SCORE to INTEGER
        )

        db.createTable(
            TeamTable.TABLE_TEAM, true,
            TeamTable.ID to INTEGER + PRIMARY_KEY + AUTOINCREMENT,
            TeamTable.TEAM_ID to TEXT + UNIQUE,
            TeamTable.TEAM_NAME to TEXT,
            TeamTable.TEAM_LEAGUE to TEXT,
            TeamTable.TEAM_BADGE to TEXT,
            TeamTable.TEAM_MANAGER to TEXT
        )

        db.createTable(
            PlayerTable.TABLE_PLAYER, true,
            PlayerTable.ID to INTEGER + PRIMARY_KEY + AUTOINCREMENT,
            PlayerTable.PLAYER_ID to TEXT + UNIQUE,
            PlayerTable.PLAYER_NAME to TEXT,
            PlayerTable.PLAYER_NAT to TEXT,
            PlayerTable.PLAYER_PIC to TEXT,
            PlayerTable.PLAYER_TEAM to TEXT

        )
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // Here you can upgrade tables, as usual
        if (newVersion > oldVersion) {
            db.dropTable(Favorite.TABLE_FAVORITE, true)
            db.dropTable(TeamTable.TABLE_TEAM, true)
            db.dropTable(PlayerTable.TABLE_PLAYER, true)
        }
    }
}

// Access property for Context
val Context.database: MyDatabaseOpenHelper
    get() = MyDatabaseOpenHelper.getInstance(applicationContext)