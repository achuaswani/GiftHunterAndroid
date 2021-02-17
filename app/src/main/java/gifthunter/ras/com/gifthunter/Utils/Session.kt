package gifthunter.ras.com.gifthunter.Utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import gifthunter.ras.com.gifthunter.MainActivity
import gifthunter.ras.com.gifthunter.Models.User

object Session {
    var loggedUser = User()
    var mAuth = MainActivity.mAuth

    fun userLogin(context: Context, email: String, password: String, status: (Boolean) -> Unit) {
        mAuth?.signInWithEmailAndPassword(email, password)?.addOnCompleteListener { task ->
            loggedUser = User(
                    mAuth.currentUser?.uid,
                    mAuth.currentUser?.email.toString()
            )
            status(task.isSuccessful)
        }
    }

    fun isNetworkAvailable(context: Context): Boolean {
        var result = false
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val networkCapabilities = connectivityManager.activeNetwork ?: return false
            val actNw =
                    connectivityManager.getNetworkCapabilities(networkCapabilities) ?: return false
            result = when {
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                else -> false
            }
        }
        return result
    }
}