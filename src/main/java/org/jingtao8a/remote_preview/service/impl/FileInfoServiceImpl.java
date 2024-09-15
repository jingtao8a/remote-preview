package org.jingtao8a.remote_preview.service.impl;
import java.util.List;
import org.jingtao8a.remote_preview.entity.po.FileInfo;
import org.jingtao8a.remote_preview.entity.query.FileInfoQuery;
import org.jingtao8a.remote_preview.vo.PaginationResultVO;
import org.jingtao8a.remote_preview.service.FileInfoService;
import org.jingtao8a.remote_preview.mapper.FileInfoMapper;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import org.jingtao8a.remote_preview.enums.PageSize;
import org.jingtao8a.remote_preview.entity.query.SimplePage;
/**
@Description:FileInfoService
@Date:2024-09-15
*/
@Service("fileInfoService")
public class FileInfoServiceImpl implements FileInfoService {

	@Resource
	private FileInfoMapper<FileInfo, FileInfoQuery> fileInfoMapper;
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

}