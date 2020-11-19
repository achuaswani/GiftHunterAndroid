package gifthunter.ras.com.gifthunter.Quiz

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.widget.TextView
import gifthunter.ras.com.gifthunter.Models.AllPalyers
import gifthunter.ras.com.gifthunter.R
import gifthunter.ras.com.gifthunter.Utils.Util
import kotlinx.android.synthetic.main.activity_quiz_result.*

class QuizResult : AppCompatActivity() {
    var allPlayers: ArrayList<AllPalyers>? = arrayListOf()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz_result)
        allPlayers = Util.getScoreBoardDetails()
        setUpView()
    }

    fun setUpView() {
        if (allPlayers == null || allPlayers!!.isEmpty()) return
        var allPlayersArray = ArrayList(allPlayers!!).sortedByDescending { it.score.toInt() }
        var firstTen = allPlayersArray!!.subList(0, 9)
        var index = 0
        var color = this.getResources().getIntArray(R.array.result_colors)
        for((index, value) in firstTen!!.withIndex()) {
            createTextView(value, color[index])
        }
    }

    fun createTextView(player: AllPalyers, color: Int) {
        val tv_name = TextView(this)
        tv_name.textSize = 30f
        tv_name.text = player.name
        tv_name.gravity = Gravity.CENTER
        tv_name.setBackgroundColor(color)
        ll_main_layout.addView(tv_name)

        val tv_score = TextView(this)
        tv_score.textSize = 25f
        tv_score.text = player.score
        tv_score.gravity = Gravity.CENTER
        tv_score.setBackgroundColor(color);
        ll_main_layout.addView(tv_score)
    }
}