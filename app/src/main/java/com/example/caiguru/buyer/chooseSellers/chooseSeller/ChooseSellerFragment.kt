package com.example.caiguru.buyer.chooseSellers.chooseSeller


import android.Manifest
import android.app.Activity
import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.caiguru.R
import com.example.caiguru.buyer.buyerAddAddress.BuyerAddressMapBoxActivity
import com.example.caiguru.buyer.chooseSellers.chooseSellerLoadMore.ChooseSellerLoadMoreActivity
import com.example.caiguru.buyer.homeBuyers.homeBuyer.HomeViewModel
import com.example.caiguru.commonScreens.loginScreen.LoginActivity
import com.example.caiguru.databinding.FragmentChooseSellerBinding
import com.example.caiguru.seller.homeSeller.GetProfileModel
import com.example.caiguru.seller.shoppingListSellers.deliveryZoneSeller.DeliveryZoneModel
import com.example.caiguru.seller.shoppingListSellers.listUploadedByCustomer.listUploadedByCustomers.CustomerParentModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.firebase.iid.FirebaseInstanceId
import com.google.gson.Gson
import constant_Webservices.Constant
import kotlinx.android.synthetic.main.buyer_add_address_dialog.*
import kotlinx.android.synthetic.main.fragment_choose_seller.*
import kotlinx.android.synthetic.main.nodata.*
import java.io.IOException

class ChooseSellerFragment : Fragment(), ChooseSellerParentAdapter.loadmore {

