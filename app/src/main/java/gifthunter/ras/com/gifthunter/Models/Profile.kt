package gifthunter.ras.com.gifthunter.Models

data class Profile (
    var userName: String = "",
    var userId: String = "",
    var role: Role = Role.user,
    var userDisplayPicture: String? = "",
    var quizPIN: ArrayList<String> = arrayListOf()
)

enum  class Role {
    admin,
    user
}
