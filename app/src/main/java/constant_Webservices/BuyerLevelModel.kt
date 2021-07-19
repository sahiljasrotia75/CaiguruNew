package constant_Webservices

import android.os.Parcel
import android.os.Parcelable

class BuyerLevelModel() :Parcelable {

    var levelType: String = ""
    var levelImage: Int = 0
    var levelname:String=""

    constructor(parcel: Parcel) : this() {
        levelType = parcel.readString().toString()
        levelImage = parcel.readInt()
        levelname = parcel.readString().toString()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(levelType)
        parcel.writeInt(levelImage)
        parcel.writeString(levelname)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<BuyerLevelModel> {
        override fun createFromParcel(parcel: Parcel): BuyerLevelModel {
            return BuyerLevelModel(parcel)
        }

        override fun newArray(size: Int): Array<BuyerLevelModel?> {
            return arrayOfNulls(size)
        }
    }

}
