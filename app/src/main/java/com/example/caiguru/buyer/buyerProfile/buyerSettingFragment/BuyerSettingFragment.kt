package com.example.caiguru.buyer.buyerProfile.buyerSettingFragment

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.Intent
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
import com.example.caiguru.buyer.buyerProfile.buyerCreditEarned.BuyerCreditEarnedActivity
import com.example.caiguru.commonScreens.loginScreen.LoginActivity
import com.example.caiguru.databinding.FragmentBuyerSettingBinding
import com.example.caiguru.seller.homeSeller.GetProfileModel
import com.example.caiguru.seller.sellerSetting.sellerBuyCredits.SellerBuyCreditsActivity
import com.example.caiguru.seller.sellerSetting.sellerProfile.settingfragment.SettingModel
import constant_Webservices.BuyerLevelModel
import constant_Webservices.Constant
import kotlinx.android.synthetic.main.dialog_logout.*


class BuyerSettingFragment : Fragment(), BuyerSettingAdapter.clickLogout {


    private lateinit var dialog: Dialog
    lateinit var mBinding: FragmentBuyerSettingBinding
    private var profileDataModel = GetProfileModel()
    lateinit var viewModel: BuyerSettingViewModel
    lateinit var adapter: BuyerSettingAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_buyer_setting, container, false)
        //  viewModel = ViewModelProviders.of(this)[BuyerSettingViewModel::class.java]
        viewModel = ViewModelProvider(this).get(BuyerSettingViewModel::class.java)

        mBinding.creditEarned.setOnClickListener {
            val intent = Intent(activity!!, BuyerCreditEarnedActivity::class.java)
            startActivity(intent)
        }

//*****************************************************************************************************//
//        viewModel.getLogout().observe(viewLifecycleOwner, Observer {
//            logoutProgressBar.visibility=View.GONE
//            Constant.showToast(it.message,activity!!)
//            val intent = Intent(activity, LoginActivity::class.java)
//            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
//            startActivity(intent)
//            activity?.finish()
//        })

//*********************************************************************************************************//
        return mBinding.root

    }


    private fun setAdapter() {

        val manager = LinearLayoutManager(activity)
        adapter = BuyerSettingAdapter(activity!!, getSearchList(), this)
        mBinding.rvprofile.layoutManager = manager
        mBinding.rvprofile.adapter = adapter


    }

    override fun openCreditsActivity() {

        val intent = Intent(context, SellerBuyCreditsActivity::class.java)
        intent.putExtra("profileKey", profileDataModel)
        startActivityForResult(intent, 200)
    }

    @SuppressLint("SetTextI18n")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 200 && resultCode == Activity.RESULT_OK) {
            val data = data!!.getParcelableExtra<GetProfileModel>("keyTotalCredits")
            if (data!!.credits.isNotEmpty()) {
                mBinding.credit.setText((Constant.roundValue(data.credits.toDouble())) + getString(R.string.credits))
                profileDataModel = data
            }

        }
    }

    override fun logout() {
        dialog = Dialog(activity!!)
        dialog.setCancelable(false)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setContentView(R.layout.dialog_logout)
        //   dialog.dialogActive.visibility = View.VISIBLE
        // dialog.setTitle("Active")
        dialog.show()
        //Constant.BACKGROUND_API_HIT=true
        dialog.yes.setOnClickListener {
            //logoutProgressBar.visibility=View.VISIBLE
            try {


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
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
            }
        }

        dialog.no.setOnClickListener {
            dialog.dismiss()
        }

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
        model.name = getString(R.string.Change_Cities)
        model.image = R.drawable.ic_arrow_shopping_lst
        list.add(model)

        model = SettingModel()
        model.name = getString(R.string.Notifications)
        model.image = R.drawable.ic_arrow_shopping_lst
        list.add(model)

        model = SettingModel()
        model.name = getString(R.string.My_Orders)
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

    private fun levelget(level: String): BuyerLevelModel {
        val bellerLevel = Constant.BuyerLevel(activity!!)
        for (category in bellerLevel) {

            if (category.levelType == level.trim()) {

                return category
            }
        }
        return BuyerLevelModel()
    }

    @SuppressLint("SetTextI18n")
    override fun onResume() {
        super.onResume()

        //var model = GetProfileModel()
//        val gson = Gson()
//        val json = Constant.getPrefs(activity!!).getString("profile", "")
        val model = Constant.getProfileData(activity!!)

        profileDataModel = model

        if (model.image.isEmpty()) {
            Glide.with(activity!!).load(R.drawable.product_placeholder)
                .into(mBinding.img)

        } else {

            Glide.with(activity!!).load(model.image).into(mBinding.img)

        }
        try {
            //  Glide.with(activity!!).load(model.image).into(mBinding.img)
            Glide.with(activity!!).load(levelget(model.buyer_level).levelImage)
                .into(mBinding.imgBatch)
            // Glide.with(activity!!).load(model.seller_level).into(mBinding.imgBatch)
            mBinding.txtname.setText(model.name)
            mBinding.reputation.setText(
                getString(R.string.reputation) + " " + Constant.getReputationString(
                    activity!!,
                    model.buyer_reputation
                )
            )
            val credits = model.credits.toDouble()
            // mBinding.credit.setText(credits.toString() + " cr")
            mBinding.credit.setText((Constant.roundValue(credits)) + getString(R.string.cr))
            val earnedCredit = model.earned_credits.toDouble()
            // mBinding.creditEarned.setText(earnedCredit.toString() + " cr")
            mBinding.creditEarned.setText((Constant.roundValue(earnedCredit)) + getString(R.string.cr))
            setAdapter()
        } catch (e: Exception) {
            e.printStackTrace()
        }


    }

}