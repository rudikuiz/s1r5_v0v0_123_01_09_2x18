package com.piramidsoft.sirs;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.piramidsoft.sirs.Adapter.BillingAdapter;
import com.piramidsoft.sirs.Model.BillingModel;
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
import butterknife.Unbinder;

import static com.piramidsoft.sirs.Utils.AppConf.URL_BILLING_B_LUNAS;
import static com.piramidsoft.sirs.Utils.AppConf.URL_BILLING_LUNAS;

public class UnLunasFragment extends Fragment {

    @BindView(R.id.rec_daftar_tagihan)
    RecyclerView recDaftarTagihan;
    @BindView(R.id.Swipe)
    SwipeRefreshLayout Swipe;
    Unbinder unbinder;
    private BillingAdapter adapter;
    private ArrayList<BillingModel> arrayList = new ArrayList<>();
    RequestQueue requestQueue;
    StringRequest stringRequest;
    OwnProgressDialog loading;
    FragmentActivity mActivity;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.un_lunas_fragment, container, false);
        unbinder = ButterKnife.bind(this, view);
        mActivity = getActivity();

        recDaftarTagihan.setHasFixedSize(true);
        GridLayoutManager layoutManager = new GridLayoutManager(mActivity, 1,
                GridLayoutManager.VERTICAL, false);
        recDaftarTagihan.setLayoutManager(layoutManager);

        loading = new OwnProgressDialog(mActivity);
        requestQueue = Volley.newRequestQueue(mActivity);

        Swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                arrayList.clear();
                getData();
            }
        });

        arrayList.clear();
        getData();
        return view;
    }

    private void getData() {
        stringRequest = new StringRequest(Request.Method.POST, URL_BILLING_B_LUNAS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    for (int a = 0; a < jsonArray.length(); a++) {
                        JSONObject json = jsonArray.getJSONObject(a);
                        BillingModel modelMenu = new BillingModel();
                        modelMenu.setNama(json.getString("NAMA"));
                        modelMenu.setTanggal(json.getString("TANGGAL"));
                        modelMenu.setHari(json.getString("HARI"));
                        modelMenu.setNomor_antrian(json.getString("NOMOR_ANTRIAN"));
                        modelMenu.setBiaya_reg(json.getString("BIAYA_REG"));

                        modelMenu.setStatus_bayar(json.getString("STS_BAYAR"));
                        if (modelMenu.getStatus_bayar().equals("0")){
                            modelMenu.setStatus_bayar("Belum Di Proses");
                        }

                        arrayList.add(modelMenu);
                    }
                    adapter = new BillingAdapter(arrayList, mActivity);
                    recDaftarTagihan.setAdapter(adapter);
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
                SessionManager sessionManager = new SessionManager(mActivity);
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
