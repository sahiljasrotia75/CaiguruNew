package com.example.caiguru.seller.sellerOrder.sellerOrdersFragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.caiguru.R
import com.example.caiguru.databinding.FragmentSellerOrderBinding
import com.example.caiguru.seller.shoppingListSellers.shoppingListSeller.ShopModel
import constant_Webservices.Constant


class SellerOrderFragment : Fragment() {
    private lateinit var adapter: SellerOrderAdapter
    private lateinit var mBinding: FragmentSellerOrderBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        mBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_seller_order, container, false)
        val seller_active_status =
            Constant.getPrefs(activity!!).getString(Constant.seller_active_status, "")

        if (seller_active_status == "1") {

            //    mBinding.switchView.visibility = View.VISIBLE
            mBinding.activeShop.background = resources.getDrawable(R.color.green)
            mBinding.activeShop.text = resources.getText(R.string.active)

        } else {
            // mBinding.switchView.visibility = View.VISIBLE
            mBinding.activeShop.background = resources.getDrawable(R.color.red)
            mBinding.activeShop.text = resources.getText(R.string.inactive)
        }
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setAdapter()
    }


    private fun setAdapter() {
        val manager = LinearLayoutManager(activity)
        adapter = SellerOrderAdapter(activity!!, getSearchList())
        mBinding.recyclerOrder.layoutManager = manager
        mBinding.recyclerOrder.adapter = adapter
    }

    private fun getSearchList(): ArrayList<ShopModel> {
        val arrayList: ArrayList<ShopModel> = ArrayList()

        var shopModel = ShopModel()
        shopModel.shopNext = R.drawable.ic_arrow_shopping_lst
        shopModel.shopImage = R.drawable.ic_pending_approvals
        shopModel.shopText =getString(R.string.Pending_Approvals)
        arrayList.add(shopModel)


        shopModel = ShopModel()
        shopModel.shopNext = R.drawable.ic_arrow_shopping_lst
        shopModel.shopImage = R.drawable.ic_to_be_delivered
        shopModel.shopText = getString(R.string.To_be_delivered)
        arrayList.add(shopModel)



        shopModel = ShopModel()
        shopModel.shopNext = R.drawable.ic_arrow_shopping_lst
        shopModel.shopImage = R.drawable.ic_completed
        shopModel.shopText =  getString(R.string.Completed)
        arrayList.add(shopModel)

        shopModel = ShopModel()
        shopModel.shopNext = R.drawable.ic_arrow_shopping_lst
        shopModel.shopImage = R.drawable.ic_cancelled
        shopModel.shopText =getString(R.string.Cancelled)
        arrayList.add(shopModel)

        return arrayList
    }


}
