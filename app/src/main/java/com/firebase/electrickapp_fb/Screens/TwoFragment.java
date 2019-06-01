package com.firebase.electrickapp_fb.Screens;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.electrickapp_fb.R;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;

import java.util.ArrayList;
import java.util.List;

public class TwoFragment extends Fragment {

    BarChart barChart;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View PageTwo = inflater.inflate(R.layout.fragment_two, container, false);

        barChart = (BarChart) PageTwo.findViewById(R.id.barchart);

        barChart.getDescription().setEnabled(false);

        ArrayList<BarEntry> barEntries = new ArrayList<>();

        barEntries.add(new BarEntry(1,10f));
        barEntries.add(new BarEntry(2,8f));
        barEntries.add(new BarEntry(3,19));
        barEntries.add(new BarEntry(4,4f));

        String[] months = new String[] {" ", "Enero", "Febrero", "Marzo", "Abril"};

        BarDataSet barDataSet = new BarDataSet(barEntries, "Consumo");
        barDataSet.setColor(Color.parseColor("#00BCD4"));

        BarData data = new BarData(barDataSet);
        data.setBarWidth(0.9f); // set custom bar width

        barChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(months));
        barChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        barChart.getXAxis().setGranularityEnabled(true);
        barChart.getXAxis().setGranularity(1f);
        barChart.getXAxis().setLabelCount(4);
        barChart.getXAxis().setCenterAxisLabels(false);
        barChart.getXAxis().setDrawAxisLine(false);
        barChart.getXAxis().setDrawGridLines(false);
        barChart.getAxisRight().setEnabled(false);

        barChart.setFitBars(false);
        barChart.setTouchEnabled(false);
        barChart.setData(data);
        barChart.invalidate();



        return PageTwo;
    }
}
