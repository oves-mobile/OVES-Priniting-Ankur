package com.example.printlibrary.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author linjy
 * @time 2015-5-5 14:32:44
 * @类说明 字符串工具类
 * <p/>
 */

public class StringUtils {

    /**
     * 1.判断字符串是否为空 is null or its length is 0 or it is made by space
     * <p/>
     * <pre>
     * isBlank(null) = true;
     * isBlank(&quot;&quot;) = true;
     * isBlank(&quot;  &quot;) = true;
     * isBlank(&quot;a&quot;) = false;
     * isBlank(&quot;a &quot;) = false;
     * isBlank(&quot; a&quot;) = false;
     * isBlank(&quot;a b&quot;) = false;
     * </pre>
     *
     * @param str
     * @return if string is null or its size is 0 or it is made by space, return
     * true, else return false.
     */
    public static boolean isBlank(String str) {
        return (str == null || str.trim().length() == 0);
    }

    /**
     * 2.判断字符串是否为空(多了"null") is null or its length is 0
     * <p/>
     * <pre>
     * isEmpty(null) = true;
     * isEmpty(&quot;&quot;) = true;
     * isEmpty(&quot;  &quot;) = false;
     * isEmpty(&quot;null&quot;) = true;
     * </pre>
     *
     * @param str
     * @return if string is null or its size is 0, return true, else return
     * false.
     */
    public static boolean isEmpty(String str) {
        return (str == null || str.length() == 0 || str.equals("null"));
    }

    /**
     * 3.对比两个字符串 compare two string
     *
     * @param actual
     * @param expected
     * @return
     * @see ObjectUtils#isEquals(Object, Object)
     */
    public static boolean isEquals(String actual, String expected) {
        return ObjectUtils.isEquals(actual, expected);
    }

    /**
     * 4.如果字符串为null设置成"",不为空则返回本身 null string to empty string
     * <p/>
     * <pre>
     * nullStrToEmpty(null) = &quot;&quot;;
     * nullStrToEmpty(&quot;&quot;) = &quot;&quot;;
     * nullStrToEmpty(&quot;aa&quot;) = &quot;aa&quot;;
     * </pre>
     *
     * @param str
     * @return
     */
    public static String nullStrToEmpty(String str) {
        return (str == null ? "" : str);
    }

    /**
     * "null"或者null 替换为""
     * <pre>
     * nullStrToEmpty(null) = &quot;&quot;;
     * nullStrToEmpty("null") = &quot;&quot;;
     * nullStrToEmpty(&quot;&quot;) = &quot;&quot;;
     * nullStrToEmpty(&quot;aa&quot;) = &quot;aa&quot;;
     * </pre>
     *
     * @param str
     * @return
     */
    public static String nullOrStrToEmpty(String str) {
//    	return (str == null ? "" : str);

        if (str == null) {
            return "";
        } else if (str.equals("null")) {
            return "";
        } else {
            return str;
        }
    }

    /**
     * 5.首字母大写 capitalize first letter
     * <p/>
     * <pre>
     * capitalizeFirstLetter(null)     =   null;
     * capitalizeFirstLetter("")       =   "";
     * capitalizeFirstLetter("2ab")    =   "2ab"
     * capitalizeFirstLetter("a")      =   "A"
     * capitalizeFirstLetter("ab")     =   "Ab"
     * capitalizeFirstLetter("Abc")    =   "Abc"
     * </pre>
     *
     * @param str
     * @return
     */
    public static String capitalizeFirstLetter(String str) {
        if (isEmpty(str)) {
            return str;
        }

        char c = str.charAt(0);
        return (!Character.isLetter(c) || Character.isUpperCase(c)) ? str : new StringBuilder(str.length()).append(Character.toUpperCase(c)).append(str.substring(1)).toString();
    }


