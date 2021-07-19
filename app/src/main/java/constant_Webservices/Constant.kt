package constant_Webservices

import android.Manifest
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.Matrix
import android.graphics.drawable.ColorDrawable
import android.location.Address
import android.location.Geocoder
import android.media.ExifInterface
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.text.TextUtils
import android.util.Base64
import android.util.Log
import android.util.Patterns
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.PurchasesUpdatedListener
import com.example.caiguru.R
import com.example.caiguru.commonScreens.commonNotifications.rateSeller.SellerRateModel
import com.example.caiguru.commonScreens.dashBoardParentActivity.searchProductOrSeller.SearchModel
import com.example.caiguru.commonScreens.registerCategory.CategoryModel
import com.example.caiguru.seller.homeSeller.GetProfileModel
import com.example.caiguru.seller.sellerSelectedDaysAndTime.DialogDaysModel
import com.example.caiguru.seller.shoppingListSellers.sellerPostShoppingList.paymentMethod.PaymentMethodModel
import com.facebook.AccessToken
import com.facebook.GraphRequest
import com.facebook.HttpMethod
import com.facebook.login.LoginManager
import com.google.android.gms.maps.model.LatLng
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.minimum_product_dialog.Ok
import kotlinx.android.synthetic.main.show_total_credits_dialog.*
import retrofit2.Retrofit
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.math.BigDecimal
import java.net.URLEncoder
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class Constant {


    companion object {
        val is_has_cart: String = "is_has_cart"
        val isDescriptionAlert1: String = "isDescriptionAlert1"
        val firstTimeOpenScreen: String = "firstTimeOpenScreen"
        val isDescriptionAlert2: String = "isDescriptionAlert2"
        var startFirstTime: Boolean = true
        var stopBacgroundTimer = 0
        val SearchNameKey: String = ""

        //  var whichSideScreenOpen: String = "buyerSide"
        var UpdateAddressInBackGround: String = "0"
        var apiHitOrNot: Int = 0
        const val PROFILE: String = "profile"
        const val Address: String = "Address"

        // const val BASE_URL = "https://appmanagement.caiguru.com/public/api/" //live

    // const val BASE_URL = "http://138.68.13.10/public/api/"//dev

      //   const val BASE_URL = "http://138.68.13.10/public/api/v1/"//test base url
        const val BASE_URL = "https://appmanagement.caiguru.com/public/api/v1/"//latest live base url
        val token: String = "Token"
        val type: String = "type"

    // const val PUBLIC_KEY = "TEST-09397867-0e96-4123-86e0-23e2c51802ab" //dev
      const val PUBLIC_KEY = "APP_USR-27bd800c-70e0-4bdb-810d-fd0986593fae" //live mercado

        //                const val ACCESS_TOKEN =
//            "TEST-687563904821730-092514-7227b4e0ae8fd9db2f578b50978ee2cb-128631035"
        const val ACCESS_TOKEN = "APP_USR-687563904821730-092514-abe5617424c1911f35dd6c9665a36111-128631035"
        var seller_active_status: String = "seller_active_status"
        var switch_active: String = "switch_active"
        var sel_cities: String = "sel_cities"
        var retrofit: Retrofit? = null
        private var billingClient: BillingClient? = null
        var hideCreditConvert: Boolean = false
        //----------------------------------shared Prefrence-------------------------------//

        fun getPrefs(context: Context): SharedPreferences {
            return context.getSharedPreferences("Login", Context.MODE_PRIVATE)//table name
        }


        fun hideSoftKeyboard(view: View, context: Context) {
            val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, InputMethodManager.RESULT_UNCHANGED_SHOWN)
        }

        //Email validate code
        fun isValidEmailId(email: String): Boolean {
            return (!TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches())
        }

        //getlocal
        fun getLocal(context: Context): String {
            return Locale.getDefault().language
        }

        //type
        fun SellerLevel(context: Context): ArrayList<SellerLevelModel> {
            val arraydata = ArrayList<SellerLevelModel>()
            var model = SellerLevelModel()
            model.levelType = "1"
            model.levelImage = R.drawable.ic_daydreamer
            model.levelname = context.getString(R.string.Daydreamer)
            arraydata.add(model)

            model = SellerLevelModel()
            model.levelType = "2"
            model.levelImage = R.drawable.ic_visionary
            model.levelname = context.getString(R.string.Visionary)
            arraydata.add(model)

            model = SellerLevelModel()
            model.levelType = "3"
            model.levelImage = R.drawable.ic_expert
            model.levelname = context.getString(R.string.Expert)
            arraydata.add(model)

            model = SellerLevelModel()
            model.levelType = "4"
            model.levelImage = R.drawable.ic_legend
            model.levelname = context.getString(R.string.Legend)
            arraydata.add(model)

            return arraydata
        }


        fun BuyerLevel(context: Context): ArrayList<BuyerLevelModel> {
            val arraydata = ArrayList<BuyerLevelModel>()
            var model = BuyerLevelModel()
            model.levelType = "1"
            model.levelImage = R.drawable.ic_buyer_level1
            model.levelname = context.getString(R.string.Rookie)
            arraydata.add(model)

            model = BuyerLevelModel()
            model.levelType = "2"
            model.levelImage = R.drawable.ic_buyer_level_2
            model.levelname = context.getString(R.string.Regular)
            arraydata.add(model)


            model = BuyerLevelModel()
            model.levelType = "3"
            model.levelImage = R.drawable.ic_buyer_level_3
            model.levelname = context.getString(R.string.Amateur)
            arraydata.add(model)

            model = BuyerLevelModel()
            model.levelType = "4"
            model.levelImage = R.drawable.ic_buyer_level_4
            model.levelname = context.getString(R.string.Advanced)
            arraydata.add(model)


            model = BuyerLevelModel()
            model.levelType = "5"
            model.levelImage = R.drawable.ic_buyer_level_5
            model.levelname = context.getString(R.string.Veteran)
            arraydata.add(model)
            return arraydata

        }


        //category
        fun categoryData(context: Context): ArrayList<CategoryModel> {
            val arraydata = ArrayList<CategoryModel>()
            var model = CategoryModel()
            model.name = context.getString(R.string.babies)
            model.imagewhite = R.drawable.ic_babies_white
            model.imageyellow = R.drawable.ic_babies_yellow
            model.category_id = "1"
            arraydata.add(model)

            model = CategoryModel()
            model.name = context.getString(R.string.Bakery)
            model.imagewhite = R.drawable.ic_bakery_white
            model.imageyellow = R.drawable.ic_bakery_yellow
            model.category_id = "2"
            arraydata.add(model)

            model = CategoryModel()
            model.name = context.getString(R.string.Cleaning_supplies)
            model.imagewhite = R.drawable.ic_cleaning_supplies_white
            model.imageyellow = R.drawable.ic_cleaning_supplies_yellow
            model.category_id = "3"
            arraydata.add(model)

            model = CategoryModel()
            model.name = context.getString(R.string.Clothes)
            model.imagewhite = R.drawable.ic_clothes_white
            model.imageyellow = R.drawable.ic_clothes_yellow
            model.category_id = "4"
            arraydata.add(model)

            model = CategoryModel()
            model.name = context.getString(R.string.Cold_cuts_dairy)
            model.imagewhite = R.drawable.ic_cold_cuts_n_dairy_white
            model.imageyellow = R.drawable.ic_cold_cuts_n_dairy_yellow
            model.category_id = "5"
            arraydata.add(model)

            model = CategoryModel()
            model.name = context.getString(R.string.Condiments_oils_sauces)
            model.imagewhite = R.drawable.ic_condiments_oils_n_sauces_white
            model.imageyellow = R.drawable.ic_condiments_oils_n_sauces
            model.category_id = "6"
            arraydata.add(model)

            model = CategoryModel()
            model.name = context.getString(R.string.Construction)
            model.imagewhite = R.drawable.ic_construction_white
            model.imageyellow = R.drawable.ic_construction_yellow
            model.category_id = "7"
            arraydata.add(model)

            model = CategoryModel()
            model.name = context.getString(R.string.Drinks)
            model.imagewhite = R.drawable.ic_alcoholic_drinks_white
            model.imageyellow = R.drawable.ic_alcoholic_drinks_yellow
            model.category_id = "8"
            arraydata.add(model)

            model = CategoryModel()
            model.name = context.getString(R.string.Fruits_vegetables)
            model.imagewhite = R.drawable.ic_fruits_n_vegetables_white
            model.imageyellow = R.drawable.ic_fruits_n_vegetables_yellow
            model.category_id = "9"
            arraydata.add(model)

            model = CategoryModel()
            model.name = context.getString(R.string.gluten_free_products)
            model.imagewhite = R.drawable.ic_gluten_free_products_white
            model.imageyellow = R.drawable.ic_gluten_free_products_yellow
            model.category_id = "10"
            arraydata.add(model)

            model = CategoryModel()
            model.name = context.getString(R.string.Grocery)
            model.imagewhite = R.drawable.ic_grocery_white
            model.imageyellow = R.drawable.ic_grocery_yellow
            model.category_id = "11"
            arraydata.add(model)

            model = CategoryModel()
            model.name = context.getString(R.string.Hardware_store)
            model.imagewhite = R.drawable.ic_hardware_store_white
            model.imageyellow = R.drawable.ic_hardware_store_yellow
            model.category_id = "12"

            arraydata.add(model)

            model = CategoryModel()
            model.name = context.getString(R.string.Health_beauty)
            model.imagewhite = R.drawable.ic_health_n_beauty_white
            model.imageyellow = R.drawable.ic_health_n_beauty_yellow
            model.category_id = "13"
            arraydata.add(model)

            model = CategoryModel()
            model.name = context.getString(R.string.health_natural_food)
            model.imagewhite = R.drawable.ic_healthy_natural_food_white
            model.imageyellow = R.drawable.ic_healthy_natural_food_yellow
            model.category_id = "14"
            arraydata.add(model)

            model = CategoryModel()
            model.name = context.getString(R.string.Home_kitchen)
            model.imagewhite = R.drawable.ic_home_n_kitchen_white
            model.imageyellow = R.drawable.ic_home_n_kitchen_yellow
            model.category_id = "15"
            arraydata.add(model)

            model = CategoryModel()
            model.name = context.getString(R.string.Meat_chicken_fish)
            model.imagewhite = R.drawable.ic_meat_chicken_n_fish_white
            model.imageyellow = R.drawable.ic_meat_chicken_n_fish_yellow
            model.category_id = "16"
            arraydata.add(model)


            model = CategoryModel()
            model.name = context.getString(R.string.Office_stationery)
            model.imagewhite = R.drawable.ic_office_n_stationary_white
            model.imageyellow = R.drawable.ic_office_n_stationary_yellow
            model.category_id = "17"
            arraydata.add(model)


            model = CategoryModel()
            model.name = context.getString(R.string.Pets)
            model.imagewhite = R.drawable.ic_pets_white
            model.imageyellow = R.drawable.ic_pets_yellow
            model.category_id = "18"
            arraydata.add(model)

            model = CategoryModel()
            model.name = context.getString(R.string.Pharmacy)
            model.imagewhite = R.drawable.ic_pharmacy_white
            model.imageyellow = R.drawable.ic_pharmacy_yellow
            model.category_id = "19"
            arraydata.add(model)


            model = CategoryModel()
            model.name = context.getString(R.string.Restaurants)
            model.imagewhite = R.drawable.ic_restauran_white
            model.imageyellow = R.drawable.ic_restauran_yellow
            model.category_id = "20"
            arraydata.add(model)


            model = CategoryModel()
            model.name = context.getString(R.string.Sports_fitness)
            model.imagewhite = R.drawable.ic_sports_n_fitness_white
            model.imageyellow = R.drawable.ic_sports_n_fitness_yellow
            model.category_id = "21"
            arraydata.add(model)

            model = CategoryModel()
            model.name = context.getString(R.string.Sweets_snacks)
            model.imagewhite = R.drawable.ic_sweets_n_snacks_white
            model.imageyellow = R.drawable.ic_sweets_n_snacks_yellow
            model.category_id = "22"
            arraydata.add(model)

            model = CategoryModel()
            model.name = context.getString(R.string.Toys_kids)
            model.imagewhite = R.drawable.ic_toys_n_kids_white
            model.imageyellow = R.drawable.ic_toys_n_kids_yellow
            model.category_id = "23"
            arraydata.add(model)

            model = CategoryModel()
            model.name = context.getString(R.string.Vegan)
            model.imagewhite = R.drawable.ic_vegan_white
            model.imageyellow = R.drawable.ic_vegan_yellow
            model.category_id = "24"
            arraydata.add(model)

            return arraydata
        }

        fun getRestClient(): Retrofit {
            if (retrofit == null) {
                retrofit = Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .build()
            }
            return retrofit!!
        }


        //category
        fun categoryData1(context: Context): ArrayList<CategoryModel> {
            val arraydata = ArrayList<CategoryModel>()

            var model = CategoryModel()
            model.name = context.getString(R.string.babies)
            model.imagewhite = R.drawable.ic_babies_white
            model.imageyellow = R.drawable.ic_babies_purple
            model.category_id = "1"
            arraydata.add(model)

            model = CategoryModel()
            model.name = context.getString(R.string.Bakery)
            model.imagewhite = R.drawable.ic_bakery_white
            model.imageyellow = R.drawable.ic_bakery_purple
            model.category_id = "2"
            arraydata.add(model)

            model = CategoryModel()
            model.name = context.getString(R.string.Cleaning_supplies)
            model.imagewhite = R.drawable.ic_cleaning_supplies_white
            model.imageyellow = R.drawable.ic_cleaning_supplies_purple
            model.category_id = "3"
            arraydata.add(model)

            model = CategoryModel()
            model.name = context.getString(R.string.Clothes)
            model.imagewhite = R.drawable.ic_clothes_white
            model.imageyellow = R.drawable.ic_clothes_purple
            model.category_id = "4"
            arraydata.add(model)

            model = CategoryModel()
            model.name = context.getString(R.string.Cold_cuts_dairy)
            model.imagewhite = R.drawable.ic_cold_cuts_n_dairy_white
            model.imageyellow = R.drawable.ic_cold_cuts_n_dairy_purple
            model.category_id = "5"
            arraydata.add(model)

            model = CategoryModel()
            model.name = context.getString(R.string.Condiments_oils_sauces)
            model.imagewhite = R.drawable.ic_condiments_oils_n_sauces_white
            model.imageyellow = R.drawable.ic_condiments__oils_n_sauces_purple
            model.category_id = "6"
            arraydata.add(model)

            model = CategoryModel()
            model.name = context.getString(R.string.Construction)
            model.imagewhite = R.drawable.ic_construction_white
            model.imageyellow = R.drawable.ic_construction_purple
            model.category_id = "7"
            arraydata.add(model)

            model = CategoryModel()
            model.name = context.getString(R.string.Drinks)
            model.imagewhite = R.drawable.ic_alcoholic_drinks_white
            model.imageyellow = R.drawable.ic_alcoholic_drinks_purple
            model.category_id = "8"
            arraydata.add(model)

            model = CategoryModel()
            model.name = context.getString(R.string.Fruits_vegetables)
            model.imagewhite = R.drawable.ic_fruits_n_vegetables_white
            model.imageyellow = R.drawable.ic_fruits_n_vegetables_purple
            model.category_id = "9"
            arraydata.add(model)

            model = CategoryModel()
            model.name = context.getString(R.string.gluten_free_products)
            model.imagewhite = R.drawable.ic_gluten_free_products_white
            model.imageyellow = R.drawable.ic_gluten_free_products_purple
            model.category_id = "10"
            arraydata.add(model)

            model = CategoryModel()
            model.name = context.getString(R.string.Grocery)
            model.imagewhite = R.drawable.ic_grocery_white
            model.imageyellow = R.drawable.ic_grocery_purple
            model.category_id = "11"
            arraydata.add(model)

            model = CategoryModel()
            model.name = context.getString(R.string.Hardware_store)
            model.imagewhite = R.drawable.ic_hardware_store_white
            model.imageyellow = R.drawable.ic_hardware_store_purple
            model.category_id = "12"
            arraydata.add(model)

            model = CategoryModel()
            model.name = context.getString(R.string.Health_beauty)
            model.imagewhite = R.drawable.ic_health_n_beauty_white
            model.imageyellow = R.drawable.ic_health_n_beauty_purple
            model.category_id = "13"
            arraydata.add(model)

            model = CategoryModel()
            model.name = context.getString(R.string.health_natural_food)
            model.imagewhite = R.drawable.ic_healthy_natural_food_white
            model.imageyellow = R.drawable.ic_healthy_natural_food_purple
            model.category_id = "14"
            arraydata.add(model)

            model = CategoryModel()
            model.name = context.getString(R.string.Home_kitchen)
            model.imagewhite = R.drawable.ic_home_n_kitchen_white
            model.imageyellow = R.drawable.ic_home_n_kitchen_purple
            model.category_id = "15"
            arraydata.add(model)

            model = CategoryModel()
            model.name = context.getString(R.string.Meat_chicken_fish)
            model.imagewhite = R.drawable.ic_meat_chicken_n_fish_white
            model.imageyellow = R.drawable.ic_meat_chicken_n_fish_purple
            model.category_id = "16"
            arraydata.add(model)

            model = CategoryModel()
            model.name = context.getString(R.string.Office_stationery)
            model.imagewhite = R.drawable.ic_office_n_stationary_white
            model.imageyellow = R.drawable.ic_office_n_stationary_purple
            model.category_id = "17"
            arraydata.add(model)
            model = CategoryModel()
            model.name = context.getString(R.string.Pets)
            model.imagewhite = R.drawable.ic_pets_white
            model.imageyellow = R.drawable.ic_pets_purple
            model.category_id = "18"
            arraydata.add(model)

            model = CategoryModel()
            model.name = context.getString(R.string.Pharmacy)
            model.imagewhite = R.drawable.ic_pharmacy_white
            model.imageyellow = R.drawable.ic_pharmacy_purple
            model.category_id = "19"
            arraydata.add(model)

            model = CategoryModel()
            model.name = context.getString(R.string.Restaurants)
            model.imagewhite = R.drawable.ic_restauran_white
            model.imageyellow = R.drawable.ic_restaurent_purple
            model.category_id = "20"
            arraydata.add(model)

            model = CategoryModel()
            model.name = context.getString(R.string.Sports_fitness)
            model.imagewhite = R.drawable.ic_sports_n_fitness_white
            model.imageyellow = R.drawable.ic_sports_n_fitness_purple
            model.category_id = "21"
            arraydata.add(model)

            model = CategoryModel()
            model.name = context.getString(R.string.Sweets_snacks)
            model.imagewhite = R.drawable.ic_sweets_n_snacks_white
            model.imageyellow = R.drawable.ic_sweets_n_snacks_purple
            model.category_id = "22"
            arraydata.add(model)

            model = CategoryModel()
            model.name = context.getString(R.string.Toys_kids)
            model.imagewhite = R.drawable.ic_toys_n_kids_white
            model.imageyellow = R.drawable.ic_toys_n_kids_purple
            model.category_id = "23"
            arraydata.add(model)

            model = CategoryModel()
            model.name = context.getString(R.string.Vegan)
            model.imagewhite = R.drawable.ic_vegan_white
            model.imageyellow = R.drawable.ic_vegan_purple
            model.category_id = "24"
            arraydata.add(model)

            return arraydata
        }


        fun orderdateTimeFormat(createdAt: String): String {
            val converter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US)
            converter.timeZone = TimeZone.getTimeZone("UTC")
            val date = converter.parse(createdAt)
            val formatter = SimpleDateFormat("dd-MM-yyy 'at' hh:mm a", Locale.US)
            //  formatter.timeZone = TimeZone.getTimeZone("UTC")
            return formatter.format(date)

        }


        fun orderdateTimeFormat1(createdAt: String): String {
            val converter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US)
            converter.timeZone = TimeZone.getTimeZone("UTC")
            val date = converter.parse(createdAt)
            val formatter = SimpleDateFormat("dd-MMM-yyy 'at' hh:mm a", Locale.US)
            //  formatter.timeZone = TimeZone.getTimeZone("UTC")
            return formatter.format(date)
        }






