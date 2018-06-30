# XChart
XChart, 自定义可灵活配置的图表库


# 控件源码
先上代码：https://github.com/zhxhcoder/XChart

# 通过Maven或Gradle引用
~~~
<dependency>
  <groupId>com.zhxh</groupId>
  <artifactId>xchartlib</artifactId>
  <version>2.3</version>
  <type>pom</type>
</dependency>
~~~

~~~
compile 'com.zhxh:xchartlib:2.3'
~~~

# XChart介绍-LineChart, BarChart,RadarChart,CandleChart,PieChart,AreaChart,SurfaceChart

![](https://github.com/zhxhcoder/XChart/blob/master/screenshots/linechart.png)

其中的折线图图表


### LinChart介绍

##### 1引用
~~~
<dependency>
  <groupId>com.zhxh</groupId>
  <artifactId>xchartlib</artifactId>
  <version>2.3</version>
  <type>pom</type>
</dependency>
~~~
或
~~~
implementation 'com.zhxh:xchartlib:2.3'
api 'com.zhxh:xchartlib:2.3'
~~~

##### 2配置
~~~

    <com.zhxh.xchartlib.LineChart
        android:id="@+id/lineChart1"
        android:layout_width="260dp"
        android:layout_height="140dp"
        android:layout_marginTop="30dp"
        android:background="@android:color/white"
        app:XaxisColor="#E8E8E8"
        app:XcanvasHeight="140dp"
        app:XcanvasWidth="260dp"
        app:XisAnim="false"
        app:XlineColor="#ff4c51"
        app:XshaderEndColor="#70ff4c51"
        app:XshaderStartColor="#00ff4c51"
        app:XshowXcount="3"
        app:XshowYcount="6"
        app:XtextColor="#8997A5"
        app:layout_constraintLeft_toLeftOf="parent"
        app:layout_constraintRight_toRightOf="parent"
        app:layout_constraintTop_toTopOf="parent" />

    <com.zhxh.xchartlib.LineChart
        android:id="@+id/lineChart2"
        android:layout_width="260dp"
        android:layout_height="140dp"
        android:background="@android:color/white"
        app:XaxisColor="#E8E8E8"
        app:XcanvasHeight="140dp"
        app:XcanvasWidth="260dp"
        app:XisAnim="false"
        app:XlineColor="#ff4c51"
        app:XshowXcount="2"
        app:XshowYType="1"
        app:XshowYcount="6"
        app:XtextColor="#8997A5"
        app:layout_constraintBottom_toBottomOf="parent"
        app:layout_constraintLeft_toLeftOf="parent"
        app:layout_constraintRight_toRightOf="parent"
        app:layout_constraintTop_toTopOf="parent" />

    <com.zhxh.xchartlib.LineChart
        android:id="@+id/lineChart3"
        android:layout_width="260dp"
        android:layout_height="140dp"
        android:background="@android:color/white"
        app:XaxisColor="#E8E8E8"
        app:XcanvasHeight="140dp"
        app:XcanvasWidth="260dp"
        app:XisAnim="false"
        app:XlineColor="#ff4c51"
        app:XshowXcount="4"
        app:XshowYcount="5"
        app:XtextColor="#8997A5"
        app:layout_constraintBottom_toBottomOf="parent"
        app:layout_constraintLeft_toLeftOf="parent"
        app:layout_constraintRight_toRightOf="parent"
        app:layout_constraintTop_toTopOf="parent"
        app:layout_constraintVertical_bias="0.927" />

~~~

# Version 1.3 发布说明

加上了动画效果，动态展示折线绘制过程

~~~
app:XisAnim="false"
~~~

加上了x轴数量的显示，并单独对首项和末项处理文字显示位置

~~~
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
~~~
### BarChart引用
待补充

### CandleChart引用
待补充

### PieChart引用
待补充

### RadarChart引用
待补充

### AreaChart引用
待补充

### SurfaceChart引用
待补充

# Version 1.4 发布说明

加上了可配置y轴数据的位置的地方

~~~
        app:XshowXcount="3"
        app:XshowYcount="6"
~~~

# Version 2.1 发布说明

加上了设置shader效果

~~~
        app:XshaderEndColor="#70ff4c51"
        app:XshaderStartColor="#00ff4c51"
~~~
# Version 2.2 发布说明

y轴上数值单位与格式配置

~~~
        lineChart2.bindData(dataList1);
        lineChart2.bindYUnit("万");
        lineChart2.bindYFormat("%.1f");
        lineChart2.show();
~~~

# 下版本计划

待定




# 开源协议

 > Copyright (C) 2018, zhxh
 >
 > Licensed under the Apache License, Version 2.0 (the "License");
 > you may not use this file except in compliance with the License.
 > You may obtain a copy of the License at
 >
 > http://www.apache.org/licenses/LICENSE-2.0
 >
 > Unless required by applicable law or agreed to in writing, software
 > distributed under the License is distributed on an "AS IS" BASIS,
 > WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 > See the License for the specific language governing permissions and
 > limitations under the License.
