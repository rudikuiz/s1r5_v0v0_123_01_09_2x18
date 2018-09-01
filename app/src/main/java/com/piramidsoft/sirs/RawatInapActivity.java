package com.piramidsoft.sirs;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RawatInapActivity extends AppCompatActivity {

    @BindView(R.id.lbNama)
    TextView lbNama;
    @BindView(R.id.lbAsalRujukan)
    TextView lbAsalRujukan;
    @BindView(R.id.lbSistemBayar)
    TextView lbSistemBayar;
    @BindView(R.id.lbTglPelayanan)
    TextView lbTglPelayanan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rawat_inap);
        ButterKnife.bind(this);
    }
}
