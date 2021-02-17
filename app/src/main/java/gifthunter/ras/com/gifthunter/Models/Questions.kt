package gifthunter.ras.com.gifthunter.Models

data class Question (
        var id: String = "",
        var questionText : String = "",
        var answer : String = "0",
        var options: ArrayList<String> = arrayListOf(),
        var media: String = "")
object localQuestionBoard {
    var questionSet = arrayListOf<Question>()
}