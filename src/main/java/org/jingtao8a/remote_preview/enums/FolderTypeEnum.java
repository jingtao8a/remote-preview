package org.jingtao8a.remote_preview.enums;

public enum FolderTypeEnum {
    FILE(0, "文件"),
    FOLDER(1, "目录");

    private Integer type;
    private String desc;

    FolderTypeEnum(Integer type, String desc) {
        this.type = type;
        this.desc = desc;
    }

    public Integer getType() {
        return type;
    }

    public String getDesc() {
        return desc;
    }
}
