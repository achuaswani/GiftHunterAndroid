package gifthunter.ras.com.gifthunter.Register

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.content.Intent
import android.widget.EditText
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import gifthunter.ras.com.gifthunter.MainActivity
import gifthunter.ras.com.gifthunter.R
import gifthunter.ras.com.gifthunter.UserData.UserDataActivity
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
            if (Util.isNetworkAvailable(context)) {
                if (email.equals("") || pwd.equals("") || confirmpassword.equals("")) {
                    errortxt.setText(getString(R.string.manditory))

                } else if (!pwd.equals(confirmpassword)) {
                    errortxt.setText(getString(R.string.confirmpwdmsg))
                } else {
                    println("btnsignup else case");
                    mAuth?.createUserWithEmailAndPassword(email, pwd)
                            ?.addOnCompleteListener(this) { task ->
                                println("createUserWithEmailAndPassword");
                                if (task.isSuccessful) {
                                    // Sign in success, update UI with the signed-in user's information
                                    MainActivity.loggedUser = mAuth?.getCurrentUser()!!;
                                    updateUI(MainActivity.loggedUser.toString())
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

    private fun updateUI(userId: String) {

        val intent = Intent(this, UserDataActivity::class.java)
        intent.putExtra("UserDataActivity", userId.toString())
        startActivity(intent)
    }
}

