package gifthunter.ras.com.gifthunter.Utils

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.widget.Toast
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import gifthunter.ras.com.gifthunter.MainActivity
import gifthunter.ras.com.gifthunter.Models.*

object Util {
    var databasePINReference = MainActivity.database.reference.child(AppConstants.NODE_ACTIVEQUIZ)
    var databaseQuestinsReference = MainActivity.database.reference.child(AppConstants.NODE_QUESTIONSLIST)
    var userReference = MainActivity.databaseRootRef.child(AppConstants.NODE_USERS)
    var databaseScoreBoardReference = MainActivity.database.reference.child(AppConstants.NODE_SCOREBOARDS)
    private var quizId:String = ""
    private var profileData = ProfileModel()
    private var loggedUser = UserModel()
    var players = arrayListOf<String>()

    fun getMeAsUser(): UserModel {
        loggedUser = UserModel(MainActivity.loggedUser.uid, MainActivity.loggedUser.email.toString())
        return  loggedUser
    }

    fun getUserData(profileModel: (ProfileModel?) -> Unit) {
        if(MainActivity.mAuth.currentUser != null) {
            val db = userReference.child(MainActivity.loggedUser.uid)
            db.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if (dataSnapshot.exists()) {
                        profileData = dataSnapshot.getValue(ProfileModel::class.java)!!
                        profileModel(profileData)
                    } else {
                        profileModel(null)
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    profileModel(null)
                }
            })
        } else {
            profileModel(null)
        }
    }

    fun updateProfile(profileData: ProfileModel, success: (Boolean) -> Unit) {
        userReference.child(MainActivity.loggedUser.uid).setValue(profileData)
            .addOnSuccessListener(OnSuccessListener<Void> {
                MainActivity.profileData = profileData
                success(true)
            })
            .addOnFailureListener(OnFailureListener {
                success(false)
            })
    }

    fun isNetworkAvailable(context: Context): Boolean {
        var result = false
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val networkCapabilities = connectivityManager.activeNetwork ?: return false
            val actNw =
                    connectivityManager.getNetworkCapabilities(networkCapabilities) ?: return false
            result = when {
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                else -> false
            }
        }
        return result
    }

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

    fun scoreboardFromDatabase(pin: String, success: (Boolean) -> Unit) {
        var cnt = 0
        var arrTemp = arrayListOf<Player>()
        var arrTempSorted = ArrayList<Player>()

        localPlayersScores.scoreboard.clear()

        databaseScoreBoardReference.child(pin).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                for (player in p0.children) {
                    val playerName = player.key.toString()
                    val playerPoints = player.child("score").value as Long
                    val player = Player(playerName, playerPoints)
                    if (playerName == profileData.displayName) {
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

    fun addPointsToDatabase(score: Int, pin: String) {
        databaseScoreBoardReference.child(pin).child(profileData.displayName.toString()).child("score").setValue(score)
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

    fun saveScoreToProfile(score: Int) {
        var points = profileData.points?.toInt() ?: 0
        var totalPoints = (points + score).toString()
        userReference.child(MainActivity.loggedUser.uid).child(AppConstants.NODE_TOTALPOINTS).setValue(totalPoints)
        MainActivity.profileData.points = totalPoints
    }
}