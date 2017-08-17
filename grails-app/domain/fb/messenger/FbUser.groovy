package fb.messenger

class FbUser {
    Long fbUserId
    String firstName
    String lastName
    String profilePicUrl
    String gender
    String locale
    Integer timezone

    String getFbUserIdStr() {
        return fbUserId.toString()
    }


    static constraints = {
    }
}
