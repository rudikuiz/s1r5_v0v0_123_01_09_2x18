package com.piramidsoft.sirs;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
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
import com.bumptech.glide.Glide;
import com.piramidsoft.sirs.Model.FileModel;
import com.piramidsoft.sirs.Utils.AndLog;
import com.piramidsoft.sirs.Utils.AppConf;
import com.piramidsoft.sirs.Utils.GlobalToast;
import com.piramidsoft.sirs.Utils.MediaProcess;
import com.piramidsoft.sirs.Utils.OwnProgressDialog;
import com.piramidsoft.sirs.Utils.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PendaftaranActivity extends AppCompatActivity {


    @BindView(R.id.rbP)
    RadioButton rbP;
    @BindView(R.id.rbW)
    RadioButton rbW;
    @BindView(R.id.rg)
    RadioGroup rg;
    @BindView(R.id.etUmur)
    EditText etUmur;
    @BindView(R.id.etNamaOrtu)
    EditText etNamaOrtu;
    @BindView(R.id.btRegestrasi)
    Button btRegestrasi;

    @BindView(R.id.etNamaLengkap)
    EditText etNamaLengkap;
    @BindView(R.id.etKodePasien)
    EditText etKodePasien;
    @BindView(R.id.etPhone)
    EditText etPhone;
    @BindView(R.id.etUsername)
    EditText etUsername;
    @BindView(R.id.etPassword)
    EditText etPassword;
    OwnProgressDialog loading;
    @BindView(R.id.etTTL)
    EditText etTTL;
    @BindView(R.id.spIdentitas)
    Spinner spIdentitas;
    @BindView(R.id.etIdentitas)
    EditText etIdentitas;
    @BindView(R.id.btTake)
    Button btTake;
    @BindView(R.id.etNoKK)
    EditText etNoKK;
    @BindView(R.id.imgKtp)
    ImageView imgKtp;

    private StringRequest stringRequest;
    private RequestQueue requestQueue;
    private final int MY_SOCKET_TIMEOUT_MS = 10 * 1000;
    String jk, result, id_pasien, id_poli;
    private static final String TAG_SUCCESS = "result";
    Boolean isLoading;
    private SessionManager sessionManager;
    private Uri mHighQualityImageUri;
    File outFileKtp, sdCard, dir;
    private final int PROS_ID = 8844;
    String image, fileName;
    Bitmap decoded;
    int bitmap_size = 50, TAKE_IMAGE = 777;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pendaftaran);
        ButterKnife.bind(this);
        requestQueue = Volley.newRequestQueue(PendaftaranActivity.this);
        sessionManager = new SessionManager(PendaftaranActivity.this);
        isLoading = false;
        loading = new OwnProgressDialog(PendaftaranActivity.this);