    private var addressModelActivityResult = DeliveryZoneModel()
    private var model = GetProfileModel()
    private var activityGetDataStatus: Int = 0
    private var checked: Boolean = true
    private var checked2: Boolean = false
    var searchText: String = ""
    private var checkedHome: Int = 0
    private var checkedSelf: Int = 0
    lateinit var viewModel: HomeViewModel
    private lateinit var mSellerChooseAdapter: ChooseSellerParentAdapter
    private lateinit var mSearchSellerAdapter: ChooseSellerSearchProductAdapter
    private lateinit var mvmodel: ChooseSellerViewModel
    private lateinit var dialog: Dialog
    var addressModel = DeliveryZoneModel()
    private lateinit var mbinding: FragmentChooseSellerBinding
    private var fusedLocationClient: FusedLocationProviderClient? = null
    private var lat: String = ""
    private var long: String = ""
    private var Addressdatas: String = ""
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mbinding =
            FragmentChooseSellerBinding.inflate(LayoutInflater.from(context), container, false)
        dialog = Dialog(activity!!)
        //mvmodel = ViewModelProviders.of(this)[ChooseSellerViewModel::class.java]
        // viewModel = ViewModelProviders.of(this)[HomeViewModel::class.java]
        mvmodel = ViewModelProvider(this).get(ChooseSellerViewModel::class.java)
        viewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(activity!!)
        return mbinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        model = GetProfileModel()
        val gson = Gson()
        val json = Constant.getPrefs(activity!!).getString(Constant.PROFILE, "")
        if (json!!.isNotEmpty()) {
            model = gson.fromJson(json, GetProfileModel::class.java)
        } else {
            mbinding.noDataChooseSeller.visibility = View.VISIBLE
            mbinding.searchRecycler.visibility = View.GONE
            mbinding.sellerRecycler.visibility = View.GONE
            mbinding.progressPagination.visibility = View.GONE

        }
        if (model.lat.isNotEmpty() && model.long.isNotEmpty()) {
            lat = model.lat
            long = model.long
            Addressdatas = model.full_address
            if (checked == true) {
                checked = false
                checkedHome = 1
                mbinding.progressPagination.visibility = View.VISIBLE
                mvmodel.setChooseSeller(
                    lat,
                    long,
                    checkedHome.toString(),
                    checkedSelf.toString()
                )
            } else {
                checked2 = true
                checkedSelf = 1
                mbinding.progressPagination.visibility = View.VISIBLE
                mvmodel.setChooseSeller(
                    lat,
                    long,
                    checkedHome.toString(),
                    checkedSelf.toString()
                )
            }
        }
        openLocation()
        setAdapter()
        setAdapterSearch()
        Constant.hideSoftKeyboard(view, context!!)
        LOgoutBannedUser()
        //radio group click
        checkBoxLayout.setOnCheckedChangeListener(object : RadioGroup.OnCheckedChangeListener {
            override fun onCheckedChanged(group: RadioGroup?, checkedId: Int) {
                try {
                    if (checkedId == R.id.chechbox1) {
                        checked2 = false
                        checked = false
                        checkedHome = 1
                        checkedSelf = 0
                        mbinding.progressPagination.visibility = View.VISIBLE
                        if (searchText.isEmpty()) {
                            mvmodel.setChooseSeller(
                                lat,
                                long,
                                checkedHome.toString(),
                                checkedSelf.toString()
                            )
                        } else {
                            mvmodel.searchProduct(
                                searchText,
                                lat,
                                long,
                                checkedHome.toString(),
                                checkedSelf.toString()
                            )
                        }
                    } else {
                        checked2 = false
                        checked = false
                        checkedSelf = 1
                        checkedHome = 0
                        mbinding.progressPagination.visibility = View.VISIBLE
                        if (searchText.isEmpty()) {
                            mvmodel.setChooseSeller(
                                lat,
                                long,
                                checkedHome.toString(),
                                checkedSelf.toString()
                            )
                        } else {
                            mvmodel.searchProduct(
                                searchText,
                                lat,
                                long,
                                checkedHome.toString(),
                                checkedSelf.toString()
                            )
                        }

                    }
                } catch (e: Exception) {
                    e.printStackTrace()

                }
            }

        })
        //*********************set the observer of choose seller********************//
        //sucessful
        mvmodel.mSucessfulChooseSellerData().observe(viewLifecycleOwner, Observer {
            mbinding.progressPagination.visibility = View.GONE
            mbinding.noDataChooseSeller.visibility = View.GONE
            if (it.isEmpty()) {
                mbinding.noDataChooseSeller.visibility = View.VISIBLE
                mbinding.searchRecycler.visibility = View.GONE
                mbinding.sellerRecycler.visibility = View.GONE
                mbinding.progressPagination.visibility = View.GONE
            } else {
                mSellerChooseAdapter.update(
                    it,
                    mbinding.search.text.toString(),
                    lat, long, Addressdatas
                )
                mbinding.progressPagination.visibility = View.GONE
                mbinding.noDataChooseSeller.visibility = View.GONE
                mbinding.sellerRecycler.visibility = View.VISIBLE
                mbinding.searchRecycler.visibility = View.GONE
            }
            dialog.dismiss()
        })

        //failure
        mvmodel.mFailureChosseSeller().observe(viewLifecycleOwner, Observer {
            mbinding.progressPagination.visibility = View.GONE
            NODatadescriptionText.text = it
            mbinding.noDataChooseSeller.visibility = View.VISIBLE
            mbinding.sellerRecycler.visibility = View.GONE
            mbinding.searchRecycler.visibility = View.GONE
            //  Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            Constant.showToast(it, activity!!)

//            val intent = Intent(context, NetworkErrorActivity::class.java)
//            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
//            startActivity(intent)
            dialog.dismiss()
        })

        //*********************set the observer of search product******************//
        //sucessful
        mvmodel.mSearchSucessfulData().observe(viewLifecycleOwner, Observer {
            mbinding.noDataChooseSeller.visibility = View.GONE
            mbinding.progressPagination.visibility = View.GONE
            mbinding.sellerRecycler.visibility = View.GONE
            if (it.isEmpty()) {
                mSearchSellerAdapter.removeItems()
                mbinding.progressPagination.visibility = View.GONE
                mbinding.searchRecycler.visibility = View.GONE
                mbinding.noDataChooseSeller.visibility = View.VISIBLE
                mbinding.sellerRecycler.visibility = View.GONE

            } else {
                mbinding.progressPagination.visibility = View.GONE
                mbinding.searchRecycler.visibility = View.VISIBLE
                mbinding.sellerRecycler.visibility = View.GONE
                mSearchSellerAdapter.updateData(
                    it,
                    searchText,
                    lat, long, Addressdatas
                )
            }
        })
        //failure
        mvmodel.mSearchFailure().observe(viewLifecycleOwner, Observer {
            mbinding.progressPagination.visibility = View.GONE
            mbinding.searchRecycler.visibility = View.GONE
            mbinding.noDataChooseSeller.visibility = View.VISIBLE
            mbinding.sellerRecycler.visibility = View.INVISIBLE


        })

