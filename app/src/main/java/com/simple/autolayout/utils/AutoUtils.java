package com.simple.autolayout.utils;

import android.view.View;

import com.simple.autolayout.AutoLayoutInfo;
import com.simple.autolayout.attr.Attrs;
import com.simple.autolayout.attr.AutoAttr;
import com.simple.autolayout.config.AutoLayoutConifg;
import com.simple.log.Slog;
import com.simple.multiscreenplay.R;
 
public class AutoUtils {

	/**
	 * 会直接将view的LayoutParams上设置的width，height直接进行百分比处理
	 * 
	 * @param view
	 */
	public static void auto(View view) {
		autoSize(view);
		autoPadding(view);
		autoMargin(view);
		autoTextSize(view, AutoAttr.BASE_DEFAULT);
	}

	/**
	 * @param view
	 * @param attrs
	 *            #Attrs.WIDTH|Attrs.HEIGHT
	 * @param base
	 *            AutoAttr.BASE_WIDTH|AutoAttr.BASE_HEIGHT|AutoAttr.BASE_DEFAULT
	 */
	public static void auto(View view, int attrs, int base) {
		AutoLayoutInfo autoLayoutInfo = AutoLayoutInfo.getAttrFromView(view,
				attrs, base);
		if (autoLayoutInfo != null)
			autoLayoutInfo.fillAttrs(view);
	}

	public static void autoTextSize(View view) {
		auto(view, Attrs.TEXTSIZE, AutoAttr.BASE_DEFAULT);
	}

	public static void autoTextSize(View view, int base) {
		auto(view, Attrs.TEXTSIZE, base);
	}

	public static void autoMargin(View view) {
		auto(view, Attrs.MARGIN, AutoAttr.BASE_DEFAULT);
	}

	public static void autoMargin(View view, int base) {
		auto(view, Attrs.MARGIN, base);
	}

	public static void autoPadding(View view) {
		auto(view, Attrs.PADDING, AutoAttr.BASE_DEFAULT);
	}

	public static void autoPadding(View view, int base) {
		auto(view, Attrs.PADDING, base);
	}

	public static void autoSize(View view) {
		auto(view, Attrs.WIDTH | Attrs.HEIGHT, AutoAttr.BASE_DEFAULT);
	}

	public static void autoSize(View view, int base) {
		auto(view, Attrs.WIDTH | Attrs.HEIGHT, base);
	}

	public static boolean autoed(View view) {
		Object tag = view.getTag(R.id.id_tag_autolayout_size);
		if (tag != null)
			return true;
		view.setTag(R.id.id_tag_autolayout_size, "Just Identify");
		return false;
	}

	public static float getPercentWidth1px() {
		int screenWidth = AutoLayoutConifg.getInstance().getScreenWidth();
		int designWidth = AutoLayoutConifg.getInstance().getDesignWidth();
		return 1.0f * screenWidth / designWidth;
	}

	public static float getPercentHeight1px() {
		int screenHeight = AutoLayoutConifg.getInstance().getScreenHeight();
		int designHeight = AutoLayoutConifg.getInstance().getDesignHeight();
		return 1.0f * screenHeight / designHeight;
	}

	public static int getPercentWidthSize(int val) {
		int screenWidth = AutoLayoutConifg.getInstance().getScreenWidth();
		int designWidth = AutoLayoutConifg.getInstance().getDesignWidth();

		return (int) (val * 1.0f / designWidth * screenWidth);
	}

	public static int getPercentWidthSizeBigger(int val) {
		Slog.d("atuoutils", "=======>" + val);
		
		int screenWidth = AutoLayoutConifg.getInstance().getScreenWidth();
		int screenheight = AutoLayoutConifg.getInstance().getScreenHeight();
		int designWidth = AutoLayoutConifg.getInstance().getDesignWidth();
		int designHight = AutoLayoutConifg.getInstance().getDesignHeight();
		int valResult = 0;
//		if (screenWidth < screenheight) {
//			if (val == 1920) {
//				valResult = screenheight;
//			} else if (val == 1080) {
//				valResult = AutoLayoutConifg.getInstance().getScreenWidth();
//			} else {
//				int res = val * screenheight;
//				if (res % designHight == 0) {
//					valResult = res / designWidth;
//				} else {
//					valResult = res / designWidth + 1;
//				}
//			}
//		} else {
//			if (val == 1920) {
//				valResult = screenWidth;
//			} else if (val == 1080) {
//				valResult = AutoLayoutConifg.getInstance().getScreenHeight();
//			} else {
				int res = val * screenWidth;
				if (res % designWidth == 0) {
					valResult = res / designWidth;
				} else {
					valResult = res / designWidth + 1;
				}
//			}
//		}
		return valResult;
	}

	public static int getPercentHeightSizeBigger(int val) {
		int screenWidth = AutoLayoutConifg.getInstance().getScreenWidth();
		int screenHeight = AutoLayoutConifg.getInstance().getScreenHeight();
		int designHeight = AutoLayoutConifg.getInstance().getDesignHeight();
		int designWidth = AutoLayoutConifg.getInstance().getDesignWidth();
		int valResult = 0;
		/**
		 * 此处当宽度小于高度的时候 说明设备的实际显示方式是竖屏
		 * 
		 * 所以此处就需要根据宽小于高的方式重新计算各view的时间距离和大小
		 */
//		if (screenWidth < screenHeight) {
//			if (val == 1920) {
//				valResult = screenHeight;
//			} else if (val == 1080) {
//				valResult = AutoLayoutConifg.getInstance().getScreenWidth();
//			} else {
//				int res = val * screenWidth;
//				if (res % designWidth == 0) {
//					valResult = res / designHeight;
//				} else {
//					valResult = res / designHeight + 1;
//				}
//			}
//		} else {
//			if (val == 1080) {
//				valResult = screenHeight;
//			} else if (val == 1920) {
//				valResult = AutoLayoutConifg.getInstance().getScreenWidth();
//			} else {
				int res = val * screenHeight;
				if (res % designHeight == 0) {
					valResult = res / designHeight;
				} else {
					valResult = res / designHeight + 1;
				}
//			}
//		}
		Slog.d("atuoutils", "getPercentHeightSizeBigger =======>" + val +"  === >" + valResult);
		return valResult;
	}

	// public static int getPercentHeightSize(int val) {
	// int screenHeight = AutoLayoutConifg.getInstance().getScreenHeight();
	// int designHeight = AutoLayoutConifg.getInstance().getDesignHeight();
	//
	// return (int) (val * 1.0f / designHeight * screenHeight);
	// }
}
