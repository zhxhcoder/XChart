package com.zhxh.xchart;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zhxh.xchartlib.LineChart;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    LineChart lineChart1;
    LineChart lineChart2;
    LineChart lineChart3;
    List<ChartData> dataList1;

    String testResult = "[{\"logDay\":\"20151109\",\"yield\":\"0.85\"},{\"logDay\":\"20151224\",\"yield\":\"1.45\"},{\"logDay\":\"20160218\",\"yield\":\"-11.51\"},{\"logDay\":\"20160407\",\"yield\":\"-18.91\"},{\"logDay\":\"20160526\",\"yield\":\"-25.67\"},{\"logDay\":\"20160715\",\"yield\":\"-28.60\"},{\"logDay\":\"20160901\",\"yield\":\"-28.97\"},{\"logDay\":\"20161028\",\"yield\":\"-21.31\"},{\"logDay\":\"20161215\",\"yield\":\"-10.03\"},{\"logDay\":\"20170209\",\"yield\":\"18.13\"},{\"logDay\":\"20170329\",\"yield\":\"149.55\"},{\"logDay\":\"20170519\",\"yield\":\"391.25\"},{\"logDay\":\"20170710\",\"yield\":\"657.75\"},{\"logDay\":\"20170825\",\"yield\":\"1107.66\"},{\"logDay\":\"20171019\",\"yield\":\"1178.11\"},{\"logDay\":\"20171206\",\"yield\":\"1018.84\"},{\"logDay\":\"20180124\",\"yield\":\"947.24\"},{\"logDay\":\"20180320\",\"yield\":\"1083.45\"},{\"logDay\":\"20180511\",\"yield\":\"1324.60\"}]";

    List<ChartData> testDatas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lineChart1 = findViewById(R.id.lineChart1);
        lineChart2 = findViewById(R.id.lineChart2);
        lineChart3 = findViewById(R.id.lineChart3);


        testDatas = new Gson().fromJson(testResult, new TypeToken<List<ChartData>>() {
        }.getType());

        dataList1 = new ArrayList<>();
        dataList1.add(new ChartData("05-01", "0.85"));
        dataList1.add(new ChartData("05-02", "1.45"));
        dataList1.add(new ChartData("05-03", "-11.51"));
        dataList1.add(new ChartData("05-04", "-18.91"));
        dataList1.add(new ChartData("05-05", "-25.67"));
        dataList1.add(new ChartData("05-06", "-8.8"));
        dataList1.add(new ChartData("05-07", "1178.11"));
        dataList1.add(new ChartData("05-08", "1018.84"));
        dataList1.add(new ChartData("05-09", "947.24"));
        dataList1.add(new ChartData("05-10", "1324.60"));

        lineChart1.bindData(testDatas);
        lineChart1.show();

        lineChart2.bindData(dataList1);
        lineChart2.bindYUnit("万");
        lineChart2.bindYFormat("%.1f");
        lineChart2.show();

        lineChart3.bindData(dataList1);
        lineChart3.show();

    }
}
