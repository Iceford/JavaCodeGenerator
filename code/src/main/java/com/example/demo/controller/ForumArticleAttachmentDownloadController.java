package com.example.demo.controller;

import java.util.List;

import com.example.demo.entity.query.ForumArticleAttachmentDownloadQuery;
import com.example.demo.entity.po.ForumArticleAttachmentDownload;
import com.example.demo.entity.vo.ResponseVO;
import com.example.demo.service.ForumArticleAttachmentDownloadService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 用户附件下载 Controller
 */
@RestController("forumArticleAttachmentDownloadController")
@RequestMapping("/forumArticleAttachmentDownload")
public class ForumArticleAttachmentDownloadController extends ABaseController{

	@Resource
	private ForumArticleAttachmentDownloadService forumArticleAttachmentDownloadService;
	/**
	 * 根据条件分页查询
	 */
	@RequestMapping("/loadDataList")
	public ResponseVO loadDataList(ForumArticleAttachmentDownloadQuery query){
		return getSuccessResponseVO(forumArticleAttachmentDownloadService.findListByPage(query));
	}

	/**
	 * 新增
	 */
	@RequestMapping("/add")
	public ResponseVO add(ForumArticleAttachmentDownload bean) {
		forumArticleAttachmentDownloadService.add(bean);
		return getSuccessResponseVO(null);
	}

	/**
	 * 批量新增
	 */
	@RequestMapping("/addBatch")
	public ResponseVO addBatch(@RequestBody List<ForumArticleAttachmentDownload> listBean) {
		forumArticleAttachmentDownloadService.addBatch(listBean);
		return getSuccessResponseVO(null);
	}

	/**
	 * 批量新增/修改
	 */
	@RequestMapping("/addOrUpdateBatch")
	public ResponseVO addOrUpdateBatch(@RequestBody List<ForumArticleAttachmentDownload> listBean) {
		forumArticleAttachmentDownloadService.addBatch(listBean);
		return getSuccessResponseVO(null);
	}

	/**
	 * 根据FileIdAndUserId查询对象
	 */
	@RequestMapping("/getForumArticleAttachmentDownloadByFileIdAndUserId")
	public ResponseVO getForumArticleAttachmentDownloadByFileIdAndUserId(String fileId,String userId) {
		return getSuccessResponseVO(forumArticleAttachmentDownloadService.getForumArticleAttachmentDownloadByFileIdAndUserId(fileId,userId));
	}

	/**
	 * 根据FileIdAndUserId修改对象
	 */
	@RequestMapping("/updateForumArticleAttachmentDownloadByFileIdAndUserId")
	public ResponseVO updateForumArticleAttachmentDownloadByFileIdAndUserId(ForumArticleAttachmentDownload bean,String fileId,String userId) {
		forumArticleAttachmentDownloadService.updateForumArticleAttachmentDownloadByFileIdAndUserId(bean,fileId,userId);
		return getSuccessResponseVO(null);
	}

	/**
	 * 根据FileIdAndUserId删除
	 */
	@RequestMapping("/deleteForumArticleAttachmentDownloadByFileIdAndUserId")
	public ResponseVO deleteForumArticleAttachmentDownloadByFileIdAndUserId(String fileId,String userId) {
		forumArticleAttachmentDownloadService.deleteForumArticleAttachmentDownloadByFileIdAndUserId(fileId,userId);
		return getSuccessResponseVO(null);
	}
}