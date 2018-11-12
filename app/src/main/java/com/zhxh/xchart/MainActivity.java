package com.zhxh.xchart;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zhxh.xchart.dummy.ChartData;
import com.zhxh.xchartlib.LineChart;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    LineChart lineChartSmall;
    LineChart lineChart0;
    LineChart lineChart2;
    LineChart lineChart3;
    List<ChartData> dataList1;

    String testResult = "[{\"logDay\":\"20160912\",\"yield\":\"0\"},{\"logDay\":\"20161021\",\"yield\":\"126.73\"},{\"logDay\":\"20161123\",\"yield\":\"170.76\"},{\"logDay\":\"20161226\",\"yield\":\"251.48\"},{\"logDay\":\"20170203\",\"yield\":\"260.03\"},{\"logDay\":\"20170308\",\"yield\":\"282.79\"},{\"logDay\":\"20170412\",\"yield\":\"283.97\"},{\"logDay\":\"20170516\",\"yield\":\"367.10\"},{\"logDay\":\"20170620\",\"yield\":\"275.20\"},{\"logDay\":\"20170721\",\"yield\":\"148.27\"},{\"logDay\":\"20170823\",\"yield\":\"279.44\"},{\"logDay\":\"20170925\",\"yield\":\"401.01\"},{\"logDay\":\"20171102\",\"yield\":\"425.70\"},{\"logDay\":\"20171205\",\"yield\":\"401.86\"},{\"logDay\":\"20180108\",\"yield\":\"430.53\"},{\"logDay\":\"20180208\",\"yield\":\"308.31\"},{\"logDay\":\"20180320\",\"yield\":\"359.98\"},{\"logDay\":\"20180424\",\"yield\":\"337.41\"},{\"logDay\":\"20180529\",\"yield\":\"314.25\"},{\"logDay\":\"20180709\",\"yield\":\"230.41\"}]";

    List<ChartData> testDatas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lineChart0 = findViewById(R.id.lineChart0);
        lineChartSmall = findViewById(R.id.lineChartSmall);
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

        lineChartSmall.bindData(testDatas);
        lineChartSmall.show();

        lineChart0.bindData(testDatas);
        lineChart0.show();

        lineChart0.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, TabActivity.class)));

        lineChart2.bindData(dataList1);
        lineChart2.bindYUnit("万");
        lineChart2.bindYFormat("%.1f");
        lineChart2.show();

        lineChart3.bindData(dataList1);
        lineChart3.show();

    }
}
