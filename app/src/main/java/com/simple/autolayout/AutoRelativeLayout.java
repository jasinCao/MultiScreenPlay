package com.simple.autolayout;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.simple.autolayout.utils.AutoLayoutHelper;
import com.simple.log.Slog;
import com.simple.multiscreenplay.R;

public class AutoRelativeLayout extends RelativeLayout {
	private final AutoLayoutHelper mHelper = new AutoLayoutHelper(this);

	public AutoRelativeLayout(Context context) {
		super(context);
	}

	public AutoRelativeLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public AutoRelativeLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	// @TargetApi(Build.VERSION_CODES.LOLLIPOP)
	public AutoRelativeLayout(Context context, AttributeSet attrs,
			int defStyleAttr, int defStyleRes) {
		// super(context, attrs, defStyleAttr, defStyleRes);
		super(context, attrs, defStyleAttr);
	}

	@Override
	public LayoutParams generateLayoutParams(AttributeSet attrs) {
		return new LayoutParams(getContext(), attrs);
	}


	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		if (!isInEditMode())
			mHelper.adjustChildren();
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}

	@Override
	protected void onLayout(boolean changed, int left, int top, int right,
			int bottom) {
		super.onLayout(changed, left, top, right, bottom);
	}

	public static class LayoutParams extends RelativeLayout.LayoutParams
			implements AutoLayoutHelper.AutoLayoutParams {
		private AutoLayoutInfo mAutoLayoutInfo;

		public LayoutParams(Context c, AttributeSet attrs) {
			super(c, attrs);
			mAutoLayoutInfo = AutoLayoutHelper.getAutoLayoutInfo(c, attrs);
		}

		public LayoutParams(int width, int height) {
			super(width, height);
		}

		public LayoutParams(ViewGroup.LayoutParams source) {
			super(source);
		}

		public LayoutParams(MarginLayoutParams source) {
			super(source);
		}

		@Override
		public AutoLayoutInfo getAutoLayoutInfo() {
			return mAutoLayoutInfo;
		}

	}
}
