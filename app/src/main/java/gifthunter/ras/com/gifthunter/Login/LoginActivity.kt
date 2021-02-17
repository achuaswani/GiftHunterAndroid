package gifthunter.ras.com.gifthunter.Login

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import com.google.firebase.auth.FirebaseAuth
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import gifthunter.ras.com.gifthunter.Dashboard.DashboardActivity
import gifthunter.ras.com.gifthunter.MainActivity
import gifthunter.ras.com.gifthunter.R
import gifthunter.ras.com.gifthunter.Register.RegisterActivity
import gifthunter.ras.com.gifthunter.Utils.ProfileDataService
import gifthunter.ras.com.gifthunter.Utils.Session
import gifthunter.ras.com.gifthunter.Utils.Util


class LoginActivity : AppCompatActivity() {
    var mAuth: FirebaseAuth? = null
    val context = this
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        mAuth = MainActivity.mAuth
        val btnRegister = findViewById<Button>(R.id.register)
        btnRegister.setOnClickListener{
            navigateToSignup()
        }
        val btnLogin = findViewById<Button>(R.id.login)
        btnLogin.setOnClickListener{
            navigateToDashboard();

        }
    }
    public override fun onStart() {
        super.onStart()
    }

    private fun routeToDashboard() {
        val intentToOpenDashboard = Intent(this, DashboardActivity::class.java)
        startActivity(intentToOpenDashboard)
    }
    private fun navigateToSignup() {
        val intent = Intent(this, RegisterActivity::class.java)
        startActivity(intent)
    }
    private fun navigateToDashboard() {
        val errortxt = findViewById<TextView>(R.id.loginerror)
        if (Session.isNetworkAvailable(context)) {
            val emailVal = findViewById<EditText>(R.id.emailid)
            val pwdVal = findViewById<EditText>(R.id.pwdtxt)
            if (emailVal.getText().toString().equals("") || pwdVal.getText().toString().equals("")) {
                errortxt.setText(getString(R.string.manditory));
            } else {
                Session.userLogin(this, emailVal.text.toString(), pwdVal.text.toString()) { success ->
                            if (success) {
                                val uid = Session.loggedUser.uid
                                if (uid != null) {
                                    ProfileDataService.listen(uid)
                                    if (ProfileDataService.isProfileLoaded) {
                                        routeToDashboard()
                                    }
                                }
                            } else {
                                errortxt.setText(getString(R.string.techincalerror));
                            }
                        }
            }
        }else{
            errortxt.setText(getString(R.string.netwrkerror));
        }
    }

}
