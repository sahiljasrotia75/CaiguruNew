package constant_Webservices

import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*


interface WebServices {

    //-- login /
    @FormUrlEncoded
    @POST("login")
    fun login(
        @Field("email") name: String,
        @Field("password") password: String
    ): Call<ResponseBody>

    /// -- forgotpassword /
    @FormUrlEncoded
    @POST("forgotpassword")
    fun forgotpassword(
        @Field("email") name: String
    ): Call<ResponseBody>

    /// -- check_email_valid /
    @FormUrlEncoded
    @POST("check_email_valid")
    fun check_email_valid(
        @Field("email") email: String
    ): Call<ResponseBody>

    /// -- mercandopago /
    @FormUrlEncoded
    @POST("mercadopago")
    fun mercadopago(
        @Header("Authorization") Token: String,
        @Field("amount") amount: String
    ): Call<ResponseBody>

    // -- Register /
    @FormUrlEncoded
    @POST("register")
    fun register(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String,
        @Field("social_type") social_type: String,
        @Field("social_id") social_id: String,
        @Field("referral_code") referral_code: String,
        @Field("categories") categories: String,
        @Field("image") image: String
    ): Call<ResponseBody>

    @FormUrlEncoded
    @POST("social_login")
    fun socialLogin(
        @Field("social_id") socialId: String?,
        @Field("social_type") socialType: String
    ): Call<ResponseBody>

    //-- update Image /
//    @FormUrlEncoded
//    @POST("uploadimage")
//    fun uploadimage(
//        @Field("image") image: String
//    ): Call<ResponseBody>

    @Multipart
    @POST("uploadimage")
    fun uploadimage(
        @Part image: MultipartBody.Part
    ): Call<ResponseBody>


    //-- update Image /
    @FormUrlEncoded
    @POST("post_seller_shopping_list")
    fun post_seller_shopping_list(
        @Header("Authorization") Token: String,
        @Field("list_id") list_id: String?,
        @Field("listingname") listingname: String?,
        @Field("cat_id") cat_id: String?,
        @Field("delivery_zone") delivery_zone: String?,
        @Field("delivery_days") delivery_days: String?,
        @Field("pickup_details") pickup_details: String?,
        @Field("minimum_purchase_amount") minimum_purchase_amount: String?,
        @Field("commission_per") commission_per: String,
        @Field("product_details") product_details: String,
        @Field("locale") locale: String,
        @Field("sel_cities") sel_cities: String,
        @Field("credits") creditsToDeduct: String,
        @Field("payment_methods") payment_methods: String
    ): Call<ResponseBody>

    // .......post buyer shopping list......//
    @FormUrlEncoded
    @POST("post_buyer_shopping_list")
    fun post_buyer_shopping_list(
        @Header("Authorization") Token: String,
        @Field("list_id") list_id: String?,
        @Field("listingname") listingname: String?,
        @Field("cat_id") cat_id: String?,
        @Field("delivery_location") delivery_location: String?,
        @Field("delivery_days") delivery_days: String?,
        @Field("product_details") product_details: String,
        @Field("locale") locale: String,
        @Field("credits") creditsToDeduct: String
    ): Call<ResponseBody>

    //......................switch_setting......................//
    @FormUrlEncoded
    @POST("switch_setting")
    fun switch_setting(
        @Header("Authorization") Token: String,
        @Field("switch_active") switch_active: String,
        @Field("type") type: String
    ): Call<ResponseBody>


    //* --getProfile--*//
    @FormUrlEncoded
    @POST("getprofile")
    fun getprofile(
        @Header("Authorization") Token: String,
        @Header("Accept") Accept: String,
        @Field("obj_token") obj_token: String?,
        @Field("obj_type") obj_type: String
    ): Call<ResponseBody>

    //*---get_seller_shopping_list--*//
    @FormUrlEncoded
    @POST("get_seller_shopping_list")
    fun get_seller_shopping_list(
        @Header("Authorization") Token: String,
        @Field("status") status: String,
        @Field("page") page: String//done
    ): Call<ResponseBody>

