package com.example.caiguru.commonScreens.chat.chatMessage

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.ExifInterface
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.text.InputFilter
import android.text.InputType
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.load.resource.bitmap.TransformationUtils
import com.example.caiguru.R
import com.example.caiguru.commonScreens.chat.ModelChat
import com.example.caiguru.commonScreens.hideEmoji.HideEmoji
import com.example.caiguru.databinding.OpenFromCameraPermissionBinding
import constant_Webservices.Constant
import kotlinx.android.synthetic.main.activity_chat_messages.*
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*


class ChatMessagesActivity : AppCompatActivity() {

    private var firstOPen: Int = 0
    private lateinit var mAdapter: MessageAdapter
    private lateinit var mViewModel: ChatMessageViewModel
    private lateinit var model: ModelChat
    private var time: Long = 0
    private var photoFile: File? = null
    var check: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_messages)
        //mViewModel = ViewModelProviders.of(this)[ChatMessageViewModel::class.java]
        mViewModel = ViewModelProvider(this).get(ChatMessageViewModel::class.java)
        model = intent.getParcelableExtra("model")!!
        //****************get the message first time
        mViewModel.getMessageFirstTime(model)
        chatBox.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE or InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_FLAG_CAP_SENTENCES)

        settingUpToolbar()
        setMessageAdapter()
        mViewModel.getMessageList().observe(this, Observer {
            if (it.isNotEmpty()) {
                mAdapter.update(it)
                //**************************grtMessagesConinous
                if (firstOPen == 0) {
                    firstOPen = 1
                    mViewModel.getMessagesConinous(model)
                }
            }

            // if(isFirst) {
            messageList.scrollToPosition(it.size - 1)
            //   isFirst = false
            // }
        })
        chatBox.filters = arrayOf<InputFilter>(HideEmoji(this))
        sendMessageClick()
    }

    private fun sendMessageClick() {
        sendMessage.setOnClickListener {
            val messageText = chatBox.text.toString()

            if (messageText.isEmpty()) {
                Toast.makeText(this, getString(R.string.Enter_message), Toast.LENGTH_LONG).show()
            } else {

                if (time > 0) {
                    time = 0
                    time = System.currentTimeMillis()
                } else {
                    time = System.currentTimeMillis()
                }
                mViewModel.sendMessage(messageText, time)
                chatBox.setText("")
            }
        }
    }

    private fun setMessageAdapter() {
        mAdapter = MessageAdapter(this, model)
        messageList.layoutManager = LinearLayoutManager(this)
        messageList.adapter = mAdapter
    }

    //.........setup tool bar..............//
    private fun settingUpToolbar() {
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        title = ""
        setSupportActionBar(toolbar)
        //set the text
        val text = findViewById<TextView>(R.id.toolbartittle)
        //text.text = model.listingname
        text.text = getString(R.string.chat)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_back_icon)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item!!.itemId == android.R.id.home) {
            mViewModel.stopBackGroundService()
            finish()
        } else if (item.itemId == R.id.addAttachment) {

            val checkSelfPermission =
                ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA)
            if (checkSelfPermission != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(
                        android.Manifest.permission.CAMERA,
                        android.Manifest.permission.WRITE_EXTERNAL_STORAGE
                    ),
                    2
                )
            } else {
                showDialog()
            }
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == 2 && grantResults[0] == 0) {
            showDialog()
        } else {
            Toast.makeText(this, getString(R.string.Accept_storage_permissions), Toast.LENGTH_LONG)
                .show()
        }
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


    @SuppressLint("IntentReset")
    private fun openGallery() {

        val intent = Intent(
            Intent.ACTION_PICK,
            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        );
        intent.setType("image/*")
        startActivityForResult(intent, 1)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.chat_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        try {
            if (requestCode == 29) {
                //   val contentURI: Bitmap = data.extras.get("data") as Bitmap
                try {

                    val resultBitmap = getRotateImage(photoFile)
                    val uri = getImageUri(resultBitmap)

//                    val uri = getImageUri(contentURI)//used for web services

                    // mbinding.img.setImageBitmap(contentURI)// we can set the image here but we want to save the image  in the server
//..................................Api FOr Update Image....................................................................//

                    mViewModel.postSelectedImage(Constant.saveBitmapToFile(uri)!!)//send the data for the api

                   // mViewModel.postSelectedImage(uri)//send the data for the api

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

                    mViewModel.postSelectedImage(file)


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
            inImage.compress(Bitmap.CompressFormat.JPEG, 90, out)
            out.flush()
            out.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return file
    }

    override fun onStop() {
        super.onStop()
        mViewModel.stopBackGroundService()
    }

}
