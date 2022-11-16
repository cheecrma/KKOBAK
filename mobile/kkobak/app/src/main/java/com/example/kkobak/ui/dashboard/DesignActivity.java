package com.example.kkobak.ui.dashboard;

import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.transition.Explode;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.util.Pair;
import androidx.viewpager.widget.ViewPager;

import com.example.kkobak.R;
import com.example.kkobak.ui.dashboard.fragments.ExpandingFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;

public class DesignActivity extends AppCompatActivity implements ExpandingFragment.OnExpandingClickListener {
    ViewPager viewPager;
    ViewGroup back;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_design);

        viewPager = findViewById(R.id.viewPager);
        back = findViewById(R.id.back);

        ButterKnife.bind(this);
        setupWindowAnimations();
//
        TravelViewPagerAdapter adapter = new TravelViewPagerAdapter(getSupportFragmentManager());
        adapter.addAll(generateTravelList());
        viewPager.setPageMargin(getResources().getDimensionPixelOffset(R.dimen.viewpager_margin));
        viewPager.setAdapter(adapter);
//
//
//        ExpandingPagerFactory.setupViewPager(viewPager);
//
//        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
//            @Override
//            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//                ExpandingFragment expandingFragment = ExpandingPagerFactory.getCurrentFragment(viewPager);
//                if(expandingFragment != null && expandingFragment.isOpenend()){
//                    expandingFragment.close();
//                }
//            }
//
//            @Override
//            public void onPageSelected(int position) {
//            }
//
//            @Override
//            public void onPageScrollStateChanged(int state) {
//
//            }
//        });
    }

    @Override
    public void onBackPressed() {
        if(!ExpandingPagerFactory.onBackPressed(viewPager)){
            super.onBackPressed();
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void setupWindowAnimations() {
        Explode slideTransition = new Explode();
        getWindow().setReenterTransition(slideTransition);
        getWindow().setExitTransition(slideTransition);
    }

    private List<Travel> generateTravelList(){
        List<Travel> travels = new ArrayList<>();
        for(int i=0;i<5;++i){
            travels.add(new Travel("1번도시", R.drawable.seychelles));
            travels.add(new Travel("2번도시", R.drawable.shh));
            travels.add(new Travel("3번도시", R.drawable.newyork));
            travels.add(new Travel("4번도시", R.drawable.p1));
        }
        return travels;
    }
    @SuppressWarnings("unchecked")
    private void startInfoActivity(View view, Travel travel) {
        Activity activity = this;
        ActivityCompat.startActivity(activity,
                InfoActivity.newInstance(activity, travel),
                ActivityOptionsCompat.makeSceneTransitionAnimation(
                                activity,
                                new Pair<>(view, getString(R.string.transition_image)))
                        .toBundle());
    }

    @Override
    public void onExpandingClick(View v) {
        //v is expandingfragment layout
        View view = v.findViewById(R.id.image);
        Travel travel = generateTravelList().get(viewPager.getCurrentItem());
        startInfoActivity(view,travel);
    }
}
