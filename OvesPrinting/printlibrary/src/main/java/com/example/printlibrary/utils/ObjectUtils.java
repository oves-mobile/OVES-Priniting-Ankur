package com.example.printlibrary.utils;

/**
 * @author linjy
 * @time 2015-5-5 16:01:36
 * @类说明： 对象工具类
 * 
 *       <pre>
 * 1.对比两个类是否相同
 * 2.long数组转换为Long数组
 * 3.Long数组转换为long数组
 * 4.int数组转换为Integer数组
 * 5.Integer数组转换为int数组
 * 6.对比两个类compare
 * </pre>
 */
public class ObjectUtils
{

    /**
     * 1.对比两个类是否相同
     * compare two object
     * 
     * @param actual
     * @param expected
     * @return <ul>
     *         <li>if both are null, return true</li>
     *         <li>return actual.{@link Object#equals(Object)}</li>
     *         </ul>
     */
    public static boolean isEquals(Object actual, Object expected)
    {
        return actual == null ? expected == null : actual.equals(expected);
    }

    /**
     * 2.long数组转换为Long数组
     * convert long array to Long array
     * 
     * @param source
     * @return
     */
    public static Long[] transformLongArray(long[] source)
    {
        Long[] destin = new Long[source.length];
        for (int i = 0; i < source.length; i++)
        {
            destin[i] = source[i];
        }
        return destin;
    }

    /**
     * 3.Long数组转换为long数组
     * convert Long array to long array
     * 
     * @param source
     * @return
     */
    public static long[] transformLongArray(Long[] source)
    {
        long[] destin = new long[source.length];
        for (int i = 0; i < source.length; i++)
        {
            destin[i] = source[i];
        }
        return destin;
    }

    /**
     * 4.int数组转换为Integer数组
     * convert int array to Integer array
     * 
     * @param source
     * @return
     */
    public static Integer[] transformIntArray(int[] source)
    {
        Integer[] destin = new Integer[source.length];
        for (int i = 0; i < source.length; i++)
        {
            destin[i] = source[i];
        }
        return destin;
    }

    /**
     * 5.Integer数组转换为int数组
     * convert Integer array to int array
     * 
     * @param source
     * @return
     */
    public static int[] transformIntArray(Integer[] source)
    {
        int[] destin = new int[source.length];
        for (int i = 0; i < source.length; i++)
        {
            destin[i] = source[i];
        }
        return destin;
    }

    /**
     * 6.对比两个lei
     * compare two object
     * <ul>
     * <strong>About result</strong>
     * <li>if v1 > v2, return 1</li>
     * <li>if v1 = v2, return 0</li>
     * <li>if v1 < v2, return -1</li>
     * </ul>
     * <ul>
     * <strong>About rule</strong>
     * <li>if v1 is null, v2 is null, then return 0</li>
     * <li>if v1 is null, v2 is not null, then return -1</li>
     * <li>if v1 is not null, v2 is null, then return 1</li>
     * <li>return v1.{@link Comparable#compareTo(Object)}</li>
     * </ul>
     * 
     * @param v1
     * @param v2
     * @return
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    public static <V> int compare(V v1, V v2)
    {
        return v1 == null ? (v2 == null ? 0 : -1) : (v2 == null ? 1 : ((Comparable) v1).compareTo(v2));
    }
}
