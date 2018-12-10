package com.zhxh.xchart;

import android.util.Log;

import com.google.gson.Gson;
import com.zhxh.xchart.dummy.PayResultCallBackData;

import java.util.ArrayList;
import java.util.List;

public class CommonUtils {
    public static void main(String[] args) {

        PayResultCallBackData data = new PayResultCallBackData();
        data.setType(1);
        String json = new Gson().toJson(data);

        Log.d("coursePaymentCallBack", "json " + json );
        Log.d("coursePaymentCallBack", "javascript:coursePaymentCallBack(" + json + ")");



    }


}