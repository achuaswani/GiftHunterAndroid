package gifthunter.ras.com.gifthunter.Dashboard
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import gifthunter.ras.com.gifthunter.Home.HomeFragment
import gifthunter.ras.com.gifthunter.Quiz.QuizFragment
import gifthunter.ras.com.gifthunter.Settings.SettingsFragment

/**
 * Created by U26448 on 10/5/18.
 */
class DashboardAdapter (fm: FragmentManager) : FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT){
    override fun getItem(position: Int): Fragment {
        println("getItem-- $position")
        var fragment: Fragment = SettingsFragment()
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