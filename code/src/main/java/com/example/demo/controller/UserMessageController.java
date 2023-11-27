package com.example.demo.controller;

import java.util.List;

import com.example.demo.entity.query.UserMessageQuery;
import com.example.demo.entity.po.UserMessage;
import com.example.demo.entity.vo.ResponseVO;
import com.example.demo.service.UserMessageService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 用户消息 Controller
 */
@RestController("userMessageController")
@RequestMapping("/userMessage")
public class UserMessageController extends ABaseController{

	@Resource
	private UserMessageService userMessageService;
	/**
	 * 根据条件分页查询
	 */
	@RequestMapping("/loadDataList")
	public ResponseVO loadDataList(UserMessageQuery query){
		return getSuccessResponseVO(userMessageService.findListByPage(query));
	}

	/**
	 * 新增
	 */
	@RequestMapping("/add")
	public ResponseVO add(UserMessage bean) {
		userMessageService.add(bean);
		return getSuccessResponseVO(null);
	}

	/**
	 * 批量新增
	 */
	@RequestMapping("/addBatch")
	public ResponseVO addBatch(@RequestBody List<UserMessage> listBean) {
		userMessageService.addBatch(listBean);
		return getSuccessResponseVO(null);
	}

	/**
	 * 批量新增/修改
	 */
	@RequestMapping("/addOrUpdateBatch")
	public ResponseVO addOrUpdateBatch(@RequestBody List<UserMessage> listBean) {
		userMessageService.addBatch(listBean);
		return getSuccessResponseVO(null);
	}

	/**
	 * 根据MessageId查询对象
	 */
	@RequestMapping("/getUserMessageByMessageId")
	public ResponseVO getUserMessageByMessageId(Integer messageId) {
		return getSuccessResponseVO(userMessageService.getUserMessageByMessageId(messageId));
	}

	/**
	 * 根据MessageId修改对象
	 */
	@RequestMapping("/updateUserMessageByMessageId")
	public ResponseVO updateUserMessageByMessageId(UserMessage bean,Integer messageId) {
		userMessageService.updateUserMessageByMessageId(bean,messageId);
		return getSuccessResponseVO(null);
	}

	/**
	 * 根据MessageId删除
	 */
	@RequestMapping("/deleteUserMessageByMessageId")
	public ResponseVO deleteUserMessageByMessageId(Integer messageId) {
		userMessageService.deleteUserMessageByMessageId(messageId);
		return getSuccessResponseVO(null);
	}

	/**
	 * 根据ArticleIdAndCommentIdAndSendUserIdAndMessageType查询对象
	 */
	@RequestMapping("/getUserMessageByArticleIdAndCommentIdAndSendUserIdAndMessageType")
	public ResponseVO getUserMessageByArticleIdAndCommentIdAndSendUserIdAndMessageType(String articleId,Integer commentId,String sendUserId,Integer messageType) {
		return getSuccessResponseVO(userMessageService.getUserMessageByArticleIdAndCommentIdAndSendUserIdAndMessageType(articleId,commentId,sendUserId,messageType));
	}

	/**
	 * 根据ArticleIdAndCommentIdAndSendUserIdAndMessageType修改对象
	 */
	@RequestMapping("/updateUserMessageByArticleIdAndCommentIdAndSendUserIdAndMessageType")
	public ResponseVO updateUserMessageByArticleIdAndCommentIdAndSendUserIdAndMessageType(UserMessage bean,String articleId,Integer commentId,String sendUserId,Integer messageType) {
		userMessageService.updateUserMessageByArticleIdAndCommentIdAndSendUserIdAndMessageType(bean,articleId,commentId,sendUserId,messageType);
		return getSuccessResponseVO(null);
	}

	/**
	 * 根据ArticleIdAndCommentIdAndSendUserIdAndMessageType删除
	 */
	@RequestMapping("/deleteUserMessageByArticleIdAndCommentIdAndSendUserIdAndMessageType")
	public ResponseVO deleteUserMessageByArticleIdAndCommentIdAndSendUserIdAndMessageType(String articleId,Integer commentId,String sendUserId,Integer messageType) {
		userMessageService.deleteUserMessageByArticleIdAndCommentIdAndSendUserIdAndMessageType(articleId,commentId,sendUserId,messageType);
		return getSuccessResponseVO(null);
	}
}