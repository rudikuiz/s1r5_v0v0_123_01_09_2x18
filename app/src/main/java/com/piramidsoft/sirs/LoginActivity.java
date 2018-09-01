package com.piramidsoft.sirs;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.piramidsoft.sirs.Utils.AndLog;
import com.piramidsoft.sirs.Utils.AppConf;
import com.piramidsoft.sirs.Utils.GlobalToast;
import com.piramidsoft.sirs.Utils.OwnProgressDialog;
import com.piramidsoft.sirs.Utils.SessionManager;
import com.piramidsoft.sirs.Utils.VolleyHttp;
import com.scottyab.aescrypt.AESCrypt;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.GeneralSecurityException;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.piramidsoft.sirs.Utils.SessionManager.APIKEY;
import static com.piramidsoft.sirs.Utils.SessionManager.ID_PASIEN;
import static com.piramidsoft.sirs.Utils.SessionManager.NAMA;
import static com.piramidsoft.sirs.Utils.SessionManager.REFRESH_CODE;
import static com.piramidsoft.sirs.Utils.SessionManager.TOKEN;

public class LoginActivity extends AppCompatActivity {

    @BindView(R.id.logo)
    ImageView logo;
    @BindView(R.id.username)
    EditText username;
    @BindView(R.id.password)
    EditText password;
    @BindView(R.id.login)
    Button login;
    @BindView(R.id.register)
    TextView register;
    ConnectivityManager conMgr;
    OwnProgressDialog loading;
    String result;
    private static final String TAG_SUCCESS = "result";
    @BindView(R.id.forgot)
    TextView forgot;
    private SessionManager sessionManager;
    RequestQueue requestQueue;
    private final int PROS_ID = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            Window w = getWindow();
//            w.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
//            w.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
//        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        cekInternet();
        requestQueue = Volley.newRequestQueue(LoginActivity.this);
        sessionManager = new SessionManager(LoginActivity.this);
        loading = new OwnProgressDialog(LoginActivity.this);

        if (sessionManager.isLoggedIn()) {
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            finish();
            startActivity(intent);
        }

        if (ActivityCompat.checkSelfPermission(LoginActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(LoginActivity.this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(LoginActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(LoginActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(LoginActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(LoginActivity.this, Manifest.permission.RECEIVE_SMS) != PackageManager.PERMISSION_GRANTED
                ){
            ActivityCompat.requestPermissions(LoginActivity.this,
                    new String[]{
                            Manifest.permission.CALL_PHONE,
                            Manifest.permission.SEND_SMS,
                            Manifest.permission.RECEIVE_SMS,
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.CAMERA
                    },
                    PROS_ID);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case PROS_ID: {

                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                } else {
                    finish();
                    GlobalToast.ShowToast(LoginActivity.this, "Permission denied to read your External storage");

                }
                return;
            }
        }
    }

    private void IsLoginSubmit() {
        loading.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConf.URL_LOGIN, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                AndLog.ShowLog("login", response);
                loading.dismiss();
                try {
                    JSONObject object = new JSONObject(response);
                    result = object.getString(TAG_SUCCESS);
                    if (result.equals("true")) {
                        String idpasien = object.getString(ID_PASIEN);
                        String nama = object.getString(NAMA);
                        String token = object.getString(TOKEN);
                        String apikey = object.getString(APIKEY);
                        String refreshcode = object.getString(REFRESH_CODE);

                        Toast.makeText(getApplicationContext(), object.getString("message"), Toast.LENGTH_LONG).show();

                        try {
                            sessionManager.createLoginSession(
                                    AESCrypt.encrypt("id", idpasien),
                                    AESCrypt.encrypt("nam", nama),
                                    AESCrypt.encrypt("tok", token),
                                    AESCrypt.encrypt("api", apikey),
                                    AESCrypt.encrypt("ref", refreshcode)
                            );
                        } catch (GeneralSecurityException e) {
                            e.printStackTrace();
                        }

                        startActivity(new Intent(LoginActivity.this, MainActivity.class));

                    } else {
                        Toast.makeText(getApplicationContext(),
                                object.getString("message"), Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    loading.dismiss();
                    GlobalToast.ShowToast(LoginActivity.this, getString(R.string.gagal_menyambungkan));
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                GlobalToast.ShowToast(LoginActivity.this, getString(R.string.disconnected));
                loading.dismiss();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<String, String>();
                params.put("username", username.getText().toString().trim());
                params.put("password", password.getText().toString().trim());

                return params;
            }
        };
        stringRequest.setTag(AppConf.httpTag);
        VolleyHttp.getInstance(LoginActivity.this).addToRequestQueue(stringRequest);
    }

    private void cekInternet() {
        conMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        {
            if (conMgr.getActiveNetworkInfo() != null
                    && conMgr.getActiveNetworkInfo().isAvailable()
                    && conMgr.getActiveNetworkInfo().isConnected()) {
            } else {
                Toast.makeText(getApplicationContext(), "No Internet Connection",
                        Toast.LENGTH_LONG).show();
            }
        }
    }

    @OnClick({R.id.login, R.id.register, R.id.forgot})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.login:
                if (!username.getText().toString().isEmpty() && !password.getText().toString().isEmpty()) {
                    IsLoginSubmit();
                } else {
                    GlobalToast.ShowToast(LoginActivity.this, "Kolom tidak boleh kosong");
                }

                break;
            case R.id.register:
                startActivity(new Intent(LoginActivity.this, PendaftaranActivity.class));
                break;
            case R.id.forgot:
//                startActivity(new Intent(LoginActivity.this, SmsActivity.class));
                break;
        }
    }
}
