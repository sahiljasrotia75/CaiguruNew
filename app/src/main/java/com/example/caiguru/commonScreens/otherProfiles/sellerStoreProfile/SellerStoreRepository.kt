package com.example.caiguru.commonScreens.otherProfiles.sellerStoreProfile

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.example.caiguru.R
import com.example.caiguru.buyer.buyerOrder.finalizeOrder.FinalizeModel
import com.example.caiguru.seller.homeSeller.GetProfileModel
import com.example.caiguru.seller.sellerOrder.sellerPendingApprovals.SellerApprovalModel
import com.example.caiguru.seller.shoppingListSellers.sellerPostShoppingList.PostShoppingModel
import com.example.caiguru.seller.shoppingListSellers.shoppingListPosted.ShoppingListModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

import constant_Webservices.Constant
import constant_Webservices.WebServices
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class SellerStoreRepository(var application: Application) {
    var mSucessfull = MutableLiveData<GetProfileModel>()
    var mFailuree = MutableLiveData<String>()

    fun getsellerstore(model: FinalizeModel, setPriceFilter: String) {
        val retrofit = Constant.getRestClient()
        val services = retrofit.create(WebServices::class.java)
        val call: Call<ResponseBody> = services.getsellerstore(
            "Bearer " + Constant.getPrefs(application).getString(
                Constant.token,
                ""
            ), model.seller_id, model.lat, model.long, model.cat_id, setPriceFilter
        )
        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                try {
                    if (response.isSuccessful) {
                        val resp = response.body()!!.string()
                        val json = JSONObject(resp)
                        val status = json.optString("status")
                        if (status == "true") {
                            val gson = Gson()
                            val modelData: GetProfileModel =
                                gson.fromJson(
                                    json.toString(),
                                    object : TypeToken<GetProfileModel>() {}.type
                                )
//                            if (modelData.lists.size > 0) {
//                                modelData.lists[0].isExpanded = true
//                            }
                            mSucessfull.value = modelData

                        } else {
                            val msg = application.getString(R.string.network_error)
                            mFailuree.value = msg
                        }
                    } else {
                        val msg = application.getString(R.string.network_error)
                        mFailuree.value = msg
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                val errormsg = application.getString(R.string.network_error)
                mFailuree.value = errormsg
            }
        })

    }

    //status true observer
    fun getdata(): MutableLiveData<GetProfileModel> {
        return mSucessfull
    }

    //error observer
    fun errorget(): MutableLiveData<String> {

        return mFailuree
    }


    //**************************follow unfollow api********************//
    var mSucessfullFollowUnfollow = MutableLiveData<GetProfileModel>()
    var mFailureeFollowUnfollow = MutableLiveData<String>()

    //********************follow or unfollow api************//
    fun setFollowUnfollow(data: FinalizeModel, followStatus: Int) {

        val retrofit = Constant.getRestClient()
        val services = retrofit.create(WebServices::class.java)
        val call: Call<ResponseBody> = services.follow_unfollow(
            "Bearer " + Constant.getPrefs(application).getString(
                Constant.token,
                ""
            ), data.seller_id, followStatus.toString()
        )
        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {

                try {
                    val resp = response.body()!!.string()
                    val json = JSONObject(resp)
                    val status = json.optString("status")
                    if (status == "true") {
                        val gson = Gson()
                        val modelData: GetProfileModel =
                            gson.fromJson(
                                json.toString(),
                                object : TypeToken<GetProfileModel>() {}.type
                            )
                        mSucessfullFollowUnfollow.value = modelData
                        // {"status":"true","follow_status":"1"}
                    } else {
                        var msg = application.getString(R.string.network_error)
                        mFailureeFollowUnfollow.value = msg
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                val errormsg = application.getString(R.string.network_error)
                mFailureeFollowUnfollow.value = errormsg
            }
        })
    }


    //status true observer
    fun mSucessfullFollowUnfollow(): MutableLiveData<GetProfileModel> {
        return mSucessfullFollowUnfollow
    }

    //error observer
    fun mFailureeFollowUnfollow(): MutableLiveData<String> {

        return mFailureeFollowUnfollow
    }

    //*********************blocked user*****************//
    val mSucessfulBlockedUser = MutableLiveData<String>()
    val mFailureBlockedUser = MutableLiveData<String>()

    fun blockedUser(data: SellerApprovalModel, type: String) {

        val retrofit = Constant.getRestClient()
        val services = retrofit.create(WebServices::class.java)
        val call: Call<ResponseBody> = services.block_unblock_user(
            "Bearer " + Constant.getPrefs(application).getString(
                Constant.token,
                ""
            ), data.buyer_id, type, "1"
        )
        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                try {
                    if (response.isSuccessful) {
                        val resp = response.body()!!.string()
                        val json = JSONObject(resp)
                        val status = json.optString("status")
                        val message = json.optString("message_${Constant.getLocal(application)}")
                        if (status == "true") {

                            mSucessfulBlockedUser.value = message

                        } else {
                            val msg = message
                            mFailureBlockedUser.value = msg
                        }
                    } else {
                        val msg = application.getString(R.string.network_error)
                        mFailureBlockedUser.value = msg
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                val errormsg = application.getString(R.string.network_error)
                mFailureBlockedUser.value = errormsg
            }
        })

    }

    //status true observer
    fun getSucessfulBlockedUser(): MutableLiveData<String> {
        return mSucessfulBlockedUser
    }

    //error observer
    fun errorgetBlockedUser(): MutableLiveData<String> {

        return mFailureBlockedUser
    }
