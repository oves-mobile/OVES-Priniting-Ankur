package com.example.printlibrary.permission;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;


/**
 * @author tianbf
 * 权限管理中心
 * Created by on 2016/9/26.
 */
public class PermissionCenter {

    private Activity mActivity;

    public PermissionCenter(Activity activity) {
        mActivity = activity;
    }

    private static final int REQUEST_EXTERNAL_STORAGE = 1;

    private static String[] PERMISSIONS_STORAGE = {
            "android.permission.BLUETOOTH",
            "android.permission.BLUETOOTH_ADMIN"};

    public void verifyStoragePermissions(Activity activity) {

        try {

            int permission = ActivityCompat.checkSelfPermission(activity, "android.permission.BLUETOOTH");
            if (permission != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE, REQUEST_EXTERNAL_STORAGE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String[] permissions;
    private AlertDialog dialog;


    public void setOnActivityResult(int requestCode) {
        if (requestCode == 123) {

            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                // Check whether the permission has been obtained
                int i = ContextCompat.checkSelfPermission(mActivity, permissions[0]);
                // 权限是否已经 授权 GRANTED---授权  DINIED---拒绝
                if (i != PackageManager.PERMISSION_GRANTED) {
                    // 提示用户应该去应用设置界面手动开启权限
                    showDialogTipUserGoToAppSetting();
                } else {
                    if (dialog != null && dialog.isShowing()) {
                        dialog.dismiss();
                    }
                    Toast.makeText(mActivity, "权限获取成功", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    // 提示用户去应用设置界面手动开启权限

    private void showDialogTipUserGoToAppSetting() {

        dialog = new AlertDialog.Builder(mActivity)
                .setTitle("存储权限不可用") //Storage permissions are not available
                .setMessage("请在-应用设置-权限-中，允许志众打印使用存储权限来保存用户数据") //Please in-Application Settings-Permissions-to allow Zhizhong Printing to use storage permissions to save user data
                .setPositiveButton("立即开启", new DialogInterface.OnClickListener() { //Open now
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Jump to the application settings interface
                        goToAppSetting();
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //finish();
                    }
                }).setCancelable(false).show();
    }

    // 跳转到当前应用的设置界面
    private void goToAppSetting() {
        Intent intent = new Intent();

        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", mActivity.getPackageName(), null);
        intent.setData(uri);

        mActivity.startActivityForResult(intent, 123);
    }

    public void setOnRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 321) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    // 判断用户是否 点击了不再提醒。(检测该权限是否还可以申请)
                    boolean b = mActivity.shouldShowRequestPermissionRationale(permissions[0]);
                    if (!b) {
                        // 用户还是想用我的 APP 的
                        // 提示用户去应用设置界面手动开启权限
                        showDialogTipUserGoToAppSetting();
                    }
                    mActivity.finish();
                } else {
                    // Toast.makeText(mActivity, "权限获取成功", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    public void permissionCheck(String[] permissions) {
        this.permissions = permissions;
        // 版本判断。当手机系统大于 23 时，才有必要去判断权限是否获取
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            // 检查该权限是否已经获取
            int i = ContextCompat.checkSelfPermission(mActivity, permissions[0]);
            // 权限是否已经 授权 GRANTED---授权  DINIED---拒绝
            if (i != PackageManager.PERMISSION_GRANTED) {
                // 如果没有授予该权限，就去提示用户请求
                showDialogTipUserRequestPermission();
            }
        }
    }

    // 提示用户该请求权限的弹出框
    private void showDialogTipUserRequestPermission() {

        startRequestPermission();//不弹提示直接获取权限

        /*new AlertDialog.Builder(mActivity)
                .setTitle("存储权限不可用")
                .setMessage("由于志众打印需要获取存储空间，为你存储个人信息；\n否则，您将无法正常存储模板")
                .setPositiveButton("立即开启", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startRequestPermission();
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mActivity.finish();
                    }
                }).setCancelable(false).show();*/
    }

    // 开始提交请求权限
    private void startRequestPermission() {
        ActivityCompat.requestPermissions(mActivity, permissions, 321);
    }

}