package com.gzf01.rxzsmbclient.model.helper;

import android.content.Context;

import com.gzf01.rxzmvvm.global.Rxzmvvm;
import com.gzf01.rxzsmbclient.model.api.OnPermissionCallBack;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

/**
 * Title: PermissionHelper 类 <br/>
 * Description:  <br/>
 *
 * @version 0.0.1
 * @since 0.0.1
 */
public class PermissionHelper {
    public static void requestPermission(Context context, String permission, OnPermissionCallBack callBack){
        Dexter.withContext(context)
                .withPermission(permission)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        callBack.success();
                    }
                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {
                        Rxzmvvm.toastShow("授权失败");
                    }
                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                        Rxzmvvm.toastShow("需要再次获取权限");
                    }
                }).check();
    }
}
