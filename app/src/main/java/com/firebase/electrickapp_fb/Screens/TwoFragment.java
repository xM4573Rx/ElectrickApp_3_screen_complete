package com.firebase.electrickapp_fb.Screens;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.firebase.electrickapp_fb.R;
import com.firebase.electrickapp_fb.misc.Consumos;
import com.firebase.electrickapp_fb.misc.RecyclerViewAdapterConsumo;
import com.firebase.electrickapp_fb.misc.RecyclerViewAdapterTiempo;
import com.firebase.electrickapp_fb.misc.Tiempos;
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

    RecyclerViewAdapterConsumo adapter_consumo;
    RecyclerViewAdapterTiempo adapter_tiempo;

    RecyclerView recyclerView_consumo;
    RecyclerView recyclerView_tiempo;

    private ArrayList<Consumos> mConsumoList = new ArrayList<>();
    private ArrayList<Tiempos> mTiempoList = new ArrayList<>();
    private BarChart barChart;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View PageTwo = inflater.inflate(R.layout.fragment_two, container, false);

        barChart = (BarChart) PageTwo.findViewById(R.id.barchart);

        barChart.getDescription().setEnabled(false);

        ArrayList<BarEntry> barEntries = new ArrayList<>();

        barEntries.add(new BarEntry(1,10f));
        barEntries.add(new BarEntry(2,8f));
        barEntries.add(new BarEntry(3,19f));
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

        recyclerView_consumo = (RecyclerView) PageTwo.findViewById(R.id.consumo_list);
        recyclerView_tiempo = (RecyclerView) PageTwo.findViewById(R.id.tiempo_list);
        // add a divider after each item for more clarity

        final RecyclerView.LayoutManager mLayoutManager1 = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        final RecyclerView.LayoutManager mLayoutManager2 = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView_consumo.setLayoutManager(mLayoutManager1);
        recyclerView_tiempo.setLayoutManager(mLayoutManager2);

        adapter_consumo = new RecyclerViewAdapterConsumo(mConsumoList, getContext());
        adapter_tiempo = new RecyclerViewAdapterTiempo(mTiempoList, getContext());
        recyclerView_consumo.setAdapter(adapter_consumo);
        recyclerView_tiempo.setAdapter(adapter_tiempo);

        final SnapHelper snapHelper1 = new LinearSnapHelper();
        final SnapHelper snapHelper2 = new LinearSnapHelper();
        snapHelper1.attachToRecyclerView(recyclerView_consumo);
        snapHelper2.attachToRecyclerView(recyclerView_tiempo);

        recyclerView_consumo.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerview, int newState) {
                super.onScrollStateChanged(recyclerview, newState);

                if(newState == RecyclerView.SCROLL_STATE_IDLE) {
                    View centerView = snapHelper1.findSnapView(mLayoutManager1);

                    int itemCount = recyclerView_consumo.getChildAdapterPosition(centerView) - 1;
                    int offset = (recyclerView_consumo.getWidth() / recyclerView_consumo.getWidth() - 1) / 2;
                    int position = ((LinearLayoutManager) mLayoutManager1).findFirstVisibleItemPosition() + offset;

                    Toast.makeText(getContext(),"posicion = " + mConsumoList.get(recyclerView_consumo.getChildAdapterPosition(centerView)).getmConsumo1(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        recyclerView_tiempo.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerview, int newState) {
                super.onScrollStateChanged(recyclerview, newState);

                if(newState == RecyclerView.SCROLL_STATE_IDLE) {
                    View centerView = snapHelper2.findSnapView(mLayoutManager2);

                    int itemCount = recyclerView_consumo.getChildAdapterPosition(centerView) - 1;
                    int offset = (recyclerView_consumo.getWidth() / recyclerView_consumo.getWidth() - 1) / 2;
                    int position = ((LinearLayoutManager) mLayoutManager2).findFirstVisibleItemPosition() + offset;

                    //Toast.makeText(getContext(),"posicion = " + mTiempoList.get(recyclerView_tiempo.getChildAdapterPosition(centerView)).getmTiempo1(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        consumoList();
        tiempoList();

        return PageTwo;
    }

    private void consumoList() {
        Consumos empty = new Consumos("");
        Consumos cons = new Consumos("$");
        Consumos time = new Consumos("kWh");
        Consumos uno = new Consumos("Quincena");
        Consumos dos = new Consumos("Meses");
        mConsumoList.add(cons);
        mConsumoList.add(time);
        //mConsumoList.add(uno);
        //mConsumoList.add(dos);
        adapter_consumo.notifyDataSetChanged();
    }

    private void tiempoList() {
        Tiempos dias = new Tiempos(R.drawable.ic_calendar_1);
        Tiempos semanas = new Tiempos(R.drawable.ic_calendar_7);
        Tiempos quincenas = new Tiempos(R.drawable.ic_calendar_15);
        Tiempos meses = new Tiempos(R.drawable.ic_calendar_30);
        mTiempoList.add(dias);
        mTiempoList.add(semanas);
        mTiempoList.add(quincenas);
        mTiempoList.add(meses);
        adapter_tiempo.notifyDataSetChanged();
    }
}
