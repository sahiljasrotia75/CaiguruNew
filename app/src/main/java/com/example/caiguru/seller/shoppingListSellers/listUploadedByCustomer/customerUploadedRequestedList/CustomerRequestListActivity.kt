package com.example.caiguru.seller.shoppingListSellers.listUploadedByCustomer.customerUploadedRequestedList

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.media.ExifInterface
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.text.InputFilter
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.FileProvider
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.load.resource.bitmap.TransformationUtils
import com.example.caiguru.R
import com.example.caiguru.buyer.buyerProfile.buyerOrderDetalis.OrderDetailsViewModel
import com.example.caiguru.commonScreens.hideEmoji.HideEmoji
import com.example.caiguru.databinding.ActivityCustomerUploadedOpenListBinding
import com.example.caiguru.databinding.OpenFromCameraPermissionBinding
import com.example.caiguru.seller.homeSeller.GetProfileModel
import com.example.caiguru.seller.homeSeller.HomeSellerModel
import com.example.caiguru.seller.shoppingListSellers.listUploadedByCustomer.listUploadedByCustomers.CustomerChildModel
import com.example.caiguru.seller.shoppingListSellers.sellerPostShoppingList.PostShoppingModel
import com.google.gson.Gson
import constant_Webservices.Constant
import kotlinx.android.synthetic.main.refute_reason_dialog.*
import kotlinx.android.synthetic.main.requested_list_custom_dialog.*
import org.json.JSONArray
import org.json.JSONObject
import java.io.*

