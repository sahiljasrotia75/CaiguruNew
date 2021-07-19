package com.example.caiguru.seller.shoppingfragment

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.caiguru.R
import com.example.caiguru.databinding.FragmentShoppingSettingBinding
import com.example.caiguru.commonScreens.loginScreen.LoginActivity
import com.example.caiguru.seller.homeSeller.GetProfileModel
import com.example.caiguru.seller.sellerSetting.sellerProfile.settingfragment.SellerSettingViewModel
import com.example.caiguru.seller.sellerSetting.sellerProfile.settingfragment.SettingAdapter
import com.example.caiguru.seller.sellerSetting.sellerProfile.settingfragment.SettingModel
import com.example.caiguru.seller.sellerSetting.sellerBuyCredits.SellerBuyCreditsActivity
import com.example.caiguru.seller.sellerSetting.sellerCreditEarned.CreditEarnedByShoppingListActivity
import com.google.gson.Gson
import constant_Webservices.Constant
import constant_Webservices.SellerLevelModel
import kotlinx.android.synthetic.main.dialog_logout.*

class SellerSettingFragment : Fragment(), SettingAdapter.clickLogout {

    private lateinit var dialog: Dialog
    private var profileDataModel = GetProfileModel()
    lateinit var mBinding: FragmentShoppingSettingBinding
    lateinit var viewModel: SellerSettingViewModel
    lateinit var adapter: SettingAdapter


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_shopping_setting, container, false)
        // viewModel = ViewModelProviders.of(this)[SellerSettingViewModel::class.java]
        viewModel = ViewModelProvider(this).get(SellerSettingViewModel::class.java)
        setObserver()
        mBinding.creditEarned.setOnClickListener {
            val intent = Intent(activity!!, CreditEarnedByShoppingListActivity::class.java)
            startActivity(intent)
        }
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setAdapter()

    }

    //***************interface to open the activity
    override fun openCreditsActivity() {
        val intent = Intent(context, SellerBuyCreditsActivity::class.java)
        intent.putExtra("profileKey", profileDataModel)
        startActivityForResult(intent, 200)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 200 && resultCode == Activity.RESULT_OK) {
            val data = data!!.getParcelableExtra<GetProfileModel>("keyTotalCredits")!!
            //  mBinding.credit.setText(data.credits + " cr")
            if (data.credits.isNotEmpty()) {
                mBinding.credit.setText(
                    (Constant.roundValue(data.credits.toDouble())) + context?.getString(
                        R.string.cr
                    )
                )
                profileDataModel = data
            }

        }
    }

    override fun logout() {
        try {
            dialog = Dialog(activity!!)
            dialog.setCancelable(false)
            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.setContentView(R.layout.dialog_logout)
            dialog.show()
            //Constant.BACKGROUND_API_HIT=true
            dialog.yes.setOnClickListener {
                // logoutProgressBar.visibility=View.VISIBLE
                val token = "Bearer " + Constant.getPrefs(activity!!).getString(
                    Constant.token,
                    ""
                )
                viewModel.logout(token)

                Constant.disconnectFromFacebook()
                dialog.dismiss()
                Constant.getPrefs(activity!!).edit().clear().apply()
                Toast.makeText(activity, getString(R.string.Logout_Sucessfully), Toast.LENGTH_SHORT)
                    .show()
                val intent = Intent(activity, LoginActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
                activity?.finish()
            }

            dialog.no.setOnClickListener {
                dialog.dismiss()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    private fun setAdapter() {
        val manager = LinearLayoutManager(activity)
        adapter = SettingAdapter(activity!!, getSearchList(), this)
        mBinding.rvprofile.layoutManager = manager
        mBinding.rvprofile.adapter = adapter
    }

    private fun setObserver() {
        // viewModel.getLogout().observe(viewLifecycleOwner, Observer {
        // logoutProgressBar.visibility=View.GONE
        //  Toast.makeText(activity, it.message, Toast.LENGTH_SHORT).show()
//            val intent = Intent(activity, LoginActivity::class.java)
//            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
//            startActivity(intent)
//            activity?.finish()
        //  })
    }


    private fun getSearchList(): ArrayList<SettingModel> {
        val list: ArrayList<SettingModel> = ArrayList()
        var model = SettingModel()
        model.name = getString(R.string.My_Profile)
        model.image = R.drawable.ic_arrow_shopping_lst
        list.add(model)

        model = SettingModel()
        model.name = getString(R.string.Buy_Credits)
        model.image = R.drawable.ic_arrow_shopping_lst
        list.add(model)

        model = SettingModel()
        model.name = getString(R.string.blocked_user)
        model.image = R.drawable.ic_arrow_shopping_lst
        list.add(model)

        model = SettingModel()
        model.name = getString(R.string.Notifications)
        model.image = R.drawable.ic_arrow_shopping_lst
        list.add(model)

        model = SettingModel()
        model.name = getString(R.string.Change_Cities)
        model.image = R.drawable.ic_arrow_shopping_lst
        list.add(model)

        model = SettingModel()
        model.name = getString(R.string.Help)
        model.image = R.drawable.ic_arrow_shopping_lst
        list.add(model)

        model = SettingModel()
        model.name = getString(R.string.Logout)
        model.image = R.drawable.ic_arrow_shopping_lst
        list.add(model)
        return list
    }

    private fun levelget(level: String): SellerLevelModel {
        val sellerLevel = Constant.SellerLevel(activity!!)
        for (category in sellerLevel) {

            if (category.levelType == level.trim()) {

                return category
            }
        }
        return SellerLevelModel()
    }

    @SuppressLint("SetTextI18n")
    override fun onResume() {
        super.onResume()
        var model = GetProfileModel()
        val gson = Gson()
        val json = Constant.getPrefs(activity!!).getString(Constant.PROFILE, "1")
        if (json != "1") {
            model = gson.fromJson(json, GetProfileModel::class.java)

            profileDataModel = model
            if (model.image.isEmpty()) {

                Glide.with(activity!!).load(R.drawable.product_placeholder)
                    .into(mBinding.img)

            } else {

                Glide.with(activity!!).load(model.image).into(mBinding.img)
            }
            Glide.with(activity!!).load(levelget(model.seller_level).levelImage)
                .into(mBinding.imgBatch)
            mBinding.txtname.setText(model.name)
            mBinding.reputation.setText(
                getString(R.string.Reputation) + " " + Constant.getReputationString(
                    activity!!,
                    model.seller_reputation
                )
            )

            val credits = model.credits.toDouble()
            mBinding.credit.setText((Constant.roundValue(credits)) + getString(R.string.crr))

            val earnedCredit = model.earned_credits.toDouble()
            mBinding.creditEarned.setText((Constant.roundValue(earnedCredit)) + getString(R.string.crr))
            setAdapter()
        }

    }


}