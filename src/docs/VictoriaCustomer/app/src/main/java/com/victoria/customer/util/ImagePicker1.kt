package com.victoria.customer.util

import android.Manifest
import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.support.v4.app.ActivityCompat
import android.support.v4.app.DialogFragment
import android.support.v4.content.FileProvider
import android.support.v7.widget.AppCompatTextView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import com.victoria.customer.BuildConfig
import com.victoria.customer.R
import com.victoria.customer.core.Common
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


class ImagePicker1 : DialogFragment(), View.OnClickListener {

    private lateinit var imagegallary:AppCompatTextView
    private lateinit var imageCamera:AppCompatTextView

    private var cropMode = 0
    private var mCurrentPhotoPath: String? = null

    private var imagePickerResult: ImagePicker1.ImagePickerResult? = null


    private val file: File? = null


    fun setImagePickerResult(imagePickerResult: ImagePicker1.ImagePickerResult) {
        this.imagePickerResult = imagePickerResult

    }

    fun setCropMode(cropMode: Int) {
        this.cropMode = cropMode
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.dialog_image_picker_with_img, container)

        imagegallary=view.findViewById(R.id.txtGallary)
        imageCamera=view.findViewById(R.id.txtCamera)
        dialog.window?.requestFeature(Window.FEATURE_NO_TITLE)
        dialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

        imagegallary.setOnClickListener(this::onClick)
        imageCamera.setOnClickListener(this::onClick)

