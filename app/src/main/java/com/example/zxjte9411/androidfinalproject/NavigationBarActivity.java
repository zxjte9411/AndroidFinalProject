package com.example.zxjte9411.androidfinalproject;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class NavigationBarActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    public final String TAG = "Debug";

    private ViewPager viewPager;

    //fragments
    private AlbumFragment albumFragment;
    private PlayListFragment playListFragmen;
    private SingerFragment singerFragment;
    private SongFragment songFragment;

    private TabLayout tabLayout;
    private NavigationView navigationView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation_bar);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        SectionPagerAdapter sectionPagerAdapter = new SectionPagerAdapter(getSupportFragmentManager());
        viewPager = findViewById(R.id.viewPager);
        viewPager.setAdapter(sectionPagerAdapter);

        tabLayout = findViewById(R.id.tblTabLine);
        tabLayout.setupWithViewPager(viewPager);
        viewPager.setOnPageChangeListener(onPageChangeListener);

        albumFragment = new AlbumFragment();
        playListFragmen = new PlayListFragment();
        singerFragment = new SingerFragment();
        songFragment = new SongFragment();

        Intent intent = getIntent();
        int viewCurrent = intent.getIntExtra("View",0);
        viewPager.setCurrentItem(viewCurrent);
        switch (viewCurrent){
            case 0:
                navigationView.setCheckedItem(R.id.nav_playList);
                break;
            case 1:
                navigationView.setCheckedItem(R.id.nav_singer);
                break;
            case 2:
                navigationView.setCheckedItem(R.id.nav_album);
                break;
            case 3:
                navigationView.setCheckedItem(R.id.nav_song);
                break;
        }
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.navigation_bar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Intent intent = new Intent();

        if (id == R.id.nav_home) {
            intent.setClass(NavigationBarActivity.this,Home.class);
            startActivity(intent);
        } else if (id == R.id.nav_queue_music) {
            intent.setClass(NavigationBarActivity.this,PlayQueue.class);
            startActivity(intent);
        } else if (id == R.id.nav_playList) {
            viewPager.setCurrentItem(0);
        } else if (id == R.id.nav_singer) {
            viewPager.setCurrentItem(1);
        } else if (id == R.id.nav_album) {
            viewPager.setCurrentItem(2);
        } else if (id == R.id.nav_song) {
            viewPager.setCurrentItem(3);
        } else if (id == R.id.nav_folder) {
            intent.setClass(NavigationBarActivity.this,Folder.class);
            startActivity(intent);
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    ViewPager.OnPageChangeListener onPageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//            Log.d("onPageScrolled", String.valueOf(position));
        }

        @Override
        public void onPageSelected(int position) {
            if(position == 0) {
                navigationView.setCheckedItem(R.id.nav_playList);
            } else if(position == 1){
                navigationView.setCheckedItem(R.id.nav_singer);
            } else if(position == 2){
                navigationView.setCheckedItem(R.id.nav_album);
            } else if(position == 3){
                navigationView.setCheckedItem(R.id.nav_song);
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {
//            Log.d("onPageScrollStateChanged", String.valueOf(state));
        }
    };

    public class SectionPagerAdapter extends FragmentPagerAdapter {
        SectionPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Fragment fragment = null;
            switch (position){
                case 0:
                    fragment = playListFragmen;
                    break;
                case 1:
                    fragment = singerFragment;
                    break;
                case 2:
                    fragment = albumFragment;
                    break;
                case 3:
                    fragment = songFragment;
                    break;
            }
            return fragment;
        }

        @Override
        public int getCount() {
            return 4;
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            switch (position){
            case 0:
                return "播放清單";
            case 1:
                return "演出者";
            case 2:
                return "專輯";
            case 3:
                return "歌曲";
            default:
                return null;
            }
        }
    }
}
