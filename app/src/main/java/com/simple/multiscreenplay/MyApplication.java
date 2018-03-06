package com.simple.multiscreenplay;

import android.app.Application;

import com.simple.log.Slog;
import com.simple.utils.Consts;
import com.simple.utils.DataManager;
import com.simple.utils.ScreenUtils;

public class MyApplication extends Application {

	private DataManager dataManager = DataManager.getInstance();

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		boolean useDeviceSize = true;
		// int[] screenSize = ScreenUtils.getScreenSize(this, useDeviceSize);
		DataManager.getInstance().init(getApplicationContext());
		int[] screenSizeNotitleBar = ScreenUtils.getScreenSize(this, true);
		Slog.d("app", screenSizeNotitleBar[0] + " " + screenSizeNotitleBar[1]);
		dataManager.saveIntData(Consts.SCREENWIDTH_NOTITLE_BAR,
				screenSizeNotitleBar[0]);
		dataManager.saveIntData(Consts.SCREENHEIGHT_NOTITLE_BAR,
				screenSizeNotitleBar[1]);
		super.onCreate();
	}

}
