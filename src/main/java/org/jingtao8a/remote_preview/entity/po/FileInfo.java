package org.jingtao8a.remote_preview.entity.po;
import lombok.Data;
import lombok.ToString;
import java.io.Serializable;
/**
@Description:
@Date:2024-09-15
*/
@Data
@ToString
public class FileInfo implements Serializable {
	/**
	 * 文件id
	*/
	private String fileId;
	/**
	 * 文件父id
	*/
	private String filePid;
	/**
	 * 文件大小
	*/
	private Long fileSize;
	/**
	 * 文件名
	*/
	private String fileName;
	/**
	 * 0:文件 1:目录
	*/
	private Integer folderType;
	/**
	 * 1:视频 2：音频 3：图片 4：pdf 5:doc  6:excel  7:txt 8:code 9:zip 10:其它
	*/
	private Integer fileType;
	/**
	 * 0:转码中 1：转码失败 2：转码成功
	*/
	private Integer status;
	/**
	 * 封面（图片、视频）
	*/
	private String fileCover;
	/**
	 * 文件路径
	*/
	private String filePath;
}