package gifthunter.ras.com.gifthunter

import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkInfo
import com.google.firebase.auth.FirebaseAuth
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.DatabaseError



class MainActivity : AppCompatActivity() {
    var mAuth: FirebaseAuth? = null
    val database = FirebaseDatabase.getInstance()
    val userdata = database.getReference("UserData")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mAuth = FirebaseAuth.getInstance()
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
        // Check if user is signed in (non-null) and update UI accordingly.
        getUserData();

    }
    private fun getUserData(){
        val currentUser = mAuth?.getCurrentUser()
        println("currentUser= $currentUser")
        if(currentUser!=null) {
            val db = userdata.child(currentUser.uid)
            // User data change listener
            db.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    // This method is called once with the initial value and again
                    // whenever data at this location is updated.
                    if (dataSnapshot.exists()) {
                        println("onDataChange--exists user")
                        //var dataObject = UserData.instance
                        UserData.instance = dataSnapshot.getValue(UserData::class.java)!!
                        print("data------"+UserData.instance.FirstName)
                        updateUI()
                    }else{
                        println("onDataChange--no user")
                        updateUserData(currentUser.toString())
                    }

                }

                override fun onCancelled(error: DatabaseError) {
                    // Failed to read value
                    println("onCancelled")
                }
            })

        }
    }
    private fun updateUserData(userId: String) {

        val intent = Intent(this, UserDataActivity::class.java)
        intent.putExtra("UserDataActivity", userId.toString())
        startActivity(intent)
    }
    private fun updateUI() {

        val intnt = Intent(this, DashboardActivity::class.java)
        startActivity(intnt)
    }
    private fun navigateToSignup() {
        val intent = Intent(this, RegisterActivity::class.java)
        startActivity(intent)
    }
    private fun navigateToDashboard() {
        val errortxt = findViewById<TextView>(R.id.loginerror)
        println("navigateToDashboard--")
        if (isNetworkAvailable()) {
            val emailVal = findViewById<EditText>(R.id.emailid)
            val pwdVal = findViewById<EditText>(R.id.pwdtxt)
            if (emailVal.getText().toString().equals("") || pwdVal.getText().toString().equals("")) {
                errortxt.setText(getString(R.string.manditory));
            } else {
                println("navigateToDashboard-- signin")

                mAuth?.signInWithEmailAndPassword(emailVal.text.toString(), pwdVal.text.toString())
                        ?.addOnCompleteListener(this) { task ->
                            println("navigateToDashboard--task.isSuccessful: $task.isSuccessful");
                            if (task.isSuccessful) {
                                println("navigateToDashboard-- ask.isSuccessful")
                                getUserData();

                            } else {
                                println("navigateToDashboard-- error: $task.exception")
                                errortxt.setText(getString(R.string.techincalerror));
                            }

                            // ...
                        }
            }
        }else{
            println("navigateToDashboard-- ask.no network")
            errortxt.setText(getString(R.string.netwrkerror));
        }
    }
    private fun isNetworkAvailable(): Boolean {
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE)
        return if (connectivityManager is ConnectivityManager) {
            val networkInfo: NetworkInfo? = connectivityManager.activeNetworkInfo
            networkInfo?.isConnected ?: false
        } else false
    }
}
