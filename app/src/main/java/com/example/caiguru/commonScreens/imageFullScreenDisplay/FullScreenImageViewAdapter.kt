package com.example.caiguru.commonScreens.imageFullScreenDisplay


import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.viewpager.widget.PagerAdapter
import com.bumptech.glide.Glide
import com.example.caiguru.R
import com.example.caiguru.seller.shoppingListSellers.sellerPostShoppingList.PostShoppingModel
import com.github.chrisbanes.photoview.PhotoView
import constant_Webservices.Constant
import java.lang.Exception


class FullScreenImageViewAdapter(
    var context: Context,
    var mData: ArrayList<PostShoppingModel>) :
    PagerAdapter() {

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object`

    }

    override fun getCount(): Int {
        return mData.size
    }

    @SuppressLint("SetTextI18n")
    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val itemView = LayoutInflater.from(container.context)
            .inflate(R.layout.full_scren_image, container, false)
        val imgDisplay = itemView.findViewById(R.id.fullScreenImg) as PhotoView
        val productName = itemView.findViewById(R.id.productName) as TextView
        val txtPrice = itemView.findViewById(R.id.txtPrice) as TextView
        try {
        if (mData[position].image != "http://18.229.181.113/dev/public/users/defaultprod.png") {
            Glide.with(context)
                .load(mData[position].image)
                .into(imgDisplay)

            if (mData[position].unit.isNotEmpty() && mData[position].priceWithComission.isNotEmpty()){
                productName.text =
                    "${mData[position].name} (${Constant.convertUnits(context,mData[position].unit)})"
                txtPrice.text = "$${Constant.roundValue(mData[position].priceWithComission.toDouble())}"
            }
        } else {
            imgDisplay.visibility = View.GONE
            productName.visibility = View.GONE
            txtPrice.visibility = View.GONE
        }

        }catch (e:Exception){
            e.printStackTrace()
        }

        container.addView(itemView)
        return itemView
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View?)
    }


}