package com.piramidsoft.sirs;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HistoryPasienActivity extends AppCompatActivity {

    @BindView(R.id.rec_history_pasien)
    RecyclerView recHistoryPasien;
    @BindView(R.id.Swipe)
    SwipeRefreshLayout Swipe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_pasien);
        ButterKnife.bind(this);
    }
}
