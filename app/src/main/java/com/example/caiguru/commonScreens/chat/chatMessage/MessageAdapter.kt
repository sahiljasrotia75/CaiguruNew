package com.example.caiguru.commonScreens.chat.chatMessage

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.caiguru.R
import com.example.caiguru.commonScreens.chat.ModelChat
import com.example.caiguru.commonScreens.imageFullScreenDisplay.zoomImageView.FullScreenImageWithZoomActivity
import com.example.caiguru.seller.homeSeller.GetProfileModel
import com.google.gson.Gson
import constant_Webservices.BuyerLevelModel
import constant_Webservices.Constant
import constant_Webservices.SellerLevelModel
import kotlinx.android.synthetic.main.item_message_sent.view.image_message_profile
import kotlinx.android.synthetic.main.item_message_sent.view.image_message_profile2
import kotlinx.android.synthetic.main.item_message_sent.view.layoutMessage
import kotlinx.android.synthetic.main.item_message_sent.view.layoutImage
import kotlinx.android.synthetic.main.item_message_sent.view.message_image
import kotlinx.android.synthetic.main.item_message_sent.view.text_message_body
import kotlinx.android.synthetic.main.item_message_sent.view.text_message_time
import kotlinx.android.synthetic.main.item_message_sent.view.text_message_time2
import kotlinx.android.synthetic.main.item_messege_received.view.*
import kotlinx.android.synthetic.main.item_messege_received.view.imgBatchs
import java.io.File

