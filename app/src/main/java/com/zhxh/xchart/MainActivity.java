package com.zhxh.xchart;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.zhxh.xchartlib.LineChart;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    LineChart lineChart1;
    List<ChartData> dataList1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lineChart1 = findViewById(R.id.lineChart1);

        dataList1 = new ArrayList<>();
        dataList1.add(new ChartData("05-01", "10"));
        dataList1.add(new ChartData("05-02", "12"));
        dataList1.add(new ChartData("05-03", "15"));
        dataList1.add(new ChartData("05-03", "15"));


    }
}
