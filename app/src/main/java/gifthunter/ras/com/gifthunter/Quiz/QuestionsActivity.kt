package gifthunter.ras.com.gifthunter.Quiz

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.widget.TextView
import android.widget.Toast
import gifthunter.ras.com.gifthunter.Models.localQuestionBoard
import gifthunter.ras.com.gifthunter.R
import gifthunter.ras.com.gifthunter.Utils.AppConstants
import gifthunter.ras.com.gifthunter.Utils.ProfileDataService
import gifthunter.ras.com.gifthunter.Utils.Util
import kotlinx.android.synthetic.main.activity_questions.*

class QuestionsActivity : AppCompatActivity() {

    var questionNumber: Int = 0
    var questionId: String = ""
    var previousQuestionId: String = ""
    var totalCount: Int = 1
    var score: Int = 0
    var correctAnswer: String = "0"
    var countDown: TextView? = null
    lateinit var timer: CountDownTimer
    private var elapsedTime = 0L
    var pin: String = ""
    private var scoreAlgorithm: Int = ((AppConstants.MAX_SECONDS - elapsedTime) * AppConstants.POINTS_MULTIPLIER).toInt()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_questions)
        pin = intent.getStringExtra(AppConstants.INTENT_MSG_PIN).toString()
        setupView()
    }

    private fun setupView() {
        if (localQuestionBoard.questionSet.size != 0) {
            countDown = findViewById<TextView>(R.id.countDown)
            totalCount = localQuestionBoard.questionSet.size
            timerSetup()
            setUpButtons()
            updateQuestions()
        }
    }

    fun timerSetup() {
        timer = object : CountDownTimer(30000, 1000) {

            override fun onTick(millisUntilFinished: Long) {
                elapsedTime = millisUntilFinished/1000
                if(countDown != null) {
                    countDown!!.setText("Minutes Remaining: " + elapsedTime);
                }
            }

            override fun onFinish() {
                print("Finish")

                checkAnswer(null)
            }
        }
    }

    fun setUpButtons() {
        opt1.setOnClickListener {
            timer.cancel()
            checkAnswer(0)
        }
        opt2.setOnClickListener {
            timer.cancel()
            checkAnswer(1)
        }
        opt3.setOnClickListener {
            timer.cancel()
            checkAnswer(2)
        }
        opt4.setOnClickListener {
            timer.cancel()
            checkAnswer(3)
        }
    }

    fun updateQuestions() {
        if (questionNumber < localQuestionBoard.questionSet.size) {
            timer.start()
            var questionData = localQuestionBoard.questionSet[questionNumber]
            correctAnswer = questionData.answer
            questionId = questionData.id
            questionText.text = questionData.questionText
            opt1.text = questionData.options[0]
            opt2.text = questionData.options[1]
            opt3.text = questionData.options[2]
            opt4.text = questionData.options[3]
            count.text = "${(questionNumber+1)} / ${totalCount.toString()}"
        } else {
            Toast.makeText(this, getString(R.string.quiz_done), Toast.LENGTH_LONG).show()
            finish();
            val userName = ProfileDataService.profile?.userName
            if (userName != null) {
                Util.scoreboardFromDatabase(pin, userName) {
                    if (it) {
                        val intent = Intent(this, QuizResult::class.java)
                        startActivity(intent)
                    } else {
                        Toast.makeText(this, getString(R.string.general_error), Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
        scorelbl.text = "Score: ${score.toString()}"
        progress.getLayoutParams().width = (progress.getLayoutParams().width / totalCount) * questionNumber+1
    }


    fun checkAnswer(buttonID: Int?) {
        if (previousQuestionId != questionId) {
            var correctAnswerValue = localQuestionBoard.questionSet[questionNumber].options[correctAnswer.toInt()]
            var message = getString(R.string.wrong_answer) + " ${correctAnswerValue}"

            if (buttonID != null) {
                if(correctAnswer == buttonID.toString()) {
                    score += scoreAlgorithm
                    val userName = ProfileDataService.profile?.userName
                    if (userName != null) {
                        Util.addPointsToDatabase(score, pin, userName)
                    }
                    message = getString(R.string.correct_answer) + " ${correctAnswerValue}"
                }
            } else {
                message = getString(R.string.time_up) + " ${correctAnswerValue}"
            }
            questionNumber = questionNumber + 1
            previousQuestionId = questionId
            previousQuestionResultId.text = message
        }
        updateQuestions()
    }
}