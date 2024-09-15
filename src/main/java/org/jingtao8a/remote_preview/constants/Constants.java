package org.jingtao8a.remote_preview.constants;

public class Constants {
    public static final Integer FILE_ID_LENGTH = 20;
    public static final String ROOT_FILE_ID = "0";
    public static final String TEMP_FILE_DIR = "/temp_file/";
    public static final String COVER_SUFFIX = ".jpg";
    public static final Integer LENGTH_150 = 150;
    public static final Integer LENGTH_50 = 50;
    public static final String TS_NAME = "index.ts";
    public static final String M3U8_NAME = "index.m3u8";
    public static final Integer REDIS_KEY_EXPIRES_ONE_MIN = 60;
    public static final Integer REDIS_KEY_EXPIRES_FIVE_MIN = REDIS_KEY_EXPIRES_ONE_MIN * 5;
    public static final String REDIS_KEY_DOWNLOAD= "remote-preview:download:";
}
