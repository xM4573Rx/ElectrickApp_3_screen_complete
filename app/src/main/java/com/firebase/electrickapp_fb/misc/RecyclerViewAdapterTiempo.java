package com.firebase.electrickapp_fb.misc;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.electrickapp_fb.R;

import java.util.ArrayList;

public class RecyclerViewAdapterTiempo extends RecyclerView.Adapter<RecyclerViewAdapterTiempo.TiempoViewHolder> {

    private ArrayList<Tiempos> mTiempoList;
    private Context context;

    private int selectedItem = -1;

    @NonNull
    @Override
    public TiempoViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View tiempoView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.tiempo_item, viewGroup, false);

        TiempoViewHolder cvh = new TiempoViewHolder(tiempoView);

        return cvh;
    }

    @Override
    public void onBindViewHolder(@NonNull TiempoViewHolder viewHolder, int i) {
        Tiempos currentItem = mTiempoList.get(i % mTiempoList.size());
        viewHolder.txtview.setText(currentItem.getmTiempo1());
    }

    public RecyclerViewAdapterTiempo(ArrayList<Tiempos> mTiempoList, Context context){
        this.mTiempoList = mTiempoList;
        this.context = context;
    }

    @Override
    public int getItemCount() {
        return mTiempoList.size();
    }

    public class TiempoViewHolder extends RecyclerView.ViewHolder {

        TextView txtview;

        public TiempoViewHolder(View view) {
            super(view);

            txtview = (TextView) view.findViewById(R.id.tiempo_label);
        }
    }
}