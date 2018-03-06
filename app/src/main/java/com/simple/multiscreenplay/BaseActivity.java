package com.simple.multiscreenplay;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

public class BaseActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}

	@SuppressWarnings("unchecked")
	public <T extends View> T getView(int id) {
		return (T) findViewById(id);
	}
}
