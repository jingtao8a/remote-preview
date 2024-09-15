package org.jingtao8a.remote_preview.controller;
import java.util.List;
import org.jingtao8a.remote_preview.entity.po.FileInfo;
import org.jingtao8a.remote_preview.entity.query.FileInfoQuery;
import org.jingtao8a.remote_preview.service.FileInfoService;
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
@RequestMapping("/fileInfo")
public class FileInfoController extends ABaseController {

	@Resource
	private FileInfoService fileInfoService;
	/**
	 * 分页查询
	*/
	@RequestMapping("loadDataList")
	public ResponseVO loadDataList(FileInfoQuery query) {
		return getSuccessResponseVO(fileInfoService.findListByPage(query));
	}

	/**
	 * 新增
	*/
	@RequestMapping("add")
	public ResponseVO add(FileInfo bean) {
		fileInfoService.add(bean);
		return getSuccessResponseVO(null);
	}

	/**
	 * 新增/修改
	*/
	@RequestMapping("addOrUpdate")
	public ResponseVO addOrUpdate(FileInfo bean) {
		fileInfoService.addOrUpdate(bean);
		return getSuccessResponseVO(null);
	}

	/**
	 * 批量新增
	*/
	@RequestMapping("addBatch")
	public ResponseVO addBatch(@RequestBody List<FileInfo> listBean) {
		fileInfoService.addBatch(listBean);
		return getSuccessResponseVO(null);
	}

	/**
	 * 批量新增/修改
	*/
	@RequestMapping("addOrUpdateBatch")
	 public ResponseVO addOrUpdateBatch(@RequestBody List<FileInfo> listBean) {
		fileInfoService.addOrUpdateBatch(listBean);
		return getSuccessResponseVO(null);
	}

	/**
	 * 根据FileId查询
	*/
	@RequestMapping("getFileInfoByFileId")
	public  ResponseVO getFileInfoByFileId(String fileId) {
		return getSuccessResponseVO(fileInfoService.selectByFileId(fileId));
	}

	/**
	 * 根据FileId更新
	*/
	@RequestMapping("updateFileInfoByFileId")
	public ResponseVO updateFileInfoByFileId(FileInfo bean, String fileId) {
		fileInfoService.updateByFileId(bean, fileId);
		return getSuccessResponseVO(null);
	}

	/**
	 * 根据FileId删除
	*/
	@RequestMapping("deleteFileInfoByFileId")
	public ResponseVO deleteFileInfoByFileId(String fileId) {
		fileInfoService.deleteByFileId(fileId);
		return getSuccessResponseVO(null);
	}

}