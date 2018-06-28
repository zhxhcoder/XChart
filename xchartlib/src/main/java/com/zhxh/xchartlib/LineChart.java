package com.zhxh.xchartlib;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by zhxh on 2018/5/28
 */
public class LineChart extends View {

    public LineChart(Context context) {
        super(context);
    }

    public LineChart(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public LineChart(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * 既可以在xml中配置也可以直接代码生成
     * @param builder 通过builder生成
     */
    public LineChart(Builder builder) {
        super(builder.context);
    }

    public static class Builder {
        //必需参数
        Context context;
        //可选参数
        int lineType;

        public Builder(Context context) {
            this.context = context;
        }

        public Builder lineType(int lineType) {
            this.lineType = lineType;
            return this;
        }

        public LineChart build() {
            return new LineChart(this);
        }
    }





}
