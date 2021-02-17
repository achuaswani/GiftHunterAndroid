package gifthunter.ras.com.gifthunter.Utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import gifthunter.ras.com.gifthunter.MainActivity
import gifthunter.ras.com.gifthunter.Models.*

object Util {
    private var databasePINReference = MainActivity.database.reference.child(AppConstants.NODE_ACTIVEQUIZ)
    private var databaseQuestinsReference = MainActivity.database.reference.child(AppConstants.NODE_QUESTIONSLIST)
    private var databaseScoreBoardReference = MainActivity.database.reference.child(AppConstants.NODE_SCOREBOARDS)
    private var quizId:String = ""
    var players = arrayListOf<String>()

    fun fetchQuizSet(pin: String, status: (Boolean) -> Unit) {
        val quizRef = databasePINReference.child(pin)
        quizRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) { status(false) }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {

                    val mQuiz = dataSnapshot.getValue(Quiz::class.java)!!
                    localQuizDatabase.quizzes.add(mQuiz)
                    quizId = mQuiz.quizId
                    status(true)
                }
            }
        })
    }

    fun getQuestion(status: (Boolean) -> Unit) {
        val questionsRef = databaseQuestinsReference.child(quizId)

        localQuestionBoard.questionSet.clear()

        questionsRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                var questionNumber = 0
                for (question: DataSnapshot in p0.children) {
                    val mQuestion = question.getValue(Question::class.java)!!
                    localQuestionBoard.questionSet.add(questionNumber++, mQuestion)
                }
                status(true)
            }

            override fun onCancelled(p0: DatabaseError) {
                status(false)
            }
        })
    }

    fun validatePIN(pin: String, success: (Boolean) -> Unit) {
        databasePINReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                if (p0.hasChild(pin)) {
                   success(true)
                } else {
                    success(false)
                }
            }

            override fun onCancelled(p0: DatabaseError) {}
        })
    }

    fun scoreboardFromDatabase(pin: String, userName: String, success: (Boolean) -> Unit) {
        var cnt = 0
        var arrTemp = arrayListOf<ScoreBoard>()

        localPlayersScores.scoreboard.clear()

        databaseScoreBoardReference.child(pin).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                for (player in p0.children) {
                    val playerName = player.key.toString()
                    val playerPoints = player.child("score").value as Long
                    val player = ScoreBoard(playerName, playerPoints)
                    if (playerName == userName) {
                        localPlayersScores.currentUserScore = player
                    }
                    arrTemp.add(cnt++, player)
                }

                for (pass in 0 until arrTemp.size) {
                    for (currentPoistion in pass+1 until arrTemp.size) {
                        if ((arrTemp[pass].score) < (arrTemp[currentPoistion].score)) {
                            val tmp = arrTemp[pass]
                            arrTemp[pass] = arrTemp[currentPoistion]
                            arrTemp[pass].rank = pass + 1
                            arrTemp[currentPoistion] = tmp
                        }
                    }
                }
                localPlayersScores.scoreboard.addAll(arrTemp)
                success(true)
            }

            override fun onCancelled(p0: DatabaseError) { success(false) }
        })
    }

    fun addPointsToDatabase(score: Int, pin: String, userName: String) {
        databaseScoreBoardReference.child(pin).child(userName).child("score").setValue(score)
    }

    fun scoreboardInit(pin: String) {
        players = arrayListOf()
        databaseScoreBoardReference.child(pin).child(AppConstants.NODE_ALLPLAYERS).addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    for (player: DataSnapshot in snapshot.children) {
                        players.add(player.child("name").value as String)
                    }
                }
                override fun onCancelled(p0: DatabaseError) {}

        })

        databaseScoreBoardReference.child(pin).child(AppConstants.NODE_ALLPLAYERS).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                for (player in players) {
                    databasePINReference.child(pin).child(player).child("score").setValue(0)
                }
            }

            override fun onCancelled(p0: DatabaseError) {}
        })
    }
}