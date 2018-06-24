package com.example.zxjte9411.androidfinalproject;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;

import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.RuntimePermissions;

@RuntimePermissions
public class Home extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    /*-------------------------------------------------*/
    public static ArrayAdapter arrayListViewAdapter;
    public static final String previousPag = "..";
    public static File root, folder;
    public static String path;
    public static ArrayList<String> fileNameList;
    public static ArrayList<File> filePathList;
    /*-------------------------------------------------*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        HomePermissionsDispatcher.needsPermissionWithPermissionCheck(this);

        setContentView(R.layout.activity_home);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_home);
        /*----------------------------------------*/
        root = Environment.getExternalStorageDirectory();
        path = Environment.getExternalStorageDirectory().getPath();
        folder = new File(path);
        fileNameList = new ArrayList<>();
        filePathList = new ArrayList<>();
        for(int i = 0;i<folder.listFiles().length;i++){
            fileNameList.add(folder.listFiles()[i].getName());
            filePathList.add(folder.listFiles()[i]);
        }
        Collections.sort(fileNameList);
        fileNameList.add(0,previousPag);
        arrayListViewAdapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1,fileNameList);

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
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        return id == R.id.action_settings || super.onOptionsItemSelected(item);

    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        Intent intent = new Intent();
        intent.setClass(Home.this, NavigationBarActivity.class);

        if (id == R.id.nav_home) {
            // Handle the camera action

        } else if (id == R.id.nav_queue_music) {
            intent.setClass(Home.this,PlayQueue.class);
            startActivity(intent);
        } else if (id == R.id.nav_playList) {
            intent.putExtra("View", 0);
            startActivity(intent);
        } else if (id == R.id.nav_singer) {
            intent.putExtra("View", 1);
            startActivity(intent);
        } else if (id == R.id.nav_album) {
            intent.putExtra("View", 2);
            startActivity(intent);
        } else if (id == R.id.nav_song) {
            intent.putExtra("View", 3);
            startActivity(intent);
        } else if (id == R.id.nav_folder) {
            intent.setClass(Home.this,Folder.class);
            startActivity(intent);
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @NeedsPermission({Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE})
    void needsPermission() {
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        HomePermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

    @OnShowRationale({Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE})
    void onShowRationale(final PermissionRequest request) {
    }

    @OnPermissionDenied({Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE})
    void onPermissionDenied() {
    }

    @OnNeverAskAgain({Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE})
    void onNeverAskAgain() {
    }
}
