package com.zhxh.xchart.linechart;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.animation.DecelerateInterpolator;
import android.widget.HorizontalScrollView;

import com.zhxh.xchart.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 自定义折线图
 * 实现左滑功能
 */
public class ScrollChartView extends HorizontalScrollView {
    //xy坐标轴颜色
    private int xylinecolor = 0xffe2e2e2;
    //xy坐标轴宽度
    private int xylinewidth = dpToPx(1);
    //xy坐标轴文字颜色
    private int xytextcolor = 0xff7e7e7e;
    //xy坐标轴文字大小
    private int xytextsize = spToPx(12);
    //折线图中折线的颜色
    private int linecolor = 0xff02bbb7;
    private int linecolor1 = 0xff02bbb7;
    //x轴各个坐标点水平间距
    private int interval = dpToPx(50);
    //背景颜色
    private int bgcolor = 0x00000000;
    //是否在ACTION_UP时，根据速度进行自滑动，没有要求，建议关闭，过于占用GPU
    private boolean isScroll = false;
    //绘制XY轴坐标对应的画笔
    private Paint xyPaint;
    //绘制XY轴的文本对应的画笔
    private Paint xyTextPaint;
    //画折线对应的画笔
    private Paint linePaint;
    private Paint linePaint2;
    private int width;
    private int height;
    //x轴的原点坐标
    private int xOri;
    //y轴的原点坐标
    private int yOri;
    //第一个点X的坐标
    private float xInit;
    //第一个点对应的最大Y坐标
    private float maxXInit;
    //第一个点对应的最小X坐标
    private float minXInit;
    //x轴坐标对应的数据
    private List<String> xValue = new ArrayList<>();
    //y轴坐标对应的数据
    private List<Float> yValue = new ArrayList<>();
    //折线对应的数据
    private Map<String, Float> value = new HashMap<>();
    private Map<String, Float> value1 = new HashMap<>();
    //点击的点对应的X轴的第几个点，默认1
    private int selectIndex = 1;
    //X轴刻度文本对应的最大矩形，为了选中时，在x轴文本画的框框大小一致
    private Rect xValueRect;
    //速度检测器
    private VelocityTracker velocityTracker;
    private int widthPixels;

    private scrollToView scrollToView;
    private float startX = 2000;

    public void setScrollToView(scrollToView scrollToView) {
        this.scrollToView = scrollToView;
    }

    public ScrollChartView(Context context) {
        this(context, null);
    }

    public ScrollChartView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ScrollChartView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
        initPaint();

