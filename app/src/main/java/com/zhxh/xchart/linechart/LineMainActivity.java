package com.zhxh.xchart.linechart;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.zhxh.xchart.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class LineMainActivity extends AppCompatActivity{
    //x轴坐标对应的数据
    private List<String> xValue = new ArrayList<>();
    //y轴坐标对应的数据
    private List<Float> yValue = new ArrayList<>();
    //折线对应的数据
    private Map<String, Float> value = new LinkedHashMap<>();
    //第二条折线
    private Map<String, Float> value1 = new HashMap<>();
    //第一条折线对应的折点
    List<Float> mlist=new ArrayList<>();
    //第二条折线对应的折点
    List<Float> mlist1=new ArrayList<>();

    ScrollChartView scrollChartView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_line_main);
        for (int i = 10; i > 0; i--) {
            mlist.add((float) (Math.random()*3.5+0.5));
            mlist1.add((float) (Math.random()*3.5+0.5));
        }
        for (int i =0; i <10 ; i++) {
            xValue.add("11-1"+i);
            value.put("11-1"+i, mlist.get(i));
            value1.put("11-1"+i, mlist1.get(i));
        }

            yValue.add((float) 0.55);
            yValue.add((float) 1.55);
            yValue.add((float) 2.55);
            yValue.add((float) 3.55);
            yValue.add((float) 4.55);
        scrollChartView = (ScrollChartView) findViewById(R.id.chartview);
        scrollChartView.setValue(value,value1, xValue, yValue);
    }


}

