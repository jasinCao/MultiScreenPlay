package com.simple.utils;

import java.lang.reflect.Field;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Point;
import android.os.Build;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;

public class ScreenUtils {

	public static int getStatusBarHeight(Context context) {
		int result = 0;
		try {
			int resourceId = context.getResources().getIdentifier(
					"status_bar_height", "dimen", "android");
			if (resourceId > 0) {
				result = context.getResources().getDimensionPixelSize(
						resourceId);
			}
		} catch (Resources.NotFoundException e) {
			e.printStackTrace();
		}
		return result;
	}

	public static int[] getScreenSize(Context context, boolean useDeviceSize) {

		int[] size = new int[2];

		WindowManager w = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		Display d = w.getDefaultDisplay();
		DisplayMetrics metrics = new DisplayMetrics();
		d.getMetrics(metrics);
		// since SDK_INT = 1;
		int widthPixels = metrics.widthPixels;
		int heightPixels = metrics.heightPixels;

		if (!useDeviceSize) {
			size[0] = widthPixels;
			size[1] = heightPixels  - getStatusBarHeight(context);
			return size;
		}
		// includes window decorations (statusbar bar/menu bar)
		if (Build.VERSION.SDK_INT >= 14 && Build.VERSION.SDK_INT < 17)
			try {
				widthPixels = (Integer) Display.class.getMethod("getRawWidth")
						.invoke(d);
				heightPixels = (Integer) Display.class
						.getMethod("getRawHeight").invoke(d);
			} catch (Exception ignored) {
			}
		// includes window decorations (statusbar bar/menu bar)
		if (Build.VERSION.SDK_INT >= 17)
			try {
				Point realSize = new Point();
				Display.class.getMethod("getRealSize", Point.class).invoke(d,
						realSize);
				widthPixels = realSize.x;
				heightPixels = realSize.y;
			} catch (Exception ignored) {
			}
		size[0] = widthPixels;
//		int statusBar = getStatusBarHeight(context);
		size[1] = heightPixels;
		return size;
	}

	static int getStatusBarHight(Context context) {
		Class<?> c = null;
		Object obj = null;
		Field filed = null;
		int x = 0;
		try {
			//  field = c.getField("status_bar_height");  
			c = Class.forName("com.android.internal.R$dimen");
			obj = c.newInstance();
			try {
				filed = c.getField("status_bar_height");
				x = Integer.parseInt(filed.get(obj).toString());
			} catch (NoSuchFieldException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return context.getResources().getDimensionPixelSize(x);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}

}
