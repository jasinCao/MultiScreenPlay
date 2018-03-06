package com.simple.view;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.SurfaceTexture;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnVideoSizeChangedListener;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.TextureView;
import android.view.ViewGroup;
import android.view.TextureView.SurfaceTextureListener;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.LinearInterpolator;
import android.widget.Scroller;

import com.simple.log.Slog;
import com.simple.utils.Consts;
import com.simple.utils.DataManager;

public class VideoPlayerView extends TextureView implements
		SurfaceTextureListener, OnVideoSizeChangedListener,
		IPlayerViewController {

	private ObjectAnimator animatorX, animatorY, animatorH, animatorW;

	private AnimatorSet set;

	private int w;

	private int h;

	private int screenWidth, screenHeight;

	private int animDuration = 0;
	// float screenHeightPercent;
	// float screenWidthPercent;
	private MediaPlayerRunnable mediaPlayerRunnable;

	private static final String TAG = "VideoPlayerView";

	private String currPath;

	private IPlayerListener listener;

	private DataManager dataManager = DataManager.getInstance();

	private Scroller mScroller;

	public VideoPlayerView(Context context) {
		super(context);
		init();
	}

	public VideoPlayerView(Context context, AttributeSet paramAttributeSet) {
		super(context, paramAttributeSet);
		init();
	}

	public VideoPlayerView(Context context, AttributeSet paramAttributeSet,
			int paramInt) {
		super(context, paramAttributeSet, paramInt);
		init();
	}

	private void init() {
		mScroller = new Scroller(getContext(), new LinearInterpolator());
		screenWidth = dataManager.getIntData(Consts.SCREENWIDTH_NOTITLE_BAR);
		screenHeight = dataManager.getIntData(Consts.SCREENHEIGHT_NOTITLE_BAR);
		// screenHeightPercent = (float) screenHeight / Consts.DESGIN_HEIGHT;
		// screenWidthPercent = (float) screenWidth / Consts.DESGIN_WIDTH;
		setSurfaceTextureListener(this);
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
		handler.removeMessages(300);
		handler.sendEmptyMessageDelayed(300, 300);
	}

	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			int initHight = screenWidth / 16 * 9;
			screenHeight = initHight;
			adjustPosition(screenWidth, initHight, 0, 0);
		};
	};

	private ViewGroup parentView;

	private int parentWidth, parentHeight, srcPointX, srcPointY;

	@Override
	protected void onLayout(boolean changed, int left, int top, int right,
			int bottom) {
		// TODO Auto-generated method stub
		super.onLayout(changed, left, top, right, bottom);
		Slog.d(TAG, left + " " + top + " " + right + "  " + changed);
		if (parentView == null) {
			parentView = (ViewGroup) this.getParent();
			parentWidth = parentView.getWidth();
			parentHeight = parentView.getHeight();
			int srcWidth = parentWidth / 5 * 2;
			srcPointX = parentWidth - srcWidth;
			int srcHeiht = srcWidth / 16 * 9; // 缩放后的视频窗口大小保持原有比例
			srcPointY = parentHeight - srcHeiht - 200; // 计算右边缩小Y点的位置
		}
	}

	@Override
	protected void onFinishInflate() {
		// TODO Auto-generated method stub
		super.onFinishInflate();
	}

	public void setPlayerListener(IPlayerListener listener) {
		this.listener = listener;
		if (mediaPlayerRunnable != null) {
			mediaPlayerRunnable.setPlayerListener(listener);
		}
	}

	public void adjustPosition(int w, int h, float x, float y) {
		Slog.d(TAG, "=>" + w + " " + h);
		if (getWidth() == w && h == getH()) {
			animDuration = 0;
		}
		if (set != null) {
			set.end();
		}
		animatorX.setFloatValues(x);
		animatorY.setFloatValues(y);
		animatorH.setIntValues(h);
		animatorW.setIntValues(w);
		set.setDuration(animDuration);
		set.start();
		ObjectAnimator.ofFloat(this, "alpha", 0f, 1f).setDuration(400).start();
	}

	@Override
	public void stop() {
		if (mediaPlayerRunnable == null) {
			return;
		}
		mediaPlayerRunnable.stop();
	}

	public int getW() {
		return w;
	}

	public void setW(int mWidth) {
		this.w = mWidth;
		LayoutParams layoutParams = getLayoutParams();
		layoutParams.width = mWidth;
		setLayoutParams(layoutParams);
	}

	public int getH() {
		return h;
	}

	public void setH(int mHeight) {
		this.h = mHeight;
		LayoutParams layoutParams = getLayoutParams();
		layoutParams.height = mHeight;
		setLayoutParams(layoutParams);
	}

	@Override
	public void startPlay(String path) {
		if (mediaPlayerRunnable == null) {
			return;
		}
		currPath = path;
		mediaPlayerRunnable.play(path);
	}

	@Override
	public void pause() {
		if (mediaPlayerRunnable == null) {
			return;
		}
		mediaPlayerRunnable.pause();
	}

	@Override
	public void resume() {
		if (mediaPlayerRunnable == null) {
			return;
		}
		mediaPlayerRunnable.resume();
	}

	@Override
	public void seek(int mSec) {
		if (mediaPlayerRunnable == null) {
			return;
		}
		mediaPlayerRunnable.seek(mSec);
	}

	@Override
	public boolean isPalying() {
		if (mediaPlayerRunnable == null) {
			return false;
		}
		return mediaPlayerRunnable.isPalying();
	}

	@Override
	public boolean isPause() {
		if (mediaPlayerRunnable == null) {
			return false;
		}

		return mediaPlayerRunnable.isPause();
	}

	@Override
	public void release() {
		if (mediaPlayerRunnable == null) {
			return;
		}
		mediaPlayerRunnable.release();
	}

	@Override
	public int getDuration() {
		if (mediaPlayerRunnable == null) {
			return 0;
		}
		return mediaPlayerRunnable.getDuration();
	}

	@Override
	public int getCurrentPos() {
		if (mediaPlayerRunnable == null) {
			return 0;
		}
		return mediaPlayerRunnable.getCurrentPos();
	}

	@Override
	public void onSeek() {

	}

	@Override
	public void onSurfaceTextureAvailable(SurfaceTexture arg0, int arg1,
			int arg2) {
		// TODO Auto-generated method stub
		Slog.d(TAG, "onSurfaceTextureAvailable");
		mediaPlayerRunnable = new MediaPlayerRunnable(arg0, this, getContext());
		mediaPlayerRunnable.setPlayerListener(listener);
		post(mediaPlayerRunnable);
	}

	@Override
	public boolean onSurfaceTextureDestroyed(SurfaceTexture arg0) {
		// TODO Auto-generated method stub
		Slog.d(TAG, "onSurfaceTextureDestroyed");
		return false;
	}

	@Override
	public void onSurfaceTextureSizeChanged(SurfaceTexture arg0, int arg1,
			int arg2) {
		// TODO Auto-generated method stub
		Slog.d(TAG, "onSurfaceTextureSizeChanged");

	}

	@Override
	public void onSurfaceTextureUpdated(SurfaceTexture arg0) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onVideoSizeChanged(MediaPlayer arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		videoSizeAutoAdjust(arg1, arg2);
	}

	private void videoSizeAutoAdjust(final int videoWidth, final int videoHeight) {
		if (listener != null) {
			listener.onPlayStart();
		}
		if (videoHeight > videoWidth) {
			post(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					post(new Runnable() {
						public void run() {
							int changeHeight = screenHeight;
							float percent = (float) screenHeight / videoHeight;
							int changeWidth = (int) ((float) videoWidth * percent);

							if (changeWidth > screenWidth) {
								changeWidth = screenWidth;
								percent = (float) screenWidth / videoWidth;
								changeHeight = (int) ((float) videoHeight * percent);
							}

							final int x = screenWidth / 2 - changeWidth / 2;
							final int y = screenHeight / 2 - changeHeight / 2;
							Slog.d("vplayer", " w < h=========>" + videoWidth
									+ "  " + videoHeight + "  " + changeWidth
									+ " " + changeHeight);
							adjustPosition(changeWidth, changeHeight, x, y);
						}
					});

				}
			});
		} else {
			post(new Runnable() {
				public void run() {
					// 960 545
					int changeWidth = screenWidth;
					float percent = (float) screenWidth / videoWidth;
					int changeHeight = (int) ((float) videoHeight * percent);
					if (changeHeight > screenHeight) {
						changeHeight = screenHeight;
						percent = (float) screenHeight / (float) videoHeight;
						changeWidth = (int) ((float) videoWidth * percent);
					}
					final int x = screenWidth / 2 - changeWidth / 2;
					final int y = screenHeight / 2 - changeHeight / 2;
					Slog.d("vplayer", " w > h=========>" + videoWidth + "  "
							+ videoHeight + "  " + changeWidth + " "
							+ changeHeight);
					adjustPosition(changeWidth, changeHeight, x, y);
				}
			});
		}

	}

	@Override
	public void surfaceGone() {
		// TODO Auto-generated method stub
		// this.setVisibility(View.INVISIBLE);
		ObjectAnimator.ofFloat(this, "alpha", 0f, 0f).setDuration(200).start();
		// this.setVisibility(View.INVISIBLE);
	}

	@Override
	public void setSurfaceCtrlView(View v) {
		// TODO Auto-generated method stub

	}

	@Override
	public void play() {
		// TODO Auto-generated method stub
		if (mediaPlayerRunnable == null) {
			return;
		}
		mediaPlayerRunnable.play(currPath);
	}

	@Override
	public void setCoustomScreenSize(int w, int h) {
		// TODO Auto-generated method stub

	}

	private float firstY, firstX;

	/**
	 * 此变量专门用于滑动使用 不能直接使用fistY的原因是因为fistY在判断手指滑动方向的时候会导致 计算不精准从而导致 scrollby的指不均匀
	 */
	private float mDeltaY, mDeltaX;
	int crrentY, crrentX;
