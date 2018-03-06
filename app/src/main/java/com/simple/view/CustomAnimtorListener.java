package com.simple.view;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.view.View;

/**
 * 继承自动画原生监听器目的是为了不现实没有用的函数
 * @author giaour
 *
 */
public class CustomAnimtorListener implements AnimatorListener {

	@Override
	public void onAnimationCancel(Animator arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onAnimationEnd(Animator arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onAnimationRepeat(Animator arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onAnimationStart(Animator arg0) {
		// TODO Auto-generated method stub

	}

	public void onAnimationEnd(View... views) {
		// TODO Auto-generated method stub

	}
}
