package com.example.caiguru.commonScreens.registerActivity

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.ColorDrawable
import android.media.ExifInterface
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.text.InputFilter
import android.text.Spannable
import android.text.SpannableString
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.TransformationUtils
import com.example.caiguru.R
import com.example.caiguru.commonScreens.hideEmoji.HideEmoji
import com.example.caiguru.commonScreens.loginScreen.LoginActivity
import com.example.caiguru.commonScreens.loginScreen.ModelFacebook
import com.example.caiguru.commonScreens.registerCategory.RegisterCategoryActivity
import com.example.caiguru.commonScreens.termsConditionPrivacyPolicy.TermsAndCondtionActivity
import com.example.caiguru.databinding.ActivityRegisterBinding
import com.example.caiguru.databinding.OpenFromCameraPermissionBinding
import constant_Webservices.Constant
import constant_Webservices.Constant.Companion.isValidEmailId
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.switch_dialog.*
import java.io.File
import java.io.FileDescriptor
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


class RegisterActivity : AppCompatActivity(), NextClick {
    private var checkedbtn: Boolean = false
    private var model = RegisterModel()
    private lateinit var mvmodel: RegisterViewModel
    private lateinit var mbinding: ActivityRegisterBinding
    private var photoFile: File? = null
    var check: Int = -1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fullScreen()
        mbinding = DataBindingUtil.setContentView(this, R.layout.activity_register)
        mvmodel = ViewModelProviders.of(this)[RegisterViewModel::class.java]
        //setclick
        mbinding.click = this
        alredyAccount()

        mbinding.edtname.filters = arrayOf<InputFilter>(HideEmoji(this))
        setDataFromIntent()
        setSpannableTextTermCondition()
       // TermAndConditionDialog()
        imageRadioButton.setOnClickListener {
            if (checkedbtn == false) {
                checkedbtn = true
                imageRadioButton.setImageDrawable(getDrawable(R.drawable.ic_radio_button_checked_black_24dp))
            } else {
                checkedbtn = false
                imageRadioButton.setImageDrawable(getDrawable(R.drawable.ic_radio_button_unchecked_white_24dp))
            }
        }
        //hide keyboard on layout
        mbinding.linearLayout.setOnClickListener {
            Constant.hideSoftKeyboard(it, this)
        }
        //...............set the observer of update Image.....................//
        //sucessful
        mvmodel.getdataSucessful().observe(this, androidx.lifecycle.Observer {
            setProfilePic(it.image)
            mbinding.progressBar.visibility = View.INVISIBLE
            mbinding.img.visibility = View.VISIBLE
            mbinding.editimage.visibility = View.VISIBLE
        })
        //failure
        mvmodel.errorData().observe(this, androidx.lifecycle.Observer {
            Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
            mbinding.progressBar.visibility = View.INVISIBLE
            mbinding.img.visibility = View.VISIBLE
            mbinding.editimage.visibility = View.VISIBLE
        })


        //....set the click on the image and create the dialog onclick for open the camera or gallery...............//
        mbinding.img.setOnClickListener {
            if (Constant.checkPermissionForCameraGallery(this))
                showDialog()
        }

