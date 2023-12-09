package com.example.finalapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;
import com.google.android.material.tabs.TabLayout;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // TabLayout 및 ViewPager 초기화
        TabLayout tabLayout = findViewById(R.id.tabLayout);
        ViewPager viewPager = findViewById(R.id.viewPager);

        // 각 탭에 대한 프래그먼트 어댑터 생성
        TabPagerAdapter adapter = new TabPagerAdapter(getSupportFragmentManager());

        // 프래그먼트 추가 (MealInputFragment, MealCheckFragment, MealAnalysisFragment)
        adapter.addFragment(new MealInputFragment(), "식사 입력");
        adapter.addFragment(new MealCheckFragment(), "식사 확인");
        adapter.addFragment(new MealAnalysisFragment(), "식사 분석");

        // ViewPager에 어댑터 설정
        viewPager.setAdapter(adapter);

        // TabLayout과 ViewPager 연결
        tabLayout.setupWithViewPager(viewPager);

        // 각 탭에 아이콘 또는 추가적인 설정을 원하는 경우 추가 설정 가능
        // tabLayout.getTabAt(0).setIcon(R.drawable.ic_input);
        // tabLayout.getTabAt(1).setIcon(R.drawable.ic_check);
        // tabLayout.getTabAt(2).setIcon(R.drawable.ic_analysis);
    }
}
