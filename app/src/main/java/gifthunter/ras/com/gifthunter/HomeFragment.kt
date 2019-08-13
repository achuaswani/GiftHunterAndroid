package gifthunter.ras.com.gifthunter

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.fragment_home.*


/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [HomeFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeFragment : Fragment() {

    // TODO: Rename and change types of parameters
    private var mParam1: String? = null
    private var mParam2: String? = null

    private var mListener: OnFragmentInteractionListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        print("onCreate-------------" )
        super.onCreate(savedInstanceState)

        if (arguments != null) {
            mParam1 = arguments!!.getString(ARG_PARAM1)
            mParam2 = arguments!!.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {


        // Inflate the layout for this fragment
        print("\nonCreateView-------------" )
        val view: View = inflater.inflate(R.layout.fragment_home, container, false)
//        // Inflate the layout for this fragment
        val txtWecome = view.findViewById<TextView>(R.id.txtusername)
        val txtAge = view.findViewById<TextView>(R.id.txtage)
        val txtScore = view.findViewById<TextView>(R.id.txtscore)
        val txtLevel = view.findViewById<TextView>(R.id.txtlevel)
        val txtFathername = view.findViewById<TextView>(R.id.txtfather)
        val txtMothername = view.findViewById<TextView>(R.id.txtmother)
        val txtBestfriend = view.findViewById<TextView>(R.id.txtbestfriend)
        val txtSection = view.findViewById<TextView>(R.id.txtsection)


        var userData = UserData.instance
        if (userData != null) {
            print("\nUserData: Message data is updated:  "+userData.FirstName+", "+ userData.LastName +","+ userData.Points)

            print("\nUserData-------------" + userData.toString())

            txtWecome.text = "Welcome, "+userData.FirstName +" "+userData.LastName
            txtAge.text = "${txtAge.text.toString()}: ${userData.Age.toString()}"
            txtScore.text = "${txtScore.text.toString()} :  ${userData.Points.toString()}"
            txtBestfriend.text = "${txtBestfriend.text.toString()} :  ${userData.BestFriend}"
            txtFathername.text = txtFathername.text.toString()+": "+ userData.FatherName
            txtMothername.text = txtMothername.text.toString() +": "+userData.MotherName
            txtLevel.text = txtLevel.text.toString()+": "+ userData.Level.toString()
            txtSection.text = txtSection.text.toString()+": "+ userData.Section
            print("\nUserData-------------" + txtWecome.text)



        }
        return view
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
         * @return A new instance of fragment HomeFragment.
         */
        // TODO: Rename and change types and number of parameters

    }

}// Required empty public constructor
