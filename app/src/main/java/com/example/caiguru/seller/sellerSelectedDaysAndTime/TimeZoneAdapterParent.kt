package com.example.caiguru.seller.sellerSelectedDaysAndTime

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.caiguru.R
import constant_Webservices.Constant
import kotlinx.android.synthetic.main.seller_time_zone_adapter.view.*
import kotlinx.android.synthetic.main.timezone_alert_custom_dialog.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class TimeZoneAdapterParent(
    var context: Activity
) :
    RecyclerView.Adapter<TimeZoneAdapterParent.Viewholder>(),
    TimeZoneAdapterChild.TimeSelectListener {
    private lateinit var dialogReplicateTimeAdapter: DialogReplicateTimeAdapter
    private lateinit var dialog: Dialog
    private var mData = ArrayList<DaysParentModel>()
    lateinit var recyclerAdapter: TimeZoneAdapterChild
    var timeSlotTo = 0
  //  var click = context as parentDataInterface
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Viewholder {
        val view =
            LayoutInflater.from(context).inflate(R.layout.seller_time_zone_adapter, parent, false)
        return Viewholder(view)

    }

    override fun getItemCount(): Int {
        return mData.size
    }

    override fun onBindViewHolder(holder: Viewholder, position: Int) {
        val data = mData[position]
        holder.itemView.txtweekszone.text = Constant.getDayString(context, data.weeks)

        if (data.isExpanded) {
            holder.itemView.relativechild.visibility = View.VISIBLE
            holder.itemView.updown.setImageResource(R.drawable.ic_up_arrow)

        } else {
            holder.itemView.relativechild.visibility = View.GONE
            holder.itemView.updown.setImageResource(R.drawable.test)
        }

        //......child adapter..........//
        recyclerAdapter = TimeZoneAdapterChild(context, this, position)
        val manager = LinearLayoutManager(context)
        holder.recyclerChild.layoutManager = manager//set layout in recycler
        holder.recyclerChild.adapter = recyclerAdapter
        recyclerAdapter.updateData(data)


        //set the click on the layout
        holder.itemView.card3.setOnClickListener {
            mData[position].isExpanded = !data.isExpanded
            notifyDataSetChanged()
        }
    }

    fun update(it: ArrayList<DaysParentModel>) {
        mData = it
        notifyDataSetChanged()

    }

    class Viewholder(view: View) : RecyclerView.ViewHolder(view) {
        var recyclerChild: RecyclerView = itemView.findViewById(R.id.recyclerChild)
    }

    // click of ok
    override fun onTimeSelect(
        childIndex: Int,
        parentIndex: Int,
        time: String,
        isFrom: Boolean
    ) {
        //click.getData(childIndex, parentIndex)
        if (isFrom) {
            if (childIndex == 0) {
                mData[parentIndex].slotList[childIndex].from = time//slot 1 from
               mData[parentIndex].slotList[0].to = context.getString(R.string.To)
//                mData[parentIndex].slotList[1].from = context.getString(R.string.From)
//                mData[parentIndex].slotList[1].to = context.getString(R.string.To)
                notifyDataSetChanged()
                return
            } else {

                mData[parentIndex].slotList[childIndex].from = time//slot 2 from
                mData[parentIndex].slotList[1].to = context.getString(R.string.To)
                notifyDataSetChanged()
                return
            }
        } else {
            if (childIndex == 0) {
                mData[parentIndex].slotList[childIndex].to = time // slot 1 to
//                mData[parentIndex].slotList[1].from = context.getString(R.string.From)
//                mData[parentIndex].slotList[1].to = context.getString(R.string.To)
                notifyDataSetChanged()
                return
            }

            if (childIndex == 1) {
                mData[parentIndex].slotList[childIndex].to = time  //slot 2 to
                notifyDataSetChanged()
                return
            }
        }
        notifyDataSetChanged()
    }

    //set the click of time intervals
    override fun replicatesTimeZone(
        slot: SlotModel,
        position: Int,
        parentPosition: Int
    ) {
        if (position == 0) {
//0 for the 1st slot time set
            fillSameTimeForFirstSlotDiloag(slot, position, parentPosition, mData)
        } else {
            //1 for 2nd slot time set
            fillSameTimeForSecondSlotDiloag(slot, position, parentPosition, mData)
        }
    }

    //************************1st slot time fill up
    private fun fillSameTimeForFirstSlotDiloag(
        slot: SlotModel,
        ChildPosition: Int,
        parentPosition: Int,
        mData: ArrayList<DaysParentModel>
    ) {
        dialog = Dialog(context)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setContentView(R.layout.timezone_alert_custom_dialog)
        dialog.show()

        val layoutManager = GridLayoutManager(context, 2)
        layoutManager.orientation = RecyclerView.VERTICAL

        dialogReplicateTimeAdapter =
            DialogReplicateTimeAdapter(
                context, allDataOfDaysDialog(
                    GetPreselectedDays(mData, slot, ChildPosition), parentPosition
                )
            )
        dialog.DaysRecyclerDialog.setLayoutManager(layoutManager)
        dialog.DaysRecyclerDialog.adapter = dialogReplicateTimeAdapter

        dialog.okButton.setOnClickListener {
            val formatter =SimpleDateFormat("hh:mm a", Locale.US)
            val converter = SimpleDateFormat("hh:mm a", Locale.US)
            var itemSelected = false
            for (item in dialogReplicateTimeAdapter.getAllData()) {
                if (item.isSelected == true) {
                    itemSelected = true
                    break
                }
            }
            var shoPopUpORNot=""
            //change code by adding some conditions
            if (itemSelected) {
                for (item in dialogReplicateTimeAdapter.getAllData()) {
                    for (items in this.mData) {
                        if (item.isSelected && item.daysPosition == items.position && items.slotList[1].from.contains(
                                context.getString(R.string.From)
                            ) && items.slotList[1].to.contains(context.getString(R.string.To))
                        ) {

                            items.slotList[ChildPosition].from = slot.from
                            items.slotList[ChildPosition].to = slot.to
                        }
                        else if (item.isSelected && item.daysPosition == items.position && items.slotList[1].from.contains(
                                ":"
                            ) && items.slotList[1].to.contains(":")) {
                            val sourceCurrentTime = formatter.parse(items.slotList[1].from)//my current time
                            val sourceto = converter.parse( slot.to)

                            if (sourceCurrentTime.before(sourceto) || sourceCurrentTime.equals(sourceto)){
//                                items.slotList[ChildPosition].from = slot.from
//                                items.slotList[ChildPosition].to = slot.to

                                items.slotList[ChildPosition].from = context.getString(R.string.From)
                                items.slotList[ChildPosition].to = context.getString(R.string.To)

                               if (shoPopUpORNot.isEmpty()){
                                   shoPopUpORNot=items.weeks
                               }else{
                                   shoPopUpORNot="$shoPopUpORNot, ${items.weeks}"
                               }
                            }else{
                                items.slotList[ChildPosition].from = slot.from
                                items.slotList[ChildPosition].to = slot.to
                            }
                        }
                    }
                }
                if (shoPopUpORNot.isNotEmpty()){
                   openNOtPrefillingDays(shoPopUpORNot)
                }
                dialog.dismiss()
                notifyDataSetChanged()
            }

//            if (itemSelected) {
//                for (item in dialogReplicateTimeAdapter.getAllData()) {
//                    for (items in this.mData) {
//                        if (item.isSelected && item.daysPosition == items.position) {
//                            items.slotList[ChildPosition].from = slot.from
//                            items.slotList[ChildPosition].to = slot.to
//                        }
//                    }
//                }
//                dialog.dismiss()
//                notifyDataSetChanged()
//            }
            else {
                Toast.makeText(
                    context,
                    context.getString(R.string.Please_select_at_least_one),
                    Toast.LENGTH_SHORT
                )
                    .show()
                return@setOnClickListener
            }

            notifyDataSetChanged()


        }

    }



    //error dialog case of failure
    private fun openNOtPrefillingDays(shoPopUpORNot: String) {
        val mDialog = AlertDialog.Builder(context)
        mDialog.setTitle(context.getString(R.string.alert))
        mDialog.setMessage(context.getString(R.string.Time_cannot_be_replicated)+" "+Constant.getDayString(context,shoPopUpORNot))
        mDialog.setPositiveButton(
            context.getString(R.string.ok)
        ) { dialog, which ->
            dialog.cancel()
        }
        mDialog.show()
    }

    //*****************2nd dialog**************//
    private fun fillSameTimeForSecondSlotDiloag(
        slot: SlotModel,
        ChildPosition: Int,
        parentPosition: Int,
        mData: ArrayList<DaysParentModel>
    ) {
        dialog = Dialog(context)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setContentView(R.layout.timezone_alert_custom_dialog)
        dialog.show()

        val layoutManager = GridLayoutManager(context, 2)
        layoutManager.orientation = RecyclerView.VERTICAL

        dialogReplicateTimeAdapter =
            DialogReplicateTimeAdapter(
                context, allDataOfDaysDialog(
                    GetPreselectedDays(mData, slot, ChildPosition), parentPosition
                )
            )
        dialog.DaysRecyclerDialog.setLayoutManager(layoutManager)
        dialog.DaysRecyclerDialog.adapter = dialogReplicateTimeAdapter

        dialog.okButton.setOnClickListener {
            val formatter = SimpleDateFormat("hh:mm a", Locale.US)
            val converter = SimpleDateFormat("hh:mm a", Locale.US)
            var itemSelected = false
            for (item in dialogReplicateTimeAdapter.getAllData()) {
                if (item.isSelected == true) {
                    itemSelected = true
                    break
                }
            }
            var shoPopUpORNot=""
            //change code by adding some conditions
            if (itemSelected) {
                for (item in dialogReplicateTimeAdapter.getAllData()) {
                    for (items in this.mData) {
                        if (item.isSelected && item.daysPosition == items.position && items.slotList[0].from.contains(
                                context.getString(R.string.From)
                            ) && items.slotList[0].to.contains(context.getString(R.string.To))
                        ) {

                            items.slotList[ChildPosition].from = slot.from
                            items.slotList[ChildPosition].to = slot.to
                        }
                        else if (item.isSelected && item.daysPosition == items.position && items.slotList[0].from.contains(
                                ":"
                            ) && items.slotList[0].to.contains(":")) {
                            val sourceto = converter.parse( items.slotList[0].to)//my current time
                            val sourceCurrentTime = formatter.parse(slot.from)

                            if (sourceCurrentTime.before(sourceto) || sourceCurrentTime.equals(sourceto)){
//                                items.slotList[0].from = slot.from
//                                items.slotList[0].to = slot.to

                                items.slotList[ChildPosition].from = context.getString(R.string.From)
                                items.slotList[ChildPosition].to = context.getString(R.string.To)

                                if (shoPopUpORNot.isEmpty()){
                                    shoPopUpORNot=items.weeks
                                }else{
                                    shoPopUpORNot="$shoPopUpORNot, ${items.weeks}"
                                }

                            }else{
                                items.slotList[ChildPosition].from = slot.from
                                items.slotList[ChildPosition].to = slot.to
                            }


                        }
//                        else {
//                            Toast.makeText(
//                                context,
//                                context.getString(R.string.Please_select_at_least_one),
//                                Toast.LENGTH_SHORT
//                            )
//                                .show()
//                        }
                    }
                }
                if (shoPopUpORNot.isNotEmpty()){
                    openNOtPrefillingDays(shoPopUpORNot)
                }
                dialog.dismiss()
                notifyDataSetChanged()
            }   else {
                Toast.makeText(
                    context,
                    context.getString(R.string.Please_select_at_least_one),
                    Toast.LENGTH_SHORT
                )
                    .show()
                return@setOnClickListener
            }

//            if (itemSelected) {
//                for (item in dialogReplicateTimeAdapter.getAllData()) {
//                    for (items in this.mData) {
//                        if (item.isSelected && item.daysPosition == items.position) {
//                            items.slotList[ChildPosition].from = slot.from
//                            items.slotList[ChildPosition].to = slot.to
//                        }
//                    }
//                }
//                dialog.dismiss()
//                notifyDataSetChanged()
//            }
//            else {
//                Toast.makeText(
//                    context,
//                    context.getString(R.string.Please_select_at_least_one),
//                    Toast.LENGTH_SHORT
//                )
//                    .show()
//                return@setOnClickListener
//            }

            notifyDataSetChanged()


        }

    }

    //**************************second slot time fill up

    //*****************preselected logic of slots
    private fun GetPreselectedDays(
        mData: ArrayList<DaysParentModel>,
        slot: SlotModel,
        childPosition: Int
    ): ArrayList<DialogDaysModel> {
        val localArray = ArrayList<DialogDaysModel>()
        for (item in mData) {
            val model = DialogDaysModel()
            if (item.slotList[childPosition].from == slot.from && item.slotList[childPosition].to == slot.to) {
                model.isSelected = true
                model.dayName = item.weeks
                model.daysPosition = item.position
                localArray.add(model)
            } else {
                model.isSelected = false
                model.dayName = item.weeks
                model.daysPosition = item.position
                localArray.add(model)
            }
        }
        return localArray
    }

    //filter the days that we dont show in the selected
    //step 2 set all the data in the array
    private fun allDataOfDaysDialog(
        getPreselectedDays: ArrayList<DialogDaysModel>,
        position: Int
    ): ArrayList<DialogDaysModel> {
        val array = ArrayList<DialogDaysModel>()
        for (item in getPreselectedDays) {
            val model = DialogDaysModel()
            if (position != item.daysPosition.toInt()) {
                model.isSelected = item.isSelected
                model.daysPosition = item.daysPosition
                model.dayName = item.dayName
                array.add(model)
            }
        }
        return array
    }

    fun getUpdatedList(): ArrayList<DaysParentModel> {
        return mData
    }

    fun clearTimeSlot() {

        timeSlotTo = 0
        notifyDataSetChanged()
    }


//    interface parentDataInterface {
//        fun getData(childIndex: Int, parentIndex: Int)
//    }

}