//        getKodePasien();

        if (sessionManager.getNama() == null) {

        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(PendaftaranActivity.this);

            builder.setTitle("Option Choose");
            builder.setMessage("Didalam halaman ini masih tersisa data inputan anda" + "\n" + "Apakah anda ingin menampilkan data itu kembali??");

            builder.setPositiveButton("Ya", new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface dialog, int which) {

                    etNamaLengkap.setText(sessionManager.getNama());
                    etUmur.setText(sessionManager.getKeyUmur());
                    etNamaOrtu.setText(sessionManager.getKeyOrtu());
                    if (sessionManager.getKeyJk().equals("L")) {
                        rbP.setChecked(true);
                    } else if (sessionManager.getKeyJk().equals("P")) {
                        rbW.setChecked(true);
                    }
                    etPhone.setText(sessionManager.getKeyTelepon());
//                    etUsername.setText(sessionManager.getKeyUsername());
//                    etPassword.setText(sessionManager.getKeyPassword());

                }
            });

            builder.setNegativeButton("Tidak", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {

                    // Do nothing
                    dialog.dismiss();
                    etNamaLengkap.requestFocus();
                }
            });

            AlertDialog alert = builder.create();
            alert.show();
        }
    }


    private void SaveLog() {
        sessionManager.setNama(etNamaLengkap.getText().toString());
        sessionManager.setKeyUmur(etUmur.getText().toString());
        sessionManager.setKeyOrtu(etNamaOrtu.getText().toString());
        sessionManager.setKeyTelepon(etPhone.getText().toString());
//        sessionManager.setKeyUsername(etUsername.getText().toString());
        sessionManager.setKeyJk(jk);
    }

    private void SubmitInsertPasien() {
        isLoading = true;
        loading.show();
        StringRequest strReq = new StringRequest(Request.Method.POST, AppConf.URL_TAMBAH_PASIEN, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jObj = new JSONObject(response);
                    result = jObj.getString(TAG_SUCCESS);
                    if (result.equals("true")) {
//                        isLoading = true;
//                        AndLog.ShowLog("InsertPasien", "---> Running");
//                        generateIdPasien();

                        isLoading = false;
                        loading.dismiss();
                        finish();
                        SaveLog();

                        GlobalToast.ShowToast(PendaftaranActivity.this, "Data Sukses di simpan");

                    } else {
                        GlobalToast.ShowToast(PendaftaranActivity.this, "Failed to insert database");
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),
                        "gagal bro", Toast.LENGTH_LONG).show();
                isLoading = false;
                loading.dismiss();
                SaveLog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<String, String>();
//                params.put("kode_pasien", etKodePasien.getText().toString());
                params.put("nama", etNamaLengkap.getText().toString());
                params.put("jk", jk);
                params.put("umur", etUmur.getText().toString());
                params.put("ortu", etNamaOrtu.getText().toString());
                params.put("telepon", etPhone.getText().toString());
                params.put("identitas", spIdentitas.getSelectedItem().toString());
                params.put("no_identitas", etIdentitas.getText().toString());
                params.put("kk", etNoKK.getText().toString());

                if (decoded != null) {
                    params.put("foto_ktp", getStringImage(decoded));
                }

                params.put("username", etUsername.getText().toString());
                params.put("password", etPassword.getText().toString());
                params.put("full_name", etNamaLengkap.getText().toString());
                params.put("phone", etPhone.getText().toString());

                AndLog.ShowLog("InsertPasien", params.toString());
                return params;
            }

        };
        requestQueue.add(strReq);
    }



    private void generateIdPasien() {
        stringRequest = new StringRequest(Request.Method.GET, AppConf.URL_GENERATE_ID_PASIEN, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject object = new JSONObject(response);
                    id_pasien = object.getString("data");
//                    SubmitInsertUserAdmum();
                    AndLog.ShowLog("AmbilIDTerbaru", "---> Running");
                    AndLog.ShowLog("id_pasienLog", id_pasien);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                if (error instanceof TimeoutError) {
                    Toast.makeText(PendaftaranActivity.this, "timeout", Toast.LENGTH_SHORT).show();
                } else if (error instanceof NoConnectionError) {
                    Toast.makeText(PendaftaranActivity.this, "no connection", Toast.LENGTH_SHORT).show();
                }
                SaveLog();
                loading.dismiss();
                isLoading = false;
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

    private void getKodePasien() {
        stringRequest = new StringRequest(Request.Method.GET, AppConf.URL_GENERATE_KODE_PASIEN, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject object = new JSONObject(response);
                    etKodePasien.setText(object.getString("kode"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                AndLog.ShowLog("genereates_kode", response.toString());


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                if (error instanceof TimeoutError) {
                    Toast.makeText(PendaftaranActivity.this, "timeout", Toast.LENGTH_SHORT).show();
                } else if (error instanceof NoConnectionError) {
                    Toast.makeText(PendaftaranActivity.this, "no connection", Toast.LENGTH_SHORT).show();
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

    private void InsertGenerateKodePasien() {
        stringRequest = new StringRequest(Request.Method.GET, AppConf.URL_INSERT_KODE_PASIEN, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                AndLog.ShowLog("InsertGenerateKode", response.toString());
                isLoading = false;
                loading.dismiss();
                finish();
                GlobalToast.ShowToast(PendaftaranActivity.this, "Data Sukses di simpan");
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                if (error instanceof TimeoutError) {
                    Toast.makeText(PendaftaranActivity.this, "timeout", Toast.LENGTH_SHORT).show();
                } else if (error instanceof NoConnectionError) {
                    Toast.makeText(PendaftaranActivity.this, "no connection", Toast.LENGTH_SHORT).show();
                }
                SaveLog();
                isLoading = false;
                loading.dismiss();
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


    public String getCurrentDate() {
        final Calendar c = Calendar.getInstance();
        int year, month, day;
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DATE);
        return day + "/" + (month + 1) + "/" + year;
    }

    public void onRadioButtonClicked() {

        int id = rg.getCheckedRadioButtonId();
        switch (id) {
            case R.id.rbP:
                jk = "L";
                break;
            case R.id.rbW:
                jk = "P";
                break;
        }
    }

    private FileModel generateTimeStampPhotoFileUri() {

        FileModel fileModel = null;
        Uri photoFileUri = null;
        File photoFile = null;
        File outputDir = getPhotoDirectory();
        if (outputDir != null) {
            photoFile = new File(outputDir, System.currentTimeMillis()
                    + ".jpg");
            photoFileUri = Uri.fromFile(photoFile);

            fileModel = new FileModel(photoFileUri, photoFile);
        }

        return fileModel;
    }

    private File getPhotoDirectory() {
        File outputDir = null;
        String externalStorageStagte = Environment.getExternalStorageState();
        if (externalStorageStagte.equals(Environment.MEDIA_MOUNTED)) {
            File photoDir = Environment.getExternalStorageDirectory();
            outputDir = new File(photoDir, "/ImageRS");
            if (!outputDir.exists())
                if (!outputDir.mkdirs()) {
                    Toast.makeText(
                            this,
                            "Failed to create directory "
                                    + outputDir.getAbsolutePath(),
                            Toast.LENGTH_SHORT).show();
                    outputDir = null;
                }
        }
        return outputDir;
    }

    private boolean checkPermission() {

        boolean allowed = false;
        if (ActivityCompat.checkSelfPermission(PendaftaranActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(PendaftaranActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(PendaftaranActivity.this,
                    new String[]{
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.CAMERA
                    },
                    PROS_ID);
        } else {
            allowed = true;
        }

        return allowed;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        sdCard = Environment.getExternalStorageDirectory();
        dir = new File(sdCard.getAbsolutePath() + "/ImageRS");
        dir.mkdirs();
        fileName = String.format("%d.jpg", System.currentTimeMillis());


        if (requestCode == TAKE_IMAGE && resultCode == RESULT_OK) {
            if (btTake.isClickable()) {
                try {
                    if (mHighQualityImageUri == null) {
                        GlobalToast.ShowToast(PendaftaranActivity.this, "Gagal memuat gambar, silahkan coba kembali.");
                    } else {
                        Bitmap bmp = MediaStore.Images.Media.getBitmap(getContentResolver(), mHighQualityImageUri);
                        MediaProcess.bitmapToFile(bmp, outFileKtp.getAbsolutePath(), 30);
                        bmp = MediaProcess.scaledBitmap(outFileKtp.getAbsolutePath());
                        MediaProcess.bitmapToFile(bmp, outFileKtp.getAbsolutePath(), 30);
                        Glide.with(PendaftaranActivity.this).load(outFileKtp.getAbsolutePath()).into(imgKtp);
                        image = outFileKtp.getAbsolutePath();

                        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                        bmp.compress(Bitmap.CompressFormat.JPEG, bitmap_size, bytes);
                        decoded = BitmapFactory.decodeStream(new ByteArrayInputStream(bytes.toByteArray()));
//                        imgKtp.setImageBitmap(decoded);
//                        Glide.with(FormPengajuan.this).load(outFileSelfi.getAbsolutePath()).into(imgSelfi);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void takeImageKtp() {
        FileModel fileModel = generateTimeStampPhotoFileUri();
        mHighQualityImageUri = fileModel.getUriPath();
        outFileKtp = fileModel.getFilePath();
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, mHighQualityImageUri);
        startActivityForResult(intent, TAKE_IMAGE);

    }

    public String getStringImage(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, bitmap_size, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    @OnClick({R.id.btTake, R.id.btRegestrasi})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btTake:
                if (checkPermission()) {
                    takeImageKtp();
                }
                break;
            case R.id.btRegestrasi:
                onRadioButtonClicked();
                if (!etNamaLengkap.getText().toString().isEmpty() &&
                        !etNamaOrtu.getText().toString().isEmpty() &&
                        !etUmur.getText().toString().isEmpty() &&
                        !etPhone.getText().toString().isEmpty() &&
                        !etUsername.getText().toString().isEmpty() &&
                        !etPassword.getText().toString().isEmpty()) {
                    if (decoded == null){
                        GlobalToast.ShowToast(PendaftaranActivity.this, "String decoded masih kosong!");
                    }else {
                        SubmitInsertPasien();
                    }

                } else {
                    GlobalToast.ShowToast(PendaftaranActivity.this, "Kolom tidak boleh kosong!");
                }
                break;
        }
    }
}
