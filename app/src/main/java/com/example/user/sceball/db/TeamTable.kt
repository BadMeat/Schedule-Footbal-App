package com.example.user.sceball.db

data class TeamTable(
    val id: Long?, val teamId: String?,
    val teamName: String?, val teamLeague: String?,
    val teamBadge: String?, val teamManger: String?
) {
    companion object {
        const val TABLE_TEAM: String = "TABLE_TEAM"
        const val ID: String = "ID_"
        const val TEAM_ID: String = "TEAM_ID"
        const val TEAM_NAME: String = "TEAM_NAME"
        const val TEAM_LEAGUE: String = "TEAM_LEAGUE"
        const val TEAM_BADGE: String = "TEAM_BADGE"
        const val TEAM_MANAGER: String = "TEAM_MANAGER"
    }
}