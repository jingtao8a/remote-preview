package org.jingtao8a.remote_preview;

import org.jingtao8a.remote_preview.config.AppConfig;
import org.jingtao8a.remote_preview.constants.Constants;
import org.jingtao8a.remote_preview.entity.po.FileInfo;
import org.jingtao8a.remote_preview.entity.query.FileInfoQuery;
import org.jingtao8a.remote_preview.enums.FileTypeEnum;
import org.jingtao8a.remote_preview.enums.FolderTypeEnum;
import org.jingtao8a.remote_preview.enums.StatusEnum;
import org.jingtao8a.remote_preview.mapper.FileInfoMapper;
import org.jingtao8a.remote_preview.service.FileInfoService;
import org.jingtao8a.remote_preview.utils.StringTools;
import org.junit.jupiter.api.Test;
import org.slf4j.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Resource;

@SpringBootTest(classes = {org.jingtao8a.remote_preview.RemotePreviewApplication.class})
class RemotePreviewApplicationTests {
	private Logger logger = LoggerFactory.getLogger(RemotePreviewApplicationTests.class);
	@Resource
	private AppConfig appConfig;
	@Resource
	private FileInfoService fileInfoService;

	@Test
	public void clearApp() {
		fileInfoService.clear();
	}
	@Test
//	@Transactional(rollbackFor = Exception.class)
	public void initApp() throws IOException {
		System.out.println(appConfig.getRemotePreviewMaterial());
//		System.out.println(appConfig.getLogRootLevel());
		File remotePreviewMaterialDir = new File(appConfig.getRemotePreviewMaterial());
		if (!remotePreviewMaterialDir.exists()) {
			logger.error("remotePreviewMaterial does not exist or is not a directory");
			return;
		}
		if (!remotePreviewMaterialDir.isDirectory()) {
			logger.error("remotePreviewMaterial is not a directory");
			return;
		}
		logger.info("start scan remotePreviewMaterial");
		List<FileInfo> fileInfoList = new ArrayList<>();
		File[] fileList = remotePreviewMaterialDir.listFiles();
		for (File file : fileList) {
			FileInfo newFileInfo = new FileInfo();
			String fileId = StringTools.getRandomString(Constants.FILE_ID_LENGTH);
			newFileInfo.setFileId(fileId);
			newFileInfo.setFilePid(Constants.ROOT_FILE_ID);
			newFileInfo.setFileName(file.getName());

			if (file.isDirectory()) {//目录
				newFileInfo.setFolderType(FolderTypeEnum.FOLDER.getType());
				newFileInfo.setStatus(StatusEnum.USING.getStatus());
				fileInfoList.add(newFileInfo);
				traverseDirectory(fileInfoList, file, fileId);
			} else if (file.isFile()) {//文件
				newFileInfo.setFileSize(file.getTotalSpace());
				newFileInfo.setFolderType(FolderTypeEnum.FILE.getType());
				newFileInfo.setFileType(FileTypeEnum.getFileTypeBySuffix(StringTools.getFileSuffix(file.getName())).getType());
				newFileInfo.setStatus(StatusEnum.TRANSFER.getStatus());
				newFileInfo.setFilePath(file.getCanonicalPath());
				fileInfoList.add(newFileInfo);
			}
		}
//		System.out.println(fileInfoList);
		fileInfoService.addBatch(fileInfoList);
	}
	void traverseDirectory(List<FileInfo> fileInfoList, File dir, String dirFileId) throws IOException {
		File[] fileList = dir.listFiles();
		for (File file : fileList) {
			FileInfo newFileInfo = new FileInfo();
			String fileId = StringTools.getRandomString(Constants.FILE_ID_LENGTH);
			newFileInfo.setFileId(fileId);
			newFileInfo.setFilePid(dirFileId);
			newFileInfo.setFileName(file.getName());

			if (file.isDirectory()) {//目录
				newFileInfo.setFolderType(FolderTypeEnum.FOLDER.getType());
				newFileInfo.setStatus(StatusEnum.USING.getStatus());
				fileInfoList.add(newFileInfo);
				traverseDirectory(fileInfoList, file, fileId);
			} else if (file.isFile()) {//文件
				newFileInfo.setFileSize(file.getTotalSpace());
				newFileInfo.setFolderType(FolderTypeEnum.FILE.getType());
				newFileInfo.setFileType(FileTypeEnum.getFileTypeBySuffix(StringTools.getFileSuffix(file.getName())).getType());
				newFileInfo.setStatus(StatusEnum.TRANSFER.getStatus());
				newFileInfo.setFilePath(file.getCanonicalPath());
				fileInfoList.add(newFileInfo);
			}
		}
	}
}
