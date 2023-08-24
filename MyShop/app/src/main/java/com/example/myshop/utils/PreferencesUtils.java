package com.example.myshop.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Bei on 2016/9/26.
 * 用于操作SharedPreferences的工具类
 */

public class PreferencesUtils {
    public static String PREFERENCE_NAME = "MyShop_Pref_Common";

    /**
     * 将字符串存放入SharedPreferences
     * @param context
     * @param key SharedPreferences中的key
     * @param value 与key对应的需要修改的value
     * @return 如果成功写入这返回真
     */
    public static boolean putString(Context context, String key, String value){
        SharedPreferences settings = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(key, value);
        return  editor.commit();
    }

    /**
     * 获取存入SharedPreferences中的信息
     * @param context
     * @param key 在SharedPreferences中存在的key
     * @return SharedPreferences的value如果存在返回value或者default，抛出ClassCastException如果想要获取的值不是String类型
     */
    public static String getString(Context context, String key) {
        return getString(context, key, null);
    }

    /**
     * 获取存入SharedPreferences中的信息
     * @param context
     * @param key 在SharedPreferences中存在的key
     * @param defaultValue  如果SharedPreferences不存在的时候返回的默认值
     * @return SharedPreferences的value如果存在返回value或者default，抛出ClassCastException如果想要获取的值不是String类型
     */
    public static String getString(Context context, String key, String defaultValue) {
        SharedPreferences settings = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        return settings.getString(key, defaultValue);
    }

    /**
     * 存放int值放入SharedPreferences
     * @param context
     * @param key 要修改的SharedPreferences的key
     * @param value SharedPreferences中药修改的value的新值
     * @return
     */
    public static boolean putInt(Context context, String key, int value) {
        SharedPreferences settings = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor  editor = settings.edit();
        editor.putInt(key, value);
        return editor.commit();
    }

    /**
     * 从SharedPreferences获取int值
     * @param context
     * @param key SharedPreferences中的key
     * @return SharedPreferences的value如果存在返回value或者default，抛出ClassCastException如果想要获取的值不是Int类型
     * @see #getInt(Context, String, int)
     */
    public static int getInt(Context context, String key) {
        return getInt(context, key, -1);
    }

    /**
     * 从SharedPreferences获取int值
     * @param context
     * @param key SharedPreferences中的key
     * @param defaultValue 如果SharedPreferences不存在返回的默认值
     * @return SharedPreferences的value如果存在返回value或者default，抛出ClassCastException如果想要获取的值不是Int类型
     * @see #getInt(Context, String, int)
     */
    public static int getInt(Context context, String key, int defaultValue) {
        SharedPreferences settings = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        return settings.getInt(key, defaultValue);
    }

    /**
     * 将value存放入SharedPreferences
     * @param context
     * @param key SharedPreferences中的key
     * @param value 与key对应的需要修改的value
     * @return 如果成功写入这返回真
     */
    public static boolean putLong(Context context, String key, long value) {
        SharedPreferences settings = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putLong(key, value);
        return editor.commit();
    }

    /**
     * 从SharedPreferences获取long值
     * @param context
     * @param key SharedPreferences中的key
     * @return SharedPreferences的value如果存在返回value或者default，抛出ClassCastException如果想要获取的值不是long类型
     * @see #getLong(Context, String, long)
     */
    public static long getLong(Context context, String key) {
        return getLong(context, key, -1);
    }

    /**
     * 从SharedPreferences获取long值
     * @param context
     * @param key SharedPreferences中的key
     * @param defaultValue 如果SharedPreferences不存在返回的默认值
     * @return SharedPreferences的value如果存在返回value或者default，抛出ClassCastException如果想要获取的值不是long类型
     * @see #getLong(Context, String, long)
     */
    public static long getLong(Context context, String key, long defaultValue) {
        SharedPreferences settings = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        return settings.getLong(key, defaultValue);
    }

    /**
     * 将value存放入SharedPreferences
     * @param context
     * @param key SharedPreferences中的key
     * @param value 与key对应的需要修改的value
     * @return 如果成功写入这返回真
     */
    public static boolean putFloat(Context context, String key, float value) {
        SharedPreferences settings = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putFloat(key, value);
        return editor.commit();
    }


    /**
     * 从SharedPreferences获取float值
     * @param context
     * @param key SharedPreferences中的key
     * @return SharedPreferences的value如果存在返回value或者default，抛出ClassCastException如果想要获取的值不是float类型
     * @see #getFloat(Context, String, float)
     */
    public static float getFloat(Context context, String key) {
        return getFloat(context, key, -1);
    }

    /**
     * 从SharedPreferences获取float值
     * @param context
     * @param key SharedPreferences中的key
     * @param defaultValue 如果SharedPreferences不存在返回的默认值
     * @return SharedPreferences的value如果存在返回value或者default，抛出ClassCastException如果想要获取的值不是float类型
     * @see #getFloat(Context, String, float)
     */
    public static float getFloat(Context context, String key, float defaultValue) {
        SharedPreferences settings = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        return settings.getFloat(key, defaultValue);
    }

    /**
     * 将value存放入SharedPreferences
     * @param context
     * @param key SharedPreferences中的key
     * @param value 与key对应的需要修改的value
     * @return 如果成功写入这返回真
     */
    public static boolean putBoolean(Context context, String key, boolean value) {
        SharedPreferences settings = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean(key, value);
        return editor.commit();
    }

    /**
     * 从SharedPreferences获取boolean值
     * @param context
     * @param key SharedPreferences中的key
     * @return SharedPreferences的value如果存在返回value或者default，抛出ClassCastException如果想要获取的值不是boolean类型
     * @see #getBoolean(Context, String, boolean)
     */
    public static boolean getBoolean(Context context, String key) {
        return getBoolean(context, key, false);
    }

    /**
     * 从SharedPreferences获取boolean值
     * @param context
     * @param key SharedPreferences中的key
     * @param defaultValue 如果SharedPreferences不存在返回的默认值
     * @return SharedPreferences的value如果存在返回value或者default，抛出ClassCastException如果想要获取的值不是boolean类型
     */
    public static boolean getBoolean(Context context, String key, boolean defaultValue) {
        SharedPreferences settings = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        return settings.getBoolean(key, defaultValue);
    }


}
