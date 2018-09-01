package com.piramidsoft.sirs;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.piramidsoft.sirs.Adapter.AdapterHistory;
import com.piramidsoft.sirs.Adapter.BillingAdapter;
import com.piramidsoft.sirs.Model.BillingModel;
import com.piramidsoft.sirs.Model.HistoryPasienModel;
import com.piramidsoft.sirs.Utils.OwnProgressDialog;
import com.piramidsoft.sirs.Utils.SessionManager;
import com.scottyab.aescrypt.AESCrypt;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.piramidsoft.sirs.Utils.AppConf.URL_BILLING_LUNAS;
import static com.piramidsoft.sirs.Utils.AppConf.URL_HISTORY_CHECKUP;

public class HistoryPasienActivity extends AppCompatActivity {

    @BindView(R.id.rec_history_pasien)
    RecyclerView recHistoryPasien;
    @BindView(R.id.Swipe)
    SwipeRefreshLayout Swipe;

    private AdapterHistory adapter;
    private ArrayList<HistoryPasienModel> arrayList = new ArrayList<>();
    RequestQueue requestQueue;
    StringRequest stringRequest;
    OwnProgressDialog loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_pasien);
        ButterKnife.bind(this);

        recHistoryPasien.setHasFixedSize(true);
        GridLayoutManager layoutManager = new GridLayoutManager(HistoryPasienActivity.this, 1,
                GridLayoutManager.VERTICAL, false);
        recHistoryPasien.setLayoutManager(layoutManager);

        loading = new OwnProgressDialog(HistoryPasienActivity.this);
        requestQueue = Volley.newRequestQueue(HistoryPasienActivity.this);

        Swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                arrayList.clear();
                getData();
            }
        });

        arrayList.clear();
        getData();
    }

    private void getData() {
        stringRequest = new StringRequest(Request.Method.POST, URL_HISTORY_CHECKUP, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    for (int a = 0; a < jsonArray.length(); a++) {
                        JSONObject json = jsonArray.getJSONObject(a);
                        HistoryPasienModel modelMenu = new HistoryPasienModel();
                        modelMenu.setTgl(json.getString("TGL"));
                        modelMenu.setPoli(json.getString("TIPE"));
                        modelMenu.setBiaya(json.getString("BIAYA"));

                        arrayList.add(modelMenu);
                    }
                    adapter = new AdapterHistory(arrayList, HistoryPasienActivity.this);
                    recHistoryPasien.setAdapter(adapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                loading.dismiss();
                if (Swipe != null) {
                    Swipe.setRefreshing(false);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                SessionManager sessionManager = new SessionManager(HistoryPasienActivity.this);
                Map<String, String> params = new HashMap<String, String>();
                try {
                    params.put("id_pasien", AESCrypt.decrypt("id", sessionManager.getIdPasien()));
                    Log.d("params", String.valueOf(params));

                } catch (GeneralSecurityException e) {
                    e.printStackTrace();
                }

                return params;
            }

        };

        requestQueue.add(stringRequest);
    }
}
