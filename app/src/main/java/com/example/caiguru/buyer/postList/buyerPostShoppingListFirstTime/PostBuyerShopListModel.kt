package com.example.caiguru.buyer.postList.buyerPostShoppingListFirstTime

import com.example.caiguru.seller.shoppingListSellers.sellerPostShoppingList.PostShoppingModel
import java.util.*

class PostBuyerShopListModel {
    var totalCredits: Int = 0
    var credDitToDeduct: Double = 0.0
    var listingname: String = ""
    var category_id: String = ""
    var deliveryZoneJsonList: String = ""
    var daysTimeZoneJsonArray: String = ""
    var shoppingProductListJSonArray: String = ""
    var listId: String = ""
    var PostShoppingModel = ArrayList<PostShoppingModel>()
}