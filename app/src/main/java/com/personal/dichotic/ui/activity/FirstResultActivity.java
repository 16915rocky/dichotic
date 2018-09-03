package com.personal.dichotic.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.personal.dichotic.R;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Rocky on 2018/5/14 0014.
 */

public class LastActivity extends AppCompatActivity {
    @BindView(R.id.tb_find)
    Toolbar tbFind;
    @BindView(R.id.pie_chart)
    PieChart pieChart;
    @BindView(R.id.tv_rea_score)
    TextView tvReaScore;
    @BindView(R.id.tv_same_score)
    TextView tvSameScore;
    private int leftScore = 0, rightScore = 0, reaScore = 0,errorScore = 0, sameScore = 0;
    private int count = 0;
    private DecimalFormat mFormat;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        leftScore = getIntent().getIntExtra("leftScore", 0);
        rightScore = getIntent().getIntExtra("rightScore", 0);
        sameScore = getIntent().getIntExtra("sameScore", 0);
        errorScore = 30-leftScore-rightScore;
        reaScore = Math.abs(leftScore - rightScore);
        setContentView(R.layout.activity_last);
        ButterKnife.bind(this);
        //设置%小数点位数
        mFormat = new DecimalFormat("###,###,##0.0"); // use one decimal
        setToolbar();
        setPieChart();
        if (rightScore > leftScore) {
            tvReaScore.setText(mFormat.format(((float) reaScore / 30 * 100)) + "%");
        }else{
            tvReaScore.setText(" -"+mFormat.format(((float) reaScore / 30 * 100)) + "%");
        }
        tvSameScore.setText(mFormat.format(((float) sameScore / 6 * 100))+"%"+"("+sameScore+"/6)");
    }

    private void setToolbar() {
        tbFind.setNavigationIcon(R.mipmap.ic_arrow_left);
        setSupportActionBar(tbFind);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        tbFind.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent();
                intent.putExtra("reset","false");
                intent.setClass(LastActivity.this, SelectionPatternActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void setPieChart() {
        List<PieEntry> strings = new ArrayList<>();

        strings.add(new PieEntry((float) leftScore / 30 * 100, "左耳分数:" + leftScore + "/30"));
        strings.add(new PieEntry((float) rightScore / 30 * 100, "右耳分数:" + rightScore + "/30"));
        strings.add(new PieEntry((float) errorScore / 30 * 100, "错误分数:" + errorScore     + "/30"));

        //strings.add(new PieEntry(70f, "REA"));

        PieDataSet dataSet = new PieDataSet(strings, "");

        ArrayList<Integer> colors = new ArrayList<Integer>();
        colors.add(getResources().getColor(R.color.colorRed));
        colors.add(getResources().getColor(R.color.colorLightBlue));
        colors.add(getResources().getColor(R.color.colorGreen));
        dataSet.setColors(colors);
        dataSet.setValueFormatter(new MyValueFormatter());
        PieData pieData = new PieData(dataSet);
        pieData.setDrawValues(true);
        pieData.setValueTextSize(15f);
        Description description = new Description();
        description.setText("分听结果");
        description.setTextSize(15f);
        pieChart.setDescription(description);
        pieChart.setHoleRadius(0f);
        pieChart.setTransparentCircleRadius(0f);
        pieChart.setData(pieData);
        pieChart.animateX(3000, Easing.EasingOption.EaseOutBack);
        pieChart.animateY(3000, Easing.EasingOption.EaseOutBack);
        pieChart.invalidate();


    }

    public class MyValueFormatter implements IValueFormatter {



        public MyValueFormatter() {

        }

        @Override
        public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
            // write your logic here

            return mFormat.format(value) + " %"; // e.g. append a dollar-sign
        }
    }
}
