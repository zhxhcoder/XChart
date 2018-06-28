package com.zhxh.xchartlib;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import java.util.List;
import java.util.Map;

/**
 * Created by zhxh on 2018/5/28
 */
public class LineChart extends View {

    private Map<String, Float> dataMap; //坐标轴里面的点
    private List<Float> yList; // Y轴上点  从小到大排列

    private List<FundChartData> dataList;
    private List<FundChartData> normalList;
    private int dataNum;
    private int xNum;
    private int yNum;

    private float yHeightPerValue;

    private float canvasWight;
    private float canvasHeight;
    private float density;

    private Paint paintTextWhite;
    private Paint paintTextGrey;
    private Paint paintLineRed;
    private Paint paintLineBlue;
    private Paint paintLineGrey;
    private Paint paintGradient;


    private boolean isAnim;
    private int axisColor;
    private int textColor;
    private int lineColor;
    private int chartHeight;
    private int chartWidth;


    /**
     * 既可以在xml中配置也可以直接代码生成
     *
     * @param builder 通过builder生成
     */
    public LineChart(Builder builder) {
        super(builder.context);

        init(builder, null);

    }

    public LineChart(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        init(new Builder(context), attrs);
    }

    public LineChart(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init(new Builder(context), attrs);
    }


    public static class Builder {
        //必需参数
        Context context;
        //可选参数

        boolean isAnim;
        int axisColor;
        int textColor;
        int lineColor;
        int chartHeight;
        int chartWidth;

        public Builder(Context context) {
            this.context = context;
        }

        public Builder isAnim(boolean isAnim) {
            this.isAnim = isAnim;
            return this;
        }

        public Builder axisColor(int axisColor) {
            this.axisColor = axisColor;
            return this;
        }

        public Builder textColor(int textColor) {
            this.textColor = textColor;
            return this;
        }

        public Builder lineColor(int lineColor) {
            this.lineColor = lineColor;
            return this;
        }

        public Builder chartHeight(int chartHeight) {
            this.chartHeight = chartHeight;
            return this;
        }

        public Builder chartWidth(int chartWidth) {
            this.chartWidth = chartWidth;
            return this;
        }

        public LineChart build() {
            return new LineChart(this);
        }
    }


    private void init(Builder builder, AttributeSet attrs) {

        if (attrs != null) {
            TypedArray a = builder.context.obtainStyledAttributes(attrs, R.styleable.LineChart);

            isAnim = a.getBoolean(R.styleable.LineChart_XisAnim, isAnim);
            axisColor = a.getColor(R.styleable.LineChart_XaxisColor, axisColor);
            textColor = a.getColor(R.styleable.LineChart_XtextColor, textColor);
            lineColor = a.getColor(R.styleable.LineChart_XlineColor, lineColor);
            chartHeight = a.getDimensionPixelSize(R.styleable.LineChart_XchartHeight, chartHeight);
            chartWidth = a.getDimensionPixelSize(R.styleable.LineChart_XchartWidth, chartWidth);
        }

    }


}
