package gifthunter.ras.com.gifthunter.Quiz

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import android.app.AlertDialog
import gifthunter.ras.com.gifthunter.R
import gifthunter.ras.com.gifthunter.Utils.AppConstants
import gifthunter.ras.com.gifthunter.Utils.Util

class QuizFragment : Fragment() {
    lateinit var mView: View
    private var mListener: QuizFragment.OnFragmentInteractionListener? = null
    private var pin = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        print("test--quiz")
        mView = inflater.inflate(R.layout.fragment_quiz, container, false)
        var startQuizButton = mView.findViewById<Button>(R.id.start_quiz)
        startQuizButton.setOnClickListener {
            createDialog(0)
        }

        var viewResultsButton = mView.findViewById<Button>(R.id.view_results)
        viewResultsButton.setOnClickListener {
            createDialog(1)
        }
        return mView
    }


    override fun setUserVisibleHint(visible: Boolean) {
        if (visible) {

        } else {
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
            mListener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        mListener = null
    }

    interface OnFragmentInteractionListener {
        fun onFragmentInteraction(uri: Uri)
    }


    private fun createDialog(tag: Int) {
        var mBuilder = AlertDialog.Builder(context!!)
        val mView = layoutInflater.inflate(R.layout.dialog_enterpin, null)
        val field = mView.findViewById<EditText>(R.id.et_enterpin)
        val confirmButton = mView.findViewById<Button>(R.id.pin_confirm_button)
        val cancelButton = mView.findViewById<Button>(R.id.pin_cancel_button)
        mBuilder.setView(mView)
        val mDialog = mBuilder.create()
        confirmButton.setOnClickListener {
            pin = field.text.toString().trim()
            if (pin.isEmpty())
                Toast.makeText(context, getString(R.string.invalid_pin_toast), Toast.LENGTH_LONG).show()
            else {
                val pin = field.text.toString().trim()
                Util.validatePIN(pin) { response ->
                    if(response){
                        confirmationHandler(tag)
                        mDialog.dismiss()
                    } else {
                        mDialog.dismiss()
                        Toast.makeText(context, getString(R.string.no_active_quiz), Toast.LENGTH_LONG).show()
                    }
                }
            }
        }

        cancelButton.setOnClickListener {
            mDialog.cancel()
        }
        mDialog.show()
    }

    fun confirmationHandler(tag: Int) {
        Util.fetchQuizSet(pin) { success ->
            if (success && tag == 0) {
                openQuestions()
            } else if (tag == 1) {

                Util.scoreboardFromDatabase(pin) {
                    val intentToOpenReuslts = Intent(context, QuizResult::class.java)
                    startActivity(intentToOpenReuslts)
                }
            } else {
                Toast.makeText(context, getString(R.string.no_active_quiz), Toast.LENGTH_LONG).show()
            }
        }
    }

    fun openQuestions() {
        Util.getQuestion() {success ->
            if (success) {
                val intentToOpenQuestion = Intent(context, QuestionsActivity::class.java)
                intentToOpenQuestion.putExtra(AppConstants.INTENT_MSG_PIN, pin)
                startActivity(intentToOpenQuestion)
            }
        }
    }
}
