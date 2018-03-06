package com.simple.view;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.Scroller;

import com.simple.autolayout.AutoRelativeLayout;
import com.simple.log.Slog;
import com.simple.utils.Consts;
import com.simple.utils.DataManager;

public class SlidingCtrlScaleView extends AutoRelativeLayout {

	private static final String TAG = "SlidingCtrlScaleView";

	private ObjectAnimator animatorX, animatorY, animatorH, animatorW;
	private AnimatorSet set;

	private int w;

	private int h;

	private int screenWidth, screenHeight;

	private int animDuration = 0;

	private DataManager dataManager = DataManager.getInstance();

	private Scroller mScroller;

	private ViewGroup parentView;
	private PlayerScaleView playerScaleView;

	private int targetWidth, targetHeiht;

	private float firstY, firstX;

	private int parentWidth, parentHeight, targetPointX, targetPointY;

	public SlidingCtrlScaleView(Context context) {
		super(context);
		init();
		// TODO Auto-generated constructor stub
	}

	public SlidingCtrlScaleView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
		// TODO Auto-generated constructor stub
	}

	public SlidingCtrlScaleView(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
		init();
		// TODO Auto-generated constructor stub
	}

	private void init() {
		mScroller = new Scroller(getContext(), new LinearInterpolator());
		screenWidth = dataManager.getIntData(Consts.SCREENWIDTH_NOTITLE_BAR);
		screenHeight = dataManager.getIntData(Consts.SCREENHEIGHT_NOTITLE_BAR);
		// screenHeightPercent = (float) screenHeight / Consts.DESGIN_HEIGHT;
		// screenWidthPercent = (float) screenWidth / Consts.DESGIN_WIDTH;
		animatorX = ObjectAnimator.ofFloat(this, "x", 0);
		animatorY = ObjectAnimator.ofFloat(this, "y", 0);
		animatorH = ObjectAnimator.ofInt(this, "h", 0);
		animatorW = ObjectAnimator.ofInt(this, "w", 0);
		set = new AnimatorSet();
		set.setDuration(0);
		set.addListener(new CustomAnimtorListener() {
			@Override
			public void onAnimationEnd(Animator arg0) {
				super.onAnimationEnd(arg0);
			}
		});
		set.playTogether(animatorX, animatorY, animatorH, animatorW);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// TODO Auto-generated method stub
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		// TODO Auto-generated method stub
		super.onLayout(changed, l, t, r, b);

		/**
		 * 获取外层布局
		 */
//		parentView = (ViewGroup) this.getParent();
//		/**
//		 * 获取子view
//		 */
//		playerScaleView = (PlayerScaleView) this.getChildAt(0);
//
//		parentWidth = parentView.getWidth();
//		parentHeight = parentView.getHeight();
//		targetWidth = parentWidth / 5 * 2;
//		targetPointX = parentWidth - targetWidth;
//		targetHeiht = targetWidth / 16 * 9; // 缩放后的视频窗口大小保持原有比例
//		targetPointY = parentHeight - targetHeiht - 200; // 计算右边缩小Y点的位置
	}

	/**
	 * 此变量专门用于滑动使用 不能直接使用fistY的原因是因为fistY在判断手指滑动方向的时候会导致 计算不精准从而导致 scrollby的指不均匀
	 */
	private float mDeltaY, mDeltaX;
	private int crrentY, crrentX;
	private int currentW, currentH;
	long touchTime = 0;
	private VelocityTracker mVelocityTracker = null;

