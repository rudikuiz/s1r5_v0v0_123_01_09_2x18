package com.piramidsoft.sirs.Utils;

/**
 * Created by Web on 16/04/2016.
 */

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class SessionManager {

    SharedPreferences pref;

    Editor editor;

    Context _context;

    int PRIVATE_MODE = 0;

    public static final String ID_PASIEN = "id_pasien";
    public static final String NAMA = "name";
    public static final String TOKEN = "token";
    public static final String APIKEY = "apikey";
    public static final String REFRESH_CODE = "refresh_code";

    private static final String PREF_NAME = "com.piramidsoft.sirs.log";

    private static final String IS_LOGIN = "IsLoggedIn";

    private static final String KEY_IDUSER = "iduser";

    private static final String KEY_NAMA = "name";

    private static final String KEY_UMUR = "umur";

    private static final String KEY_TELEPON = "telepon";

    private static final String KEY_ORTU = "ortu";

    private static final String KEY_USERNAME = "rate";

    private static final String SESSION = "_session";

    private static final String KEY_PASSWORD = "max";

    private static final String KEY_JK = "jk";

    private static final String KEY_BARCODE = "barcode";

    private static final String KEY_ANTRIAN = "antrian";

    private static final String VTOKEN = "token";

    private static final String FIRSTLOOK = "firstLook";


    public SessionManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void createLoginSession(String idpasien, String nama, String token, String apikey, String refresh_code) {

        editor.putBoolean(IS_LOGIN, true);

        editor.putString(NAMA, nama);

        editor.putString(ID_PASIEN, idpasien);

        editor.putString(TOKEN, token);

        editor.putString(APIKEY, apikey);

        editor.putString(REFRESH_CODE, refresh_code);


        editor.commit();
    }

    public boolean checkLogin() {
        // Check login status

        boolean stLogin = true;

        if (!this.isLoggedIn()) {

            stLogin = false;
        }

        return stLogin;

    }

    public void setFirstlook() {

        editor.putBoolean(FIRSTLOOK, true);
        editor.commit();
    }

    public boolean checkFirstLook() {
        // Check login status

        boolean stLook = true;

        if (!this.isFirstLook()) {

            stLook = false;
        }

        return stLook;

    }

    public void logoutUser() {
        // Clearing all data from Shared Preferences
        boolean look = false;
        if (this.checkFirstLook()) {
            look = true;
        }

        editor.clear();
        editor.commit();

        if (look) {
            this.setFirstlook();
        }


    }

    public String getIdPasien() {
        return pref.getString(ID_PASIEN, null);
    }

    public boolean isLoggedIn() {
        return pref.getBoolean(IS_LOGIN, false);
    }

    public boolean isFirstLook() {
        return pref.getBoolean(FIRSTLOOK, false);
    }

    public String getNama() {
        return pref.getString(KEY_NAMA, null);
    }

    public void setNama(String nama) {
        editor.putString(KEY_NAMA, nama);
        editor.commit();
    }

    public String getKeyBarcode() {
        return pref.getString(KEY_BARCODE, null);
    }

    public void setKeyBarcode(String val) {
        editor.putString(KEY_BARCODE, val);
        editor.commit();
    }

    public String getKeyAntrian() {
        return pref.getString(KEY_ANTRIAN, null);
    }

    public void setKeyAntrian(String val) {
        editor.putString(KEY_ANTRIAN, val);
        editor.commit();
    }

    public String getKeyUmur() {
        return pref.getString(KEY_UMUR, null);
    }

    public void setKeyUmur(String idsession) {
        editor.putString(KEY_UMUR, idsession);
        editor.commit();
    }

    public String getKeyOrtu() {
        return pref.getString(KEY_ORTU, null);
    }

    public void setKeyOrtu(String incall) {
        editor.putString(KEY_ORTU, incall);
        editor.commit();
    }

    public String getKeyTelepon() {
        return pref.getString(KEY_TELEPON, null);
    }

    public void setKeyTelepon(String inchat) {
        editor.putString(KEY_TELEPON, inchat);
        editor.commit();
    }

    public String getKeyUsername() {
        return pref.getString(KEY_USERNAME, null);
    }

    public void setKeyUsername(String rate) {
        editor.putString(KEY_USERNAME, rate);
        editor.commit();
    }

    public String getKeyJk() {
        return pref.getString(KEY_JK, null);
    }

    public void setKeyJk(String value) {
        editor.putString(KEY_JK, value);
        editor.commit();
    }

    public String getKeyPassword() {
        return pref.getString(KEY_PASSWORD, null);
    }

    public void setKeyPassword(String value) {
        editor.putString(KEY_PASSWORD, value);
        editor.commit();
    }


}