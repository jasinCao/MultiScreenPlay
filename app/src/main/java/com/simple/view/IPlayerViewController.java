package com.simple.view;

import android.view.View;

public interface IPlayerViewController {

	public void setPlayerListener(IPlayerListener listener);

	public void stop();

	public void startPlay(String path);

	public void play();

	public void pause();

	public void resume();

	public void seek(int mSec);

	public boolean isPalying();

	public boolean isPause();

	public void release();

	public int getDuration();

	public int getCurrentPos();

	public void onSeek();

	public void setSurfaceCtrlView(View v);

	public void surfaceGone();

	public void setCoustomScreenSize(int w, int h);


}
