package com.example.favcars.view.activities

import android.Manifest
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.provider.MediaStore
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.favcars.R
import com.example.favcars.application.FavCarsApplication
import com.example.favcars.databinding.ActivityAddUpdateCarsBinding
import com.example.favcars.databinding.DialogCustomImageSelectionBinding
import com.example.favcars.databinding.DialogCustomListBinding
import com.example.favcars.model.entities.Car
import com.example.favcars.view.adapters.CustomListItemAdapter
import com.example.favcars.view_model.CarsViewModel
import com.example.favcars.view_model.CarsViewModelFactory
import com.example.favdish.util.Constants
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream
import java.util.*

class AddUpdateCarsActivity : AppCompatActivity(), View.OnClickListener {

    private var TAG: String = AddUpdateCarsActivity::class.java.name
    private lateinit var mBinding: ActivityAddUpdateCarsBinding
    private var mImagePath: String = ""
    private lateinit var mCustomListDialog: Dialog

    private val mFavCarsViewModelFactory: CarsViewModel by viewModels {
        CarsViewModelFactory((application as FavCarsApplication).repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding = ActivityAddUpdateCarsBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        setUpActionBar()
        mBinding.ivAddCarImage.setOnClickListener(this)
        mBinding.etType.setOnClickListener(this)
        mBinding.etEnginePower.setOnClickListener(this)
        mBinding.etPriceRange.setOnClickListener(this)
        mBinding.btSubmit.setOnClickListener(this)
    }

    private fun setUpActionBar() {
        setSupportActionBar(mBinding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        mBinding.toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
    }

    /**
     * Method to display the custom Item dialog
     */
    private fun customItemsDialog(title: String, itemsList: List<String>, selection: String) {
        mCustomListDialog = Dialog(this)
        val binding: DialogCustomListBinding = DialogCustomListBinding.inflate(layoutInflater)
        mCustomListDialog.setContentView(binding.root)

        // set the values
        binding.tvTitle.text = title
        binding.rvList.layoutManager = LinearLayoutManager(this)
        binding.rvList.adapter = CustomListItemAdapter(this, itemsList, selection)
        mCustomListDialog.show()
    }

    /**
     * Method display the custom dialog
     */
    private fun customImageDialog() {
        mCustomListDialog = Dialog(this)
        val binding: DialogCustomImageSelectionBinding =
            DialogCustomImageSelectionBinding.inflate(layoutInflater)
        binding.tvCamera.setOnClickListener {
            askPermissions(
                Manifest.permission.CAMERA,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) {
                val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                startActivityForResult(intent, CAMERA)
            }
            mCustomListDialog.dismiss()
        }
        binding.tvGallery.setOnClickListener {
            askPermissions(
                Manifest.permission.READ_EXTERNAL_STORAGE,
            ) {
                val galleryIntent =
                    Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                startActivityForResult(galleryIntent, GALLERY)
            }
            mCustomListDialog.dismiss()
        }
        mCustomListDialog.setContentView(binding.root)
        mCustomListDialog.show()
    }

    /**
     * Method to ask for permissions
     */
    private fun askPermissions(vararg permissions: String, doWork: () -> Unit) {
        Dexter.withContext(this)
            .withPermissions(
                *permissions
            ).withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
                    report?.let {
                        if (report.areAllPermissionsGranted()) {
                            doWork()
                        }
                    }
                }

                override fun onPermissionRationaleShouldBeShown(
                    permissions: MutableList<PermissionRequest>?,
                    token: PermissionToken?
                ) {
                    token?.continuePermissionRequest()
                }

            }).onSameThread().check()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == CAMERA) {
                data?.extras?.let {
                    val thumbnail: Bitmap = data.extras!!.get("data") as Bitmap
                    Glide.with(this)
                        .load(thumbnail)
                        .centerCrop()
                        .into(mBinding.ivImage)

                    //save the image to the sdcard
                    mImagePath = saveImageToInternalStorage(thumbnail)
                    Log.d(TAG, "Image saved: $mImagePath")
                    //set the image
                    mBinding.ivAddCarImage.setImageDrawable(
                        ContextCompat.getDrawable(this, R.drawable.ic_vector_edit)
                    )
                }
            } else if (requestCode == GALLERY) {
                data?.let {
                    val photoURI = data.data

                    Glide.with(this)
                        .load(photoURI)
                        .centerCrop()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .listener(object : RequestListener<Drawable> {
                            override fun onLoadFailed(
                                e: GlideException?,
                                model: Any?,
                                target: Target<Drawable>?,
                                isFirstResource: Boolean
                            ): Boolean {
                                Log.e(TAG, "Error while loading image")
                                return false;
                            }

                            override fun onResourceReady(
                                resource: Drawable?,
                                model: Any?,
                                target: Target<Drawable>?,
                                dataSource: DataSource?,
                                isFirstResource: Boolean
                            ): Boolean {
                                resource?.let {
                                    val bitmap: Bitmap = resource.toBitmap()
                                    //save the image to the sdcard
                                    mImagePath = saveImageToInternalStorage(bitmap)
                                }
                                return false
                            }

                        })
                        .into(mBinding.ivImage)

                    //Change the add icon to edit
                    mBinding.ivAddCarImage.setImageDrawable(
                        ContextCompat.getDrawable(this, R.drawable.ic_vector_edit)
                    )
                }
            }
        } else if (resultCode == Activity.RESULT_CANCELED) {
            Log.e(TAG, "Image selection cancelled")
        }
    }

    /**
     * Method to save the image to internal storage
     */
    private fun saveImageToInternalStorage(bitmap: Bitmap): String {
        val wrapper = ContextWrapper(applicationContext)
        var file = wrapper.getDir(IMAGE_DIRECTORY, Context.MODE_PRIVATE)
        file = File(file, "${UUID.randomUUID()}.jpg")
        try {
            val stream: OutputStream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
            stream.flush()
            stream.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return file.absolutePath
    }

    override fun onClick(v: View?) {
        v?.let {
            when (v.id) {
                R.id.iv_add_car_image -> {
                    customImageDialog()
                    return
                }
                R.id.et_type -> {
                    customItemsDialog(
                        resources.getString(R.string.title_select_car_type),
                        Constants.carTypes(), Constants.CAR_TYPE
                    )
                    return
                }
                R.id.et_engine_power -> {
                    customItemsDialog(
                        resources.getString(R.string.title_select_car_engine_pow),
                        Constants.carEnginePowerList(), Constants.CAR_ENGINE_POWER
                    )
                    return
                }
                R.id.et_price_range -> {
                    customItemsDialog(
                        resources.getString(R.string.title_select_car_price),
                        Constants.carPriceRange(), Constants.CAR_PRICE
                    )
                }
                //get all the details
                R.id.bt_submit -> {
                    val title = mBinding.etName.text.toString().trim { it <= ' ' }
                    val type = mBinding.etType.text.toString().trim { it <= ' ' }
                    val enginePower = mBinding.etEnginePower.text.toString().trim { it <= ' ' }
                    val description = mBinding.etDescription.text.toString().trim { it <= ' ' }
                    val price = mBinding.etPriceRange.text.toString().trim { it <= ' ' }
                    val review = mBinding.etReview.text.toString().trim { it <= ' ' }

                    //check if attributes are valid
                    when {
                        TextUtils.isEmpty(mImagePath) -> {
                            displayToast(
                                this@AddUpdateCarsActivity,
                                resources.getString(R.string.err_msg_select_car_image)
                            )
                        }
                        TextUtils.isEmpty(title) -> {
                            displayToast(
                                this@AddUpdateCarsActivity,
                                resources.getString(R.string.err_msg_name)
                            )
                        }
                        TextUtils.isEmpty(type) -> {
                            displayToast(
                                this@AddUpdateCarsActivity,
                                resources.getString(R.string.err_msg_type)
                            )
                        }
                        TextUtils.isEmpty(enginePower) -> {
                            displayToast(
                                this@AddUpdateCarsActivity,
                                resources.getString(R.string.err_msg_engine_power)
                            )
                        }
                        TextUtils.isEmpty(description) -> {
                            displayToast(
                                this@AddUpdateCarsActivity,
                                resources.getString(R.string.err_msg_description)
                            )
                        }
                        TextUtils.isEmpty(price) -> {
                            displayToast(
                                this@AddUpdateCarsActivity,
                                resources.getString(R.string.err_msg_price)
                            )
                        }

                        else -> {
                            val carDetails: Car = Car(
                                mImagePath,
                                Constants.CAR_IMAGE_SOURCE_LOCAL,
                                title,
                                type,
                                enginePower,
                                description,
                                price,
                                review,
                                false
                            )

                            mFavCarsViewModelFactory.insert(carDetails)
                            displayToast(this@AddUpdateCarsActivity, "Car added successfully!")
                            //Close the activity
                            finish()
                        }
                    }

                }
            }
        }
    }

    /**
     * Method to update the edit text with user selectable
     */
    fun selectedListItem(item: String, selection: String) {
        mCustomListDialog.dismiss()
        when (selection) {
            Constants.CAR_TYPE -> {
                mBinding.etType.setText(item.uppercase())
            }
            Constants.CAR_ENGINE_POWER -> {
                mBinding.etEnginePower.setText(item.uppercase())
            }
            Constants.CAR_PRICE -> {
                mBinding.etPriceRange.setText(item.uppercase())
            }
        }
    }

    private fun displayToast(
        context: Context,
        string: String
    ): Unit = Toast.makeText(context, string, Toast.LENGTH_SHORT).show()

    companion object {
        private const val CAMERA = 1
        private const val GALLERY = 2
        private const val IMAGE_DIRECTORY = "FavDishImages"
    }
}