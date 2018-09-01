package com.piramidsoft.sirs;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.View;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RekammedisActivity extends AppCompatActivity {

    @BindView(R.id.btIndexRawatJalan)
    CardView btIndexRawatJalan;
    @BindView(R.id.btINdexRawatInap)
    CardView btINdexRawatInap;
    @BindView(R.id.btIGD)
    CardView btIGD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rekammedis);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.btIndexRawatJalan, R.id.btINdexRawatInap, R.id.btIGD})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btIndexRawatJalan:
                startActivity(new Intent(RekammedisActivity.this, RawatJalanActivity.class));
                break;
            case R.id.btINdexRawatInap:
                startActivity(new Intent(RekammedisActivity.this, RawatInapActivity.class));
                break;
            case R.id.btIGD:
                startActivity(new Intent(RekammedisActivity.this, IndexIGDActivity.class));
                break;
        }
    }
}
