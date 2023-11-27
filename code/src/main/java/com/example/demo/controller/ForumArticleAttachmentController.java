package com.example.demo.controller;

import java.util.List;

import com.example.demo.entity.query.ForumArticleAttachmentQuery;
import com.example.demo.entity.po.ForumArticleAttachment;
import com.example.demo.entity.vo.ResponseVO;
import com.example.demo.service.ForumArticleAttachmentService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 文件信息 Controller
 */
@RestController("forumArticleAttachmentController")
@RequestMapping("/forumArticleAttachment")
public class ForumArticleAttachmentController extends ABaseController{

	@Resource
	private ForumArticleAttachmentService forumArticleAttachmentService;
	/**
	 * 根据条件分页查询
	 */
	@RequestMapping("/loadDataList")
	public ResponseVO loadDataList(ForumArticleAttachmentQuery query){
		return getSuccessResponseVO(forumArticleAttachmentService.findListByPage(query));
	}

	/**
	 * 新增
	 */
	@RequestMapping("/add")
	public ResponseVO add(ForumArticleAttachment bean) {
		forumArticleAttachmentService.add(bean);
		return getSuccessResponseVO(null);
	}

	/**
	 * 批量新增
	 */
	@RequestMapping("/addBatch")
	public ResponseVO addBatch(@RequestBody List<ForumArticleAttachment> listBean) {
		forumArticleAttachmentService.addBatch(listBean);
		return getSuccessResponseVO(null);
	}

	/**
	 * 批量新增/修改
	 */
	@RequestMapping("/addOrUpdateBatch")
	public ResponseVO addOrUpdateBatch(@RequestBody List<ForumArticleAttachment> listBean) {
		forumArticleAttachmentService.addBatch(listBean);
		return getSuccessResponseVO(null);
	}

	/**
	 * 根据FileId查询对象
	 */
	@RequestMapping("/getForumArticleAttachmentByFileId")
	public ResponseVO getForumArticleAttachmentByFileId(String fileId) {
		return getSuccessResponseVO(forumArticleAttachmentService.getForumArticleAttachmentByFileId(fileId));
	}

	/**
	 * 根据FileId修改对象
	 */
	@RequestMapping("/updateForumArticleAttachmentByFileId")
	public ResponseVO updateForumArticleAttachmentByFileId(ForumArticleAttachment bean,String fileId) {
		forumArticleAttachmentService.updateForumArticleAttachmentByFileId(bean,fileId);
		return getSuccessResponseVO(null);
	}

	/**
	 * 根据FileId删除
	 */
	@RequestMapping("/deleteForumArticleAttachmentByFileId")
	public ResponseVO deleteForumArticleAttachmentByFileId(String fileId) {
		forumArticleAttachmentService.deleteForumArticleAttachmentByFileId(fileId);
		return getSuccessResponseVO(null);
	}
}