//        fun timesAgoLogic(createdDate: String): String {
//            val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
//            formatter.timeZone = TimeZone.getTimeZone("UTC")
//            val date = formatter.parse(createdDate)
//            val dateMillis = date.time
//            val cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
//            val currentMillis = cal.timeInMillis
//
//            var diffrence = currentMillis - dateMillis
//
//            val secondMillis = 1000
//            val minuteMillis = secondMillis * 60
//            val hourMillis = minuteMillis * 60
//            val daysMillis = hourMillis * 24
//            val weekMillis = daysMillis * 7
//            val monthMillis = daysMillis * 30
//            val yearMillis = daysMillis * 365
//
//            val years = diffrence / yearMillis
//            diffrence = diffrence % yearMillis
//
//            val months = diffrence / monthMillis
//            diffrence = diffrence % monthMillis
//
//            val weeks = diffrence / weekMillis
//            diffrence = diffrence % weekMillis
//
//            val days = diffrence / daysMillis
//            diffrence = diffrence % daysMillis
//
//            val hours = diffrence / hourMillis
//            diffrence = diffrence % hourMillis
//
//            val minutes = diffrence / minuteMillis
//            diffrence = diffrence % minuteMillis
//
//            val time = "Just now"
//
//            if (years > 0) {
//                return if (years > 1) {
//                    "$years years ago"
//                } else {
//                    "$years year ago"
//                }
//            }
//
//            if (months > 0) {
//                return if (months > 1) {
//                    "$months months ago"
//                } else {
//                    "$months month ago"
//                }
//            }
//
//            if (weeks > 0) {
//                return if (weeks > 1) {
//                    "$weeks weeks ago"
//                } else {
//                    "$weeks week ago"
//                }
//            }
//
//            if (days > 0) {
//                return if (days > 1) {
//                    "$days days ago"
//                } else {
//                    "$days day ago"
//                }
//            }
//
//            if (hours > 0) {
//                return if (hours > 1) {
//                    "$hours hours ago"
//                } else {
//                    "$hours hour ago"
//                }
//            }
//            if (minutes > 0) {
//                return if (minutes > 1) {
//                    "$minutes mins ago"
//                } else {
//                    "$minutes min ago"
//                }
//            }
//
//            return time
//        }


        fun timesAgoLogic(ourDate: String, application: Context): String {
            val ourDate = ourDate
            var value: Date? = null
            val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
            val dateFormatter =
                SimpleDateFormat(
                    "yyyy-MM-dd HH:mm:ss",
                    Locale.getDefault()
                ) //this format changeable

            formatter.timeZone = TimeZone.getTimeZone("UTC")
            value = formatter.parse(ourDate)

            val dateMillis = value.time
            dateFormatter.timeZone = TimeZone.getDefault()
            val cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"))

            val currentMillis = cal.timeInMillis

            val diff = currentMillis - dateMillis
//            current_date - createdAtDate

            val seconds = diff / 1000
            val minutes = seconds / 60
            val hours = minutes / 60
            val days = hours / 24
            val weeks = days / 7
            val months = weeks / 4
            val years = months / 12


            if (Locale.getDefault().language == "es") {

                if (years > 0) {
                    return if (years > 1) {
                        application.getString(R.string.ago) + " " + years.toString() + " " + application.getString(
                            R.string.years
                        )
                    } else {
                        application.getString(R.string.ago) + " " + years.toString() + " " + application.getString(
                            R.string.year
                        )
                    }
                }

                if (months > 0) {
                    return if (months > 1) {
                        application.getString(R.string.ago) + " " + months.toString() + " " + application.getString(
                            R.string.months
                        )
                    } else {

                        application.getString(R.string.ago) + " " + months.toString() + " " + application.getString(
                            R.string.month
                        )
                    }
                }
                if (weeks > 0) {
                    return if (weeks > 1) {
                        application.getString(R.string.ago) + " " + weeks.toString() + " " + application.getString(
                            R.string.weeks
                        )
                    } else {
                        application.getString(R.string.ago) + " " + weeks.toString() + " " + application.getString(
                            R.string.week
                        )
                    }
                }
                if (days > 0) {
                    return if (days > 1) {
                        application.getString(R.string.ago) + " " + days.toString() + " " + application.getString(
                            R.string.days
                        )
                    } else {
                        application.getString(R.string.ago) + " " + days.toString() + " " + application.getString(
                            R.string.day
                        )
                    }
                }
                if (hours > 0) {
                    return if (hours > 1) {
                        application.getString(R.string.ago) + " " + hours.toString() + " " + application.getString(
                            R.string.hours
                        )
                    } else {
                        application.getString(R.string.ago) + " " + hours.toString() + " " + application.getString(
                            R.string.hour
                        )
                    }
                }
                if (minutes > 0) {
                    return if (minutes > 1) {
                        application.getString(R.string.ago) + " " + minutes.toString() + " " + application.getString(
                            R.string.mins
                        )
                    } else {
                        application.getString(R.string.ago) + " " + minutes.toString() + " " + application.getString(
                            R.string.min
                        )
                    }
                }
                return application.getString(R.string.justNow)

            } else {


                if (years > 0) {
                    return if (years > 1) {
                        years.toString() + " " + application.getString(R.string.years_ago)
                    } else {
                        years.toString() + " " + application.getString(R.string.year_ago)
                    }
                }

                if (months > 0) {
                    return if (months > 1) {
                        months.toString() + " " + application.getString(R.string.months_ago)
                    } else {

                        months.toString() + " " + application.getString(R.string.month_ago)
                    }
                }
                if (weeks > 0) {
                    return if (weeks > 1) {
                        weeks.toString() + " " + application.getString(R.string.weeks_ago)
                    } else {
                        weeks.toString() + " " + application.getString(R.string.week_ago)
                    }
                }
                if (days > 0) {
                    return if (days > 1) {
                        days.toString() + " " + application.getString(R.string.days_ago)
                    } else {
                        days.toString() + " " + application.getString(R.string.day_ago)
                    }
                }
                if (hours > 0) {
                    return if (hours > 1) {
                        hours.toString() + " " + application.getString(R.string.hours_ago)
                    } else {
                        hours.toString() + " " + application.getString(R.string.hour_ago)
                    }
                }
                if (minutes > 0) {
                    return if (minutes > 1) {
                        minutes.toString() + " " + application.getString(R.string.mins_ago)
                    } else {
                        minutes.toString() + " " + application.getString(R.string.min_ago)
                    }
                }
                return application.getString(R.string.justNow)

            }


        }


