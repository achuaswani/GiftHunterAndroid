package gifthunter.ras.com.gifthunter

/**
 * Created by U26448 on 10/15/18.
 */
class Question {
    var questionText : String = ""
    var answer : String = "0"
    var option1: String = ""
    var option2: String = ""
    var option3: String = ""
    var option4: String = ""
    constructor() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    constructor(qstntx: String, ans: String, opt1: String?, opt2: String?, opt3: String?, opt4: String?) {
        this.questionText = qstntx!!
        this.option1 = opt1!!
        this.option2 = opt2!!
        this.option3 = opt3!!
        this.option4 = opt4!!
        this.answer = ans!!
    }
    fun getQuestion(): String {
        return questionText
    }
    fun getOptions(): String {
        return this.option1
    }

    fun getTrueResult(): String {
        return answer
    }
}