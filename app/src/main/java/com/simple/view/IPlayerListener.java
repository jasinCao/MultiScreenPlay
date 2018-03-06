package com.simple.view;


public interface IPlayerListener
{

    public void onPlayStart();

    public void onPlayPause();

    public void onPlayStop();

    public void onPlayCompletion();

    public void onPlayInit();

    public void onPlayError(String path);
    
    public void onPlayResume();

    public void onPlayBack();

    public void onSnycStatus();

    public void onSeekComplete();

    public void onControlStop();

    public void onProgress(int progress);
    
    public void onSeek();
    
    public void onTimeChange(int maxLength,int timer);
    
}