//****************SearchApi

    var mFailureSearrch = MutableLiveData<String>()
    var mSucessfulSearchStore = MutableLiveData<ArrayList<PostShoppingModel>>()
    val localArray = ArrayList<PostShoppingModel>()
    fun getSearchData(
        search: String,
        model: FinalizeModel,
        page: String,
        setPriceFilter: String,
        searchUsedProds: String
    ) {
        val retrofit = Constant.getRestClient()
        val services = retrofit.create(WebServices::class.java)
        val call: Call<ResponseBody> = services.search_store_products(
            "Bearer " + Constant.getPrefs(application).getString(
                Constant.token,
                ""
            ), model.seller_id, model.lat, model.long, search, setPriceFilter,searchUsedProds, page

        )
        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                try {
                    if (response.isSuccessful) {
                        if (page == "1") {
                            localArray.clear()
                        }
                        val res = response.body()!!.string()
                        val json = JSONObject(res)
                        val status = json.optString("status")

                        val msg = json.optString("message_${Constant.getLocal(application)}")
                        if (status == "true") {
                            val usedProds = json.optString("usedProds").toString()
                            val count = json.optString("count").toString()
                            val products = json.optJSONArray("products").toString()
                            val gson = Gson()
                            val arrayData: ArrayList<PostShoppingModel> =
                                gson.fromJson(
                                    products,
                                    object : TypeToken<ArrayList<PostShoppingModel>>() {}.type
                                )
                            localArray.addAll(arrayData)
                            localArray[0].usedProds=usedProds
                            localArray[0].searchCaseShowBtn="showLoadMoreBtn"
                            localArray[0].count=count.toInt()
                            mSucessfulSearchStore.value = localArray
                        } else {
                            val msg = msg
                            mFailureSearrch.value = msg.toString()

                        }
                    }

                } catch (e: java.lang.Exception) {
                    val msg = application.getString(R.string.network_error)
                    mFailureSearrch.value = msg
                    e.printStackTrace()
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                val msg = application.getString(R.string.network_error)
                mFailureSearrch.value = msg
            }

        })

    }

    //status true observer
    fun mScucessfulSearchProdcut(): MutableLiveData<ArrayList<PostShoppingModel>> {
        return mSucessfulSearchStore
    }

    //error observer
    fun getErrorSearch(): MutableLiveData<String> {

        return mFailureSearrch
    }

    var mFailureLoadMore = MutableLiveData<String>()
    var mSucessfulLoadMore = MutableLiveData<ShoppingListModel>()

    fun LoadMoreData(
        adapterData: ShoppingListModel,
        localMOdelData: FinalizeModel,
        setPriceFilter: String
    ) {

        val retrofit = Constant.getRestClient()
        val services = retrofit.create(WebServices::class.java)
        val call: Call<ResponseBody> = services.load_more_store_products(
            "Bearer " + Constant.getPrefs(application).getString(
                Constant.token,
                ""
            ),
            localMOdelData.seller_id,
            localMOdelData.lat,
            localMOdelData.long,
            adapterData.cat_id,
            setPriceFilter,
            adapterData.usedProds,
            adapterData.page.toString()

        )
        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                try {
                    if (response.isSuccessful) {
//                        if (page == "1") {
//                            localArray.clear()
//                        }
                        val res = response.body()!!.string()
                        val json = JSONObject(res)
                        val status = json.optString("status")

                        val msg = json.optString("message_${Constant.getLocal(application)}")
                        if (status == "true") {
                         //   val products = json.optJSONArray("products").toString()
                            val gson = Gson()
                            val arrayData: ShoppingListModel =
                                gson.fromJson(
                                    json.toString(),
                                    object : TypeToken<ShoppingListModel>() {}.type
                                )
                         //   localArray.addAll(arrayData)
                            mSucessfulLoadMore.value = arrayData
                        } else {
                            val msg = msg
                            mFailureLoadMore.value = msg.toString()

                        }
                    }

                } catch (e: java.lang.Exception) {
                    val msg = application.getString(R.string.network_error)
                    mFailureLoadMore.value = msg
                    e.printStackTrace()
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                val msg = application.getString(R.string.network_error)
                mFailureLoadMore.value = msg
            }

        })

    }

    //status true observer
    fun mSucessfulLoadMore(): MutableLiveData<ShoppingListModel> {
        return mSucessfulLoadMore
    }

    //error observer
    fun mFailureLoadMore(): MutableLiveData<String> {

        return mFailureLoadMore
    }


    //******************save_seller_cart**************//
    var mFailureSavedCart = MutableLiveData<String>()
    var mSucessfulSavedCart = MutableLiveData<String>()
    fun save_seller_cart(sellerId: String, jsonProductArray: String) {
        val retrofit = Constant.getRestClient()
        val services = retrofit.create(WebServices::class.java)
        val call: Call<ResponseBody> = services.save_seller_cart(
            "Bearer " + Constant.getPrefs(application).getString(
                Constant.token,
                ""
            ),
            sellerId,
            jsonProductArray

        )
        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                try {
                    if (response.isSuccessful) {
                        val res = response.body()!!.string()
                        val json = JSONObject(res)
                        val status = json.optString("status")

                        val msg = json.optString("message_${Constant.getLocal(application)}")
                        if (status == "true") {
//
                            mSucessfulSavedCart.value = msg
                        } else {
                            mFailureSavedCart.value = msg

                        }
                    }

                } catch (e: java.lang.Exception) {
                    val msg = application.getString(R.string.network_error)
                    mFailureSavedCart.value = msg
                    e.printStackTrace()
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                val msg = application.getString(R.string.network_error)
                mFailureSavedCart.value = msg
            }

        })

    }

    fun mSucessfulSavedCart(): MutableLiveData<String> {
        return mSucessfulSavedCart
    }

    fun mFailureSavedCart(): MutableLiveData<String> {

        return mFailureSavedCart
    }

    //***********************get_seller_cart***************//
    var mFailureCart = MutableLiveData<String>()
    var mSucessfulCartData = MutableLiveData<ArrayList<PostShoppingModel>>()
    fun get_seller_cart(sellerId: String) {

        val retrofit = Constant.getRestClient()
        val services = retrofit.create(WebServices::class.java)
        val call: Call<ResponseBody> = services.get_seller_cart(
            "Bearer " + Constant.getPrefs(application).getString(
                Constant.token,
                ""
            ),
            sellerId

        )
        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                try {
                    if (response.isSuccessful) {
                        val res = response.body()!!.string()
                        val json = JSONObject(res)
                        val status = json.optString("status")

                        val msg = json.optString("message_${Constant.getLocal(application)}")
                        if (status == "true") {
                            val productArray = json.optJSONArray("products").toString()

                            val gson = Gson()
                            val arrayData: ArrayList<PostShoppingModel> =
                                gson.fromJson(
                                    productArray,
                                    object : TypeToken<ArrayList<PostShoppingModel>>() {}.type
                                )
                            mSucessfulCartData.value = arrayData
                        } else {
                            mFailureCart.value = msg

                        }
                    }

                } catch (e: java.lang.Exception) {
                    val msg = application.getString(R.string.network_error)
                    mFailureCart.value = msg
                    e.printStackTrace()
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                val msg = application.getString(R.string.network_error)
                mFailureCart.value = msg
            }

        })

    }
    fun mSucessfulCartData(): MutableLiveData<ArrayList<PostShoppingModel>> {
        return mSucessfulCartData
    }

    fun mFailureCart(): MutableLiveData<String> {

        return mFailureCart
    }
}
