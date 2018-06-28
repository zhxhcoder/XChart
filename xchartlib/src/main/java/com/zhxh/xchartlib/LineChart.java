package com.zhxh.xchartlib;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;

import com.zhxh.xchartlib.entity.IAxisValue;

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

    private List<? extends IAxisValue> dataList;
    private int dataNum;

    private float yHeightPerValue;

    private float density;

    private Paint paintText;
    private Paint paintAxis;
    private Paint paintLine;

    /**
     * 坐标轴 原点 x点y点
     */
    private PointF pOrigin;
    private PointF pRight;
    private PointF pTop;

    private float minY;
    private float maxY;

    private static final int intervals = 87;
    private float xDataOffset;
    private Canvas canvas;

    private boolean isAnim;
    private int showXcount;
    private int showYcount;

    private int showYType;

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
        int showYType;

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

        public Builder showYType(int showYType) {
            this.showYType = showYType;
            return this;
        }

        public LineChart build() {
            return new LineChart(this);
        }
    }


    private void init(Builder builder, AttributeSet attrs) {

        isAnim = builder.isAnim;
        showYType = builder.showYType;
        int axisColor = builder.axisColor;
        int textColor = builder.textColor;
        int lineColor = builder.lineColor;
        float canvasHeight = builder.canvasHeight;
        float canvasWidth = builder.canvasWidth;

        if (attrs != null) {
            TypedArray a = builder.context.obtainStyledAttributes(attrs, R.styleable.LineChart);

            isAnim = a.getBoolean(R.styleable.LineChart_XisAnim, isAnim);
            axisColor = a.getColor(R.styleable.LineChart_XaxisColor, axisColor);
            textColor = a.getColor(R.styleable.LineChart_XtextColor, textColor);
            lineColor = a.getColor(R.styleable.LineChart_XlineColor, lineColor);
            canvasHeight = a.getDimensionPixelSize(R.styleable.LineChart_XcanvasHeight, (int) canvasHeight);
            canvasWidth = a.getDimensionPixelSize(R.styleable.LineChart_XcanvasWidth, (int) canvasWidth);
            showXcount = a.getInt(R.styleable.LineChart_XshowXcount, showXcount);
            showYcount = a.getInt(R.styleable.LineChart_XshowYcount, showYcount);
            showYType = a.getInt(R.styleable.LineChart_XshowYType, showYType);
        }


        DisplayMetrics displayMetrics = builder.context.getResources().getDisplayMetrics();

        density = displayMetrics.density;

        paintText = new Paint();
        paintText.setAntiAlias(true);
        paintText.setColor(textColor);
        paintText.setStrokeWidth(5 * density);
        paintText.setTextSize(10f * density);

        paintAxis = new Paint();
        paintAxis.setAntiAlias(true);
        paintAxis.setColor(axisColor);
        paintAxis.setStrokeWidth(0.5f * density);

        paintLine = new Paint();
        paintLine.setAntiAlias(true);
        paintLine.setColor(lineColor);
        paintLine.setStrokeWidth(1f * density);

        pOrigin = new PointF();
        pRight = new PointF();
        pTop = new PointF();

        if (showYType == 1) {
            pOrigin.set(canvasWidth * 0.19f, canvasHeight * 0.8f);
            pRight.set(canvasWidth * 0.9f, canvasHeight * 0.8f);
            pTop.set(canvasWidth * 0.19f, canvasHeight * 0.1f);
        } else {
            pOrigin.set(canvasWidth * 0.1f, canvasHeight * 0.8f);
            pRight.set(canvasWidth * 0.9f, canvasHeight * 0.8f);
            pTop.set(canvasWidth * 0.1f, canvasHeight * 0.1f);
        }

    }


    public LineChart bindData(List<? extends IAxisValue> dataList) {

        this.dataList = dataList;
        dataNum = dataList.size();

        if (dataNum == 0) {
            return this;
        }

        dataMap = new HashMap<String, Float>();
        yList = new ArrayList<Float>();

        for (IAxisValue data : dataList) {

            if (!TextUtils.isEmpty(data.xValue()) && !TextUtils.isEmpty(data.yValue()))
                dataMap.put(data.xValue(), Float.valueOf(data.yValue()));
        }

        minY = getMinValue(dataList);
        maxY = getMaxValue(dataList);

        yHeightPerValue = (pOrigin.y - pTop.y) / (maxY - minY);

        for (int i = 0; i < showYcount; i++) {
            yList.add(minY + (i) * (maxY - minY) / (showYcount - 1));
        }

        return this;
    }

    public void show() {
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
        float yOffset = (pOrigin.y - pTop.y) / (showYcount - 1);

        for (int i = 0; i < showYcount; i++) {

            canvas.drawLine(pOrigin.x,
                    pOrigin.y - i * yOffset,
                    pRight.x,
                    pRight.y - i * yOffset, paintAxis);

            if (showYType == 1) {
                canvas.drawText(String.format("%.2f", yList.get(i)),
                        pOrigin.x - paintText.measureText(String.format("%.2f", yList.get(i))) - 8 * density,
                        pOrigin.y - i * yOffset + 3 * density,
                        paintText);
            }

        }

        /**
         * 画坐标竖线 以及x轴下面的日期 从左往右
         * 当第一项或最后一项时特殊处理
         */
        float xShowOffset = (pRight.x - pOrigin.x) / (showXcount - 1);
        for (int i = 0; i < showXcount; i++) {
            //canvas.drawLine(pOrigin.x + i * xShowOffset, pOrigin.y, pTop.x + i * xShowOffset, pTop.y, paintAxis);

            if (i == 0) {
                canvas.drawText(dataList.get(i).xValue(),
                        pOrigin.x + i * xShowOffset,
                        pOrigin.y + 18 * density, paintText);
            } else if (i == showXcount - 1) {
                canvas.drawText(dataList.get(i).xValue(),
                        pOrigin.x + i * xShowOffset - paintText.measureText(dataList.get(i).xValue()),
                        pOrigin.y + 18 * density, paintText);
            } else {
                canvas.drawText(dataList.get(i).xValue(),
                        pOrigin.x + i * xShowOffset - paintText.measureText(dataList.get(i).xValue()) / 2,
                        pOrigin.y + 18 * density, paintText);
            }
        }

        /*************************************************************************/
        xDataOffset = (pRight.x - pOrigin.x) / (dataNum - 1);

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
        return pOrigin.y - yHeightPerValue * (dateProfit - minY);
    }

    public float getMaxValue(List<? extends IAxisValue> dataList) {
        float maxValue = 0;
        for (IAxisValue data : dataList) {
            if (Float.parseFloat(data.yValue()) >= maxValue) {
                maxValue = Float.parseFloat(data.yValue());
            }
        }
        return maxValue;
    }

    public float getMinValue(List<? extends IAxisValue> dataList) {
        float minValue = Float.parseFloat(dataList.get(0).yValue());
        for (IAxisValue data : dataList) {
            if (Float.parseFloat(data.yValue()) <= minValue) {
                minValue = Float.parseFloat(data.yValue());
            }
        }
        return minValue;
    }

    private void drawItemData(int i) {

        canvas.drawPoint(pOrigin.x + i * xDataOffset, getDataYvalue(dataMap.get(dataList.get(i).xValue())), paintLine);

        //canvas.drawLine(pOrigin.x + i * xDataOffset, pOrigin.y, pOrigin.x + i * xDataOffset, getDataYvalue(dataMap.get(dataList.get(i).xValue())), paintLine);

        if (i >= 1) {
            canvas.drawLine(pOrigin.x + (i - 1) * xDataOffset, getDataYvalue(dataMap.get(dataList.get((i - 1)).xValue()))
                    , pOrigin.x + i * xDataOffset, getDataYvalue(dataMap.get(dataList.get(i).xValue())), paintLine);
        }

    }
}