    /**
     * 7.得到html中的<a>标签内容
     * <p/>
     * get innerHtml from href
     * <p/>
     * <pre>
     * getHrefInnerHtml(null)                                  = ""
     * getHrefInnerHtml("")                                    = ""
     * getHrefInnerHtml("mp3")                                 = "mp3";
     * getHrefInnerHtml("&lt;a innerHtml&lt;/a&gt;")                    = "&lt;a innerHtml&lt;/a&gt;";
     * getHrefInnerHtml("&lt;a&gt;innerHtml&lt;/a&gt;")                    = "innerHtml";
     * getHrefInnerHtml("&lt;a&lt;a&gt;innerHtml&lt;/a&gt;")                    = "innerHtml";
     * getHrefInnerHtml("&lt;a href="baidu.com"&gt;innerHtml&lt;/a&gt;")               = "innerHtml";
     * getHrefInnerHtml("&lt;a href="baidu.com" title="baidu"&gt;innerHtml&lt;/a&gt;") = "innerHtml";
     * getHrefInnerHtml("   &lt;a&gt;innerHtml&lt;/a&gt;  ")                           = "innerHtml";
     * getHrefInnerHtml("&lt;a&gt;innerHtml&lt;/a&gt;&lt;/a&gt;")                      = "innerHtml";
     * getHrefInnerHtml("jack&lt;a&gt;innerHtml&lt;/a&gt;&lt;/a&gt;")                  = "innerHtml";
     * getHrefInnerHtml("&lt;a&gt;innerHtml1&lt;/a&gt;&lt;a&gt;innerHtml2&lt;/a&gt;")        = "innerHtml2";
     * </pre>
     *
     * @param href
     * @return <ul>
     * <li>if href is null, return ""</li>
     * <li>if not match regx, return source</li>
     * <li>return the last string that match regx</li>
     * </ul>
     */
    public static String getHrefInnerHtml(String href) {
        if (isEmpty(href)) {
            return "";
        }

        String hrefReg = ".*<[\\s]*a[\\s]*.*>(.+?)<[\\s]*/a[\\s]*>.*";
        Pattern hrefPattern = Pattern.compile(hrefReg, Pattern.CASE_INSENSITIVE);
        Matcher hrefMatcher = hrefPattern.matcher(href);
        if (hrefMatcher.matches()) {
            return hrefMatcher.group(1);
        }
        return href;
    }

    /**
     * 8.在html中的特殊
     * process special char in html
     * <p/>
     * <pre>
     * htmlEscapeCharsToString(null) = null;
     * htmlEscapeCharsToString("") = "";
     * htmlEscapeCharsToString("mp3") = "mp3";
     * htmlEscapeCharsToString("mp3&lt;") = "mp3<";
     * htmlEscapeCharsToString("mp3&gt;") = "mp3\>";
     * htmlEscapeCharsToString("mp3&amp;mp4") = "mp3&mp4";
     * htmlEscapeCharsToString("mp3&quot;mp4") = "mp3\"mp4";
     * htmlEscapeCharsToString("mp3&lt;&gt;&amp;&quot;mp4") = "mp3\<\>&\"mp4";
     * </pre>
     *
     * @param source
     * @return
     */
    public static String htmlEscapeCharsToString(String source) {
        return StringUtils.isEmpty(source) ? source : source.replaceAll("&lt;", "<").replaceAll("&gt;", ">").replaceAll("&amp;", "&").replaceAll("&quot;", "\"");
    }

    /**
     * 9.从文字的字节码中将全角空格的字节码替换为半角空格的字节码 transform half width char to full width
     * char
     * <p/>
     * <pre>
     * fullWidthToHalfWidth(null) = null;
     * fullWidthToHalfWidth("") = "";
     * fullWidthToHalfWidth(new String(new char[] {12288})) = " ";
     * fullWidthToHalfWidth("！＂＃＄％＆) = "!\"#$%&";
     * </pre>
     *
     * @param s
     * @return
     */
    public static String fullWidthToHalfWidth(String s) {
        if (isEmpty(s)) {
            return s;
        }

        char[] source = s.toCharArray();
        for (int i = 0; i < source.length; i++) {
            if (source[i] == 12288) {
                source[i] = ' ';
                // } else if (source[i] == 12290) {
                // source[i] = '.';
            } else if (source[i] >= 65281 && source[i] <= 65374) {
                source[i] = (char) (source[i] - 65248);
            } else {
                source[i] = source[i];
            }
        }
        return new String(source);
    }

    /**
     * 10.从文字的字节码中将半角空格的字节码替换为全角空格的字节码 transform full width char to half width
     * char
     * <p/>
     * <pre>
     * halfWidthToFullWidth(null) = null;
     * halfWidthToFullWidth("") = "";
     * halfWidthToFullWidth(" ") = new String(new char[] {12288});
     * halfWidthToFullWidth("!\"#$%&) = "！＂＃＄％＆";
     * </pre>
     *
     * @param s
     * @return
     */
    public static String halfWidthToFullWidth(String s) {
        if (isEmpty(s)) {
            return s;
        }

        char[] source = s.toCharArray();
        for (int i = 0; i < source.length; i++) {
            if (source[i] == ' ') {
                source[i] = (char) 12288;
                // } else if (source[i] == '.') {
                // source[i] = (char)12290;
            } else if (source[i] >= 33 && source[i] <= 126) {
                source[i] = (char) (source[i] + 65248);
            } else {
                source[i] = source[i];
            }
        }
        return new String(source);
    }