        widthPixels = context.getResources().getDisplayMetrics().widthPixels;

    }

    boolean isStart;

    /**
     * 初始化
     *
     * @param context
     * @param attrs
     * @param defStyleAttr
     */
    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.scrollchartView, defStyleAttr, 0);
        int count = array.getIndexCount();
        for (int i = 0; i < count; i++) {
            int attr = array.getIndex(i);
            switch (attr) {
                case R.styleable.scrollchartView_xylinecolor://xy坐标轴颜色
                    xylinecolor = array.getColor(attr, xylinecolor);
                    break;
                case R.styleable.scrollchartView_xylinewidth://xy坐标轴宽度
                    xylinewidth = (int) array.getDimension(attr, TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, xylinewidth, getResources().getDisplayMetrics()));
                    break;
                case R.styleable.scrollchartView_xytextcolor://xy坐标轴文字颜色
                    xytextcolor = array.getColor(attr, xytextcolor);
                    break;
                case R.styleable.scrollchartView_xytextsize://xy坐标轴文字大小
                    xytextsize = (int) array.getDimension(attr, TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, xytextsize, getResources().getDisplayMetrics()));
                    break;
                case R.styleable.scrollchartView_linecolor://折线图中折线的颜色
                    linecolor = array.getColor(attr, linecolor);
                    break;
                case R.styleable.scrollchartView_linecolor1://折线图中折线的颜色
                    linecolor1 = array.getColor(attr, linecolor1);
                    break;
                case R.styleable.scrollchartView_interval://x轴各个坐标点水平间距
                    interval = (int) array.getDimension(attr, TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, interval, getResources().getDisplayMetrics()));
                    break;
                case R.styleable.scrollchartView_bgcolor: //背景颜色
                    bgcolor = array.getColor(attr, bgcolor);
                    break;
                case R.styleable.scrollchartView_isScroll://是否在ACTION_UP时，根据速度进行自滑动
                    isScroll = array.getBoolean(attr, isScroll);
                    break;
            }

        }

        array.recycle();

    }

    /**
     * 初始化畫筆
     */
    private void initPaint() {
        xyPaint = new Paint();
        xyPaint.setAntiAlias(true);
        xyPaint.setStrokeWidth(xylinewidth);
        xyPaint.setStrokeCap(Paint.Cap.ROUND);
        xyPaint.setColor(xylinecolor);

        xyTextPaint = new Paint();
        xyTextPaint.setAntiAlias(true);
        xyTextPaint.setTextSize(xytextsize);
        xyTextPaint.setStrokeCap(Paint.Cap.ROUND);
        xyTextPaint.setColor(xytextcolor);
        xyTextPaint.setStyle(Paint.Style.STROKE);

        linePaint = new Paint();
        linePaint.setAntiAlias(true);
        linePaint.setStrokeWidth(xylinewidth);
        linePaint.setStrokeCap(Paint.Cap.ROUND);
        linePaint.setColor(linecolor);
        linePaint.setStyle(Paint.Style.STROKE);

        linePaint2 = new Paint();
        linePaint2.setAntiAlias(true);
        linePaint2.setStrokeWidth(xylinewidth);
        linePaint2.setStrokeCap(Paint.Cap.ROUND);
        linePaint2.setColor(Color.parseColor("#FFFEA03C"));
        linePaint2.setStyle(Paint.Style.STROKE);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        if (changed) {
            //这里需要确定几个基本点，只有确定了xy轴原点坐标，第一个点的X坐标值及其最大最小值
            width = getWidth();
            height = getHeight();
            //Y轴文本最大宽度
            float textYWdith = getTextBounds("000", xyTextPaint).width();
            for (int i = 0; i < yValue.size(); i++) {//求取y轴文本最大的宽度
                float temp = getTextBounds(yValue.get(i) + "", xyTextPaint).width();
                if (temp > textYWdith)
                    textYWdith = temp;
            }
            int dp2 = dpToPx(5);
            int dp3 = dpToPx(3);
            xOri = (int) (dp2 + textYWdith + dp2 + xylinewidth);//dp2是y轴文本距离左边，以及距离y轴的距离
//            //X轴文本最大高度
            xValueRect = getTextBounds("000", xyTextPaint);
            float textXHeight = xValueRect.height();
            for (int i = 0; i < xValue.size(); i++) {//求取x轴文本最大的高度
                Rect rect = getTextBounds(xValue.get(i) + "", xyTextPaint);
                if (rect.height() > textXHeight)
                    textXHeight = rect.height();
                if (rect.width() > xValueRect.width())
                    xValueRect = rect;
            }
            yOri = (int) (height - dp2 - textXHeight - dp3 - xylinewidth);//dp3是x轴文本距离底边，dp2是x轴文本距离x轴的距离
            xInit = interval + xOri;
            minXInit = (float) (width - interval * (xValue.size() - 0.1f));//减去0.1f是因为最后一个X周刻度距离右边的长度为X轴可见长度的10%
            maxXInit = xInit;
        }
        super.onLayout(changed, left, top, right, bottom);
    }

    @Override
    protected void onDraw(Canvas canvas) {
//        super.onDraw(canvas);
        canvas.drawColor(bgcolor);
        drawXY(canvas);
        drawBrokenLineAndPoint(canvas);
    }

    /**
     * 绘制折线和折线交点处对应的点
     *
     * @param canvas
     */
    private void drawBrokenLineAndPoint(Canvas canvas) {
        if (xValue.size() <= 0)
            return;
        //重新开一个图层
        int layerId = canvas.saveLayer(0, 0, width, height, null, Canvas.ALL_SAVE_FLAG);
        drawBrokenLine(canvas);
//        drawBrokenPoint(canvas);

        // 将折线超出x轴坐标的部分截取掉
        linePaint.setStyle(Paint.Style.FILL);
        linePaint.setColor(bgcolor);
        linePaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        Rect rectF = new Rect(widthPixels - (xOri - xylinewidth / 2) - dpToPx(15), 0, widthPixels, yOri);
        canvas.drawRect(rectF, linePaint);
        linePaint.setXfermode(null);
        //保存图层
        canvas.restoreToCount(layerId);
    }

    /**
     * 绘制折线
     *
     * @param canvas
     */
    private void drawBrokenLine(Canvas canvas) {
        //折线1
        linePaint.setStyle(Paint.Style.STROKE);
        linePaint.setColor(linecolor);
        //绘制折线
        Path path = new Path();
        float x = xInit + interval * 0;
        float y = yOri - yOri * (1 - 0.1f) * value.get(xValue.get(0)) / yValue.get(yValue.size() - 1);
        path.moveTo(x, y);
        for (int i = 1; i < xValue.size(); i++) {
            x = xInit + interval * i;
            y = yOri - yOri * (1 - 0.1f) * value.get(xValue.get(i)) / yValue.get(yValue.size() - 1);
            path.lineTo(x, y);
        }
        canvas.drawPath(path, linePaint);

        //折线2
        linePaint2.setStyle(Paint.Style.STROKE);
        linePaint2.setColor(Color.parseColor("#FFFEA03C"));
        //绘制折线
        Path path1 = new Path();
        float x1 = xInit + interval * 0;
        float y1 = yOri - yOri * (1 - 0.1f) * value1.get(xValue.get(0)) / yValue.get(yValue.size() - 1);
        path1.moveTo(x1, y1);
        for (int i = 1; i < xValue.size(); i++) {
            x1 = xInit + interval * i;
            y1 = yOri - yOri * (1 - 0.1f) * value1.get(xValue.get(i)) / yValue.get(yValue.size() - 1);
            path1.lineTo(x1, y1);
        }
        canvas.drawPath(path1, linePaint2);
    }

    /**
     * 绘制XY坐标
     *
     * @param canvas
     */
    private void drawXY(Canvas canvas) {
        int length = dpToPx(4);//刻度的长度

        Path path = new Path();
        //绘制X轴坐标
        canvas.drawLine(widthPixels - (xOri - xylinewidth / 2) - dpToPx(15), yOri + xylinewidth / 2, xOri, yOri + xylinewidth / 2, xyPaint);
        //绘制x轴箭头
//        xyPaint.setStyle(Paint.Style.STROKE);

        //整个X轴的长度
        float xLength = xInit + interval * (xValue.size() - 1) + (widthPixels - (xOri - xylinewidth / 2) - xOri) * 0.1f;
        if (xLength < widthPixels - (xOri - xylinewidth / 2))
            xLength = width;
//        path.moveTo(xLength - dpToPx(12), yOri + xylinewidth / 2 - dpToPx(5));
//        path.lineTo(xLength - xylinewidth / 2, yOri + xylinewidth / 2);
//        path.lineTo(xLength - dpToPx(12), yOri + xylinewidth / 2 + dpToPx(5));
        canvas.drawPath(path, xyPaint);
        //绘制x轴刻度
        for (int i = 0; i < xValue.size(); i++) {
            float x = xInit + interval * i;
            if (x >= xOri) {//只绘制从原点开始的区域
                xyTextPaint.setColor(xytextcolor);
                canvas.drawLine(x, yOri, x, yOri - length, xyPaint);
                //绘制X轴文本
                String text = xValue.get(i);
                Rect rect = getTextBounds(text, xyTextPaint);
//                if (i == selectIndex - 1) {
//                    xyTextPaint.setColor(linecolor);
//                    canvas.drawText(text, 0, text.length(), x - rect.width() / 2, yOri + xylinewidth + dpToPx(2) + rect.height(), xyTextPaint);
//                    canvas.drawRoundRect(x - xValueRect.width() / 2 - dpToPx(3), yOri + xylinewidth + dpToPx(1), x + xValueRect.width() / 2 + dpToPx(3), yOri + xylinewidth + dpToPx(2) + xValueRect.height() + dpToPx(2), dpToPx(2), dpToPx(2), xyTextPaint);
//                } else {
                canvas.drawText(text, 0, text.length(), x - rect.width() / 2, yOri + xylinewidth + dpToPx(5) + rect.height(), xyTextPaint);
//                }
            }
        }

        //绘制Y坐标
        canvas.drawLine(widthPixels - (xOri - xylinewidth / 2) - dpToPx(15), 0, widthPixels - (xOri - xylinewidth / 2) - dpToPx(15), yOri, xyPaint);
        //绘制y轴箭头
//        xyPaint.setStyle(Paint.Style.STROKE);
//        path = new Path();
//        path.moveTo(widthPixels - (xOri - xylinewidth / 2 - dpToPx(5)), dpToPx(12));
//        path.lineTo(widthPixels - (xOri - xylinewidth / 2), (xylinewidth / 2));
//        path.lineTo(widthPixels - (xOri - xylinewidth / 2 + dpToPx(5)), dpToPx(12));
        Log.d("ChartView", "---------------:" + (widthPixels - (xOri - xylinewidth / 2 - dpToPx(5))) + "---------" + (widthPixels - dpToPx(12)));
        Log.d("ChartView", "---------------:" + (widthPixels - (xOri - xylinewidth / 2) + "---------" + (widthPixels - (xylinewidth / 2))));
        Log.d("ChartView", "---------------:" + (widthPixels - (xOri - xylinewidth / 2 + dpToPx(5)) + "---------" + (widthPixels - dpToPx(12))));

        canvas.drawPath(path, xyPaint);
        //绘制y轴刻度
        int yLength = (int) (yOri * (1 - 0.1f) / (yValue.size() - 1));//y轴上面空出10%,计算出y轴刻度间距
        for (int i = 0; i < yValue.size(); i++) {
            //绘制Y轴刻度
            canvas.drawLine(0, yOri - yLength * i + xylinewidth / 2, widthPixels - (xOri - xylinewidth / 2) - dpToPx(15), yOri - yLength * i + xylinewidth / 2, xyPaint);
            xyTextPaint.setColor(xytextcolor);
            //绘制Y轴文本
            String text = yValue.get(i) + "";
            Rect rect = getTextBounds(text, xyTextPaint);
            switch (text.length()) {
                case 1:
                    canvas.drawText(text, 0, text.length(), widthPixels - rect.width() - rect.width() - rect.width() - dpToPx(15), yOri - yLength * i + rect.height() / 2, xyTextPaint);
                    break;
                case 2:
                    canvas.drawText(text, 0, text.length(), widthPixels - rect.width() - (rect.width() / 2) - dpToPx(13), yOri - yLength * i + rect.height() / 2, xyTextPaint);
                    break;
                case 3:
                    canvas.drawText(text, 0, text.length(), widthPixels - rect.width() - dpToPx(11), yOri - yLength * i + rect.height() / 2, xyTextPaint);
                    break;
                case 4:
                    canvas.drawText(text, 0, text.length(), widthPixels - rect.width() - dpToPx(9), yOri - yLength * i + rect.height() / 2, xyTextPaint);
                    break;
            }


            Log.d("ChartView", "xOri - xylinewidth - dpToPx(2) - rect.width():" + (xOri - xylinewidth - dpToPx(2) - rect.width()));
        }
        if (!isStart) {
            if (interval * xValue.size() > widthPixels - (xOri - xylinewidth / 2)) {//当期的宽度不足以呈现全部数据
                float dis = 100 - startX;
                if (xInit + dis < minXInit) {
                    xInit = minXInit;
                } else if (xInit + dis > maxXInit) {
                    xInit = maxXInit;
                } else {
                    xInit = xInit + dis;
                }
                invalidate();
            }
            isStart = true;
        }
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (isScrolling)
            return super.onTouchEvent(event);
        this.getParent().requestDisallowInterceptTouchEvent(true);//当该view获得点击事件，就请求父控件不拦截事件
        obtainVelocityTracker(event);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startX = event.getX();
                break;
            case MotionEvent.ACTION_MOVE:
                if (interval * xValue.size() > widthPixels - (xOri - xylinewidth / 2)) {//当期的宽度不足以呈现全部数据
                    float dis = event.getX() - startX;
                    startX = event.getX();
                    if (xInit + dis < minXInit) {
                        xInit = minXInit;
                    } else if (xInit + dis > maxXInit) {
                        xInit = maxXInit;
                    } else {
                        xInit = xInit + dis;
                    }
                    Log.e("======>>interval", interval + "");
                    Log.e("======>>xValue", xValue + "");
                    Log.e("======>>widthPixels", widthPixels - (xOri - xylinewidth / 2) + "");
                    Log.e("======>>startX", startX + "");
                    Log.e("======>>xInit", xInit + "");
                    Log.e("======>>minXInit", minXInit + "");
                    Log.e("======>>dis", dis + "");


                    invalidate();
                }
                break;
            case MotionEvent.ACTION_UP:
//                clickAction(event);
                scrollAfterActionUp();
                this.getParent().requestDisallowInterceptTouchEvent(false);
                recycleVelocityTracker();
                break;
            case MotionEvent.ACTION_CANCEL:
                this.getParent().requestDisallowInterceptTouchEvent(false);
                recycleVelocityTracker();
                break;
        }
        return true;
    }

    //是否正在滑动
    private boolean isScrolling = false;

    /**
     * 手指抬起后的滑动处理
     */
    private void scrollAfterActionUp() {
        if (!isScroll)
            return;
        final float velocity = getVelocity();
        float scrollLength = maxXInit - minXInit;
        if (Math.abs(velocity) < 10000)//10000是一个速度临界值，如果速度达到10000，最大可以滑动(maxXInit - minXInit)
            scrollLength = (maxXInit - minXInit) * Math.abs(velocity) / 10000;
        ValueAnimator animator = ValueAnimator.ofFloat(0, scrollLength);
        animator.setDuration((long) (scrollLength / (maxXInit - minXInit) * 1000));//时间最大为1000毫秒，此处使用比例进行换算
        animator.setInterpolator(new DecelerateInterpolator());
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float value = (float) valueAnimator.getAnimatedValue();
                if (velocity < 0 && xInit > minXInit) {//向左滑动
                    if (xInit - value <= minXInit)
                        xInit = minXInit;
                    else
                        xInit = xInit - value;
                } else if (velocity > 0 && xInit < maxXInit) {//向右滑动
                    if (xInit + value >= maxXInit)
                        xInit = maxXInit;
                    else
                        xInit = xInit + value;

                }
                invalidate();
            }
        });
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
                isScrolling = true;
            }

            @Override
            public void onAnimationEnd(Animator animator) {
                isScrolling = false;
            }

            @Override
            public void onAnimationCancel(Animator animator) {
                isScrolling = false;
            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
        animator.start();

    }

    /**
     * 获取速度
     *
     * @return
     */
    private float getVelocity() {
        if (velocityTracker != null) {
            velocityTracker.computeCurrentVelocity(1000);
            return velocityTracker.getXVelocity();
        }
        return 0;
    }

    /**
     * 点击X轴坐标或者折线节点
     *
     * @param event
     */
