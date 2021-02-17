package gifthunter.ras.com.gifthunter.Register

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.content.Intent
import android.widget.EditText
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import gifthunter.ras.com.gifthunter.Dashboard.DashboardActivity
import gifthunter.ras.com.gifthunter.MainActivity
import gifthunter.ras.com.gifthunter.R
import gifthunter.ras.com.gifthunter.Utils.ProfileDataService
import gifthunter.ras.com.gifthunter.Utils.Session
import gifthunter.ras.com.gifthunter.Utils.Util

class RegisterActivity : AppCompatActivity() {
    var mAuth: FirebaseAuth? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        mAuth = MainActivity.mAuth
        var newemailtxt = findViewById<EditText>(R.id.newemail)

        var newpwdtxt = findViewById<EditText>(R.id.newpwd)

        var confirmpwdtxt = findViewById<EditText>(R.id.confirmpwd)

        val btnSignup = findViewById<Button>(R.id.signup)
        val context = this

        btnSignup.setOnClickListener {
            var email = newemailtxt.text.toString()
            var pwd = newpwdtxt.text.toString()
            var confirmpassword = confirmpwdtxt.text.toString()
            var errortxt = findViewById<TextView>(R.id.errorlbl)
            errortxt.setText("");
            if (Session.isNetworkAvailable(context)) {
                if (email.equals("") || pwd.equals("") || confirmpassword.equals("")) {
                    errortxt.setText(getString(R.string.manditory))

                } else if (!pwd.equals(confirmpassword)) {
                    errortxt.setText(getString(R.string.confirmpwdmsg))
                } else {
                    mAuth?.createUserWithEmailAndPassword(email, pwd)
                            ?.addOnCompleteListener(this) { task ->
                                if (task.isSuccessful) {
                                    MainActivity.loggedUser = mAuth?.getCurrentUser()!!;
                                    // Sign in success, update UI with the signed-in user's information
                                    val uid = Session.loggedUser.uid
                                    if (uid != null) {
                                        ProfileDataService.listen(uid)
                                        if (ProfileDataService.isProfileLoaded) {
                                            routeToDashboard()
                                        }
                                    }
                                } else {
                                    // If sign in fails, display a message to the user.
                                    errortxt.setText(getString(R.string.techincalerror))

                                }
                            }
                }
            } else {
                errortxt.setText(getString(R.string.netwrkerror))
            }
        }
    }

    private fun routeToDashboard() {
        val intentToOpenDashboard = Intent(this, DashboardActivity::class.java)
        startActivity(intentToOpenDashboard)
    }
}

