package com.example.caiguru.seller.shoppingListSellers.sellerPostShoppingList

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.text.InputFilter
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import androidx.core.content.FileProvider
import androidx.databinding.DataBindingUtil
import androidx.exifinterface.media.ExifInterface
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.TransformationUtils
import com.example.caiguru.R
import com.example.caiguru.commonScreens.hideEmoji.HideEmoji
import com.example.caiguru.databinding.OpenFromCameraPermissionBinding
import com.example.caiguru.seller.sellerChooseCategory.SellerCategoryModel
import com.example.caiguru.seller.sellerChooseCategory.SellerChooseCategoryActivity
import com.example.caiguru.seller.shoppingListSellers.deliveryZoneSeller.DeliverZoneAdapter
import com.example.caiguru.seller.shoppingListSellers.deliveryZoneSeller.DeliveryZoneModel
import com.example.caiguru.seller.shoppingListSellers.sellerPostShoppingList.savedProductSeller.SavedProductActivity
import com.example.caiguru.seller.shoppingListSellers.sellerPostShoppingList.dragDropProduct.DragDropProductActivity
import constant_Webservices.Constant
import kotlinx.android.synthetic.main.activity_shopping_list_part1.*
import kotlinx.android.synthetic.main.activity_shopping_list_part1.btnfinish
import kotlinx.android.synthetic.main.activity_shopping_list_part1.seekbar
import kotlinx.android.synthetic.main.activity_shopping_list_part1.txtcomission
import kotlinx.android.synthetic.main.activity_shopping_list_part1.txtname
import kotlinx.android.synthetic.main.activity_shopping_list_part1.txtproductOfYourShopping
import kotlinx.android.synthetic.main.add_price_custom_dialog.view.*
import kotlinx.android.synthetic.main.seller_add_more_product_dialog.*
import kotlinx.android.synthetic.main.seller_add_more_product_dialog.img
import kotlinx.android.synthetic.main.seller_add_more_product_dialog.progressdialog
import kotlinx.android.synthetic.main.toolbar.view.*
import org.json.JSONArray
import org.json.JSONObject
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class ShoppingListPart1Activity : AppCompatActivity(), PostShoppingAdapter.editDataInterface {
    private var seller_maximum_commission: String = ""
    private var seller_minimum_commission: Int = 0
    private var sellerLevel: String = ""
    private var creditPerProduct: String = ""
    var freeProducts = 0
    //private var creditToDeduct: String = ""
    private var sellerCredits: String = ""
    private var photoFile: File? = null
    var check: Int = -1
    //  private lateinit var dialogs: Dialog

    private var categoryModelList = SellerCategoryModel()
    private var productShoppingList = java.util.ArrayList<PostShoppingModel>()///updated data

    //............................//
    private var adapterShoppingPosition: Int = -1
    private var adapterEditShoppingModel: PostShoppingModel = PostShoppingModel()
    private lateinit var dialog: Dialog
    lateinit var adapter: PostShoppingAdapter
    lateinit var mAdapter: DeliverZoneAdapter
    private val chooseCategoryRequestCode: Int = 11
    private lateinit var mvmodel: PostShoppingViewModel
    private lateinit var text: TextView
    var model = PostShoppingModel()
    var modelDialog = DialogModel()
    var deliveryZoneModel = DeliveryZoneModel()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shopping_list_part1)
        mvmodel = ViewModelProvider(this).get(PostShoppingViewModel::class.java)
        settingUpToolbar()
        setAdapterShoppingList()
        //  activeInActiveCheck()
        firstimeInitvariables()
        setAllObserver()
        setAllClicks()
        seekBarClick()
        creditDeductionLogic()
        dialog = Dialog(this)
    }

    //.........setuptool bar..............//
    private fun settingUpToolbar() {
        val toolbar = findViewById<Toolbar>(R.id.shoppingListToolBar)
        title = ""
        setSupportActionBar(toolbar)
        //set the text
        text = findViewById(R.id.toolbartittle)
        text.text = getText(R.string.Post_Shopping_List)
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

//    private fun activeInActiveCheck() {
//        val seller_active_status =
//            Constant.getPrefs(this).getString(Constant.seller_active_status, "")
//
//        if (seller_active_status == "1") {
//            activeHome.background = resources.getDrawable(R.color.green)
//            activeHome.text = resources.getText(R.string.active)
//        } else {
//            activeHome.background = resources.getDrawable(R.color.red)
//            activeHome.text = resources.getText(R.string.inactive)
//        }
//    }

    //****************init variable first time
    private fun firstimeInitvariables() {
        if (Constant.getProfileData(this).listcount != "0") {
            shoppingProgresss.background = resources.getDrawable(R.color.yellow)
            shoppingProgresss.text = getString(R.string.completed_screen_text2)
        } else {
            shoppingProgresss.background =
                resources.getDrawable(R.mipmap.twenty_percent_progressbar)
            shoppingProgresss.text = getString(R.string._40_completed_for_registering)
        }
        //show toolbar variables    // show the icon of dollar
        shoppingListToolBar.checkTotalCredits.visibility = View.VISIBLE
        seller_maximum_commission = Constant.getProfileData(this).seller_maximum_commission
        seller_minimum_commission = Constant.getProfileData(this).seller_minimum_commission.toInt()
        sellerCredits = Constant.getProfileData(this).credits
        sellerLevel = Constant.getProfileData(this).seller_level
        creditPerProduct = Constant.getProfileData(this).per_product_credits

//*************hide the sekbar check or comision visible or invisible check
        //1 for visible else hide
        if (Constant.getProfileData(this).enable_seller_commission == "1") {
            txtcomission.visibility = View.VISIBLE
            seekbar.visibility = View.VISIBLE
            seekbar.setProgress(seller_minimum_commission)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                seekbar.min = seller_minimum_commission
            }
        } else {
            txtcomission.visibility = View.GONE
            seekbar.visibility = View.GONE
        }
        //***************************set the seek bar by default***************//

//*****************hide the emoji*********************//
        txtname.filters = arrayOf<InputFilter>(HideEmoji(this), InputFilter.LengthFilter(50))
        //by default set the seekbar
        txtcomission.text =
            "${getString(R.string.Your_Comission)} ${seller_minimum_commission}${"%"}"

//************hide keyboard****************//
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)


    }

    //********observer
    private fun setAllObserver() {

        //...........set  the observer of add shopping list..........//
        mvmodel.sendData().observe(this, Observer {
            productShoppingList = it
            shoppingListData(it)
        })
        //.............observer of update image in the dialog box..................//
        //data sucessful
        mvmodel.getdataDialogSucessful().observe(this, Observer {
            adapterEditShoppingModel.image = it.image
            modelDialog.image = it.image//set image globally
            setProfilePic(it.image)
            dialog.progressdialog.visibility = View.INVISIBLE
            dialog.img.visibility = View.VISIBLE
        })
        //data failure
        mvmodel.geterrorDialog().observe(this, Observer {
            Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
            dialog.progressdialog.visibility = View.INVISIBLE
            dialog.img.visibility = View.VISIBLE
        })


    }

    private fun setAllClicks() {
        //toolBar Click
        shoppingListToolBar.checkTotalCredits.setOnClickListener {
            Constant.customDialogShowTotalCredits(this)
        }
        //................set the click on the category name...........................//
        txtcategorys.setOnClickListener {
            if (txtcategorys.text.isEmpty()) {
                val intent = Intent(this, SellerChooseCategoryActivity::class.java)
                startActivityForResult(intent, chooseCategoryRequestCode)
            } else {
                val intent = Intent(this, SellerChooseCategoryActivity::class.java)
                intent.putExtra("keyPrefillCategory", txtcategorys.text.toString().trim())
                startActivityForResult(intent, chooseCategoryRequestCode)
            }
        }
//set the click on the saved products
        txtSavedProducts.setOnClickListener {
//            if (categoryModelList.name.isEmpty()) {
//                Constant.showToast(getString(R.string.Please_Select_Category), this)
//                return@setOnClickListener
//            } else {
                val intent = Intent(this, SavedProductActivity::class.java)
                intent.putExtra("KeyCategory", categoryModelList)
                intent.putParcelableArrayListExtra("KeyProductArray", adapter.getAllShoppingArrayData())
                startActivityForResult(intent, 500)
         //   }

        }
        //..........set the click on the add product in list...................//
        txtNewProducts.setOnClickListener {
//set the popup when the user add the first product
            if (adapter.getAllDataArraySize() <= 0) {
                showPopUpProductAdd()
            } else {
                if (Constant.getProfileData(this).enable_seller_commission == "1") {

                    if (seekbar.progress < seller_minimum_commission) {
                        Toast.makeText(
                            this,
                            getString(R.string.Please_Select_Comission_toast2) + " " + seller_minimum_commission + "%",
                            Toast.LENGTH_SHORT
                        ).show()
                        return@setOnClickListener
                    } else {
                        txtname.setFocusable(false)
                        addProductCustomDialog("add")//first time dialog open
                    }
                } else {
                    txtname.setFocusable(false)
                    addProductCustomDialog("add")//first time dialog open
                }
            }

        }

        //******************set the click after 1st screen complete and user go to the 2nd part of the screen
        btnfinish.setOnClickListener {
            if (txtname.text.isEmpty()) {
                Toast.makeText(
                    this,
                    getString(R.string.Please_Enter_Name_Of_The_List),
                    Toast.LENGTH_SHORT
                ).show()
            } else if (txtcategorys.text.isEmpty()) {
                Toast.makeText(
                    this,
                    getString(R.string.Please_Select_Category),
                    Toast.LENGTH_SHORT
                ).show()

            } else if (productShoppingList.size == 0) {
                Toast.makeText(
                    this,
                    getString(R.string.Please_Add_Your_Product),
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }
            //seekbar check
            else if (productShoppingList.size <= 1) {
                Constant.firstTimeCaiguruDialog(this)
                return@setOnClickListener
            }
            //seekbar check
            else if (Constant.getProfileData(this).enable_seller_commission == "1" && seekbar.progress < seller_minimum_commission) {
                Toast.makeText(
                    this,
                    getString(R.string.Please_Select_Comission_toast2) + " " + seller_minimum_commission + "%",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            } else if (productShoppingList.size > 0 && productShoppingList.size > freeProducts) {
                AlertUserAddProductAfterFive()
            } else {
                var commission_per = 0
                if (Constant.getProfileData(this).enable_seller_commission == "1") {
                    commission_per = seekbar.progress
                }
                val intent = Intent(this, SellerPostShoppingListActivity::class.java)
                intent.putExtra("keyListName", txtname.text.toString().trim())
                intent.putExtra("keyComissionPer", commission_per.toString())
                intent.putExtra("keyCategoryId", categoryModelList.category_id)
                intent.putExtra("keyProductJsonArray", shoppingProductListJSonArray())
                startActivityForResult(intent, 1000)
            }


        }
    }


    //...........json format shopping product list..........//
    private fun shoppingProductListJSonArray(): String {

        val jsonarray = JSONArray()
        for (i in 0 until adapter.getAllShoppingArrayData().size) {
            val jsonobject = JSONObject()
            jsonobject.put("name", adapter.getAllShoppingArrayData()[i].name)
            jsonobject.put("image", adapter.getAllShoppingArrayData()[i].image)
            jsonobject.put("unit", adapter.getAllShoppingArrayData()[i].unit)
            jsonobject.put("price", adapter.getAllShoppingArrayData()[i].price)
            jsonobject.put("priceWithComission",adapter.getAllShoppingArrayData()[i].priceWithComission)
            jsonobject.put("saved_product_id", adapter.getAllShoppingArrayData()[i].saved_product_id)
            jsonobject.put("status", adapter.getAllShoppingArrayData()[i].status)
            jsonarray.put(jsonobject)
        }
        Log.d("shoppingListJSonArray= ", jsonarray.toString())
        return jsonarray.toString()
    }


    //..................set seek bar click..................//
    private fun seekBarClick() {
        // Set a SeekBar change listener
        seekbar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, i: Int, b: Boolean) {
                // Display the current progress of SeekBar
                txtcomission.text = getString(R.string.Your_Comission) + " " + i + "%"
                seekBar.max = seller_maximum_commission.toInt()
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    seekBar.min = seller_minimum_commission
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {
                adapter.update(productShoppingList, seekBar.progress)
                if (seekBar.progress < seller_minimum_commission) {
                    seekBarAlertToast()
                }
            }
        })
    }

    private fun seekBarAlertToast() {
        Toast.makeText(
            this,
            getString(R.string.Please_Select_Comission_toast2) + " " + seller_minimum_commission + "%",
            Toast.LENGTH_SHORT
        ).show()
    }

    //......................... adapter interface data of shopping list send to dialog dialog..............//
    override fun edtData(data: PostShoppingModel, position: Int) {
        adapterEditShoppingModel = data
        adapterShoppingPosition = position
        adapterEditShoppingModel.image = data.image
        txtproductOfYourShopping.visibility = View.INVISIBLE
        // txtaddmores.text = getString(R.string.Add_product)

        txtname.setFocusable(false)
        addProductCustomDialog("edit")
    }

    //..........adapter interface for deelting the data in the particular position
    override fun deleteShoppingList(list: ArrayList<PostShoppingModel>, position: Int) {
        mvmodel.deleteProduct(position, list)
        if (list.size > 0) {
            list.removeAt(position)
        }
        shoppingListData(list)
    }

    //................shopping adapter  data function
    private fun shoppingListData(it: ArrayList<PostShoppingModel>) {
        adapter.update(it, seekbar.progress)
        if (it.size > 0) {
            txtproductOfYourShopping.visibility = View.VISIBLE
            //txtaddmores.text = getString(R.string.Add_more_product)
        } else {
            txtproductOfYourShopping.visibility = View.INVISIBLE
            //   txtaddmores.text = getString(R.string.Add_product)
        }
    }


    //.....................shopping list adapter...........//
    private fun setAdapterShoppingList() {
        val manager = LinearLayoutManager(this)
        adapter = PostShoppingAdapter(this)
        recyclersShopping.layoutManager = manager
        recyclersShopping.adapter = adapter
        //set the drag and drop
//        val callback: ItemTouchHelper.Callback = ItemMoveCallback(adapter)
//        val touchHelper = ItemTouchHelper(callback)
//        touchHelper.attachToRecyclerView(recyclersShopping)
    }

    override fun dragDropListClick(list: ArrayList<PostShoppingModel>) {
        val intent = Intent(this, DragDropProductActivity::class.java)
        intent.putParcelableArrayListExtra("KeyDragDropData", list)
        startActivityForResult(intent, 10001)

    }

    //*********************************** ON ACTIVITY RESULT **********************************//
    //......getting the result of name category...........//
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 1000 && resultCode == Activity.RESULT_OK) {

            finish()
        }