    /**
     * 11.判断字符串是否为int
     *
     * @param str
     * @return
     */
    public static boolean IsIntegral(String str) {
        Pattern pattern = Pattern.compile("[0-9]*");
        return pattern.matcher(str).matches();
    }

    /**
     * 12.空格替换为;
     *
     * @param str
     * @return
     */
    public static String spaceReplace(String str) {
        if (str.contains(" "))
            return str.replace(" ", ";");
        return str;
    }

    // -------------------------检测是否有emoji字符-----------------------------

    /**
     * 13.检测是否有emoji字符
     *
     * @param source
     * @return 一旦含有就抛出
     */
    public static boolean containsEmoji(String source) {
        if (StringUtils.isBlank(source)) {
            return false;
        }
        int len = source.length();
        for (int i = 0; i < len; i++) {
            char codePoint = source.charAt(i);

            if (isEmojiCharacter(codePoint)) {
                // do nothing，判断到了这里表明，确认有表情字符
                return true;
            }
        }

        return false;
    }

    /**
     * 14.检测是否有emoji字符
     *
     * @param codePoint
     * @return
     */
    private static boolean isEmojiCharacter(char codePoint) {
        return (codePoint == 0x0) || (codePoint == 0x9) || (codePoint == 0xA) || (codePoint == 0xD) || ((codePoint >= 0x20) && (codePoint <= 0xD7FF)) || ((codePoint >= 0xE000) && (codePoint <= 0xFFFD)) || ((codePoint >= 0x10000) && (codePoint <= 0x10FFFF));
    }

    /**
     * 15.过滤emoji 或者 其他非文字类型的字符
     *
     * @param source
     * @return
     */
    public static String filterEmoji(String source) {
        if (source.trim().length() == 0) {
            return " ";
        }

        if (source.length() == 2) {
            source = " " + source;
        }

        if (!containsEmoji(source)) {
            return source;// 如果不包含，直接返回
        }
        // 到这里铁定包含
        StringBuilder buf = null;

        int len = source.length();

        for (int i = 0; i < len; i++) {
            char codePoint = source.charAt(i);

            if (isEmojiCharacter(codePoint)) {
                if (buf == null) {
                    buf = new StringBuilder(source.length());
                }

                buf.append(codePoint);
            } else {
            }
        }

        if (buf == null) {
            return source;// 如果没有找到 emoji表情，则返回源字符串
        } else {
            if (buf.length() == len) {// 这里的意义在于尽可能少的toString，因为会重新生成字符串
                buf = null;
                return source;
            } else {
                return buf.toString();
            }
        }
    }

    /**
     * 16.计算两个日期型的时间相差多少时间
     *
     * @return
     */
    @SuppressLint("SimpleDateFormat")
    public static String twoDateDistance(String startDate) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date now = df.parse(df.format(new Date()));
            Date date = df.parse(startDate);
            long l = now.getTime() - date.getTime();
            long day = l / (24 * 60 * 60 * 1000);// 天
            long hours = l / (60 * 60 * 1000);// 小时
            long minu = l / (60 * 1000);// 分
            // long s=l/1000;//秒
            long year = day / 365;// 年
            long month = day / 30;// 月
            if (year > 0) {
                return year + "年前";
            } else if (month > 0 && month < 12) {
                return month + "个月前";
            } else if (day > 0 && day < 30) {
                return day + "天前";
            } else if (hours > 0 && hours < 24) {
                return hours + "小时前";
            } else if (minu > 0 && minu < 60) {
                return minu + "分前";
            } else {
                return "刚刚发布";
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return startDate;
    }

    /**
     * @param str
     * @return
     */
    public static String[] sharePicSemicolonHandle(String str) {
        if (isEmpty(str))
            return null;
        return str.split("!#b7");
    }

    /**
     * @param str
     * @return
     */
    public static String[] shareCommaHandle(String str) {
        if (isEmpty(str))
            return null;
        return str.split("~#a6");
    }

    /**
     * 转换日期格式
     *
     * @param time
     * @return yyyy-MM-dd HH:mm格式日期字符串
     * @TODO
     * @2015-5-22
     */
    public static String format(String time) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date date = null;
        try {
            date = format.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return format.format(date);
    }