        return view
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        retainInstance = true


    }


    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.txtCamera -> {

                if (Build.VERSION.SDK_INT >= 23) {
                    if (ActivityCompat.checkSelfPermission(activity!!, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
                            ActivityCompat.checkSelfPermission(activity!!, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                            || ActivityCompat.checkSelfPermission(activity!!, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        requestPermissions(Common.PERMISSIONS_CAMERA, Common.REQUEST_CAMERA_PERMISSION);
                    } else {
                        dispatchTakePictureIntent();
                    }

                } else dispatchTakePictureIntent();

            }
            R.id.txtGallary -> {


                if (Build.VERSION.SDK_INT >= 23) {

                    if (ActivityCompat.checkSelfPermission(activity!!, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                            || ActivityCompat.checkSelfPermission(activity!!, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        requestPermissions(Common.PERMISSIONS_GALLERY, Common.REQUEST_GALLERY_PERMISSION);
                    } else {
                        openGallory();
                    }

                } else openGallory();

            }

        }
    }


    private fun openGallory() {
        try {
            val i = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(i, Common.RequestCode.RESULT_LOAD_IMAGE)
        } catch (e: Exception) {
        }

    }


    private fun dispatchTakePictureIntent() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(activity!!.packageManager) != null) {
            // Create the File where the photo should go
            var photoFile: File? = null

            var photoURI: Uri? = null

            try {
                photoFile = createImageFile()
                photoURI = FileProvider.getUriForFile(context!!, BuildConfig.APPLICATION_ID + ".provider", photoFile)
            } catch (ex: IOException) {

            }

            // Continue only if the File was successfully created
            if (photoFile != null) {
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                /*     takePictureIntent.putExtra("crop", "true");
                //indicate aspect of desired crop
                takePictureIntent.putExtra("aspectX", 1);
                takePictureIntent.putExtra("aspectY", 1);
                //indicate output X and Y
                takePictureIntent.putExtra("outputX", 256);
                takePictureIntent.putExtra("outputY", 256);
                //retrieve data on return
                takePictureIntent.putExtra("return-data", true);*/
                startActivityForResult(takePictureIntent, Common.RequestCode.REQUEST_TAKE_PHOTO)
            }
        }
    }

    @Throws(IOException::class)
    private fun createImageFile(): File {
        // Create an image file name
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val imageFileName = "JPEG_" + timeStamp + "_"
        val storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES)
        val image = File.createTempFile(
                imageFileName, /* prefix */
                ".jpg", /* suffix */
                storageDir      /* directory */
        )

        // Save a file: path for use with ACTION_VIEW intents
        /*mCurrentPhotoPath = "file:" + image.getAbsolutePath();*/
        mCurrentPhotoPath = image.absolutePath
        return image
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        if (requestCode == Common.RequestCode.REQUEST_TAKE_PHOTO && resultCode == Activity.RESULT_OK) {

            if (mCurrentPhotoPath != null) {
                imagePickerResult?.onResult(mCurrentPhotoPath!!);
                dismiss();
                //cropImage(mCurrentPhotoPath)
            }
        } else if (requestCode == Common.RequestCode.RESULT_LOAD_IMAGE && resultCode == Activity.RESULT_OK) {

            if (data != null) {
                val selectedImage = data.data
                if (selectedImage != null) {
                    val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)
                    val cursor = activity!!.contentResolver.query(selectedImage, filePathColumn, null, null, null)
                    if (cursor != null) {
                        cursor.moveToFirst()
                        val columnIndex = cursor.getColumnIndex(filePathColumn[0])
                        mCurrentPhotoPath = cursor.getString(columnIndex)
                        cursor.close()
                        imagePickerResult?.onResult(mCurrentPhotoPath!!);
                        dismiss();
                        //cropImage(mCurrentPhotoPath)
                    }
                }
            }
        } /*else if (requestCode == UCrop.REQUEST_CROP) {
            if (data != null) {
                try {
                    final Uri resultUri = UCrop.getOutput(data);
                    if (resultUri != null) {
                        file = new File(resultUri.getPath());

                        if(imagePickerResult!=null)
                        imagePickerResult.onResult(resultUri.getPath());
                        dismiss();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }*/

    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if (requestCode == Common.REQUEST_CAMERA_PERMISSION) {


            // We have requested multiple permissions for contacts, so all of them need to be
            // checked.

            if (PermissionUtil.verifyPermissions(grantResults)) {
                // All required permissions have been granted, display contacts fragment.
                /*startService();*/
                dispatchTakePictureIntent()

            } else {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE) || !shouldShowRequestPermissionRationale(Manifest.permission.CAMERA) || !shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE)) {    // Comment 2.

                    if (imagePickerResult != null) {
                        imagePickerResult!!.onError(getString(R.string.validation_for_storage_permission), dialog)
                    }

                }


            }

        } else if (requestCode == Common.REQUEST_GALLERY_PERMISSION) {

            if (PermissionUtil.verifyPermissions(grantResults)) {
                // All required permissions have been granted, display contacts fragment.
                /*startService();*/
                openGallory()
            } else {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE) || !shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {    // Comment 2.
                    if (imagePickerResult != null)
                        imagePickerResult!!.onError(getString(R.string.validation_for_storage_permission), dialog)
                }


            }

        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }

    /* private void cropImage(String mCurrentPhotoPath) {
        if (mCurrentPhotoPath != null && !(mCurrentPhotoPath.isEmpty())) {
            //Log.e("File::",mCurrentPhotoPath);
            UCrop.Options option = new UCrop.Options();

            option.setAspectRatioOptions(0,
                    new AspectRatio("1:1", 1, 1));
            option.setFreeStyleCropEnabled(false);
            option.setToolbarColor(ContextCompat.getColor(Objects.requireNonNull(getContext()), R.color.appColor));
            option.setToolbarTitle(getString(R.string.app_name));
            option.setStatusBarColor(getContext().getResources().getColor(R.color.appColorDark));
            option.setToolbarWidgetColor(getContext().getResources().getColor(R.color.app_white));
            try {
                UCrop.of(Uri.fromFile(new File(mCurrentPhotoPath)), Uri.fromFile(File.createTempFile("image_", ".jpg", getContext().getCacheDir()))).withOptions(option).start(getContext(), ImagePicker1.this, UCrop.REQUEST_CROP);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }




*/

    interface ImagePickerResult {
        fun onResult(path: String)
        fun onError(error: String, dialog: Dialog)
    }

}
