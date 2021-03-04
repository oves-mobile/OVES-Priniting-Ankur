package com.example.printlibrary.utils;

import android.content.Context;
import android.content.SharedPreferences;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/***************************************
 * 
 * @author linjy
 * @time 2015-5-5 14:29:22
 * @类说明 配置文件工具类
 * 
 *      <pre>
 * 1.int写入
 * 2.int获取
 * 3.String 写入
 * 4.String 获取
 * 5.boolean 写入
 * 6.boolean 获取
 * 7.Long 写入
 * 8.Long 获取
 * </pre>
 * 
 **************************************/
public class SharedPreferencesUtils
{

    public static final String PREFERENCES_NAME = "cfg"; // 配置文件名称

    /**
     * 1.int写入
     * 
     * @param context
     * @param key
     * @param value
     */
    public static void putIntPreferences(Context context, String key, int value)
    {
        SharedPreferences settings = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt(key, value);
        editor.commit();
    }

    /**
     * 2.int获取
     * 
     * @param context
     * @param key
     * @return 若为空默认0
     */
    public static int getIntPreferences(Context context, String key, int defaultValue)
    {
        SharedPreferences settings = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
        return settings.getInt(key, defaultValue);
    }

    /**
     * int获取
     * 默认值为-1
     */
    public static int getIntPreferences(Context context, String key)
    {
        SharedPreferences settings = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
        return settings.getInt(key, -1);
    }

    /**
     * 3.String 写入
     * 
     * @param context
     * @param key
     * @param value
     */
    public static void putStringPreferences(Context context, String key, String value)
    {
        SharedPreferences settings = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(key, value);
        editor.commit();
    }

    /**
     * 4.String 获取
     * 
     * @param context
     * @param key
     */
    public static String getStringValue(Context context, String key, String defValue)
    {
        SharedPreferences settings = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
        return settings.getString(key, defValue);
    }

    /**
     * String 获取
     * 默认值为空字符串
     */
    public static String getStringValue(Context context, String key)
    {
        SharedPreferences settings = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
        return settings.getString(key, "");
    }

    /**
     * 5.boolean 写入
     * 
     * @param context
     * @param key
     * @param value
     */
    public static void putBooleanPreferences(Context context, String key, boolean value)
    {
        SharedPreferences settings = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    /**
     * 
     * boolean 获取
     * 默认值为false
     * */
    public static boolean getBooleanPreferences(Context context, String key)
    {
        SharedPreferences settings = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
        return settings.getBoolean(key, false);
    }

    /**
     * 6.boolean 获取
     * 
     * @param context
     * @param key
     * @param defValue
     * @return
     */
    public static boolean getBooleanPreferences(Context context, String key, boolean defValue)
    {
        SharedPreferences settings = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
        return settings.getBoolean(key, defValue);
    }

    /**
     * 7.Long 写入
     * 
     * @param context
     * @param key
     * @param value
     */
    public static void putLongPreferences(Context context, String key, long value)
    {
        SharedPreferences settings = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putLong(key, value);
        editor.commit();
    }

    /**
     * 8.Long 获取
     * 
     * @param context
     * @param key
     * @return
     */
    public static long getLongPreferences(Context context, String key, Long defValue)
    {
        SharedPreferences settings = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
        return settings.getLong(key, defValue);
    }

    /**
     * Long 获取
     * 默认值为-1
     * 
     */
    public static long getLongPreferences(Context context, String key)
    {
        SharedPreferences settings = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
        return settings.getLong(key, -1);
    }

    /**
     * 移除某个key值已经对应的值
     * @param context
     * @param key
     */
    public static void remove(Context context, String key)
    {
        SharedPreferences sp = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.remove(key);
        SharedPreferencesCompat.apply(editor);
    }

    /**
     * 清除所有数据
     * @param context
     */
    public static void clear(Context context)
    {
        SharedPreferences sp = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.clear();
        SharedPreferencesCompat.apply(editor);
    }

    /**
     * 查询某个key是否已经存在
     * @param context
     * @param key
     * @return
     */
    public static boolean contains(Context context, String key)
    {
        SharedPreferences sp = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
        return sp.contains(key);
    }

    /**
     * 创建一个解决SharedPreferencesCompat.apply方法的一个兼容类
     *
     * @author zhy
     *
     */
    private static class SharedPreferencesCompat
    {
        private static final Method sApplyMethod = findApplyMethod();

        /**
         * 反射查找apply的方法
         *
         * @return
         */
        @SuppressWarnings({ "unchecked", "rawtypes" })
        private static Method findApplyMethod()
        {
            try
            {
                Class clz = SharedPreferences.Editor.class;
                return clz.getMethod("apply");
            } catch (NoSuchMethodException e)
            {
            }

            return null;
        }

        /**
         * 如果找到则使用apply执行，否则使用commit
         *
         * @param editor
         */
        public static void apply(SharedPreferences.Editor editor)
        {
            try
            {
                if (sApplyMethod != null)
                {
                    sApplyMethod.invoke(editor);
                    return;
                }
            } catch (IllegalArgumentException e)
            {
            } catch (IllegalAccessException e)
            {
            } catch (InvocationTargetException e)
            {
            }
            editor.commit();
        }
    }
}
