package com.piramidsoft.sirs;

import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.widget.Button;
import android.widget.EditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SmsActivity extends AppCompatActivity {

    @BindView(R.id.etNomor)
    EditText etNomor;
    @BindView(R.id.etPesan)
    EditText etPesan;
    @BindView(R.id.btKirim)
    Button btKirim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sms);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.btKirim)
    public void onViewClicked() {
        Intent intent = getIntent();
        PendingIntent pi = PendingIntent.getActivity(getApplicationContext(), 0, intent, 0);
        SmsManager sms = SmsManager.getDefault();

        sms.sendTextMessage(etNomor.getText().toString(), null, etPesan.getText().toString(), pi, null);
    }
}
