package com.example.intern.hello_world;

import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.Date;

public class MainActivity extends AppCompatActivity {
    private TextView text;
    private TextView text2;
    private TextView text3;
    private Switch button2;
    private Handler hand = new Handler();
    private CpuInfo reader = new CpuInfo();
    private LineChart mChart;


    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textSetter();
        periodic();
        buttonSetter();
        chartSetter();
    }
    private void periodic(){//定期処理を開始させる
        hand.postDelayed( func, 1000);
    }
    private final Runnable func= new Runnable() {
        @Override
        public void run() {

            heatSet();
            text3.setText((new Date()).toString());
            addEntry();
            hand.postDelayed( func, 1000);
        }
    };
    private void addEntry() {
        LineData data = mChart.getData();
        if (data != null) {
            ILineDataSet set = data.getDataSetByIndex(0);
            if (set == null) {
                set = createSet();
                data.addDataSet(set);
            }
            String bunkatu[] = cpuReader();
            data.addEntry(new Entry(set.getEntryCount(), (float) (Integer.parseInt(bunkatu[1]))), 0);
            text2.setText(bunkatu[0]);
            data.notifyDataChanged();
            mChart.notifyDataSetChanged();
            mChart.setVisibleXRangeMaximum(120);
            mChart.moveViewToX(data.getEntryCount());
        }
    }
    private LineDataSet createSet() {

        LineDataSet set = new LineDataSet(null, "Dynamic Data");
        set.setAxisDependency(YAxis.AxisDependency.LEFT);
        set.setColor(ColorTemplate.getHoloBlue());
        set.setCircleColor(Color.WHITE);
        set.setLineWidth(2f);
        set.setCircleRadius(4f);
        set.setFillAlpha(65);
        set.setFillColor(ColorTemplate.getHoloBlue());
        set.setHighLightColor(Color.rgb(244, 117, 117));
        set.setValueTextColor(Color.WHITE);
        set.setValueTextSize(9f);
        set.setDrawValues(false);
        return set;
    }
    private void textSetter(){
        text  = (TextView)findViewById(R.id.textview);
        text2 = (TextView)findViewById(R.id.textview2);
        text3 = (TextView)findViewById(R.id.textview3);
        text.setText(reader.fileRead());
        text2.setText(cpuReader()[0]);
    }
    private void buttonSetter(){
        button2 = (Switch)findViewById(R.id.switch1);
        button2.setOnCheckedChangeListener(
                new CompoundButton.OnCheckedChangeListener() {

                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                        if(isChecked){
                            Intent intent = new Intent(MainActivity.this, MyService.class);
                            MainActivity.this.startService(intent);
                        }
                        else{
                            Intent intent = new Intent(MainActivity.this, MyService.class);
                            MainActivity.this.startService(intent);
                        }
                    }
                }
        );
    }
    private void chartSetter(){

        mChart = (LineChart) findViewById(R.id.chart1);
        mChart.setOnChartValueSelectedListener(null);
        mChart.setDescription("");
        mChart.setNoDataTextDescription("You need to provide data for the chart.");
        mChart.setTouchEnabled(true);

        // enable scaling and dragging
        mChart.setDragEnabled(true);
        mChart.setScaleEnabled(true);
        mChart.setDrawGridBackground(false);

        // if disabled, scaling can be done on x- and y-axis separately
        mChart.setPinchZoom(true);

        // set an alternative background color
        mChart.setBackgroundColor(Color.LTGRAY);

        LineData data = new LineData();
        data.setValueTextColor(Color.WHITE);

        // add empty data
        mChart.setData(data);

        // get the legend (only possible after setting data)
        Legend l = mChart.getLegend();

        // modify the legend ...
        // l.setPosition(LegendPosition.LEFT_OF_CHART);
        l.setForm(Legend.LegendForm.LINE);
        //l.setTypeface(mTfLight);
        l.setTextColor(Color.WHITE);

        XAxis xl = mChart.getXAxis();
        // xl.setTypeface(mTfLight);
        xl.setTextColor(Color.WHITE);
        xl.setDrawGridLines(false);
        xl.setAvoidFirstLastClipping(true);
        xl.setEnabled(true);

        YAxis leftAxis = mChart.getAxisLeft();
        //leftAxis.setTypeface(mTfLight);
        leftAxis.setTextColor(Color.WHITE);
        leftAxis.setAxisMaxValue(100f);
        leftAxis.setAxisMinValue(0f);
        leftAxis.setDrawGridLines(true);

        YAxis rightAxis = mChart.getAxisRight();
        rightAxis.setEnabled(false);
    }
    private String[] cpuReader(){
        String[] cpu = reader.cpuRead();
        if (Integer.parseInt(cpu[1])>50){
            text2.setTextColor(Color.RED);
        }
        else{
            text2.setTextColor(Color.BLACK);
        }
        return cpu;
    }
    private void heatSet(){
        String heat = reader.fileRead();
        if (!heat.equals("℃")) {
            if (Integer.parseInt(heat.substring(0, heat.length() - 1)) >= 50) {
                text.setTextColor(Color.RED);
            } else {
                text.setTextColor(Color.BLACK);
            }
        }
        text.setText(heat);
    }
}
