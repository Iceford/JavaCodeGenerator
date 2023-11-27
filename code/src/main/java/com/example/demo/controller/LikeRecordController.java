package com.example.demo.controller;

import java.util.List;

import com.example.demo.entity.query.LikeRecordQuery;
import com.example.demo.entity.po.LikeRecord;
import com.example.demo.entity.vo.ResponseVO;
import com.example.demo.service.LikeRecordService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 点赞记录 Controller
 */
@RestController("likeRecordController")
@RequestMapping("/likeRecord")
public class LikeRecordController extends ABaseController{

	@Resource
	private LikeRecordService likeRecordService;
	/**
	 * 根据条件分页查询
	 */
	@RequestMapping("/loadDataList")
	public ResponseVO loadDataList(LikeRecordQuery query){
		return getSuccessResponseVO(likeRecordService.findListByPage(query));
	}

	/**
	 * 新增
	 */
	@RequestMapping("/add")
	public ResponseVO add(LikeRecord bean) {
		likeRecordService.add(bean);
		return getSuccessResponseVO(null);
	}

	/**
	 * 批量新增
	 */
	@RequestMapping("/addBatch")
	public ResponseVO addBatch(@RequestBody List<LikeRecord> listBean) {
		likeRecordService.addBatch(listBean);
		return getSuccessResponseVO(null);
	}

	/**
	 * 批量新增/修改
	 */
	@RequestMapping("/addOrUpdateBatch")
	public ResponseVO addOrUpdateBatch(@RequestBody List<LikeRecord> listBean) {
		likeRecordService.addBatch(listBean);
		return getSuccessResponseVO(null);
	}

	/**
	 * 根据OpId查询对象
	 */
	@RequestMapping("/getLikeRecordByOpId")
	public ResponseVO getLikeRecordByOpId(Integer opId) {
		return getSuccessResponseVO(likeRecordService.getLikeRecordByOpId(opId));
	}

	/**
	 * 根据OpId修改对象
	 */
	@RequestMapping("/updateLikeRecordByOpId")
	public ResponseVO updateLikeRecordByOpId(LikeRecord bean,Integer opId) {
		likeRecordService.updateLikeRecordByOpId(bean,opId);
		return getSuccessResponseVO(null);
	}

	/**
	 * 根据OpId删除
	 */
	@RequestMapping("/deleteLikeRecordByOpId")
	public ResponseVO deleteLikeRecordByOpId(Integer opId) {
		likeRecordService.deleteLikeRecordByOpId(opId);
		return getSuccessResponseVO(null);
	}

	/**
	 * 根据ObjectIdAndUserIdAndOpType查询对象
	 */
	@RequestMapping("/getLikeRecordByObjectIdAndUserIdAndOpType")
	public ResponseVO getLikeRecordByObjectIdAndUserIdAndOpType(String objectId,String userId,Integer opType) {
		return getSuccessResponseVO(likeRecordService.getLikeRecordByObjectIdAndUserIdAndOpType(objectId,userId,opType));
	}

	/**
	 * 根据ObjectIdAndUserIdAndOpType修改对象
	 */
	@RequestMapping("/updateLikeRecordByObjectIdAndUserIdAndOpType")
	public ResponseVO updateLikeRecordByObjectIdAndUserIdAndOpType(LikeRecord bean,String objectId,String userId,Integer opType) {
		likeRecordService.updateLikeRecordByObjectIdAndUserIdAndOpType(bean,objectId,userId,opType);
		return getSuccessResponseVO(null);
	}

	/**
	 * 根据ObjectIdAndUserIdAndOpType删除
	 */
	@RequestMapping("/deleteLikeRecordByObjectIdAndUserIdAndOpType")
	public ResponseVO deleteLikeRecordByObjectIdAndUserIdAndOpType(String objectId,String userId,Integer opType) {
		likeRecordService.deleteLikeRecordByObjectIdAndUserIdAndOpType(objectId,userId,opType);
		return getSuccessResponseVO(null);
	}
}