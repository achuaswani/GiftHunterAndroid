package gifthunter.ras.com.gifthunter.UserData

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import gifthunter.ras.com.gifthunter.Dashboard.DashboardActivity
import gifthunter.ras.com.gifthunter.MainActivity
import gifthunter.ras.com.gifthunter.R
import gifthunter.ras.com.gifthunter.Models.ProfileModel
import gifthunter.ras.com.gifthunter.Utils.Util
import kotlinx.android.synthetic.main.activity_user_data.*
import kotlinx.android.synthetic.main.activity_user_data.age

class UserDataActivity : AppCompatActivity() {
    var profileModel = ProfileModel()
    var mAuth: FirebaseAuth? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_data)
        mAuth = MainActivity.mAuth
        profileModel = MainActivity.profileData
        submit.setOnClickListener {
            profileModel = ProfileModel(
                    displayName.text.toString(),
                    "",
                    about.text.toString(),
                    age.text.toString(),
                    section.text.toString(),
                    profileModel.points.toString()
            )

            Util.updateProfile(profileModel) {success ->
                if(success) {
                    val intent = Intent(this, DashboardActivity::class.java)
                    startActivity(intent)
                } else {
                    Toast.makeText(this, getString(R.string.something_wrong), Toast.LENGTH_LONG).show()
                }
            }

        }

    }

    public override fun onStart() {
        super.onStart()

    }

    fun setUpView() {
        displayName.setText(profileModel.displayName)
        about.setText(profileModel.about)
        age.setText(profileModel.age)
        section.setText(profileModel.grade)

    }
}
