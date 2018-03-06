package com.simple.view;

import com.simple.autolayout.AutoRelativeLayout;
import com.simple.log.Slog;

import android.R;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.Shader.TileMode;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Scroller;

/**
 * 
 * @author GiaourCao
 * 
 */
public class SlidingFinishPageView extends AutoRelativeLayout {

	private ViewGroup parentView;
	private int parentWidth;
	private Scroller scroller;
	private Activity activity;
	private final int WIDTHSCALE = 30;
	private final float ALPHASCALE = 1.2f;
	private int mDeltaX;
	private final int SNAP_VELOCITY = 500;
	/**
	 * 是否关闭页面
	 */
	private boolean isClose;
	/**
	 * 速率跟踪�?
	 */
	private VelocityTracker mVelocityTracker = null;

	public SlidingFinishPageView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		scroller = new Scroller(getContext(), new LinearInterpolator());
	}

	public SlidingFinishPageView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		scroller = new Scroller(getContext(), new LinearInterpolator());
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
		if (parentView == null) {
			parentView = (ViewGroup) this.getParent();
			parentWidth = parentView.getWidth();
			ImageView shadowView = new ImageView(getContext());
			shadowView.setImageBitmap(drawShadowRectBitmap());

			FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
					LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			params.leftMargin = -(parentWidth / WIDTHSCALE);
			params.topMargin = 0;
			parentView.addView(shadowView, params);
		}

	}

	public void setActivity(Activity activity) {
		if (activity == null) {
			throw new IllegalArgumentException("activity is null");
		}
		this.activity = activity;
		activity.setTheme(R.style.Theme_Translucent_NoTitleBar_Fullscreen);
	}

	private int firstY, firstX;

	@Override
	public boolean onInterceptTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		int motionX = (int) event.getRawX();
		if (mVelocityTracker == null) {
			mVelocityTracker = VelocityTracker.obtain();
		}
		mVelocityTracker.addMovement(event);
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			mDeltaX = motionX;
			firstX = motionX;
			firstY = (int) event.getRawY();
		} else if (event.getAction() == MotionEvent.ACTION_MOVE) {
			/**
			 * 此处为判断是上下滑动还是左右滑动
			 */
			if ((motionX - firstX) > (Math.abs((int) event.getRawY() - firstY))) {
				startAlpha();
				// 此处为界面跟随手指�?移动
				int currentX = getCurrentViewCoorX();
				int moveByX = (int) (mDeltaX - motionX);
				// 此处为判断手指是�?��个方向滑�?避免出现逆向闪动
				if (mDeltaX > motionX) {
					if (currentX == 0) {
						moveByX = 0;
					} else if (currentX < 0) {
						moveByX = currentX;
					}
				}
				parentView.scrollBy(moveByX, 0);
				mDeltaX = motionX;
				return true;
			}

		} else if (event.getAction() == MotionEvent.ACTION_UP) {

			final VelocityTracker velocityTracker = mVelocityTracker;
			velocityTracker.computeCurrentVelocity(1000); // 1秒钟会跑多少像素
			int velocityX = (int) velocityTracker.getXVelocity(); // 根据设置的时�?
			// 计算滑动时间的像素比�?
			// 此处获取移动的view的真实坐�?因为如果直接获取手指的坐标的�?会根据手指的实际位置移动页面会出现页面跳动的效果

			int moveViewX = getCurrentViewCoorX();
			if ((motionX - firstX) > (Math.abs((int) event.getRawY() - firstY))) {
				if (velocityX > SNAP_VELOCITY) {
					isClose = true;
					scroller.startScroll(-moveViewX, 0,
							moveViewX - parentWidth, 0, 300);
					postInvalidate();
					return false;
				}
			}
			if (moveViewX > parentWidth / 2) {
				isClose = true;
				scroller.startScroll(-moveViewX, 0, moveViewX - parentWidth, 0,
						300);
			} else {
				isClose = false;
				scroller.startScroll(-moveViewX, 0, moveViewX, 0, 300);
			}
			postInvalidate();
		}

		return false;
	}

	private boolean onTouch;

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		onTouch = true;
		int motionX = (int) event.getRawX();
		if (mVelocityTracker == null) {
			mVelocityTracker = VelocityTracker.obtain();
		}else{
			mVelocityTracker.addMovement(event); 
		}
		mVelocityTracker.addMovement(event);
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			mDeltaX = motionX;
			firstX = motionX;
			firstY = (int) event.getRawY();
		} else if (event.getAction() == MotionEvent.ACTION_MOVE) {
			if ((motionX - firstX) < (Math.abs((int) event.getRawY() - firstY))) {
				return false;
			}

			startAlpha();
			// 此处为界面跟随手指�?移动
			int currentX = getCurrentViewCoorX();
			int moveByX = (int) (mDeltaX - motionX);

			// 此处为判断手指是�?��个方向滑�?避免出现逆向闪动
			if (mDeltaX > motionX) {
				if (currentX == 0) {
					moveByX = 0;
				} else if (currentX < 0) {
					moveByX = currentX;
				}
			}
			parentView.scrollBy(moveByX, 0);
			mDeltaX = motionX;

		} else if (event.getAction() == MotionEvent.ACTION_UP) {
//			mVelocityTracker.computeCurrentVelocity(1, (float)0.01);
			final VelocityTracker velocityTracker = mVelocityTracker;
			velocityTracker.computeCurrentVelocity(1000); // 1秒钟会跑多少像素
			int velocityX = (int) velocityTracker.getXVelocity(); // 根据设置的时�?
																	// 计算滑动时间的像素比�?
			// 此处获取移动的view的真实坐�?因为如果直接获取手指的坐标的�?会根据手指的实际位置移动页面会出现页面跳动的效果

			int moveViewX = getCurrentViewCoorX();
			if ((motionX - firstX) > (Math.abs((int) event.getRawY() - firstY))) {
				if (velocityX > SNAP_VELOCITY) {
					isClose = true;
					scroller.startScroll(-moveViewX, 0,
							moveViewX - parentWidth, 0, 300);
					postInvalidate();
					return false;
				}
			}

			if (moveViewX > parentWidth / 2) {
				isClose = true;
				scroller.startScroll(-moveViewX, 0, moveViewX - parentWidth, 0,
						300);
			} else {
				isClose = false;
				scroller.startScroll(-moveViewX, 0, moveViewX, 0, 300);
			}
			postInvalidate();
		}
		return true;
	}

	/**
	 * 获取当前View的坐�?也就�?改父layout
	 * 
	 * @return
	 */
	int getCurrentViewCoorX() {
		int[] coor = new int[2];
		this.getLocationInWindow(coor);
		return coor[0];
	}

	/**
	 * 当手指松�?��据滑动的距离计算�?关闭还是 打开
	 * 
	 * @param motionX
	 */
	void startScrollSubView() {
		// 此处获取移动的view的真实坐�?因为如果直接获取手指的坐标的�?会根据手指的实际位置移动页面会出现页面跳动的效果
		int[] p = new int[2];
		this.getLocationInWindow(p);
		int moveViewX = p[0];
		if (moveViewX > parentWidth / 2) {
			isClose = true;
			scroller.startScroll(-moveViewX, 0, moveViewX - parentWidth, 0, 300);
		} else {
			isClose = false;
			scroller.startScroll(-moveViewX, 0, moveViewX, 0, 300);
		}
		postInvalidate();
	}

	/**
	 * 创建�?��阴影方块bitmap
	 * 
	 * @return
	 */
	Bitmap drawShadowRectBitmap() {
		/**
		 * 创建�?��空的bitmap
		 */
		Bitmap bitmap = Bitmap.createBitmap((int) this.getWidth() / WIDTHSCALE,
				(int) this.getHeight(), Config.ARGB_8888);
		Canvas canvas = new Canvas(bitmap);
		/**
		 * 画一个黑色方�?
		 */
		Paint rectPaint = new Paint();
		rectPaint.setColor(Color.BLACK);
		Rect rect = new Rect(0, 0, this.getWidth() / WIDTHSCALE,
				this.getHeight());
		canvas.drawRect(rect, rectPaint);

		/**
		 * 针对黑色方块的线性渐�?
		 */
		Paint shaderPaint = new Paint();
		LinearGradient shader = new LinearGradient(
				this.getWidth() / WIDTHSCALE, 0, 0, 0, 0x70000000, 0x00000000,
				TileMode.MIRROR);
		shaderPaint.setShader(shader);
		shaderPaint.setXfermode(new PorterDuffXfermode(Mode.DST_IN));
		canvas.drawRect(0, 0, this.getWidth() / WIDTHSCALE, this.getHeight(),
				shaderPaint);
		return bitmap;
	}

	@Override
	protected void onFinishInflate() {
		// TODO Auto-generated method stub

		super.onFinishInflate();
	}

	@Override
	public void computeScroll() {
		// TODO Auto-generated method stub
		if (scroller.computeScrollOffset()) {
			parentView.scrollTo(scroller.getCurrX(), 0);
			// handler.sendEmptyMessage(1);
			startAlpha();
			if (isClose) {
				activity.finish();
			}
			Slog.d("coorx", "->" + scroller.getCurrX());
			postInvalidate();
		}
//		handler.removeMessages(0);
//		handler.sendEmptyMessageDelayed(0, 100);
		super.computeScroll();
	}

	private boolean isUserAlpha = true;

	public void setUseAlpha(boolean isUserAlpha) {
		this.isUserAlpha = isUserAlpha;
	}

	void startAlpha() {
		if (isUserAlpha) {
			Slog.d("coorx", parentView.getScrollX());
			int alphaWidth = parentWidth / 3;
 
			if (Math.abs(parentView.getScrollX()) > alphaWidth) {
				
				Log.d("coor2","==" + (ALPHASCALE
						- (float) Math.abs(parentView.getScrollX())
						/ (float) (parentWidth + alphaWidth)));
				this.setAlpha(ALPHASCALE
						- (float) Math.abs(parentView.getScrollX())
						/ (float) (parentWidth + alphaWidth));
			} else {
				this.setAlpha(1.0f);
			}
		}
	}

//	/**
//	 * 界面刷新handler 非常有必�?
//	 */
//	Handler handler = new Handler() {
//
//		public void handleMessage(android.os.Message msg) {
//			if (msg.what == 1) {
//				startAlpha();
//				return;
//			}
//			if (isClose) {
//				activity.finish();
//			} else {
//				System.out.println("=====================hand");
//				if (onTouch) {
//					onTouch = false;
//					postInvalidate();
//				}
//			}
//		};
//
//	};

}