//	@Override
//	public boolean onTouchEvent(MotionEvent event) {
//		// TODO Auto-generated method stub
//		float motionY = event.getRawY();
//		float motionX = event.getRawX();
//		if (mVelocityTracker == null) {
//			mVelocityTracker = VelocityTracker.obtain();
//		}
//		mVelocityTracker.addMovement(event);
//		if (event.getAction() == MotionEvent.ACTION_DOWN) {
//			firstY = event.getRawY();
//			firstX = event.getRawX();
//			mDeltaY = firstY;
//			mDeltaX = firstX;
//			// currentW = this.getWidth();
//			currentH = this.getHeight();
//			touchTime = System.currentTimeMillis();
//			/**
//			 * 用scroollto 来实现移动计算
//			 */
//			crrentY = getCurrentViewCoorX(1);
//			crrentX = getCurrentViewCoorX(0);
//			Slog.d("ACTION_UP", "->  ACTION_DOWN");
//		} else if (event.getAction() == MotionEvent.ACTION_MOVE) {
//			if (Math.abs((int) event.getRawX() - firstX) > (Math
//					.abs((int) event.getRawY() - firstY))) {
//				return false;
//			}
//			// int moveByY = (int) (mDeltaY - motionY);
//			// int moveByX = (int) (mDeltaX - motionY);
//			// Slog.d(TAG, "moveByY======" + moveByY);
//			// parentView.scrollBy(moveByX, moveByY);
//			// mDeltaY = motionY;
//			// mDeltaX = motionX;
//			/**
//			 * 此处为scrollTo的手指移动方案 主要是根据手指的每次的移动比例来计算实际view的根据宽高的移动比例
//			 */
//
//			// TODO Auto-generated method stub
//			float percent = (float) (motionY - firstY) / (float) parentHeight;
//			int moveByY = (int) -(percent * targetPointY + crrentY);
//			int moveByX = (int) -(percent * targetPointX + crrentX);
//			// Slog.d(TAG, "srcPointY ======" + targetPointY + "  moveByY "
//			// + moveByY + " srcPointX  ===>" + targetPointX
//			// + " moveByX  " + moveByX);
//
//			/**
//			 * 右边小窗口区域边界判断
//			 */
//			if (Math.abs(moveByX) > targetPointX) {
//				moveByX = -targetPointX;
//			}
//			if (Math.abs(moveByY) > targetPointY) {
//				moveByY = -targetPointY;
//			}
//			if (moveByX > 0) {
//				moveByX = 0;
//			}
//			if (moveByY > 0) {
//				moveByY = 0;
//			}
//
//			/**
//			 * 计算窗口大小
//			 */
//			int newW = parentWidth + moveByX;
//			int newH = newW / 16 * 9;
//			Slog.d(TAG, "   targetPointW " + targetWidth + "  newW " + newW
//					+ " moveByX " + moveByX + " currentW " + currentW);
//			playerScaleView.setW(newW);
//			playerScaleView.setH(newH);
//			parentView.scrollTo(moveByX, moveByY);
//			return true;
//		} else if (event.getAction() == MotionEvent.ACTION_UP) {
//			// if (Math.abs((int) event.getRawX() - firstX) > (Math
//			// .abs((int) event.getRawY() - firstY))) {
//			// Slog.d("ACTION_UP", "->" + "move x");
//			// return true;
//			// }
//			if (System.currentTimeMillis() - touchTime < 100
//					&& crrentY == targetPointY) {
//				mScroller
//						.startScroll(-crrentX, -crrentY, crrentX, crrentY, 300);
//				postInvalidate();
//				return true;
//			}
//			startAlpha();
//			final VelocityTracker velocityTracker = mVelocityTracker;
//			velocityTracker.computeCurrentVelocity(1000); // 1秒钟会跑多少像素
//			int velocityX = (int) Math.abs(velocityTracker.getXVelocity()); // 根据设置的时�?
//			Slog.d("ACTION_UP", " velocityX ->" + velocityX);
//			/**
//			 * 此处初始作品如果是0 就说明是从上向下拉 这个时候 下拉的长度大于3分之一就自动向下
//			 * 
//			 * 
//			 * 反之就是向上拉
//			 */
//			if (crrentY == 0 || crrentY < targetPointY / 3) {
//				Slog.d("ACTION_UP", "->" + "yes " + crrentY);
//				crrentY = getCurrentViewCoorX(1);
//				crrentX = getCurrentViewCoorX(0);
//
//				if (crrentY > targetPointY / 2 || velocityX > 1000) {
//					mScroller.startScroll(-crrentX, -crrentY, crrentX
//							- targetPointX, crrentY - targetPointY, 300);
//				} else {
//					mScroller.startScroll(-crrentX, -crrentY, crrentX, crrentY,
//							300);
//				}
//			} else {
//				Slog.d("ACTION_UP", "->" + "else" + crrentY);
//				crrentY = getCurrentViewCoorX(1);
//				crrentX = getCurrentViewCoorX(0);
//				if (crrentY < targetPointY - targetPointY / 2
//						|| velocityX > 1000) {
//					mScroller.startScroll(-crrentX, -crrentY, crrentX, crrentY,
//							300);
//				} else {
//					mScroller.startScroll(-crrentX, -crrentY, crrentX
//							- targetPointX, crrentY - targetPointY, 300);
//				}
//			}
//
//			Slog.d("computeScroll", "开始重回");
//			postInvalidate();
//		}
//		return true;
//	}

//	@Override
//	public void computeScroll() {
//		// TODO Auto-generated method stub
//		Slog.d("computeScroll", "->" + mScroller.getCurrX());
//		if (mScroller.computeScrollOffset()) {
//			parentView.scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
//			// handler.sendEmptyMessage(1);
//
//			/**
//			 * 计算窗口大小
//			 */
//			int newW = parentWidth + mScroller.getCurrX();
//			int newH = newW / 16 * 9;
//			Slog.d(TAG, "   targetPointW " + targetWidth + "  newW " + newW
//					+ " moveByX " + mScroller.getCurrX() + " currentW "
//					+ currentW);
//			playerScaleView.setW(newW);
//			playerScaleView.setH(newH);
//			Slog.d("coorx", "->" + mScroller.getCurrX());
//			postInvalidate();
//		}
//		// handler.removeMessages(0);
//		// handler.sendEmptyMessageDelayed(0, 100);
//		super.computeScroll();
//	}

//	void startAlpha() {
//		Slog.d("coorx", parentView.getScrollX());
//		int alphaWidth = parentWidth / 3;
//
//		if (Math.abs(parentView.getScrollX()) > alphaWidth) {
//			this.setAlpha(1.2f - (float) Math.abs(parentView.getScrollX())
//					/ (float) (parentWidth + alphaWidth));
//		} else {
//			this.setAlpha(1.0f);
//		}
//	}

	/**
	 * 获取当前View的坐�?也就�?改父layout
	 * 
	 * @return
	 */
	int getCurrentViewCoorX(int index) {
		int[] coor = new int[2];
		this.getLocationInWindow(coor);
		return coor[index];
	}

}