//        fun uploadImage(
//            bitmap2: Uri,
//            context: Context
//        ): File {
//            val bitmap = MediaStore.Images.Media.getBitmap(context.contentResolver, bitmap2)
//            var size = 0
//            // val resultBitmap = getImageUri(bitmap)
//
//            var file = getImageUri3(bitmap, 0)
//// Get length of file in bytes
//            val fileSizeInBytes = file.length()
//// Convert the bytes to Kilobytes (1 KB = 1024 Bytes)
//            val fileSizeInKB = fileSizeInBytes / 1024
//// Convert the KB to MegaBytes (1 MB = 1024 KBytes)
//            fileSizeInMB = fileSizeInKB / 1024
////            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
////                file = getImageUri3(
////                    TransformationUtils.rotateImage(
////                        BitmapFactory.decodeFile(file.absolutePath),
////                        0
////                    ), 1
////                )
////            }
////            else {
//
//                try {
//
//
//                    var fileee = getImageUri35(bitmap)
////                    val fileSizeInBytes = fileee.length()
////// Convert the bytes to Kilobytes (1 KB = 1024 Bytes)
////                    val fileSizeInKB = fileSizeInBytes / 1024
////// Convert the KB to MegaBytes (1 MB = 1024 KBytes)
////                    val fileSizeInMB = fileSizeInKB / 1024
//                    val bb = getRotateImage(fileee, fileSizeInMB)
//                    file = getImageUri35(bb)
//
//                } catch (e: Exception) {
//                    e.printStackTrace()
//                }
////          }
//            // var bitmapByteCount = BitmapCompat.getAllocationByteCount(bitmap)
////            if (fileSizeInMB > 1) {
////                // var bitmdap = BitmapFactory.decodeFile(file.absolutePath)
////                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
////                    file = getImageUri3(
////                        TransformationUtils.rotateImage(
////                            BitmapFactory.decodeFile(file.absolutePath),
////                            0
////                        ), 1
////                    )
////                }else{
////                    file = getImageUri3(
////                        TransformationUtils.rotateImage(
////                            BitmapFactory.decodeFile(file.absolutePath),
////                            0
////                        ), 1
////                    )
////                }
////
////            } else {
////                file = getImageUri3(
////                    TransformationUtils.rotateImage(
////                        BitmapFactory.decodeFile(file.absolutePath),
////                        0
////                    ), 1
////                )
////            }
//            return file
//        }

        fun uploadImage2(
            bitmap: Bitmap,
            context: Context
        ): File {

            val file = getImageUri35(bitmap)

            return file
        }

        fun flipImage(bitmap: Bitmap, horizontal: Boolean, vertical: Boolean): Bitmap {
            val matrix = Matrix()
            matrix.preScale(
                (if (horizontal) -1 else 1).toFloat(),
                (if (vertical) -1 else 1).toFloat()
            )
            return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
        }


        private fun getImageUri35(inImage: Bitmap): File {
            val root: String = Environment.getExternalStorageDirectory().toString()
            val myDir = File("$root/req_images")
            val fname = "Image_profile.jpg"
            val file = File(myDir, fname)
            if (file.exists())
                file.delete()
            myDir.mkdirs()
            try {
                val out = FileOutputStream(file)
                inImage.compress(Bitmap.CompressFormat.JPEG, 90, out)
                out.flush()
                out.close()
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return file
        }


        fun checkPermissionForCameraGallery(context: Activity): Boolean {
            if (ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.CAMERA
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    context, arrayOf(
                        Manifest.permission.CAMERA,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                    ), 102
                )
                return false
            } else if (ActivityCompat.checkSelfPermission(
                    context, Manifest.permission.WRITE_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    context, arrayOf(
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.CAMERA

                    ), 102
                )
                return false
            } else {
                return true
            }
        }


        //****************inverted commas
        fun getDayString(context: Context, day: String): String {
            if (day.contains("Sunday")) {
                return day.replace("Sunday", context.getString(R.string.Sunday))
            }

            if (day.contains("Monday")) {
                return day.replace("Monday", context.getString(R.string.Monday))
            }

            if (day.contains("Tuesday")) {
                return day.replace("Tuesday", context.getString(R.string.Tuesday))
            }

            if (day.contains("Wednesday")) {
                return day.replace("Wednesday", context.getString(R.string.Wednesday))
            }

            if (day.contains("Thursday")) {
                return day.replace("Thursday", context.getString(R.string.Thusday))
            }

            if (day.contains("Friday")) {
                return day.replace("Friday", context.getString(R.string.Friday))
            }

            if (day.contains("Saturday")) {
                return day.replace("Saturday", context.getString(R.string.Saturday))
            }

            return ""
        }

        fun getReputationString(context: Context, reputation: String): String {
            if (reputation == "Very Bad") {
                return context.getString(R.string.Very_Bad)
            }

            if (reputation == "Bad") {
                return context.getString(R.string.Bad)
            }

            if (reputation == "Regular") {
                return context.getString(R.string.Regular)
            }

            if (reputation == "Good") {
                return context.getString(R.string.Good)
            }

            if (reputation == "Very Good") {
                return context.getString(R.string.Very_Good)
            }

            return ""
        }


        //----------------------------------Billing (inApPurchase)-------------------------------//

        //set the level of rating
        fun sellerRatingData(context: Context): ArrayList<SellerRateModel> {
            val mSucessfulRateArrayList = java.util.ArrayList<SellerRateModel>()
            var model = SellerRateModel()
            model.rateImageSelected = R.drawable.ic_very_bad
            model.rateImageUnSelected = R.drawable.ic_very_bad_unselected
            model.rateStatusText = context.getString(R.string.Very_Bad)
            model.rating = "1"
            mSucessfulRateArrayList.add(model)

            model = SellerRateModel()
            model.rateImageSelected = R.drawable.ic_bad
            model.rateImageUnSelected = R.drawable.ic_bad_unselected
            model.rateStatusText = context.getString(R.string.Bad)
            model.rating = "2"
            mSucessfulRateArrayList.add(model)

            model = SellerRateModel()
            model.rateImageSelected = R.drawable.ic_regular
            model.rateImageUnSelected = R.drawable.ic_regular_unselected
            model.rateStatusText = context.getString(R.string.Regular)
            model.rating = "3"
            mSucessfulRateArrayList.add(model)

            model = SellerRateModel()
            model.rateImageSelected = R.drawable.ic_good
            model.rateImageUnSelected = R.drawable.ic_good_unselected
            model.rateStatusText = context.getString(R.string.Good)
            model.rating = "4"
            mSucessfulRateArrayList.add(model)

            model = SellerRateModel()
            model.rateImageSelected = R.drawable.ic_very_good
            model.rateImageUnSelected = R.drawable.ic_very_good_unselected
            model.rateStatusText = context.getString(R.string.Very_Good)
            model.rating = "5"
            mSucessfulRateArrayList.add(model)

            return mSucessfulRateArrayList
        }


        fun getBillingClient(context: Context): BillingClient {
            if (billingClient == null) {
                billingClient =
                    BillingClient.newBuilder(context)
                        .setListener(context as PurchasesUpdatedListener).enablePendingPurchases()
                        .build()
            }

            return billingClient!!
        }


        fun rotateBitmap(bitmap: Bitmap, degrees: Int): Bitmap {
            val matrix = Matrix();
            matrix.postRotate(degrees.toFloat())
            return Bitmap.createBitmap(
                bitmap,
                0,
                0,
                bitmap.getWidth(),
                bitmap.getHeight(),
                matrix,
                true
            )
        }

        fun getBitmapImage(
            selectedPicture: Uri,
            context: Context
        ): Bitmap {
            val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)
            val cursor = context.getContentResolver().query(
                selectedPicture,
                filePathColumn,
                null,
                null,
                null
            )
            cursor!!.moveToFirst()

            val columnIndex = cursor.getColumnIndex(filePathColumn[0])
            val picturePath = cursor.getString(columnIndex)
            cursor.close()
            var exif: ExifInterface? = null
            val imagess = BitmapFactory.decodeFile(picturePath)
            var loadedBitmap = getResizedBitmap(imagess, 650)
