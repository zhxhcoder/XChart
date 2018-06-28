package com.zhxh.xchart;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.zhxh.xchartlib.LineChart;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    LineChart lineChart1;
    LineChart lineChart2;
    List<ChartData> dataList1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lineChart1 = findViewById(R.id.lineChart1);
        lineChart2 = findViewById(R.id.lineChart2);

        dataList1 = new ArrayList<>();
        dataList1.add(new ChartData("05-01", "10.1"));
        dataList1.add(new ChartData("05-02", "12"));
        dataList1.add(new ChartData("05-03", "15"));
        dataList1.add(new ChartData("05-04", "15"));
        dataList1.add(new ChartData("05-05", "18"));
        dataList1.add(new ChartData("05-06", "8.8"));
        dataList1.add(new ChartData("05-07", "9.8"));
        dataList1.add(new ChartData("05-08", "5.8"));
        dataList1.add(new ChartData("05-09", "20"));
        dataList1.add(new ChartData("05-10", "18.8"));

        lineChart1.bindData(dataList1);
        lineChart1.show();

        lineChart2.bindData(dataList1);
        lineChart2.show();

    }
}
