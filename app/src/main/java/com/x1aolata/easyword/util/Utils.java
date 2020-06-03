package com.x1aolata.easyword.util;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Build;
import android.util.Base64;
import android.util.Log;
import android.view.WindowManager;
import android.widget.EditText;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * @author x1aolata
 * @date 2020/5/30 13:33
 * @script 工具类
 */
public class Utils {

    public static boolean verificationGreater(EditText editText, int length) {

        return editText.getText().toString().length() >= length;

    }

    public static boolean verificationEqual(EditText editText, int length) {

        return editText.getText().toString().length() >= length;

    }


    public static void setTranslucent(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // 设置状态栏透明
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }

    /**
     * 判断网络状态
     *
     * @param context
     * @return
     */
    public static boolean isConnected(Context context) {
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (manager != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                NetworkCapabilities networkCapabilities = manager.getNetworkCapabilities(manager.getActiveNetwork());
                if (networkCapabilities != null) {
                    return networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
                            || networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)
                            || networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET);
                }
            } else {
                NetworkInfo networkInfo = manager.getActiveNetworkInfo();
                return networkInfo != null && networkInfo.isConnected();
            }
        }
        return false;
    }


    /**
     * Bitmap 转成 Base64
     *
     * @param bitmap
     * @return
     */
    public static String image2Base64(Bitmap bitmap) {
        //以防解析错误之后bitmap为null
        if (bitmap == null)
            return "解析异常";
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        //此步骤为将bitmap进行压缩，选择了原格式png，
        // 第二个参数为压缩质量，我选择了原画质，也就是100，
        // 第三个参数传入outputstream去写入压缩后的数据
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
        try {
            outputStream.flush();
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //将获取到的outputstream转换成byte数组
        byte[] bytes = outputStream.toByteArray();
        //android.util包下有Base64工具类，直接调用，格式选择Base64.DEFAULT即可
        String str = Base64.encodeToString(bytes, Base64.DEFAULT);
        //打印数据，下面计算用
        Log.i("MyLog", "imageToBase64: " + str.length());
        return str;
    }


    /**
     * 将Base64转成Bitmap
     *
     * @param s
     * @return
     */
    public static Bitmap base642Image(String s) {
        //用base64.decode解析编码
        byte[] bytes = Base64.decode(s, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }


}
