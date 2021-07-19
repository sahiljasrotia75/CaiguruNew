package com.example.caiguru.buyer.buyerOrder

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.caiguru.R
import com.example.caiguru.commonScreens.dashBoardParentActivity.DashBoardViewModel
import com.example.caiguru.databinding.BuyerOrderFragmentBinding
import com.example.caiguru.seller.homeSeller.GetProfileModel
import com.example.caiguru.seller.shoppingListSellers.shoppingListSeller.ShopModel
import constant_Webservices.Constant


class BuyerOrderFragment : Fragment() {
    private lateinit var viewModelDashBoard: DashBoardViewModel
    private lateinit var adapter: BuyerOrderAdapter
    private lateinit var mBinding: BuyerOrderFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        mBinding =
            DataBindingUtil.inflate(inflater, R.layout.buyer_order_fragment, container, false)

        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModelDashBoard = ViewModelProvider(this).get(DashBoardViewModel::class.java)

        setAdapter()

//        setObserver()
    }

//    private fun setObserver() {
//        viewModelDashBoard.getdataProfile().observe(viewLifecycleOwner, Observer {
//            adapter.upDate(Constant.getProfileData(context!!).finalizeCount)
//
//        })
//
//    }

//    private fun initData() {
//        val shredpref = context?.getSharedPreferences("yogeshData", Context.MODE_PRIVATE)!!
//        val tokenFirebase = shredpref.getString("deviceId", "")!!
//        viewModelDashBoard.getProfile(tokenFirebase)//get profile Api
//    }


    private fun setAdapter() {
        val manager = LinearLayoutManager(activity)
        adapter = BuyerOrderAdapter(activity!!, getSearchList())
        mBinding.recyclerOrder.layoutManager = manager
        mBinding.recyclerOrder.adapter = adapter
    }

    private fun getSearchList(): ArrayList<ShopModel> {
        val arrayList: ArrayList<ShopModel> = ArrayList()

        var shopModel = ShopModel()
        shopModel.shopNext = R.drawable.ic_arrow_shopping_lst
        shopModel.shopImage = R.drawable.ic_finalize_orders
        shopModel.shopText = getString(R.string.finalize_your_order)
        arrayList.add(shopModel)

        shopModel = ShopModel()
        shopModel.shopNext = R.drawable.ic_arrow_shopping_lst
        shopModel.shopImage = R.drawable.ic_pending_approvals
        shopModel.shopText = getString(R.string.Pending_Approvals)
        arrayList.add(shopModel)

        shopModel = ShopModel()
        shopModel.shopNext = R.drawable.ic_arrow_shopping_lst
        shopModel.shopImage = R.drawable.ic_to_be_delivered
        shopModel.shopText = getString(R.string.To_be_delivered)
        arrayList.add(shopModel)



        shopModel = ShopModel()
        shopModel.shopNext = R.drawable.ic_arrow_shopping_lst
        shopModel.shopImage = R.drawable.ic_completed
        shopModel.shopText = getString(R.string.Completed)
        arrayList.add(shopModel)

        shopModel = ShopModel()
        shopModel.shopNext = R.drawable.ic_arrow_shopping_lst
        shopModel.shopImage = R.drawable.ic_cancelled
        shopModel.shopText = getString(R.string.Cancelled)
        arrayList.add(shopModel)

        return arrayList
    }

//    override fun onResume() {
//        super.onResume()
//        initData()
//    }

}
