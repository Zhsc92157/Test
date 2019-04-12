package com.zhsc.test.util;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.content.ContextCompat;

import java.util.ArrayList;

public class PermissionUtil {

    public static boolean hasPermission(Context context,String permission){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(context, permission) !=
                    PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    public static void requestPermissions(Activity mActivity,String[] permissions,int requestCode){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            mActivity.requestPermissions(permissions, requestCode);
        }
    }

    public static String[] getDeniedPermissions(Context context,String[] permissions){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            ArrayList<String> deniedPermissionsList = new ArrayList<>();
            for(String permission:permissions){
                if(context.checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED);
                    deniedPermissionsList.add(permission);
            }
            int size = deniedPermissionsList.size();
            if (size > 0)
                return deniedPermissionsList.toArray(new String[deniedPermissionsList.size()]);
            else
                return null;
        }
        return null;
    }

}
