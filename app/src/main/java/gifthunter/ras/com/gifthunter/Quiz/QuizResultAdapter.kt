package gifthunter.ras.com.gifthunter.Quiz

import android.app.Activity
import android.graphics.Color
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import gifthunter.ras.com.gifthunter.Models.Player
import gifthunter.ras.com.gifthunter.R

class QuizResultAdapter (private val context: Activity, private val players: ArrayList<Player>):
        ArrayAdapter<Player>(context, R.layout.quiz_result_list, players) {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val inflater = context.layoutInflater
        val rowView = inflater.inflate(R.layout.quiz_result_list, null)
        val playerName = rowView.findViewById(R.id.playerName) as TextView
        val imageView = rowView.findViewById(R.id.prizeIcon) as ImageView
        val playerPoint = rowView.findViewById(R.id.playerPoint) as TextView
        playerName.text = players[position].name
        playerPoint.text = players[position].score.toString()
        rowView.setEnabled(false);
        rowView.setOnClickListener(null);
        if (players[position].rank == 1) {
            imageView.setImageResource(R.drawable.ic_undraw_winners)
        }
        var colorArray = context.getResources().getIntArray(R.array.result_colors)

        rowView.setBackgroundColor(colorArray[position])

        return rowView
    }

    override fun getCount(): Int {
        return players.size
    }

    override fun getItem(position: Int): Player {
        return players[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

}