class CustomerRequestListActivity : AppCompatActivity(),
    CustomerRequestListChildAdapter.UpdateChildImage,
    CustomerRequestListParentAdapter.reportListInterface {

    private var getProfileModel = GetProfileModel()
    private var list_id: String = ""
    private var childData = PostShoppingModel()
    private var globalData = CustomerChildModel()
    private var childPosition: Int = 0
    private lateinit var dialog: Dialog
    private var username: String = ""
    private lateinit var text: TextView
    private lateinit var mvmodel: CustomerRequestListViewModel
    private lateinit var mbinding: ActivityCustomerUploadedOpenListBinding
    private lateinit var customerParentAdapter: CustomerRequestListParentAdapter
    private var photoFile: File? = null
    private var isUploadedImage: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mbinding =
            DataBindingUtil.setContentView(this, R.layout.activity_customer_uploaded_open_list)
       // mvmodel = ViewModelProviders.of(this)[CustomerRequestListViewModel::class.java]
        mvmodel = ViewModelProvider(this).get(CustomerRequestListViewModel::class.java)
        val gson = Gson()
        val json = Constant.getPrefs(this).getString(Constant.PROFILE, "")
        getProfileModel = gson.fromJson(json, GetProfileModel::class.java)

        if (intent.hasExtra("keyHomeFragmentData")) {
            val homeSellerModel = intent.getParcelableExtra<HomeSellerModel>("keyHomeFragmentData")!!
            username = homeSellerModel.name
            list_id = homeSellerModel.list_id
        } else {
            val data = intent.getParcelableExtra<CustomerChildModel>("keymodel")!!
            username = data.name
            list_id = data.id
        }


        val seller_active_status =
            Constant.getPrefs(this).getString(Constant.seller_active_status, "")

        if (seller_active_status == "1") {
            //    mBinding.switchView.visibility = View.VISIBLE
            mbinding.activeHome.background = resources.getDrawable(R.color.green)
            mbinding.activeHome.text = resources.getText(R.string.active)

        } else {
            // mBinding.switchView.visibility = View.VISIBLE
            mbinding.activeHome.background = resources.getDrawable(R.color.red)
            mbinding.activeHome.text = resources.getText(R.string.inactive)
        }
        setAdapter()
        SettingUpToolbar()
        //hit api of get buyer list
        mvmodel.setBuyerlist(list_id)

        //direct send the data
//*********************observver of buyer list
        mvmodel.mSucessfulGetbuyerList().observe(this, Observer {
            globalData = it
            mbinding.progressPagination.visibility = View.GONE
            if (it == null) {
                mbinding.txtNoData.visibility = View.VISIBLE
                mbinding.ParentLayout.visibility = View.INVISIBLE
            } else {
                mbinding.txtNoData.visibility = View.INVISIBLE
                mbinding.ParentLayout.visibility = View.VISIBLE
                it.name = username
                customerParentAdapter.update(it)
                mbinding.recyclerParent.visibility = View.VISIBLE
            }
            if (it.quote_requested == "1") {
                mbinding.btnnext1.visibility = View.GONE
            } else {
                mbinding.btnnext1.visibility = View.VISIBLE
            }
        })

        //failure case
        mvmodel.mFailureGetbuyerList().observe(this, Observer {
           // Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
            Constant.showToast(   it.message,this)
            mbinding.txtNoData.visibility = View.VISIBLE
            mbinding.ParentLayout.visibility = View.INVISIBLE
            mbinding.progressPagination.visibility = View.GONE
        })


        //save the data on the click
        mbinding.btnnext1.setOnClickListener {
            val data = customerParentAdapter.model
            val shoppinglIst = customerParentAdapter.model1

            val gson = Gson()
            val json = Constant.getPrefs(this).getString(Constant.PROFILE, "")
            val shredpref = gson.fromJson(json, GetProfileModel::class.java)

            var priceOfAtleastOneProduct = 0
            var imageOfAtleastOneProduct = 0
            var getProduct1 = 0
            var priceshowProduct = 0
            for (i in 0 until shoppinglIst.size) {
                if (shoppinglIst[i].price.trim().isNotEmpty() && shoppinglIst[i].image.trim()
                        .isNotEmpty()
                ) {
                    getProduct1 = 1
                }
            }
            for (i in 0 until shoppinglIst.size) {
                if (shoppinglIst[i].price.trim().isNotEmpty() && shoppinglIst[i].image.trim()
                        .isNotEmpty()
                ) {
                    getProduct1 = 1
                }
                if (shoppinglIst[i].price.trim().isNotEmpty() && shoppinglIst[i].image.isEmpty()) {
                    imageOfAtleastOneProduct = 1
                }
                if (shoppinglIst[i].price.trim().isEmpty() && shoppinglIst[i].image.isNotEmpty()) {
                    priceshowProduct = 1
                }
                if (getProduct1 == 0) {
                    if (shoppinglIst[i].price.trim().isEmpty() && shoppinglIst[i].image.trim()
                            .isEmpty()
                    ) {
                        priceOfAtleastOneProduct = 1
                    }
                }


            }

            if (priceOfAtleastOneProduct == 1) {
//                Toast.makeText(
//                    this,
//                    getString(R.string.Please_enter_price_and_image_of_atleast_1_product),
//                    Toast.LENGTH_SHORT
//                ).show()
                Constant.showToast(
                    getString(R.string.Please_enter_price_and_image_of_atleast_1_product),
                    this
                )
            } else if (imageOfAtleastOneProduct == 1) {
//                Toast.makeText(
//                    this,
//                    getString(R.string.Please_enter_the_image),
//                    Toast.LENGTH_SHORT
//                ).show()
                Constant.showToast(    getString(R.string.Please_enter_the_image),this)
            } else if (priceshowProduct == 1) {
//                Toast.makeText(
//                    this,
//                    getString(R.string.Please_Enter_The_Price),
//                    Toast.LENGTH_SHORT
//                ).show()
                Constant.showToast(    getString(R.string.Please_Enter_The_Price),this)
            } else if (getProfileModel.enable_seller_commission == "1" && data.comission.toInt() < shredpref.seller_minimum_commission.toInt()) {
                Toast.makeText(
                    this,
                    getString(R.string.Please_Select_Comission_toast2) + " " + shredpref.seller_minimum_commission + "%",
                    Toast.LENGTH_SHORT
                ).show()

            } else if (data.payment_methods.isEmpty()) {
//                Toast.makeText(
//                    this,
//                    getString(R.string.please_select_payment_method),
//                    Toast.LENGTH_SHORT
//                ).show()
                Constant.showToast(    getString(R.string.please_select_payment_method),this)
            }

//            else if (data.comission.isEmpty()) {
//                Toast.makeText(
//                    this,
//                    getString(R.string.Please_select_the_commission),
//                    Toast.LENGTH_SHORT
//                ).show()
//            }
//            else if (data.comission.toInt()<shredpref.seller_minimum_commission.toInt()) {
//                Toast.makeText(
//                    this,
//                    getString(R.string.Please_Select_Comission_toast2)+" "+shredpref.seller_minimum_commission+"%",
//                    Toast.LENGTH_SHORT
//                ).show()
//            }
            else {
                addProductCustomDialog()
            }
        }
        //******************observer of requsted api****************//
        //sucessful case
        mvmodel.mSucessfulRequestBuyerList().observe(this, Observer {
            // Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
            mbinding.progressPagination.visibility = View.GONE
            dialog.dismiss()
            finish()
        })
        //failure
        mvmodel.mFailureRequestBuyerList().observe(this, Observer {

          //  Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
            Constant.showToast( it,this)
            mbinding.progressPagination.visibility = View.GONE

        })

        //**************upload image observer******************//
        //sucessful
        mvmodel.mSucessfullUploadImage().observe(this, Observer {
            childData.progressBar = false
            customerParentAdapter.UpdateInProgress(childData, childPosition)
            isUploadedImage = false
            customerParentAdapter.UpdateImageFailProgress(isUploadedImage)
            customerParentAdapter.UpdateImage(it, childPosition)
            // imageProgress.visibility = View.GONE

        })
        //failure
        mvmodel.mFailureeUploadImage().observe(this, Observer {
          //  Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
            Constant.showToast( it,this)
            //  imageProgress.visibility = View.GONE
            childData.progressBar = false
            isUploadedImage = false
            customerParentAdapter.UpdateImageFailProgress(isUploadedImage)
            customerParentAdapter.UpdateInProgress(childData, childPosition)
        })
        //*************************report list reponse
        //sucessfull
        mvmodel.mSucessfulREportList().observe(this, Observer {
           // Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
            Constant.showToast( it,this)
            dialog.waitBtn.visibility = View.GONE
            dialog.SubmitBtn.visibility = View.VISIBLE
            dialog.dismiss()
        })
        //failure
        mvmodel.mFailureReposrtlist().observe(this, Observer {
            //Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
            Constant.showToast( it,this)
            dialog.waitBtn.visibility = View.GONE
            dialog.SubmitBtn.visibility = View.VISIBLE
            dialog.dismiss()
        })

    }

    //...............set up dialog for getting the product list................//
    private fun addProductCustomDialog() {
        dialog = Dialog(this)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setContentView(R.layout.requested_list_custom_dialog)
        dialog.show()

        //yes click
        //*****************set the api of requsted buying list*******************//
        dialog.yes.setOnClickListener {
            val data = customerParentAdapter.model
            Log.e("eroooooor", data.payment_methods)
            mvmodel.setRequestBuyerList(data, jsonFormatProductDetails())
            mbinding.progressPagination.visibility = View.VISIBLE
        }
        dialog.no.setOnClickListener {
            dialog.dismiss()
        }
    }

    //******************json format*****************//
    //  [{"id":1,"price":"10","image":"123"},{"id":2,"price":"10","image":"123"}]
    fun jsonFormatProductDetails(): String {
        val shoppinglIst = customerParentAdapter.model1
        val json = JSONArray()
        for (i in 0 until shoppinglIst.size) {
            val jsonObject = JSONObject()
            jsonObject.put("id", shoppinglIst[i].id)
            jsonObject.put("price", shoppinglIst[i].price)
            jsonObject.put("priceWithComission", shoppinglIst[i].priceWithComission)
            jsonObject.put("image", shoppinglIst[i].image)
            json.put(jsonObject)
        }
        return json.toString()
    }

    fun setAdapter() {
        val manager = LinearLayoutManager(this)
        mbinding.recyclerParent.layoutManager = manager
        customerParentAdapter = CustomerRequestListParentAdapter(this)
        mbinding.recyclerParent.adapter = customerParentAdapter
    }

    //.........setuptool bar..............//
    @SuppressLint("SetTextI18n")
    private fun SettingUpToolbar() {

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        title = ""
        setSupportActionBar(toolbar)
        //set the text
        text = findViewById(R.id.toolbartittle)
        //     text.text = username + getString(R.string.s) + " " + getText(R.string.Requested_List)
        text.text = username
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_back_icon)

    }

    //..........back button click...........//
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item != null) {
            if (item.itemId == android.R.id.home) {
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    //************************upload image of products***********//
    override fun upDateImage(
        position: Int,
        mData: PostShoppingModel
    ) {
        childPosition = position
        childData = mData
        if (Constant.checkPermissionForCameraGallery(this))
            showDialog()
    }

    override fun removeImage(position: Int, mData: PostShoppingModel) {
        childPosition = position
        childData = mData
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
        //val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        //val imageFileName = "JPEG_" + timeStamp + "_"
        val imageFileName = "Caiguru_Photo_"
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

    @Throws(IOException::class)
//    fun ResizeImages(sPath: String?, sTo: String?) {
//        var photo: Bitmap = BitmapFactory.decodeFile(sPath)
//        photo = Bitmap.createScaledBitmap(photo, 300, 300, false)
//        val bytes = ByteArrayOutputStream()
//        photo.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
//        val f = File(sTo)
//        f.createNewFile()
//        val fo = FileOutputStream(f)
//        fo.write(bytes.toByteArray())
//        fo.close()
//        val file = File(sPath)
//        file.delete()
//    }

    private fun openGallery() {
        val intent = Intent(
            Intent.ACTION_PICK,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        )
        intent.setType("image/*")
        startActivityForResult(intent, 1)
    }

    //url link get to used this
//    private fun getImageUri(inImage: Bitmap): File {
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
//            inImage.compress(Bitmap.CompressFormat.JPEG, 100, out)
//            out.flush()
//            out.close()
//        } catch (e: Exception) {
//            e.printStackTrace()
//        }
//        return file
//    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        //......................getting the image of dialog...........//

        try {
            if (requestCode == 29 && resultCode == Activity.RESULT_OK) {
                try {
                    dialog = Dialog(this)
//..................................Api FOr Update Image.................................................................//
                    if (photoFile == null) {
                        isUploadedImage = false
                        customerParentAdapter.UpdateImageFailProgress(isUploadedImage)
                    } else {
                        isUploadedImage = true
                        customerParentAdapter.UpdateImageFailProgress(isUploadedImage)
                        //image rotation
                        val resultBitmap = getRotateImage(photoFile)
                        val uri = getImageUri(resultBitmap)
                        mvmodel.uploadProfileImage(Constant.saveBitmapToFile(uri))//send the data for the api
                        childData.progressBar = true
                        customerParentAdapter.UpdateInProgress(childData, childPosition)

                    }

                    //  dialog.progressBar.visibility = View.VISIBLE
                    //  dialog.img.visibility = View.INVISIBLE

                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
            //2nd code image req code
            else if (requestCode == 1 && data != null) {
                try {
//                    val bitmap2: Uri = data.data
//
//                    val file: File = Constant.uploadImage(bitmap2, this)
                    val selectedPicture: Uri = data.getData()!!

                    val file: File =
                        Constant.uploadImage2(Constant.getBitmapImage(selectedPicture, this), this)

                    //..................................Api FOr Update Image from gallery....................................................................//
                    if (file == null) {
                        isUploadedImage = false
                        customerParentAdapter.UpdateImageFailProgress(isUploadedImage)
                    } else {
                        isUploadedImage = true
                        customerParentAdapter.UpdateImageFailProgress(isUploadedImage)
                        mvmodel.uploadProfileImage(file)
                        //  imageProgress.visibility = View.VISIBLE
                        childData.progressBar = true
                        customerParentAdapter.UpdateInProgress(childData, childPosition)
                    }

                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun getImageUri(inImage: Bitmap): File {
        val root: String = Environment.getExternalStorageDirectory().toString()
        val myDir = File("$root/req_images")
        myDir.mkdirs()
        val fname = "Image_profile.jpg"
        val file = File(myDir, fname)
        if (file.exists())
            file.delete()

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

    private fun getRotateImage(photoFile: File?): Bitmap {
        val ei: ExifInterface = ExifInterface(photoFile!!.path)
        val orientation = ei.getAttributeInt(
            ExifInterface.TAG_ORIENTATION,
            ExifInterface.ORIENTATION_UNDEFINED
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

        }

        return rotatedBitmap!!
    }

    //*************set the click on report list
    override fun reportList(data: CustomerChildModel) {
        dialog = Dialog(this)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setContentView(R.layout.refute_reason_dialog)
        dialog.edtReason.hint = getString(R.string.enter_reason)
        dialog.show()
        dialog.edtReason.filters = arrayOf<InputFilter>(
            HideEmoji(this)
        )

        dialog.SubmitBtn.setOnClickListener {
            if (dialog.edtReason.text.isEmpty()) {
//                Toast.makeText(
//                    this,
//                    getString(R.string.enter_reason),
//                    Toast.LENGTH_SHORT
//                )
//                    .show()
                Constant.showToast( getString(R.string.enter_reason),this)
            } else {
                mvmodel.reportList(dialog.edtReason.text.toString().trim(), data)
                dialog.waitBtn.visibility = View.VISIBLE
                dialog.SubmitBtn.visibility = View.GONE
            }

        }
    }

}

