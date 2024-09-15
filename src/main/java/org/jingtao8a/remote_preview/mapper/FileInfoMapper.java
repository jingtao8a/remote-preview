package org.jingtao8a.remote_preview.mapper;
import org.apache.ibatis.annotations.Param;
/**
@Description:Mapper
@Date:2024-09-15
*/

public interface FileInfoMapper<T,P> extends BaseMapper {
	/**
	 * 根据FileId查询
	*/
	 T selectByFileId(@Param("fileId") String fileId);

	/**
	 * 根据FileId更新
	*/
	 Long updateByFileId(@Param("bean") T t, @Param("fileId") String fileId);

	/**
	 * 根据FileId删除
	*/
	 Long deleteByFileId(@Param("fileId") String fileId);

	 // 清空fileInfo
	 Long deleteTable();
}