package com.example.demo.controller;

import java.util.List;

import com.example.demo.entity.query.UserIntegralRecordQuery;
import com.example.demo.entity.po.UserIntegralRecord;
import com.example.demo.entity.vo.ResponseVO;
import com.example.demo.service.UserIntegralRecordService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 用户积分记录表 Controller
 */
@RestController("userIntegralRecordController")
@RequestMapping("/userIntegralRecord")
public class UserIntegralRecordController extends ABaseController{

	@Resource
	private UserIntegralRecordService userIntegralRecordService;
	/**
	 * 根据条件分页查询
	 */
	@RequestMapping("/loadDataList")
	public ResponseVO loadDataList(UserIntegralRecordQuery query){
		return getSuccessResponseVO(userIntegralRecordService.findListByPage(query));
	}

	/**
	 * 新增
	 */
	@RequestMapping("/add")
	public ResponseVO add(UserIntegralRecord bean) {
		userIntegralRecordService.add(bean);
		return getSuccessResponseVO(null);
	}

	/**
	 * 批量新增
	 */
	@RequestMapping("/addBatch")
	public ResponseVO addBatch(@RequestBody List<UserIntegralRecord> listBean) {
		userIntegralRecordService.addBatch(listBean);
		return getSuccessResponseVO(null);
	}

	/**
	 * 批量新增/修改
	 */
	@RequestMapping("/addOrUpdateBatch")
	public ResponseVO addOrUpdateBatch(@RequestBody List<UserIntegralRecord> listBean) {
		userIntegralRecordService.addBatch(listBean);
		return getSuccessResponseVO(null);
	}

	/**
	 * 根据RecordId查询对象
	 */
	@RequestMapping("/getUserIntegralRecordByRecordId")
	public ResponseVO getUserIntegralRecordByRecordId(Integer recordId) {
		return getSuccessResponseVO(userIntegralRecordService.getUserIntegralRecordByRecordId(recordId));
	}

	/**
	 * 根据RecordId修改对象
	 */
	@RequestMapping("/updateUserIntegralRecordByRecordId")
	public ResponseVO updateUserIntegralRecordByRecordId(UserIntegralRecord bean,Integer recordId) {
		userIntegralRecordService.updateUserIntegralRecordByRecordId(bean,recordId);
		return getSuccessResponseVO(null);
	}

	/**
	 * 根据RecordId删除
	 */
	@RequestMapping("/deleteUserIntegralRecordByRecordId")
	public ResponseVO deleteUserIntegralRecordByRecordId(Integer recordId) {
		userIntegralRecordService.deleteUserIntegralRecordByRecordId(recordId);
		return getSuccessResponseVO(null);
	}
}