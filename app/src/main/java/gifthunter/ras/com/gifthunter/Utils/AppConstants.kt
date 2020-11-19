package gifthunter.ras.com.gifthunter.Utils

object AppConstants {
    // Intent extra to switch views for Cast
    const val SWITCH_TO_GAMERROM = 1
    const val SWITCH_TO_SCOREBOARD = 2
    const val SWITCH_TO_FINALSCORE = 3

    // Intent messages
    const val INTENT_MSG_ID = "id"
    const val INTENT_MSG_PIN = "PIN"
    const val INTENT_MSG_NAME = "name"
    const val INTENT_MSG_DESC = "desc"
    const val INTENT_MSG_QUESTION_TITLE = "questionText"
    const val INTENT_MSG_QUESTION_ANSWER = "answer"
    const val INTENT_MSG_QUESTION_FILE_PATH = "media"

    // Firebase Nodes
    const val NODE_ALLPLAYERS = "AllPlayers"
    const val NODE_ACTIVEQUIZ = "ActiveQuiz"
    const val NODE_USERS = "Profile"
    const val NODE_QUESTIONSLIST = "QuestionsList"
    const val NODE_QUIZZESLIST = "QuizzesList"
    const val NODE_SCOREBOARDS = "ScoreBoards"
    const val NODE_TOTALPOINTS = "points"
    //Constants
    const val MAX_SECONDS = 30
    const val POINTS_MULTIPLIER = 10

}