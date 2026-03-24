package com.xpf.p2p.activity

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.ContentUris
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import com.hjq.permissions.OnPermissionCallback
import com.hjq.permissions.XXPermissions
import com.xpf.p2p.base.BaseActivity
import com.xpf.p2p.databinding.ActivityUserInfoBinding
import com.xpf.p2p.ui.main.view.MainActivity
import com.xpf.p2p.utils.BitmapUtils
import com.xpf.p2p.utils.ToastUtil
import com.xpf.p2p.utils.UIUtils
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream

/**
 * Created by Vance on 2016/8/3 :)
 * Function:用户信息展示页
 */
class UserInfoActivity : BaseActivity<ActivityUserInfoBinding>() {

    override fun createViewBinding(inflater: LayoutInflater) = ActivityUserInfoBinding.inflate(inflater)

    override fun initData() {
        binding.titleBar.tvTitle.text = "用户信息"
        binding.titleBar.ivSetting.visibility = View.GONE

        binding.titleBar.ivBack.setOnClickListener { removeCurrentActivity() }
        binding.tvIcon.setOnClickListener { requestPermissions() }
        binding.logout.setOnClickListener { logout() }
    }

    private fun requestPermissions() {
        val perms = arrayOf(
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA
        )

        XXPermissions.with(this)
            .permission(*perms)
            .request(object : OnPermissionCallback {
                override fun onGranted(permissions: MutableList<String>, allGranted: Boolean) {
                    if (!allGranted) {
                        ToastUtil.show(applicationContext, "获取部分权限成功，但部分权限未正常授予")
                        return
                    }
                    changeIcon()
                }

                override fun onDenied(permissions: MutableList<String>, doNotAskAgain: Boolean) {
                    if (doNotAskAgain) {
                        ToastUtil.show(applicationContext, "被永久拒绝授权，请手动授予权限")
                        XXPermissions.startPermissionActivity(this@UserInfoActivity, permissions)
                    } else {
                        ToastUtil.show(applicationContext, "获取权限失败")
                    }
                }
            })
    }

    private fun logout() {
        val sp = getSharedPreferences("user_info", Context.MODE_PRIVATE)
        sp.edit().clear().apply()
        val filePath = "${cacheDir}/tx.png"
        val file = File(filePath)
        if (file.exists()) {
            file.delete()
        }
        removeAll()
        goToActivity(MainActivity::class.java, null)
    }

    private fun changeIcon() {
        AlertDialog.Builder(this)
            .setTitle("图片来源")
            .setItems(arrayOf("相机", "图库")) { _, which ->
                when (which) {
                    0 -> {
                        val camera = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                        @Suppress("DEPRECATION")
                        startActivityForResult(camera, CAMERA)
                    }
                    1 -> {
                        val picture = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                        @Suppress("DEPRECATION")
                        startActivityForResult(picture, PICTURE)
                    }
                }
            }.show()
    }