    //*---list_uploaded_by_customer--*//
    @FormUrlEncoded
    @POST("list_uploaded_by_customer")
    fun list_uploaded_by_customer(
        @Header("Authorization") Token: String,
        @Field("sel_cities") ids: String
    ): Call<ResponseBody>


    //*---logout--*//
    @POST("logout")
    fun logout(
        @Header("Authorization") Token: String
    ): Call<ResponseBody>


    //*---_seller_Close_list--*//
    @FormUrlEncoded
    @POST("closelist")
    fun closelist(
        @Header("Authorization") Token: String,
        @Field("list_id") list_id: String?
    ): Call<ResponseBody>

    //*---_open_again--*//
    @FormUrlEncoded
    @POST("open_again")
    fun open_again(
        @Header("Authorization") Token: String,
        @Field("list_id") list_id: String?
    ): Call<ResponseBody>


    //*---update_password--*//
    @FormUrlEncoded
    @POST("update_password")
    fun update_password(
        @Header("Authorization") Token: String,
        @Field("password") password: String?,
        @Field("old_password") old_password: String?
    ): Call<ResponseBody>


    //*---get_Cities--*//
    @FormUrlEncoded
    @POST("getcities")
    fun getcities(
        @Header("Authorization") Authorization: String,
        @Field("page") page: String,//done
        @Field("search") search: String?
    ): Call<ResponseBody>


    //*---update_city--*//
    @FormUrlEncoded
    @POST("update_city")
    fun update_city(
        @Header("Authorization") Authorization: String,
        @Field("sel_cities") sel_cities: String?
    ): Call<ResponseBody>

    //*---get_buyer_list--*//
    @FormUrlEncoded
    @POST("get_buyer_list")
    fun get_buyer_list(
        @Header("Authorization") Authorization: String,
        @Field("list_id") list_id: String?
    ): Call<ResponseBody>


    //*---choose_seller--*//
    @FormUrlEncoded
    @POST("choose_seller")
    fun choose_seller(
        @Header("Authorization") Authorization: String,
        @Field("self_pickup") self_pickup: String,
        @Field("home_delivery") home_delivery: String?,
        @Field("lat") lat: String?,
        @Field("long") long: String?
    ): Call<ResponseBody>

    //*---search_by_product--*//
    @FormUrlEncoded
    @POST("search_by_product")
    fun search_by_product(
        @Header("Authorization") Authorization: String,
        @Field("search") search: String,
        @Field("self_pickup") self_pickup: String,
        @Field("home_delivery") home_delivery: String?,
        @Field("lat") lat: String?,
        @Field("long") long: String?
    ): Call<ResponseBody>


    //*---seller_all_listing--*//
    @FormUrlEncoded
    @POST("seller_all_listing")
    fun seller_all_listing(
        @Header("Authorization") Authorization: String,
        @Field("seller_id") seller_id: String,
        @Field("lat") lat: String,
        @Field("long") long: String,
        @Field("search") search: String,
        @Field("cat_id") cat_id: String,
        @Field("self_pickup") self_pickup: String,
        @Field("home_delivery") home_delivery: String
    ): Call<ResponseBody>


    //*---seller_all_listing--*//
    @FormUrlEncoded
    @POST("load_more_sellers")
    fun load_more_sellers(
        @Header("Authorization") Authorization: String,
        @Field("cat_id") cat_id: String,
        @Field("self_pickup") self_pickup: String,
        @Field("home_delivery") home_delivery: String,
        @Field("lat") lat: String,
        @Field("long") long: String
    ): Call<ResponseBody>

    //*---seller_status--*//
    @FormUrlEncoded
    @POST("seller_status")
    fun seller_status(
        @Header("Authorization") Authorization: String,
        @Field("seller_active_status") seller_active_status: String
    ): Call<ResponseBody>


