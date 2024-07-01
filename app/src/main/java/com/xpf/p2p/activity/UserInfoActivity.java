package com.xpf.p2p.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.hjq.permissions.OnPermissionCallback;
import com.hjq.permissions.XXPermissions;
import com.xpf.p2p.R;
import com.xpf.p2p.base.BaseActivity;
import com.xpf.p2p.ui.main.view.MainActivity;
import com.xpf.p2p.utils.BitmapUtils;
import com.xpf.p2p.utils.ToastUtil;
import com.xpf.p2p.utils.UIUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.List;


/**
 * Created by Vance on 2016/8/3 :)
 * Function:用户信息展示页
 * {@link # https://github.com/xinpengfei520/P2P}
 */
public class UserInfoActivity extends BaseActivity {

    private static final int CAMERA = 1;
    private static final int PICTURE = 2;

    ImageView ivBack;
    TextView tvTitle;
    ImageView ivSetting;
    ImageView ivIcon;
    TextView tvIcon;
    Button logout;

    @Override
    protected void initData() {
        ivBack = findViewById(R.id.iv_back);
        tvTitle = findViewById(R.id.tv_title);
        ivSetting = findViewById(R.id.iv_setting);
        ivIcon = findViewById(R.id.iv_icon);
        logout = findViewById(R.id.logout);
        tvTitle.setText("用户信息");
        ivSetting.setVisibility(View.GONE);

        ivBack.setOnClickListener(v -> removeCurrentActivity());
        tvIcon.setOnClickListener(v -> requestPermissions());
        logout.setOnClickListener(this::logout);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_user_info;
    }

    private void requestPermissions() {
        String[] perms = {
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA};

        XXPermissions.with(this)
                .permission(perms)
                .request(new OnPermissionCallback() {

                    @Override
                    public void onGranted(@NonNull List<String> permissions, boolean allGranted) {
                        if (!allGranted) {
                            ToastUtil.show(getApplicationContext(), "获取部分权限成功，但部分权限未正常授予");
                            return;
                        }
                        changeIcon();
                    }

                    @Override
                    public void onDenied(@NonNull List<String> permissions, boolean doNotAskAgain) {
                        if (doNotAskAgain) {
                            ToastUtil.show(getApplicationContext(), "被永久拒绝授权，请手动授予权限");
                            // 如果是被永久拒绝就跳转到应用权限系统设置页面
                            XXPermissions.startPermissionActivity(UserInfoActivity.this, permissions);
                        } else {
                            ToastUtil.show(getApplicationContext(), "获取权限失败");
                        }
                    }
                });
    }

    private void logout(View view) {
        // 1.清空本地存储的用户数据：sp存储中保存的用户信息。本地用户头像
        // 1.1sp存储中保存的用户信息的清除
        SharedPreferences sp = this.getSharedPreferences("user_info", Context.MODE_PRIVATE);
        sp.edit().clear().commit();//清空sp存储的数据。（user_info.xml仍然存在，但是内部没有数据）
        //2.本地用户头像文件的删除
        String filePath = this.getCacheDir() + "/tx.png";
        File file = new File(filePath);
        if (file.exists()) {
            file.delete();
        }
        // 2.结束当前Activity的显示
        this.removeAll();
        this.goToActivity(MainActivity.class, null);
    }