//    private void clickAction(MotionEvent event) {
//        int dp8 = dpToPx(8);
//        float eventX = event.getX();
//        float eventY = event.getY();
//        for (int i = 0; i < xValue.size(); i++) {
//            //节点
//            float x = xInit + interval * i;
//            float y = yOri - yOri * (1 - 0.1f) * value.get(xValue.get(i)) / yValue.get(yValue.size() - 1);
//            if (eventX >= x - dp8 && eventX <= x + dp8 &&
//                    eventY >= y - dp8 && eventY <= y + dp8 && selectIndex != i + 1) {//每个节点周围8dp都是可点击区域
//                selectIndex = i + 1;
//                invalidate();
//                return;
//            }
//            //X轴刻度
//            String text = xValue.get(i);
//            Rect rect = getTextBounds(text, xyTextPaint);
//            x = xInit + interval * i;
//            y = yOri + xylinewidth + dpToPx(2);
//            if (eventX >= x - rect.width() / 2 - dp8 && eventX <= x + rect.width() + dp8 / 2 &&
//                    eventY >= y - dp8 && eventY <= y + rect.height() + dp8 && selectIndex != i + 1) {
//                selectIndex = i + 1;
//                invalidate();
//                return;
//            }
//        }
//    }


    /**
     * 获取速度跟踪器
     *
     * @param event
     */
    private void obtainVelocityTracker(MotionEvent event) {
        if (!isScroll)
            return;
        if (velocityTracker == null) {
            velocityTracker = VelocityTracker.obtain();
        }
        velocityTracker.addMovement(event);
    }

    /**
     * 回收速度跟踪器
     */
    private void recycleVelocityTracker() {
        if (velocityTracker != null) {
            velocityTracker.recycle();
            velocityTracker = null;
        }
    }

    public int getSelectIndex() {
        return selectIndex;
    }

    public void setSelectStart() {
        if (interval * xValue.size() > widthPixels - (xOri - xylinewidth / 2)) {//当期的宽度不足以呈现全部数据
            float dis = 100 - startX;
            if (xInit + dis < minXInit) {
                xInit = minXInit;
            } else if (xInit + dis > maxXInit) {
                xInit = maxXInit;
            } else {
                xInit = xInit + dis;
            }
            invalidate();
        }
    }

    public void setSelectIndex(int selectIndex) {
        this.selectIndex = selectIndex;
        invalidate();
    }

    public void setxValue(List<String> xValue) {
        this.xValue = xValue;
    }

    public void setyValue(List<Float> yValue) {
        this.yValue = yValue;
        invalidate();
    }

    public void setValue(Map<String, Float> value) {
        this.value = value;
        invalidate();
    }

    public void setValue1(Map<String, Float> value1) {
        this.value1 = value1;
        invalidate();
    }

    public void setValue(Map<String, Float> value, Map<String, Float> value1, List<String> xValue, List<Float> yValue) {
        this.value = value;
        this.value1 = value1;
        this.xValue = xValue;
        this.yValue = yValue;
        invalidate();
    }

    public List<String> getxValue() {
        return xValue;
    }

    public List<Float> getyValue() {
        return yValue;
    }

    public Map<String, Float> getValue() {
        return value;
    }

    /**
     * 获取丈量文本的矩形
     *
     * @param text
     * @param paint
     * @return
     */
    private Rect getTextBounds(String text, Paint paint) {
        Rect rect = new Rect();
        paint.getTextBounds(text, 0, text.length(), rect);
        return rect;
    }

    /**
     * dp转化成为px
     *
     * @param dp
     * @return
     */
    private int dpToPx(int dp) {
        float density = getContext().getResources().getDisplayMetrics().density;
        return (int) (dp * density + 0.5f * (dp >= 0 ? 1 : -1));
    }

    /**
     * sp转化为px
     *
     * @param sp
     * @return
     */
    private int spToPx(int sp) {
        float scaledDensity = getContext().getResources().getDisplayMetrics().scaledDensity;
        return (int) (scaledDensity * sp + 0.5f * (sp >= 0 ? 1 : -1));
    }


    public interface scrollToView {
        void scrollToview(float xLength, float yLength);
    }
}

