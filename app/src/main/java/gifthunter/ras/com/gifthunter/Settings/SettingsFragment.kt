package gifthunter.ras.com.gifthunter.Settings

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import gifthunter.ras.com.gifthunter.MainActivity
import gifthunter.ras.com.gifthunter.R

class SettingsFragment : Fragment() {
    val settingsArr = arrayOf("Edit Profile", "Privacy", "About Us","Purchase Points", "Help","Signout")
    private var mListener: OnFragmentInteractionListener? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view: View = inflater.inflate(R.layout.fragment_settings, container, false)
        print("\nonCreateView-settings")
        val listView:ListView = view.findViewById(R.id.listView)
        val arrayAdapter: ArrayAdapter<String> = ArrayAdapter(context!!, android.R.layout.simple_spinner_item, settingsArr)
        listView.adapter = arrayAdapter
        listView.setOnItemClickListener { parent, view, position, id ->
            print(settingsArr.count())
            if (position == settingsArr.count() - 1) {
                signOut()
            }
        }
        return view
    }

    // TODO: Rename method, update argument and hook method into UI event
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


    private fun signOut(){

        MainActivity.mAuth!!.signOut()
        getActivity()!!.finish()
    }

}