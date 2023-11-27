package com.example.demo.controller;

import java.util.List;

import com.example.demo.entity.query.SysSettingQuery;
import com.example.demo.entity.po.SysSetting;
import com.example.demo.entity.vo.ResponseVO;
import com.example.demo.service.SysSettingService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 系统设置信息 Controller
 */
@RestController("sysSettingController")
@RequestMapping("/sysSetting")
public class SysSettingController extends ABaseController{

	@Resource
	private SysSettingService sysSettingService;
	/**
	 * 根据条件分页查询
	 */
	@RequestMapping("/loadDataList")
	public ResponseVO loadDataList(SysSettingQuery query){
		return getSuccessResponseVO(sysSettingService.findListByPage(query));
	}

	/**
	 * 新增
	 */
	@RequestMapping("/add")
	public ResponseVO add(SysSetting bean) {
		sysSettingService.add(bean);
		return getSuccessResponseVO(null);
	}

	/**
	 * 批量新增
	 */
	@RequestMapping("/addBatch")
	public ResponseVO addBatch(@RequestBody List<SysSetting> listBean) {
		sysSettingService.addBatch(listBean);
		return getSuccessResponseVO(null);
	}

	/**
	 * 批量新增/修改
	 */
	@RequestMapping("/addOrUpdateBatch")
	public ResponseVO addOrUpdateBatch(@RequestBody List<SysSetting> listBean) {
		sysSettingService.addBatch(listBean);
		return getSuccessResponseVO(null);
	}

	/**
	 * 根据Code查询对象
	 */
	@RequestMapping("/getSysSettingByCode")
	public ResponseVO getSysSettingByCode(String code) {
		return getSuccessResponseVO(sysSettingService.getSysSettingByCode(code));
	}

	/**
	 * 根据Code修改对象
	 */
	@RequestMapping("/updateSysSettingByCode")
	public ResponseVO updateSysSettingByCode(SysSetting bean,String code) {
		sysSettingService.updateSysSettingByCode(bean,code);
		return getSuccessResponseVO(null);
	}

	/**
	 * 根据Code删除
	 */
	@RequestMapping("/deleteSysSettingByCode")
	public ResponseVO deleteSysSettingByCode(String code) {
		sysSettingService.deleteSysSettingByCode(code);
		return getSuccessResponseVO(null);
	}
}