        mvmodel.mUpdateAddress().observe(viewLifecycleOwner, Observer {
            val token = FirebaseInstanceId.getInstance().token
            //Log.e("getToken", token!!)
            viewModel.getProfile(token!!)
            mvmodel.setChooseSeller(
                it.lat,
                it.long,
                checkedHome.toString(),
                checkedSelf.toString()
            )
            Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
            dialog.dismiss()
        })
        mvmodel.mFailure().observe(viewLifecycleOwner, Observer {
            Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
            dialog.dismiss()
        })

        // **************************************Load More Response *************************************//
        // Successfull
        mvmodel.mSucessfulload_moreData().observe(viewLifecycleOwner, Observer {
            val homeDeliveryaddress = DeliveryZoneModel()
            homeDeliveryaddress.lat = lat
            homeDeliveryaddress.long = long
            homeDeliveryaddress.address = Addressdatas
            val intent = Intent(context, ChooseSellerLoadMoreActivity::class.java)
            intent.putExtra("keyLoadMoreChooseSeller", it)
            intent.putExtra("keyLoadMoreChooseSellerSearch", mbinding.search.text.toString())
            intent.putExtra("keyLoadMoreAddress", homeDeliveryaddress)
            startActivity(intent)
        })

        // Failure
        mvmodel.mFailureload_moreData().observe(viewLifecycleOwner, Observer {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
        })