    /**
     * 转换日期格式
     *
     * @param time
     * @param template
     * @return
     */
    public static String format(String time, String template) {
        SimpleDateFormat format = new SimpleDateFormat(template);
        Date date = null;
        try {
            date = format.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return format.format(date);
    }

    /**
     * 把字符串类型的时间转换成另类型
     *
     * @param str              原数据
     * @param strFormaType     原时间类型
     * @param resultFormatType 要转换的时间类型
     * @return
     * @auth yelk
     */
    public static String formatStrToStr(String str, String strFormaType, String resultFormatType) {
        SimpleDateFormat strFormat = new SimpleDateFormat(strFormaType);
        SimpleDateFormat resultFormat = new SimpleDateFormat(resultFormatType);
        String result = "";
        if (isEmpty(str)) {
            return result;
        }
        try {
            Date date = strFormat.parse(str);
            result = resultFormat.format(date);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 讲string转换成date
     *
     * @param dateString
     * @return
     */
    public static Date formatStrToDate(String dateString) {
        Date date = null;
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        if (isEmpty(dateString)) {
            return date;
        }
        try {
            date = format.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }


    /**
     * 如果非空，则给视图设置文字
     */
    public static void setText(TextView textView, String text) {
        if (!isEmpty(text)) {
            textView.setText(text);
        } else {
            textView.setText("");
        }
    }


    /**
     * 处理距离数字，合理显示距离
     *
     * @param preText  在距离前面的文字
     * @param distance 距离（单位为米）
     */
    public static String getDistanceText(String preText, double distance) {
        double dis = 0;
        String result = "";
        DecimalFormat df = new DecimalFormat("0.00");
        DecimalFormat df2 = new DecimalFormat("0");

        if (distance >= 1000) {
            dis = distance / 1000;
            result = preText + df.format(dis) + "公里";
        } else {
            dis = distance;
            result = preText + df2.format(dis) + "米";
        }

        return result;
    }

    /**
     * 先对原始文字进行非空判断，然后添加一段文字并设置到视图中
     *
     * @param textView     填充数据的视图
     * @param rawText      原始文字
     * @param appendText   添加的文字
     * @param appendBefore 是否是添加在前面
     */
    public static void setText(TextView textView, String rawText, String appendText, boolean appendBefore) {
        String text = "";

        if (!isEmpty(rawText)) {
            text = rawText;
        }

        if (appendBefore) {
            text = appendText + text;
        } else {
            text = text + appendText;
        }

        textView.setText(text);
    }

    /**
     * 先对原始文字进行非空判断，然后添加一段文字并设置到视图中，添加文字在前面
     *
     * @param textView   填充数据的视图
     * @param rawText    原始文字
     * @param appendText 添加的文字
     */
    public static void setText(TextView textView, String rawText, String appendText) {
        setText(textView, rawText, appendText, true);
    }

    /**
     * List集合转成String，中间用“，”分隔
     *
     * @param list
     * @return
     */
    public static String listToStringWithSplit(List<String> list, String split) {
        if (ListUtils.isEmpty(list)) {
            return "";
        }
        StringBuffer sb = new StringBuffer();
        for (String item : list) {
            sb.append(item);
            sb.append(split);
        }
        return sb.toString().substring(0, sb.toString().length() - 1);
    }


    /*****************************待测试*******************************************/
    //1.判断字符串是否仅为数字:
    public static boolean isNumber(String str) {

        for (int i = str.length(); --i >= 0; ) {

            if (!Character.isDigit(str.charAt(i))) {

                return false;

            }

        }

        return true;

    }

//2>用正则表达式

    public static boolean isNumeri(String str) {

        Pattern pattern = Pattern.compile("[0-9]*");

        return pattern.matcher(str).matches();

    }

//3>用ascii码

    public static boolean isNumeric(String str) {

        for (int i = str.length(); --i >= 0; ) {

            int chr = str.charAt(i);

            if (chr < 48 || chr > 57)

                return false;

        }

        return true;

    }


//2.判断一个字符串的首字符是否为字母

    public static boolean test(String s) {
        char c = s.charAt(0);
        int i = (int) c;
        if ((i >= 65 && i <= 90) || (i >= 97 && i <= 122)) {
            return true;
        } else {
            return false;
        }
    }


    public static boolean check(String fstrData) {
        char c = fstrData.charAt(0);
        if (((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z'))) {
            return true;
        } else {
            return false;
        }
    }


//3 .判断是否为汉字

    public static boolean vd(String str) {

        char[] chars = str.toCharArray();
        boolean isGB2312 = false;
        for (int i = 0; i < chars.length; i++) {
            byte[] bytes = ("" + chars[i]).getBytes();
            if (bytes.length == 2) {
                int[] ints = new int[2];
                ints[0] = bytes[0] & 0xff;
                ints[1] = bytes[1] & 0xff;
                if (ints[0] >= 0x81 && ints[0] <= 0xFE && ints[1] >= 0x40 && ints[1] <= 0xFE) {
                    isGB2312 = true;
                    break;
                }
            }
        }
        return isGB2312;
    }


    // android判断EditText输入的数字、中文还是字母方法
    public static void isEditStr(Context context, String txt) {
        Pattern p = Pattern.compile("[0-9]*");
        Matcher m = p.matcher(txt);
        if (m.matches()) {
            Toast.makeText(context, "输入的是数字", Toast.LENGTH_SHORT).show();
        }
        p = Pattern.compile("[a-zA-Z]");
        m = p.matcher(txt);
        if (m.matches()) {
            Toast.makeText(context, "输入的是字母", Toast.LENGTH_SHORT).show();
        }
        p = Pattern.compile("[\u4e00-\u9fa5]");
        m = p.matcher(txt);
        if (m.matches()) {
            Toast.makeText(context, "输入的是汉字", Toast.LENGTH_SHORT).show();
        }
    }

    //    判断字符串中是否仅包含字母数字和汉字
//      *各种字符的unicode编码的范围：
//            * 汉字：[0x4e00,0x9fa5]（或十进制[19968,40869]）
//            * 数字：[0x30,0x39]（或十进制[48, 57]）
//            *小写字母：[0x61,0x7a]（或十进制[97, 122]）
//            * 大写字母：[0x41,0x5a]（或十进制[65, 90]）
    public static boolean isLetterDigitOrChinese(String str) {
        String regex = "^[a-z0-9A-Z\u4e00-\u9fa5]+$";
        return str.matches(regex);
    }

    /**
     * java字节码转字符串
     *
     * @param b
     * @return
     */
    public static String byte2hex(byte[] b) { //一个字节的数，

        // 转成16进制字符串

        String hs = "";
        String tmp = "";
        for (int n = 0; n < b.length; n++) {
            //整数转成十六进制表示

            tmp = (java.lang.Integer.toHexString(b[n] & 0XFF));
            if (tmp.length() == 1) {
                hs = hs + "0" + tmp;
            } else {
                hs = hs + tmp;
            }
        }
        tmp = null;
        return hs.toUpperCase(); //转成大写

    }

    public static String toHexString(String s) {
        String str = "";
        for (int i = 0; i < s.length(); i++) {
            int ch = (int) s.charAt(i);
            String s4 = Integer.toHexString(ch);
            str = str + s4;
        }
        return str;
    }

    /**
     * Convert hex string to byte[]
     *
     * @param hexString the hex string
     * @return byte[]
     */
    public static byte[] hexStringToBytes(String hexString) {
        if (hexString == null || hexString.equals("")) {
            return null;
        }
        //2A51704910EA2922
        hexString = hexString.toUpperCase();
        //8
        int length = hexString.length() / 2;
        //
        char[] hexChars = hexString.toCharArray();
        LogUtils.e("ANKUR", "hexChars : " + hexChars);
        byte[] d = new byte[length];
        for (int i = 0; i < length; i++) {
            int pos = i * 2;
            d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));
        }
        LogUtils.e("ANKUR", "byteArray d : " + d);
        return d;
    }

    /**
     * Convert char to byte
     *
     * @param c char
     * @return byte
     */
    private static byte charToByte(char c) {
        return (byte) "0123456789ABCDEF".indexOf(c);
    }

    /**
     * byte数据转换成字符串
     *
     * @param buffer
     * @return
     */
    public static String getStringGB2312(byte[] buffer) {
        String s = "";
        try {
            s = new String(buffer, "gb2312");//转换成GB2312字符

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return s;
    }

    /**
     * Get hexadecimal random number
     *
     * @param len
     * @return
     * @throws
     */
    public static String randomHexString(int len) {
        try {
            StringBuffer result = new StringBuffer();
            for (int i = 0; i < len; i++) {
                result.append(Integer.toHexString(new Random().nextInt(16)));
            }
            return result.toString().toUpperCase();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }
}
