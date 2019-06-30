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

public class RecyclerViewAdapterConsumo extends RecyclerView.Adapter<RecyclerViewAdapterConsumo.ConsumoViewHolder> {

    private ArrayList<Consumos> mConsumoList;
    private Context context;

    private int selectedItem = -1;

    @NonNull
    @Override
    public ConsumoViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View consumoView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.consumo_item, viewGroup, false);

        ConsumoViewHolder cvh = new ConsumoViewHolder(consumoView);

        return cvh;
    }

    @Override
    public void onBindViewHolder(@NonNull ConsumoViewHolder viewHolder, int i) {
        Consumos currentItem = mConsumoList.get(i % mConsumoList.size());
        viewHolder.txtview.setText(currentItem.getmConsumo1());
    }

    public RecyclerViewAdapterConsumo(ArrayList<Consumos> mConsumoList, Context context){
        this.mConsumoList = mConsumoList;
        this.context = context;
    }

    @Override
    public int getItemCount() {
        return mConsumoList.size();
    }

    public class ConsumoViewHolder extends RecyclerView.ViewHolder {

        TextView txtview;

        public ConsumoViewHolder(View view) {
            super(view);

            txtview = (TextView) view.findViewById(R.id.consumo_label);
        }
    }
}