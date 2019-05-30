package com.firebase.electrickapp_fb.Screens;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.electrickapp_fb.MainActivity;
import com.firebase.electrickapp_fb.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.text.DecimalFormat;

public class OneFragment extends Fragment {

    public String watt;
    public String proyeccion;
    public String watthora;
    public String ActualWatt;
    public String Costtext;
    public String unidad;
    public String telefono;

    public double cost;
    public double wattactual;
    public double costwatt;
    public double valor;

    public int Porcentaje = 0;
    public double Aux = 0.0;

    TextView watts;
    TextView tv1, tv2, tv3, tv4, tv5, tv6;
    ProgressBar progressBar;

    DatabaseReference mDataBaseReference = FirebaseDatabase.getInstance().getReference();
    DatabaseReference mWattChild = mDataBaseReference.child("User");
    DatabaseReference mWattHoraChild = mDataBaseReference.child("User");
    DatabaseReference mProyeccionChild = mDataBaseReference.child("User");

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View PageOne = inflater.inflate(R.layout.fragment_one, container, false);

        progressBar = (ProgressBar) PageOne.findViewById(R.id.IdProgress);
        tv1 = (TextView) PageOne.findViewById(R.id.IdBufferIn);
        tv2 = (TextView) PageOne.findViewById(R.id.textView2);
        tv3 = (TextView) PageOne.findViewById(R.id.textView7);
        tv4 = (TextView) PageOne.findViewById(R.id.textView3);
        tv5 = (TextView) PageOne.findViewById(R.id.WattInstantaneo);
        tv6 = (TextView) PageOne.findViewById(R.id.Unidad);

        if (getArguments() != null) {
            telefono = getArguments().getString(MainActivity.DATO_KEY_TEL);
            unidad = this.getArguments().getString(MainActivity.DATO_KEY_NAME);
        }

        //Toast.makeText(getContext(),"Number = " + unidad, Toast.LENGTH_SHORT).show();

        mProyeccionChild.child(telefono).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                proyeccion = (String) dataSnapshot.child("proyeccion").getValue();
                String watthora = null;
                try {
                    watthora = dataSnapshot.child("wattHora").getValue().toString();
                }catch (NullPointerException ignored){

                }
                wattactual = Float.valueOf(watthora) * 0.001;
                costwatt = 466.14;
                cost = wattactual * costwatt;
                valor = Double.valueOf(proyeccion.substring(0, proyeccion.indexOf("#")));
                unidad = proyeccion.substring(proyeccion.indexOf("#") + 1, proyeccion.indexOf("*"));
                //Toast.makeText(UserInterfaz.this,unidad, Toast.LENGTH_SHORT).show();
                ////////////////////////////////////////////////////////////////////////////////////////////

                Porcentaje = (int) (Aux*100/valor);
                if(Porcentaje >= 100){
                    Porcentaje = 100;
                }

                tv4.setText("Proyección");
                switch (unidad) {
                case "kWh":
                    // int D = (int) (wattactual*100.0/valor);
                    Aux = wattactual;

                    tv1.setText(String.valueOf((int) valor));
                    tv6.setText("kWh");
                    break;
                case "$":
                    //  int D = (int) (cost*100.0/valor);
                    Aux = cost;

                    tv1.setText(String.valueOf((int)valor));
                    tv6.setText("$");
                    break;
                }

                progressBar.setProgress(Porcentaje);
                progressBar.getProgressDrawable().setColorFilter(Color.argb(255,(int)(2.55*Porcentaje),(int)(255-2.55*Porcentaje) ,0), PorterDuff.Mode.SRC_IN);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        mWattChild.child(telefono).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                watt = dataSnapshot.child("watt").getValue().toString();
                tv5.setText(watt + " Watts");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        mWattHoraChild.child(telefono).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                watthora = dataSnapshot.child("wattHora").getValue().toString();
                wattactual = Float.valueOf(watthora) * 0.001;
                costwatt = 466.14;
                cost = wattactual * costwatt;
                ActualWatt = String.valueOf(new DecimalFormat("##.###").format(wattactual));
                Costtext = String.valueOf(new DecimalFormat("##.##").format(cost));
                proyeccion = dataSnapshot.child("proyeccion").getValue().toString();
                valor = Double.valueOf(proyeccion.substring(0, proyeccion.indexOf("#")));
                unidad = proyeccion.substring(proyeccion.indexOf("#") + 1, proyeccion.indexOf("*"));
                //Toast.makeText(UserInterfaz.this,unidad, Toast.LENGTH_SHORT).show();
                ////////////////////////////////////////////////////////////////////////////////////////////

                Porcentaje = (int) (Aux*100/valor);
                if(Porcentaje >= 100){
                    Porcentaje = 100;
                }

                tv4.setText("Proyección");
                switch (unidad) {
                    case "kWh":
                        // int D = (int) (wattactual*100.0/valor);
                        Aux = wattactual;

                        tv1.setText(String.valueOf((int) valor));
                        tv6.setText("kWh");
                        break;
                    case "$":
                        //  int D = (int) (cost*100.0/valor);
                        Aux = cost;

                        tv1.setText(String.valueOf((int)valor));
                        tv6.setText("$");
                        break;
                }

                progressBar.setProgress(Porcentaje);
                progressBar.getProgressDrawable().setColorFilter(Color.argb(255,(int)(2.55*Porcentaje),(int)(255-2.55*Porcentaje) ,0), PorterDuff.Mode.SRC_IN);

                tv2.setText(ActualWatt + " kWh");
                tv3.setText("$ " + Costtext);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return PageOne;
    }
}