package fb.messenger

import com.google.gson.annotations.SerializedName

class FbUserProfile {
    @SerializedName("first_name")
    String firstName

    @SerializedName("last_name")
    String lastName

    @SerializedName("profile_pic")
    String profilePicUrl

    @SerializedName("locale")
    String locale

    @SerializedName("timezone")
    Integer timezone

    @SerializedName("gender")
    String gender
}