//
//	@Override
//	public boolean onTouchEvent(MotionEvent event) {
//		// TODO Auto-generated method stub
//		float motionY = event.getRawY();
//		float motionX = event.getRawX();
//		if (event.getAction() == MotionEvent.ACTION_DOWN) {
//			firstY = event.getRawY();
//			firstX = event.getRawX();
//			mDeltaY = firstY;
//			mDeltaX = firstX;
//			/**
//			 * 用scroollto 来实现移动计算
//			 */
//			crrentY = getCurrentViewCoorX(1);
//			crrentX = getCurrentViewCoorX(0);
//		} else if (event.getAction() == MotionEvent.ACTION_MOVE) {
//			if (Math.abs((int) event.getRawX() - firstX) > (Math
//					.abs((int) event.getRawY() - firstY))) {
//				return false;
//			}
//			Slog.d(TAG, "mt======" + motionY);
//			Slog.d(TAG, "fy======" + firstY);
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
//			int moveByY = (int) -(percent * srcPointY + crrentY);
//			int moveByX = (int) -(percent * srcPointX + crrentX);
//			Slog.d(TAG, "srcPointY ======" + srcPointY + "  moveByY " + moveByY
//					+ " srcPointX  ===>" + srcPointX + " moveByX  " + moveByX);
//			/**
//			 * 右边小窗口区域边界判断
//			 */
//			if (Math.abs(moveByX) > srcPointX) {
//				moveByX = -srcPointX;
//			}
//			if (Math.abs(moveByY) > srcPointY) {
//				moveByY = -srcPointY;
//			}
//			if (moveByX > 0) {
//				moveByX = 0;
//			}
//			if (moveByY > 0) {
//				moveByY = 0;
//			}
//			parentView.scrollTo(moveByX, moveByY);
//			return true;
//		} else if (event.getAction() == MotionEvent.ACTION_UP) {
//			if (Math.abs((int) event.getRawX() - firstX) > (Math
//					.abs((int) event.getRawY() - firstY))) {
//				Slog.d("computeScroll", "->" + "move x");
//				return true;
//			}
//			crrentY = getCurrentViewCoorX(1);
//			crrentX = getCurrentViewCoorX(0);
//			Slog.d("computeScroll", "->" + "crrentY = >" + crrentY + " crrentX "
//					+ crrentX + (crrentY > srcPointY / 2));
//			if (crrentY > srcPointY / 2) {
//				mScroller.startScroll(-crrentX, -crrentY, crrentX - srcPointX,
//						crrentY - srcPointY, 300);
//			} else {
//				mScroller
//						.startScroll(-crrentX, -crrentY, crrentX, crrentY, 300);
//			}
//			Slog.d("computeScroll","开始重回");
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
//			Slog.d("coorx", "->" + mScroller.getCurrX());
//			postInvalidate();
//		}
//		// handler.removeMessages(0);
//		// handler.sendEmptyMessageDelayed(0, 100);
//		super.computeScroll();
//	}

	
	@Override
	public void computeScroll() {
		// TODO Auto-generated method stub
		Slog.d("computeScroll", ">>>>>>" + mScroller.getCurrX());
		super.computeScroll();
	}
	
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
