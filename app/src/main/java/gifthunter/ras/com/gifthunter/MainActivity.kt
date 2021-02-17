package gifthunter.ras.com.gifthunter

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.os.Handler
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import gifthunter.ras.com.gifthunter.Dashboard.DashboardActivity
import gifthunter.ras.com.gifthunter.Login.LoginActivity
import gifthunter.ras.com.gifthunter.Utils.Util

class MainActivity : AppCompatActivity() {

    companion object {
        lateinit var mAuth: FirebaseAuth
        lateinit var database: FirebaseDatabase
        lateinit var databaseRootRef: DatabaseReference
        lateinit var loggedUser: FirebaseUser
        lateinit var mStorageRef: StorageReference
    }

    var updateHandler: Handler? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Firebase init
        mAuth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        databaseRootRef = database.reference
        mStorageRef = FirebaseStorage.getInstance().reference

        updateHandler = Handler()
    }

    override fun onStart() {
        super.onStart()
        val currentUser = mAuth.currentUser
        var runnable: Runnable? = null

        if (currentUser != null) {
            runnable = Runnable {
                val intent = Intent(this, DashboardActivity::class.java)
                startActivity(intent)
                updateHandler!!.removeCallbacks(runnable!!)
            }
            loggedUser = currentUser

        } else {
            runnable = Runnable {
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                updateHandler!!.removeCallbacks(runnable!!)
            }
        }
        updateHandler!!.postDelayed(runnable, 3000)
    }
}

