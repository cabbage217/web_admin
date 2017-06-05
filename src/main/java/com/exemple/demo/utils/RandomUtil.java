package com.exemple.demo.utils;

import java.util.Random;

/**
 * Created by Caby on 2017-05-25 9:27 PM.
 */
public class RandomUtil {
    private static final String CharSet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789abcdefghijklmnopqrstuvwxyz";

    /**
     * 生成一个指定长度的字符串
     * @param length Integer, 要生成的字符串的长度
     * @return String
     */
    static public String randomString(Integer length) {
        Random random = new Random();
        char[] text = new char[length];
        for (int i = 0; i < length; ++i) {
            text[i] = CharSet.charAt(random.nextInt(CharSet.length()));
        }
        return new String(text);
    }
}
