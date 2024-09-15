package org.jingtao8a.remote_preview.controller;
import org.apache.commons.lang3.StringUtils;
import org.jingtao8a.remote_preview.config.AppConfig;
import org.jingtao8a.remote_preview.constants.Constants;
import org.jingtao8a.remote_preview.entity.po.FileInfo;
import org.jingtao8a.remote_preview.entity.query.FileInfoQuery;
import org.jingtao8a.remote_preview.enums.FileTypeEnum;
import org.jingtao8a.remote_preview.enums.FolderTypeEnum;
import org.jingtao8a.remote_preview.enums.ResponseCodeEnum;
import org.jingtao8a.remote_preview.enums.StatusEnum;
import org.jingtao8a.remote_preview.exception.BusinessException;
import org.jingtao8a.remote_preview.service.FileInfoService;
import org.jingtao8a.remote_preview.utils.StringTools;
import org.jingtao8a.remote_preview.vo.PaginationResultVO;
import org.jingtao8a.remote_preview.vo.ResponseVO;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.io.*;

/**
@Description:FileInfoController
@Date:2024-09-15
*/
@RestController
@RequestMapping("/file")
public class FileInfoController extends ABaseController {

	@Resource
	private FileInfoService fileInfoService;
	@Resource
	private AppConfig appConfig;
	/**
	 * 分页查询
	*/
	@RequestMapping("/loadDataList")
	public ResponseVO loadDataList(FileInfoQuery query) {
		PaginationResultVO<FileInfo>  paginationResultVO = fileInfoService.findListByPage(query);
		return getSuccessResponseVO(paginationResultVO);
	}

	@RequestMapping("/getFolderInfo")
	public ResponseVO getFolderInfo(@RequestParam("path") String path) {
		String[] pathArray = path.split("/");
		FileInfoQuery fileInfoQuery = new FileInfoQuery();
		fileInfoQuery.setFolderType(FolderTypeEnum.FOLDER.getType());
		fileInfoQuery.setFileIdArray(pathArray);
		String orderBy = "field(file_id,\"" + StringUtils.join(pathArray, "\",\"") + "\")";
		fileInfoQuery.setOrderBy(orderBy);
		List<FileInfo> fileInfoList = fileInfoService.findListByParam(fileInfoQuery);
		return getSuccessResponseVO(fileInfoList);
	}

	@RequestMapping("/getCover/{coverName}")
	//coverName默认为fileId.jpg
	public void getCover(HttpServletResponse response, @PathVariable("coverName") String coverName) {
		String coverSuffix = StringTools.getFileSuffix(coverName);
		String filePath = appConfig.getProjectFolder() + Constants.TEMP_FILE_DIR + coverName;
		coverSuffix = coverSuffix.replace(".", "");
		String contentType = "image/" + coverSuffix;
		response.setContentType(contentType);
		response.setHeader("Cache-Control", "max-age=2592000");
		if (!new File(filePath).exists()) {//文件不存在
			//do nothing
			logger.info("cover {} not exist", filePath);
		}
		readFile(response, filePath);
	}

	@RequestMapping("/getFile/{fileId}")
	//获取除了video其它文件信息
	public void getFile(HttpServletResponse response, @PathVariable("fileId") String fileId) throws BusinessException {
		FileInfo fileInfo = fileInfoService.selectByFileId(fileId);
		if (fileInfo == null) {//文件不存在
			throw new BusinessException(ResponseCodeEnum.CODE_700);
		}
		assert(!fileInfo.getFileType().equals(FileTypeEnum.VIDEO.getType()));//getFile不请求video文件
		assert(fileInfo.getStatus().equals(StatusEnum.USING.getStatus()));//除了video外的其它文件不需要转码
		readFile(response, fileInfo.getFilePath());
	}

	@RequestMapping("/getVideo/{fileId}")
	public void getVideo(HttpServletResponse response, @PathVariable("fileId") String fileId) throws BusinessException {
		FileInfo fileInfo = null;
		String filePath = null;
		if (fileId.endsWith(".ts")) {//请求的是ts文件，说明video已经转码成功了
			String realFileId = fileId.split("_")[0];
			filePath = appConfig.getProjectFolder() + Constants.TEMP_FILE_DIR + realFileId + "/" + fileId;
		} else {
			fileInfo = fileInfoService.selectByFileId(fileId);
			if (fileInfo == null) {//文件不存在
				throw new BusinessException(ResponseCodeEnum.CODE_700);
			}
			assert(fileInfo.getFileType().equals(FileTypeEnum.VIDEO.getType()));//默认请求video文件
			if (fileInfo.getStatus().equals(StatusEnum.TRANSFER_FAIL.getStatus())) {
				throw new BusinessException(ResponseCodeEnum.CODE_701);
			}
			if (fileInfo.getStatus().equals(StatusEnum.TRANSFER.getStatus())) {
				fileInfoService.transferVideo(fileInfo);
				throw new BusinessException(ResponseCodeEnum.CODE_702);
			}
			filePath = appConfig.getProjectFolder() + Constants.TEMP_FILE_DIR + fileId + "/" + Constants.M3U8_NAME;
		}
		readFile(response, filePath);
	}
}