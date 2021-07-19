package com.example.caiguru.seller.shoppingListSellers.openListt.openList

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.caiguru.R
import com.example.caiguru.commonScreens.imageFullScreenDisplay.ImageFullScreenActivity
import com.example.caiguru.seller.shoppingListSellers.sellerPostShoppingList.DialogModel
import com.example.caiguru.seller.shoppingListSellers.sellerPostShoppingList.PostShoppingModel
import constant_Webservices.Constant
import kotlinx.android.synthetic.main.open_list_child_adapter.view.*
import kotlinx.android.synthetic.main.open_list_child_adapter.view.productImage
import kotlinx.android.synthetic.main.open_list_child_adapter.view.productName
import kotlinx.android.synthetic.main.open_list_child_adapter.view.productUnit
import org.json.JSONArray

class OpenListChildAdapter(
    var context: Context
) :
    RecyclerView.Adapter<OpenListChildAdapter.ViewHolder>() {
    var child = ArrayList<DialogModel>()
    var click = context as playPauseInterface
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(context).inflate(R.layout.open_list_child_adapter, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return child.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val list = child[position]
        holder.itemView.productName.text =
            list.name + "(" + Constant.convertUnits(context, list.unit) + ")"
        holder.itemView.productUnit.text =
            "$" + Constant.roundValue(list.priceWithComission.toDouble())
        Glide.with(context).load(list.image).placeholder(R.drawable.product_placeholder)
            .into(holder.itemView.productImage)

        holder.itemView.productImage.setOnClickListener {
            val array = ArrayList<PostShoppingModel>()
            if (list.image != "http://18.229.181.113/dev/public/users/defaultprod.png") {
                val intent = Intent(context, ImageFullScreenActivity::class.java)
                //change code image
                for (item in child) {
                    val model = PostShoppingModel()
                    model.image = item.image
                    model.unit = item.unit
                    model.priceWithComission = item.priceWithComission
                    model.name = item.name
                    array.add(model)
                }
                intent.putExtra("keyImage", array)
                intent.putExtra("keyImagePosition", position)
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
                context.startActivity(intent)
            }
        }

        //"1: Play 2:Pause"
        ////check the pause and play btn
        if (list.status == "1") {
            holder.itemView.imgPausePlay.setImageDrawable(context.resources.getDrawable(R.drawable.ic_pause))

        } else {
            holder.itemView.imgPausePlay.setImageDrawable(context.resources.getDrawable(R.drawable.ic_play))

        }
        //set the click on the pause and play btnb
        holder.itemView.imgPausePlay.setOnClickListener {
            if (list.status == "1") {
                list.status = "2"
            } else {
                list.status = "1"
            }
            click.PlayPauseClick(list)
            notifyDataSetChanged()
        }

    }

    fun updateData(data: ArrayList<DialogModel>) {
        child = data
        notifyDataSetChanged()
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    interface playPauseInterface {
        fun PlayPauseClick(list: DialogModel)
    }

    fun getAllData(): ArrayList<DialogModel> {
        val array = ArrayList<DialogModel>()
        for (item in child) {
            val model = DialogModel()
            model.name = item.name
            model.image = item.image
            model.unit = item.unit
            model.qty = item.qty
            model.price = item.price
            model.priceWithComission = item.priceWithComission
            model.saved_product_id = item.saved_product_id
            model.list_id = item.list_id
            model.id = item.id
            model.status = item.status

            array.add(model)
        }
        return array

    }

}
