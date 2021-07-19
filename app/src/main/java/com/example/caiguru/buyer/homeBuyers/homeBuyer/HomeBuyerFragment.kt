package com.example.caiguru.buyer.homeBuyers.homeBuyer

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.InputFilter
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.caiguru.R
import com.example.caiguru.commonScreens.hideEmoji.HideEmoji
import com.example.caiguru.seller.shoppingListSellers.closeList.sellerClosedList.CloseListPagination
import com.example.caiguru.seller.shoppingListSellers.deliveryZoneSeller.DeliveryZoneModel
import com.google.firebase.iid.FirebaseInstanceId
import constant_Webservices.Constant
import kotlinx.android.synthetic.main.activity_buyer_home_view_detail.*
import kotlinx.android.synthetic.main.fragment_home_buyer.*
import kotlinx.android.synthetic.main.fragment_home_buyer.view.*
import kotlinx.android.synthetic.main.refute_reason_dialog.*
import kotlinx.android.synthetic.main.switch_dialog.*


class HomeBuyerFragment : Fragment(), HomeAdapter.setLikeInterface {

    private var localAddress: String = ""
    private var localLatitude: String = ""
    private var localLongitude: String = ""
    lateinit var viewModel: HomeViewModel
    private lateinit var dialog: Dialog
    lateinit var adapter: HomeAdapter
    lateinit var listener: SwitchListener
    lateinit var mView: View
    var islastpageData: Boolean = false
    var isLoadingMoreItems: Boolean = false
    var page = 1
    var layoutpag = LinearLayoutManagerWithSmoothScroller(activity)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mView = inflater.inflate(R.layout.fragment_home_buyer, container, false)
        //  viewModel = ViewModelProviders.of(this)[HomeViewModel::class.java]
        viewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        setAdapter()
        setObserver()//buyer observer
        clickEvents()
        viewSwitcher() //switcher
        //WhichSideUser()//hide or unhide the switch
        return mView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //***********************pagination**********************//
        buyerShoppingListRecycler.addOnScrollListener(object : CloseListPagination(layoutpag) {
            override fun loadMoreItems() {
                isLoadingMoreItems = true
                page++
                //get the data of home buyer
                //************home buyer Api*******//
                viewModel.setBuyerHome(localLatitude, localLongitude, page)
                progressPagination.visibility = View.VISIBLE
            }

            override fun isLastPage(): Boolean {
                return islastpageData
            }

            override fun isLoading(): Boolean {
                return isLoadingMoreItems
            }

        })
    }

    private fun getCurrentLatLong() {
        try {
            if (Constant.getProfileData(context!!).lat.isNotEmpty()) {
                localLatitude = Constant.getProfileData(context!!).lat
                localLongitude = Constant.getProfileData(context!!).long
                localAddress = Constant.getProfileData(context!!).full_address
                ProgressBuyerHomes.visibility = View.VISIBLE
                viewModel.setBuyerHome(localLatitude, localLongitude, page)
            }
            else {
                val token = FirebaseInstanceId.getInstance().token
                if (token != null) {
                    Log.e("getTokenHomePage1", token)
                }
                if (token != null) {
                    viewModel.getProfile(token)
                    Log.e("Step2", "api hit")
                }
            }
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }

//    fun WhichSideUser() {
//        if (Constant.whichSideScreenOpen == "buyerSide") {
//            viewSwitcher()
//        } else {
//            mView.switchView.visibility = View.GONE
//        }
//    }


    private fun viewSwitcher() {

        val type = Constant.getPrefs(activity!!).getString(Constant.type, "")
        val switch = Constant.getPrefs(activity!!).getString(Constant. switch_active, "")

        if (switch == "2" && type == "2") {

            mView.switchView.visibility = View.GONE

        } else {
            mView.switchView.visibility = View.VISIBLE
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            listener = context as SwitchListener
        } catch (e: ClassCastException) {
            e.stackTrace
        }
    }

    private fun clickEvents() {
        mView.switchView.setOnClickListener {
            showSwitchDialog()
        }
    }

    private fun setAdapter() {
        adapter = HomeAdapter(activity!!, this)
        mView.buyerShoppingListRecycler.layoutManager = layoutpag
        mView.buyerShoppingListRecycler.adapter = adapter
        mView.buyerShoppingListRecycler.adapter = adapter

    }

    //***********observer
    private fun setObserver() {
        viewModel.mSuccessfulBuyerHome().observe(viewLifecycleOwner, Observer {
            ProgressBuyerHomes.visibility = View.GONE
            progressPagination.visibility = View.GONE
            if (it.isEmpty()) {
                NODataSeller.visibility = View.VISIBLE
                buyerShoppingListRecycler.visibility = View.GONE
            } else {
                adapter.update(it, localAddress, localLatitude, localLongitude)
                isLoadingMoreItems = false
                NODataSeller.visibility = View.GONE
                buyerShoppingListRecycler.visibility = View.VISIBLE
            }
        })
        //failure
        viewModel.mErrorbuyerHome().observe(viewLifecycleOwner, Observer {
            NODataSeller.visibility = View.VISIBLE
            //NODatadescriptionText.text = it
            ProgressBuyerHomes.visibility = View.GONE
            progressPagination.visibility = View.GONE
            buyerShoppingListRecycler.visibility = View.GONE
            //  Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            Constant.showToast(it, context!!)
        })

        //************observer of get profile api****************//
        viewModel.getdata().observe(viewLifecycleOwner, Observer {
            ProgressBuyerHomes.visibility = View.GONE
            progressPagination.visibility = View.GONE
            if (it.full_address.isEmpty()) {
                buyerShoppingListRecycler.visibility = View.GONE
                NODataSeller.visibility = View.VISIBLE
            } else {
                NODataSeller.visibility = View.GONE
                buyerShoppingListRecycler.visibility = View.VISIBLE
                //*******set the data for the api********//
                localAddress = it.full_address
                localLatitude = it.lat
                localLongitude = it.long
                ProgressBuyerHomes.visibility = View.VISIBLE
                viewModel.setBuyerHome(it.lat, it.long, page)
                viewSwitcher()
            }
        })
        //******************in case of failure********************//
        viewModel.errorget().observe(viewLifecycleOwner, Observer {
            progressPagination.visibility = View.GONE
            ProgressBuyerHomes.visibility = View.GONE
            NODataSeller.visibility = View.VISIBLE
            buyerShoppingListRecycler.visibility = View.GONE
            // Toast.makeText(context!!, it.message, Toast.LENGTH_SHORT).show()
            Constant.showToast(it.message, context!!)
        })
//set the report observer
        viewModel.mFailureReposrtlist().observe(viewLifecycleOwner, Observer {
            Constant.showToast(it,context!!)
            dialog.dismiss()

        })
        viewModel.mSucessfulREportList().observe(viewLifecycleOwner, Observer {
            Constant.showToast(it,context!!)
            dialog.dismiss()

        })
    }


    private fun showSwitchDialog() {
        dialog = Dialog(activity!!)
        // dialog.setCancelable(false);
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setContentView(R.layout.switch_dialog)
        dialog.show()

        dialog.yes.setOnClickListener {
            // here swith permanently to seller
            listener.onSwitchChangeYes()
            dialog.dismiss()
        }

        dialog.no.setOnClickListener {
            // not a permanent seller
            listener.onSwitchChangeNo()
            dialog.dismiss()
        }
    }

    interface SwitchListener {
        fun onSwitchChangeNo()
        fun onSwitchChangeYes()
    }

    override fun setLike(hasLiked: Boolean, model: HomeModel) {
        // 1: Like, 0: Dislike
        var Like: String = ""
        if (hasLiked) {
            Like = "1"
            viewModel.setLikeDisLike(Like, model.post_id, model.is_like)
        } else {
            Like = "0"
            viewModel.setLikeDisLike(Like, model.post_id, model.is_like)
        }
    }

    // *****************share kit open*************//
    override fun shareShoppingList(
        model: HomeModel,
        allComission: String
    ) {
        var allText = ""
        for (item in model.products) {
            if (allText.isEmpty()) {

                allText =
                    "${getString(R.string.namesShare)}: ${item.name}, ${getString(R.string.Unit)}: ${
                        Constant.convertUnits(
                            context!!,
                            item.unit
                        )
                    }, ${
                        getString(
                            R.string.Price
                        )
                    }: $${item.priceWithComission}"

            } else {
                allText =
                    allText + " \n" + "${getString(R.string.namesShare)}: ${item.name}, ${
                        getString(
                            R.string.Unit
                        )
                    }: ${
                        Constant.convertUnits(
                            context!!,
                            item.unit
                        )
                    }, ${
                        getString(
                            R.string.Price
                        )
                    }: $${item.priceWithComission}"
            }
        }

        val message =
            "${getString(R.string.share_List_text)}\n$allText\n${getString(R.string.ShareLinkText)}: ${
                Constant.getProfileData(
                    context!!
                ).sharelink
            }"
        val share = Intent(Intent.ACTION_SEND)
        share.type = "text/plain"
        share.putExtra(Intent.EXTRA_TEXT, message)
        startActivityForResult(Intent.createChooser(share, getString(R.string.Share)), 1)
    }

    //***************set the click on the purchase button**********//
