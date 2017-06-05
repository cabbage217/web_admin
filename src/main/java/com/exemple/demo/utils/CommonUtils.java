package com.exemple.demo.utils;

import com.google.common.base.Charsets;
import com.google.common.hash.HashFunction;
import com.google.common.hash.Hasher;
import com.google.common.hash.Hashing;

import java.util.List;

/**
 * Created by Caby on 2017-06-02 3:45 PM.
 */
public class CommonUtils {
    /**
     * 计算sha1值
     * @param strings List<String>
     * @return String
     */
    static public String sha1(List<String> strings) {
        if (strings == null || strings.isEmpty()) return "";
        HashFunction hf = Hashing.sha1();
        Hasher hasher = hf.newHasher();
        for (String str : strings) {
            hasher.putString(str, Charsets.US_ASCII);
        }
        return hasher.hash().toString();
    }
}
