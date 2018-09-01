package com.piramidsoft.sirs.Adapter;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.piramidsoft.sirs.Model.BillingModel;
import com.piramidsoft.sirs.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BillingAdapter extends RecyclerView.Adapter<BillingAdapter.BillingHolder> {


    private ArrayList<BillingModel> arrayList;
    private Context context;

    public BillingAdapter(ArrayList<BillingModel> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }

    @Override
    public BillingHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_billing, parent, false);
        return new BillingHolder(view);
    }

    @Override
    public void onBindViewHolder(BillingHolder holder, int position) {
        holder.lbNama.setText(arrayList.get(position).getNama());
        holder.lbTglPelayanan.setText(arrayList.get(position).getTanggal());
        holder.lbHari.setText(arrayList.get(position).getHari());
        holder.lbNoAntrian.setText(arrayList.get(position).getNomor_antrian());
        holder.lbbiaya.setText(arrayList.get(position).getBiaya_reg());
        holder.lbStatus.setText(arrayList.get(position).getStatus_bayar());
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class BillingHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.lbNama)
        TextView lbNama;
        @BindView(R.id.lbTglPelayanan)
        TextView lbTglPelayanan;
        @BindView(R.id.lbHari)
        TextView lbHari;
        @BindView(R.id.lbNoAntrian)
        TextView lbNoAntrian;
        @BindView(R.id.lbbiaya)
        TextView lbbiaya;
        @BindView(R.id.lbStatus)
        TextView lbStatus;

        public BillingHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