    //*---request_buyer_list--*//
    @FormUrlEncoded
    @POST("request_buyer_list")
    fun request_buyer_list(
        @Header("Authorization") Authorization: String,
        @Field("list_id") list_id: String,
        @Field("buyer_id") buyer_id: String,
        @Field("products") products: String,
        @Field("total_price") total_price: String,
        @Field("comission_per") comission_per: String,
        @Field("payment_methods") payment_methods: String
    ): Call<ResponseBody>


    //*-------update_address-------*//

    @FormUrlEncoded
    @POST("update_address")
    fun update_address(
        @Header("Authorization") Authorization: String,
        @Field("full_address") full_address: String,
        @Field("lat") lat: String,
        @Field("long") long: String?
    ): Call<ResponseBody>


    //*-------update_address-------*//

    @FormUrlEncoded
    @POST("buy_listing")
    fun buy_listing(
        @Header("Authorization") Authorization: String,
        @Field("seller_id") seller_id: String,
        @Field("listing_details") listing_details: String,
        @Field("total_credits") total_credits: String,
        @Field("address") address: String
    ): Call<ResponseBody>

    //*-------suggest_products-------*//

    @FormUrlEncoded
    @POST("suggest_products")
    fun suggest_products(
        @Header("Authorization") Authorization: String,
        @Field("req_ids") req_ids: String,
        @Field("suggest_products") suggest_products: String
    ): Call<ResponseBody>


    //*-------update_categories-------*//
    @FormUrlEncoded
    @POST("update_categories")
    fun update_categories(
        @Header("Authorization") Authorization: String,
        @Field("categories") categories: String

    ): Call<ResponseBody>

    //*-------load_more_buyer_list-------*//
    @FormUrlEncoded
    @POST("load_more_buyer_list")
    fun load_more_buyer_list(
        @Header("Authorization") Authorization: String,
        @Field("cat_id") cat_id: String

    ): Call<ResponseBody>

    //*-------get_all_close_list-------*//
    @FormUrlEncoded
    @POST("get_all_close_list")
    fun get_all_close_list(
        @Header("Authorization") Authorization: String,
        @Field("cat_id") cat_id: String,
        @Field("page") page: String//done

    ): Call<ResponseBody>


    //*-------update_profile-------*//

    @FormUrlEncoded
    @POST("update_profile")
    fun update_profile(
        @Header("Authorization") Authorization: String,
        @Field("name") name: String,
        @Field("image") image: String,
        @Field("email") email: String,
        @Field("categories") categories: String,
        @Field("full_address") full_address: String,
        @Field("lat") lat: String,
        @Field("long") long: String

    ): Call<ResponseBody>


    //*-------update_wallet-------*//

    @FormUrlEncoded
    @POST("update_wallet")
    fun update_wallet(
        @Header("Authorization") Authorization: String,
        @Field("amount") amount: String,
        @Field("credits") credits: String,
        @Field("response") response: String
    ): Call<ResponseBody>


    //*-------get_seller_requests-------*//
    @FormUrlEncoded
    @POST("get_seller_requests")
    fun get_seller_requests(
        @Header("Authorization") Authorization: String,
        @Field("status") status: String,
        @Field("page") page: String,//Done
        @Field("list_type") list_type: String
    ): Call<ResponseBody>

    //*-------your_shopping_lists-------*//
    @FormUrlEncoded
    @POST("your_shopping_lists")
    fun your_shopping_lists(
        @Header("Authorization") Authorization: String,
        @Field("page") page: String//done
    ): Call<ResponseBody>


    //*-------get_request_detail-------*//

    @FormUrlEncoded
    @POST("get_request_detail")
    fun get_request_detail(
        @Header("Authorization") Authorization: String,
        @Field("req_id") req_id: String,
        @Field("list_type") list_type: String
    ): Call<ResponseBody>

    //*-------earned_by_reffer-------*//
    @FormUrlEncoded
    @POST("earned_by_reffer")
    fun earned_by_reffer(
        @Header("Authorization") Authorization: String,
        @Field("") abc: String
    ): Call<ResponseBody>