class MessageAdapter(var context: Context, var otherUser: ModelChat) :
    RecyclerView.Adapter<MessageAdapter.ViewHolder>() {

 //   private var myProfile: GetProfileModel
    var messageList = ArrayList<MessageItem>()
    var inflater = LayoutInflater.from(context)

//    init {
//        val gson = Gson()
//        val json = Constant.getPrefs(context).getString(Constant.PROFILE, "")
//        myProfile = gson.fromJson(json, GetProfileModel::class.java)
//
//    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val view = if (viewType == 1) {
            inflater.inflate(R.layout.item_message_sent, parent, false)
        } else {
            inflater.inflate(R.layout.item_messege_received, parent, false)
        }

        return ViewHolder(view)
    }

    override fun getItemViewType(position: Int): Int {
        val message = messageList[position]
        return if (message.user_id == otherUser.user_id) {
            2
        } else {
            1
        }
    }

    override fun getItemCount(): Int {
        return messageList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val message = messageList[position]

        when (holder.itemViewType) {
            1 -> {

                if (message.msgType == "1"){
                    // show text message
                    holder.itemView.text_message_body.text =message.message.substring(0, 1).toUpperCase() + message.message.substring(1)
                    holder.itemView.layoutMessage.visibility = View.VISIBLE
                    holder.itemView.layoutImage.visibility = View.GONE

                    if (message.created_at == context.getString(R.string.Just_Now)) {
                        holder.itemView.text_message_time.text = message.created_at
                    } else {
                        holder.itemView.text_message_time.text =
                            Constant.timesAgoLogic(message.created_at,context)
                    }
//                    if (Constant.getProfileData(context).image.isNotEmpty()){
//                        Glide.with(context).load(Constant.getProfileData(context).image)
//                            .into(holder.itemView.image_message_profile)
//                    }else{
//                        Glide.with(context).load(R.drawable.user_placeholder)
//                            .into(holder.itemView.image_message_profile)
//                    }
                    Glide.with(context).load(Constant.getProfileData(context).image)
                        .into(holder.itemView.image_message_profile)

                }
                else{
                    // show image message
                    holder.itemView.layoutMessage.visibility = View.GONE
                    holder.itemView.layoutImage.visibility = View.VISIBLE

                    if (message.created_at == context.getString(R.string.Just_Now)) {
                        holder.itemView.text_message_time2.text = message.created_at
                    } else {
                        holder.itemView.text_message_time2.text =
                            Constant.timesAgoLogic(message.created_at,context)
                    }
//                    if (Constant.getProfileData(context).image.isNotEmpty()){
//                        Glide.with(context).load(Constant.getProfileData(context).image)
//                            .into(holder.itemView.image_message_profile)
//                    }else{
//                        Glide.with(context).load(R.drawable.user_placeholder)
//                            .into(holder.itemView.image_message_profile)
//                    }

                    if (message.image.startsWith("http")){
                        Glide.with(context).load(message.image)
                            .into(holder.itemView.message_image)
                    }else{

                        val file = File(message.image)
                        Glide.with(context).load(file)
                            .into(holder.itemView.message_image)

                    }
                    //not check the cde
                    Glide.with(context).load(Constant.getProfileData(context).image)
                        .into(holder.itemView.image_message_profile2)
                }

            }

            else -> {
               // holder.itemView.text_message_body.text = message.message
               // holder.itemView.text_message_time.text = Constant.timesAgoLogic(message.created_at,context)
                //holder.itemView.text_message_name.text = otherUser.name



                if (message.msgType == "1"){
                    // show text message
                    if (otherUser.type == "2"){
                        val data = levelget(otherUser.level)
                        holder.itemView.imgBatchs.setImageResource(data.levelImage)
                    }else {
                        val data = levelgetBuyer(otherUser.level)
                        holder.itemView.imgBatchs.setImageResource(data.levelImage)
                    }

                    holder.itemView.text_message_name.text = otherUser.name
                    holder.itemView.text_message_time.text = Constant.timesAgoLogic(message.created_at,context)
                    holder.itemView.text_message_body.text = message.message.substring(0, 1).toUpperCase() + message.message.substring(1)
                    holder.itemView.layoutMessage.visibility = View.VISIBLE
                    holder.itemView.layoutImage.visibility = View.GONE
                    Glide.with(context).load(otherUser.image)
                        .into(holder.itemView.image_message_profile)

                }else{
                    // show image message
                    if (otherUser.type == "2"){
                        val data = levelget(otherUser.level)
                        holder.itemView.imgBatchs2.setImageResource(data.levelImage)
                    }else {
                        val data = levelgetBuyer(otherUser.level)
                        holder.itemView.imgBatchs2.setImageResource(data.levelImage)
                    }

                    holder.itemView.text_message_name2.text = otherUser.name
                    holder.itemView.text_message_time2.text = Constant.timesAgoLogic(message.created_at,context)
                    holder.itemView.layoutMessage.visibility = View.GONE
                    holder.itemView.layoutImage.visibility = View.VISIBLE
                    if (message.image.startsWith("http")){
                        Glide.with(context).load(message.image)
                            .into(holder.itemView.message_image)
                    }else{
                        val file = File(message.image)
                        Glide.with(context).load(file)
                            .into(holder.itemView.message_image)
                    }

                    Glide.with(context).load(otherUser.image)
                        .into(holder.itemView.image_message_profile2)
                }
            }
        }


        holder.itemView.message_image.setOnClickListener {
           // val list=ArrayList<PostShoppingModel>()
            if (message.image!="http://18.229.181.113/dev/public/users/defaultprod.png"){
                val intent = Intent(context, FullScreenImageWithZoomActivity::class.java)
                //change code image
//                for (item in messageList) {
//                    val model = PostShoppingModel()
//                    model.image = item.image
////                    model.price = item.price
////                    model.unit = item.unit
//                    list.add(model)
//                }

                intent.putExtra("keyImage", message.image)
               // intent.putExtra("keyImagePosition", position)

                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
                context.startActivity(intent)
            }
        }

    }

    fun update(it: List<MessageItem>?) {
        messageList = it as ArrayList<MessageItem>
        notifyDataSetChanged()
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    private fun levelget(level: String): SellerLevelModel {
        val sellerLevel = Constant.SellerLevel(context)
        for (category in sellerLevel) {

            if (category.levelType == level.trim()) {

                return category
            }
        }
        return SellerLevelModel()
    }

    private fun levelgetBuyer(level: String): BuyerLevelModel {
        val sellerLevel = Constant.BuyerLevel(context)
        for (category in sellerLevel) {

            if (category.levelType == level.trim()) {

                return category
            }
        }
        return BuyerLevelModel()
    }
}