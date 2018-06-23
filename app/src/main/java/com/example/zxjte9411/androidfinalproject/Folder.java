package com.example.zxjte9411.androidfinalproject;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class Folder extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    /*-------------------------------------*/
    private final String previousPag = "..";
    private File root, folder;
    private String path;
    private ArrayList<String> list;
    private ListView pathListView;
    ArrayAdapter arrayAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_folder);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_folder);

        pathListView = findViewById(R.id.path_list_view);
        pathListView.setOnItemClickListener(onItemClickListener);
        root = Environment.getExternalStorageDirectory();
        path = Environment.getExternalStorageDirectory().getPath();
        folder = new File(path);
        list = new ArrayList<String>();
        list.add(previousPag);
        list.addAll(Arrays.asList(folder.list(mediafilefilter)));
        arrayAdapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1,list);
        pathListView.setAdapter(arrayAdapter);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.folder, menu);
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
    public boolean onNavigationItemSelected(MenuItem item) {
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

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            Toast.makeText(Folder.this, list.get(i), Toast.LENGTH_SHORT).show();
            folder = new File(folder.getPath() + "/" + list.get(i));
            Toast.makeText(Folder.this, folder.getAbsolutePath(), Toast.LENGTH_SHORT).show();
            list.clear();
            list.add(previousPag);
            list.addAll(Arrays.asList(folder.list(mediafilefilter)));
            Collections.sort(list);
            pathListView.setAdapter(new ArrayAdapter(Folder.this,android.R.layout.simple_list_item_1,list));
        }
    };

    FilenameFilter mediafilefilter = new FilenameFilter(){
        private String[] filter = {".mp3",".ogg",".mp4"};
        @Override
        public boolean accept(File dir, String filename) {
            for(int i= 0;i< filter.length ; i++){
                if(filename.indexOf(filter[i]) != -1)return true;
                else if(!dir.isFile())return true;
            }
            return false;
        }};
}
