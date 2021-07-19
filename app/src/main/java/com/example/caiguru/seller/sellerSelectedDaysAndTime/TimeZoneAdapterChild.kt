package com.example.caiguru.seller.sellerSelectedDaysAndTime

import android.annotation.SuppressLint
import android.app.TimePickerDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.caiguru.R
import constant_Webservices.Constant
import kotlinx.android.synthetic.main.seller_time_zone_expandable_child_layout.view.*
import java.text.SimpleDateFormat
import java.util.*


class TimeZoneAdapterChild(
    var context: Context,
    timeListener: TimeSelectListener,
    var parentPosition: Int
) : RecyclerView.Adapter<TimeZoneAdapterChild.Viewholder>() {
    private var GlobalTime: Date? = null
    private var GlobalTimeFrom2: Date? = null
    var childPosition = -1
    var listener: TimeSelectListener = timeListener
    var arrayslot1 = ArrayList<SlotModel>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Viewholder {
        val view = LayoutInflater.from(context)
            .inflate(R.layout.seller_time_zone_expandable_child_layout, parent, false)
        return Viewholder(view)
    }

    override fun getItemCount(): Int {
        return arrayslot1.size

    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: Viewholder, position: Int) {
        childPosition = position

        val slot = arrayslot1[position]
        if (slot.from.contains(":")) {
            holder.itemView.TxtFrom1.text = Constant.ConvertAmPmFormat(context, slot.from)
        } else {
            holder.itemView.TxtFrom1.text = slot.from
        }
        //to
        if (slot.to.contains(":")) {
            holder.itemView.textTo1.text = Constant.ConvertAmPmFormat(context, slot.to)
        } else {
            holder.itemView.textTo1.text = slot.to
        }

        holder.itemView.texttimeInterval1.text =
            "${context.getString(R.string.Timeinterval)} ${position + 1}"

        //clicks of from
        holder.itemView.TxtFrom1.setOnClickListener {
//            if (position == 1 && (arrayslot1[0].from.contains(context.getString(R.string.From)) || arrayslot1[0].to.contains(
//                    context.getString(R.string.To)
//                ))
//            ) {
//                Toast.makeText(
//                    context,
//                    context.getString(R.string.Please_fill_the_interval),
//                    Toast.LENGTH_SHORT
//                )
//                    .show()
//                return@setOnClickListener
//            }
            //slot 1 to time
            if (arrayslot1[0].to.contains(":")) {
                val converter = SimpleDateFormat("hh:mm a", Locale.US)
                val sourceto = converter.parse(arrayslot1[0].to)
                GlobalTime = sourceto
            }
            //slot 2 from time
            if (arrayslot1[1].from.contains(":")) {
                val converter = SimpleDateFormat("hh:mm a", Locale.US)
                val sourceto = converter.parse(arrayslot1[1].from)
                GlobalTimeFrom2 = sourceto
            }
            timeDialog(context, slot.from, slot.to, position, true, 1)
        }
        //clicks of to
        holder.itemView.textTo1.setOnClickListener {

            if (holder.itemView.TxtFrom1.text.contains(context.getString(R.string.From))) {

                Toast.makeText(
                    context,
                    context.getString(R.string.Please_select_the_time_in_from),
                    Toast.LENGTH_SHORT
                )
                    .show()
                return@setOnClickListener
            }
            //slot 2 from time
            if (arrayslot1[1].from.contains(":")) {
                val converter = SimpleDateFormat("hh:mm a", Locale.US)
                val sourceto = converter.parse(arrayslot1[1].from)
                GlobalTimeFrom2 = sourceto
            }

            timeDialog(context, slot.from, slot.to, position, false, 2)
        }
        //***************replicate button click
        holder.itemView.btnReplicate.setOnClickListener {
            if (slot.from.contains(context.getString(R.string.From)) || slot.to.contains(
                    context.getString(
                        R.string.To
                    )
                )
            ) {
                Toast.makeText(
                    context,
                    context.getString(R.string.fill_the_time_interval),
                    Toast.LENGTH_SHORT
                )
                    .show()
                return@setOnClickListener
            } else {
                listener.replicatesTimeZone(slot, position, parentPosition)
            }
        }

    }

    fun updateData(
        updateData: DaysParentModel
    ) {
        arrayslot1 = updateData.slotList
        notifyDataSetChanged()

    }

    class Viewholder(itemView: View) : RecyclerView.ViewHolder(itemView)

    @SuppressLint("SetTextI18n")
    fun timeDialog(
        context: Context,
        from: String,
        to: String,
        position: Int,
        isFrom: Boolean,
        i: Int
    ) {
        val mcurrentTime = Calendar.getInstance()
        val hour = mcurrentTime.get(Calendar.HOUR_OF_DAY)
        val minute = mcurrentTime.get(Calendar.MINUTE)
        val mTimePicker: TimePickerDialog
        mTimePicker = TimePickerDialog(
            context,
            TimePickerDialog.OnTimeSetListener { timePicker, selectedHour, selectedMinute ->
                //*****************time slot of To
                if (i == 2) {//To check before time i==2 is used for to
                    val abc = dateFormatter(
                        selectedHour,
                        selectedMinute,
                        position,
                        isFrom,
                        i,
                        from,
                        to
                    )
                    if (abc.equals("abc")) {
                        Toast.makeText(
                            context,
                            context.getString(R.string.You_cannot_select_time),
                            Toast.LENGTH_SHORT
                        ).show()
                        return@OnTimeSetListener
                    }
                }

//*****************time slot of From
                if (i == 1) {// i ==1 is used for from
                    val FromSlot = dateFormatter(
                        selectedHour,
                        selectedMinute,
                        position,
                        isFrom,
                        i,
                        from,
                        to
                    )
                    if (FromSlot.equals("FromSlot")) {
                        Toast.makeText(
                            context,
                            context.getString(R.string.You_cannot_select_time),
                            Toast.LENGTH_SHORT
                        ).show()
                        return@OnTimeSetListener
                    }
                }
            }, hour, minute, true
        )//Yes 24 hour time
        mTimePicker.setTitle(context.getString(R.string.Select_Time))
        mTimePicker.show()
    }

    private fun dateFormatter(
        selectedHour: Int,
        selectedMinute: Int,
        position: Int,
        isFrom: Boolean,
        i: Int,
        from: String,
        to: String
    ): String {
        val date = "$selectedHour:$selectedMinute"
        val formatter = SimpleDateFormat("HH:mm", Locale.US)
        val converter = SimpleDateFormat("hh:mm a", Locale.US)
        val sourceCurrentTime = formatter.parse(date)//my current time

        if (i == 2) {
            val sourceto = converter.parse(from)
            if (sourceCurrentTime.before(sourceto) || sourceCurrentTime.equals(sourceto)) {
                return "abc"
            } else if (i == 2 && position == 0) {
                if (GlobalTimeFrom2 != null) {
                    if (sourceCurrentTime.after(GlobalTimeFrom2) || sourceCurrentTime.equals(
                            GlobalTimeFrom2
                        )
                    ) {
                        Toast.makeText(
                            context,
                            context.getString(R.string.invalidate_date_text),
                            Toast.LENGTH_SHORT
                        ).show()
                        return "validate date"

                    } else {
                        val formattedTime = converter.format(sourceCurrentTime)
                        listener.onTimeSelect(
                            position,
                            parentPosition,
                            formattedTime,
                            isFrom
                        )
                        return formattedTime
                    }
                }
            } else {
                val formattedTime = converter.format(sourceCurrentTime)
                listener.onTimeSelect(
                    position,
                    parentPosition,
                    formattedTime,
                    isFrom
                )
                return formattedTime
            }
        }

        ///for the second slot from compare with 1st slot of to
        if (i == 1 && position == 1) {
            if (GlobalTime != null) {
                if (sourceCurrentTime.before(GlobalTime) || sourceCurrentTime.equals(GlobalTime)) {
                    return "FromSlot"
                } else {
                    val formattedTime = converter.format(sourceCurrentTime)
                    listener.onTimeSelect(
                        position,
                        parentPosition,
                        formattedTime,
                        isFrom
                    )
                    return formattedTime
                }
            } else {
                val formattedTime = converter.format(sourceCurrentTime)
                listener.onTimeSelect(
                    position,
                    parentPosition,
                    formattedTime,
                    isFrom
                )
                return formattedTime
            }
        }
        //****************check for the from 0 postion compare with the from 1 **********//
        if (i == 1 && position == 0) {
            if (GlobalTimeFrom2 != null) {
                if (sourceCurrentTime.after(GlobalTimeFrom2) || sourceCurrentTime.equals(
                        GlobalTimeFrom2
                    )
                ) {
                    return "FromSlot"
                } else {
                    val formattedTime = converter.format(sourceCurrentTime)
                    listener.onTimeSelect(
                        position,
                        parentPosition,
                        formattedTime,
                        isFrom
                    )
                    return formattedTime
                }
            } else {
                val formattedTime = converter.format(sourceCurrentTime)
                listener.onTimeSelect(
                    position,
                    parentPosition,
                    formattedTime,
                    isFrom
                )
                return formattedTime
            }
        } else {
            val formattedTime = converter.format(sourceCurrentTime)
            listener.onTimeSelect(
                position,
                parentPosition,
                formattedTime,
                isFrom
            )
            return formattedTime
        }
    }


    interface TimeSelectListener {
        fun onTimeSelect(
            childIndex: Int,
            parentIndex: Int,
            time: String,
            isFrom: Boolean
        )

        fun replicatesTimeZone(
            slot: SlotModel,
            position: Int,
            parentPosition: Int
        )
    }
}
