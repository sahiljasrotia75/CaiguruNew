package com.example.caiguru.commonScreens.referralCode

import android.app.Application
import androidx.lifecycle.AndroidViewModel

class ReferralViewModel(application: Application):AndroidViewModel(application) {

    var repo :RefferalCodeRepository = RefferalCodeRepository(application)

}