// 1 code getting the category data
        try {
            if (requestCode == chooseCategoryRequestCode && resultCode == RESULT_OK) {
                val datasmodel = data!!.getParcelableExtra<SellerCategoryModel>("keyname")!!
                categoryModelList = datasmodel
                txtcategorys.text = datasmodel.name
            }
            //......................getting the image of dialog...........//
            else if (requestCode == 22 && resultCode == Activity.RESULT_OK) {
                //   try {

                val resultBitmap = getRotateImage(photoFile)
                val uri = getImageUri(resultBitmap)

//..................................Api FOr Update Image....................................................................//
                mvmodel.profileImage(Constant.saveBitmapToFile(uri)!!)//send the data for the api
                // mvmodel.profileImage(uri)//send the data for the api
                dialog.progressdialog.visibility = View.VISIBLE
                dialog.img.visibility = View.INVISIBLE

            }
//2nd code
            else if (requestCode == 1 && data != null) {
                try {
                    val selectedPicture: Uri = data.getData()!!

                    val file: File =
                        Constant.uploadImage2(
                            Constant.getBitmapImage(selectedPicture, this),
                            this
                        )
                    dialog.progressdialog.visibility = View.VISIBLE
                    dialog.img.visibility = View.INVISIBLE
                    mvmodel.profileImage(file)//send the data for the api

                } catch (e: Exception) {
                    e.printStackTrace()
                    Toast.makeText(this, getString(R.string.Failed), Toast.LENGTH_SHORT).show()
                }
            } else if (requestCode == 500 && resultCode == Activity.RESULT_OK) {

                val productSavedArray =
                    data!!.getParcelableArrayListExtra<PostShoppingModel>("keySavedProductData")!!


                mvmodel.ShoppingListData(productSavedArray, false, "")
            } else if (requestCode == 10001 && resultCode == Activity.RESULT_OK) {

                val productSavedArray =
                    data!!.getParcelableArrayListExtra<PostShoppingModel>("KeyDragDropData")!!
                mvmodel.ShoppingListData(productSavedArray, false,"clearArray")
            }
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
            Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
        }
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


    //...............set up dialog for getting the product list................//
    private fun addProductCustomDialog(from: String) {
        dialog = Dialog(this)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setContentView(R.layout.seller_add_more_product_dialog)
        dialog.show()
        //hide the emoji
        dialog.edtname.filters = arrayOf(HideEmoji(this), InputFilter.LengthFilter(50))
        val Units = arrayOf("Select Unit", "unit", "oz", "lbs")
        val spinnerAdapters = SpinnerShoppingListAdapter(this, Units)
        dialog.edtunit2!!.adapter = spinnerAdapters
        dialog.setOnCancelListener {
            txtname.setFocusableInTouchMode(true)
            txtname.setFocusable(true)
        }


       // edit case model
        if (adapterEditShoppingModel.name.length > 0) {
            val model = DialogModel()
            model.name = adapterEditShoppingModel.name
            model.unit = adapterEditShoppingModel.unit
            model.price = adapterEditShoppingModel.price
            model.image = adapterEditShoppingModel.image
            model.priceWithComission = adapterEditShoppingModel.priceWithComission//editext case field
            dialog.edtname.setText(model.name)

            if (model.unit == "unit") {
                dialog.edtunit2.setSelection(1)
            } else if (model.unit == "oz") {
                dialog.edtunit2.setSelection(2)
            } else if (model.unit == "lbs") {
                dialog.edtunit2.setSelection(3)
            }

            dialog.edtprice.setText(model.price)
            if (model.image.isNotEmpty()) {
                Glide.with(this)
                    .load(model.image)
                    .into(dialog.img)
            } else {
                Glide.with(this)
                    .load(R.drawable.ic_product_img)
                    .into(dialog.img)
            }
            dialog.edtPriceWithComission.setText(Constant.roundValue(model.priceWithComission.toDouble()))
        }
        else {
//all field is empty
            dialog.edtname.setText("")
            dialog.edtunit2.setSelection(0)
            dialog.edtprice.setText("")
            dialog.edtPriceWithComission.setText("")//new field
            Glide.with(this)
                .load(R.drawable.ic_product_img)
                .into(dialog.img)
        }

//****************************set the price by the dialog****************//
        dialog.edtprice.setOnClickListener {
            addPriceCustomDialog(
                "Price",
                dialog.edtprice.text.toString().trim(),
                Constant.getProfileData(this).plateform_commission
            )
        }
        //*****************Price With comission*****************//
        dialog.edtPriceWithComission.setOnClickListener {
            addPriceCustomDialog(
                "PriceWithComission",
                dialog.edtPriceWithComission.text.toString().trim(),
                Constant.getProfileData(this).plateform_commission
            )
        }


        //..........set the click on the image............//

        dialog.img.setOnClickListener {
            //set the user alert first time when the user came
            if (adapter.getAllDataArraySize() <= 0) {
                showInformationPopup()
            } else {
                if (Constant.checkPermissionForCameraGallery(this))
                    showDialog()
            }
        }

        //set the click button of custom dialog
        dialog.btnAdd.setOnClickListener {
            txtname.setFocusableInTouchMode(true)
            txtname.setFocusable(true)
            val id = dialog.edtunit2.selectedItemId.toInt()

            //*****************edit  button model click  check the data is  available or not
            if (adapterEditShoppingModel.name.length > 0) {
                adapterEditShoppingModel.name = dialog.edtname.text.toString()
                adapterEditShoppingModel.unit = setUnit(dialog.edtunit2.selectedItem.toString())
                adapterEditShoppingModel.price = dialog.edtprice.text.toString()
                adapterEditShoppingModel.priceWithComission = dialog.edtPriceWithComission.text.toString()
                //adapterShoppingModel!!.image = modelDialog.image

                if (adapterEditShoppingModel.name.isEmpty()) {
                    Toast.makeText(
                        this,
                        getString(R.string.Please_Enter_The_Name),
                        Toast.LENGTH_SHORT
                    )
                        .show()
                    return@setOnClickListener

                } else if (adapterEditShoppingModel.name.length > 50) {
                    Toast.makeText(
                        this,
                        getString(R.string.Please_add_less_than_thirty_char),
                        Toast.LENGTH_SHORT
                    )
                        .show()
                    return@setOnClickListener
                } else if (adapterEditShoppingModel.unit == "Select Unit") {
                    Toast.makeText(
                        this,
                        getString(R.string.Please_Enter_The_Unit_Of_Measurement),
                        Toast.LENGTH_SHORT
                    )
                        .show()
                    return@setOnClickListener
                } else if (adapterEditShoppingModel.price.isEmpty()) {
                    Toast.makeText(
                        this,
                        getString(R.string.Please_Enter_The_Price),
                        Toast.LENGTH_SHORT
                    )
                        .show()
                    return@setOnClickListener
                } else if (adapterEditShoppingModel.image.isEmpty()) {
                    Toast.makeText(
                        this,
                        getString(R.string.Please_select_The_Image),
                        Toast.LENGTH_SHORT
                    )
                        .show()
                    return@setOnClickListener
                } else if (adapterEditShoppingModel.priceWithComission.isEmpty()) {
                    Toast.makeText(
                        this,
                        getString(R.string.Please_enter_the_price_comission),
                        Toast.LENGTH_SHORT
                    )
                        .show()
                    return@setOnClickListener
                } else {
                    val old = adapterEditShoppingModel.name
                    val new = dialog.edtname.text.toString()
                    //name check
                    if (old == new) {
                        adapterEditShoppingModel.name = old
                        adapterEditShoppingModel.unit =
                            setUnit(dialog.edtunit2.selectedItem.toString())
                        adapterEditShoppingModel.price = dialog.edtprice.text.toString()
                        // adapterShoppingModel!!.image = modelDialog.image
                        mvmodel.editListUpdate(
                            adapterEditShoppingModel,
                            adapterShoppingPosition,
                            false,
                            ""
                        )
                        Constant.hideSoftKeyboard(it, this)
                        dialog.cancel()
                    } else {
                        adapterEditShoppingModel.name = new
                        adapterEditShoppingModel.unit =
                            setUnit(dialog.edtunit2.selectedItem.toString())
                        adapterEditShoppingModel.price = dialog.edtprice.text.toString()
                        // adapterShoppingModel!!.image = modelDialog.image
                        mvmodel.editListUpdate(
                            adapterEditShoppingModel,
                            adapterShoppingPosition,
                            true,
                            "nameEdited"
                        )
                        Constant.hideSoftKeyboard(it, this)
                        dialog.cancel()

                    }
                }

            }
            //**********************without edit data first time they will come************//
            else {

                val model = PostShoppingModel()
                if (dialog.edtname.text.isEmpty()) {
                    Toast.makeText(
                        this,
                        getString(R.string.Please_Enter_The_Name),
                        Toast.LENGTH_SHORT
                    )
                        .show()
                } else if (dialog.edtname.text.length > 50) {
                    Toast.makeText(
                        this,
                        getString(R.string.Please_add_less_than_thirty_char),
                        Toast.LENGTH_SHORT
                    )
                        .show()
                } else if (id == 0) {

                    Toast.makeText(
                        this,
                        getString(R.string.Please_Enter_The_Unit_Of_Measurement),
                        Toast.LENGTH_SHORT
                    )
                        .show()
                } else if (dialog.edtprice.text.isEmpty()) {

                    Toast.makeText(
                        this,
                        getString(R.string.Please_Enter_The_Price),
                        Toast.LENGTH_SHORT
                    )
                        .show()
                } else if (dialog.edtPriceWithComission.text.isEmpty()) {

                    Toast.makeText(
                        this,
                        getString(R.string.Please_enter_the_price_comission),
                        Toast.LENGTH_SHORT
                    )
                        .show()
                } else if (adapterEditShoppingModel.image.isEmpty()) {

                    Toast.makeText(
                        this,
                        getString(R.string.Please_select_The_Image),
                        Toast.LENGTH_SHORT
                    )
                        .show()
                } else {

                    model.name = dialog.edtname.text.toString()
                    model.unit = setUnit(dialog.edtunit2.selectedItem.toString())
                    model.price = dialog.edtprice.text.toString()
                    model.priceWithComission = dialog.edtPriceWithComission.text.toString()
                    model.image =adapterEditShoppingModel.image//global model we want to set the image
                    model.saved_product_id ="0"
                    model.status ="1"
                    val array = ArrayList<PostShoppingModel>()

                    //********************** that is the case of prefilling************//
                    if (from == "edit") {
                        val old = adapterEditShoppingModel.name
                        val new = dialog.edtname.text.toString()

                        if (old == new) {
                            adapterEditShoppingModel.name = old
                            adapterEditShoppingModel.unit =
                                setUnit(dialog.edtunit2.selectedItem.toString())
                            adapterEditShoppingModel.price = dialog.edtprice.text.toString()
                            adapterEditShoppingModel.priceWithComission =
                                dialog.edtPriceWithComission.text.toString()
                            // adapterShoppingModel!!.image = modelDialog.image
                            mvmodel.editListUpdate(
                                adapterEditShoppingModel,
                                adapterShoppingPosition,
                                false,
                                ""
                            )
                            Constant.hideSoftKeyboard(it, this)
                            dialog.cancel()
                        }
                        else {
                            adapterEditShoppingModel.name = new
                            adapterEditShoppingModel.unit =
                                setUnit(dialog.edtunit2.selectedItem.toString())
                            adapterEditShoppingModel.price = dialog.edtprice.text.toString()
                            adapterEditShoppingModel.priceWithComission =
                                dialog.edtPriceWithComission.text.toString()
                            // adapterShoppingModel!!.image = modelDialog.image
                            mvmodel.editListUpdate(
                                adapterEditShoppingModel,
                                adapterShoppingPosition,
                                true,
                                "nameEdited"
                            )
                            Constant.hideSoftKeyboard(it, this)
                            dialog.cancel()
                        }
                        return@setOnClickListener
                    }

                        array.add(model)
                        mvmodel.ShoppingListData(array, false, "")
                        Constant.hideSoftKeyboard(it, this)
                        dialog.cancel()

                    dialog.cancel()
                }
            }
        }
        dialog.setOnDismissListener {
            adapterEditShoppingModel = PostShoppingModel()
            adapterShoppingPosition = -1
        }
    }


    private fun setUnit(unit: String): String {
        var units = getString(R.string.Select_Unit)
        if (unit == "1") {
            units = "unit"
        } else if (unit == "2") {
            units = "oz"
        } else if (unit == "3") {
            units = "lbs"
        }
        return units

    }

    //....................................Update profile image by gallary and camera.................//
    private fun showDialog() {
        val builder: AlertDialog.Builder =
            AlertDialog.Builder(this)
        builder.setTitle(getString(R.string.Open_By))
        val mBinding: OpenFromCameraPermissionBinding =
            DataBindingUtil.inflate(
                LayoutInflater.from(this),
                R.layout.open_from_camera_permission,
                null,
                false
            )

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
        } catch (ex: Exception) {
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
            // intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            startActivityForResult(intent, 22)
        }
    }

    @SuppressLint("IntentReset")
    private fun openGallery() {
//        val intent = Intent(
//            Intent.ACTION_PICK,
//            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI
//        )
//        intent.setType("image/*")
//        startActivityForResult(intent, 1)


        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        startActivityForResult(intent, 1)
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

    //url link get to used this
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
            inImage.compress(Bitmap.CompressFormat.JPEG, 90, out)
            out.flush()
            out.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return file
    }

    //set the image in the dialog
    private fun setProfilePic(image: String) {
        adapterEditShoppingModel.image = image
        modelDialog.image = image
        Glide.with(this)
            .load(image)
            .into(dialog.img)
    }

    // add price custom dialog
    private fun addPriceCustomDialog(
        type: String,
        Price: String,
        plateformCommission: String
    ) {
        val view = LayoutInflater.from(this).inflate(R.layout.add_price_custom_dialog, null)
        val mBuilder = AlertDialog.Builder(this)
            .setView(view)
            .create()
        //prefilling
        if (type == "Price") {
            view.productheading.text = getString(R.string.Set_Price)
            if (Price.isEmpty()) {
                view.edtPrice.setText("")
            } else {
                view.edtPrice.setText(Price)
            }
        } else {
            view.productheading.text = getString(R.string.Price_header_text)
            view.edtPrice.hint = getString(R.string.Enter_price_With_comissionTExt2)
            if (Price.isEmpty()) {
                view.edtPrice.setText("")
            } else {
                view.edtPrice.setText(Price)
            }
        }
        mBuilder.show()
        //set the click on the button
        view.btnAddPrice.setOnClickListener {
            if (view.edtPrice.text.isEmpty()) {
                Toast.makeText(
                    this,
                    getString(R.string.Please_add_product_price),
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            } else if (view.edtPrice.text.toString().toDouble() <= 0) {
                Toast.makeText(
                    this,
                    getString(R.string.price_should_be_greater_than_zero),
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            } else {
                if (type == "Price") {
                    var price = view.edtPrice.text.toString().trim()
                    price = price.replace(",", ".")
                    val commission_per = seekbar.progress
                    val allComission = commission_per + plateformCommission.toDouble()
                    val comissiontotal = price.toDouble() / 100 * allComission
                    val alltotal = price.toDouble() + comissiontotal


                    dialog.edtprice.setText(Constant.roundValue(price.toDouble()))
                    dialog.edtPriceWithComission.setText(Constant.roundValue(alltotal))
                    mBuilder.dismiss()
                } else {

                    var price = view.edtPrice.text.toString().trim()
                    price = price.replace(",", ".")
                    val commission_per = seekbar.progress
                    val allComission = commission_per + plateformCommission.toDouble()
                    val priceA = price.toDouble() * 100
                    val PriceB = 100 + allComission
                    val comissiontotal = priceA / PriceB
                    dialog.edtPriceWithComission.setText(Constant.roundValue(price.toDouble()))
                    dialog.edtprice.setText(Constant.roundValue(comissiontotal))
                    mBuilder.dismiss()
                }
            }
        }

    }


    //*******************Add product popup
    private fun showPopUpProductAdd() {
        val mDialog = AlertDialog.Builder(this)
        mDialog.setTitle(getString(R.string.alert))
        mDialog.setMessage(
            getString(R.string.add_product_alert_text)
        )
        mDialog.setPositiveButton(
            getString(R.string.ok)
        ) { dialog, which ->
            if (Constant.getProfileData(this).enable_seller_commission == "1") {

                if (seekbar.progress < seller_minimum_commission) {
                    Toast.makeText(
                        this,
                        getString(R.string.Please_Select_Comission_toast2) + " " + seller_minimum_commission + "%",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    txtname.setFocusable(false)
                    addProductCustomDialog("add")//first time dialog open
                }
            } else {
                txtname.setFocusable(false)
                addProductCustomDialog("add")//first time dialog open
            }
            dialog.cancel()
        }
//        mDialog.setNegativeButton(
//            getString(R.string.no)
//        ) { dialog, which ->
//            dialog.cancel()
//
//        }
        mDialog.show()
    }

    @SuppressLint("SetTextI18n")
    private fun creditDeductionLogic() {
        when (sellerLevel) {
            "1" -> freeProducts = 5
            "2" -> freeProducts = 6
            "3" -> freeProducts = 8
            "4" -> freeProducts = 10
        }
        textMoreProductes.text =
                //${getString(R.string.credit_for_additional)}
            "${getString(R.string.You_need_to_pay)} $creditPerProduct ${getString(R.string.credit_for_additional)} $freeProducts ${
                getString(
                    R.string.Product
                )
            }"

    }

    //***************************************dialog AlertUserAddProductAfterFive
    fun AlertUserAddProductAfterFive(

    ) {
        val credits = Constant.getProfileData(this).credits.toDouble().toInt().toString()
        val mDialog = android.app.AlertDialog.Builder(this)
        mDialog.setTitle(getString(R.string.alert))
        mDialog.setMessage(
            "${getString(R.string.You_need_to_pay)} $creditPerProduct ${getString(R.string.credit_for_additional)} $freeProducts ${
                getString(
                    R.string.Product
                )
            }. ${getString(R.string.credits_in_your_account)}: ${credits} ${getString(R.string.cr)}"
        )
        mDialog.setPositiveButton(
            getString(R.string.yes)
        ) { dialog, which ->
            if (sellerCredits.toDouble().toInt() < mvmodel.showcreditToDeduct().toDouble()) {
                Log.e("credits", mvmodel.showcreditToDeduct())
                Toast.makeText(
                    this,
                    getString(R.string.You_have_no_credits),
                    Toast.LENGTH_LONG
                )
                    .show()
                dialog.dismiss()
            } else {
                var commission_per = 0
                if (Constant.getProfileData(this).enable_seller_commission == "1") {
                    commission_per = seekbar.progress
                }
                val intent = Intent(this, SellerPostShoppingListActivity::class.java)
                intent.putExtra("keyListName", txtname.text.toString().trim())
                intent.putExtra("keyComissionPer", commission_per.toString())
                intent.putExtra("keyCategoryId", categoryModelList.category_id)
                intent.putExtra("keyProductJsonArray", shoppingProductListJSonArray())
                startActivityForResult(intent, 1000)
            }

        }
        mDialog.setNegativeButton(
            getString(R.string.no)
        ) { dialog, which ->
            dialog.cancel()

        }
        mDialog.show()
    }

    private fun showInformationPopup() {
        val mDialog = AlertDialog.Builder(this)
        mDialog.setTitle(getString(R.string.alert))
        mDialog.setCancelable(false)
        mDialog.setMessage(
            getString(R.string.information_alert)
        )
        mDialog.setPositiveButton(
            getString(R.string.ok)
        ) { dialog, which ->
            if (Constant.checkPermissionForCameraGallery(this))
                showDialog()
            dialog.cancel()
        }

        mDialog.show()
    }
}
