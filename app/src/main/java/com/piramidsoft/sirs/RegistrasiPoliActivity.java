package com.piramidsoft.sirs;

import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.piramidsoft.sirs.Utils.AndLog;
import com.piramidsoft.sirs.Utils.AppConf;
import com.piramidsoft.sirs.Utils.DecimalsFormat;
import com.piramidsoft.sirs.Utils.GlobalToast;
import com.piramidsoft.sirs.Utils.OwnProgressDialog;
import com.piramidsoft.sirs.Utils.SessionManager;
import com.scottyab.aescrypt.AESCrypt;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RegistrasiPoliActivity extends AppCompatActivity {
    @BindView(R.id.etPoli)
    Spinner etPoli;
    @BindView(R.id.etNamaDokter)
    EditText etNamaDokter;
    @BindView(R.id.etHarga)
    EditText etHarga;
    @BindView(R.id.btSubmit)
    Button btSubmit;
    @BindView(R.id.etBiaya)
    EditText etBiaya;
    @BindView(R.id.txAntrian)
    TextView txAntrian;
    @BindView(R.id.crd_Registrasi)
    CardView crdRegistrasi;
    @BindView(R.id.crd_Antrian)
    CardView crdAntrian;
    @BindView(R.id.QRCode)
    ImageView QRCode;
    private ArrayList<String> ListPoli = new ArrayList<>();
    private ArrayList<String> ItemIdDokter = new ArrayList<>();
    private ArrayList<String> ItemIdPoli = new ArrayList<>();
    private ArrayList<String> ItemBiaya = new ArrayList<>();
    private StringRequest stringRequest;
    private RequestQueue requestQueue;
    private final int MY_SOCKET_TIMEOUT_MS = 10 * 1000;
    String id_dokter, result, biaya, id_poli, itembiaya, antrian, barcodes;
    int barcode;
    Boolean isLoading;
    OwnProgressDialog loading;
    private static final String TAG_SUCCESS = "result";
    public final static int QRcodeWidth = 500 ;
    private static final String IMAGE_DIRECTORY = "/QRcodeRumahSakit";
    Bitmap bitmap ;
    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrasi_poli);
        ButterKnife.bind(this);
        requestQueue = Volley.newRequestQueue(RegistrasiPoliActivity.this);
        sessionManager = new SessionManager(RegistrasiPoliActivity.this);
        isLoading = true;
        loading = new OwnProgressDialog(RegistrasiPoliActivity.this);
        getAntrian();
        getDataPoli();
        etPoli.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                id_dokter = ItemIdDokter.get(position);
                id_poli = ItemIdPoli.get(position);
                itembiaya = ItemBiaya.get(position);
                etHarga.setText("Rp. " + DecimalsFormat.priceWithoutDecimal(itembiaya));
                getNamaDokter();
                getHarga();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }

    private void generateQR(){
        if(barcodes.length() == 0){
            Toast.makeText(RegistrasiPoliActivity.this, "Enter String!", Toast.LENGTH_SHORT).show();
        }else {
            try {
                bitmap = TextToImageEncode(barcodes);
                QRCode.setImageBitmap(bitmap);
                crdAntrian.setVisibility(View.VISIBLE);
                crdRegistrasi.setVisibility(View.GONE);
                String path = saveImage(bitmap);  //give read write permission
                Toast.makeText(RegistrasiPoliActivity.this, "QRCode saved to -> "+path, Toast.LENGTH_SHORT).show();
            } catch (WriterException e) {
                e.printStackTrace();
            }

        }
    }

    public String saveImage(Bitmap myBitmap) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        myBitmap.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
        File wallpaperDirectory = new File(
                Environment.getExternalStorageDirectory() + IMAGE_DIRECTORY);
        // have the object build the directory structure, if needed.

        if (!wallpaperDirectory.exists()) {
            Log.d("dirrrrrr", "" + wallpaperDirectory.mkdirs());
            wallpaperDirectory.mkdirs();
        }

        try {
            File f = new File(wallpaperDirectory, Calendar.getInstance()
                    .getTimeInMillis() + ".jpg");
            f.createNewFile();   //give read write permission
            FileOutputStream fo = new FileOutputStream(f);
            fo.write(bytes.toByteArray());
            MediaScannerConnection.scanFile(this,
                    new String[]{f.getPath()},
                    new String[]{"image/jpeg"}, null);
            fo.close();
            Log.d("TAG", "File Saved::--->" + f.getAbsolutePath());

            return f.getAbsolutePath();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return "";

    }

    private Bitmap TextToImageEncode(String Value) throws WriterException {
        BitMatrix bitMatrix;
        try {
            bitMatrix = new MultiFormatWriter().encode(
                    Value,
                    BarcodeFormat.DATA_MATRIX.QR_CODE,
                    QRcodeWidth, QRcodeWidth, null
            );

        } catch (IllegalArgumentException Illegalargumentexception) {

            return null;
        }
        int bitMatrixWidth = bitMatrix.getWidth();

        int bitMatrixHeight = bitMatrix.getHeight();

        int[] pixels = new int[bitMatrixWidth * bitMatrixHeight];

        for (int y = 0; y < bitMatrixHeight; y++) {
            int offset = y * bitMatrixWidth;

            for (int x = 0; x < bitMatrixWidth; x++) {

                pixels[offset + x] = bitMatrix.get(x, y) ?
                        getResources().getColor(R.color.black):getResources().getColor(R.color.white);
            }
        }
        Bitmap bitmap = Bitmap.createBitmap(bitMatrixWidth, bitMatrixHeight, Bitmap.Config.ARGB_4444);

        bitmap.setPixels(pixels, 0, 500, 0, 0, bitMatrixWidth, bitMatrixHeight);
        return bitmap;
    }

    private void getDataPoli() {
        stringRequest = new StringRequest(Request.Method.GET, AppConf.URL_POLI, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject object = new JSONObject(response);
                    JSONArray jsonArray = object.getJSONArray("data");
                    for (int a = 0; a < jsonArray.length(); a++) {
                        JSONObject json = jsonArray.getJSONObject(a);
                        ListPoli.add(json.optString("NAMA"));
                        ItemIdPoli.add(json.optString("ID"));
                        ItemIdDokter.add(json.optString("ID_PEG_DOKTER"));
                        ItemBiaya.add(json.optString("BIAYA"));
                    }
                    isLoading = false;
                    loading.dismiss();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                etPoli.setAdapter(new ArrayAdapter<String>(RegistrasiPoliActivity.this,
                        android.R.layout.simple_spinner_item,
                        ListPoli));

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                if (error instanceof TimeoutError) {
                    Toast.makeText(RegistrasiPoliActivity.this, "timeout", Toast.LENGTH_SHORT).show();
                    isLoading = false;
                    loading.dismiss();
                } else if (error instanceof NoConnectionError) {
                    Toast.makeText(RegistrasiPoliActivity.this, "no connection", Toast.LENGTH_SHORT).show();
                    isLoading = false;
                    loading.dismiss();
                }
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);


    }

    private void getNamaDokter() {

        stringRequest = new StringRequest(Request.Method.POST, AppConf.URL_DATA_BY_ID, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    for (int a = 0; a < jsonArray.length(); a++) {
                        JSONObject json = jsonArray.getJSONObject(a);

                        String nama_dok = json.getString("NAMA");
                        etNamaDokter.setText(nama_dok);

                    }
                    if (jsonArray.length() == 0) {
                        etNamaDokter.setText("Dokter belum ada");
                    }

                    isLoading = false;
                    loading.dismiss();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                isLoading = false;
                loading.dismiss();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put("ID", id_dokter);

                return params;
            }

        };

        requestQueue.add(stringRequest);
    }

    private void getHarga() {
        stringRequest = new StringRequest(Request.Method.POST, AppConf.URL_GETBIAYA, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                AndLog.ShowLog("rrrrr]", response);
                try {
                    JSONObject object = new JSONObject(response);
                    JSONArray jsonArray = object.getJSONArray("data");
                    for (int a = 0; a < jsonArray.length(); a++) {
                        JSONObject json = jsonArray.getJSONObject(a);

                        biaya = json.getString("BIAYA");
                        etBiaya.setText("Rp. " + DecimalsFormat.priceWithoutDecimal(biaya) + ",-");

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                SessionManager sessionManager = new SessionManager(RegistrasiPoliActivity.this);

                Map<String, String> params = new HashMap<String, String>();

                try {
                    params.put("id", AESCrypt.decrypt("id", sessionManager.getIdPasien()));
                    Log.d("asd", params.toString());
                } catch (GeneralSecurityException e) {
                    e.printStackTrace();
                }

                return params;
            }

        };

        requestQueue.add(stringRequest);
    }

    private void getAntrian() {
        stringRequest = new StringRequest(Request.Method.POST, AppConf.URL_ANTRIAN, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject object = new JSONObject(response);
                    JSONArray jsonArray = object.getJSONArray("data");
                    for (int a = 0; a < jsonArray.length(); a++) {
                        JSONObject json = jsonArray.getJSONObject(a);

                        barcodes = json.getString("BARCODE");
                        antrian = json.getString("NOMOR_ANTRIAN");
                        txAntrian.setText(antrian);

//                                               sessionManager.setKeyBarcode(AESCrypt.encrypt("bar", barcodes));
//                        sessionManager.setKeyAntrian(AESCrypt.encrypt("ant", antrian));

                    }
                    if (jsonArray.length() > 0) {

                        generateQR();
                    } else {
                        crdAntrian.setVisibility(View.GONE);
                        crdRegistrasi.setVisibility(View.VISIBLE);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                SessionManager sessionManager = new SessionManager(RegistrasiPoliActivity.this);

                Map<String, String> params = new HashMap<String, String>();

                try {
                    params.put("id", AESCrypt.decrypt("id", sessionManager.getIdPasien()));
                    Log.d("asd", params.toString());
                } catch (GeneralSecurityException e) {
                    e.printStackTrace();
                }

                return params;
            }

        };

        requestQueue.add(stringRequest);
    }

    private void SubmitInsertUserAdmum() {
        loading.show();
        StringRequest strReq = new StringRequest(Request.Method.POST, AppConf.URL_TAMBAH_USER_ADMUM, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jObj = new JSONObject(response);
                    result = jObj.getString(TAG_SUCCESS);
                    if (result.equals("true")) {
                        GlobalToast.ShowToast(RegistrasiPoliActivity.this, "Data Berhasil dikirim ke Database");
                        finish();
                    } else {
                        GlobalToast.ShowToast(RegistrasiPoliActivity.this, "Failed to insert database");
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                GlobalToast.ShowToast(RegistrasiPoliActivity.this, "Gagal menyimpan, coba ulangi lagi");
                isLoading = false;
                loading.dismiss();

            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                SessionManager sessionManager = new SessionManager(RegistrasiPoliActivity.this);

                Map<String, String> params = new HashMap<String, String>();
                try {
                    params.put("id_pasien", AESCrypt.decrypt("id", sessionManager.getIdPasien()));
                    params.put("id_poli", id_poli);
                    params.put("biaya_reg", biaya);
                    params.put("id_loket", "3");
                    params.put("kd_antrian", "A");

                    AndLog.ShowLog("swebd", params.toString());
                } catch (GeneralSecurityException e) {
                    e.printStackTrace();
                }

                return params;


            }

        };
        requestQueue.add(strReq);
    }

    @OnClick(R.id.btSubmit)
    public void onViewClicked() {
        SubmitInsertUserAdmum();

    }
}