        //check the email vaild observer
        mvmodel. mSucessfulEmailRegisteration().observe(this, androidx.lifecycle.Observer {
            val intent = Intent(this, RegisterCategoryActivity::class.java)
            intent.putExtra("keydata", model)
            startActivity(intent)
            btnWait.visibility = View.INVISIBLE
            btnnext.visibility = View.VISIBLE
        })
        mvmodel. mFailureEmailRegisteration().observe(this, androidx.lifecycle.Observer {
            Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
            btnWait.visibility = View.INVISIBLE
            btnnext.visibility = View.VISIBLE
        })


    }

    private fun setSpannableTextTermCondition() {
        val acceptText = getString(R.string.I_accept_the)
        val termText = getString(R.string.terms_and_conditions)
        val and=getString(R.string.and)
        val privacypolicy=getString(R.string.Privacy_Policy)
        val allString = "$acceptText $termText $and $privacypolicy"
        val span = SpannableString(allString)
        val boldSpan = StyleSpan(Typeface.BOLD)
        val boldSpan1 = StyleSpan(Typeface.BOLD)


        val clickableSpanStartTermAndConditionClick = object : ClickableSpan() {
            override fun onClick(textView: View) {
                TermAndConditionDialog()
            }

            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.isUnderlineText = false
            }
        }

        val clickableSpanPrivacyPolicyClick = object : ClickableSpan() {
            override fun onClick(textView: View) {
                PrivacyPolicyDialog()
            }

            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.isUnderlineText = false
            }
        }


        //set the click inn the span
        span.setSpan(
            clickableSpanStartTermAndConditionClick,
            acceptText.length + 1,
            termText.length + acceptText.length + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        //set the 2nd click
        span.setSpan(
            clickableSpanPrivacyPolicyClick,
            acceptText.length + termText.length + and.length + 3,
            privacypolicy.length + acceptText.length + termText.length + and.length + 3,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )


        span.setSpan(
            ForegroundColorSpan(resources.getColor(R.color.white)),
            0,
            acceptText.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        span.setSpan(
            ForegroundColorSpan(resources.getColor(R.color.yellow)),
            acceptText.length + 1,
            termText.length + acceptText.length + 1,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        span.setSpan(
            ForegroundColorSpan(resources.getColor(R.color.white)),
            acceptText.length + termText.length + 2,
            and.length + acceptText.length + termText.length + 2,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        span.setSpan(
            ForegroundColorSpan(resources.getColor(R.color.yellow)),
            acceptText.length + termText.length + and.length + 3,
            privacypolicy.length + acceptText.length + termText.length + and.length + 3,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )


        span.setSpan(
            boldSpan, acceptText.length + 1,
            termText.length + acceptText.length + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        span.setSpan(
            boldSpan1,
            acceptText.length + termText.length + and.length + 3,
            privacypolicy.length + acceptText.length + termText.length + and.length + 3,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        txtTermCondition.movementMethod = LinkMovementMethod.getInstance()//important for the click case
        txtTermCondition.setText(span, TextView.BufferType.SPANNABLE)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == 102) {
            if (grantResults.contains(-1)) {

            } else {
                showDialog()
            }
        }
    }

    private fun setProfilePic(image: String) {
        model.image = image
        Glide.with(this)
            .load(image)
            .into(mbinding.img)
    }

    private fun setDataFromIntent() {
        if (intent.hasExtra("facebook")) {
            val facebookModel: ModelFacebook = intent.getParcelableExtra("facebook")!!
            setProfilePic(facebookModel.picture)
            mbinding.edtname.setText(facebookModel.name)
         mbinding.edtemail.setText(facebookModel.email)

            model.image = facebookModel.picture
            model.name = facebookModel.name
            model.email = facebookModel.email
            model.social_id = facebookModel.id
            model.social_type = "2"
            model.password = facebookModel.id

            mbinding.edtpassword.visibility = View.GONE
            mbinding.edtconfirmPassword.visibility = View.GONE
            mbinding.titlePass.visibility = View.GONE
            mbinding.titleConPass.visibility = View.GONE

        }
    }


    private fun getRotateImage(photoFile: File?): Bitmap {
        val ei: ExifInterface = ExifInterface(photoFile!!.path)
        val orientation = ei.getAttributeInt(
            ExifInterface.TAG_ORIENTATION,
            ExifInterface.ORIENTATION_UNDEFINED
        )

        val bitmap = BitmapFactory.decodeFile(photoFile.path)

        var rotatedBitmap: Bitmap? = bitmap
        when (orientation) {

            ExifInterface.ORIENTATION_ROTATE_90 ->
                rotatedBitmap = TransformationUtils.rotateImage(bitmap, 90)

            ExifInterface.ORIENTATION_ROTATE_180 ->
                rotatedBitmap = TransformationUtils.rotateImage(bitmap, 180)

            ExifInterface.ORIENTATION_ROTATE_270 ->
                rotatedBitmap = TransformationUtils.rotateImage(bitmap, 270)

            ExifInterface.ORIENTATION_NORMAL ->
                rotatedBitmap = bitmap
        }

        return rotatedBitmap!!
    }

    //....................................Update profile image by gallary and camera.................//
    private fun showDialog() {

        val builder: androidx.appcompat.app.AlertDialog.Builder =
            androidx.appcompat.app.AlertDialog.Builder(this)
        builder.setTitle(getString(R.string.Open_By))
        val mBinding: OpenFromCameraPermissionBinding =
            DataBindingUtil.inflate(
                LayoutInflater.from(this),
                R.layout.open_from_camera_permission,
                null,
                false
            )

        if (check == 0) {
            mBinding.camera.isChecked = true
            mBinding.gallery.isChecked = false
        }
        if (check == 1) {
            mBinding.gallery.isChecked = false
            mBinding.camera.isChecked = true
        }
        builder.setView(mBinding.root)
        builder.setCancelable(false)

        builder.setPositiveButton(getString(R.string.ok)) { dialog, which ->

            if (mBinding.camera.isChecked) {
                openCamera()
                check = 0


            } else if (mBinding.gallery.isChecked) {
                openGallery()
                check = 1
            }
            dialog.dismiss()
        }

        builder.setNegativeButton(getString(R.string.cancel)) { dialog, which ->

        }
        builder.create()
        builder.show()

    }

    private fun openCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        try {
            photoFile = createImageFile()
        } catch (ex: Exception) {
            // Error occurred while creating the File
            Log.i("", "IOException")
        }
        // Continue only if the File was successfully created
        if (photoFile != null) {
            val uri = FileProvider.getUriForFile(
                this,
                "com.app.caiguru" + ".provider",
                photoFile!!
            )
            intent.putExtra(MediaStore.EXTRA_OUTPUT, uri)
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            startActivityForResult(intent, 29)
        }
//        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
//        startActivityForResult(intent, 29)
    }


    @SuppressLint("IntentReset")
    private fun openGallery() {

        var intent = Intent(
            Intent.ACTION_PICK,
            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        );
        intent.setType("image/*")
        startActivityForResult(intent, 1)
//        val intent = Intent("android.intent.action.GET_CONTENT")
//        intent.type = "image/*"
//        startActivityForResult(intent, 1)
    }

    //full screen code
    fun fullScreen() {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

    }

    @SuppressLint("SimpleDateFormat")
    private fun createImageFile(): File {
        // Create an image file name
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val imageFileName = "JPEG_" + timeStamp + "_"
        val storageDir = Environment.getExternalStoragePublicDirectory(
            Environment.DIRECTORY_PICTURES
        )
        val image = File.createTempFile(
            imageFileName,  // prefix
            ".jpg",         // suffix
            storageDir      // directory
        )
        return image
    }

    //url link get to used this
    private fun getImageUri(inImage: Bitmap): File {
        val root: String = Environment.getExternalStorageDirectory().toString()
        val myDir = File("$root/req_images")
        myDir.mkdirs()
        val fname = "Image_profile.jpg"
        val file = File(myDir, fname)
        if (file.exists())
            file.delete()

        try {
            val out = FileOutputStream(file)
            inImage.compress(Bitmap.CompressFormat.JPEG, 80, out)
            out.flush()
            out.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return file
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        try {
            if (requestCode == 29) {
                try {
                    val resultBitmap = getRotateImage(photoFile)
                    val uri = getImageUri(resultBitmap)

//..................................Api FOr Update Image....................................................................//
                    mvmodel.profileImage(Constant.saveBitmapToFile(uri)!!)//send the data for the api
                    //  mvmodel.profileImage(uri)//send the data for the api
                    mbinding.progressBar.visibility = View.VISIBLE
                    mbinding.img.visibility = View.INVISIBLE

                } catch (e: Exception) {
                    e.printStackTrace()
                    Toast.makeText(this, getString(R.string.Failed), Toast.LENGTH_SHORT).show()
                }

            }
//2nd code
            else if (requestCode == 1 && data != null) {
                try {

                    val selectedPicture: Uri = data.getData()!!

                    val file: File =
                        Constant.uploadImage2(Constant.getBitmapImage(selectedPicture, this), this)

                    mvmodel.profileImage(file)
                    mbinding.progressBar.visibility = View.VISIBLE
                    mbinding.img.visibility = View.INVISIBLE


                } catch (e: Exception) {
                    e.printStackTrace()
                    Toast.makeText(this, getString(R.string.Failed), Toast.LENGTH_SHORT).show()
                }
            } else if (requestCode == 2 && data != null) {
                try {
                    Toast.makeText(this, "lll!", Toast.LENGTH_SHORT).show()
                } catch (e: Exception) {
                    e.printStackTrace()
                    Toast.makeText(this, getString(R.string.Failed), Toast.LENGTH_SHORT).show()
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }


    }


    //set click on button
    override fun setClicked(name: String, email: String, password: String, referral_code: String) {

        if (intent.hasExtra("facebook")) {
            when {
                mbinding.edtname.text.isEmpty() -> Toast.makeText(
                    this,
                    getString(R.string.Please_Enter_The_Name),
                    Toast.LENGTH_SHORT
                ).show()
                mbinding.edtemail.text.isEmpty() -> Toast.makeText(
                    this,
                    getString(R.string.Please_Enter_The_Email),
                    Toast.LENGTH_SHORT
                ).show()

                (!isValidEmailId(mbinding.edtemail.text.toString().trim())) ->
                    Toast.makeText(
                        applicationContext,
                        R.string.Enter_the_Valid_Email,
                        Toast.LENGTH_SHORT
                    ).show()

                checkedbtn == false -> Toast.makeText(
                    this,
                    getString(R.string.term_condition_Toast),
                    Toast.LENGTH_SHORT
                ).show()

                else -> {

                    //send the data to the api
                    model.name = name
                    model.email = email
                    model.referral_code = referral_code
                    btnWait.visibility=View.VISIBLE
                    btnnext.visibility=View.INVISIBLE
                    mvmodel.check_email_valid(email)

//                    val intent = Intent(this, RegisterCategoryActivity::class.java)
//                    intent.putExtra("keydata", model)
//                    startActivity(intent)


                }
            }
        }
        else {
            when {
                mbinding.edtname.text.isEmpty() -> Toast.makeText(
                    this,
                    getString(R.string.Please_Enter_The_Name),
                    Toast.LENGTH_SHORT
                ).show()

//                mbinding.edtname.text.length >=18 -> Toast.makeText(
//                    this,
//                    getString(R.string.C),
//                    Toast.LENGTH_SHORT
//                ).show()

                mbinding.edtemail.text.isEmpty() -> Toast.makeText(
                    this,
                    getString(R.string.Please_Enter_The_Email),
                    Toast.LENGTH_SHORT
                ).show()

                mbinding.edtpassword.text.isEmpty() -> Toast.makeText(
                    this,
                    getString(R.string.enter_password),
                    Toast.LENGTH_SHORT
                ).show()

                mbinding.edtconfirmPassword.text.isEmpty() -> Toast.makeText(
                    this,
                    getString(R.string.enter_confirm_password),
                    Toast.LENGTH_SHORT
                ).show()


                mbinding.edtpassword.text.toString() != mbinding.edtconfirmPassword.text.toString() -> Toast.makeText(
                    this,
                    R.string.Please_Enter_The_Correct_password,
                    Toast.LENGTH_SHORT
                ).show()

                (!isValidEmailId(mbinding.edtemail.text.toString().trim())) ->
                    Toast.makeText(
                        applicationContext,
                        R.string.Enter_the_Valid_Email,
                        Toast.LENGTH_SHORT
                    ).show()

                checkedbtn == false -> Toast.makeText(
                    this,
                    getString(R.string.term_condition_Toast),
                    Toast.LENGTH_SHORT
                ).show()

                else -> {

                    //send the data to the api
                    model.name = name
                    model.email = email
                    model.password = password
                    model.referral_code = referral_code
//                    val intent = Intent(this, RegisterCategoryActivity::class.java)
//                    intent.putExtra("keydata", model)
//                    startActivity(intent)
                    //email validation api
                    btnWait.visibility=View.VISIBLE
                    btnnext.visibility=View.INVISIBLE
                    mvmodel.check_email_valid(email)

                }
            }
        }


    }

    //click
    fun alredyAccount() {
        //already have an account click
        mbinding.txtSignUpView.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)
            finish()

        }
    }

    fun TermAndConditionDialog() {
            val dialog = Dialog(this)
            // dialog.setCancelable(false);
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.setContentView(R.layout.terms_condition_dialog)
            dialog.show()


            dialog.yes.setOnClickListener {
                val intent = Intent(
                    this, TermsAndCondtionActivity::class.java
                )
                intent.putExtra("TermsEnglish", "Yes")
                startActivity(intent)
                dialog.dismiss()
            }


            dialog.no.setOnClickListener {
                val intent = Intent(
                    this, TermsAndCondtionActivity::class.java
                )
                intent.putExtra("TermsSpanish", "Yes")
                startActivity(intent)
                dialog.dismiss()
            }

    }
    fun PrivacyPolicyDialog() {
        val dialog = Dialog(this)
        // dialog.setCancelable(false);
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setContentView(R.layout.terms_condition_dialog)
        dialog.show()


        dialog.yes.setOnClickListener {
            val intent = Intent(
                this, TermsAndCondtionActivity::class.java
            )
            intent.putExtra("PrivacyEnglish", "Yes")
            startActivity(intent)
            dialog.dismiss()
        }


        dialog.no.setOnClickListener {
            val intent = Intent(
                this, TermsAndCondtionActivity::class.java
            )
            intent.putExtra("PrivacySpanish", "Yes")
            startActivity(intent)
            dialog.dismiss()
        }

    }

}






