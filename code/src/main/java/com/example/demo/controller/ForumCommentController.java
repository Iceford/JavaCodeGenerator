package com.example.demo.controller;

import java.util.List;

import com.example.demo.entity.query.ForumCommentQuery;
import com.example.demo.entity.po.ForumComment;
import com.example.demo.entity.vo.ResponseVO;
import com.example.demo.service.ForumCommentService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 评论 Controller
 */
@RestController("forumCommentController")
@RequestMapping("/forumComment")
public class ForumCommentController extends ABaseController{

	@Resource
	private ForumCommentService forumCommentService;
	/**
	 * 根据条件分页查询
	 */
	@RequestMapping("/loadDataList")
	public ResponseVO loadDataList(ForumCommentQuery query){
		return getSuccessResponseVO(forumCommentService.findListByPage(query));
	}

	/**
	 * 新增
	 */
	@RequestMapping("/add")
	public ResponseVO add(ForumComment bean) {
		forumCommentService.add(bean);
		return getSuccessResponseVO(null);
	}

	/**
	 * 批量新增
	 */
	@RequestMapping("/addBatch")
	public ResponseVO addBatch(@RequestBody List<ForumComment> listBean) {
		forumCommentService.addBatch(listBean);
		return getSuccessResponseVO(null);
	}

	/**
	 * 批量新增/修改
	 */
	@RequestMapping("/addOrUpdateBatch")
	public ResponseVO addOrUpdateBatch(@RequestBody List<ForumComment> listBean) {
		forumCommentService.addBatch(listBean);
		return getSuccessResponseVO(null);
	}

	/**
	 * 根据CommentId查询对象
	 */
	@RequestMapping("/getForumCommentByCommentId")
	public ResponseVO getForumCommentByCommentId(Integer commentId) {
		return getSuccessResponseVO(forumCommentService.getForumCommentByCommentId(commentId));
	}

	/**
	 * 根据CommentId修改对象
	 */
	@RequestMapping("/updateForumCommentByCommentId")
	public ResponseVO updateForumCommentByCommentId(ForumComment bean,Integer commentId) {
		forumCommentService.updateForumCommentByCommentId(bean,commentId);
		return getSuccessResponseVO(null);
	}

	/**
	 * 根据CommentId删除
	 */
	@RequestMapping("/deleteForumCommentByCommentId")
	public ResponseVO deleteForumCommentByCommentId(Integer commentId) {
		forumCommentService.deleteForumCommentByCommentId(commentId);
		return getSuccessResponseVO(null);
	}
}