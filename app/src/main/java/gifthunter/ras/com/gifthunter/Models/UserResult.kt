package gifthunter.ras.com.gifthunter.Models

data class UserResult (
        var pin: String = "",
        var userName: String = "",
        var score: Int = 0,
        var quizDetails: Quiz
)
