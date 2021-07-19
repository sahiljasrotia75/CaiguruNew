package com.example.caiguru.buyer.buyerProfile.buyerEditProfile

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.location.Address
import android.location.Geocoder
import android.media.ExifInterface
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.text.InputFilter
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.content.FileProvider
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.TransformationUtils
import com.example.caiguru.R
import com.example.caiguru.buyer.buyerAddAddress.BuyerAddressMapBoxActivity
import com.example.caiguru.buyer.buyerProfile.buyerCreditEarned.BuyerCreditEarnedViewModel
import com.example.caiguru.buyer.buyerProfile.follower.FollowerBuyerActivity
import com.example.caiguru.buyer.buyerProfile.followings.FollowingBuyerActivity
import com.example.caiguru.commonScreens.hideEmoji.HideEmoji
import com.example.caiguru.commonScreens.otherProfiles.otherProfile.OtherProfileViewActivity
import com.example.caiguru.databinding.OpenFromCameraPermissionBinding
import com.example.caiguru.commonScreens.registerCategory.CategoryModel
import com.example.caiguru.commonScreens.registerCategory.RegisterCategoryActivity
import com.example.caiguru.databinding.ActivityBuyerEditprofileBinding
import com.example.caiguru.seller.shoppingListSellers.deliveryZoneSeller.DeliveryZoneModel
import com.example.caiguru.seller.homeSeller.GetProfileModel
import com.example.caiguru.seller.sellerChangePassword.ChangePasswordActivity
import com.example.caiguru.seller.sellerChooseCategory.SellerCategoryModel
import com.example.caiguru.seller.shoppingListSellers.listUploadedByCustomer.listUploadedByCustomers.CustomerChildModel
import com.google.firebase.iid.FirebaseInstanceId
import com.google.gson.Gson
import constant_Webservices.BuyerLevelModel
import constant_Webservices.Constant
import kotlinx.android.synthetic.main.activity_editprofile.editemail
import kotlinx.android.synthetic.main.activity_editprofile.editname
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class BuyerEditProfileActivity : AppCompatActivity() {


    private  var addressFull: String=""
    private var model = GetProfileModel()
    lateinit var mbinding: ActivityBuyerEditprofileBinding
    lateinit var mvmodel: BuyerEditProfileViewModel
    lateinit var text: TextView
    private var photoFile: File? = null
    private var lat: String = ""
    private var long: String = ""
    var updatedimage = ""
    private var tokens: String = ""
    var addressModel = DeliveryZoneModel()
    private lateinit var editCategoryAdapter: BuyerEditCategoryAdapter
    private var menuMain: Menu? = null

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mbinding = DataBindingUtil.setContentView(this, R.layout.activity_buyer_editprofile)
      //  mvmodel = ViewModelProviders.of(this)[BuyerEditProfileViewModel::class.java]
        mvmodel = ViewModelProvider(this).get(BuyerEditProfileViewModel::class.java)
        SettingUpToolbar()
        //code change for updating follower and follower
        tokens = FirebaseInstanceId.getInstance().token!!

        model = GetProfileModel()
        val gson = Gson()
        val json = Constant.getPrefs(this).getString(Constant.PROFILE, "")
        model = gson.fromJson(json, GetProfileModel::class.java)
        hidePasswordField()
        editname.filters = arrayOf<InputFilter>(HideEmoji(this))
        editemail.filters = arrayOf<InputFilter>(HideEmoji(this))

        if (model.image.isEmpty()) {
            Glide.with(this).load(R.drawable.product_placeholder)
                .into(mbinding.img)
        } else {
            Glide.with(this).load(model.image).into(mbinding.img)
        }

        Glide.with(this).load(levelget(model.buyer_level).levelImage).into(mbinding.imgBatchbuyer)
        mbinding.following.setText(model.followings + " " + getString(R.string.Followings) + " ")
        mbinding.followers.setText(" " + model.followers + " " + getString(R.string.Followers))
        mbinding.point.setText(
            levelget(model.buyer_level).levelname + " " + "(" + model.buyer_level + " " + getString(
                R.string.points
            ) + ")"
        )
        Glide.with(this).load(levelget(model.buyer_level).levelImage).into(mbinding.levelimgbuyer)
        mbinding.good.setText(Constant.getReputationString(this, model.buyer_reputation))
        mbinding.editname.setText(model.name)
        mbinding.editemail.setText(model.email)
        getCategoryAdapter()

        if (addressModel.lat.isEmpty()) {
            lat = model.lat
            long = model.long
        }
        mbinding.editaddress.setText(model.full_address)
        //*********************observer of update profile image****************//
        //sucessful
        mvmodel.getdatasucessful().observe(this, Observer {
            updatedimage = it.image
            Glide.with(this).asBitmap().load(updatedimage).into(mbinding.img)
            mbinding.progressimg.visibility = View.GONE
        })
        //failure
        mvmodel.errorget().observe(this, Observer {
            Constant.showToast(it,this)
            mbinding.progressimg.visibility = View.GONE
        })

        mbinding.editchangepassword.setOnClickListener {
            val intent = Intent(this, ChangePasswordActivity::class.java)
            startActivity(intent)

        }
        mbinding.edit.setOnClickListener {
            val intent = Intent(this, RegisterCategoryActivity::class.java)
            intent.putExtra("edit", model)
            startActivityForResult(intent, 201)
        }
        //image set
        mbinding.relativeImage.setOnClickListener {
            if (Constant.checkPermissionForCameraGallery(this))
                showDialog()
        }
        //**************************************observer updateprofile ***************************************************//

        mvmodel.mSucessfulupdateData().observe(this, Observer {
            Constant.showToast(it.msg,this)

            mbinding.btnPleaseWait.visibility = View.GONE
            mbinding.btnUpdate.visibility = View.VISIBLE

        })
        mvmodel.mFailureError().observe(this, Observer {
            Constant.showToast( it.message,this)
            mbinding.btnPleaseWait.visibility = View.GONE
            mbinding.btnUpdate.visibility = View.VISIBLE
        })

        //***********************************************Update Profile*************************************//
        mbinding.btnUpdate.setOnClickListener {

            if (mbinding.editname.text.isEmpty()) {

                Constant.showToast(getString(R.string.please_enter_your_name),this)
            } else if (mbinding.editemail.text.isEmpty()) {
                Constant.showToast(getString(R.string.please_enter_your_email),this)
            } else if (mbinding.editaddress.text.isEmpty()) {
                Constant.showToast(getString(R.string.please_enter_your_address),this)
            } else {

                val edtname = editname.text.toString().trim()
                val editemail = editemail.text.toString().trim()
                //  val editaddress = editaddress.text.toString().trim()

                if (updatedimage != "") {
                    mvmodel.updateprofile(
                        edtname,
                        updatedimage,
                        editemail,
                        model.categories,
                        addressModel.address,
                        lat,
                        long
                    )
                    mbinding.btnPleaseWait.visibility = View.VISIBLE
                    mbinding.btnUpdate.visibility = View.GONE
                } else {
                    mvmodel.updateprofile(
                        edtname,
                        model.image,
                        editemail,
                        model.categories,
                        addressModel.address,
                        lat,
                        long
                    )
                    mbinding.btnPleaseWait.visibility = View.VISIBLE
                    mbinding.btnUpdate.visibility = View.GONE
                }
            }
        }

        mbinding.editaddress.setOnClickListener {
            // val intent = Intent(this, UpdatedAddressActivity::class.java)
            val intent = Intent(this, BuyerAddressMapBoxActivity::class.java)
            intent.putExtra("change", "address")
            if (addressModel.lat.isEmpty()) {
                lat = model.lat
                long = model.long
                addressFull = model.full_address
                intent.putExtra("keyProfileBuyerLat", lat)
                intent.putExtra("keyProfileBuyerLong", long)
                intent.putExtra("MapType", addressFull)
                startActivityForResult(intent, 110)
            } else {
                intent.putExtra("keyProfileBuyerLat", lat)
                intent.putExtra("keyProfileBuyerLong", long)
                intent.putExtra("MapType", addressFull)
                startActivityForResult(intent, 110)
            }
            try {
                if (ActivityCompat.checkSelfPermission(
                        this,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                        this, Manifest.permission.ACCESS_COARSE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    ActivityCompat.requestPermissions(
                        this, arrayOf(
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION
                        ), 110
                    )

                }

            } catch (e: Exception) {
                e.printStackTrace()
            }

        }

        //set the click on the following
        mbinding.following.setOnClickListener {
            val intent = Intent(this, FollowingBuyerActivity::class.java)
            startActivity(intent)
        }
        //set the click on the follower
        mbinding.followers.setOnClickListener {
            val intent = Intent(this, FollowerBuyerActivity::class.java)
            startActivity(intent)
        }
        //****************updating the follower and following
        //***********sucessful
        mvmodel.mSucessfulGetProfile().observe(this, Observer {
            mbinding.following.setText(it.followings + " " + getString(R.string.Followings) + " ")
            mbinding.followers.setText(" " + it.followers + " " + getString(R.string.Followers))
        })
        //failure
        mvmodel.errorGetProfile().observe(this, Observer {
            Constant.showToast(it.message,this)
        })

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == 102) {
            if (grantResults.contains(-1)) {

            } else {
                showDialog()

            }
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onResume() {
        super.onResume()
        mvmodel.getProfile(tokens)
//        model = GetProfileModel()
//        val gson = Gson()
//        val json = Constant.getPrefs(this).getString(Constant.PROFILE, "")
//        model = gson.fromJson(json, GetProfileModel::class.java)
//
//        if (model.image.isEmpty()) {
//            Glide.with(this).load(R.drawable.product_placeholder)
//                .into(mbinding.img)
//        } else {
//            Glide.with(this).load(model.image).into(mbinding.img)
//        }
//
//        Glide.with(this).load(levelget(model.buyer_level).levelImage).into(mbinding.imgBatchbuyer)
//        mbinding.following.setText(model.followings + " " + getString(R.string.Following) + " ")
//        mbinding.followers.setText(" " + model.followers + " " + getString(R.string.Followers))
//        mbinding.point.setText(
//            levelget(model.buyer_level).levelname + " " + "(" + model.buyer_level + " " + getString(
//                R.string.points
//            ) + ")"
//        )
//        Glide.with(this).load(levelget(model.buyer_level).levelImage).into(mbinding.levelimgbuyer)
//        mbinding.good.setText(model.buyer_reputation)
//        mbinding.editname.setText(model.name)
//        mbinding.editemail.setText(model.email)
//        getCategoryAdapter()

    }
    private fun hidePasswordField() {
        if (Constant.getProfileData(this).social_type=="1"){
            mbinding.view2.visibility=View.VISIBLE
            mbinding.textViewee.visibility=View.VISIBLE
            mbinding.editchangepassword.visibility=View.VISIBLE
        }else{
            mbinding.view2.visibility=View.GONE
            mbinding.textViewee.visibility=View.GONE
            mbinding.editchangepassword.visibility=View.GONE
        }

    }
    private fun getRotateImage(photoFile: File?): Bitmap {

        val ei: ExifInterface = ExifInterface(photoFile!!.path)
        val orientation = ei.getAttributeInt(
            ExifInterface.TAG_ORIENTATION,
            1
        )
        val bitmap = BitmapFactory.decodeFile(photoFile.path)

        var rotatedBitmap: Bitmap? = bitmap
        when (orientation) {
            ExifInterface.ORIENTATION_ROTATE_90 ->
                rotatedBitmap = TransformationUtils.rotateImage(bitmap, 90)

            ExifInterface.ORIENTATION_ROTATE_180 ->
                rotatedBitmap = TransformationUtils.rotateImage(bitmap, 180)

            ExifInterface.ORIENTATION_ROTATE_270 ->
                rotatedBitmap = TransformationUtils.rotateImage(bitmap, 270)


            ExifInterface.ORIENTATION_NORMAL ->
                rotatedBitmap = bitmap
            ExifInterface.ORIENTATION_FLIP_HORIZONTAL -> flipImage(bitmap, true, false)
            ExifInterface.ORIENTATION_FLIP_VERTICAL -> flipImage(bitmap, false, true)
        }

        return rotatedBitmap!!
    }


    private fun rotateImage(bitmap: Bitmap, degrees: Float): Bitmap {
        val matrix = Matrix()
        matrix.postRotate(degrees)
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
    }

    private fun flipImage(bitmap: Bitmap, horizontal: Boolean, vertical: Boolean): Bitmap {
        val matrix = Matrix()
        matrix.preScale((if (horizontal) -1 else 1).toFloat(), (if (vertical) -1 else 1).toFloat())
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
    }

    private fun showDialog() {
        val builder: androidx.appcompat.app.AlertDialog.Builder =
            androidx.appcompat.app.AlertDialog.Builder(this)
        builder.setTitle(getString(R.string.Open_By))
        val mBinding: OpenFromCameraPermissionBinding =
            DataBindingUtil.inflate(
                LayoutInflater.from(this),
                R.layout.open_from_camera_permission,
                null,
                false
            )
        var check: Int = -1

        if (check == 0) {

            mBinding.camera.isChecked = true
            mBinding.gallery.isChecked = false
        }
        if (check == 1) {
            mBinding.gallery.isChecked = false
            mBinding.camera.isChecked = true
        }
        builder.setView(mBinding.root)

        builder.setCancelable(false)

        builder.setPositiveButton(getString(R.string.ok)) { dialog, which ->

            if (mBinding.camera.isChecked) {
                openCamera()
                check = 0


            } else if (mBinding.gallery.isChecked) {
                openGallery()
                check = 1

            }
            dialog.dismiss()
        }

        builder.setNegativeButton(getString(R.string.cancel)) { dialog, which ->

        }
        builder.create()
        builder.show()
    }


    @SuppressLint("IntentReset")
    private fun openGallery() {
//        val intent = Intent("android.intent.action.GET_CONTENT")
//        intent.type = "image/*"
//        startActivityForResult(intent, 1)

        val intent = Intent(
            Intent.ACTION_PICK,
          MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        );
        intent.setType("image/*")
        startActivityForResult(intent, 1)

    }

    private fun openCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        try {
            photoFile = createImageFile()
        } catch (ex: IOException) {
            // Error occurred while creating the File
            Log.i("", "IOException")
        }
        // Continue only if the File was successfully created
        if (photoFile != null) {
            val uri = FileProvider.getUriForFile(
                this,
                "com.app.caiguru" + ".provider",
                photoFile!!
            )
            intent.putExtra(MediaStore.EXTRA_OUTPUT, uri)
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            startActivityForResult(intent, 29)
        }
    }

    @SuppressLint("SimpleDateFormat")
    private fun createImageFile(): File {
        // Create an image file name
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val imageFileName = "JPEG_" + timeStamp + "_"
        val storageDir = Environment.getExternalStoragePublicDirectory(
            Environment.DIRECTORY_PICTURES
        )
        val image = File.createTempFile(
            imageFileName,  // prefix
            ".jpg",         // suffix
            storageDir      // directory
        )
        return image
    }


    private fun SettingUpToolbar() {
        val toolbar = findViewById<Toolbar>(R.id.commontoolbar)
        title = ""
        setSupportActionBar(toolbar)
        //set the text
        text = findViewById(R.id.toolbartittle)
        text.text = getText(R.string.myprofile)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_back_icon)
    }

    // ..........back button click...........//
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item != null) {
            if (item.itemId == android.R.id.home) {
                finish()
            } else if (item.itemId == R.id.edit) {
                val models = CustomerChildModel()
                val intent = Intent(this, OtherProfileViewActivity::class.java)
                models.buyer_id = model.user_id
                models.name = model.name
                intent.putExtra("keyOpenMyProfileBuyerSide", models)
                startActivity(intent)
            }
        }

        return super.onOptionsItemSelected(item)
    }

    //****************************************** set Menu in toolbar ****************************//
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        getMenuInflater().inflate(R.menu.profile_menu, menu)
        menuMain = menu
        return true
    }

    private fun levelget(level: String): BuyerLevelModel {

        val bellerLevel = Constant.BuyerLevel(this)
        for (category in bellerLevel) {
            if (category.levelType == level.trim()) {

                return category
            }
        }
        return BuyerLevelModel()
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 110 && data != null) {

            if (resultCode == Activity.RESULT_OK) {
                try {
                    val geocoder = Geocoder(this)
                    val address: Address?
                    try {
                        lat = data.getStringExtra("lat")!!
                        long = data.getStringExtra("long")!!
                        // 2
                        val addresses =
                            geocoder.getFromLocation(lat.toDouble(), long.toDouble(), 1)
                        address = addresses[0]
                        Log.e("Map Fragment ", address.toString())
                        val builder = StringBuilder()
                        builder.append(address?.getAddressLine(0)).append(",\t")
                        val resultDestination = builder.toString()
                        addressModel = data.getParcelableExtra("deliveryZone")!!
                      //  addressModel.address = resultDestination
                        addressFull=addressModel.address
                        mbinding.editaddress.setText(addressModel.address)
                        Log.e("Map Fragment2 ", resultDestination)
                    } catch (e: IOException) {
                        Log.e("MapsActivity", e.localizedMessage)
                    }
                } catch (e: Exception) {
                    Log.e("MainActivity", e.message.toString())
                }
            }
        }
        //......................getting the image of dialog...........//
        try {
            if (requestCode == 29) {

                try {

                    val resultBitmap = getRotateImage(photoFile)
                    val uri = getImageUri(resultBitmap)
//..................................Api FOr Update Image....................................................................//
                    //   mvmodel.profileImage(uri)//send the data for the api
                    Constant.saveBitmapToFile(uri)?.let {
                        mvmodel.profileImage(it)
                    }//send the data for the api
                    mbinding.progressimg.visibility = View.VISIBLE

                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
            //2nd code image req code
            else if (requestCode == 1 && data != null) {
                try {
//                    val bitmap2: Uri = data.data!!
//                    var file: File = Constant.uploadImage(bitmap2, this)
                    val selectedPicture: Uri = data.getData()!!
                    val file: File =
                        Constant.uploadImage2(Constant.getBitmapImage(selectedPicture, this), this)
                    //..................................Api FOr Update Image from gallery....................................................................//
                    mvmodel.profileImage(file)// send the data for the api
                    mbinding.progressimg.visibility = View.VISIBLE
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        if (requestCode == 201 && resultCode == Activity.RESULT_OK) {
            val model = data!!.getParcelableExtra<CategoryModel>("keyUpdatedData")!!
            editCategoryAdapter.update(categoryDataChecked(model))
        }
    }

    private fun categoryDataChecked(getData: CategoryModel): java.util.ArrayList<SellerCategoryModel> {
        val categoriesList = Constant.categoryData1(this)
        val arrayData = ArrayList<SellerCategoryModel>()
        val selectedCategories = getData.category_id.split(",")  // ( -> converted that list to array that why we write .split(,))
        val arrayListCategoryId = ArrayList<String>()//global

        for (category in categoriesList) {
            for (i in 0 until selectedCategories.size) {
                if (category.category_id == selectedCategories[i].trim()) {
                    val model = SellerCategoryModel()
                    model.name = category.name
                    model.imagewhite = category.imagewhite
                    model.imageyellow = category.imageyellow
                    model.category_id = category.category_id

                    arrayListCategoryId.add(category.category_id)
                    arrayData.add(model)
                }
            }

        }
        if (arrayListCategoryId.size > 0) {
            val categoryId = TextUtils.join(",", arrayListCategoryId)  //   Convert arraylist to string
            model.categories = categoryId
        }
        return arrayData
    }

    //url link get to used this
//    private fun getImageUri2(inImage: Bitmap): File {
//        val root: String = Environment.getExternalStorageDirectory().toString()
//        val myDir = File("$root/req_images")
//        myDir.mkdirs()
//        val fname = "Image_profile.jpg"
//        val file = File(myDir, fname)
//        if (file.exists())
//            file.delete()
//
//        try {
//            val out = FileOutputStream(file)
//            inImage.compress(Bitmap.CompressFormat.JPEG, 80, out)
//            out.flush()
//            out.close()
//        } catch (e: Exception) {
//            e.printStackTrace()
//        }
//        return file
//    }

    private fun getImageUri(inImage: Bitmap): File {
        val root: String = Environment.getExternalStorageDirectory().toString()
        val myDir = File("$root/req_images")
        val fname = "Image_profile.jpg"
        val file = File(myDir, fname)
        if (file.exists())
            file.delete()
        myDir.mkdirs()
        try {
            val out = FileOutputStream(file)
            inImage.compress(Bitmap.CompressFormat.JPEG, 100, out)
            out.flush()
            out.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return file
    }

    //get selcted city adapter
    private fun getCategoryAdapter() {
        val manager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        mbinding.rveditcategory.layoutManager = manager
        editCategoryAdapter = BuyerEditCategoryAdapter(this)
        mbinding.rveditcategory.adapter = editCategoryAdapter
        editCategoryAdapter.update(categoryData())
    }

    fun categoryData(): ArrayList<SellerCategoryModel> {
        val categoriesList = Constant.categoryData1(this)
        // from profile details
        val gson = Gson()
        val json = Constant.getPrefs(this).getString(Constant.PROFILE, "")
        val model: GetProfileModel = gson.fromJson(json, GetProfileModel::class.java)
        val selectedCategories = model.categories.split(",")
        //global
        val arrayData = ArrayList<SellerCategoryModel>()
        for (category in categoriesList) {
            for (i in 0 until selectedCategories.size) {
                if (category.category_id == selectedCategories[i].trim()) {
                    val model = SellerCategoryModel()
                    model.name = category.name
                    model.imagewhite = category.imagewhite
                    model.imageyellow = category.imageyellow
                    model.category_id = category.category_id
                    arrayData.add(model)
                }
            }
        }
        return arrayData
    }

}