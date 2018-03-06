package com.simple.multiscreenplay;

import java.io.File;

import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.view.View;
import android.widget.Toast;

import com.simple.view.VideoPlayerView;

public class PlayerActivity extends BaseActivity {

	private VideoPlayerView playerView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_player);
		playerView = getView(R.id.video_player);
	}

	public void onClick(View v) {

		Toast.makeText(getApplicationContext(), "show msg", 3000).show();

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		handler.sendEmptyMessageDelayed(1, 500);
		super.onResume();
	}

	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			playerView.startPlay(getStoragePath(getApplicationContext())
					+ "o_1b4qflgbtqmg7damqp1pu51r5k9.mp4");
		};
	};

	public static String getStoragePath(Context context) {
		File sdDir = null;
		boolean sdCardExist = Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED); // 判断sd卡是否存在
		if (sdCardExist) {
			sdDir = Environment.getExternalStorageDirectory();// 获取跟目录
			if (sdDir == null) {
				return "";
			}
		} else {
			return context.getFilesDir().getPath() + File.separator;
		}
		return sdDir.getPath() + File.separator;
	}

}
