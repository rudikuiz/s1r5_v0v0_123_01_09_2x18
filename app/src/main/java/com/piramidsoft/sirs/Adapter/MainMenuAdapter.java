package com.piramidsoft.sirs.Adapter;


import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.piramidsoft.sirs.Model.MenuModel;
import com.piramidsoft.sirs.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainMenuAdapter extends RecyclerView.Adapter<MainMenuAdapter.MainMenuHolder> {


    private ArrayList<MenuModel> arrayList = new ArrayList<>();
    private Context context;

    public MainMenuAdapter(ArrayList<MenuModel> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }

    @Override
    public MainMenuHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_menu, parent, false);
        return new MainMenuHolder(view);
    }

    @Override
    public void onBindViewHolder(MainMenuHolder holder, final int position) {
        holder.Icon.setImageResource(arrayList.get(position).getImages());
        holder.Title.setText(arrayList.get(position).getTitle());
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (position == 0) {

                }

                if (position == 1) {

                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class MainMenuHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.Icon)
        ImageView Icon;
        @BindView(R.id.Title)
        TextView Title;
        @BindView(R.id.card_view)
        CardView cardView;

        public MainMenuHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
