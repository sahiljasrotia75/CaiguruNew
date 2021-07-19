package com.example.caiguru.seller.sellerSelectedDaysAndTime

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData

class TimeZoneViewModel(application: Application) : AndroidViewModel(application) {


    var mrepo = TimeZoneRepository(application)

    fun sendDayWeek(): MutableLiveData<ArrayList<DaysParentModel>> {
        return mrepo.sendDayWeek()


    }


}