//            val options = BitmapFactory.Options().apply {
//                inJustDecodeBounds = true
//            }
//            BitmapFactory.decodeResource(context.resources, picturePath, options)
//            val imageHeight  = options.outHeight
//            val imageWidth = options.outWidth
//            val imageType = options.outMimeType

            try {
                val pictureFile = File(picturePath)
                exif = ExifInterface(pictureFile.absolutePath)
            } catch (e: IOException) {
                e.printStackTrace()
            }

            var orientation = ExifInterface.ORIENTATION_NORMAL

            if (exif != null)
                orientation = exif.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL
                )

            when (orientation) {
                ExifInterface.ORIENTATION_ROTATE_90 ->
                    loadedBitmap = rotateBitmap(loadedBitmap!!, 90)

                ExifInterface.ORIENTATION_ROTATE_180 ->
                    loadedBitmap = rotateBitmap(loadedBitmap!!, 180)


                ExifInterface.ORIENTATION_ROTATE_270 ->
                    loadedBitmap = rotateBitmap(loadedBitmap!!, 270)

            }
            Log.e("ImageSize", loadedBitmap!!.getByteCount().toString())
            return loadedBitmap
        }

        fun getResizedBitmap(image: Bitmap, maxSize: Int): Bitmap? {
            var width = image.width
            var height = image.height
            val bitmapRatio = width.toFloat() / height.toFloat()
            if (bitmapRatio > 1) {
                width = maxSize
                height = (width / bitmapRatio).toInt()
            } else {
                height = maxSize
                width = (height * bitmapRatio).toInt()
            }
            return Bitmap.createScaledBitmap(image, width, height, true)
        }


        //***********************camera code for low size file return
        fun saveBitmapToFile(file: File): File? {
            return try {

                // BitmapFactory options to downsize the image
                val o = BitmapFactory.Options()
                o.inJustDecodeBounds = true
                o.inSampleSize = 6
                // factor of downsizing the image
                var inputStream = FileInputStream(file)
                //Bitmap selectedBitmap = null;
                BitmapFactory.decodeStream(inputStream, null, o)
                inputStream.close()

                // The new size we want to scale to
                val REQUIRED_SIZE = 90

                // Find the correct scale value. It should be the power of 2.
                var scale = 1
                while (o.outWidth / scale / 2 >= REQUIRED_SIZE &&
                    o.outHeight / scale / 2 >= REQUIRED_SIZE
                ) {
                    scale *= 2
                }
                val o2 = BitmapFactory.Options()
                o2.inSampleSize = scale
                inputStream = FileInputStream(file)
                val selectedBitmap = BitmapFactory.decodeStream(inputStream, null, o2)
                inputStream.close()

                // here i override the original image file
                file.createNewFile()
                val outputStream = FileOutputStream(file)
                selectedBitmap!!.compress(Bitmap.CompressFormat.JPEG, 90, outputStream)
                return file
            } catch (e: java.lang.Exception) {
                null
            }
        }

        //resize the image of camera
