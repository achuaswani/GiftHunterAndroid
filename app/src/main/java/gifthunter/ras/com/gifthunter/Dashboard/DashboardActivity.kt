package gifthunter.ras.com.gifthunter.Dashboard

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import gifthunter.ras.com.gifthunter.*
import gifthunter.ras.com.gifthunter.Quiz.QuizFragment
import gifthunter.ras.com.gifthunter.Settings.SettingsFragment
import kotlinx.android.synthetic.main.activity_dashboard.*

class DashboardActivity : QuizFragment.OnFragmentInteractionListener, SettingsFragment.OnFragmentInteractionListener, AppCompatActivity()  {
    internal lateinit var viewpageradapter: DashboardAdapter //Declare PagerAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)
        viewpageradapter= DashboardAdapter(supportFragmentManager)


        this.viewPager.setAdapter(viewpageradapter)  //Binding PagerAdapter with ViewPager
        this.tab_layout.setupWithViewPager(this.viewPager)
    }
    override fun onStart()
    {
        super.onStart();
        println("-onStart")

    }
    override fun onFragmentInteraction(uri: Uri) {
        // save some data from the fragment...
        // other business logic...
    }

}
