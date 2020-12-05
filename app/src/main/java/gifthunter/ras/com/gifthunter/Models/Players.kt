package gifthunter.ras.com.gifthunter.Models

import gifthunter.ras.com.gifthunter.MainActivity

data class Player (
        var name: String = "",
        var score: Long = 0,
        var rank: Int = 0
)
object localPlayersScores {
    var scoreboard = mutableListOf<Player>()
    var currentUserScore: Player = Player()
}