package com.simple.view;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import android.content.Context;
import android.graphics.SurfaceTexture;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnBufferingUpdateListener;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnInfoListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.media.MediaPlayer.OnSeekCompleteListener;
import android.media.MediaPlayer.OnVideoSizeChangedListener;
import android.os.Handler;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;

import com.simple.log.Slog;

public class MediaPlayerRunnable implements Runnable,
		OnBufferingUpdateListener, OnErrorListener, OnCompletionListener,
		OnPreparedListener, OnSeekCompleteListener, OnInfoListener {
	private final int SEND_TIME_MSG = 1234;
	private final int CHECKPLAY_STATE_MSG = 1236;
	private final int SEND_COMPLATE_MSG = 1235;
	float screenHeightPercent;
	float screenWidthPercent;

	private static final String TAG = "MediaControler";

	private static MediaPlayer mediaPlayer = null;

	private IPlayerListener playerListener = null;

	private boolean isPrepared = false;

	private boolean isPause = false;

	private String currPath;

	private SurfaceTexture surfaceText;

	private SurfaceHolder surfaceHolder;

	private OnVideoSizeChangedListener onVideoSizeChangedListener;

	private boolean isPlayMisic = false;

	private Context context;

	public MediaPlayerRunnable(Context context) {
		// TODO Auto-generated constructor stub
		this.context = context;
		isPlayMisic = true;
	}

	public MediaPlayerRunnable(SurfaceTexture surfaceText,
			OnVideoSizeChangedListener onVideoSizeChangedListener,
			Context context) {
		// TODO Auto-generated constructor stub
		this.context = context;
		this.surfaceText = surfaceText;
		this.onVideoSizeChangedListener = onVideoSizeChangedListener;
	}

	public MediaPlayerRunnable(SurfaceHolder surfaceHolder,
			OnVideoSizeChangedListener onVideoSizeChangedListener,
			Context context) {
		// TODO Auto-generated constructor stub
		this.context = context;
		this.surfaceHolder = surfaceHolder;
		this.onVideoSizeChangedListener = onVideoSizeChangedListener;
	}

	public void setOnInfoListener(OnInfoListener listener) {
		if (null != mediaPlayer) {
			mediaPlayer.setOnInfoListener(listener);
		}
	}

	public void setSurfaceHolder(SurfaceHolder holder) {
		if (mediaPlayer != null) {
			try {
				surfaceHolder = holder;
				mediaPlayer.setDisplay(holder);
			} catch (Exception e) {
				// TODO: handle exception
				Log.d(TAG, "=========>" + e.toString());
			}

		}
	}

	@Override
	public void onPrepared(MediaPlayer mp) {

		Log.d(TAG, "onPrepared ok");
		try {
			// isPlaying = true;
			isPrepared = true;
			isPause = false;
			// mediaPlayer.setLooping(true);
			System.out.println("=====================>onprepared start play");
			mediaPlayer.start();
			mediaPlayer.setOnCompletionListener(this);
			if (isPlayMisic) {
				return;
			}
			handler.removeMessages(SEND_TIME_MSG);
			if (surfaceHolder != null) {
				mediaPlayer.setDisplay(surfaceHolder);
				handler.sendEmptyMessageDelayed(SEND_TIME_MSG, 2000);
			} else {
				handler.sendEmptyMessage(SEND_TIME_MSG);
			}
			Log.d(TAG, "start play");
		} catch (Exception e) {
			Log.d(TAG, "start:" + e.toString());
		}
		if (playerListener != null) {
			// playerListener.onPlayStart();
		} else {
			Log.d(TAG, "playerListener:" + playerListener);
		}
	}

	public void setPlayerListener(IPlayerListener listener) {
		playerListener = listener;
	}

	@Override
	public void onCompletion(MediaPlayer mp) {
		// if (mp.isPlaying()) {
		// return;
		// }
		handler.removeMessages(SEND_COMPLATE_MSG);
		handler.removeMessages(SEND_TIME_MSG);
		Slog.d("play", "onCompletion   1 " + (playerListener != null));
		if (playerListener != null) {
			Slog.d("play", "onCompletion   2 ");
			playerListener.onPlayCompletion();
		}
	}

	@Override
	public void onBufferingUpdate(MediaPlayer mp, int percent) {
		if (playerListener != null) {
			// playerListener.BufferingUpdate(mp, percent);
		}
	}

	@Override
	public boolean onError(MediaPlayer mp, int what, int extra) {
		Log.e(TAG, "onError:" + what + "--" + extra);
		handler.removeMessages(SEND_COMPLATE_MSG);
		handler.removeMessages(SEND_TIME_MSG);
		return false;
	}

	@Override
	public boolean onInfo(MediaPlayer mp, int what, int extra) {
		Log.d(TAG, "onInfo:" + what + ":" + extra);
		return false;
	}

	@Override
	public void onSeekComplete(MediaPlayer mp) {
		Log.d(TAG, "onSeekComplete");

		if (playerListener != null) {
			playerListener.onSeekComplete();
		}
	}

	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {

			if (CHECKPLAY_STATE_MSG == msg.what) {
				handler.removeMessages(CHECKPLAY_STATE_MSG);
				if (getDuration() - getCurrentPos() < 1000
						&& getCurrentPos() != 0) {
					Log.d("play", "play over do complatetion===============>"
							+ getCurrentPos());
					onCompletion(mediaPlayer);
				}
				return;
			}

			if (msg.what == SEND_TIME_MSG) {
				if (playerListener != null) {
					// int timer = (int) (getCurrentPos() / 1000);
					// int maxLength = (int) getDuration() / 1000;

					Log.d("player", "getDuration===============>"
							+ getDuration());
					Log.d("player", "getCurrentPos===============>"
							+ getCurrentPos());

					if (getDuration() - getCurrentPos() < 1000) {
						handler.sendEmptyMessageDelayed(CHECKPLAY_STATE_MSG,
								5000);
					}
					playerListener.onTimeChange(0, 0);
					handler.removeMessages(SEND_TIME_MSG);
					handler.sendEmptyMessageDelayed(SEND_TIME_MSG, 1000);
				}
			} else if (SEND_COMPLATE_MSG == msg.what) {
				onCompletion(mediaPlayer);
			}

		};
	};

	public void stop() {
		try {
			handler.removeCallbacksAndMessages(null);
			if (mediaPlayer != null) {
				Log.d(TAG, "stop");
				// isPlaying = false;
				mediaPlayer.stop();

			}
		} catch (Exception e) {
			Log.e(TAG, e.toString());
		} finally {
			if (playerListener != null) {
				playerListener.onPlayStop();
			}
		}
	}

	public void play(String path) {
		isPrepared = false;

		// isPlaying = false;
		try {
			if (mediaPlayer != null) {
				// mediaPlayer.stop();
				mediaPlayer.reset();
				// SystemClock.sleep(1000);
			} else {
				createPlayer();
				Log.d("play", "crate player" + path);

			}
		} catch (Exception e) {
			Log.e(TAG, "paly:" + e.toString());
		}

		if (mediaPlayer != null) {
			try {
				Log.d("play", "start play path is ============>" + path);
				mediaPlayer.setOnCompletionListener(null);
				try {
					mediaPlayer.setDataSource(path);
				} catch (Exception e) {
					// TODO: handle exception
					if (playerListener != null) {
						playerListener.onPlayError(path);
					}
				}

				mediaPlayer.prepare();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
				Log.d("play", "error 1 ");
				handler.removeMessages(SEND_COMPLATE_MSG);
				handler.sendEmptyMessage(SEND_COMPLATE_MSG);
			} catch (IllegalStateException e) {
				e.printStackTrace();
				Log.d("play", "play eroor 2 ============>" + e.toString());
				handler.removeMessages(SEND_COMPLATE_MSG);
				handler.sendEmptyMessage(SEND_COMPLATE_MSG);
			} catch (IOException e) {
				e.printStackTrace();
				// handler.removeMessages(SEND_COMPLATE_MSG);
				// handler.sendEmptyMessage(SEND_COMPLATE_MSG);
				Log.d("play", "play eroor 3 ============>" + e.toString());
				if (playerListener != null) {
					playerListener.onPlayError(path);
				}

			}
		}
	}

	public void pause() {
		Log.i(TAG, "pause called isPlaying:");
		handler.removeMessages(SEND_TIME_MSG);
		if (mediaPlayer != null && isPrepared && !isPause) {
			Log.i(TAG, "pause called");
			try {
				// isPlaying = false;
				isPause = true;
				mediaPlayer.pause();
			} catch (Exception e) {
				Log.e(TAG, "pause:" + e.toString());
			} finally {
				if (playerListener != null) {
					playerListener.onPlayPause();
				}
			}
		}
	}

	public void resume() {

		if (mediaPlayer != null && isPrepared && isPause) {
			Log.i(TAG, "resume called");
			try {
				handler.removeMessages(SEND_TIME_MSG);
				handler.sendEmptyMessage(SEND_TIME_MSG);
				// isPlaying = true;
				isPause = false;
				long i = System.currentTimeMillis();
				mediaPlayer.start();
				Log.d("mpt", "" + (System.currentTimeMillis() - i));
				Log.e(TAG, "resume");
			} catch (Exception e) {
				Log.e(TAG, "resume:" + e.toString());
			} finally {
				if (playerListener != null) {
					playerListener.onPlayResume();
				}
			}
		}
	}

	public void seek(int mSec) {
		Log.e(TAG, "seek:" + mSec);
		if (mSec < 0)
			return;
		if (mediaPlayer != null && isPrepared) {
			try {
				int tmp = mediaPlayer.getDuration();
				Log.e(TAG, "tmp tmp:" + tmp);

				// else if ((tmp - mSec) < 2000)
				// {
				// mSec = tmp - 2000;
				// }

				Log.e(TAG, "seek actual:" + mSec);
				mediaPlayer.seekTo(mSec);
				playerListener.onPlayResume();
			} catch (Exception e) {
				Log.e(TAG, "seek:" + e.toString());
			}
		}
	}

	public boolean isPalying() {

		try {
			if (mediaPlayer != null) {
				return mediaPlayer.isPlaying();
			}
		} catch (Exception e) {
			Log.e(TAG, "isPalying: " + e.getMessage(), e);
		}
		return false;
	}

	public boolean isPause() {

		if (mediaPlayer != null && mediaPlayer.isPlaying()) {
			return isPause;
		}
		return false;
	}

	public void release() {
		handler.removeCallbacksAndMessages(null);
		if (mediaPlayer != null) {
			mediaPlayer.reset();
			mediaPlayer.release();
			mediaPlayer = null;
		}
	}

	public int getDuration() {
		if (mediaPlayer != null && isPrepared) {
			try {
				// Log.e(TAG, "getDuration:"+mediaPlayer.getDuration());
				return mediaPlayer.getDuration();
			} catch (Exception e) {
				Log.e(TAG, "getDuration:" + e.toString());
			}
		}
		return 0;
	}

	public int getCurrentPos() {
		if (mediaPlayer != null && isPrepared) {
			try {
				return mediaPlayer.getCurrentPosition();
			} catch (Exception e) {
				Log.e(TAG, "getCurrentPos:" + e.toString());
			}
		}
		return 0;
	}

	public void onDestroy() {
		Log.d(TAG, "onDestroy");
		mediaPlayer.stop();
		mediaPlayer.release();
		mediaPlayer = null;

	}

	public void playControl(int control) {

		Log.e(TAG, "playControl:" + control);
		if (mediaPlayer == null)
			return;
		switch (control) {
		case 1:
			pause();
			break;
		case 2:
			resume();
			break;
		case 3:
			Log.e(TAG, "playControl:stop");
			stop();
			if (playerListener != null) {
				playerListener.onControlStop();
			}
			break;

		default:
			break;
		}
	}

	public void onProgress(int progress) {
		if (playerListener != null) {
			playerListener.onProgress(progress);
		}
	}

	public void onSeek() {
		if (playerListener != null) {
			playerListener.onSeek();
		}
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		createPlayer();
	}

	private void createPlayer() {
		mediaPlayer = getMediaPlayer(context);

		if (surfaceText != null) {
			Surface surface = new Surface(surfaceText);
			mediaPlayer.setSurface(surface);
		}
		mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
		mediaPlayer.setScreenOnWhilePlaying(true);
		if (!isPlayMisic) {
			mediaPlayer.setOnPreparedListener(this);
			mediaPlayer
					.setOnVideoSizeChangedListener(onVideoSizeChangedListener);
		}
		mediaPlayer.setOnBufferingUpdateListener(this);
		mediaPlayer.setOnErrorListener(this);
		mediaPlayer.setOnSeekCompleteListener(this);
		mediaPlayer.setOnInfoListener(this);
		if (playerListener != null) {
			playerListener.onPlayInit();
		}
	}

	public MediaPlayer getMediaPlayer(Context context) {
		MediaPlayer mediaplayer = new MediaPlayer();
		if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.KITKAT) {
			return mediaplayer;
		}
		try {
			Class<?> cMediaTimeProvider = Class
					.forName("android.media.MediaTimeProvider");
			Class<?> cSubtitleController = Class
					.forName("android.media.SubtitleController");
			Class<?> iSubtitleControllerAnchor = Class
					.forName("android.media.SubtitleController$Anchor");
			Class<?> iSubtitleControllerListener = Class
					.forName("android.media.SubtitleController$Listener");
			Constructor constructor = cSubtitleController
					.getConstructor(new Class[] { Context.class,
							cMediaTimeProvider, iSubtitleControllerListener });
			Object subtitleInstance = constructor.newInstance(context, null,
					null);
			Field f = cSubtitleController.getDeclaredField("mHandler");
			f.setAccessible(true);
			try {
				f.set(subtitleInstance, new Handler());
			} catch (IllegalAccessException e) {
				return mediaplayer;
			} finally {
				f.setAccessible(false);
			}
			Method setsubtitleanchor = mediaplayer.getClass().getMethod(
					"setSubtitleAnchor", cSubtitleController,
					iSubtitleControllerAnchor);
			setsubtitleanchor.invoke(mediaplayer, subtitleInstance, null);
		} catch (Exception e) {
		}
		return mediaplayer;
	}

}
