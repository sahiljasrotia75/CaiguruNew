package com.example.caiguru.commonScreens.imageFullScreenDisplay.zoomImageView

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.example.caiguru.R
import com.example.caiguru.seller.shoppingListSellers.sellerPostShoppingList.PostShoppingModel
import it.sephiroth.android.library.imagezoom.ImageViewTouchBase
import kotlinx.android.synthetic.main.activity_full_screen_image_with_zoom.*

class FullScreenImageWithZoomActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_full_screen_image_with_zoom)
       // fullScreenImges.setDisplayType(ImageViewTouchBase.DisplayType.FIT_TO_SCREEN)
        val data = intent.getStringExtra("keyImage")

        Glide.with(this)
            .load(data)
            .into(fullScreenImges)

        cross.setOnClickListener {
            finish()

        }
    }
}
