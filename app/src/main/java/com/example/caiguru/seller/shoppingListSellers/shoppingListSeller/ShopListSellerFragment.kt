package com.example.caiguru.seller.shoppingListSellers.shoppingListSeller

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.caiguru.R
import com.example.caiguru.databinding.FragmentShopSellerBinding
import constant_Webservices.Constant

class ShopListSellerFragment : Fragment() {

    lateinit var mBinding: FragmentShopSellerBinding
    lateinit var adapter: ShopAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_shop_seller, container, false)

        val seller_active_status = Constant.getPrefs(activity!!).getString(Constant.seller_active_status, "")

        if (seller_active_status == "1") {

            //    mBinding.switchView.visibility = View.VISIBLE
            mBinding.activeShop.background = resources.getDrawable(R.color.green)
            mBinding.activeShop.text = resources.getText(R.string.active)

        } else {
            // mBinding.switchView.visibility = View.VISIBLE
            mBinding.activeShop.background = resources.getDrawable(R.color.red)
            mBinding.activeShop.text = resources.getText(R.string.inactive)
        }
        setAdapter()
        return mBinding.root

    }

    private fun setAdapter() {
        val manager = LinearLayoutManager(activity)
        adapter = ShopAdapter(activity!!, getSearchList())
        mBinding.rvShopList.layoutManager = manager
        mBinding.rvShopList.adapter = adapter
    }

    private fun getSearchList(): ArrayList<ShopModel> {
        val arrayList: ArrayList<ShopModel> = ArrayList()

        var shopModel = ShopModel()
        shopModel.shopNext = R.drawable.ic_arrow_shopping_lst
        shopModel.shopImage = R.drawable.ic_post
        shopModel.shopText = getString(R.string.sell_post_shopping)
        arrayList.add(shopModel)


        shopModel = ShopModel()
        shopModel.shopNext = R.drawable.ic_arrow_shopping_lst
        shopModel.shopImage = R.drawable.ic_list_uploaded_by_customers
        shopModel.shopText = getString(R.string.List_uploaded_by_customers)
        arrayList.add(shopModel)



        shopModel = ShopModel()
        shopModel.shopNext = R.drawable.ic_arrow_shopping_lst
        shopModel.shopImage = R.drawable.ic_open_lists
        shopModel.shopText = getString(R.string.Open_list)
        arrayList.add(shopModel)

        shopModel = ShopModel()
        shopModel.shopNext = R.drawable.ic_arrow_shopping_lst
        shopModel.shopImage = R.drawable.ic_close_lists
        shopModel.shopText = getString(R.string.Closed_lists)
        arrayList.add(shopModel)

        return arrayList
    }
}