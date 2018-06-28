package com.zhxh.xchartlib;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;

import com.zhxh.xchartlib.entity.AxisValue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zhxh on 2018/5/28
 */
public class LineChart extends View {

    private Map<String, Float> dataMap; //坐标轴里面的点
    private List<Float> yList; // Y轴上点  从小到大排列

    private List<? extends AxisValue> dataList;
    private List<? extends AxisValue> normalList;
    private int dataNum;
    private int xNum;
    private int yNum;

    private float yHeightPerValue;

    private float density;

    private Paint paintTextWhite;
    private Paint paintTextGrey;
    private Paint paintLineRed;
    private Paint paintLineBlue;
    private Paint paintLineGrey;
    private Paint paintGradient;


    /**
     * 坐标轴 原点 x点y点
     */
    private PointF pOrigin;
    private PointF pRight;
    private PointF pTop;

    private float minY;
    private float maxY;

    private static final int intervals = 87;
    private float xOffset;
    private Canvas canvas;


    /**************************自定义数据****************************/


    private boolean isAnim;
    private int axisColor = 0xFFFFF0FF;
    private int textColor = 0xFFF0FFFF;
    private int lineColor = 0xFFFF0F0F;
    private float canvasHeight;
    private float canvasWidth;


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
        int canvasHeight;
        int canvasWidth;

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

        public Builder canvasHeight(int canvasHeight) {
            this.canvasHeight = canvasHeight;
            return this;
        }

        public Builder canvasWidth(int canvasWidth) {
            this.canvasWidth = canvasWidth;
            return this;
        }

