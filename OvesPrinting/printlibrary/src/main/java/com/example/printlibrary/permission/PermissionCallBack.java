package com.example.printlibrary.permission;

/**
 * 权限授权回调
 * @author tianbf
 * Created by on 2016/9/26.
 */
public interface PermissionCallBack {
    void onSuccess(String permission);

    void onFailed(String permission);

}