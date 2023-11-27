package com.example.demo.controller;

import java.util.List;

import com.example.demo.entity.query.ForumBoardQuery;
import com.example.demo.entity.po.ForumBoard;
import com.example.demo.entity.vo.ResponseVO;
import com.example.demo.service.ForumBoardService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 文章板块信息 Controller
 */
@RestController("forumBoardController")
@RequestMapping("/forumBoard")
public class ForumBoardController extends ABaseController{

	@Resource
	private ForumBoardService forumBoardService;
	/**
	 * 根据条件分页查询
	 */
	@RequestMapping("/loadDataList")
	public ResponseVO loadDataList(ForumBoardQuery query){
		return getSuccessResponseVO(forumBoardService.findListByPage(query));
	}

	/**
	 * 新增
	 */
	@RequestMapping("/add")
	public ResponseVO add(ForumBoard bean) {
		forumBoardService.add(bean);
		return getSuccessResponseVO(null);
	}

	/**
	 * 批量新增
	 */
	@RequestMapping("/addBatch")
	public ResponseVO addBatch(@RequestBody List<ForumBoard> listBean) {
		forumBoardService.addBatch(listBean);
		return getSuccessResponseVO(null);
	}

	/**
	 * 批量新增/修改
	 */
	@RequestMapping("/addOrUpdateBatch")
	public ResponseVO addOrUpdateBatch(@RequestBody List<ForumBoard> listBean) {
		forumBoardService.addBatch(listBean);
		return getSuccessResponseVO(null);
	}

	/**
	 * 根据BoardId查询对象
	 */
	@RequestMapping("/getForumBoardByBoardId")
	public ResponseVO getForumBoardByBoardId(Integer boardId) {
		return getSuccessResponseVO(forumBoardService.getForumBoardByBoardId(boardId));
	}

	/**
	 * 根据BoardId修改对象
	 */
	@RequestMapping("/updateForumBoardByBoardId")
	public ResponseVO updateForumBoardByBoardId(ForumBoard bean,Integer boardId) {
		forumBoardService.updateForumBoardByBoardId(bean,boardId);
		return getSuccessResponseVO(null);
	}

	/**
	 * 根据BoardId删除
	 */
	@RequestMapping("/deleteForumBoardByBoardId")
	public ResponseVO deleteForumBoardByBoardId(Integer boardId) {
		forumBoardService.deleteForumBoardByBoardId(boardId);
		return getSuccessResponseVO(null);
	}
}