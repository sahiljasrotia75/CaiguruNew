package com.example.caiguru.buyer.buyerLists.buyerShoppingListFragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.caiguru.R
import com.example.caiguru.buyer.buyerLists.buyerShopListModify.BuyerShopListModifyActivity
import com.example.caiguru.buyer.buyerLists.buyerQuotedShoppingList.BuyeropenShoppingListActivity
import com.example.caiguru.commonScreens.loginScreen.LoginActivity
import com.example.caiguru.databinding.BuyerShoppinglistFragmentBinding
import com.example.caiguru.seller.shoppingListSellers.closeList.sellerClosedList.CloseListPagination
import constant_Webservices.Constant
import kotlinx.android.synthetic.main.buyer_shoppinglist_fragment.*
import java.util.ArrayList

class BuyerShoppingFragment : Fragment(), modifyClick {


    private var observerData = ArrayList<BuyerShopModel>()
    lateinit var mBinding: BuyerShoppinglistFragmentBinding
    lateinit var viewModel: BuyerShoppingViewModel
    var islastpageData: Boolean = false
    var isLoadingMoreItems: Boolean = false
    var page = 1
    var layoutpag = LinearLayoutManager(activity)

    lateinit var adapter: BuyerShopAdapter
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.buyer_shoppinglist_fragment,
            container, false
        )
      //  viewModel = ViewModelProviders.of(this)[BuyerShoppingViewModel::class.java]
        viewModel = ViewModelProvider(this).get(BuyerShoppingViewModel::class.java)
        setAdapter()
        Pagination()
        setObserver()
        LOgoutBannedUser()
        //  viewModel.your_shopping_lists()
        mBinding.progress.visibility = View.VISIBLE
        return mBinding.root
    }


    private fun setObserver() {
        viewModel.mSucessfulshopListData().observe(viewLifecycleOwner, Observer {
            observerData = it
            mBinding.progressPAg.visibility = View.GONE
            if (it.isEmpty()) {
                mBinding.listSrcreenNoData.visibility = View.VISIBLE
                mBinding.progress.visibility = View.GONE
                mBinding.rvShoppingList.visibility = View.GONE
            } else {
                mBinding.rvShoppingList.visibility = View.VISIBLE
                adapter.update(it)
                mBinding.listSrcreenNoData.visibility = View.GONE
                mBinding.progress.visibility = View.GONE
            }
            isLoadingMoreItems = false
        })


        viewModel.mFailureShopList().observe(viewLifecycleOwner, Observer {
            mBinding.listSrcreenNoData.visibility = View.VISIBLE
            mBinding.progress.visibility = View.GONE
            mBinding.progressPAg.visibility = View.GONE
        //    Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            Constant.showToast(it,context!!)
//            val intent = Intent(context, NetworkErrorActivity::class.java)
//            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
//            startActivity(intent)
        })
    }

    private fun setAdapter() {
        //val manager = LinearLayoutManager(activity)
        adapter = BuyerShopAdapter(this.activity!!, this)
        mBinding.rvShoppingList.layoutManager = layoutpag
        mBinding.rvShoppingList.adapter = adapter
    }

    private fun LOgoutBannedUser() {
        viewModel.logoutBannedUser().observe(viewLifecycleOwner, Observer {
         //   Toast.makeText(activity, it.message, Toast.LENGTH_SHORT).show()
            Constant.showToast(it.message,context!!)
            val intent = Intent(activity, LoginActivity::class.java)
            startActivity(intent)
            activity?.finish()
        })
    }

    override fun modify(model: BuyerShopModel) {

        if (model.modify == "1") {
            val intent = Intent(activity, BuyerShopListModifyActivity::class.java)
            intent.putExtra("keyname", model)
            activity!!.startActivity(intent)
        } else {
            val intent = Intent(activity, BuyeropenShoppingListActivity::class.java)
            intent.putExtra("keyname", model)
            activity!!.startActivity(intent)
        }

    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == 75) {
            viewModel.your_shopping_lists(page.toString())
            //buyer side
        }
    }

    override fun onResume() {
        super.onResume()
        page = 1
        viewModel.your_shopping_lists(page.toString())
    }
    private fun Pagination() {
        //***********************pagination**********************//
        mBinding.rvShoppingList.addOnScrollListener(object : CloseListPagination(layoutpag) {
            override fun loadMoreItems() {
                isLoadingMoreItems = true
                progressPAg.visibility = View.VISIBLE
                page++
                //************get notification api
                viewModel.your_shopping_lists(page.toString())
            }

            override fun isLastPage(): Boolean {
                return islastpageData
            }

            override fun isLoading(): Boolean {
                return isLoadingMoreItems
            }
        })
    }
}