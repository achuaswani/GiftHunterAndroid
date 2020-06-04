package gifthunter.ras.com.gifthunter.UserData

/**
 * Created by U26448 on 9/18/18.
 */

class UserData {
    companion object {
        var instance = UserData()
    }
    var key : String? = ""
    var EmailId : String? = ""
    var Angry : Boolean? = false
    var Sad : Boolean? = false
    var Happy : Boolean? = false
    var BestFriend : String? = ""
    var Challenger : String? = ""
    var Character : String? = ""
    var FamilyGroup : String? = ""
    var FirstName : String? = ""
    var LastName : String? = ""
    var Age : Int? = 0
    var MotherName : String? = ""
    var FatherName : String? = ""
    var Section : String? = ""
    var Points : Int? = 0
    var FriendsGroup : String? = ""
    var Level : Int = 1
    var Minutes: Int = 0
    var QuestionNumber: Int = 0
    //var ref: DatabaseReference
    constructor(){

    }
    constructor(age: Int, firstname: String, lastname: String, challenger: String,
                section: String, bestfriend: String, email: String, character: String,
                friendsgroup: String, fathername: String, mothername: String, familygroup: String, angry: Boolean,
                sad: Boolean, happy: Boolean, level: Int, points: Int, minutes: Int){
        this.Age = age
        this.FirstName = firstname
        this.LastName = lastname
        this.Challenger = challenger
        this.Section = section
        this.BestFriend = bestfriend
        this.EmailId =  email
        this.Character = character
        this.FriendsGroup = friendsgroup
        this.FatherName = fathername
        this.MotherName = mothername
        this.FamilyGroup = familygroup
        this.Angry = angry
        this.Sad = sad
        this.Happy = happy
        this.Level = level
        this.Points = points
        this.Minutes = minutes
    }




}