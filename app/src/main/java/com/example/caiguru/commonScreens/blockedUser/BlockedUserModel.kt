package com.example.caiguru.commonScreens.blockedUser

import android.os.Parcel
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import kotlinx.android.parcel.RawValue

@Parcelize
class BlockedUserModel() : Parcelable {
    var block_user_id = 0
    var created_at = ""
    var type = 0
    var buyer_name = ""
    var buyer_image = ""
    var buyer_level = ""
    var reputation = ""


}