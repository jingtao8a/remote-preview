package org.jingtao8a.remote_preview.utils;

import org.apache.commons.lang3.RandomStringUtils;

public class StringTools {
    public static String getRandomString(int count) {
        return RandomStringUtils.random(count, true, true);
    }

    public static String getRandomNumber(int count) {
        return RandomStringUtils.random(count, false, true);
    }

    public static String getFileNameNoSuffix(String fileName) {
        int index = fileName.lastIndexOf(".");
        if (index == -1) {
            return fileName;
        }
        return fileName.substring(0, index);
    }
    public static String getFileSuffix(String fileName) {
        int index = fileName.lastIndexOf(".");
        if (index == -1) {
            return "";
        }
        return fileName.substring(index);
    }
}
