package com.example.caiguru.seller.homeSeller

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.caiguru.R
import com.example.caiguru.commonScreens.loginScreen.LoginActivity
import com.example.caiguru.databinding.FragmentHomeSellerBinding
import com.example.caiguru.seller.shoppingListSellers.closeList.sellerClosedList.CloseListPagination
import com.example.caiguru.seller.shoppingListSellers.sellerPostShoppingList.ShoppingListPart1Activity
import com.google.firebase.iid.FirebaseInstanceId
import constant_Webservices.Constant
import constant_Webservices.Constant.Companion.token
import kotlinx.android.synthetic.main.fragment_home_seller.*
import kotlinx.android.synthetic.main.home_seller_posting_list_layout.view.*
import kotlinx.android.synthetic.main.nodata.*
import kotlinx.android.synthetic.main.nodata.view.*
import kotlinx.android.synthetic.main.switch_dialog.*
import java.lang.Exception

class HomeSellerFragment : Fragment() {
    private lateinit var adapterSeller: HomeSellerAdapter
    lateinit var mBinding: FragmentHomeSellerBinding
    lateinit var viewModel: HomeSellerViewModel
    private lateinit var dialog: Dialog
    lateinit var listener: SwitchListenerBuyer
    var islastpageData: Boolean = false
    var isLoadingMoreItems: Boolean = false
    var page = 1
    var layoutpag = LinearLayoutManager(activity)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_home_seller, container, false)
        viewModel = ViewModelProvider(this).get(HomeSellerViewModel::class.java)
        clickEvents()
//        try {
//            val token = FirebaseInstanceId.getInstance().token
//            Log.e("getToken", token)
//        } catch (e: Exception) {
//            e.printStackTrace()
//        }

        SetAdapterHomeSeller()
        switchTypeData()
        setObserver()
        homeSellerObserver()
        LOgoutBannedUser()
        return mBinding.root
    }


    private fun homeSellerObserver() {
        viewModel.mSuccessfulHomeData().observe(viewLifecycleOwner, Observer {
            homeProgress.visibility = View.GONE
            homeProgressPagination.visibility = View.GONE
            isLoadingMoreItems = false

            if (it.isEmpty()) {

                txtNoDatas.visibility = View.VISIBLE
            } else {
                txtNoDatas.visibility = View.GONE
                adapterSeller.upDate(it)
                SellerHomeRecycler.visibility = View.VISIBLE
            }

        })
        // failure
        viewModel.mErrorHomeData().observe(viewLifecycleOwner, Observer {
            txtNoDatas.visibility = View.VISIBLE
            homeProgressPagination.visibility = View.GONE
            SellerHomeRecycler.visibility = View.GONE
            homeProgress.visibility = View.GONE
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //***********************pagination**********************//
        mBinding.SellerHomeRecycler.addOnScrollListener(object : CloseListPagination(layoutpag) {
            override fun loadMoreItems() {
                isLoadingMoreItems = true
                homeProgressPagination.visibility = View.VISIBLE
                page++
                //get the data of home seller
                viewModel.getHomeSellerData(page.toString())
                homeProgressPagination.visibility = View.VISIBLE

            }

            override fun isLastPage(): Boolean {
                return islastpageData
            }

            override fun isLoading(): Boolean {
                return isLoadingMoreItems
            }

        })
    }

    private fun switchTypeData() {
        val type = Constant.getPrefs(activity!!).getString(Constant.type, "")
        val switch = Constant.getPrefs(activity!!).getString(Constant.switch_active, "")

        if (switch == "2" && type == "2") {
            mBinding.switchView.visibility = View.VISIBLE

        } else {
            mBinding.switchView.visibility = View.VISIBLE
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            listener = context as SwitchListenerBuyer
        } catch (e: ClassCastException) {
            e.stackTrace
        }
    }

    private fun clickEvents() {
        mBinding.switchView.setOnClickListener {
            showSwitchDialog()
        }
        //**********ope the list
        mBinding.FirstTimeListCreate.btnPostShpooingList.setOnClickListener {
            val intent = Intent(context, ShoppingListPart1Activity::class.java)
            startActivity(intent)
        }
    }

    private fun showSwitchDialog() {
        dialog = Dialog(activity!!)
        // dialog.setCancelable(false);
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setContentView(R.layout.switch_dialog_buyer)
        dialog.show()


        dialog.yes.setOnClickListener {
            // here swith permanently to seller
            listener.onSwitchChangeBuyerYes()
            dialog.dismiss()
        }

        dialog.no.setOnClickListener {
            // not a permanent seller
            listener.onSwitchChangeBuyerNo()
            dialog.dismiss()
        }
    }


    fun setObserver() {
        mBinding.txtNoDatas.NODatadescriptionText.text = getString(R.string.order_will_appear_here)
        viewModel.SucessfulData().observe(viewLifecycleOwner, Observer {
            mBinding.activeHome.visibility = View.VISIBLE
            val seller_active_status =
                Constant.getPrefs(activity!!).getString(Constant.seller_active_status, "")
            if (seller_active_status == "1") {
                listener.onStatus(seller_active_status)
                mBinding.activeHome.background = resources.getDrawable(R.color.green)
                mBinding.activeHome.text = resources.getText(R.string.active)
            } else {
                listener.onStatus(seller_active_status!!)
                mBinding.activeHome.background = resources.getDrawable(R.color.red)
                mBinding.activeHome.text = resources.getText(R.string.inactive)
            }
            if (Constant.getProfileData(context!!).listcount == "0") {
                FirstTimeListCreate.visibility = View.VISIBLE
                SellerHomeRecycler.visibility = View.GONE
                txtNoDatas.visibility = View.GONE

            } else {
                FirstTimeListCreate.visibility = View.GONE
                //get the data of home seller
                viewModel.getHomeSellerData(page.toString())
                homeProgress.visibility = View.VISIBLE
            }
        })
        viewModel.errorget().observe(viewLifecycleOwner, Observer {
            // txtNoData.visibility = View.VISIBLE
            homeProgressPagination.visibility = View.GONE
            SellerHomeRecycler.visibility = View.GONE
            homeProgress.visibility = View.GONE
            mBinding.activeHome.visibility = View.INVISIBLE
            Toast.makeText(activity, it.message, Toast.LENGTH_SHORT).show()
        })
    }

    interface SwitchListenerBuyer {
        fun onSwitchChangeBuyerNo()
        fun onSwitchChangeBuyerYes()
        fun onStatus(seller_active_status: String)
    }

    override fun onResume() {
        super.onResume()
//        if (Constant.getProfileData(context!!).listcount=="0"){
//            viewModel.getProfile(token)
//        }else{
//            viewModel.getProfile(token)
//        }
        viewModel.getProfile(token)
        page = 1
        //get the data of home seller
//        viewModel.getHomeSellerData(page.toString())
//        homeProgress.visibility = View.VISIBLE

    }

    private fun SetAdapterHomeSeller() {
        adapterSeller = HomeSellerAdapter(activity!!)
        mBinding.SellerHomeRecycler.layoutManager = layoutpag
        mBinding.SellerHomeRecycler.adapter = adapterSeller
    }

    private fun LOgoutBannedUser() {
        viewModel.logoutBannedUser().observe(viewLifecycleOwner, Observer {
            Toast.makeText(activity, it.message, Toast.LENGTH_SHORT).show()
            val intent = Intent(activity, LoginActivity::class.java)
            startActivity(intent)
            activity!!.finish()
        })
    }
}