    //*-------earned_by_listing-------*//
    @FormUrlEncoded
    @POST("earned_by_listing")
    fun earned_by_listing(
        @Header("Authorization") Authorization: String,
        @Field("page") page: String//done
    ): Call<ResponseBody>


    //*-------change_request_status-------*//
    @FormUrlEncoded
    @POST("change_request_status")
    fun change_request_status(
        @Header("Authorization") Authorization: String,
        @Field("req_id") req_id: String,
        @Field("status") status: String,
        @Field("list_type") list_type: String,
        @Field("reason") reason: String

    ): Call<ResponseBody>


    //*-------change_buyer_request_status-------*//
    @FormUrlEncoded
    @POST("change_buyer_request_status")
    fun change_buyer_request_status(
        @Header("Authorization") Authorization: String,
        @Field("req_id") req_id: String,
        @Field("status") status: String
    ): Call<ResponseBody>


    //*-------list_all_request-------*//
    @FormUrlEncoded
    @POST("list_all_request")
    fun list_all_request(
        @Header("Authorization") Authorization: String,
        @Field("page") page: String,//done
        @Field("list_id") list_id: String
    ): Call<ResponseBody>


    //*-------get_buyer_request_detail-------*//
    @FormUrlEncoded
    @POST("get_buyer_request_detail")
    fun get_buyer_request_detail(
        @Header("Authorization") Authorization: String,
        @Field("req_id") req_id: String
    ): Call<ResponseBody>


    //*-------getotherprofile-------*//
    @FormUrlEncoded
    @POST("getotherprofile")
    fun getotherprofile(
        @Header("Authorization") Authorization: String,
        @Field("user_id") user_id: String,
        @Field("type") type: String
    ): Call<ResponseBody>


    //*-------getotherprofile-------*//
    @FormUrlEncoded
    @POST("follow_unfollow")
    fun follow_unfollow(
        @Header("Authorization") Authorization: String,
        @Field("user_id") user_id: String,
        @Field("follow_status") type: String
    ): Call<ResponseBody>


    //*-------get_follow_data-------*//
    @FormUrlEncoded
    @POST("get_follow_data")
    fun get_follow_data(
        @Header("Authorization") Authorization: String,
        @Field("type") type: String,
        @Field("page") page: String//done
    ): Call<ResponseBody>


    /************************************************************
     * Chatting webservices here
     ************************************************************/
    @FormUrlEncoded
    @POST("get_chat_channel")
    fun getChatChannels(
        @Header("Authorization") Authorization: String,
        @Field("type") userType: String
    ): Call<ResponseBody>

    @FormUrlEncoded
    @POST("get_archived_chat_channel")
    fun getArchiveChatChannels(
        @Header("Authorization") Authorization: String,
        @Field("type") userType: String
    ): Call<ResponseBody>


    @FormUrlEncoded
    @POST("get_messages")
    fun getChatMessages(
        @Header("Authorization") Authorization: String,
        @Field("channel_id") channelId: String,
        @Field("last_id") lastId: String
    ): Call<ResponseBody>

    @FormUrlEncoded
    @POST("get_bg_messages")
    fun get_bg_messages(
        @Header("Authorization") Authorization: String,
        @Field("channel_id") channelId: String,
        @Field("last_id") lastId: String
    ): Call<ResponseBody>

    @FormUrlEncoded
    @POST("send_message")
    fun sendMessage(
        @Header("Authorization") Authorization: String,
        @Field("channel_id") channelId: String,
        @Field("message") message: String,
        @Field("image") image: String,
        @Field("last_id") lastId: String
    ): Call<ResponseBody>

    //***********my order********//
    @FormUrlEncoded
    @POST("myorders")
    fun myorders(
        @Header("Authorization") Authorization: String,
        @Field("page") page: String//done
    ): Call<ResponseBody>

