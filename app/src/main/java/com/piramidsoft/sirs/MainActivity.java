package com.piramidsoft.sirs;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.piramidsoft.sirs.Utils.GlobalToast;
import com.piramidsoft.sirs.Utils.SessionManager;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.drawerlayout)
    DrawerLayout mDrawerLayout;
    @BindView(R.id.flContent)
    FrameLayout flContent;
    @BindView(R.id.bottomNav)
    BottomNavigationView bottomNav;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    private ActionBarDrawerToggle mToggle;
    private FragmentManager fragmentManager;
    SessionManager sessionManager;
    private final int PROS_ID = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        sessionManager = new SessionManager(MainActivity.this);
        mToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.open, R.string.close);
        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();

//        setSupportActionBar(toolbar);

        fragmentManager = getSupportFragmentManager();
        bottomNav.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        fragmentManager
                .beginTransaction()
                .replace(R.id.flContent, new HomeFragment(), getString(R.string.topup))
                .commit();
        if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.RECEIVE_SMS) != PackageManager.PERMISSION_GRANTED
                ){
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{
                            Manifest.permission.CALL_PHONE,
                            Manifest.permission.SEND_SMS,
                            Manifest.permission.RECEIVE_SMS,
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
                    GlobalToast.ShowToast(MainActivity.this, "Permission denied to read your External storage");

                }
                return;
            }
        }
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.nav_home:
                    fragmentManager
                            .beginTransaction()
                            .replace(R.id.flContent, new HomeFragment(), getString(R.string.topup))
                            .commit();
                    return true;
                case R.id.nav_setting:
                    fragmentManager
                            .beginTransaction()
                            .replace(R.id.flContent, new SettingFragment(), getString(R.string.panic))
                            .commit();

                    return true;
            }
            return false;
        }

    };


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.my_option_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.logout) {
            new AlertDialog.Builder(MainActivity.this)
                    .setMessage("Apakah anda yakin ingin keluar dari aplikasi ini??")
                    .setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                            startActivity(intent);
                            sessionManager.logoutUser();
                            finish();
                        }
                    })
                    .setNegativeButton("No", null)
                    .show();
        }  else if (item.getItemId() == R.id.setting) {
            GlobalToast.ShowToast(MainActivity.this, "Setting");

        }


        return true;
    }
}
