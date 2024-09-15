package org.jingtao8a.remote_preview.controller;
import org.apache.commons.lang3.StringUtils;
import org.jingtao8a.remote_preview.entity.po.FileInfo;
import org.jingtao8a.remote_preview.entity.query.FileInfoQuery;
import org.jingtao8a.remote_preview.enums.FolderTypeEnum;
import org.jingtao8a.remote_preview.service.FileInfoService;
import org.jingtao8a.remote_preview.vo.PaginationResultVO;
import org.jingtao8a.remote_preview.vo.ResponseVO;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import javax.annotation.Resource;
import java.util.List;

/**
@Description:FileInfoController
@Date:2024-09-15
*/
@RestController
@RequestMapping("/file")
public class FileInfoController extends ABaseController {

	@Resource
	private FileInfoService fileInfoService;
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
}