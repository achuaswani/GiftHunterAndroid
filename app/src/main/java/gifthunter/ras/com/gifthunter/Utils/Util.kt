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
    var databasePinREF = MainActivity.database.reference.child(AppConstants.NODE_ACTIVEQUIZ)
    var databaseQuestinsRef = MainActivity.database.reference.child(AppConstants.NODE_QUESTIONSLIST)
    var userRef = MainActivity.databaseRootRef.child(AppConstants.NODE_USERS)
    var databaseAScoreBoardREF = MainActivity.database.reference.child(AppConstants.NODE_SCOREBOARDS)
    private var quizId:String = ""
    private var profileData = ProfileModel()
    private var loggedUser = UserModel()

    fun getMeAsUser(): UserModel {
        loggedUser = UserModel(MainActivity.loggedUser.uid, MainActivity.loggedUser.email.toString())
        return  loggedUser
    }

    fun getUserData(profileModel: (ProfileModel?) -> Unit) {
        if(MainActivity.mAuth.currentUser != null) {
            val db = userRef.child(MainActivity.loggedUser.uid)
            db.addValueEventListener(object : ValueEventListener {
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
        userRef.child(MainActivity.loggedUser.uid).setValue(profileData)
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
        val quizRef = databasePinREF.child(pin)
        quizRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) { status(false) }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {

                    val mQuiz = dataSnapshot.getValue(Quiz::class.java)!!
                    for (player: DataSnapshot in dataSnapshot.child(AppConstants.NODE_ALLPLAYERS).children) {
                        val player = player.getValue(AllPalyers::class.java)!!
                        mQuiz.allPlayers.add(player)
                    }
                    localQuizDatabase.quizzes.add(mQuiz)
                    quizId = mQuiz.quizId
                    status(true)
                }
            }
        })
    }

    fun getQuestion(status: (Boolean) -> Unit) {
        val questionsRef = databaseQuestinsRef.child(quizId)
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
        databasePinREF.addListenerForSingleValueEvent(object : ValueEventListener {
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

    fun getQuizData() : Quiz? {
       for (quiz in localQuizDatabase.quizzes) {
           if(quiz.quizId == quizId) {
               return quiz
           }
       }
        return null
    }

    fun getScoreBoardDetails() : ArrayList<AllPalyers>? {
        return getQuizData()?.allPlayers
    }

    fun scoreboardInit(pin: String, score: String) {

        databasePinREF.child(pin).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                var scoreCard = AllPalyers(profileData.displayName.toString(), score)
                var count = getScoreBoardDetails()?.size ?: 0
                databasePinREF.child(pin).child(AppConstants.NODE_ALLPLAYERS)
                        .child(count.toString()).setValue(scoreCard)
                saveScoreToProfile(score)
            }

            override fun onCancelled(p0: DatabaseError) {}
        })
    }

    fun saveScoreToProfile(score: String) {
        var points = profileData.points?.toInt() ?: 0
        var totalPoints = (points + score.toInt()).toString()
        loggedUser.uid?.let { userRef.child(it).child(AppConstants.NODE_TOTALPOINTS).setValue(totalPoints) }
        profileData.points = totalPoints
    }

}