package com.piramidsoft.sirs;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.piramidsoft.sirs.Adapter.SlidePageAdapter;
import com.piramidsoft.sirs.Utils.SessionManager;
import com.scottyab.aescrypt.AESCrypt;
import com.viewpagerindicator.CirclePageIndicator;

import java.security.GeneralSecurityException;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import me.relex.circleindicator.CircleIndicator;

public class HomeFragment extends Fragment {

    @BindView(R.id.tvNamaPasien)
    TextView tvNamaPasien;

    Unbinder unbinder;
    SessionManager sessionManager;
    FragmentActivity mActivity;
    @BindView(R.id.vpSlide)
    ViewPager vpSlide;
    @BindView(R.id.cpIndicator)
    CirclePageIndicator cpIndicator;
    @BindView(R.id.lyIndicator)
    LinearLayout lyIndicator;
    @BindView(R.id.rlRegistrasiPoli)
    RelativeLayout rlRegistrasiPoli;
    @BindView(R.id.rlDaftarTagihan)
    RelativeLayout rlDaftarTagihan;
    @BindView(R.id.rlRekammedis)
    RelativeLayout rlRekammedis;
    @BindView(R.id.rlHistory)
    RelativeLayout rlHistory;
    @BindView(R.id.rlPasienDarurat)
    RelativeLayout rlPasienDarurat;
    @BindView(R.id.rlEmergency)
    RelativeLayout rlEmergency;
    @BindView(R.id.indicator)
    CircleIndicator indicator;
    private Handler handler;
    private int slidePage;
    private ArrayList<Integer> slideList;
    private int SLIDE_DELAY = 5000;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_fragment, container, false);
        unbinder = ButterKnife.bind(this, view);
        mActivity = getActivity();
        sessionManager = new SessionManager(mActivity);
        try {
            tvNamaPasien.setText(AESCrypt.decrypt("nam", sessionManager.getNama()));
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        }

//        slidePage = 0;
//        handler = new Handler();
//        slideList = new ArrayList<>();
//        slideList.add(R.drawable.banner1);
//        slideList.add(R.drawable.banner2);
//        slideList.add(R.drawable.banner3);
//        SlidePageAdapter adapter = new SlidePageAdapter(mActivity, slideList);
//        vpSlide.setAdapter(adapter);
//

//
//        handler.post(runnable);

        initViewPager();
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private void initViewPager() {
        final ScreenSlidePagerAdapter pagerAdapter = new ScreenSlidePagerAdapter(getFragmentManager());
        vpSlide.setAdapter(pagerAdapter);
        cpIndicator.setCentered(true);
        cpIndicator.setViewPager(vpSlide);
        handler = new Handler();
        runnable = new Runnable() {
            int pos = 0;

            @Override
            public void run() {
                if (pos < 4) {
                    pos++;
                } else {
                    pos = 0;
                }
                vpSlide.setCurrentItem(pos);
                handler.postDelayed(runnable, 3000);
            }
        };
        handler.postDelayed(runnable, 3000);

    }

    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            ScreenSlidePageFragment fragment = new ScreenSlidePageFragment();
            Bundle bundle = new Bundle();
            if (position % 2 == 0) {
                bundle.putInt("image", R.drawable.banner1);
            } else {
                bundle.putInt("image", R.drawable.banner2);
            }
            fragment.setArguments(bundle);
            return fragment;
        }

        @Override
        public int getCount() {
            return 5;
        }
    }


    @OnClick({R.id.rlRegistrasiPoli, R.id.rlDaftarTagihan, R.id.rlRekammedis, R.id.rlHistory, R.id.rlPasienDarurat, R.id.rlEmergency})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rlRegistrasiPoli:
                startActivity(new Intent(getActivity(), RegistrasiPoliActivity.class));
                break;
            case R.id.rlDaftarTagihan:
                startActivity(new Intent(getActivity(), DaftarTagihanActivity.class));
                break;
            case R.id.rlRekammedis:
                startActivity(new Intent(getActivity(), RekammedisActivity.class));
                break;
            case R.id.rlHistory:
                startActivity(new Intent(getActivity(), HistoryPasienActivity.class));
                break;
            case R.id.rlPasienDarurat:
                startActivity(new Intent(getActivity(), PanicActivity.class));
                break;

        }
    }

    private Runnable runnable = new Runnable() {
        public void run() {
            vpSlide.setCurrentItem(slidePage, true);

            if (slideList.size() == (slidePage + 1)) {
                slidePage = 0;
            } else {
                slidePage++;
            }
            handler.postDelayed(runnable, SLIDE_DELAY);
        }
    };


    @Override
    public void onDestroy() {
        super.onDestroy();

        if (handler != null) {
            handler.removeCallbacks(runnable);
        }
    }
}
