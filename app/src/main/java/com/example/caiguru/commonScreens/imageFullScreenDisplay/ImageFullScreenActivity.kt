package com.example.caiguru.commonScreens.imageFullScreenDisplay

import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.MotionEvent
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.caiguru.R
import com.example.caiguru.commonScreens.dashBoardParentActivity.dashboardStartingNotification.sellerUnSeenNotification.ZoomOutPageTransformer
import com.example.caiguru.databinding.ActivityImageFullScreenBinding
import com.example.caiguru.seller.shoppingListSellers.sellerPostShoppingList.PostShoppingModel
import kotlinx.android.synthetic.main.activity_image_full_screen.*


class ImageFullScreenActivity : AppCompatActivity() {

    private lateinit var AdapterFullScreen: FullScreenImageViewAdapter
    private lateinit var mbinding: ActivityImageFullScreenBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mbinding = DataBindingUtil.setContentView(this, R.layout.activity_image_full_screen)
        window.setBackgroundDrawable(ColorDrawable(0))

        val setdata = intent.getParcelableArrayListExtra<PostShoppingModel>("keyImage")!!
        val position = intent.getIntExtra("keyImagePosition", 0)

        AdapterFullScreen = FullScreenImageViewAdapter(this, setdata)

        viewPagerFullScreenView.setPageTransformer(
            true,
            ZoomOutPageTransformer()
        )
        viewPagerFullScreenView!!.adapter = AdapterFullScreen
        viewPagerFullScreenView.setCurrentItem(position)
   


        mbinding.cross.setOnClickListener {
//            val intent = Intent()
//            setResult(Activity.RESULT_OK, intent)//set the result
            finish()


        }

    }
    override fun onTouchEvent(ev: MotionEvent?): Boolean {
        try {
            return super.onTouchEvent(ev)
        } catch (ex: IllegalArgumentException) {
            ex.printStackTrace()
        }
        return false
    }


}
