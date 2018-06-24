package com.example.zxjte9411.androidfinalproject;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
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
import android.widget.ArrayAdapter;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

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

    //    public static MediaPlayer mediaPlayer = new MediaPlayer();
    public static MusicInterface mi;
    /*-------------------------------------------------*/
    public static ArrayAdapter arrayListViewAdapter;
    public static ArrayAdapter musicPlayListAdapter;
    /*----File----*/
    public static final String previousPag = "..";
    public static File root, folder;
    public static String path;

    public static ArrayList<String> fileNameList;
    public static ArrayList<File> filePathList;
    public static ArrayList<File> musicPlayList;
    private static boolean isInitialize = false;
    //用于设置音乐播放器的播放进度
    private static SeekBar sb;
    private static TextView tv_progress;
    private static TextView tv_total;
    public static String name;
    //创建消息处理器对象
    @SuppressLint("HandlerLeak")
    public static Handler handler = new Handler(){

        //在主线程中处理从子线程发送过来的消息
        @Override
        public void handleMessage(Message msg) {

            //获取从子线程发送过来的音乐播放的进度
            Bundle bundle = msg.getData();

            //歌曲的总时长(毫秒)
            int duration = bundle.getInt("duration");

            //歌曲的当前进度(毫秒)
            int currentPostition = bundle.getInt("currentPosition");

            //刷新滑块的进度
            sb.setMax(duration);
            sb.setProgress(currentPostition);

            //歌曲的总时长
            int minute = duration / 1000 / 60;
            int second = duration / 1000 % 60;

            String strMinute = null;
            String strSecond = null;

            //如果歌曲的时间中的分钟小于10
            if(minute < 10) {

                //在分钟的前面加一个0
                strMinute = "0" + minute;
            } else {

                strMinute = minute + "";
            }

            //如果歌曲的时间中的秒钟小于10
            if(second < 10)
            {
                //在秒钟前面加一个0
                strSecond = "0" + second;
            } else {

                strSecond = second + "";
            }

            tv_total.setText(strMinute + ":" + strSecond);

            //歌曲当前播放时长
            minute = currentPostition / 1000 / 60;
            second = currentPostition / 1000 % 60;

            //如果歌曲的时间中的分钟小于10
            if(minute < 10) {

                //在分钟的前面加一个0
                strMinute = "0" + minute;
            } else {

                strMinute = minute + "";
            }

            //如果歌曲的时间中的秒钟小于10
            if(second < 10) {

                //在秒钟前面加一个0
                strSecond = "0" + second;
            } else {

                strSecond = second + "";
            }

            tv_progress.setText(strMinute + ":" + strSecond);
            tv_name.setText(name);
        }
    };
    private static TextView tv_name;
    MyServiceConn conn;
    Intent intent;

    /*-------------------------------------------------*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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

        HomePermissionsDispatcher.needsPermissionWithPermissionCheck(this);
        try {
            MusicService.path = musicPlayList.get(0).getPath();
        }
        catch (Exception e){
            Toast.makeText(this, "Music 目錄下沒有歌曲檔案", Toast.LENGTH_SHORT).show();
        }

        sb = findViewById(R.id.sb);
        tv_progress = (TextView) findViewById(R.id.tv_progress);
        tv_total = (TextView) findViewById(R.id.tv_total);
        tv_name = findViewById(R.id.tv_name);

        //创建意图对象
        intent = new Intent(this, MusicService.class);

        //启动服务
        startService(intent);

        //创建服务连接对象
        conn = new MyServiceConn();

        //绑定服务
        bindService(intent, conn, BIND_AUTO_CREATE);

        //获得布局文件上的滑动条
        sb = (SeekBar) findViewById(R.id.sb);

        //为滑动条添加事件监听
        sb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            //当滑动条中的进度改变后,此方法被调用
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            //滑动条刚开始滑动,此方法被调用
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            //当滑动条停止滑动,此方法被调用
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

                //根据拖动的进度改变音乐播放进度
                int progress = seekBar.getProgress();

                //改变播放进度
                mi.seekTo(progress);
            }
        });

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
        initializeFile();
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

    //播放音乐按钮响应函数
    public void play(View view) {

        //播放音乐
        mi.play();
    }

    //暂停播放音乐按钮响应函数
    public void pausePlay(View view) {

        //暂停播放音乐
        mi.pausePlay();
    }

    //继续播放音乐按钮响应函数
    public void continuePlay (View view) {

        //继续播放音乐
        mi.continuePlay();
    }

    //退出音乐播放按钮响应函数
    public void exit(View view) {

        //解绑服务
        unbindService(conn);

        //停止服务
        stopService(intent);

        //结束这个activity
        finish();
    }

    //实现服务器连接接口
    class MyServiceConn implements ServiceConnection {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {

            //获得中间人对象
            mi = (MusicInterface) service;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }

    }

    public boolean isMusicFormat(String fileName){
        String[] formats = {".mp3", ".ogg"};
        for(String format: formats){
            return fileName.endsWith(format);
        }
        return false;
    }

    @NeedsPermission({Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE})
    void initializeFile(){
        if (isInitialize){
            return;
        }else {
            isInitialize = true;
        }
        /*資料夾在所有檔案後面 播放清單沒有資料夾*/
        root = Environment.getExternalStorageDirectory();
        path = Environment.getExternalStorageDirectory().getPath();
        folder = new File(path + "/Music");
        fileNameList = new ArrayList<>();
        filePathList = new ArrayList<>();
        musicPlayList = new ArrayList<>();
        /*暫存資料夾路徑及檔名*/
        ArrayList<String> fileNameListTmp = new ArrayList<>();
        ArrayList<File> filePathListTmp = new ArrayList<>();
        for(File fileName: folder.listFiles()){
            Log.v("files",fileName.getName());
            if(fileName.isFile()){ // 檢查是不是檔案
                if(isMusicFormat(fileName.getName())){ //檢查是否是音樂格式
                    Log.v("addinf","added!");
                    fileNameList.add(fileName.getName());
                    filePathList.add(fileName);
                    musicPlayList.add(fileName);
                }
            }else{// 是資料夾
                if(!fileName.getName().startsWith(".")){
                    fileNameListTmp.add(fileName.getName());
                    filePathListTmp.add(fileName);
                }
            }
        }
        Collections.sort(musicPlayList);
        ArrayList<String> temp = new ArrayList<>();
        for(File musicFileName:musicPlayList) {
            temp.add(musicFileName.getName());
        }
        musicPlayListAdapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1,temp);//音樂清單
        Collections.sort(fileNameList);//只有檔案名稱
        Collections.sort(filePathList);//檔按路徑
        Collections.sort(fileNameListTmp);//只有檔案名稱
        Collections.sort(filePathListTmp);//檔按路徑
        fileNameList.addAll(fileNameListTmp);
        filePathList.addAll(filePathListTmp);
        fileNameList.add(0,previousPag);
        arrayListViewAdapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1,fileNameList);//所有檔案清單
        try {
            MusicService.path = musicPlayList.get(0).getPath();
            name = musicPlayList.get(0).getName();
        }
        catch (Exception e){
            Toast.makeText(this, "當前目錄無音樂", Toast.LENGTH_SHORT).show();
        }
    }
}