        //**********************set the click on the search button*********************//
        mbinding.search.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                try {
                    searchText = s.toString()
                    if (s!!.length > 0) {
                        mbinding.progressPagination.visibility = View.VISIBLE
                        mbinding.sellerRecycler.visibility = View.GONE
                        mvmodel.searchProduct(
                            s.toString(),
                            lat,
                            long,
                            checkedHome.toString(),
                            checkedSelf.toString()
                        )
                    } else {
                        //by default open this when the text is not search
                        mbinding.progressPagination.visibility = View.VISIBLE
                        mvmodel.setChooseSeller(
                            lat,
                            long,
                            checkedHome.toString(),
                            checkedSelf.toString()
                        )
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }


        })

        //******************cancel button by default api set****************//
        mbinding.txtcancle.setOnClickListener {
            mbinding.progressPagination.visibility = View.VISIBLE
            //hide keyboard
            Constant.hideSoftKeyboard(it, context!!)
            //clear text
            mbinding.search.setText("")
            //set the api
            mvmodel.setChooseSeller(
                lat,
                long,
                checkedHome.toString(),
                checkedSelf.toString()
            )
        }
    }

    // ******************************************Load More API************************************//
    override fun loadmoreClick(data: CustomerParentModel, position: Int) {
        mvmodel.loadmoreSeller(
            data.cat_id, lat,
            long,
            checkedHome.toString(),
            checkedSelf.toString()
        )
    }

    // *********************************** Add Address by custom dialog ******************************************//
    private fun addAddressCustomDialog(i: Int) {
        try {
            var model = GetProfileModel()
            val gson = Gson()
            val json = Constant.getPrefs(this.activity!!).getString("profile", "")
            if (json!!.isNotEmpty()) {
                model = gson.fromJson(json, GetProfileModel::class.java)
            }
            dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.setContentView(R.layout.buyer_add_address_dialog)
            dialog.addressHome.visibility = View.INVISIBLE
            if (addressModel.address.isEmpty()) {
                dialog.address.setText(model.full_address)
                lat = model.lat
                long = model.long

            } else {
                dialog.address.setText(addressModel.address)
                lat = addressModel.lat
                long = addressModel.long
            }

            dialog.show()

            if (i == 1) {
                dialog.cancel.visibility = View.GONE
            } else {
                dialog.cancel.visibility = View.VISIBLE
                dialog.addresspleaseSelect.visibility = View.GONE
                dialog.addressheading.visibility = View.VISIBLE
            }
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
        //set the click button of custom dialog
        dialog.viewmap.setOnClickListener {
            // val intent = Intent(activity!!, UpdatedAddressActivity::class.java)
            val intent = Intent(activity!!, BuyerAddressMapBoxActivity::class.java)
            intent.putExtra("keyChooseSellerLat", lat)
            intent.putExtra("keyChooseSellerLong", long)
            startActivityForResult(intent, 129)
            try {

                if (ActivityCompat.checkSelfPermission(
                        activity!!,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                        activity!!, Manifest.permission.ACCESS_COARSE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    val mDialog = android.app.AlertDialog.Builder(context)
                    mDialog.setTitle(context?.getString(R.string.alert))
                    mDialog.setCancelable(false)

                    mDialog.setMessage(getString(R.string.LOcationPermissionCustomPopText))
                    mDialog.setPositiveButton(
                        context?.getString(R.string.ok),
                        DialogInterface.OnClickListener { dialog, which ->
                            ActivityCompat.requestPermissions(
                                activity!!, arrayOf(
                                    Manifest.permission.ACCESS_FINE_LOCATION,
                                    Manifest.permission.ACCESS_COARSE_LOCATION
                                ), 129
                            )
                            Handler().postDelayed({}, 1000)
                            dialog.cancel()
                        })
                    mDialog.show()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        dialog.updateButton.setOnClickListener {
            if (addressModelActivityResult.lat.isEmpty()) {

                if (addressModel.lat.isEmpty()) {
                    addressModel.lat = model.lat
                    addressModel.long = model.long
                    addressModel.address = model.full_address
                } else {
                    Addressdatas = addressModel.address
                    lat = addressModel.lat
                    long = addressModel.long
                }
            } else {
                addressModel = addressModelActivityResult
                Addressdatas = addressModelActivityResult.address
                lat = addressModelActivityResult.lat
                long = addressModelActivityResult.long
            }

            if (dialog.address.text.isEmpty()) {
                Toast.makeText(
                    context,
                    getString(R.string.Please_Enter_your_Address),
                    Toast.LENGTH_SHORT
                )
                    .show()
            } else {
                if (model.full_address.isEmpty()) {

                    mvmodel.updatedAddress(addressModel)

                } else {
                    mbinding.progressPagination.visibility = View.VISIBLE
                    mvmodel.setChooseSeller(
                        lat,
                        long,
                        checkedHome.toString(),
                        checkedSelf.toString()
                    )
                }
            }
        }
        dialog.cancel.setOnClickListener {
            dialog.dismiss()
        }
    }
    //***************************************On Activity Result*************************************//

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 129 && data != null) {
            if (resultCode == Activity.RESULT_OK) {
                activityGetDataStatus = 1
                try {
                    val geocoder = Geocoder(activity!!)
                    val address: Address?
                    try {
                        val latitudeResult = data.getStringExtra("lat")
                        val longitudeResult = data.getStringExtra("long")
                        // 2
                        val addresses =
                            geocoder.getFromLocation(
                                latitudeResult!!.toDouble(),
                                longitudeResult!!.toDouble(),
                                1
                            )
                        address = addresses[0]
                        Log.e("Map Fragment ", address.toString())
                        val builder = StringBuilder()
                        // builder.append(address?.featureName).append(",\t")
                        builder.append(address?.getAddressLine(0)).append(",\t")
                        val resultDestination = builder.toString()
                        addressModelActivityResult = data.getParcelableExtra("deliveryZone")!!
                        addressModelActivityResult.address = resultDestination
                        addressModelActivityResult.lat = latitudeResult
                        addressModelActivityResult.long = longitudeResult
                        //Addressdatas = resultDestination
                        if (addressModelActivityResult.address.isEmpty()) {
                            Toast.makeText(
                                context,
                                getString(R.string.network_error),
                                Toast.LENGTH_SHORT
                            ).show()

                        } else {
                            dialog.address.setText(addressModelActivityResult.address)
                        }
                        Log.e("Map Fragment2 ", resultDestination)
                    } catch (e: IOException) {
                        Log.e("MapsActivity", e.localizedMessage)
                    }
                } catch (e: Exception) {
                    Log.e("MainActivity", e.message.toString())
                }
            }
        }
    }

    //get location
    fun openLocation() {
        try {
            if (ActivityCompat.checkSelfPermission(
                    context!!,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    context!!, Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                val mDialog = android.app.AlertDialog.Builder(context)
                mDialog.setTitle(context?.getString(R.string.alert))
                mDialog.setCancelable(false)

                mDialog.setMessage(getString(R.string.LOcationPermissionCustomPopText))
                mDialog.setPositiveButton(
                    context?.getString(R.string.ok)
                ) { dialog, which ->
                    ActivityCompat.requestPermissions(
                        activity!!, arrayOf(
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION
                        ), 101
                    )
                    Handler().postDelayed({}, 1000)
                    dialog.cancel()
                }
                mDialog.show()

            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    //bydefault adapter
    private fun setAdapter() {
        val manager = LinearLayoutManager(context)
        mbinding.sellerRecycler.layoutManager = manager
        mSellerChooseAdapter = ChooseSellerParentAdapter(activity!!, this)
        mbinding.sellerRecycler.adapter = mSellerChooseAdapter
    }

    //search adapter
    private fun setAdapterSearch() {
        val manager = LinearLayoutManager(context)
        mbinding.searchRecycler.layoutManager = manager
        mSearchSellerAdapter = ChooseSellerSearchProductAdapter(activity!!)
        mbinding.searchRecycler.adapter = mSearchSellerAdapter
    }

    fun callMenuDialog() {

        addAddressCustomDialog(2)

    }

    override fun onResume() {
        super.onResume()
        //status
        model = GetProfileModel()
        val gson = Gson()
        val json = Constant.getPrefs(activity!!).getString(Constant.PROFILE, "")
        if (json!!.isNotEmpty()) {
            model = gson.fromJson(json, GetProfileModel::class.java)

        }

        if (activityGetDataStatus == 1) {
            activityGetDataStatus = 0
        } else {
            if (model.lat.isEmpty() && model.long.isEmpty()) {
                mbinding.noDataChooseSeller.visibility = View.VISIBLE
                mbinding.searchRecycler.visibility = View.GONE
                mbinding.sellerRecycler.visibility = View.GONE
                mbinding.progressPagination.visibility = View.GONE
            } else {
                mbinding.progressPagination.visibility = View.VISIBLE
                if (searchText.isEmpty()) {
                    mvmodel.setChooseSeller(
                        lat,
                        long,
                        checkedHome.toString(),
                        checkedSelf.toString()
                    )
                } else {
                    mvmodel.searchProduct(
                        searchText,
                        lat,
                        long,
                        checkedHome.toString(),
                        checkedSelf.toString()
                    )
                }


//                mbinding.progressPagination.visibility = View.VISIBLE
//                mvmodel.setChooseSeller(
//                    lat.toDouble(),
//                    long.toDouble(),
//                    checkedHome.toString(),
//                    checkedSelf.toString()
//                )
            }
        }
    }

    private fun LOgoutBannedUser() {
        mvmodel.logoutBannedUser().observe(viewLifecycleOwner, Observer {
            Toast.makeText(activity, it.message, Toast.LENGTH_SHORT).show()
            val intent = Intent(activity, LoginActivity::class.java)
            startActivity(intent)
            activity?.finish()
        })
    }

}

