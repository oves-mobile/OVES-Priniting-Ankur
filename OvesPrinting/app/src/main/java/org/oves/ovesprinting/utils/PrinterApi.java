package org.oves.ovesprinting.utils;

import android.graphics.Bitmap;

import com.example.printlibrary.utils.StringUtils;
import com.lvrenyang.io.Pos;
import com.wisdom.tian.utils.ImageUtil;

/**
 * 公司： 志众电子有限公司
 * 说明： 志众打印机API
 * 作者： 湖中鸟
 * QQ:    403949692
 */
public class PrinterApi {

    // byte1+byte2
    public static byte[] byteMerger(byte[] byte_1, byte[] byte_2) {
        int size = 0;
        if (byte_1 != null){
            size += byte_1.length;
        }
        if (byte_2 != null){
            size += byte_2.length;
        }
        byte[] byte_3 = new byte[size];
        int pos = 0;
        if (byte_1 != null) {
            System.arraycopy(byte_1, 0, byte_3, pos, byte_1.length);
            pos += byte_1.length;
        }
        if (byte_2 != null) {
            System.arraycopy(byte_2, 0, byte_3, pos, byte_2.length);
            //pos += byte_2.length;
        }
        return byte_3;
    }

    // 获取打印GBK字符串
    public static byte[] getPrintStringGBKBytes(String str){
        try {
            byte [] data = str.getBytes("GBK");
            return data;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    // 获取打印UTF8字符串
    public static byte[] getPrintStringUTF8Bytes(String str){
        try {
            byte [] data = str.getBytes("UTF8");
            return data;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    // 获取128条码
    // str 输入只能是英文字符或数字
    public static byte[] getPrintBarcodeBytes(String str){
        try {
            byte []data = getPrintStringGBKBytes(str);      // 输入只能是英文字符或数字
            //byte []data = getPrintStringUTF8Bytes(str);      // 输入只能是英文字符或数字
            data = byteMerger(StringUtils.hexStringToBytes("1d6b08"), data);
            data = byteMerger(data, StringUtils.hexStringToBytes("00"));
            return data;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    // 获取128条码
    // str 输入只能是英文字符或数字
    // textPos: 文本位置
    //      0   不打印文本
    //      1   在条码上面
    //      2   在条码下面
    //      3   条码上下两个位置
    // widht： 条码宽度放大倍数: 1---6
    // height：条码高度:  1--n
    public static byte[] getPrintBarcodeBytes(String str, int textPos, int width_X, int height){
        try {
            byte[] data_pos = byteMerger(StringUtils.hexStringToBytes("1d48"), new byte[]{(byte)textPos});   // 文本位置
            byte[] data_height = byteMerger(StringUtils.hexStringToBytes("1d68"), new byte[]{(byte)height});   // 条码高度
            byte[] data_widht = byteMerger(StringUtils.hexStringToBytes("1d77"), new byte[]{(byte)width_X});   // 条码宽度放大倍数

            data_pos = byteMerger(data_pos, data_height);
            data_pos = byteMerger(data_pos, data_widht);
            byte []data = getPrintBarcodeBytes(str);      // 输入只能是英文字符或数字
            data = byteMerger(data_pos, data);
            return data;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    // 数据长度不能超过128bytes
    public static byte[] getQrcodeBytes(int size, int errLevel, byte[] data){
        if (size > 16) size = 16;
        if (errLevel > 3) errLevel = 3;
        byte[] data_size = byteMerger(StringUtils.hexStringToBytes("1d286b03003143"), new byte[]{(byte)size});   // 二维码大小
        byte[] data_err = byteMerger(StringUtils.hexStringToBytes("1d286b03003145"), new byte[]{(byte)(0x30+errLevel)});   // 二维码错误等级 30/31/32/33
        data_size = byteMerger(data_size, data_err);
        // 二维码数据
        byte [] data_head = byteMerger(StringUtils.hexStringToBytes("1d286B"),
                new byte[]{(byte)((data.length+3)&0xFF), 0x00, 0x31, 0x50, 0x30});
        data = byteMerger(data_head, data);

        data = byteMerger(data_size, data);
        return data;
    }

    // 获取二维码
    // 输入可以为中文
    // size: 0--16   0表示不改变大小
    // errLevel:  0-3
    // 数据长度不能超过128bytes
    public static byte[] getPrintQrcodeGBKBytes(String str, int size, int errLevel){
        try {
            byte []data = getPrintStringGBKBytes(str);
            return getQrcodeBytes(size, errLevel, data);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    // 获取二维码
    // 输入可以为中文
    // size: 0--16   0表示不改变大小
    // errLevel:  0-3
    // 数据长度不能超过128bytes
    public static byte[] getPrintQrcodeUTF8Bytes(String str, int size, int errLevel){
        try {
            byte []data = getPrintStringUTF8Bytes(str);
            return getQrcodeBytes(size, errLevel, data);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    // 以下打印图片指令，适用于所有打印机。
    // bitmap: 位图
    // printWidth: 打印宽度， 不得超过打印头最大宽度（如384，576等）
    //             若位图宽度比打印头大， 则会根据printWidht尺寸裁剪
    // bPicture:   是否按照图片方式解析打印.
    //              图片方式解析，会压缩部分信息。对于有文字的图片，可能会失真。
    public static byte[] getPrintBitmap(Bitmap bitmap, int printWidth, boolean bPicture){
        return Pos.POS_Bitmap2Data(bitmap, printWidth, bPicture?0:1, 0);
    }

    // 以下打印图片指令，适用于专用的标签打印机。数据有压缩, , 压缩率较低
    // 不是所有机器都支持。 具体咨询 志众电子...
    // bitmap: 位图
    // printWidth: 打印宽度， 不得超过打印头最大宽度（如384，576等）
    //             若位图宽度比打印头大， 则会根据printWidht尺寸裁剪
    // bPicture:   是否按照图片方式解析打印.
    //              图片方式解析，会压缩部分信息。对于有文字的图片，可能会失真。
    public static byte[] getPrintBitmap_zz(Bitmap bitmap, int printWidth, boolean bPicture){
        return ImageUtil.Bitmap2PosCmd(bitmap, printWidth, bPicture);
    }


    // 以下打印图片指令，适用于专用的标签打印机。数据有压缩, 数据压缩率较高, 速度较快
    // 不是所有机器都支持。 具体咨询 志众电子...
    // bitmap: 位图
    // printWidth: 打印宽度， 不得超过打印头最大宽度（如384，576等）
    //             若位图宽度比打印头大， 则会根据printWidht尺寸裁剪
    // bPicture:   是否按照图片方式解析打印.
    //              图片方式解析，会压缩部分信息。对于有文字的图片，可能会失真。
    public static byte[] getPrintBitmapFast_zz(Bitmap bitmap, int printWidth, boolean bPicture){
        return ImageUtil.Bitmap2PosFastPictureCmd(bitmap, printWidth, bPicture);
    }

    // convert bitmap 2 TSPL command
    public static byte[] getPrintBitmapTspl(Bitmap bitmap, int printWidth, boolean bPicture, boolean bRev){
        return ImageUtil.bitmap2TsplDotsmap(bitmap, printWidth, bPicture, bRev);
    }


}
