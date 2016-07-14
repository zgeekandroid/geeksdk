package com.commonslibrary.commons.utils;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Set;


public class SharePreferenceUtil {

	private volatile static SharePreferenceUtil instance;
	public SharedPreferences preferences;

	private SharePreferenceUtil() {
	}

	/** Returns singleton class instance */
	public static synchronized SharePreferenceUtil getInstance(Context context) {
//		if (instance == null) {
			instance = new SharePreferenceUtil();
			instance.preferences = context.getSharedPreferences(context.getApplicationContext().getPackageName(),Context.MODE_PRIVATE);
//		}
		return instance;
	}

	/**
	 * 储存String值
	 * 
	 * @param key
	 * @param value
	 */
	public void setStringValue(String key, String value) {
		preferences.edit().putString(key, value).apply();
	}

	/**
	 * 获取String值,默认为空字符串
	 * 
	 * @param key
	 * @return
	 */
	public String getStringValue(String key) {
		return preferences.getString(key, "");
	}

	/**
	 * 获取String值
	 * 
	 * @param key
	 * @return
	 */
	public String getStringValue(String key, String defaultValue) {
		return preferences.getString(key, defaultValue);
	}

	/**
	 * 储存Boolean值
	 * 
	 * @param key
	 * @param value
	 */
	public void setBooleanValue(String key, Boolean value) {
		preferences.edit().putBoolean(key, value).apply();
	}

	/**
	 * 获取Boolean值
	 * 
	 * @param key
	 * @return
	 */
	public boolean getBooleanValue(String key) {
		return preferences.getBoolean(key, false);
	}

	/**
	 * 获取Boolean值
	 * 
	 * @param key
	 * @return
	 */
	public boolean getBooleanValue(String key, boolean defaultValue) {
		return preferences.getBoolean(key, defaultValue);
	}

	/**
	 * 设置Long值
	 * 
	 * @param key
	 * @param value
	 */
	public void setLongValue(String key, long value) {
		preferences.edit().putLong(key, value).apply();
	}

	/**
	 * 获取Long值
	 * 
	 * @param key
	 * @return
	 */
	public long getLongValue(String key) {
		return preferences.getLong(key, 0L);
	}

	/**
	 * 储存int值
	 * 
	 * @param key
	 * @param value
	 */
	public void setIntValue(String key, int value) {
		preferences.edit().putInt(key, value).apply();
	}

	/**
	 * 获取int值,默认-1
	 * 
	 * @param key
	 * @return
	 */
	public int getIntValue(String key) {
		return preferences.getInt(key, -1);
	}

	/**
	 * 获取int值
	 * 
	 * @param key
	 * @param defalutValue
	 * @return
	 */
	public int getIntValue(String key, int defalutValue) {
		return preferences.getInt(key, defalutValue);
	}

	/**
	 * 设置Float值
	 * 
	 * @param key
	 * @param value
	 */
	public void setFloatValue(String key, float value) {
		preferences.edit().putFloat(key, value).apply();
	}

	/**
	 * 获取Float值
	 * 
	 * @param key
	 * @param value
	 * @return
	 */
	public float getFloatValue(String key, float value) {
		return preferences.getFloat(key, value);
	}


	/**
	 * 保存set 数据
	 * @param key
	 * @param values
	 */
	public void setSetValue(String key,  Set<String> values){
		preferences.edit().putStringSet(key,values).apply();
	}

	/**
	 * 获取set 数据
	 * @param key

	 */
	public Set<String> getSetValue(String key){
		return preferences.getStringSet(key, null);
	}

}
