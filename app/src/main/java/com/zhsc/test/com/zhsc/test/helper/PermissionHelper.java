package com.zhsc.test.com.zhsc.test.helper;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import com.zhsc.test.com.zhsc.test.util.PermissionUtil;
import com.zhsc.test.impl.PermissionInterface;

/**
 * 动态权限帮助类
 */
public class PermissionHelper {

    private Activity activity;
    private PermissionInterface permissionInterface;
    private String permission;
    private int callBackCode;


    public PermissionHelper(Activity activity, PermissionInterface permissionInterface) {
        this.activity = activity;
        this.permissionInterface = permissionInterface;
    }

    /**
     * 请求权限
     */
    public void requestPermissions() {
        String[] deniedPermissions = PermissionUtil.getDeniedPermissions(activity,permissionInterface.getPermissions());
        if (deniedPermissions != null && deniedPermissions.length>0)
            PermissionUtil.requestPermissions(activity,deniedPermissions,permissionInterface.getPermissionRequestCode());
        else
            permissionInterface.requestPermissionsSuccess();
    }

    /**
     * 在Activity中的onRequestPermissionsResult中调用,用来接收结果判断
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    public boolean requestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == permissionInterface.getPermissionRequestCode()) {
            boolean isAllGranted = true;
            for (int result : grantResults) {
                if (result == PackageManager.PERMISSION_DENIED) {
                    isAllGranted = false;
                    break;
                }
            }
            if (isAllGranted == false){
                permissionInterface.requestPermissionsFail();
            }else
                permissionInterface.requestPermissionsSuccess();
            return true;
        }
        return false;
    }
}