    //***********myorderdetail********//
    @FormUrlEncoded
    @POST("myorderdetail")
    fun myorderdetail(
        @Header("Authorization") Authorization: String,
        @Field("req_id") req_id: String,
        @Field("list_type") list_type: String
    ): Call<ResponseBody>

    //************get_notifications**********//
    @FormUrlEncoded
    @POST("get_notifications")
    fun get_notifications(
        @Header("Authorization") Authorization: String,
        @Field("page") page: String//done
    ): Call<ResponseBody>

    //************get_notifications**********//
    @FormUrlEncoded
    @POST("notification_read")
    fun notification_read(
        @Header("Authorization") Authorization: String,
        @Field("notification_id") page: String
    ): Call<ResponseBody>

    //************get_homepage_notifications**********//
    @FormUrlEncoded
    @POST("get_homepage_notifications")
    fun get_homepage_notifications(
        @Header("Authorization") Authorization: String,
        @Field("check") empty: String
    ): Call<ResponseBody>


    //************get_seller_homepage_notifications**********//
    @FormUrlEncoded
    @POST("get_seller_homepage_notifications")
    fun get_seller_homepage_notifications(
        @Header("Authorization") Authorization: String,
        @Field("check") empty: String
    ): Call<ResponseBody>

    //************change_buyer_confirmation_status**********//
    @FormUrlEncoded
    @POST("change_buyer_confirmation_status")
    fun change_buyer_confirmation_status(
        @Header("Authorization") Authorization: String,
        @Field("req_id") req_id: String,
        @Field("status") status: String,
        @Field("list_type") list_type: String,
        @Field("rating") rating: String,
        @Field("comment") comment: String,
        @Field("contact_name") contact_name: String,
        @Field("contact_number") contact_number: String
    ): Call<ResponseBody>

    //************get_dispute_detail**********//
    @FormUrlEncoded
    @POST("get_dispute_detail")
    fun get_dispute_detail(
        @Header("Authorization") Authorization: String,
        @Field("req_id") req_id: String,
        @Field("list_type") list_type: String
    ): Call<ResponseBody>

    //************seller_action_on_dispute**********//
    @FormUrlEncoded
    @POST("seller_action_on_dispute")
    fun seller_action_on_dispute(
        @Header("Authorization") Authorization: String,
        @Field("req_id") req_id: String,
        @Field("list_type") list_type: String,
        @Field("status") status: String,
        @Field("refute_reason") refute_reason: String
    ): Call<ResponseBody>

    //************rate_to_buyer**********//
    @FormUrlEncoded
    @POST("rate_to_buyer")
    fun rate_to_buyer(
        @Header("Authorization") Authorization: String,
        @Field("req_id") req_id: String,
        @Field("list_type") list_type: String,
        @Field("rating") rating: String,
        @Field("comment") comment: String
    ): Call<ResponseBody>

    //************seller_hompage**********//
    @FormUrlEncoded
    @POST("seller_hompage")
    fun seller_hompage(
        @Header("Authorization") Authorization: String,
        @Field("page") page: String//done page
    ): Call<ResponseBody>

    //************seller_hompage**********//
    @FormUrlEncoded
    @POST("buyer_hompage")
    fun buyer_hompage(
        @Header("Authorization") Authorization: String,
        @Field("page") page: String,//done
        @Field("lat") lat: String,
        @Field("long") long: String
    ): Call<ResponseBody>

    //************seller_hompage**********//
    @FormUrlEncoded
    @POST("buyer_feed_like_dislike")
    fun buyer_feed_like_dislike(
        @Header("Authorization") Authorization: String,
        @Field("post_id") post_id: String,
        @Field("like") like: String
    ): Call<ResponseBody>

    //************get_feed_comments**********//
    @FormUrlEncoded
    @POST("get_feed_comments")
    fun get_feed_comments(
        @Header("Authorization") Authorization: String,
        @Field("post_id") post_id: String,
        @Field("page") page: String//done
    ): Call<ResponseBody>

