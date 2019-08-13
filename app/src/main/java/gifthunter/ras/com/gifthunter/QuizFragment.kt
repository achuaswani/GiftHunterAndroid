package gifthunter.ras.com.gifthunter

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import gifthunter.ras.com.gifthunter.R.styleable.AlertDialog
import kotlinx.android.synthetic.main.fragment_quiz.*
import kotlinx.android.synthetic.main.fragment_quiz.view.*
import android.os.CountDownTimer
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_user_data.*


/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [QuizFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [QuizFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class QuizFragment : Fragment() {

    // TODO: Rename and change types of parameters
    private var mParam1: String? = null
    private var mParam2: String? = null
    var pickedAnswer : String = "1"
    var questionNumber : Int = 0
    var totalCount: Int = 0
    var score : Int = 0
    var correctAnswer: String = "0"
    var level = 1
    var mDatabase = FirebaseDatabase.getInstance().getReference("QA")
    var mAuth: FirebaseAuth? = null
    var itemsRez = ArrayList<Question>()
    var counteDown: TextView? = null
    private var mListener: OnFragmentInteractionListener? = null
    val userData = UserData.instance
    lateinit var timer: CountDownTimer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            mParam1 = arguments!!.getString(ARG_PARAM1)
            mParam2 = arguments!!.getString(ARG_PARAM2)
        }
        retrieveData()

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        print("test--quiz")
        val view = inflater.inflate(R.layout.fragment_quiz, container, false)
        counteDown = view.findViewById<TextView>(R.id.countDown)

        view.opt1.setOnClickListener {
                pickedAnswer = view.opt1.tag as String
                checkAnswer()
        }
        view.opt2.setOnClickListener {
                pickedAnswer = view.opt2.getTag() as String
                checkAnswer()
        }
        view.opt3.setOnClickListener {
                pickedAnswer = view.opt3.getTag() as String
                checkAnswer()
        }
        view.opt4.setOnClickListener {
                pickedAnswer = view.opt4.getTag() as String
                checkAnswer()
        }

        return view
    }

    override fun setUserVisibleHint(visible: Boolean) {
        super.setUserVisibleHint(visible)
        print("\nonHiddenChanged-----------------------"+visible)
        var minute = userData.Minutes
        if(minute == 0){

        }
        var milseconds: Long = (minute * 60 * 1000).toLong()
        timer = object : CountDownTimer(milseconds, 60*1000) {

            override fun onTick(millisUntilFinished: Long) {

                minute = ((millisUntilFinished/1000)/60).toInt()
                if(counteDown != null) {
                    counteDown!!.setText("Minutes Remaining: " + minute);
                    userData.Minutes = minute+1

                }
            }

            override fun onFinish() {
                print("Finish")
                userData.Minutes = 0
            }
        }
            if (visible) {
                timer.start()

            } else {
                timer.cancel()
            }
    }

    // TODO: Rename method, update argument and hook method into UI event
    fun onButtonPressed(uri: Uri) {
        if (mListener != null) {
            mListener!!.onFragmentInteraction(uri)
        }
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
            mListener = context
        } else {
            throw RuntimeException(context!!.toString() + " must implement OnFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        mListener = null
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     *
     *
     * See the Android Training lesson [Communicating with Other Fragments](http://developer.android.com/training/basics/fragments/communicating.html) for more information.
     */
    interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        fun onFragmentInteraction(uri: Uri)
    }

    companion object {
        // TODO: Rename parameter arguments, choose names that match
        // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
        private val ARG_PARAM1 = "param1"
        private val ARG_PARAM2 = "param2"

        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment QuizFragment.
         */
        // TODO: Rename and change types and number of parameters
        fun newInstance(param1: String, param2: String): QuizFragment {
            val fragment = QuizFragment()
            val args = Bundle()
            args.putString(ARG_PARAM1, param1)
            args.putString(ARG_PARAM2, param2)
            fragment.arguments = args
            return fragment
        }
    }
    fun retrieveData(){
        println("retrieveData")
        mAuth = FirebaseAuth.getInstance();
        val currentUser = mAuth?.getCurrentUser()
        println("currentUser --- $currentUser")
       // var allQuestions = mutableMapOf<String,Any>()

        var levelString = "Level$level"
        val db = mDatabase.child("FirstStd").child(levelString)
        println("db --- $levelString")
        db.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError?) {
                println("onCancelled ---")

                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                questionNumber = 0
                //adding artist to the list
                if (dataSnapshot.exists()) {

                    for (singleSnapshot in dataSnapshot.children) {
                        var qstntxt = singleSnapshot.key.toString()
                        var opt1txt = ""
                        var opt2txt = ""
                        var opt3txt = ""
                        var opt4txt = ""
                        var ans = "0"
                       // println("item--- $qstntxt")
                        val children = singleSnapshot!!.children
                        children.forEach {
                            if(it.key.toString()=="opt1") {
                                opt1txt = it.value.toString()
                            }
                            if(it.key.toString()=="opt2") {
                                opt2txt = it.value.toString()
                            }
                            if(it.key.toString()=="opt3") {
                                opt3txt = it.value.toString()
                            }
                            if(it.key.toString()=="opt4") {
                                opt4txt = it.value.toString()
                            }
                            if(it.key.toString()=="correctanswer") {
                                ans = it.value.toString()
                            }


                            }
                       itemsRez.add(Question(qstntxt,ans,opt1txt,opt2txt,opt3txt,opt4txt))

                        questionNumber++

                    }
                    totalCount = itemsRez.count()
                    questionNumber = 0

                    getQuestion()
                }
            }


        })
    }
     fun getQuestion(){
         if(questionNumber<itemsRez.count()){
             //updateUI()
             var firstQuestion = itemsRez[questionNumber]
             print(itemsRez[questionNumber].option1)
             qstnId.text = firstQuestion.questionText
             opt1.setText(firstQuestion.option1)
             opt2.setText(firstQuestion.option2)
             opt3.setText(firstQuestion.option3)
             opt4.setText(firstQuestion.option4)
             correctAnswer = firstQuestion.answer
             updateUI()
         }else{
             questionNumber = 0
             itemsRez = ArrayList<Question>()
             level++
             retrieveData()
         }

     }
     fun updateUI(){
         scorelbl.text = "Score: ${score.toString()}"
         count.text = "${totalCount.toString()}/${(questionNumber+1 / totalCount).toString()}"
         //progress.text = (questionNumber+1 / totalCount).toString()

         //progress.getLayoutParams().width = (progress.getLayoutParams().width/totalCount) * questionNumber+1
         print(progress.getLayoutParams().width)
     }

    fun checkAnswer(){
        if(correctAnswer == pickedAnswer){
            score = score + 1
            //ProgressHUD.showSuccess("Correct")
        }else{
            //ProgressHUD.showError("Wrong")
        }
        questionNumber = questionNumber+1
        getQuestion()

    }



}
