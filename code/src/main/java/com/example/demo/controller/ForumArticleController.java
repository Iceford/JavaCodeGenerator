package com.example.demo.controller;

import java.util.List;

import com.example.demo.entity.query.ForumArticleQuery;
import com.example.demo.entity.po.ForumArticle;
import com.example.demo.entity.vo.ResponseVO;
import com.example.demo.service.ForumArticleService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 文章信息 Controller
 */
@RestController("forumArticleController")
@RequestMapping("/forumArticle")
public class ForumArticleController extends ABaseController{

	@Resource
	private ForumArticleService forumArticleService;
	/**
	 * 根据条件分页查询
	 */
	@RequestMapping("/loadDataList")
	public ResponseVO loadDataList(ForumArticleQuery query){
		return getSuccessResponseVO(forumArticleService.findListByPage(query));
	}

	/**
	 * 新增
	 */
	@RequestMapping("/add")
	public ResponseVO add(ForumArticle bean) {
		forumArticleService.add(bean);
		return getSuccessResponseVO(null);
	}

	/**
	 * 批量新增
	 */
	@RequestMapping("/addBatch")
	public ResponseVO addBatch(@RequestBody List<ForumArticle> listBean) {
		forumArticleService.addBatch(listBean);
		return getSuccessResponseVO(null);
	}

	/**
	 * 批量新增/修改
	 */
	@RequestMapping("/addOrUpdateBatch")
	public ResponseVO addOrUpdateBatch(@RequestBody List<ForumArticle> listBean) {
		forumArticleService.addBatch(listBean);
		return getSuccessResponseVO(null);
	}

	/**
	 * 根据ArticleId查询对象
	 */
	@RequestMapping("/getForumArticleByArticleId")
	public ResponseVO getForumArticleByArticleId(String articleId) {
		return getSuccessResponseVO(forumArticleService.getForumArticleByArticleId(articleId));
	}

	/**
	 * 根据ArticleId修改对象
	 */
	@RequestMapping("/updateForumArticleByArticleId")
	public ResponseVO updateForumArticleByArticleId(ForumArticle bean,String articleId) {
		forumArticleService.updateForumArticleByArticleId(bean,articleId);
		return getSuccessResponseVO(null);
	}

	/**
	 * 根据ArticleId删除
	 */
	@RequestMapping("/deleteForumArticleByArticleId")
	public ResponseVO deleteForumArticleByArticleId(String articleId) {
		forumArticleService.deleteForumArticleByArticleId(articleId);
		return getSuccessResponseVO(null);
	}
}