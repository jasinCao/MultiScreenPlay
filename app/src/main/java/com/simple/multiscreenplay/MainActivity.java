package com.simple.multiscreenplay;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.simple.view.SlidingFinishPageView;
import com.simple.view.VideoPlayerView;

public class MainActivity extends BaseActivity {

	private SlidingFinishPageView finishPageView;
	private VideoPlayerView playerView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_main);
		finishPageView = getView(R.id.parent);
		finishPageView.setActivity(this);
	}

	public void onClick(View v) {

		startActivity(new Intent(this, PlayerActivity.class));

	}

}
