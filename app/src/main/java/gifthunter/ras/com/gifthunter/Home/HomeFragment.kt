package gifthunter.ras.com.gifthunter.Home

import android.content.Context
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import gifthunter.ras.com.gifthunter.MainActivity
import gifthunter.ras.com.gifthunter.R
import gifthunter.ras.com.gifthunter.Models.ProfileModel
import kotlinx.android.synthetic.main.fragment_home.view.*

class HomeFragment : Fragment() {
    private var mListener: OnFragmentInteractionListener? = null
    var userData = ProfileModel()
    lateinit var mView: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        mView = inflater.inflate(R.layout.fragment_home, container, false)
        setUpView()
        return mView
    }

    fun setUpView() {
        userData = MainActivity.profileData
        mView.displayNameText.text = "Welcome, "+userData.displayName
        mView.aboutText.text = mView.aboutText.text.toString()+ userData.about
        mView.ageText.text = mView.ageText.text.toString()+userData.age
        mView.scoreText.text = mView.scoreText.text.toString()+userData.points
        mView.gradeText.text = mView.gradeText.text.toString()+userData.grade
    }

    fun onButtonPressed(uri: Uri) {
        if (mListener != null) {
            mListener!!.onFragmentInteraction(uri)
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
}
