package com.simple.log;

import android.util.Log;

public class Slog {

	public static boolean isDebug = true;

	public static void setDebug(boolean isDebug) {
		Slog.isDebug = isDebug;
	}

	public static void d(Object tag, String log) {
		if (isDebug) {
			if (tag == null || log == null) {
				return;
			}

			if (tag instanceof String) {
				Log.d(tag.toString(), log);
			} else {
				Log.d(tag.getClass().getSimpleName(), log);
			}

		}

	}

	public static void d(Object tag, float log) {
		if (isDebug) {
			if (tag instanceof String) {
				Log.d(tag.toString(), log + "");
			} else {
				Log.d(tag.getClass().getSimpleName(), log + "");
			}

		}

	}

	public static void d(Object tag, int log) {
		if (isDebug) {

			if (tag instanceof String) {
				Log.d(tag.toString(), log + "");
			} else {
				Log.d(tag.getClass().getSimpleName(), log + "");
			}

		}

	}

	public static void e(Object tag, String log) {
		if (isDebug) {
			if (tag == null || log == null) {
				return;
			}

			if (tag instanceof String) {
				Log.e(tag.toString(), log);
			} else {
				Log.e(tag.getClass().getSimpleName(), log);
			}

		}

	}

	public static void e(Object tag, int log) {
		if (isDebug) {

			if (tag instanceof String) {
				Log.e(tag.toString(), log + "");
			} else {
				Log.e(tag.getClass().getSimpleName(), log + "");
			}

		}

	}

}
