package org.jingtao8a.remote_preview.service.impl;
import java.util.List;

import org.jingtao8a.remote_preview.config.AppConfig;
import org.jingtao8a.remote_preview.constants.Constants;
import org.jingtao8a.remote_preview.entity.po.FileInfo;
import org.jingtao8a.remote_preview.entity.query.FileInfoQuery;
import org.jingtao8a.remote_preview.enums.ResponseCodeEnum;
import org.jingtao8a.remote_preview.enums.StatusEnum;
import org.jingtao8a.remote_preview.exception.BusinessException;
import org.jingtao8a.remote_preview.utils.ProcessUtils;
import org.jingtao8a.remote_preview.vo.PaginationResultVO;
import org.jingtao8a.remote_preview.service.FileInfoService;
import org.jingtao8a.remote_preview.mapper.FileInfoMapper;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import org.jingtao8a.remote_preview.enums.PageSize;
import org.jingtao8a.remote_preview.entity.query.SimplePage;
import java.io.*;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.*;

/**
@Description:FileInfoService
@Date:2024-09-15
*/
@Service("fileInfoService")
public class FileInfoServiceImpl implements FileInfoService {
	private static Logger logger = LoggerFactory.getLogger(FileInfoServiceImpl.class);

	@Resource
	private FileInfoMapper<FileInfo, FileInfoQuery> fileInfoMapper;

	@Resource
	private ThreadPoolTaskExecutor threadPoolTaskExecutor;

	@Resource
	private AppConfig appConfig;

	@Resource
	private ConcurrentHashMap<String, FileInfo> fileIdMapForTransferVideo;
	/**
	 * 根据条件查询列表
	*/
	@Override
	public List<FileInfo> findListByParam(FileInfoQuery query) {
		return this.fileInfoMapper.selectList(query);
	}

	/**
	 * 根据条件查询数量
	*/
	@Override
	public Long findCountByParam(FileInfoQuery query) {
		return this.fileInfoMapper.selectCount(query);
	}

	/**
	 * 分页查询
	*/
	@Override
	public PaginationResultVO<FileInfo> findListByPage(FileInfoQuery query) {
		Long count = this.findCountByParam(query);
		Long pageSize = query.getPageSize() == null ? PageSize.SIZE15.getSize(): query.getPageSize();
		SimplePage simplePage = new SimplePage(query.getPageNo(), count, pageSize);
		query.setSimplePage(simplePage);
		List<FileInfo> userInfoList = findListByParam(query);
		PaginationResultVO<FileInfo> paginationResultVO = new PaginationResultVO<>(count, simplePage.getPageSize(), simplePage.getPageNo(), simplePage.getPageTotal(), userInfoList);
		return paginationResultVO;
	}

	/**
	 * 新增
	*/
	@Override
	public Long add(FileInfo bean) {
		return fileInfoMapper.insert(bean);
	}

	/**
	 * 新增/修改
	*/
	@Override
	public Long addOrUpdate(FileInfo bean) {
		return fileInfoMapper.insertOrUpdate(bean);
	}

	/**
	 * 批量新增
	*/
	@Override
	public Long addBatch(List<FileInfo> listBean) {
		if (listBean == null || listBean.isEmpty()) {
			return 0L;
		}
		return fileInfoMapper.insertBatch(listBean);
	}

	/**
	 * 批量新增/修改
	*/
	@Override
	 public Long addOrUpdateBatch(List<FileInfo> listBean) {
		if (listBean == null || listBean.isEmpty()) {
			return 0L;
		}
		return fileInfoMapper.insertOrUpdateBatch(listBean);
	}

	/**
	 * 根据FileId查询
	*/
	@Override
	public FileInfo selectByFileId(String fileId) {
		return fileInfoMapper.selectByFileId(fileId);
	}

	/**
	 * 根据FileId更新
	*/
	@Override
	public Long updateByFileId(FileInfo bean, String fileId) {
		return fileInfoMapper.updateByFileId(bean, fileId);
	}

	/**
	 * 根据FileId删除
	*/
	@Override
	public Long deleteByFileId(String fileId) {
		return fileInfoMapper.deleteByFileId(fileId);
	}

//	清空file_info表
	@Override
	public void clear() {
		fileInfoMapper.deleteTable();
	}

	@Override
	public void transferVideo(FileInfo fileInfo) throws BusinessException {
		if (fileIdMapForTransferVideo.putIfAbsent(fileInfo.getFileId(), fileInfo) != null) {
			throw new BusinessException(ResponseCodeEnum.CODE_702.getCode(), String.format("%s 正在转码", fileInfo.getFileName()));
		}
		threadPoolTaskExecutor.submit(()->{
			boolean transferSuccess = true;
			try {
				cutFile4Video(fileInfo.getFileId(), fileInfo.getFilePath());
			} catch (BusinessException e) {
				logger.error("文件转码失败, fileId:{} fileName:{}", fileInfo.getFileId(), fileInfo.getFileName());
				transferSuccess = false;
			} finally {
				FileInfo updateFileInfo = new FileInfo();
				updateFileInfo.setStatus(transferSuccess ? StatusEnum.USING.getStatus() :  StatusEnum.TRANSFER_FAIL.getStatus());
				fileInfoMapper.updateByFileId(updateFileInfo, fileInfo.getFileId());
			}
		});
	}

	private void cutFile4Video(String fileId, String videoFilePath) throws BusinessException {
		//创建同名切片目录
		File tsFolder = new File(appConfig.getProjectFolder() + Constants.TEMP_FILE_DIR + fileId);
		if (!tsFolder.exists()) {
			tsFolder.mkdirs();
		}
//		final String CMD_TRANSFER_2TS = "ffmpeg -y -i \"%s\"  -vcodec copy -acodec copy -bsf:v hevc_mp4toannexb %s";
		final String CMD_TRANSFER_2TS = "ffmpeg -y -i \"%s\"  -vcodec copy -acodec copy %s";
		final String CMD_CUT_TS = "ffmpeg -i %s -c copy -map 0 -f segment -segment_list %s -segment_time 30 %s/%s_%%4d.ts";
		String tsPath = tsFolder + "/" + Constants.TS_NAME;
		//生成.ts
		String cmd = String.format(CMD_TRANSFER_2TS, videoFilePath, tsPath);
		ProcessUtils.executeCommand(cmd, true);
		//生成索引文件m3u8和切片.ts
		cmd = String.format(CMD_CUT_TS, tsPath, tsFolder.getPath() + "/" + Constants.M3U8_NAME, tsFolder.getPath(), fileId);
		ProcessUtils.executeCommand(cmd, true);
		//删除index.ts
		new File(tsPath).delete();
	}
}