//        fun  getResizeCamerImage(photoFile: File?): Bitmap {
//
//            var photo =photoFile as Bitmap
//            photo = Bitmap.createScaledBitmap(photo, 100, 100, false)
//            val bytes = ByteArrayOutputStream()
//            photo.compress(Bitmap.CompressFormat.JPEG, 80, bytes)
//            return photo
//        }
//        fun calculateInSampleSize(options: BitmapFactory.Options, reqWidth: Int, reqHeight: Int): Int {
//            // Raw height and width of image
//            val (height: Int, width: Int) = options.run { outHeight to outWidth }
//            var inSampleSize = 1
//
//            if (height > reqHeight || width > reqWidth) {
//
//                val halfHeight: Int = height / 2
//                val halfWidth: Int = width / 2
//
//                // Calculate the largest inSampleSize value that is a power of 2 and keeps both
//                // height and width larger than the requested height and width.
//                while (halfHeight / inSampleSize >= reqHeight && halfWidth / inSampleSize >= reqWidth) {
//                    inSampleSize *= 2
//                }
//            }
//
//            return inSampleSize
//        }
//        fun decodeSampledBitmapFromResource(
//            res: Resources,
//            resId: Int,
//            reqWidth: Int,
//            reqHeight: Int
//        ): Bitmap {
//            // First decode with inJustDecodeBounds=true to check dimensions
//            return BitmapFactory.Options().run {
//                inJustDecodeBounds = true
//                BitmapFactory.decodeResource(res, resId, this)
//
//                // Calculate inSampleSize
//                inSampleSize = calculateInSampleSize(this, reqWidth, reqHeight)
//
//                // Decode bitmap with inSampleSize set
//                inJustDecodeBounds = false
//
//                BitmapFactory.decodeResource(res, resId, this)
//            }
//        }


        fun convertUnits(context: Context, units: String): String {
            if (units.contains("Select Unit")) {
                return units.replace("Select Unit", context.getString(R.string.Select_Unit))
            } else if (units.contains("unit")) {
                return units.replace("unit", context.getString(R.string.Unit))
            } else if (units.contains("oz")) {
                return units.replace("oz", context.getString(R.string.oz))
                return context.getString(R.string.oz)
            } else if (units.contains("lbs")) {
                return units.replace("lbs", context.getString(R.string.lbs))
            }
            return ""

        }

        fun ConvertAmPmFormat(context: Context, Time: String): String {
            if (Time.contains("AM")) {
                return Time.replace("AM", context.getString(R.string.AM))
            }
            if (Time.contains("PM")) {
                return Time.replace("PM", context.getString(R.string.PM))
            }
            return ""
        }

        fun ATconvert(context: Context, at: String): String {
            if (at.contains("at")) {
                return at.replace("at", "-")
            }
            return ""
        }
        //time Logics Array

        fun showDayDialog(context: Context): ArrayList<DialogDaysModel> {
            val arrayDays = ArrayList<DialogDaysModel>()
            var model = DialogDaysModel()
            model.dayName = context.getString(R.string.Monday)
            model.isSelected = false
            model.daysPosition = "0"
            arrayDays.add(model)

            model = DialogDaysModel()
            model.dayName = context.getString(R.string.Tuesday)
            model.isSelected = false
            model.daysPosition = "1"
            arrayDays.add(model)

            model = DialogDaysModel()
            model.dayName = context.getString(R.string.Wednesday)
            model.isSelected = false
            model.daysPosition = "2"
            arrayDays.add(model)

            model = DialogDaysModel()
            model.dayName = context.getString(R.string.Thusday)
            model.isSelected = false
            model.daysPosition = "3"
            arrayDays.add(model)

            model = DialogDaysModel()
            model.dayName = context.getString(R.string.Friday)
            model.isSelected = false
            model.daysPosition = "4"
            arrayDays.add(model)

            model = DialogDaysModel()
            model.dayName = context.getString(R.string.Saturday)
            model.isSelected = false
            model.daysPosition = "5"
            arrayDays.add(model)

            model = DialogDaysModel()
            model.dayName = context.getString(R.string.Sunday)
            model.isSelected = false
            model.daysPosition = "6"
            arrayDays.add(model)
            return arrayDays
        }


        fun SelllerRadiusLevel(context: Context): Double {
            var radius: Double = 0.5
            var model = GetProfileModel()
            val gson = Gson()
            val json = Constant.getPrefs(context).getString(Constant.PROFILE, "")
            model = gson.fromJson(json, GetProfileModel::class.java)
            if (model.seller_level == "1") {
                return 1200.0
            } else if (model.seller_level == "2") {
                return 2500.0
            } else if (model.seller_level == "3") {
                return 4000.0
            } else {
                return 5000.0
            }
        }

        //Payment method
        fun setUpPaymentMethods(context: Context): ArrayList<PaymentMethodModel> {
            val paymentArray = ArrayList<PaymentMethodModel>()
            var paymentObj = PaymentMethodModel()
            paymentObj.isSelected = false
            paymentObj.paymentId = "1"
            paymentObj.paymentName = context.getString(R.string.cash)
            paymentObj.SelectedDrawable = R.drawable.ic_selectedcheckbox_yellow
            paymentObj.UnSelectedDrawable = R.drawable.radiobutton_self_pickup_selector
            paymentArray.add(paymentObj)

            paymentObj = PaymentMethodModel()
            paymentObj.isSelected = false
            paymentObj.paymentId = "2"
            paymentObj.paymentName = context.getString(R.string.Credit)
            paymentObj.SelectedDrawable = R.drawable.ic_selectedcheckbox_yellow
            paymentObj.UnSelectedDrawable = R.drawable.radiobutton_self_pickup_selector
            paymentArray.add(paymentObj)

            paymentObj = PaymentMethodModel()
            paymentObj.isSelected = false
            paymentObj.paymentId = "3"
            paymentObj.paymentName = context.getString(R.string.debit)
            paymentObj.SelectedDrawable = R.drawable.ic_selectedcheckbox_yellow
            paymentObj.UnSelectedDrawable = R.drawable.radiobutton_self_pickup_selector
            paymentArray.add(paymentObj)

            return paymentArray
        }


        //convert the payment method
        fun ModifyCaseOpenListSetPaymentMethods(context: Context, paymentMethods: String): String {
            val localArray = Constant.setUpPaymentMethods(context)
            val allmyData = paymentMethods.split(",")
            var selectedPayment = ""
            for (items in localArray) {
                for (myString in allmyData) {
                    if (items.paymentId == myString) {
                        if (selectedPayment.isEmpty()) {
                            selectedPayment = items.paymentName
                        } else {
                            selectedPayment = "$selectedPayment, ${items.paymentName}"
                        }
                    }
                }
            }
            return selectedPayment
        }

        fun disconnectFromFacebook() {
            try {
                if (AccessToken.getCurrentAccessToken() == null) {
                    return  // already logged out
                }
                GraphRequest(
                    AccessToken.getCurrentAccessToken(),
                    "/me/permissions/",
                    null,
                    HttpMethod.DELETE,
                    GraphRequest.Callback { LoginManager.getInstance().logOut() }).executeAsync()
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
            }
        }

        fun printHashKey(pContext: Context) {
            try {
                val info = pContext.packageManager
                    .getPackageInfo(pContext.packageName, PackageManager.GET_SIGNATURES)
                for (signature in info.signatures) {
                    val md = MessageDigest.getInstance("SHA")
                    md.update(signature.toByteArray())
                    val hashKey = String(Base64.encode(md.digest(), 0))
                    Log.i("KeyRelease1", "printHashKey() Hash Key: $hashKey")
                }
            } catch (e: NoSuchAlgorithmException) {
                Log.e("KeyRelease11", "printHashKey()", e)
            } catch (e: java.lang.Exception) {
                Log.e("KeyRelease11", "printHashKey()", e)
            }
        }

        //get profile data
        fun getProfileData(context: Context): GetProfileModel {
            val gson = Gson()
            var profileModel = GetProfileModel()
            val json = getPrefs(context).getString(PROFILE, "")
            if (json != "") {
                profileModel = gson.fromJson(json, GetProfileModel::class.java)
                return profileModel
            }
            return GetProfileModel()
        }

        //show toast
        fun showToast(msg: String, context: Context) {
            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
        }

        //**************minimum Product dialog
        fun firstTimeCaiguruDialog(context: Context) {
            val dialog = Dialog(context)
            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.setContentView(R.layout.minimum_product_dialog)
            dialog.show()
            //set the click on  button
            dialog.Ok.setOnClickListener {
                dialog.dismiss()
            }

        }

        fun getLocationFromAddress(context: Context?, strAddress: String?): LatLng? {
            val coder = Geocoder(context)
            val address: List<Address>
            var p1: LatLng? = null
            try {
                // May throw an IOException
                address = coder.getFromLocationName(strAddress, 5)
                if (address == null) {
                    return null
                }
                val location: Address = address[0]
                p1 = LatLng(location.getLatitude(), location.getLongitude())
            } catch (ex: IOException) {
                ex.printStackTrace()
            }
            return p1
        }


        //*************show the total credits
        fun customDialogShowTotalCredits(context: Context) {
            var dialog = Dialog(context)
            dialog = Dialog(context)
            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.setContentView(R.layout.show_total_credits_dialog)
            dialog.show()
            val credits = roundValue(getProfileData(context).credits.toDouble())
            dialog.totalCredits.text = credits + context.getString(R.string.credits)
            //set the click on  button
            dialog.Ok.setOnClickListener {
                dialog.dismiss()
            }
        }

        //show the cash on delivery
        fun cashOnDeliveryDialog(context: Context) {
//            var dialog = Dialog(context)
//            dialog = Dialog(context)
//            dialog.window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
//            dialog.setContentView(R.layout.show_total_credits_dialog)
//            dialog.show()
//            dialog.productheadings.text = context.getString(R.string.Cash_on_Deliverys)
//            dialog.totalCredits.text = context.getString(R.string.cash_on_delivery_text)
//            //set the click on  button
//            dialog.Ok.setOnClickListener {
//                dialog.dismiss()
//            }


            val mDialog = AlertDialog.Builder(context)
            mDialog.setTitle(context.getString(R.string.Cash_on_Deliverys))
            mDialog.setMessage(context.getString(R.string.cash_on_delivery_text))
            mDialog.setPositiveButton(
                context.getString(R.string.ok)
            ) { dialog, which ->
                dialog.cancel()

            }
            mDialog.show()
        }


        //get the Search ArrayData
        //**************get the searchArrayData
        fun getSearchData(context: Context): ArrayList<SearchModel> {
//get the search data in the array
            val gson1 = Gson()
            val array = Constant.getPrefs(context).getString(SearchNameKey, "")
            if (array != "") {
                val lstArrayList: ArrayList<SearchModel> = gson1.fromJson(
                    array,
                    object : TypeToken<List<SearchModel>>() {}.type
                )
                Log.e("ysppp", lstArrayList.size.toString())

//                val values = ArrayList<SearchModel>()
//                values.clear()
//                values.addAll(lstArrayList)
//                val hashSet = HashSet<SearchModel>()
//                hashSet.addAll(values)
//                values.clear()
//                values.addAll(hashSet)
//                val unique: Set<SearchModel> =
//                    LinkedHashSet<SearchModel>(lstArrayList)
//                val  myArray1 = ArrayList<SearchModel>(unique)
                return lstArrayList
            }

            return ArrayList<SearchModel>()
        }


        //*************check package install or not
        private fun whatsappInstalledOrNot(uri: String, context: Context): Boolean {
            val pm = context.packageManager
            var app_installed = false
            app_installed = try {
                pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES)
                true
            } catch (e: PackageManager.NameNotFoundException) {
                false
            }
            return app_installed
        }

        //****************open whatsapp with message************//
        fun openWhatsappWIthMessage(context: Context) {
            ///  var smsNumber = "+91 89688 81388" // contains spaces.
            var smsNumber = "+44 7831 805229" // contains spaces.
            smsNumber = smsNumber.replace("+", "").replace(" ", "")
            val packageManager: PackageManager = context.getPackageManager()
            val i = Intent(Intent.ACTION_VIEW)

            try {
                val url =
                    "https://api.whatsapp.com/send?phone=" + smsNumber + "&text=" + URLEncoder.encode(
                        "",
                        "UTF-8"
                    )
                i.setPackage("com.whatsapp")
                i.data = Uri.parse(url)
                if (i.resolveActivity(packageManager) != null) {
                    context.startActivity(i)
                }
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
            }
        }

        //*************both code mixture
        fun openWhatsApp(context: Context) {
            val isWhatsappInstalled = whatsappInstalledOrNot("com.whatsapp", context)
            if (isWhatsappInstalled) {
                openWhatsappWIthMessage(context)
            } else {
                val uri: Uri = Uri.parse("market://details?id=com.whatsapp")
                val goToMarket = Intent(Intent.ACTION_VIEW, uri)
                Toast.makeText(
                    context, context.getString(R.string.app_not_installed),
                    Toast.LENGTH_SHORT
                ).show()
                context.startActivity(goToMarket)
            }


        }

        fun setReason(context: Context, msg: String): String {
            val local = getLocal(context)
            var setposition = 0


            if (msg == "The total amount of order is higher than my budget.") {
                setposition = 1
                //return context.getString(R.string.total_amount)
            } else if (msg == "There is no more stock.") {
                setposition = 2
                //return context.getString(R.string.more_stock)
            } else if (msg == "I can't deliver to the address indicated by the user.") {
                setposition = 3
            }
            //set in the spanish
            else if (msg == "El importe total del pedido es superior a mi presupuesto.") {
                setposition = 1
            } else if (msg == "No hay más stock") {
                setposition = 2
            } else if (msg == "No puedo entregar el pedido a la dirección del usuario") {
                setposition = 3
            }
            //set the text as per Local
            if (local == "en") {
                if (setposition == 1) {
                    return "The total amount of order is higher than my budget."
                } else if (setposition == 2) {
                    return "There is no more stock."
                } else if (setposition == 3) {
                    return "I can't deliver to the address indicated by the user."
                }
            } else {
                if (setposition == 1) {
                    return "El importe total del pedido es superior a mi presupuesto."
                } else if (setposition == 2) {
                    return "No hay más stock"
                } else if (setposition == 3) {
                    return "No puedo entregar el pedido a la dirección del usuario"
                }
            }
            return msg
        }
    }

    }

//        val sha1 = byteArrayOf(
//            0xA3.toByte(),
//            0x6E,
//            0xA8.toByte(),
//            0x4F,
//            0x0F,
//            0x64,
//            0x62,
//            0xA7.toByte(),
//            0xC5.toByte(),
//            0xAD.toByte(),
//            0xDB.toByte(),
//            0x1E,
//            0x21,
//            0xFB.toByte(),
//            0x3E,
//            0xB5.toByte(),
//            0xC8.toByte(),
//            0x13,
//            0xB7.toByte(),
//            0xAA.toByte()
//        )
//
//
//        println(
//            "keyhashGooglePlaySignIn:" + Base64.encodeToString(
//                sha1,
//                Base64.NO_WRAP
//            )
//        )
//printHashKey(this)



