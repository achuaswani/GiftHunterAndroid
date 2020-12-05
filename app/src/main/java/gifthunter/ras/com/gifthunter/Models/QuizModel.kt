package gifthunter.ras.com.gifthunter.Models

data class Quiz(
        var quizId: String = "",
        var title: String = "",
        var quizDetails: String =  "",
        var scoreBoardId: String = "",
        var hostId: String = "",
)


object localQuizDatabase {
    var quizzes = mutableListOf<Quiz>()
}