        public LineChart build() {
            return new LineChart(this);
        }
    }


    private void init(Builder builder, AttributeSet attrs) {

        isAnim = builder.isAnim;
        axisColor = builder.axisColor;
        textColor = builder.textColor;
        lineColor = builder.lineColor;
        canvasHeight = builder.canvasHeight;
        canvasWidth = builder.canvasWidth;

        if (attrs != null) {
            TypedArray a = builder.context.obtainStyledAttributes(attrs, R.styleable.LineChart);

            isAnim = a.getBoolean(R.styleable.LineChart_XisAnim, isAnim);
            axisColor = a.getColor(R.styleable.LineChart_XaxisColor, axisColor);
            textColor = a.getColor(R.styleable.LineChart_XtextColor, textColor);
            lineColor = a.getColor(R.styleable.LineChart_XlineColor, lineColor);
            canvasHeight = a.getDimensionPixelSize(R.styleable.LineChart_XcanvasHeight, (int) canvasHeight);
            canvasWidth = a.getDimensionPixelSize(R.styleable.LineChart_XcanvasWidth, (int) canvasWidth);
        }


        DisplayMetrics displayMetrics = builder.context.getResources().getDisplayMetrics();


        density = displayMetrics.density;


        paintTextWhite = new Paint();
        paintTextWhite.setAntiAlias(true);
        paintTextWhite.setColor(Color.WHITE);
        paintTextWhite.setStrokeWidth(5 * density);
        paintTextWhite.setTextSize(10f * density);

        paintTextGrey = new Paint();
        paintTextGrey.setAntiAlias(true);
        paintTextGrey.setColor(textColor);
        paintTextGrey.setStrokeWidth(2 * density);
        paintTextGrey.setTextSize(10f * density);

        paintLineBlue = new Paint();
        paintLineBlue.setAntiAlias(true);
        paintLineBlue.setColor(lineColor);
        paintLineBlue.setStrokeWidth(2 * density);

        paintLineGrey = new Paint();
        paintLineGrey.setAntiAlias(true);
        paintLineGrey.setColor(lineColor);
        paintLineGrey.setStrokeWidth(1 * density);

        paintGradient = new Paint();
        paintGradient.setAntiAlias(true);
        paintGradient.setColor(lineColor);
        paintGradient.setStrokeWidth(1 * density);

        /**
         * 初始化数据
         */

        pOrigin = new PointF();
        pRight = new PointF();
        pTop = new PointF();

        pOrigin.set(canvasWidth * 0.19f, canvasHeight * 0.8f);
        pRight.set(canvasWidth * 0.9f, canvasHeight * 0.8f);
        pTop.set(canvasWidth * 0.19f, canvasHeight * 0.1f);


    }


    public void setViewData(List<? extends AxisValue> dataList) {

        this.dataList = dataList;
        dataNum = dataList.size();

        if (dataNum == 0) {
            return;
        }

        dataMap = new HashMap<String, Float>();
        yList = new ArrayList<Float>();

        for (AxisValue data : dataList) {

            if (!TextUtils.isEmpty(data.xValue()) && !TextUtils.isEmpty(data.yValue()))
                dataMap.put(data.xValue(), Float.valueOf(data.yValue()));
        }

        xNum = 7;
        yNum = 5;
        minY = getMinValue(dataList) - (getMaxValue(dataList) - getMinValue(dataList)) / 2;
        maxY = getMaxValue(dataList) + (getMaxValue(dataList) - getMinValue(dataList)) / 2;

        yHeightPerValue = (pOrigin.y - pTop.y) / (maxY - minY);

        for (int i = 0; i < yNum; i++) {
            yList.add(minY + (i) * (maxY - minY) / (yNum - 1));
        }


        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {

        if (dataNum == 0) {
            return;
        }
        this.canvas = canvas;

        /**
         * 画横线  以及Y轴左面的数值 从下往上
         */
        float yOffset = 30f * density;

        for (int i = 0; i < yNum; i++) {

            canvas.drawLine(pOrigin.x,
                    pOrigin.y - i * yOffset,
                    pRight.x,
                    pRight.y - i * yOffset, paintLineGrey);

            canvas.drawText(String.format("%.4f", yList.get(i) / 1000),
                    pOrigin.x - 45 * density,
                    pOrigin.y - i * yOffset + 3 * density,
                    paintTextGrey);

        }


        /**
         * 画坐标竖线 以及x轴下面的日期 从左往右
         */

        xOffset = 42f * density;

        for (int i = 0; i < xNum; i++) {

            canvas.drawLine(pOrigin.x + i * xOffset, pOrigin.y, pTop.x + i * xOffset, pTop.y, paintLineGrey);

            canvas.drawText(dataList.get(i).xValue(), pOrigin.x + i * xOffset - 5 * density, pOrigin.y + 18 * density, paintTextGrey);

        }


        /*************************************************************************/
        /**
         * 点 已经 线
         */

        if (isAnim) {
            for (int i = 0; i < progress; i++) {
                drawItemData(i);
            }
            getHandler().postDelayed(runnable, intervals);

        } else {
            for (int i = 0; i < dataNum; i++) {
                drawItemData(i);
            }
        }

    }

    private int progress = 0;
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if (progress == dataNum) {
                return;
            }
            progress++;
            invalidate();
        }
    };


    private float getDataYvalue(float dateProfit) {

        float yValue = pOrigin.y - yHeightPerValue * (dateProfit - minY);

        return yValue;
    }

    public float getMaxValue(List<? extends AxisValue> dataList) {

        float maxValue = 0;

        for (AxisValue data : dataList) {

            if (Float.parseFloat(data.yValue()) >= maxValue) {
                maxValue = Float.parseFloat(data.yValue());
            }

        }

        return maxValue;

    }

    public float getMinValue(List<? extends AxisValue> dataList) {

        float minValue = Float.parseFloat(dataList.get(0).yValue());

        for (AxisValue data : dataList) {

            if (Float.parseFloat(data.yValue()) <= minValue) {
                minValue = Float.parseFloat(data.yValue());
            }
        }

        return minValue;

    }

    private void drawItemData(int i) {

        canvas.drawPoint(pOrigin.x + i * xOffset, getDataYvalue(dataMap.get(dataList.get(i).xValue())), paintLineBlue);

        canvas.drawLine(pOrigin.x + i * xOffset, pOrigin.y
                , pOrigin.x + i * xOffset, getDataYvalue(dataMap.get(dataList.get(i).xValue())), paintGradient);

        if (i >= 1) {

            canvas.drawLine(pOrigin.x + (i - 1) * xOffset, getDataYvalue(dataMap.get(dataList.get((i - 1)).xValue()))
                    , pOrigin.x + i * xOffset, getDataYvalue(dataMap.get(dataList.get(i).xValue())), paintLineBlue);

        }

    }
}
