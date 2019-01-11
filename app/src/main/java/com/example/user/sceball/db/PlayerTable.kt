package com.example.user.sceball.db

data class PlayerTable(
    val id: Long?, val playerId: String?,
    val playerName: String?, val playerNat: String?,
    val playerPic: String?, val playerTeam : String?
) {
    companion object {
        const val TABLE_PLAYER: String = "TABLE_PLAYER"
        const val ID : String = "ID_"
        const val PLAYER_ID: String = "PLAYER_ID"
        const val PLAYER_NAME: String = "PLAYER_NAME"
        const val PLAYER_NAT: String = "PLAYER_NAT"
        const val PLAYER_PIC: String = "PLAYER_PIC"
        const val PLAYER_TEAM : String = "PLAYER_TEAM"
    }
}