package org.jingtao8a.remote_preview.controller;
import java.util.List;
import org.jingtao8a.remote_preview.entity.po.FileInfo;
import org.jingtao8a.remote_preview.entity.query.FileInfoQuery;
import org.jingtao8a.remote_preview.service.FileInfoService;
import org.jingtao8a.remote_preview.vo.PaginationResultVO;
import org.jingtao8a.remote_preview.vo.ResponseVO;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.annotation.Resource;
import org.springframework.web.bind.annotation.RequestBody;
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

}