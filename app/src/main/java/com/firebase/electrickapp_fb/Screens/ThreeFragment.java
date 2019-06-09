package com.firebase.electrickapp_fb.Screens;

import android.os.Bundle;
import android.support.annotation.NonNull;
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

import java.util.ArrayList;

public class ThreeFragment  extends Fragment {

    RecyclerViewAdapterConsumo adapter;
    RecyclerView recyclerView;
    private ArrayList<Consumos> mConsumoList = new ArrayList<>();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View PageTree = inflater.inflate(R.layout.fragment_three, container, false);

        recyclerView = (RecyclerView) PageTree.findViewById(R.id.rv2);
        // add a divider after each item for more clarity

        final RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(mLayoutManager);

        adapter = new RecyclerViewAdapterConsumo(mConsumoList, getContext());
        recyclerView.setAdapter(adapter);

        final SnapHelper snapHelper = new LinearSnapHelper();
        snapHelper.attachToRecyclerView(recyclerView);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerview, int newState) {
                super.onScrollStateChanged(recyclerview, newState);

                if(newState == RecyclerView.SCROLL_STATE_IDLE) {
                    View centerView = snapHelper.findSnapView(mLayoutManager);
                    int offset = (recyclerView.getWidth() / recyclerView.getWidth() - 1) / 2;
                    int position = ((LinearLayoutManager) mLayoutManager).findFirstVisibleItemPosition() + offset;
                    Toast.makeText(getContext(),"posicion = " + position, Toast.LENGTH_SHORT).show();
                }
            }
        });

        populategroceryList();

        return PageTree;
    }

    private void populategroceryList() {
        Consumos empty = new Consumos("");
        Consumos enero = new Consumos("Enero");
        Consumos febrero = new Consumos("Febrero");
        Consumos marzo = new Consumos("Marzo");
        Consumos abril = new Consumos("Abril");
        mConsumoList.add(empty);
        mConsumoList.add(enero);
        mConsumoList.add(febrero);
        mConsumoList.add(marzo);
        mConsumoList.add(abril);
        mConsumoList.add(empty);
        adapter.notifyDataSetChanged();
    }
}