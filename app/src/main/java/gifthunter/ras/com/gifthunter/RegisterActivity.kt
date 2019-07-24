package gifthunter.ras.com.gifthunter

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.AuthResult
import com.google.android.gms.tasks.OnCompleteListener
import android.content.Intent
import android.widget.EditText
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import android.net.NetworkInfo
import android.content.Context
import android.net.ConnectivityManager
import com.google.android.gms.tasks.Task


class RegisterActivity : AppCompatActivity() {
    var mAuth: FirebaseAuth? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        mAuth = FirebaseAuth.getInstance();
        var newemailtxt = findViewById<EditText>(R.id.newemail)

        var newpwdtxt = findViewById<EditText>(R.id.newpwd)

        var confirmpwdtxt = findViewById<EditText>(R.id.confirmpwd)

        val btnSignup = findViewById<Button>(R.id.signup)

        btnSignup.setOnClickListener {
            var email = newemailtxt.text.toString()
            var pwd = newpwdtxt.text.toString()
            var confirmpassword = confirmpwdtxt.text.toString()
            var errortxt = findViewById<TextView>(R.id.errorlbl)
            errortxt.setText("");
            if (isNetworkAvailable()) {
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
                                    val user = mAuth!!.getCurrentUser();
                                    println("createUserWithEmailAndPassword--- $user")
                                    updateUI(user.toString())
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
    private fun isNetworkAvailable(): Boolean {
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE)
        return if (connectivityManager is ConnectivityManager) {
            val networkInfo: NetworkInfo? = connectivityManager.activeNetworkInfo
            networkInfo?.isConnected ?: false
        } else false
    }
    }


