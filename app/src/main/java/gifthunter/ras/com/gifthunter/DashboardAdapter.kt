package gifthunter.ras.com.gifthunter
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
/**
 * Created by U26448 on 10/5/18.
 */
class DashboardAdapter (fm: FragmentManager) : FragmentPagerAdapter(fm){
    override fun getItem(position: Int): Fragment? {
        println("getItem-- $position")
        var fragment: Fragment? = null
        if (position == 0) {
            fragment = HomeFragment()
        } else if (position == 1) {
            fragment = QuizFragment()
        } else if (position == 2) {
            fragment = SettingsFragment()
        }
        return fragment
    }


    override fun getCount(): Int {
        return 3
    }

    override fun getPageTitle(position: Int): CharSequence? {
        var title: String? = null
        if (position == 0) {
            title = "Home"
        } else if (position == 1) {
            title = "Quiz"
        } else if (position == 2) {
            title = "Settings"
        }
        return title
    }

}