    //************get_feed_comments**********//
    @FormUrlEncoded
    @POST("set_feed_comments")
    fun set_feed_comments(
        @Header("Authorization") Authorization: String,
        @Field("post_id") post_id: String,
        @Field("comment") comment: String
    ): Call<ResponseBody>

    //************get_tag_users**********//
    @FormUrlEncoded
    @POST("get_tag_users")
    fun get_tag_users(
        @Header("Authorization") Authorization: String,
        @Field("page") page: String,//done
        @Field("search") search: String,
        @Field("post_id") post_id: String
    ): Call<ResponseBody>


    //************post_tag_user**********//
    @FormUrlEncoded
    @POST("post_tag_user")
    fun post_tag_user(
        @Header("Authorization") Authorization: String,
        @Field("post_id") post_id: String,
        @Field("tag_to") tag_to: String,
        @Field("status") status: String
    ): Call<ResponseBody>


    //************buyer_feed_view**********//
    @FormUrlEncoded
    @POST("buyer_feed_view")
    fun buyer_feed_view(
        @Header("Authorization") Authorization: String,
        @Field("lat") lat: String,
        @Field("long") long: String,
        @Field("address") address: String,
        @Field("post_id") post_id: String,
        @Field("list_id") list_id: String
    ): Call<ResponseBody>

    //************cash_request**********//
    @FormUrlEncoded
    @POST("cash_request")
    fun cash_request(
        @Header("Authorization") Authorization: String,
        @Field("credits") credits: String
    ): Call<ResponseBody>


    //************buyer_delete_shopping_list**********//
    @FormUrlEncoded
    @POST("buyer_delete_shopping_list")
    fun buyer_delete_shopping_list(
        @Header("Authorization") Authorization: String,
        @Field("list_id") list_id: String
    ): Call<ResponseBody>

    //************get_shared_users**********//
    @FormUrlEncoded
    @POST("get_shared_users")
    fun get_shared_users(
        @Header("Authorization") Authorization: String,
        @Field("post_id") post_id: String,
        @Field("shared_by") shared_by: String
    ): Call<ResponseBody>

    //************rate_to_seller**********//
    @FormUrlEncoded
    @POST("rate_to_seller")
    fun rate_to_seller(
        @Header("Authorization") Authorization: String,
        @Field("req_id") req_id: String,
        @Field("list_type") list_type: String,
        @Field("rating") rating: String,
        @Field("comment") comment: String
    ): Call<ResponseBody>

    //************block_unblock_user**********//
    @FormUrlEncoded
    @POST("block_unblock_user")
    fun block_unblock_user(
        @Header("Authorization") Authorization: String,
        @Field("block_user_id") block_user_id: String,
        @Field("type") type: String,
        @Field("status") status: String
    ): Call<ResponseBody>

    //************get_block_users**********//
    @GET("get_block_users")
    fun get_block_users(
        @Header("Authorization") Authorization: String,
        @Header("Accept") Accept: String,
        @Query("page") page: String//done
    ): Call<ResponseBody>

    //************block_unblock_user**********//
    @FormUrlEncoded
    @POST("report_list")
    fun report_list(
        @Header("Authorization") Authorization: String,
        @Header("Accept") Accept: String,
        @Field("list_id") list_id: String,
        @Field("list_type") list_type: String,
        @Field("reason") reason: String
    ): Call<ResponseBody>

    //************get_block_users**********//
    @GET("updatepushtopics")
    fun updatepushtopics(
        @Header("Authorization") Authorization: String
    ): Call<ResponseBody>

    //***********buyer_feed_filter***********//
    @FormUrlEncoded
    @POST("buyer_feed_filter")
    fun buyer_feed_filter(
        @Header("Authorization") Authorization: String,
        @Field("page") page: String,
        @Field("lat") lat: String,
        @Field("long") long: String,
        @Field("search_type") search_type: String,
        @Field("search") search: String
    ): Call<ResponseBody>