    private void changeIcon() {
        new AlertDialog.Builder(this)
                .setTitle("图片来源")
                .setItems(new String[]{"相机", "图库"}, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0: // 相机
                                // 打开系统拍照程序,选择拍照图片
                                Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                startActivityForResult(camera, CAMERA);
                                break;
                            case 1: // 图库
                                // 打开系统图库程序,选择图片
                                Intent picture = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                startActivityForResult(picture, PICTURE);
                                break;
                        }
                    }
                }).show();
    }

    //Bimap:对应图片在内存中的对象
    //掌握：存储--->内存：BitmapFactory.decodeFile(String filePath)
    //                  BitmapFactory.decodeStream(InputStream is)
    //     内存--->存储：bitmap.compress(Bitmap.CompressFormat.PNG,100,OutputStream os);
    // 带回调的启动新的acitivity之后的回调方法
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CAMERA && resultCode == RESULT_OK && data != null) {

            // 拍照
            Bundle bundle = data.getExtras();
            // 获取相机返回的数据，并转换为图片格式
            Bitmap bitmap = (Bitmap) bundle.get("data");
            // bitmap圆形裁剪
            if (bitmap != null) {
                bitmap = BitmapUtils.zoom(bitmap, UIUtils.dp2px(62), UIUtils.dp2px(62));
                if (bitmap != null) {
                    Bitmap circleBitmap = BitmapUtils.circleBitmap(bitmap);
                    // 真是项目中是要上传到服务器的
                    ivIcon.setImageBitmap(circleBitmap);
                    // 将图片保存在本地
                    try {
                        saveImage(circleBitmap);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            }
        } else if (requestCode == PICTURE && resultCode == RESULT_OK && data != null) {
            //图库
            Uri selectedImage = data.getData();
            //这里返回的uri情况就有点多了
            //**:在4.4.2之前返回的uri是:content://media/external/images/media/3951或者file://....在4.4.2返回的是content://com.android.providers.media.documents/document/image:3951或者
            //总结：uri的组成，eg:content://com.example.project:200/folder/subfolder/etc
            //content:--->"scheme"
            //com.example.project:200-->"host":"port"--->"authority"[主机地址+端口(省略) =authority]
            //folder/subfolder/etc-->"path" 路径部分
            //android各个不同的系统版本,对于获取外部存储上的资源，返回的Uri对象都可能各不一样,所以要保证无论是哪个系统版本都能正确获取到图片资源的话
            //就需要针对各种情况进行一个处理了
            String pathResult = getPath(selectedImage);

            Bitmap decodeFile = BitmapFactory.decodeFile(pathResult);
            Bitmap zoomBitmap = BitmapUtils.zoom(decodeFile, UIUtils.dp2px(62), UIUtils.dp2px(62));
            // bitmap圆形裁剪p
            Bitmap circleImage = BitmapUtils.circleBitmap(zoomBitmap);
            // 真实项目当中，是需要上传到服务器的..这步我们就不做了。
            ivIcon.setImageBitmap(circleImage);
            try {
                // 保存图片到本地
                saveImage(circleImage);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    // 将修改后的图片保存在本地存储中：storage/sdcard/Android/data/应用包名/files/xxx.png
    private void saveImage(Bitmap bitmap) throws FileNotFoundException {

        String path = this.getCacheDir() + "/tx.png";
        Log.e("TAG", "path = " + path);
        try {
            FileOutputStream fos = new FileOutputStream(path);
            //bitmap压缩(压缩格式、质量、压缩文件保存的位置)
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

//        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
//
//            File externalFilesDir = this.getExternalFilesDir(null);
//            File file = new File(externalFilesDir, "icon.png");
//            // 将Bitmap持久化
//            circleBitmap.compress(Bitmap.CompressFormat.PNG, 100, new FileOutputStream(file));
//        }
    }

    // 根据系统相册选择的文件获取路径
    @SuppressLint("NewApi")
    private String getPath(Uri uri) {
        int sdkVersion = Build.VERSION.SDK_INT;
        //高于4.4.2的版本
        if (sdkVersion >= 19) {
            Log.e("TAG", "uri auth: " + uri.getAuthority());
            if (isExternalStorageDocument(uri)) {
                String docId = DocumentsContract.getDocumentId(uri);
                String[] split = docId.split(":");
                String type = split[0];
                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }
            } else if (isDownloadsDocument(uri)) {
                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"),
                        Long.valueOf(id));
                return getDataColumn(this, contentUri, null, null);
            } else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }
                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{split[1]};
                return getDataColumn(this, contentUri, selection, selectionArgs);
            } else if (isMedia(uri)) {
                String[] proj = {MediaStore.Images.Media.DATA};
                Cursor actualimagecursor = this.managedQuery(uri, proj, null, null, null);
                int actual_image_column_index = actualimagecursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                actualimagecursor.moveToFirst();
                return actualimagecursor.getString(actual_image_column_index);
            }
        } else if ("content".equalsIgnoreCase(uri.getScheme())) {
            // Return the remote address
            if (isGooglePhotosUri(uri)) {
                return uri.getLastPathSegment();
            }
            return getDataColumn(this, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }
        return null;
    }

    private String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {
        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {column};
        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return null;
    }


    private boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    public static boolean isMedia(Uri uri) {
        return "media".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }
}
