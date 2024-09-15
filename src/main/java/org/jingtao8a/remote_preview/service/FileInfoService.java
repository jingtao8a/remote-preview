package org.jingtao8a.remote_preview.service;
import java.util.List;
import org.jingtao8a.remote_preview.entity.po.FileInfo;
import org.jingtao8a.remote_preview.entity.query.FileInfoQuery;
import org.jingtao8a.remote_preview.exception.BusinessException;
import org.jingtao8a.remote_preview.vo.PaginationResultVO;

/**
@Description:Service
@Date:2024-09-15
*/
public interface FileInfoService {

	/**
	 * 根据条件查询列表
	*/
	List<FileInfo> findListByParam(FileInfoQuery param);

	/**
	 * 根据条件查询数量
	*/
	Long findCountByParam(FileInfoQuery param);

	/**
	 * 分页查询
	*/
	PaginationResultVO<FileInfo> findListByPage(FileInfoQuery param);

	/**
	 * 新增
	*/
	Long add(FileInfo bean);

	/**
	 * 新增/修改
	*/
	Long addOrUpdate(FileInfo bean);

	/**
	 * 批量新增
	*/
	Long addBatch(List<FileInfo> listBean);

	/**
	 * 批量新增/修改
	*/
	Long addOrUpdateBatch(List<FileInfo> listBean);

	/**
	 * 根据FileId查询
	*/
	FileInfo selectByFileId(String fileId);

	/**
	 * 根据FileId更新
	*/
	Long updateByFileId(FileInfo bean, String fileId);

	/**
	 * 根据FileId删除
	*/
	Long deleteByFileId(String fileId);

	void clear();

	void transferVideo(FileInfo fileId) throws BusinessException;
}