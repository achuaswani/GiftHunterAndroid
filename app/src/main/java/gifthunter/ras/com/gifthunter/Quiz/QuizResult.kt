package gifthunter.ras.com.gifthunter.Quiz

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ListView
import gifthunter.ras.com.gifthunter.Models.ScoreBoard
import gifthunter.ras.com.gifthunter.Models.localPlayersScores
import gifthunter.ras.com.gifthunter.R
import kotlinx.android.synthetic.main.activity_quiz_result.*

class QuizResult : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz_result)
        setUpView()
    }

    private fun players(): ArrayList<ScoreBoard> {
        if (localPlayersScores.scoreboard.size > 5) {
            var counter = 1
            var previousScore: Long = 0
            var resultArray = ArrayList<ScoreBoard>()
            localPlayersScores.scoreboard.forEach {
                if (counter == 5) {
                    if (!resultArray.contains(localPlayersScores.currentUserScore)) {
                        resultArray.add(localPlayersScores.currentUserScore)
                    }
                    return resultArray
                } else if (it.score != previousScore) {
                    counter += 1
                    previousScore = it.score
                }
                resultArray.add(it)
            }
            return resultArray
        } else {
            return ArrayList(localPlayersScores.scoreboard)
        }
    }

    private fun setUpView() {

        if (localPlayersScores.scoreboard.isEmpty()) return

        val listView = ListView(this)
        val adapter = QuizResultAdapter(this, players())
        listView.adapter = adapter
        ll_main_layout.addView(listView)
    }
}