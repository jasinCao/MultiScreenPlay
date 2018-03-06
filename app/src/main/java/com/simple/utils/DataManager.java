package com.simple.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.provider.Settings.System;
import android.text.TextUtils;

import com.google.gson.Gson;

/**
 * 专门用于存储和读取需要持续保存的数据
 * 
 * @author giaour-cao
 * 
 */
public class DataManager {

	private Gson gson = new Gson();

	public static DataManager manager;
	private Context context;
	public static boolean isInit;

	public synchronized static DataManager getInstance() {
		// TODO Auto-generated constructor stub
		if (manager == null) {
			manager = new DataManager();
		}
		return manager;
	}

	private SharedPreferences sp;

	public void init(Context context) {
		isInit = true;
		this.context = context;
		sp = context.getSharedPreferences("shoplink", Context.MODE_PRIVATE);
	}

	public void clearData() {
		if (sp != null) {
			sp.edit().clear().commit();
		}
	}

	/**
	 * 存储对象或者字符串 一个key只能对应一个value 也就是一次只能存一个值
	 * 
	 * @param context
	 *            必填
	 * @param key
	 *            必填
	 * @param data
	 *            根据需要填写
	 * @param listData
	 *            根据需要填写
	 * @param obj
	 *            根据需要填写
	 */
	public <T> void saveData(String key, String data, List<T> listData,
			Object obj) {
		if (TextUtils.isEmpty(key)) {
			return;
		}
		String value = "";
		if (!TextUtils.isEmpty(data)) {
			value = data;
		} else if (listData != null) {
			value = gson.toJson(listData);
		} else if (obj != null) {
			value = gson.toJson(obj);
		}
		// System.putString(context.getContentResolver(), key, value);
		Editor editor = sp.edit();
		editor.putString(key, value);
		editor.commit();
	}

	public <T> void saveData(String key, String data) {
		if (TextUtils.isEmpty(key)) {
			return;
		}

		String value = "";
		if (!TextUtils.isEmpty(data)) {
			value = data;
		}
		Editor editor = sp.edit();
		editor.putString(key, value);
		editor.commit();
		// System.putString(context.getContentResolver(), key, value);
	}

	public <T> void saveData(String key, List<T> listData) {
		String value = "";
		if (listData != null) {
			value = gson.toJson(listData);
		}
		Editor editor = sp.edit();
		editor.putString(key, value);
		editor.commit();
		// System.putString(context.getContentResolver(), key, value);
	}

	public <T> void saveObjData(String key, Object obj) {

		if (TextUtils.isEmpty(key) || obj == null) {
			return;
		}
		if (context == null) {
			return;
		}

		String value = "";
		if (obj != null) {
			value = gson.toJson(obj);
		}

		Editor editor = sp.edit();
		editor.putString(key, value);
		editor.commit();
	}

	/**
	 * 用于获取存储的对象
	 * 
	 * @param context
	 *            必填
	 * @param key
	 *            必填
	 * @param obj
	 *            必填
	 * @return
	 */
	public <T> T getObj(String key, Class<T> obj) {
		if (TextUtils.isEmpty(key)) {
			return null;
		}

		String json = sp.getString(key, "");
		if (TextUtils.isEmpty(json)) {
			return null;
		}
		try {
			return gson.fromJson(json, obj);
		} catch (Exception e) {
			// TODO: handle exception
			return null;
		}
	}

	/**
	 * 获取存储的list
	 * 
	 * @param context
	 *            必填
	 * @param key
	 *            必填
	 * @param objs
	 *            必填
	 * @return
	 */
	public <T> ArrayList<T> getList(String key, Class<T[]> objs) {
		if (TextUtils.isEmpty(key)) {
			return new ArrayList<>();
		}
		String json = sp.getString(key, "");
		if (TextUtils.isEmpty(json)) {
			return null;
		}

		ArrayList<T> arrayList = new ArrayList<>();
		List<T> list = jsonStrToList(json, objs);
		if (list != null) {
			for (int i = 0; i < list.size(); i++) {
				arrayList.add(list.get(i));
			}
			return arrayList;
		}

		return null;
	}

	public void setSystemProvaderData(String key, String data) {
		System.putString(context.getContentResolver(), key, data);
	}

	/**
	 * 获取存储的字符串
	 * 
	 * @param context
	 * @param key
	 * @return
	 */
	public String getData(String key) {

		if (context == null) {
			return "";
		}
		if (TextUtils.isEmpty(key)) {
			return "";
		}
		String data = sp.getString(key, "");
		return data;
	}

	/**
	 * 获取存储的字符串
	 * 
	 * @param context
	 * @param key
	 * @return
	 */
	public int getIntData(String key) {
		if (context == null) {
			return -1;
		}
		if (TextUtils.isEmpty(key)) {
			return -1;
		}
		int data = sp.getInt(key, -1);
		return data;
	}

	/**
	 * 获取存储的字符串
	 * 
	 * @param context
	 * @param key
	 * @return
	 */
	public void saveIntData(String key, int value) {
		if (TextUtils.isEmpty(key)) {
			return;
		}
		if (sp == null) {
			return;
		}
		Editor editor = sp.edit();
		editor.putInt(key, value);
		editor.commit();
		// System.putInt(context.getContentResolver(), key, value);
	}

	public <T> List<T> jsonStrToList(String json, Class<T[]> objs) {
		T[] arr = new Gson().fromJson(json, objs);
		List<T> list = Arrays.asList(arr);
		return list;
	}

	/**
	 * list转换成json
	 * 
	 * @param listData
	 * @return
	 */
	public <T> String listTojson(List<T> listData) {
		String value = gson.toJson(listData);
		return value;
	}

	/**
	 * json to list
	 * 
	 * @param key
	 * @param data
	 */
	public <T> ArrayList<T> jsonToArrayList(String json, Class<T[]> objs) {
		ArrayList<T> arrayList = new ArrayList<>();
		List<T> list = jsonStrToList(json, objs);
		if (list != null) {
			for (int i = 0; i < list.size(); i++) {
				arrayList.add(list.get(i));
			}
			return arrayList;
		}
		return arrayList;
	}

	public synchronized static boolean saveConfig(String filepath,
			final String data) {
		File fileInfo = null;
		fileInfo = new File(filepath);
		if (!fileInfo.exists()) {
			try {
				fileInfo.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				fileInfo.mkdirs();
			}
		}
		FileOutputStream fos = null;
		boolean success = false;
		if (fileInfo != null) {
			try {
				fos = new FileOutputStream(fileInfo);
				fos.write(data.getBytes());
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				try {
					if (fos != null) {
						fos.close();
						success = true;
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return success;
	}

	public synchronized static String getConfig(String filePath) {
		File file = new File(filePath);
		if (file.exists()) {
			InputStream in;
			try {
				in = new FileInputStream(file);
				byte b[] = new byte[(int) file.length()]; // 创建合适文件大小的数组
				in.read(b); // 读取文件中的内容到b[]数组
				in.close();
				return new String(b);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return "";
		} else {
			return "";
		}

	}
}
