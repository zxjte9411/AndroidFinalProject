package com.example.zxjte9411.androidfinalproject;

public interface MusicInterface {

    //播放音乐
    void play();

    //暂停播放音乐
    void pausePlay();

    //继续播放音乐
    void continuePlay();

    //修改音乐的播放位置
    void seekTo(int progress);

    void stop();

    boolean isPlaying();

    void setIsAllSongLooping();

    void setIsSingleLooping();
}
