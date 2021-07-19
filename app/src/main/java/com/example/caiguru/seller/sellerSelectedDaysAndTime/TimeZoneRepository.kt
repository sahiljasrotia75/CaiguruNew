package com.example.caiguru.seller.sellerSelectedDaysAndTime

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.example.caiguru.R

class TimeZoneRepository(var application: Application) {
    var mdata = MutableLiveData<ArrayList<DaysParentModel>>()

    init {
        mdata.value = daysData()
    }

    private fun daysData(): ArrayList<DaysParentModel> {

        val arraydata = ArrayList<DaysParentModel>()
        var model = DaysParentModel()
        var listSlot = ArrayList<SlotModel>()
        var slot = SlotModel()
        model.weeks = "Monday"
        model.position = "0"
        slot.from = application.getString(R.string.From)
        slot.to = application.getString(R.string.To)
        listSlot.add(slot)
        slot = SlotModel()
        slot.from = application.getString(R.string.From)
        slot.to = application.getString(R.string.To)
        listSlot.add(slot)
        model.slotList = listSlot
        arraydata.add(model)

        model = DaysParentModel()
        model.weeks = "Tuesday"
        model.position = "1"
        slot = SlotModel()
        listSlot = ArrayList()
        slot.from = application.getString(R.string.From)
        slot.to = application.getString(R.string.To)
        listSlot.add(slot)
        slot = SlotModel()
        slot.from = application.getString(R.string.From)
        slot.to = application.getString(R.string.To)
        listSlot.add(slot)
        model.slotList = listSlot
        arraydata.add(model)

        model = DaysParentModel()
        model.weeks ="Wednesday"
        model.position = "2"
        slot = SlotModel()
        listSlot = ArrayList()
        slot.from = application.getString(R.string.From)
        slot.to = application.getString(R.string.To)
        listSlot.add(slot)
        slot = SlotModel()
        slot.from = application.getString(R.string.From)
        slot.to = application.getString(R.string.To)
        listSlot.add(slot)
        model.slotList = listSlot
        arraydata.add(model)

        model = DaysParentModel()
        model.weeks = "Thursday"
        model.position = "3"
        slot = SlotModel()
        listSlot = ArrayList()
        slot.from = application.getString(R.string.From)
        slot.to = application.getString(R.string.To)
        listSlot.add(slot)
        slot = SlotModel()
        slot.from = application.getString(R.string.From)
        slot.to = application.getString(R.string.To)
        listSlot.add(slot)
        model.slotList = listSlot
        arraydata.add(model)

        model = DaysParentModel()
        model.weeks = "Friday"
        model.position = "4"
        slot = SlotModel()
        listSlot = ArrayList()
        slot.from = application.getString(R.string.From)
        slot.to = application.getString(R.string.To)
        listSlot.add(slot)
        slot = SlotModel()
        slot.from = application.getString(R.string.From)
        slot.to = application.getString(R.string.To)
        listSlot.add(slot)
        model.slotList = listSlot
        arraydata.add(model)

        model = DaysParentModel()
        model.weeks = "Saturday"
        model.position = "5"
        slot = SlotModel()
        listSlot = ArrayList()
        slot.from = application.getString(R.string.From)
        slot.to = application.getString(R.string.To)
        listSlot.add(slot)
        slot = SlotModel()
        slot.from = application.getString(R.string.From)
        slot.to = application.getString(R.string.To)
        listSlot.add(slot)
        model.slotList = listSlot
        arraydata.add(model)

        model = DaysParentModel()
        model.weeks = "Sunday"
        model.position = "6"
        slot = SlotModel()
        listSlot = ArrayList()
        slot.from = application.getString(R.string.From)
        slot.to = application.getString(R.string.To)
        listSlot.add(slot)
        slot = SlotModel()
        slot.from = application.getString(R.string.From)
        slot.to = application.getString(R.string.To)
        listSlot.add(slot)
        model.slotList = listSlot
        arraydata.add(model)

        return arraydata

    }

    fun sendDayWeek(): MutableLiveData<ArrayList<DaysParentModel>> {
        return mdata

    }


}
