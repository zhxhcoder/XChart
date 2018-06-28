package com.zhxh.xchart;

import com.zhxh.xchartlib.entity.AxisValue;

/**
 * Created by zhxh on 2018/6/28
 */
public class ChartData implements AxisValue {

    private String date;
    private String value;

    public ChartData(String date, String value) {
        this.date = date;
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public String xValue() {
        return getDate();
    }

    @Override
    public String yValue() {
        return getValue();
    }
}
