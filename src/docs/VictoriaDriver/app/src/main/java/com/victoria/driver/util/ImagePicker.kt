package com.ridewithme.util


import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.*
import android.media.ExifInterface
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.support.design.widget.BottomSheetDialogFragment
import android.support.v4.app.ActivityCompat
import android.support.v4.content.FileProvider
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import butterknife.ButterKnife
import com.victoria.driver.BuildConfig
import com.victoria.driver.R
import com.victoria.driver.core.Common
import com.victoria.driver.util.PermissionUtil
import kotlinx.android.synthetic.main.image_picker_bottom_sheet_layout.*
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class ImagePicker : BottomSheetDialogFragment() {


    private var mCurrentPhotoPath: String? = null

    private lateinit var imagePickerResult: ImagePickerResult


    private val filename: String
        get() {
            val file = File(Environment.getExternalStorageDirectory().path, "RideWithMe/images")
            if (!file.exists()) {
                file.mkdirs()
            }
            return file.absolutePath + "/" + System.currentTimeMillis() + ".jpg"

        }

    fun setImagePickerResult(imagePickerResult: ImagePickerResult) {
        this.imagePickerResult = imagePickerResult

    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.image_picker_bottom_sheet_layout, container)
        ButterKnife.bind(this, view)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        camera.setOnClickListener({ this.onClick(it) })
        gallery.setOnClickListener({ this.onClick(it) })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        retainInstance = true

        /* if (savedInstanceState != null) {
            if (savedInstanceState.containsKey("Path")) {

                mCurrentPhotoPath = savedInstanceState.getString("Path");
            }
        }*/

    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        //    outState.putString("Path", mCurrentPhotoPath);


    }


    override fun onDestroyView() {
        super.onDestroyView()

    }

    fun onClick(view: View) {
        when (view.id) {
            R.id.camera ->
                if (Build.VERSION.SDK_INT >= 23) {

                    if (ActivityCompat.checkSelfPermission(activity!!,
                                    Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(activity!!, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(activity!!, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        requestPermissions(Common.PERMISSIONS_CAMERA, Common.REQUEST_CAMERA_PERMISSION)
                    } else {
                        dispatchTakePictureIntent()
                    }

                } else
                    dispatchTakePictureIntent()
            R.id.gallery ->


                if (Build.VERSION.SDK_INT >= 23) {

                    if (ActivityCompat.checkSelfPermission(activity!!, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(activity!!, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        requestPermissions(Common.PERMISSIONS_GALLERY, Common.REQUEST_GALLERY_PERMISSION)
                    } else {
                        openGallery()
                    }

                } else
                    openGallery()
        }

    }

    private fun openGallery() {
        val i = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(i, Common.RequestCode.RESULT_LOAD_IMAGE)
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
                photoURI = FileProvider.getUriForFile(context!!, BuildConfig.APPLICATION_ID, photoFile)
            } catch (ex: IOException) {
                Log.e("TakePicture", ex.message)
            }

            // Continue only if the File was successfully created
            if (photoFile != null) {
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                startActivityForResult(takePictureIntent, Common.RequestCode.REQUEST_TAKE_PHOTO)
            }
        }
    }

    @Throws(IOException::class)
    private fun createImageFile(): File {
        // Create an image_bg file name
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
        /*mCurrentPhotoPath = "file:" + image_bg.getAbsolutePath();*/
        mCurrentPhotoPath = image.absolutePath
        return image
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        if (requestCode == Common.RequestCode.REQUEST_TAKE_PHOTO && resultCode == Activity.RESULT_OK) {

            if (mCurrentPhotoPath != null) {
                imagePickerResult.onResult(compressImage(mCurrentPhotoPath!!))
                dismiss()
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
                        if (mCurrentPhotoPath != null)
                            imagePickerResult.onResult(compressImage(mCurrentPhotoPath!!))
                    }

                }
                dismiss()
            }
        }

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
                /* Log.i("Main Activity", "Contacts permissions were NOT granted.");*/
            }

        } else if (requestCode == Common.REQUEST_GALLERY_PERMISSION) {

            if (PermissionUtil.verifyPermissions(grantResults)) {
                // All required permissions have been granted, display contacts fragment.
                /*startService();*/
                openGallery()
            } else {
                /* Log.i("Main Activity", "Contacts permissions were NOT granted.");*/
            }

        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }

    interface ImagePickerResult {
        fun onResult(path: String)
    }


    private fun compressImage(filePath: String): String {

        var scaledBitmap: Bitmap? = null

        val options = BitmapFactory.Options()

        //      by setting this field as true, the actual bitmap pixels are not loaded in the memory. Just the bounds are loaded. If
        //      you try the use the bitmap here, you will get null.
        options.inJustDecodeBounds = true
        var bmp = BitmapFactory.decodeFile(filePath, options)

        var actualHeight = options.outHeight
        var actualWidth = options.outWidth

        //      max Height and width values of the compressed image_bg is taken as 816x612

        val maxHeight = 1920.0f//1280.0f;//816.0f;
        val maxWidth = 1080.0f//852.0f;//612.0f;

        var imgRatio = (actualWidth / actualHeight).toFloat()
        val maxRatio = maxWidth / maxHeight

        //      width and height values are set maintaining the aspect ratio of the image_bg

        if (actualHeight > maxHeight || actualWidth > maxWidth) {
            if (imgRatio < maxRatio) {
                imgRatio = maxHeight / actualHeight
                actualWidth = (imgRatio * actualWidth).toInt()
                actualHeight = maxHeight.toInt()
            } else if (imgRatio > maxRatio) {
                imgRatio = maxWidth / actualWidth
                actualHeight = (imgRatio * actualHeight).toInt()
                actualWidth = maxWidth.toInt()
            } else {
                actualHeight = maxHeight.toInt()
                actualWidth = maxWidth.toInt()

            }
        }

        //      setting inSampleSize value allows to load a scaled down version of the original image_bg

        options.inSampleSize = calculateInSampleSize(options, actualWidth, actualHeight)

        //      inJustDecodeBounds set to false to load the actual bitmap
        options.inJustDecodeBounds = false

        //      this options allow android to claim the bitmap memory if it runs low on memory
        options.inPurgeable = true
        options.inInputShareable = true
        options.inTempStorage = ByteArray(16 * 1024)

        try {
            //          load the bitmap from its path
            bmp = BitmapFactory.decodeFile(filePath, options)
        } catch (exception: OutOfMemoryError) {
            exception.printStackTrace()

        }

        try {
            scaledBitmap = Bitmap.createBitmap(actualWidth, actualHeight, Bitmap.Config.ARGB_8888)
        } catch (exception: OutOfMemoryError) {
            exception.printStackTrace()
        }

        val ratioX = actualWidth / options.outWidth.toFloat()
        val ratioY = actualHeight / options.outHeight.toFloat()
        val middleX = actualWidth / 2.0f
        val middleY = actualHeight / 2.0f

        val scaleMatrix = Matrix()
        scaleMatrix.setScale(ratioX, ratioY, middleX, middleY)

        val canvas = Canvas(scaledBitmap!!)
        canvas.matrix = scaleMatrix
        canvas.drawBitmap(bmp, middleX - bmp.width / 2, middleY - bmp.height / 2, Paint(Paint.FILTER_BITMAP_FLAG))

        //      check the rotation of the image_bg and display it properly
        val exif: ExifInterface
        try {
            exif = ExifInterface(filePath)

            val orientation = exif.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION, 0)
            Log.d("EXIF", "Exif: $orientation")
            val matrix = Matrix()
            if (orientation == 6) {
                matrix.postRotate(90f)
                Log.d("EXIF", "Exif: $orientation")
            } else if (orientation == 3) {
                matrix.postRotate(180f)
                Log.d("EXIF", "Exif: $orientation")
            } else if (orientation == 8) {
                matrix.postRotate(270f)
                Log.d("EXIF", "Exif: $orientation")
            }
            scaledBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0,
                    scaledBitmap.width, scaledBitmap.height, matrix,
                    true)
        } catch (e: IOException) {
            e.printStackTrace()
        }

        var out: FileOutputStream? = null
        val filename = filename
        try {
            out = FileOutputStream(filename)

            //          write the compressed bitmap at the destination specified by filename.
            scaledBitmap!!.compress(Bitmap.CompressFormat.JPEG, 90, out)

        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        }

        return filename

    }


    private fun calculateInSampleSize(options: BitmapFactory.Options, reqWidth: Int, reqHeight: Int): Int {
        val height = options.outHeight
        val width = options.outWidth
        var inSampleSize = 1

        if (height > reqHeight || width > reqWidth) {
            val heightRatio = Math.round(height.toFloat() / reqHeight.toFloat())
            val widthRatio = Math.round(width.toFloat() / reqWidth.toFloat())
            inSampleSize = if (heightRatio < widthRatio) heightRatio else widthRatio
        }
        val totalPixels = (width * height).toFloat()
        val totalReqPixelsCap = (reqWidth * reqHeight * 2).toFloat()
        while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
            inSampleSize++
        }

        return inSampleSize
    }

}