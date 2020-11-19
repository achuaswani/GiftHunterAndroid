package gifthunter.ras.com.gifthunter.Models

data class Quiz(
        var quizId: String = "",
        var title: String = "",
        var quizDetails: String =  "",
        var scoreBoardId: String = "",
        var hostId: String = "",
        var allPlayers: ArrayList<AllPalyers> = arrayListOf()
)
data class AllPalyers (
        var name: String = "",
        var score: String = "0"
)

object localQuizDatabase {
    var quizzes = mutableListOf<Quiz>()
}