    @Suppress("DEPRECATION")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == CAMERA && resultCode == RESULT_OK && data != null) {
            val bundle = data.extras
            val bitmap = bundle?.get("data") as? android.graphics.Bitmap
            if (bitmap != null) {
                val zoomed = BitmapUtils.zoom(bitmap, UIUtils.dp2px(62).toFloat(), UIUtils.dp2px(62).toFloat())
                if (zoomed != null) {
                    val circleBitmap = BitmapUtils.circleBitmap(zoomed)
                    binding.ivIcon.setImageBitmap(circleBitmap)
                    try {
                        saveImage(circleBitmap)
                    } catch (e: FileNotFoundException) {
                        e.printStackTrace()
                    }
                }
            }
        } else if (requestCode == PICTURE && resultCode == RESULT_OK && data != null) {
            val selectedImage = data.data
            val pathResult = getPath(selectedImage!!)
            val decodeFile = BitmapFactory.decodeFile(pathResult)
            val zoomBitmap = BitmapUtils.zoom(decodeFile, UIUtils.dp2px(62).toFloat(), UIUtils.dp2px(62).toFloat())
            val circleImage = BitmapUtils.circleBitmap(zoomBitmap!!)
            binding.ivIcon.setImageBitmap(circleImage)
            try {
                saveImage(circleImage)
            } catch (e: FileNotFoundException) {
                e.printStackTrace()
            }
        }
    }

    private fun saveImage(bitmap: android.graphics.Bitmap) {
        val path = "${cacheDir}/tx.png"
        Log.e("TAG", "path = $path")
        try {
            val fos = FileOutputStream(path)
            bitmap.compress(android.graphics.Bitmap.CompressFormat.PNG, 100, fos)
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        }
    }

    @SuppressLint("NewApi")
    private fun getPath(uri: Uri): String? {
        val sdkVersion = Build.VERSION.SDK_INT
        if (sdkVersion >= 19) {
            Log.e("TAG", "uri auth: ${uri.authority}")
            if (isExternalStorageDocument(uri)) {
                val docId = DocumentsContract.getDocumentId(uri)
                val split = docId.split(":")
                val type = split[0]
                if ("primary".equals(type, ignoreCase = true)) {
                    @Suppress("DEPRECATION")
                    return "${Environment.getExternalStorageDirectory()}/${split[1]}"
                }
            } else if (isDownloadsDocument(uri)) {
                val id = DocumentsContract.getDocumentId(uri)
                val contentUri = ContentUris.withAppendedId(
                    Uri.parse("content://downloads/public_downloads"), id.toLong()
                )
                return getDataColumn(this, contentUri, null, null)
            } else if (isMediaDocument(uri)) {
                val docId = DocumentsContract.getDocumentId(uri)
                val split = docId.split(":")
                val type = split[0]
                val contentUri = when (type) {
                    "image" -> MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                    "video" -> MediaStore.Video.Media.EXTERNAL_CONTENT_URI
                    "audio" -> MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
                    else -> null
                }
                val selection = "_id=?"
                val selectionArgs = arrayOf(split[1])
                return getDataColumn(this, contentUri, selection, selectionArgs)
            } else if (isMedia(uri)) {
                val proj = arrayOf(MediaStore.Images.Media.DATA)
                @Suppress("DEPRECATION")
                val cursor = managedQuery(uri, proj, null, null, null)
                val columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
                cursor.moveToFirst()
                return cursor.getString(columnIndex)
            }
        } else if ("content".equals(uri.scheme, ignoreCase = true)) {
            return if (isGooglePhotosUri(uri)) uri.lastPathSegment
            else getDataColumn(this, uri, null, null)
        } else if ("file".equals(uri.scheme, ignoreCase = true)) {
            return uri.path
        }
        return null
    }

    private fun getDataColumn(context: Context, uri: Uri?, selection: String?, selectionArgs: Array<String>?): String? {
        val column = "_data"
        val projection = arrayOf(column)
        var cursor: android.database.Cursor? = null
        try {
            cursor = context.contentResolver.query(uri!!, projection, selection, selectionArgs, null)
            if (cursor != null && cursor.moveToFirst()) {
                val index = cursor.getColumnIndexOrThrow(column)
                return cursor.getString(index)
            }
        } finally {
            cursor?.close()
        }
        return null
    }

    private fun isExternalStorageDocument(uri: Uri): Boolean =
        "com.android.externalstorage.documents" == uri.authority

    companion object {
        private const val CAMERA = 1
        private const val PICTURE = 2

        @JvmStatic
        fun isDownloadsDocument(uri: Uri): Boolean =
            "com.android.providers.downloads.documents" == uri.authority

        @JvmStatic
        fun isMediaDocument(uri: Uri): Boolean =
            "com.android.providers.media.documents" == uri.authority

        @JvmStatic
        fun isMedia(uri: Uri): Boolean =
            "media" == uri.authority

        @JvmStatic
        fun isGooglePhotosUri(uri: Uri): Boolean =
            "com.google.android.apps.photos.content" == uri.authority
    }
}
