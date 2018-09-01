package com.piramidsoft.sirs.Adapter;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.piramidsoft.sirs.Model.HistoryPasienModel;
import com.piramidsoft.sirs.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AdapterHistory extends RecyclerView.Adapter<AdapterHistory.HolderHolder> {

    private ArrayList<HistoryPasienModel> arrayList;
    private Context context;

    public AdapterHistory(ArrayList<HistoryPasienModel> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }

    @Override
    public HolderHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_history_pasien, parent, false);
        return new HolderHolder(view);
    }

    @Override
    public void onBindViewHolder(HolderHolder holder, int position) {
        int i = position +1;
        holder.number.setText(String.valueOf(i));
        holder.etTgl.setText(arrayList.get(position).getTgl());
        holder.etPoli.setText(arrayList.get(position).getPoli());
        holder.etBiaya.setText(arrayList.get(position).getBiaya());

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class HolderHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.number)
        TextView number;
        @BindView(R.id.etTgl)
        TextView etTgl;
        @BindView(R.id.etPoli)
        TextView etPoli;
        @BindView(R.id.etBiaya)
        TextView etBiaya;

        public HolderHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