    //***********getsavedproducts***********//
    @FormUrlEncoded
    @POST("getsavedproducts")
    fun getsavedproducts(
        @Header("Authorization") Authorization: String,
        @Field("cat_id") cat_id: String,
        @Field("search") search: String,
        @Field("page") page: String
    ): Call<ResponseBody>

    //***********pauseplayproduct***********//
    @FormUrlEncoded
    @POST("pauseplayproduct")
    fun pauseplayproduct(
        @Header("Authorization") Authorization: String,
        @Field("list_id") list_id: String, //id
        @Field("product_id") product_id: String,
        @Field("status") status: String  //"1: Play 2:Pause"
    ): Call<ResponseBody>

    //***********pauseplayproduct***********//
    @FormUrlEncoded
    @POST("get_buyer_requests")
    fun get_buyer_requests(
        @Header("Authorization") Authorization: String,
        @Field("status") status: String, //1:Pending, 2:Approved, 3:Rejected, 4  Finalize  5:Completed
        @Field("page") page: String,
        @Field("list_type") list_type: String  //"In Response: list_type 1:Seller, 2:Buyer
    ): Call<ResponseBody>

    //***********getsellerstore***********//
    @FormUrlEncoded
    @POST("getsellerstore")
    fun getsellerstore(
        @Header("Authorization") Authorization: String,
        @Field("seller_id") seller_id: String,
        @Field("lat") lat: String,
        @Field("long") long: String,
        @Field("cat_id") cat_id: String,
        @Field("sort_by") sort_by: String
    ): Call<ResponseBody>


    //***********getsellerstore***********//
    @FormUrlEncoded
    @POST("search_store_products")
    fun search_store_products(
        @Header("Authorization") Authorization: String,
        @Field("seller_id") seller_id: String,
        @Field("lat") lat: String,
        @Field("long") long: String,
        @Field("search") search: String,
        @Field("sort_by") sort_by: String,
        @Field("usedProds") usedProds: String,
        @Field("page") page: String
    ): Call<ResponseBody>

    //***********load_more_store_products***********//
    @FormUrlEncoded
    @POST("load_more_store_products")
    fun load_more_store_products(
        @Header("Authorization") Authorization: String,
        @Field("seller_id") seller_id: String,
        @Field("lat") lat: String,
        @Field("long") long: String,
        @Field("cat_id") cat_id: String,
        @Field("sort_by") sort_by: String,
        @Field("usedProds") usedProds: String,
        @Field("page") page: String
    ): Call<ResponseBody>

    //***********buy_custom_list***********//
    @FormUrlEncoded
    @POST("buy_custom_list")
    fun buy_custom_list(
        @Header("Authorization") Authorization: String,
        @Field("seller_id") seller_id: String,
        @Field("products") products: String,
        @Field("total_credits") total_credits: String,
        @Field("address") address: String,
        @Field("payment_methods") payment_methods: String
    ): Call<ResponseBody>


    //***********cancel_status***********//
    @FormUrlEncoded
    @POST("cancel_status")
    fun cancel_status(
        @Header("Authorization") Authorization: String,
        @Field("req_id") req_id: String,
        @Field("list_type") list_type: String
    ): Call<ResponseBody>

    //***********save_seller_cart***********//
    @FormUrlEncoded
    @POST("save_seller_cart")
    fun save_seller_cart(
        @Header("Authorization") Authorization: String,
        @Field("seller_id") seller_id: String,
        @Field("products") products: String
    ): Call<ResponseBody>


    //***********get_seller_cart***********//
    @FormUrlEncoded
    @POST("get_seller_cart")
    fun get_seller_cart(
        @Header("Authorization") Authorization: String,
        @Field("seller_id") seller_id: String
    ): Call<ResponseBody>

    //***********get_seller_cart***********//
    @GET("clear_cart")
    fun clear_cart(
        @Header("Authorization") Authorization: String
    ): Call<ResponseBody>
}