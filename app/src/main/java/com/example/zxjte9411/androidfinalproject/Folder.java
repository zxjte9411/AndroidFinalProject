package com.example.zxjte9411.androidfinalproject;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

public class Folder extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    /*-------------------------------------*/
    private final String previousPag = "..";
    private File root, folder;
    private String path;
    private ArrayList<String> fileNameList;
    private ArrayList<File> filePathList;
    private ListView pathListView;
//    MediaPlayer mediaPlayer;
    ArrayAdapter arrayAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_folder);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_folder);
        /*----List View----*/
        pathListView = findViewById(R.id.path_list_view);
        pathListView.setOnItemClickListener(onItemClickListener);
        /*----File----*/
        root = Home.root;
        path = Home.path;
        folder = Home.folder;
        /*----ArrayList----*/
        fileNameList = new ArrayList<>();
        filePathList = new ArrayList<>();
        fileNameList = Home.fileNameList;
        filePathList = Home.filePathList;
//        for(int i = 0;i<folder.listFiles().length;i++){
//            fileNameList.add(folder.listFiles()[i].getName());
//            filePathList.add(folder.listFiles()[i]);
//        }
//        Collections.sort(fileNameList);
//        fileNameList.add(0,previousPag);
        arrayAdapter = Home.arrayListViewAdapter;
        pathListView.setAdapter(arrayAdapter);
//        mediaPlayer = new MediaPlayer();
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
        getMenuInflater().inflate(R.menu.folder, menu);
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
        intent.setClass(Folder.this,NavigationBarActivity.class);

        if (id == R.id.nav_home) {
            intent.setClass(Folder.this,Home.class);
            startActivity(intent);
        } else if (id == R.id.nav_queue_music) {
            intent.setClass(Folder.this,PlayQueue.class);
            startActivity(intent);
        } else if (id == R.id.nav_playList) {
            intent.putExtra("View",0);
            intent.putExtra("fileNameList", Home.fileNameList);
            startActivity(intent);
        } else if (id == R.id.nav_singer) {
            intent.putExtra("View",1);
            startActivity(intent);
        } else if (id == R.id.nav_album) {
            intent.putExtra("View",2);
            startActivity(intent);
        } else if (id == R.id.nav_song) {
            intent.putExtra("View",3);
            startActivity(intent);
        } else if (id == R.id.nav_folder) {

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            Toast.makeText(Folder.this, fileNameList.get(i), Toast.LENGTH_SHORT).show();
            File isFile = new File(folder.getPath() + "/" + fileNameList.get(i));
            Log.d("isFile", String.valueOf(isFile.isFile()));
            if (isFile.isFile()) {
                Log.e("folderName", isFile.getName());
                Log.e("parentPath", isFile.getParentFile().getName());
                Log.e("root", folder.getPath());

                Uri uri = Uri.fromFile(new File(filePathList.get(i - 1).getPath()));
                MusicService.path = filePathList.get(i - 1).getPath();
                if (Home.mi.isPlaying()) {
                    Home.mi.stop();
                }
                Home.mi.play();
            } else {//進入上層目錄
                if (fileNameList.get(i).equals("..")) {
                    Log.e("folderName", isFile.getName());
                    Log.e("parentPath", isFile.getParentFile().getParentFile().getName());
                    Log.e("root", folder.getPath());
                    resetListView(isFile.getParentFile().getParentFile());
                } else {//進入一般資料夾
                    Log.e("folderName", isFile.getName());
                    Log.e("parentPath", isFile.getParentFile().getName());
                    Log.e("root", folder.getPath());
                    resetListView(isFile);
                }
            }
        }
    };


    public void resetListView(File file){
        String[] formates = {".mp3", ".ogg"};
        ArrayList<File> filesPathTmp = new ArrayList<>();
        ArrayList<String> filesNameTmp = new ArrayList<>();
        fileNameList.clear();
        filePathList.clear();
        folder = new File(file.getPath());//取得當前目錄路徑
        for(int k = 0;k<folder.listFiles().length;k++){
            if(folder.listFiles()[k].isFile()){
                for(String formate: formates){
                    if(folder.listFiles()[k].getName().endsWith(formate)){
                        fileNameList.add(folder.listFiles()[k].getName());
                        filePathList.add(folder.listFiles()[k]);
                    }
                }

            }
            else{
                if(!folder.listFiles()[k].getName().startsWith(".")){
                    filesNameTmp.add(folder.listFiles()[k].getName());
                    filesPathTmp.add(folder.listFiles()[k]);
                }
            }
        }

        Collections.sort(fileNameList);
        Collections.sort(filePathList);
        fileNameList.addAll(filesNameTmp);
        filePathList.addAll(filesPathTmp);
        fileNameList.add(0, previousPag);
//        filePathList.add(0, new File(file.getParentFile().getPath() + "../"));
        arrayAdapter = new ArrayAdapter(Folder.this,android.R.layout.simple_list_item_1,fileNameList);
        pathListView.setAdapter(arrayAdapter);
        Home.arrayListViewAdapter = arrayAdapter;
        Home.fileNameList = fileNameList;
        Home.filePathList = filePathList;
        Home.folder = folder;
    }
}
