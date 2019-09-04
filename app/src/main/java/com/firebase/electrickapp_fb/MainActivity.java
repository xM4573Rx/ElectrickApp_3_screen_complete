package com.firebase.electrickapp_fb;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.firebase.electrickapp_fb.Screens.OneFragment;
import com.firebase.electrickapp_fb.Screens.ThreeFragment;
import com.firebase.electrickapp_fb.Screens.TwoFragment;
import com.firebase.electrickapp_fb.settings.Ajustes;
import com.firebase.electrickapp_fb.settings.Code;
import com.firebase.electrickapp_fb.transfer.History;
import com.firebase.electrickapp_fb.transfer.ProyData;
import com.firebase.electrickapp_fb.transfer.Selector;
import com.firebase.electrickapp_fb.transfer.Time;
import com.firebase.electrickapp_fb.transfer.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final String DATO_KEY_TEL = "TELEFONO";
    public static final String DATO_KEY_NAME = "NOMBRE";
    public static final String DATO_KEY_SELE = "SELECTOR";
    public static final String DATO_KEY_TIME = "TIME";

    public String telefono;
    public String nombre;
    public String selector;
    public String time;

    public String proyeccion;
    public String unidad;

    public double valor;

    TabLayout MyTabs;
    ViewPager MyPage;

    DatabaseReference mDataBaseReference = FirebaseDatabase.getInstance().getReference();
    DatabaseReference mUid = mDataBaseReference.child("User");
    DatabaseReference mSelectorChild = mDataBaseReference.child("User");
    DatabaseReference mTimeChild = mDataBaseReference.child("User");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        MyTabs = (TabLayout)findViewById(R.id.MyTabs);
        MyPage = (ViewPager)findViewById(R.id.MyPage);

        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("ElectrickApp");

        String Archivos[] = fileList();

        if(ArchivoExiste(Archivos,"Datos.txt")){
            //Toast.makeText(MainActivity.this,"SI", Toast.LENGTH_SHORT).show();
        }else {
            //Toast.makeText(MainActivity.this,"NO", Toast.LENGTH_SHORT).show();
            try {
                OutputStreamWriter archivo = new OutputStreamWriter(openFileOutput("Datos.txt", Activity.MODE_PRIVATE));

                archivo.write("0");
                archivo.write("#");
                archivo.write("$");
                archivo.write("*");
                archivo.flush();
                archivo.close();

            } catch (IOException e) {

            }
            //Toast.makeText(this, "Archivo creado", Toast.LENGTH_SHORT).show();
        }

        if(ArchivoExiste(Archivos,"User.txt")){
            //Toast.makeText(MainActivity.this,"SI", Toast.LENGTH_SHORT).show();
        }else {
            //Toast.makeText(MainActivity.this,"NO", Toast.LENGTH_SHORT).show();
            try {
                OutputStreamWriter archivo = new OutputStreamWriter(openFileOutput("User.txt", Activity.MODE_PRIVATE));

                archivo.write("0");
                archivo.write("%");
                archivo.write("E");
                archivo.write("&");
                archivo.flush();
                archivo.close();

            } catch (IOException e) {

            }
            //Toast.makeText(this, "Archivo creado", Toast.LENGTH_SHORT).show();
        }

        if(ArchivoExiste(Archivos,"Selector.txt")){
            //Toast.makeText(MainActivity.this,"SI", Toast.LENGTH_SHORT).show();
        }else {
            //Toast.makeText(MainActivity.this,"NO", Toast.LENGTH_SHORT).show();
            try {
                OutputStreamWriter archivo = new OutputStreamWriter(openFileOutput("Selector.txt", Activity.MODE_PRIVATE));

                archivo.write("0");
                archivo.flush();
                archivo.close();

            } catch (IOException e) {

            }
            //Toast.makeText(this, "Archivo creado", Toast.LENGTH_SHORT).show();
        }

        if(ArchivoExiste(Archivos,"Time.txt")){
            //Toast.makeText(MainActivity.this,"SI", Toast.LENGTH_SHORT).show();
        }else {
            //Toast.makeText(MainActivity.this,"NO", Toast.LENGTH_SHORT).show();
            try {
                OutputStreamWriter archivo = new OutputStreamWriter(openFileOutput("Time.txt", Activity.MODE_PRIVATE));

                archivo.write("0");
                archivo.flush();
                archivo.close();

            } catch (IOException e) {

            }
            //Toast.makeText(this, "Archivo creado", Toast.LENGTH_SHORT).show();
        }

        String archivos [] = fileList();

        if (ArchivoExiste(archivos, "Datos.txt") && ArchivoExiste(archivos, "User.txt") && ArchivoExiste(archivos, "Selector.txt") && ArchivoExiste(archivos, "Time.txt")) {
            try {
                InputStreamReader archivo1 = new InputStreamReader(openFileInput("Datos.txt"));
                InputStreamReader archivo2 = new InputStreamReader(openFileInput("User.txt"));
                InputStreamReader archivo3 = new InputStreamReader(openFileInput("Selector.txt"));
                InputStreamReader archivo4 = new InputStreamReader(openFileInput("Time.txt"));
                BufferedReader br1 = new BufferedReader(archivo1);
                BufferedReader br2 = new BufferedReader(archivo2);
                BufferedReader br3 = new BufferedReader(archivo3);
                BufferedReader br4 = new BufferedReader(archivo4);
                String linea1 = br1.readLine();
                String linea2 = br2.readLine();
                String linea3 = br3.readLine();
                String linea4 = br4.readLine();
                String AllData1 = "";
                String AllData2 = "";
                String AllData3 = "";
                String AllData4 = "";

                while(linea1 != null) {
                    AllData1 = AllData1 + linea1;// + "\r\n";
                    linea1 = br1.readLine();
                }

                while(linea2 != null) {
                    AllData2 = AllData2 + linea2;// + "\r\n";
                    linea2 = br2.readLine();
                }

                while(linea3 != null) {
                    AllData3 = AllData3 + linea3;// + "\r\n";
                    linea3 = br3.readLine();
                }

                while(linea4 != null) {
                    AllData4 = AllData4 + linea4;// + "\r\n";
                    linea4 = br4.readLine();
                }

                br1.close();
                br2.close();
                br3.close();
                br4.close();
                archivo1.close();
                archivo2.close();
                archivo3.close();
                archivo4.close();

                telefono = AllData2.substring(0, AllData2.indexOf("%"));
                nombre = AllData2.substring(AllData2.indexOf("%") + 1, AllData2.indexOf("&"));
                selector = AllData3;
                time = AllData4;

                SharedPreferences preferencias = getSharedPreferences("dato", Context.MODE_PRIVATE);
                SharedPreferences.Editor Obj_editor = preferencias.edit();
                Obj_editor.putString("DATO", telefono);
                Obj_editor.commit();

                SharedPreferences preferencias1 = getSharedPreferences("dato1", Context.MODE_PRIVATE);
                SharedPreferences.Editor Obj_editor1 = preferencias1.edit();
                Obj_editor1.putString("DATO1", nombre);
                Obj_editor1.apply();

                SharedPreferences preferencias2 = getSharedPreferences("dato2", Context.MODE_PRIVATE);
                SharedPreferences.Editor Obj_editor2 = preferencias2.edit();
                Obj_editor2.putString("DATO2", AllData1);
                Obj_editor2.apply();

                SharedPreferences preferencias3 = getSharedPreferences("dato3", Context.MODE_PRIVATE);
                SharedPreferences.Editor Obj_editor3 = preferencias3.edit();
                Obj_editor3.putString("DATO3", AllData3);
                Obj_editor3.apply();

                SharedPreferences preferencias4 = getSharedPreferences("dato4", Context.MODE_PRIVATE);
                SharedPreferences.Editor Obj_editor4 = preferencias4.edit();
                Obj_editor4.putString("DATO4", AllData4);
                Obj_editor4.apply();

                //Toast.makeText(MainActivity.this,"MODULO " + telefono, Toast.LENGTH_SHORT).show();
            } catch (IOException e) {

            }
        }

        SharedPreferences preferences = getSharedPreferences("dato", Context.MODE_PRIVATE);
        String number = preferences.getString("DATO", "0");

        SharedPreferences preferences1 = getSharedPreferences("dato1", Context.MODE_PRIVATE);
        String name = preferences1.getString("DATO1", "0");

        SharedPreferences preferences2 = getSharedPreferences("dato2", Context.MODE_PRIVATE);
        String All = preferences2.getString("DATO2", "0");

        SharedPreferences preferences3 = getSharedPreferences("dato3", Context.MODE_PRIVATE);
        String selec = preferences3.getString("DATO3", "0");

        SharedPreferences preferences4 = getSharedPreferences("dato4", Context.MODE_PRIVATE);
        String tim = preferences4.getString("DATO4", "0");

        User u = new User();
        u.setUid(number);
        u.setNombre(name);
        u.setProyeccion(All);
        u.setWatt("0");
        u.setWattHora("0");
        mDataBaseReference.child("User").child(u.getUid()).setValue(u);

        History h = new History();
        h.setHistorico("1#2$3%4&");
        mUid.child(u.getUid()).child("Historico").child("Dias").setValue(h);

        History hi = new History();
        hi.setHistorico("5#6$7%8&");
        mUid.child(u.getUid()).child("Historico").child("Semanas").setValue(hi);

        History his = new History();
        his.setHistorico("9#10$11%12&");
        mUid.child(u.getUid()).child("Historico").child("Quincenas").setValue(his);

        History hist = new History();
        hist.setHistorico("13#14$15%16&");
        mUid.child(u.getUid()).child("Historico").child("Meses").setValue(hist);

        Time t = new Time();
        t.setTiempo(tim);
        mUid.child(u.getUid()).child("Historico").child("Graph").child("Tiempo").setValue(t);

        Selector s = new Selector();
        s.setModo(selec);
        mUid.child(u.getUid()).child("Historico").child("Graph").child("Selector").setValue(s);

        SetUpViewPager(MyPage);
        MyTabs.setupWithViewPager(MyPage);

        MyTabs.getTabAt(0).setIcon(R.drawable.selector_one);
        MyTabs.getTabAt(1).setIcon(R.drawable.selector_two);
        MyTabs.getTabAt(2).setIcon(R.drawable.selector_three);
    }

    private boolean ArchivoExiste(String archivos [], String FileName) {
        for(int i = 0; i < archivos.length; i++)
            if(FileName.equals(archivos[i]))
                return true;
        return false;
    }

    public class MyViewPageAdapter extends FragmentPagerAdapter {
        private List<Fragment> MyFragment = new ArrayList<>();
        public MyViewPageAdapter(FragmentManager manager){
            super(manager);
        }

        public void AddFragmentPage(Fragment Frag){
            MyFragment.add(Frag);
        }

        @Override
        public Fragment getItem(int position) {
            return MyFragment.get(position);
        }

        @Override
        public int getCount() {
            return MyFragment.size();
        }
    }

    public void SetUpViewPager (ViewPager viewpage){
        MyViewPageAdapter Adapter = new MyViewPageAdapter(getSupportFragmentManager());

        SharedPreferences preferences = getSharedPreferences("dato", Context.MODE_PRIVATE);
        String number = preferences.getString("DATO", "0");

        SharedPreferences preferences1 = getSharedPreferences("dato1", Context.MODE_PRIVATE);
        String name = preferences1.getString("DATO1", "0");

        SharedPreferences preferences3 = getSharedPreferences("dato3", Context.MODE_PRIVATE);
        String selector = preferences3.getString("DATO3", "0");

        SharedPreferences preferences4 = getSharedPreferences("dato4", Context.MODE_PRIVATE);
        String time = preferences4.getString("DATO4", "0");

        Adapter.AddFragmentPage(newInstance1(number, name)); /*new OneFragment());*/
        Adapter.AddFragmentPage(newInstance2(number, selector, time));
        Adapter.AddFragmentPage(new ThreeFragment());
        //We Need Fragment class now
        viewpage.setAdapter(Adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item1:
                Intent intent = new Intent(MainActivity.this, Ajustes.class);
                startActivity(intent);
                return false;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        SharedPreferences preferences = getSharedPreferences("dato", Context.MODE_PRIVATE);
        String number = preferences.getString("DATO", "0");

        mSelectorChild.child(number).child("Historico").child("Graph").child("Selector").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String se = dataSnapshot.child("modo").getValue().toString();

                try {
                    OutputStreamWriter archivo = new OutputStreamWriter(openFileOutput("Selector.txt", Activity.MODE_PRIVATE));

                    archivo.write(se);
                    archivo.flush();
                    archivo.close();
                } catch (IOException e) {

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        mTimeChild.child(number).child("Historico").child("Graph").child("Tiempo").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String ti = dataSnapshot.child("tiempo").getValue().toString();

                try {
                    OutputStreamWriter archivo = new OutputStreamWriter(openFileOutput("Time.txt", Activity.MODE_PRIVATE));

                    archivo.write(ti);
                    archivo.flush();
                    archivo.close();
                } catch (IOException e) {

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private OneFragment newInstance1(String transfer, String transfer1) {

        Bundle bundle = new Bundle();
        bundle.putString(DATO_KEY_TEL, transfer);
        bundle.putString(DATO_KEY_NAME, transfer1);
        OneFragment fragment = new OneFragment();
        fragment.setArguments(bundle);

        return fragment;
    }

    private TwoFragment newInstance2(String transfer, String transfer1, String transfer2) {

        Bundle bundle = new Bundle();
        bundle.putString(DATO_KEY_TEL, transfer);
        bundle.putString(DATO_KEY_SELE, transfer1);
        bundle.putString(DATO_KEY_TIME, transfer2);
        TwoFragment fragment = new TwoFragment();
        fragment.setArguments(bundle);

        return fragment;
    }
}
