package gifthunter.ras.com.gifthunter.Utils

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import gifthunter.ras.com.gifthunter.MainActivity
import gifthunter.ras.com.gifthunter.Models.Profile
import gifthunter.ras.com.gifthunter.Models.Question

object ProfileDataService {
    var profile: Profile? = null
    var isProfileLoaded = false
    var databaseUsersReference = MainActivity.database.reference.child(AppConstants.NODE_PROFILE)
    fun listen(uid: String) {
        retrieveData(uid) { profileData ->
            if (profileData != null) else {
                profile = profileData
            }
        }
    }
    fun retrieveData(uid: String, handler: (Profile?) -> Unit) {
        databaseUsersReference.child(uid).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                for (data in p0.children) {
                        isProfileLoaded = true
                        val profileValue = data.getValue(Profile::class.java)!!
                        handler(profileValue)
                    }
                }
            override fun onCancelled(p0: DatabaseError) { handler(null)}
        })
    }

   fun clearData() {
        profile = null
        isProfileLoaded = false
    }

    // MARK: - Upload image to backend
   /* fun updateDisplayPicture(filePath: URL) {
        guard let userId = profile?.userId else {
            return
        }
        let dpStorageReference = profileStorageReference.child(userId).child("profile.jpg")
        dpStorageReference.putFile(from: filePath, metadata: nil) { metadata, error in
            guard metadata  != nil else {
                return
            }
            dpStorageReference.downloadURL { (url, error) in
                guard let downloadURL = url else {
                    return
                }
                self.profile?.userDisplayPicture = downloadURL.absoluteString
                guard let profile = self.profile else {
                    return
                }
                self.updateProfile(userValue: profile) { error in
                        if error != nil {
                            print(error.debugDescription)
                        }
                }
            }
        }
    }

    fun updateProfile(userValue: Profile, handler: @escaping (Error?) -> Void) {
        let databbase = databaseUsersReference.child(userValue.userId)
        do {
            let data = try FirebaseEncoder().encode(userValue)
                databbase.setValue(data) { error, _ in
                    self.profile = userValue
                    self.isProfileLoaded = true
                    handler(error)
                }
            } catch {
                handler(error)
                debugPrint("Error")
            }
        }

    fun updateToUserNamesList() {
        guard let profile = profile else {
            return
        }
        userNamesReference.child(profile.userName).setValue(profile.userId)
    }*/
}