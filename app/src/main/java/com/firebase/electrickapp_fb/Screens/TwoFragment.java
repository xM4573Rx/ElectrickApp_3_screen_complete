package com.firebase.electrickapp_fb.Screens;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.electrickapp_fb.MainActivity;
import com.firebase.electrickapp_fb.R;
import com.firebase.electrickapp_fb.misc.Consumos;
import com.firebase.electrickapp_fb.misc.RecyclerViewAdapterConsumo;
import com.firebase.electrickapp_fb.misc.RecyclerViewAdapterTiempo;
import com.firebase.electrickapp_fb.misc.Tiempos;
import com.firebase.electrickapp_fb.transfer.Selector;
import com.firebase.electrickapp_fb.transfer.Time;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class TwoFragment extends Fragment {

    public String telefono;
    public String modo;
    public String tiempo;
    public String dias;
    public String semanas;
    public String quincenas;
    public String meses;
    public int selector;
    public int time;

    RecyclerViewAdapterConsumo adapter_consumo;
    RecyclerViewAdapterTiempo adapter_tiempo;

    RecyclerView recyclerView_consumo;
    RecyclerView recyclerView_tiempo;

    private ArrayList<Consumos> mConsumoList = new ArrayList<>();
    private ArrayList<Tiempos> mTiempoList = new ArrayList<>();
    private BarChart barChart;

    private TextView rconsumo, lconsumo, rtiempo, ltiempo;

    DatabaseReference mDataBaseReference = FirebaseDatabase.getInstance().getReference();
    DatabaseReference mSelectorChild = mDataBaseReference.child("User");
    DatabaseReference mTimeChild = mDataBaseReference.child("User");
    DatabaseReference mDiasChild = mDataBaseReference.child("User");
    DatabaseReference mSemanasChild = mDataBaseReference.child("User");
    DatabaseReference mQuincenasChild = mDataBaseReference.child("User");
    DatabaseReference mMesesChild = mDataBaseReference.child("User");

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View PageTwo = inflater.inflate(R.layout.fragment_two, container, false);

        barChart = (BarChart) PageTwo.findViewById(R.id.barchart);

        lconsumo = (TextView) PageTwo.findViewById(R.id.decremento_consumo);
        rconsumo = (TextView) PageTwo.findViewById(R.id.incremento_consumo);
        ltiempo = (TextView) PageTwo.findViewById(R.id.decremento_tiempo);
        rtiempo = (TextView) PageTwo.findViewById(R.id.incremento_tiempo);

        if (getArguments() != null) {
            telefono = getArguments().getString(MainActivity.DATO_KEY_TEL);
            modo = getArguments().getString(MainActivity.DATO_KEY_SELE);
            tiempo = getArguments().getString(MainActivity.DATO_KEY_TIME);
        }

        //Toast.makeText(getContext(),"Number = " + modo, Toast.LENGTH_SHORT).show();

        barChart.getDescription().setEnabled(false);

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

        switch (modo) {
            case "1":
                lconsumo.setVisibility(View.INVISIBLE);
                rconsumo.setVisibility(View.VISIBLE);
                recyclerView_consumo.scrollToPosition(0);
                selector = 1;
                break;
            case "2":
                lconsumo.setVisibility(View.VISIBLE);
                rconsumo.setVisibility(View.INVISIBLE);
                recyclerView_consumo.scrollToPosition(1);
                selector = 2;
                break;
        }

        switch (tiempo) {
            case "1":
                ltiempo.setVisibility(View.INVISIBLE);
                rtiempo.setVisibility(View.VISIBLE);
                recyclerView_tiempo.scrollToPosition(0);
                time = 1;
                break;
            case "2":
                ltiempo.setVisibility(View.VISIBLE);
                rtiempo.setVisibility(View.VISIBLE);
                recyclerView_tiempo.scrollToPosition(1);
                time = 2;
                break;
            case "3":
                ltiempo.setVisibility(View.VISIBLE);
                rtiempo.setVisibility(View.VISIBLE);
                recyclerView_tiempo.scrollToPosition(2);
                time = 3;
                break;
            case "4":
                ltiempo.setVisibility(View.VISIBLE);
                rtiempo.setVisibility(View.INVISIBLE);
                recyclerView_tiempo.scrollToPosition(3);
                time = 4;
                break;
        }

        Selector s = new Selector();
        s.setModo(String.valueOf(selector));
        mSelectorChild.child(telefono).child("Historico").child("Graph").child("Selector").setValue(s);

        Time t = new Time();
        t.setTiempo(String.valueOf(time));
        mTimeChild.child(telefono).child("Historico").child("Graph").child("Tiempo").setValue(t);

        recyclerView_consumo.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerview, int newState) {
                super.onScrollStateChanged(recyclerview, newState);

                if(newState == RecyclerView.SCROLL_STATE_IDLE) {
                    View centerView = snapHelper1.findSnapView(mLayoutManager1);

                    if (mConsumoList.get(recyclerView_consumo.getChildAdapterPosition(centerView)).getmConsumo1().equals("$")){
                        lconsumo.setVisibility(View.INVISIBLE);
                        rconsumo.setVisibility(View.VISIBLE);
                        selector = 1;
                        Toast.makeText(getContext(),"Consumo en pesos", Toast.LENGTH_SHORT).show();
                    }
                    else if (mConsumoList.get(recyclerView_consumo.getChildAdapterPosition(centerView)).getmConsumo1().equals("kWh")){
                        lconsumo.setVisibility(View.VISIBLE);
                        rconsumo.setVisibility(View.INVISIBLE);
                        selector = 2;
                        Toast.makeText(getContext(),"Consumo en kWh", Toast.LENGTH_SHORT).show();
                    }

                    Selector s = new Selector();
                    s.setModo(String.valueOf(selector));
                    mSelectorChild.child(telefono).child("Historico").child("Graph").child("Selector").setValue(s);
                }
            }
        });

        recyclerView_tiempo.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if(newState == RecyclerView.SCROLL_STATE_IDLE) {
                    View centerView = snapHelper2.findSnapView(mLayoutManager2);

                    if (mTiempoList.get(recyclerView_tiempo.getChildAdapterPosition(centerView)).getmTiempo1() == 2131230844){
                        ltiempo.setVisibility(View.INVISIBLE);
                        rtiempo.setVisibility(View.VISIBLE);
                        time = 1;
                        Toast.makeText(getContext(),"Vista diaria", Toast.LENGTH_SHORT).show();
                    }
                    else if (mTiempoList.get(recyclerView_tiempo.getChildAdapterPosition(centerView)).getmTiempo1() == 2131230847){
                        ltiempo.setVisibility(View.VISIBLE);
                        rtiempo.setVisibility(View.VISIBLE);
                        time = 2;
                        Toast.makeText(getContext(),"Vista semanal", Toast.LENGTH_SHORT).show();
                    }
                    else if (mTiempoList.get(recyclerView_tiempo.getChildAdapterPosition(centerView)).getmTiempo1() == 2131230845){
                        ltiempo.setVisibility(View.VISIBLE);
                        rtiempo.setVisibility(View.VISIBLE);
                        time = 3;
                        Toast.makeText(getContext(),"Vista quincenal", Toast.LENGTH_SHORT).show();
                    }
                    else if (mTiempoList.get(recyclerView_tiempo.getChildAdapterPosition(centerView)).getmTiempo1() == 2131230846){
                        ltiempo.setVisibility(View.VISIBLE);
                        rtiempo.setVisibility(View.INVISIBLE);
                        time = 4;
                        Toast.makeText(getContext(),"Vista mensual", Toast.LENGTH_SHORT).show();
                    }

                    Time t = new Time();
                    t.setTiempo(String.valueOf(time));
                    mTimeChild.child(telefono).child("Historico").child("Graph").child("Tiempo").setValue(t);
                }
            }
        });

        mSelectorChild.child(telefono).child("Historico").child("Graph").child("Selector").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String sele = null;
                try {
                    sele = dataSnapshot.child("modo").getValue().toString();
                }catch (NullPointerException ignored){

                }
                int se = Integer.valueOf(sele);
                if (se == 1) {
                    mTimeChild.child(telefono).child("Historico").child("Graph").child("Tiempo").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            int ti = Integer.valueOf(dataSnapshot.child("tiempo").getValue().toString());
                            switch (ti) {
                                case 1:
                                    Dias_pesos(telefono);
                                    break;
                                case 2:
                                    Semanas_pesos(telefono);
                                    break;
                                case 3:
                                    Quincenas_pesos(telefono);
                                    break;
                                case 4:
                                    Meses_pesos(telefono);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }else{
                    mTimeChild.child(telefono).child("Historico").child("Graph").child("Tiempo").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            int ti = Integer.valueOf(dataSnapshot.child("tiempo").getValue().toString());
                            switch (ti) {
                                case 1:
                                    Dias_kwh(telefono);
                                    break;
                                case 2:
                                    Semanas_kwh(telefono);
                                    break;
                                case 3:
                                    Quincenas_kwh(telefono);
                                    break;
                                case 4:
                                    Meses_kwh(telefono);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

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

    private void Dias_pesos(String telefono) {
        mDiasChild.child(telefono).child("Historico").child("Dias").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                dias = (String) dataSnapshot.child("historico").getValue();
                Float dias1 = Float.valueOf(dias.substring(0, dias.indexOf("#")));
                Float dias2 = Float.valueOf(dias.substring(dias.indexOf("#") + 1, dias.indexOf("$")));
                Float dias3 = Float.valueOf(dias.substring(dias.indexOf("$") + 1, dias.indexOf("%")));
                Float dias4 = Float.valueOf(dias.substring(dias.indexOf("%") + 1, dias.indexOf("&")));

                ArrayList<BarEntry> barEntries = new ArrayList<>();

                barEntries.add(new BarEntry(1,dias1));
                barEntries.add(new BarEntry(2,dias2));
                barEntries.add(new BarEntry(3,dias3));
                barEntries.add(new BarEntry(4,dias4));

                String[] months = new String[] {" ", "Enero", "Febrero", "Marzo", "Abril"};

                BarDataSet barDataSet = new BarDataSet(barEntries, "Consumo diario en pesos");
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
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void Semanas_pesos(String telefono) {
        mSemanasChild.child(telefono).child("Historico").child("Semanas").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                semanas = (String) dataSnapshot.child("historico").getValue();
                Float semanas1 = Float.valueOf(semanas.substring(0, semanas.indexOf("#")));
                Float semanas2 = Float.valueOf(semanas.substring(semanas.indexOf("#") + 1, semanas.indexOf("$")));
                Float semanas3 = Float.valueOf(semanas.substring(semanas.indexOf("$") + 1, semanas.indexOf("%")));
                Float semanas4 = Float.valueOf(semanas.substring(semanas.indexOf("%") + 1, semanas.indexOf("&")));

                ArrayList<BarEntry> barEntries = new ArrayList<>();

                barEntries.add(new BarEntry(1,semanas1));
                barEntries.add(new BarEntry(2,semanas2));
                barEntries.add(new BarEntry(3,semanas3));
                barEntries.add(new BarEntry(4,semanas4));

                String[] months = new String[] {" ", "Enero", "Febrero", "Marzo", "Abril"};

                BarDataSet barDataSet = new BarDataSet(barEntries, "Consumo semanal en pesos");
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
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void Quincenas_pesos(String telefono) {
        mQuincenasChild.child(telefono).child("Historico").child("Quincenas").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                quincenas = (String) dataSnapshot.child("historico").getValue();
                Float quincenas1 = Float.valueOf(quincenas.substring(0, quincenas.indexOf("#")));
                Float quincenas2 = Float.valueOf(quincenas.substring(quincenas.indexOf("#") + 1, quincenas.indexOf("$")));
                Float quincenas3 = Float.valueOf(quincenas.substring(quincenas.indexOf("$") + 1, quincenas.indexOf("%")));
                Float quincenas4 = Float.valueOf(quincenas.substring(quincenas.indexOf("%") + 1, quincenas.indexOf("&")));

                ArrayList<BarEntry> barEntries = new ArrayList<>();

                barEntries.add(new BarEntry(1,quincenas1));
                barEntries.add(new BarEntry(2,quincenas2));
                barEntries.add(new BarEntry(3,quincenas3));
                barEntries.add(new BarEntry(4,quincenas4));

                String[] months = new String[] {" ", "Enero", "Febrero", "Marzo", "Abril"};

                BarDataSet barDataSet = new BarDataSet(barEntries, "Consumo quincenal en pesos");
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
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void Meses_pesos(String telefono) {
        mMesesChild.child(telefono).child("Historico").child("Meses").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                meses = (String) dataSnapshot.child("historico").getValue();
                Float meses1 = Float.valueOf(meses.substring(0, meses.indexOf("#")));
                Float meses2 = Float.valueOf(meses.substring(meses.indexOf("#") + 1, meses.indexOf("$")));
                Float meses3 = Float.valueOf(meses.substring(meses.indexOf("$") + 1, meses.indexOf("%")));
                Float meses4 = Float.valueOf(meses.substring(meses.indexOf("%") + 1, meses.indexOf("&")));

                ArrayList<BarEntry> barEntries = new ArrayList<>();

                barEntries.add(new BarEntry(1,meses1));
                barEntries.add(new BarEntry(2,meses2));
                barEntries.add(new BarEntry(3,meses3));
                barEntries.add(new BarEntry(4,meses4));

                String[] months = new String[] {" ", "Enero", "Febrero", "Marzo", "Abril"};

                BarDataSet barDataSet = new BarDataSet(barEntries, "Consumo mensual en pesos");
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
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void Dias_kwh(String telefono) {
        mDiasChild.child(telefono).child("Historico").child("Dias").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                dias = (String) dataSnapshot.child("historico").getValue();
                Float dias1 = Float.valueOf(dias.substring(0, dias.indexOf("#")));
                Float dias2 = Float.valueOf(dias.substring(dias.indexOf("#") + 1, dias.indexOf("$")));
                Float dias3 = Float.valueOf(dias.substring(dias.indexOf("$") + 1, dias.indexOf("%")));
                Float dias4 = Float.valueOf(dias.substring(dias.indexOf("%") + 1, dias.indexOf("&")));

                ArrayList<BarEntry> barEntries = new ArrayList<>();

                barEntries.add(new BarEntry(1,dias1));
                barEntries.add(new BarEntry(2,dias2));
                barEntries.add(new BarEntry(3,dias3));
                barEntries.add(new BarEntry(4,dias4));

                String[] months = new String[] {" ", "Enero", "Febrero", "Marzo", "Abril"};

                BarDataSet barDataSet = new BarDataSet(barEntries, "Consumo diario en kWh");
                barDataSet.setColor(Color.parseColor("#FFC107"));

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
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void Semanas_kwh(String telefono) {
        mSemanasChild.child(telefono).child("Historico").child("Semanas").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                semanas = (String) dataSnapshot.child("historico").getValue();
                Float semanas1 = Float.valueOf(semanas.substring(0, semanas.indexOf("#")));
                Float semanas2 = Float.valueOf(semanas.substring(semanas.indexOf("#") + 1, semanas.indexOf("$")));
                Float semanas3 = Float.valueOf(semanas.substring(semanas.indexOf("$") + 1, semanas.indexOf("%")));
                Float semanas4 = Float.valueOf(semanas.substring(semanas.indexOf("%") + 1, semanas.indexOf("&")));

                ArrayList<BarEntry> barEntries = new ArrayList<>();

                barEntries.add(new BarEntry(1,semanas1));
                barEntries.add(new BarEntry(2,semanas2));
                barEntries.add(new BarEntry(3,semanas3));
                barEntries.add(new BarEntry(4,semanas4));

                String[] months = new String[] {" ", "Enero", "Febrero", "Marzo", "Abril"};

                BarDataSet barDataSet = new BarDataSet(barEntries, "Consumo semanal en kWh");
                barDataSet.setColor(Color.parseColor("#FFC107"));

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
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void Quincenas_kwh(String telefono) {
        mQuincenasChild.child(telefono).child("Historico").child("Quincenas").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                quincenas = (String) dataSnapshot.child("historico").getValue();
                Float quincenas1 = Float.valueOf(quincenas.substring(0, quincenas.indexOf("#")));
                Float quincenas2 = Float.valueOf(quincenas.substring(quincenas.indexOf("#") + 1, quincenas.indexOf("$")));
                Float quincenas3 = Float.valueOf(quincenas.substring(quincenas.indexOf("$") + 1, quincenas.indexOf("%")));
                Float quincenas4 = Float.valueOf(quincenas.substring(quincenas.indexOf("%") + 1, quincenas.indexOf("&")));

                ArrayList<BarEntry> barEntries = new ArrayList<>();

                barEntries.add(new BarEntry(1,quincenas1));
                barEntries.add(new BarEntry(2,quincenas2));
                barEntries.add(new BarEntry(3,quincenas3));
                barEntries.add(new BarEntry(4,quincenas4));

                String[] months = new String[] {" ", "Enero", "Febrero", "Marzo", "Abril"};

                BarDataSet barDataSet = new BarDataSet(barEntries, "Consumo quincenal en kWh");
                barDataSet.setColor(Color.parseColor("#FFC107"));

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
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void Meses_kwh(String telefono) {
        mMesesChild.child(telefono).child("Historico").child("Meses").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                meses = (String) dataSnapshot.child("historico").getValue();
                Float meses1 = Float.valueOf(meses.substring(0, meses.indexOf("#")));
                Float meses2 = Float.valueOf(meses.substring(meses.indexOf("#") + 1, meses.indexOf("$")));
                Float meses3 = Float.valueOf(meses.substring(meses.indexOf("$") + 1, meses.indexOf("%")));
                Float meses4 = Float.valueOf(meses.substring(meses.indexOf("%") + 1, meses.indexOf("&")));

                ArrayList<BarEntry> barEntries = new ArrayList<>();

                barEntries.add(new BarEntry(1,meses1));
                barEntries.add(new BarEntry(2,meses2));
                barEntries.add(new BarEntry(3,meses3));
                barEntries.add(new BarEntry(4,meses4));

                String[] months = new String[] {" ", "Enero", "Febrero", "Marzo", "Abril"};

                BarDataSet barDataSet = new BarDataSet(barEntries, "Consumo mensual en kWh");
                barDataSet.setColor(Color.parseColor("#FFC107"));

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
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
