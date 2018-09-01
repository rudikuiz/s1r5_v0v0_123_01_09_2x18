package com.piramidsoft.sirs;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.FrameLayout;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DaftarTagihanActivity extends AppCompatActivity {
    @BindView(R.id.flContent)
    FrameLayout flContent;
    @BindView(R.id.bottomNav)
    BottomNavigationView bottomNav;
    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daftar_tagihan);
        ButterKnife.bind(this);
        fragmentManager = getSupportFragmentManager();
        bottomNav.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        fragmentManager
                .beginTransaction()
                .replace(R.id.flContent, new LunasFragment(), getString(R.string.lunas))
                .commit();
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.nav_b_lunas:
                    fragmentManager
                            .beginTransaction()
                            .replace(R.id.flContent, new UnLunasFragment(), getString(R.string.blunas))
                            .commit();

                    return true;

                case R.id.nav_lunas:
                    fragmentManager
                            .beginTransaction()
                            .replace(R.id.flContent, new LunasFragment(), getString(R.string.lunas))
                            .commit();
                    return true;
            }
            return false;
        }

    };

}