//    override fun purchaseButtonClick() {
//
//    }
    override fun onResume() {
        super.onResume()
        if (Constant.apiHitOrNot == 0) {
            page = 1
            //************home buyer Api*******//
            try {
                val addressModel = arguments?.getParcelable<DeliveryZoneModel>("edttext")
                if (addressModel!!.address.isNotEmpty()) {
                    // Toast.makeText(context!!, addressModel.address, Toast.LENGTH_SHORT).show()
                    localAddress = addressModel.address
                    localLatitude = addressModel.lat
                    localLongitude = addressModel.long
                    // addressText.text = localAddress
                    ProgressBuyerHomes.visibility = View.VISIBLE
                    viewModel.setBuyerHome(localLatitude, localLongitude, page)
                }
//                else {
//                    getCurrentLatLong()
//                }
            } catch (e: Exception) {
                e.printStackTrace()
                getCurrentLatLong()
            }
        } else {
            Constant.apiHitOrNot = 0
        }
    }


    //    override fun openImagesInterfaces(list: ArrayList<PostShoppingModel>, position: Int) {
//
//        val intent = Intent(context, ImageFullScreenActivity::class.java)
//        intent.putExtra("keyImage", list)
//        intent.putExtra("keyImagePosition", position)
//        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
//        startActivity(intent)
//
//    }
    //report list listner
    override fun reportListClick(model: HomeModel) {
        reportListClickDialog(model)

    }

    private fun reportListClickDialog(model: HomeModel) {
            val mDialog = AlertDialog.Builder(activity!!)
            mDialog.setTitle(getString(R.string.alert))
            mDialog.setCancelable(true)
            mDialog.setMessage(getString(R.string.report_text3))
            mDialog.setPositiveButton(
                getString(R.string.ok)
            ) { dialog, which ->
                openReasonPopup(model)
                dialog.cancel()
            }
            mDialog.setNegativeButton(
                getString(R.string.cancel)
            ) { dialog, which ->
                dialog.cancel()
            }
            mDialog.show()

    }

    private fun openReasonPopup(data: HomeModel) {
        dialog = Dialog(activity!!)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setContentView(R.layout.refute_reason_dialog)
        dialog.edtReason.hint = getString(R.string.enter_reason)
        dialog.show()
        dialog.edtReason.filters = arrayOf<InputFilter>(
            HideEmoji(activity!!)
        )

        dialog.SubmitBtn.setOnClickListener {
            if (dialog.edtReason.text.isEmpty()) {

                Constant.showToast(getString(R.string.enter_reason), activity!!)

            } else {
                viewModel.reportList(dialog.edtReason.text.toString().trim(), data)
                dialog.waitBtn.visibility = View.VISIBLE
                dialog.SubmitBtn.visibility = View.GONE
            }

        }

    }
}


