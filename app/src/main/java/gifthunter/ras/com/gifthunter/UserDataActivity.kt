package gifthunter.ras.com.gifthunter

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import com.google.firebase.auth.FirebaseAuth
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.ValueEventListener






class UserDataActivity : AppCompatActivity() {
    var mDatabase = FirebaseDatabase.getInstance().getReference("UserData")
    var mAuth: FirebaseAuth? = null
    val currentUser = mAuth?.getCurrentUser()
    var firstName = findViewById<EditText>(R.id.firstname)
    var lastName = findViewById<EditText>(R.id.lastname)
    var age = findViewById<EditText>(R.id.txtage)
    var section = findViewById<EditText>(R.id.section)
    var fatherName = findViewById<EditText>(R.id.fathername)
    var motherName = findViewById<EditText>(R.id.mothername)
    var familyGroup = findViewById<EditText>(R.id.familygroup)
    var friendsGroup = findViewById<EditText>(R.id.friendsgroup)
    var bestFriend = findViewById<EditText>(R.id.txtbestfriend)
    var characternm = findViewById<EditText>(R.id.character)
    val btnSubmit = findViewById<Button>(R.id.submit)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_data)
        mAuth = FirebaseAuth.getInstance()


        btnSubmit.setOnClickListener {
            //userdata.child(user)
            // var userValue: HashMap<String, Any>? =
            //hashMapOf("Age": UInt(ageValue) ?? 0, "EmailId": (Auth.auth().currentUser?.email)!, "FirstName": firstName.text ?? "", "LastName": lastName.text ?? "", "FatherName": fatherName.text ?? "", "MotherName":motherName.text ?? "", "BestFriend": bestFriend.text ?? "", "Character": character.text ?? "", "Class": className.text ?? "", "FamilyGroup": familyGroup.text ?? "","FriendsGroup": friendsGroup.text ?? "", "Sad": self.userData.Sad ?? false, "Angry": self.userData.Angry ?? false, "Happy": self.userData.Happy ?? false]
            //userdata.setValue()



            println("uid------ $currentUser")
//            val userData = UserData(false, "LKG", false,
//            bestFriend.text.toString(),motherName.text.toString(),
//            lastName.text.toString(), 0, friendsGroup.text.toString(),
//            characternm.text.toString(), 0,
//            true, fatherName.text.toString(),firstName.text.toString(),
//            "email", "Test", familyGroup.text.toString() , 0,0, 1)
            val userData = UserData()
            mDatabase.child(currentUser!!.uid).setValue(userData)
                    .addOnSuccessListener(OnSuccessListener<Void> {
                        val intent = Intent(this, DashboardActivity::class.java)
                        intent.putExtra("DashboardActivity", currentUser.toString())
                        startActivity(intent)
                    })
                    .addOnFailureListener(OnFailureListener {
                        // Write failed
                        // ...
                    })


        }

    }


    public override fun onStart() {
        super.onStart()
        var currentUser = mAuth?.getCurrentUser()

        var db = mDatabase.child(currentUser!!.uid)
        db.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // whenever data at this location is updated.
                if (dataSnapshot.exists()) {
                    val children = dataSnapshot.children
                    children.forEach {
                       val profile = it.value.toString()
                        var profileKey = it.key.toString()

                        println("snap: "+it.value.toString())

                        // val answertxt = findViewById<EditText>(R.id.answer)
                       // val ansData = mDatabase.child(uid)
                        //welcometxt.text = welcometxt.text.toString() + "\n You have submitted your answer. You can change your opinion anytime."

                        if(it.value.toString()!=null) when (profileKey) {
                            "FirstName" -> firstName.setText(it.value.toString())
                            "Age" -> age.setText(it.value.toString())
                            "LastName" -> lastName.setText(it.value.toString())
                            "FatherName" -> fatherName.setText(it.value.toString())
                            "MotherName" -> motherName.setText(it.value.toString())
                            "FamilyGroup" -> familyGroup.setText(it.value.toString())
                            "FriendsGroup" -> friendsGroup.setText(it.value.toString())
                            "BestFriend" -> bestFriend.setText(it.value.toString())
                            "Character" -> characternm.setText(it.value.toString())
                        }




                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
            }
        })
    }

}
