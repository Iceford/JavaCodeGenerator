package com.example.demo.service;

import java.util.List;

import com.example.demo.entity.query.ForumArticleAttachmentDownloadQuery;
import com.example.demo.entity.po.ForumArticleAttachmentDownload;
import com.example.demo.entity.vo.PaginationResultVO;


/**
 * 用户附件下载 业务接口
 */
public interface ForumArticleAttachmentDownloadService {

	/**
	 * 根据条件查询列表
	 */
	List<ForumArticleAttachmentDownload> findListByParam(ForumArticleAttachmentDownloadQuery param);

	/**
	 * 根据条件查询列表
	 */
	Integer findCountByParam(ForumArticleAttachmentDownloadQuery param);

	/**
	 * 分页查询
	 */
	PaginationResultVO<ForumArticleAttachmentDownload> findListByPage(ForumArticleAttachmentDownloadQuery param);

	/**
	 * 新增
	 */
	Integer add(ForumArticleAttachmentDownload bean);

	/**
	 * 批量新增
	 */
	Integer addBatch(List<ForumArticleAttachmentDownload> listBean);

	/**
	 * 批量新增/修改
	 */
	Integer addOrUpdateBatch(List<ForumArticleAttachmentDownload> listBean);

	/**
	 * 多条件更新
	 */
	Integer updateByParam(ForumArticleAttachmentDownload bean,ForumArticleAttachmentDownloadQuery param);

	/**
	 * 多条件删除
	 */
	Integer deleteByParam(ForumArticleAttachmentDownloadQuery param);

	/**
	 * 根据FileIdAndUserId查询对象
	 */
	ForumArticleAttachmentDownload getForumArticleAttachmentDownloadByFileIdAndUserId(String fileId,String userId);


	/**
	 * 根据FileIdAndUserId修改
	 */
	Integer updateForumArticleAttachmentDownloadByFileIdAndUserId(ForumArticleAttachmentDownload bean,String fileId,String userId);


	/**
	 * 根据FileIdAndUserId删除
	 */
	Integer deleteForumArticleAttachmentDownloadByFileIdAndUserId(String fileId,String userId);

}