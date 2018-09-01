package com.piramidsoft.sirs;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailTagihanActivity extends AppCompatActivity {

    @BindView(R.id.lbNama)
    TextView lbNama;
    @BindView(R.id.lbTglDaftar)
    TextView lbTglDaftar;
    @BindView(R.id.lbSistemBayar)
    TextView lbSistemBayar;
    @BindView(R.id.lbPelayanan)
    TextView lbPelayanan;
    @BindView(R.id.lbStatus)
    TextView lbStatus;
    @BindView(R.id.lblayanan)
    TextView lblayanan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_tagihan);
        ButterKnife.bind(this);
    }
}
